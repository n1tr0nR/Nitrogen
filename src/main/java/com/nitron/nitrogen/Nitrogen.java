package com.nitron.nitrogen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.WorldEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Nitrogen implements ModInitializer {
	public static final String MOD_ID = "nitrogen";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final String GIT_URL = "https://raw.githubusercontent.com/n1tr0nR/Data/main/players.json";

	@Override
	public void onInitialize() {
	}

	public static boolean isPlayerSupporter(PlayerEntity player){
		for (PlayerInfo playerInfo : fetchPlayers()){
			if(player.getUuidAsString().equals(playerInfo.uuid)){
				return true;
			}
		}
		return false;
	}

	public static List<PlayerInfo> fetchPlayers() {
		List<PlayerInfo> players = new ArrayList<>();
		try {
			URL url = new URL(GIT_URL);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);

			if (connection.getResponseCode() == 200) {
				InputStreamReader reader = new InputStreamReader(connection.getInputStream());
				JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();

				// check if "players" exists and is an array
				if (jsonObject.has("players") && jsonObject.get("players").isJsonArray()) {
					JsonArray playerArray = jsonObject.getAsJsonArray("players");

					for (var element : playerArray) {
						JsonObject playerObj = element.getAsJsonObject();
						String uuid = playerObj.get("uuid").getAsString();
						String username = playerObj.get("username").getAsString();
						players.add(new PlayerInfo(uuid, username));
					}
				} else {
					LOGGER.error("Error: 'players' field is missing or not an array!");
				}
				reader.close();
			} else {
				LOGGER.error("HTTP Error: " + connection.getResponseCode());
			}
			connection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return players;
	}

	public static class PlayerInfo {
		public final String uuid;
		public final String username;
		public PlayerInfo(String uuid, String username) {
			this.uuid = uuid;
			this.username = username;
		}
	}
}