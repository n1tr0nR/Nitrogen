package com.nitron.nitrogen.mixin;

import com.nitron.nitrogen.util.ColorableItem;
import com.nitron.nitrogen.util.client.NitrogenContext;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen implements ScreenHandlerProvider<T> {
    @Shadow @Nullable protected Slot focusedSlot;

    @Shadow @Final protected T handler;

    @Shadow protected abstract List<Text> getTooltipFromItem(ItemStack stack);

    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "drawMouseoverTooltip", at = @At("HEAD"), cancellable = true)
    private void ordnance$addBeltTooltipForItem(DrawContext context, int x, int y, CallbackInfo ci) {
        NitrogenContext nitrogenContext = new NitrogenContext(
                context.getScaledWindowWidth(),
                context.getScaledWindowHeight(),
                context.getMatrices(),
                context,
                context.getVertexConsumers());
        if (handler.getCursorStack().isEmpty() && this.focusedSlot != null && this.focusedSlot.hasStack()) {
            ItemStack itemStack = this.focusedSlot.getStack();
            if (itemStack.getItem() instanceof ColorableItem){
                ci.cancel();
                nitrogenContext.drawTooltip(this.textRenderer, this.getTooltipFromItem(itemStack), itemStack.getTooltipData(), x, y, itemStack);
            }
        }
    }
}
