package com.nitron.nitrogen;
import com.nitron.nitrogen.util.SupporterUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class NitrogenClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && SupporterUtils.CRASH_CONTROL && !SupporterUtils.isPlayerSupporter(client.player)){
                throw new RuntimeException("This is a Supporter only mod! Sorry!");
            }
        });
    }
}
