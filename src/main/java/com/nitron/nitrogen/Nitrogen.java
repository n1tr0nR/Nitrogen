package com.nitron.nitrogen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.nitron.nitrogen.config.Config;
import com.nitron.nitrogen.mixin.PlayerEntityMixin;
import com.nitron.nitrogen.util.ScreenShaker;
import com.nitron.nitrogen.util.SupporterUtils;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.TypedActionResult;
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

	public static final TrackedData<Integer> SCREENSHAKE_DURATION = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static final TrackedData<Float> SCREENSHAKE_INTENSITY = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register(((commandDispatcher, commandRegistryAccess, registrationEnvironment) -> {
			commandDispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("nitrogen")
					.then(CommandManager.literal("isSupporter")
							.then(CommandManager.argument("player", EntityArgumentType.player())
									.executes(commandContext -> {
										if(commandContext.getSource().getPlayer() != null){
											commandContext.getSource().getPlayer().sendMessage(
													Text.literal("Player " +
															EntityArgumentType.getPlayer(commandContext, "player").getDisplayName().getString()
															+ (SupporterUtils.isPlayerSupporter(EntityArgumentType.getPlayer(commandContext, "player"))
															? " is a supporter!" : " is not a supporter!"))
											);
										}
										return Command.SINGLE_SUCCESS;
									}))));
		}));
		MidnightConfig.init(MOD_ID, Config.class);
	}
}