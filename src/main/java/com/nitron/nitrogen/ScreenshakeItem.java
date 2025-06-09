package com.nitron.nitrogen;

import com.nitron.nitrogen.util.interfaces.ColorableItem;
import com.nitron.nitrogen.util.interfaces.ScreenShaker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ScreenshakeItem extends Item implements ColorableItem {
    public ScreenshakeItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (user instanceof ScreenShaker screenShaker) {
            screenShaker.addScreenShake(3, 40);
        }
        return super.use(world, user, hand);
    }

    @Override
    public int startColor(ItemStack stack) {
        return 0xff0000;
    }

    @Override
    public int endColor(ItemStack stack) {
        return 0x720000;
    }

    @Override
    public int backgroundColor(ItemStack stack) {
        return 0x380500;
    }
}
