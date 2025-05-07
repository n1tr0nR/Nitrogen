package com.nitron.nitrogen.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nitron.nitrogen.Nitrogen;
import com.nitron.nitrogen.util.interfaces.PlayerInfo;
import net.minecraft.entity.player.PlayerEntity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SupporterUtils {
    private static List<PlayerInfo> cachedPlayers = new ArrayList<>();
    private static long lastFetchTime = 0;
    private static final long CACHE_DURATION = 5 * 60 * 1000;
    public static boolean CRASH_CONTROL = false;

    public static List<PlayerInfo> fetchPlayers() {
        long now = System.currentTimeMillis();
        if (!cachedPlayers.isEmpty() && (now - lastFetchTime < CACHE_DURATION)) {
            return cachedPlayers;
        }

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

                if (jsonObject.has("players") && jsonObject.get("players").isJsonArray()) {
                    JsonArray playerArray = jsonObject.getAsJsonArray("players");
                    for (var element : playerArray) {
                        JsonObject playerObj = element.getAsJsonObject();
                        String uuid = playerObj.get("uuid").getAsString();
                        String username = playerObj.get("username").getAsString();
                        players.add(new PlayerInfo(uuid, username));
                    }

                    cachedPlayers = players;  // update cache
                    lastFetchTime = now;      // update timestamp
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

        return cachedPlayers; // return old cache if error occurs
    }

    public static boolean isPlayerSupporter(PlayerEntity player){
        for (PlayerInfo playerInfo : fetchPlayers()){
            if(player.getUuidAsString().equals(playerInfo.uuid())){
                return true;
            }
        }
        return false;
    }
}
