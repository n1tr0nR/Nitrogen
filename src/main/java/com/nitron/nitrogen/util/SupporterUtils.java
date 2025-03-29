package com.nitron.nitrogen.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nitron.nitrogen.Nitrogen;
import net.minecraft.entity.player.PlayerEntity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SupporterUtils {
    public static List<PlayerInfo> fetchPlayers() {
        List<PlayerInfo> players = new ArrayList<>();
        try {
            URL url = new URL("https://raw.githubusercontent.com/n1tr0nR/Data/main/players.json");
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
                    Nitrogen.LOGGER.error("Error: 'players' field is missing or not an array!");
                }
                reader.close();
            } else {
                Nitrogen.LOGGER.error("HTTP Error: " + connection.getResponseCode());
            }
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return players;
    }

    public static boolean isPlayerSupporter(PlayerEntity player){
        for (PlayerInfo playerInfo : fetchPlayers()){
            if(player.getUuidAsString().equals(playerInfo.uuid())){
                return true;
            }
        }
        return false;
    }

    public static List<PlayerInfo> list = SupporterUtils.fetchPlayers();
}
