package com.nitron.nitrogen.mixin;

import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nitron.nitrogen.util.interfaces.StatusEffectBackground;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collection;
import java.util.List;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow @Final private static Identifier EFFECT_BACKGROUND_AMBIENT_TEXTURE;

    @Shadow @Final private static Identifier EFFECT_BACKGROUND_TEXTURE;


    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
    private void renderStatusEffectOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        Collection<StatusEffectInstance> collection = this.client.player.getStatusEffects();
        if (!collection.isEmpty()) {
            Screen j = this.client.currentScreen;
            if (j instanceof AbstractInventoryScreen) {
                AbstractInventoryScreen abstractInventoryScreen = (AbstractInventoryScreen)j;
                if (abstractInventoryScreen.hideStatusEffectHud()) {
                    return;
                }
            }

            RenderSystem.enableBlend();
            int i = 0;
            int i1 = 0;
            StatusEffectSpriteManager statusEffectSpriteManager = this.client.getStatusEffectSpriteManager();
            List<Runnable> list = Lists.newArrayListWithExpectedSize(collection.size());

            for(StatusEffectInstance statusEffectInstance : Ordering.natural().reverse().sortedCopy(collection)) {
                RegistryEntry<StatusEffect> registryEntry = statusEffectInstance.getEffectType();
                if (statusEffectInstance.shouldShowIcon()) {
                    int k = context.getScaledWindowWidth();
                    int l = 1;
                    if (this.client.isDemo()) {
                        l += 15;
                    }

                    if (((StatusEffect)registryEntry.value()).isBeneficial()) {
                        ++i;
                        k -= 25 * i;
                    } else {
                        ++i1;
                        k -= 25 * i1;
                        l += 26;
                    }

                    float f;
                    if (statusEffectInstance.isAmbient()) {
                        f = 1.0F;
                        context.drawGuiTexture(EFFECT_BACKGROUND_AMBIENT_TEXTURE, k, l, 24, 24);
                    } else {
                        context.drawGuiTexture(statusEffectInstance.getEffectType().value() instanceof StatusEffectBackground bg ? bg.hudSprite() : EFFECT_BACKGROUND_TEXTURE, k, l, 24, 24);
                        if (statusEffectInstance.isDurationBelow(200)) {
                            int m = statusEffectInstance.getDuration();
                            int n = 10 - m / 20;
                            f = MathHelper.clamp((float)m / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + MathHelper.cos((float)m * (float)Math.PI / 5.0F) * MathHelper.clamp((float)n / 10.0F * 0.25F, 0.0F, 0.25F);
                        } else {
                            f = 1.0F;
                        }
                    }

                    Sprite sprite = statusEffectSpriteManager.getSprite(registryEntry);
                    int finalK = k;
                    int finalL = l;
                    list.add((Runnable)() -> {
                        context.setShaderColor(1.0F, 1.0F, 1.0F, f);
                        context.drawSprite(finalK + 3, finalL + 3, 0, 18, 18, sprite);
                        context.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    });
                }
            }

            list.forEach(Runnable::run);
            RenderSystem.disableBlend();
        }
        ci.cancel();
    }
}
