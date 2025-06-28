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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class SupporterUtils {
    private List<PlayerInfo> cachedPlayers = new ArrayList<>();
    private long lastFetchTime = 0;

    protected SupporterUtils(){
    }


    public List<PlayerInfo> fetchPlayers() {
        long now = System.currentTimeMillis();
        long CACHE_DURATION = 5 * 60 * 1000;
        if (!cachedPlayers.isEmpty() && (now - lastFetchTime < CACHE_DURATION)) {
            return cachedPlayers;
        }

        List<PlayerInfo> players = new ArrayList<>();
        try {
            HttpURLConnection connection = (HttpURLConnection) getURL().openConnection();
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
                    Nitrogen.LOGGER.error("Error: 'players' field is missing or not an array for " + getId());
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

    protected abstract String getId();

    protected abstract URL getURL() throws MalformedURLException;

    protected abstract boolean requiredSupporter();

    public boolean isPlayerSupporter(PlayerEntity player){
        for (PlayerInfo playerInfo : this.fetchPlayers()){
            if(player.getUuidAsString().equals(playerInfo.uuid())){
                return true;
            }
        }
        return false;
    }
}
