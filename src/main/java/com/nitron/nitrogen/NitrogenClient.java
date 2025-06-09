package com.nitron.nitrogen;
import com.nitron.nitrogen.config.Config;
import com.nitron.nitrogen.render.RenderUtils;
import com.nitron.nitrogen.util.SupporterUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.SpectralArrowEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.*;

public class NitrogenClient implements ClientModInitializer {
    private static final Map<UUID, Deque<Vec3d>> TRAILS = new HashMap<>();

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && SupporterUtils.CRASH_CONTROL && !SupporterUtils.isPlayerSupporter(client.player)){
                throw new RuntimeException("This is a Supporter only mod! Sorry!");
            }
        });



        WorldRenderEvents.AFTER_TRANSLUCENT.register(context -> {
            MinecraftClient client = MinecraftClient.getInstance();
            Vec3d pos = client.player.getPos();
            Box box = new Box(pos.x - 1, pos.y - 1, pos.z - 1, pos.x + 1, pos.y + 1, pos.z + 1);
            box = box.expand(40);

            if (Config.trailRenderers){
                for (AllayEntity entity : client.world.getEntitiesByClass(AllayEntity.class, box, allayEntity -> true)){
                    Deque<Vec3d> trail = TRAILS.computeIfAbsent(entity.getUuid(), id -> new ArrayDeque<>());
                    Vector3f color = new Vector3f((float) 95 / 255, (float) 205 / 255, (float) 228 / 255);

                    if (entity.isRegionUnloaded()){
                        TRAILS.remove(entity.getUuid());
                    }

                    RenderUtils.renderEntityTrail(
                            context.matrixStack(),
                            context.consumers().getBuffer(RenderLayer.getEntitySolid(Identifier.of(Nitrogen.MOD_ID, "textures/render/color.png"))),
                            context.camera(),
                            entity,
                            context.tickCounter().getTickProgress(false),
                            trail,
                            200,
                            0.1f,
                            0.001f,
                            255,
                            0,
                            (float) color.x,
                            (float) color.y,
                            (float) color.z,
                            new Vec3d(0.0, 0.25, 0.0)
                    );
                }
                for (ArrowEntity entity : client.world.getEntitiesByClass(ArrowEntity.class, box, arrowEntity -> true)){
                    Deque<Vec3d> trail = TRAILS.computeIfAbsent(entity.getUuid(), id -> new ArrayDeque<>());
                    Vector3f color = new Vector3f((float) 1, (float) 1, (float) 1);

                    if (entity.isRegionUnloaded()){
                        TRAILS.remove(entity.getUuid());
                    }

                    RenderUtils.renderEntityTrail(
                            context.matrixStack(),
                            context.consumers().getBuffer(RenderLayer.getEntityTranslucentEmissive(Identifier.of(Nitrogen.MOD_ID, "textures/render/color.png"))),
                            context.camera(),
                            entity,
                            context.tickCounter().getTickProgress(false),
                            trail,
                            50,
                            0.1f,
                            0.001f,
                            255,
                            0,
                            (float) color.x,
                            (float) color.y,
                            (float) color.z,
                            new Vec3d(0.0, 0, 0.0)
                    );
                }
                for (FireworkRocketEntity entity : client.world.getEntitiesByClass(FireworkRocketEntity.class, box, fireworkRocketEntity -> true)){
                    Deque<Vec3d> trail = TRAILS.computeIfAbsent(entity.getUuid(), id -> new ArrayDeque<>());
                    Vector3f color = new Vector3f((float) 1, (float) 1, (float) 1);

                    if (entity.isRegionUnloaded()){
                        TRAILS.remove(entity.getUuid());
                    }

                    RenderUtils.renderEntityTrail(
                            context.matrixStack(),
                            context.consumers().getBuffer(RenderLayer.getEntitySolid(Identifier.of(Nitrogen.MOD_ID, "textures/render/color.png"))),
                            context.camera(),
                            entity,
                            context.tickCounter().getTickProgress(false),
                            trail,
                            50,
                            0.05f,
                            0.001f,
                            255,
                            0,
                            (float) color.x,
                            (float) color.y,
                            (float) color.z,
                            new Vec3d(0.0, 0, 0.0)
                    );
                }
                for (SpectralArrowEntity entity : client.world.getEntitiesByClass(SpectralArrowEntity.class, box, arrowEntity -> true)){
                    Deque<Vec3d> trail = TRAILS.computeIfAbsent(entity.getUuid(), id -> new ArrayDeque<>());
                    Vector3f color = new Vector3f((float) 255 / 255, (float) 248 / 255, (float) 93 / 255);

                    if (entity.isRegionUnloaded()){
                        TRAILS.remove(entity.getUuid());
                    }

                    RenderUtils.renderEntityTrail(
                            context.matrixStack(),
                            context.consumers().getBuffer(RenderLayer.getEntityTranslucentEmissive(Identifier.of(Nitrogen.MOD_ID, "textures/render/color.png"))),
                            context.camera(),
                            entity,
                            context.tickCounter().getTickProgress(false),
                            trail,
                            50,
                            0.1f,
                            0.001f,
                            255,
                            0,
                            color.x,
                            color.y,
                            color.z,
                            new Vec3d(0.0, 0, 0.0)
                    );
                }
                for (PlayerEntity entity : client.world.getEntitiesByClass(PlayerEntity.class, box, arrowEntity -> true)){
                    Deque<Vec3d> trail = TRAILS.computeIfAbsent(entity.getUuid(), id -> new ArrayDeque<>());
                    Vector3f color = new Vector3f((float) 255 / 255, (float) 255 / 255, (float) 255 / 255);

                    if (entity.isGliding()){
                        RenderUtils.renderEntityTrail(
                                context.matrixStack(),
                                context.consumers().getBuffer(RenderLayer.getEntityTranslucentEmissive(Identifier.of(Nitrogen.MOD_ID, "textures/render/color.png"))),
                                context.camera(),
                                entity,
                                context.tickCounter().getTickProgress(false),
                                trail,
                                50,
                                0.2f,
                                0.001f,
                                255,
                                0,
                                (float) color.x,
                                (float) color.y,
                                (float) color.z,
                                new Vec3d(0.0, -0.2F, 0.0)
                        );
                    } else {
                        TRAILS.remove(entity.getUuid());
                    }
                }
            }
        });
    }
}
