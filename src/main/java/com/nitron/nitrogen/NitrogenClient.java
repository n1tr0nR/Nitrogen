package com.nitron.nitrogen;

import com.nitron.nitrogen.util.SupporterUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.minecraft.entity.player.PlayerEntity;

public class NitrogenClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientEntityEvents.ENTITY_LOAD.register(((entity, clientWorld) -> {
            if(entity instanceof PlayerEntity){
                SupporterUtils.list = SupporterUtils.fetchPlayers();
            }
        }));
    }
}
