package com.nitron.nitrogen.mixin;

import com.nitron.nitrogen.Nitrogen;
import com.nitron.nitrogen.util.interfaces.ScreenShaker;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements ScreenShaker {
    @Shadow public abstract int getDefaultPortalCooldown();

    @Shadow public abstract void sendMessage(Text message, boolean overlay);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void nitrogen$tick(CallbackInfo ci) {
        if (getScreenShakeDuration() > 0){
            setScreenShakeDuration(getScreenShakeDuration() - 1);
        }
        if (getScreenShakeDuration() <= 0 && getScreenShakeIntensity() > 0) {
            setScreenShakeIntensity(this.lerpToZero(getScreenShakeIntensity()));
        }
    }

    @Unique
    public float lerpToZero(float value) {
        float easingFactor = 0.1f;
        return value - (value * easingFactor);
    }

    @Override
    public void setScreenShakeDuration(int duration) {
        dataTracker.set(Nitrogen.SCREENSHAKE_DURATION, duration);
    }

    @Override
    public void setScreenShakeIntensity(float intensity) {
        float value = Math.clamp(intensity, 0, 10);
        if(value <= 0.01F){
            value = 0;
        }
        dataTracker.set(Nitrogen.SCREENSHAKE_INTENSITY, value);
    }

    @Override
    public int getScreenShakeDuration() {
        return dataTracker.get(Nitrogen.SCREENSHAKE_DURATION);
    }

    @Override
    public float getScreenShakeIntensity() {
        return dataTracker.get(Nitrogen.SCREENSHAKE_INTENSITY);
    }

    @Inject(method = "initDataTracker", at = @At("HEAD"))
    public void nitrogen$initDataTracker(DataTracker.Builder builder, CallbackInfo ci){
        builder.add(Nitrogen.SCREENSHAKE_INTENSITY, 0.0F);
        builder.add(Nitrogen.SCREENSHAKE_DURATION, 0);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void nitrogen$readNBT(NbtCompound nbt, CallbackInfo ci){
        nbt.putInt("ScreenShakeDuration", getScreenShakeDuration());
        nbt.putFloat("ScreenShakeIntensity", getScreenShakeIntensity());
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void nitrogen$writeNBT(NbtCompound nbt, CallbackInfo ci){
        setScreenShakeDuration(nbt.getInt("ScreenShakeDuration").isPresent() ? nbt.getInt("ScreenShakeDuration").get() : 0);
        setScreenShakeIntensity(nbt.getFloat("ScreenShakeIntensity").isPresent() ? nbt.getFloat("ScreenShakeIntensity").get() : 0);
    }

    @Unique
    public void startScreenshake(float intensity, int duration){
        setScreenShakeIntensity(intensity);
        setScreenShakeDuration(duration);
    }
}
