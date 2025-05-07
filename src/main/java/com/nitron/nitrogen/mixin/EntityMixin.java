package com.nitron.nitrogen.mixin;


import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.nitron.nitrogen.Nitrogen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(Entity.class)
public class EntityMixin<E extends VoxelShape> {
    /*@ModifyReturnValue(method = "findCollisionsForMovement", at = @At("RETURN"))
    private static List<VoxelShape> onMove(List<VoxelShape> original, @Nullable Entity entity, World world, List<VoxelShape> regularCollisions, Box movingEntityBoundingBox) {
        ImmutableList.Builder<VoxelShape> builder = ImmutableList.builderWithExpectedSize(regularCollisions.size() + 1);
        if (!regularCollisions.isEmpty()) {
            builder.addAll(regularCollisions);
        }

        WorldBorder worldBorder = world.getWorldBorder();
        boolean bl = entity != null && worldBorder.canCollide(entity, movingEntityBoundingBox);
        if (bl) {
            builder.add(worldBorder.asVoxelShape());
        }
        Box cubeBoundingBox = getBox();
        if (entity != null && cubeBoundingBox.contains(entity.getPos())) {
            if (entity instanceof PlayerEntity player) {
                if(!player.getMainHandStack().isOf(Items.NETHERITE_INGOT)){
                    builder.add(Nitrogen.boxToVoxelShape(cubeBoundingBox, true));
                }
            } else {
                builder.add(Nitrogen.boxToVoxelShape(cubeBoundingBox, true));
            }
        }

        builder.addAll(world.getBlockCollisions(entity, movingEntityBoundingBox));
        return builder.build();
    }

    @Unique
    private static @NotNull Box getBox() {
        BlockPos cubePosition = Nitrogen.TESTING_POSITION;
        float inflation = 70f;
        float x0 = cubePosition.getX() + 0.5f - inflation;
        float x1 = cubePosition.getX() + 0.5f + inflation;
        float y0 = cubePosition.getY() + 0.5f - inflation;
        float y1 = cubePosition.getY() + 0.5f + inflation;
        float z0 = cubePosition.getZ() + 0.5f - inflation;
        float z1 = cubePosition.getZ() + 0.5f + inflation;
        return new Box(x0, y0, z0, x1, y1, z1);
    }*/
}
