package com.nitron.nitrogen.mixin;

import com.nitron.nitrogen.config.Config;
import com.nitron.nitrogen.util.interfaces.ScreenShaker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow protected abstract void setRotation(float yaw, float pitch);

    @Shadow private float yaw;

    @Shadow private float pitch;

    @Inject(method = "update", at = @At("TAIL"))
    private void nitrogen$update(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci){
        float y = this.yaw;
        float p = this.pitch;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if(player != null){
            if (player instanceof ScreenShaker shaker){
                float mult = Config.mult;
                float swayFactor = 0.5F;
                float intensity = shaker.getScreenShakeIntensity();
                if(intensity > 0.01F && Config.screenshake){
                    float shakeSwayYaw = (float) ((Math.random() * 2 - 1) * mult * swayFactor * intensity);
                    float shakeSwayPitch = (float) ((Math.random() * 2 - 1) * mult * swayFactor * intensity);

                    this.setRotation(y + shakeSwayYaw, p + shakeSwayPitch);
                }
            }
        }
    }
}
