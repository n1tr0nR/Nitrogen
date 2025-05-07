package com.nitron.nitrogen.util.interfaces;

import net.minecraft.item.ItemStack;

public interface ColorableItem {
    int startColor(ItemStack stack);
    int endColor(ItemStack stack);
    int backgroundColor(ItemStack stack);
}
