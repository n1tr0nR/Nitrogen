package com.nitron.nitrogen.util.client;

import com.nitron.nitrogen.util.ColorableItem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.text.Text;
import net.minecraft.util.Util;
import org.joml.Vector2ic;

import java.util.List;
import java.util.Optional;

public class NitrogenContext {
    final int scaledWindowWidth;
    final int scaledWindowHeight;
    final MatrixStack matrices;
    final DrawContext context;
    final VertexConsumerProvider.Immediate vertexConsumers;

    public NitrogenContext(int width, int height, MatrixStack matrices, DrawContext context, VertexConsumerProvider.Immediate vertexConsumers){
        this.scaledWindowWidth = width;
        this.scaledWindowHeight = height;
        this.matrices = matrices;
        this.context = context;
        this.vertexConsumers = vertexConsumers;
    }

    public void drawTooltip(TextRenderer textRenderer, List<Text> text, Optional<TooltipData> data, int x, int y, ItemStack stack) {
        List<TooltipComponent> list = (List)text.stream().map(Text::asOrderedText).map(TooltipComponent::of).collect(Util.toArrayList());
        data.ifPresent((datax) -> list.add(list.isEmpty() ? 0 : 1, TooltipComponent.of(datax)));
        this.drawTooltip(textRenderer, list, x, y, HoveredTooltipPositioner.INSTANCE, stack);
    }

    public void drawTooltipFree(TextRenderer textRenderer, List<Text> text, Optional<TooltipData> data, int x, int y, ItemStack stack) {
        List<TooltipComponent> list = (List)text.stream().map(Text::asOrderedText).map(TooltipComponent::of).collect(Util.toArrayList());
        data.ifPresent((datax) -> list.add(list.isEmpty() ? 0 : 1, TooltipComponent.of(datax)));
        this.drawTooltipFree(textRenderer, list, x, y, HoveredTooltipPositioner.INSTANCE, stack);
    }

    private void drawTooltip(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, ItemStack stack) {
        if (!components.isEmpty()) {
            int i = 0;
            int j = components.size() == 1 ? -2 : 0;

            for(TooltipComponent tooltipComponent : components) {
                int k = tooltipComponent.getWidth(textRenderer);
                if (k > i) {
                    i = k;
                }

                j += tooltipComponent.getHeight();
            }

            Vector2ic vector2ic = positioner.getPosition(scaledWindowWidth, scaledWindowHeight, x, y, i, j);
            int n = vector2ic.x();
            int o = vector2ic.y();
            this.matrices.push();
            int p = 400;
            int finalI = i;
            int finalJ = j;
            context.draw(() -> render(context, n, o, finalI, finalJ, 400, stack));
            this.matrices.translate(0.0F, 0.0F, 400.0F);
            int q = o;

            for(int r = 0; r < components.size(); ++r) {
                TooltipComponent tooltipComponent2 = (TooltipComponent)components.get(r);
                tooltipComponent2.drawText(textRenderer, n, q, this.matrices.peek().getPositionMatrix(), this.vertexConsumers);
                q += tooltipComponent2.getHeight() + (r == 0 ? 2 : 0);
            }

            q = o;

            for(int r = 0; r < components.size(); ++r) {
                TooltipComponent tooltipComponent2 = (TooltipComponent)components.get(r);
                tooltipComponent2.drawItems(textRenderer, n, q, context);
                q += tooltipComponent2.getHeight() + (r == 0 ? 2 : 0);
            }

            this.matrices.pop();
        }
    }

    private void drawTooltipFree(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, ItemStack stack) {
        if (!components.isEmpty()) {
            int i = 0;
            int j = components.size() == 1 ? -2 : 0;

            for(TooltipComponent tooltipComponent : components) {
                int k = tooltipComponent.getWidth(textRenderer);
                if (k > i) {
                    i = k;
                }

                j += tooltipComponent.getHeight();
            }

            Vector2ic vector2ic = positioner.getPosition(scaledWindowWidth, scaledWindowHeight, x, y, i, j);
            int n = x + 12;
            int o = vector2ic.y();
            this.matrices.push();
            int p = 400;
            int finalI = i;
            int finalJ = j;
            context.draw(() -> render(context, n, o, finalI, finalJ, 400, stack));
            this.matrices.translate(0.0F, 0.0F, 400.0F);
            int q = o;

            for(int r = 0; r < components.size(); ++r) {
                TooltipComponent tooltipComponent2 = (TooltipComponent)components.get(r);
                tooltipComponent2.drawText(textRenderer, n, q, this.matrices.peek().getPositionMatrix(), this.vertexConsumers);
                q += tooltipComponent2.getHeight() + (r == 0 ? 2 : 0);
            }

            q = o;

            for(int r = 0; r < components.size(); ++r) {
                TooltipComponent tooltipComponent2 = (TooltipComponent)components.get(r);
                tooltipComponent2.drawItems(textRenderer, n, q, context);
                q += tooltipComponent2.getHeight() + (r == 0 ? 2 : 0);
            }

            this.matrices.pop();
        }
    }

    public static void render(DrawContext context, int x, int y, int width, int height, int z, ItemStack stack) {
        int i = x - 3;
        int j = y - 3;
        int k = width + 3 + 3;
        int l = height + 3 + 3;
        if (stack.getItem() instanceof ColorableItem colorableItem){
            int startColor = colorableItem.startColor();
            int endColor = colorableItem.endColor();
            int bgColor = colorableItem.backgroundColor();

            renderHorizontalLine(context, i, j - 1, k, z, bgColor);
            renderHorizontalLine(context, i, j + l, k, z, bgColor);
            renderRectangle(context, i, j, k, l, z, bgColor);
            renderVerticalLine(context, i - 1, j, l, z, bgColor);
            renderVerticalLine(context, i + k, j, l, z, bgColor);
            renderBorder(context, i, j + 1, k, l, z, startColor, endColor);
        }
    }

    private static void renderBorder(DrawContext context, int x, int y, int width, int height, int z, int startColor, int endColor) {
        renderVerticalLine(context, x, y, height - 2, z, startColor, endColor);
        renderVerticalLine(context, x + width - 1, y, height - 2, z, startColor, endColor);
        renderHorizontalLine(context, x, y - 1, width, z, startColor);
        renderHorizontalLine(context, x, y - 1 + height - 1, width, z, endColor);
    }

    private static void renderVerticalLine(DrawContext context, int x, int y, int height, int z, int color) {
        context.fill(x, y, x + 1, y + height, z, color);
    }

    private static void renderVerticalLine(DrawContext context, int x, int y, int height, int z, int startColor, int endColor) {
        context.fillGradient(x, y, x + 1, y + height, z, startColor, endColor);
    }

    private static void renderHorizontalLine(DrawContext context, int x, int y, int width, int z, int color) {
        context.fill(x, y, x + width, y + 1, z, color);
    }

    private static void renderRectangle(DrawContext context, int x, int y, int width, int height, int z, int color) {
        context.fill(x, y, x + width, y + height, z, color);
    }
}
