package com.nitron.nitrogen;

import com.nitron.nitrogen.config.Config;
import com.nitron.nitrogen.util.interfaces.NitrogenInit;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Nitrogen implements ModInitializer {
	public static final String MOD_ID = "nitrogen";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static List<String> data = new ArrayList<>();

	public static final TrackedData<Integer> SCREENSHAKE_DURATION = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static final TrackedData<Float> SCREENSHAKE_INTENSITY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);

	@Override
	public void onInitialize() {
		//new Thread(SupporterUtils::fetchPlayers).start();

		MidnightConfig.init(MOD_ID, Config.class);

		var entries = FabricLoader.getInstance().getEntrypoints(
				"nitrogen", NitrogenInit.class
		);

		for (NitrogenInit entry : entries){
			String links = entry.getLinks();
			data.add(links);
		}
	}
}