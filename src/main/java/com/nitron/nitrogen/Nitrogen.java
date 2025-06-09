package com.nitron.nitrogen;

import com.nitron.nitrogen.config.Config;
import com.nitron.nitrogen.util.SupporterUtils;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Nitrogen implements ModInitializer {
	public static final String MOD_ID = "nitrogen";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final TrackedData<Integer> SCREENSHAKE_DURATION = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static final TrackedData<Float> SCREENSHAKE_INTENSITY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);

	public static final Item SCREENSHAKE = registerItem("screenshake", new ScreenshakeItem(new Item.Settings()
			.registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, "screenshake")))
			.maxCount(64)
	));

	private static Item registerItem(String name, Item item) {
		return Registry.register(Registries.ITEM, Identifier.of(MOD_ID, name), item);
	}

	@Override
	public void onInitialize() {
		new Thread(SupporterUtils::fetchPlayers).start();
		MidnightConfig.init(MOD_ID, Config.class);
	}
}