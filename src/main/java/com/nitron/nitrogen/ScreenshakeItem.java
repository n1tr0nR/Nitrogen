package com.nitron.nitrogen;

import com.nitron.nitrogen.util.interfaces.ScreenShaker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ScreenshakeItem extends Item {
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
}
