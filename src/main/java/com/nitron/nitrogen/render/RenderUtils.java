package com.nitron.nitrogen.render;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class RenderUtils {
    /**
     * Renders a solid, double-sided cube centered at the given BlockPos.
     *
     * @param matrices  The MatrixStack
     * @param vertices  The VertexConsumer
     * @param color     RGBA color as 0xAARRGGBB
     * @param center    BlockPos to center the cube
     * @param inflation Half-size of the cube; 0.5 = normal block size
     */
    public static void renderSolidColorCube(MatrixStack matrices, VertexConsumer vertices, int color, BlockPos center, float inflation) {
        MatrixStack.Entry entry = matrices.peek();
        float x0 = center.getX() + 0.5f - inflation; float x1 = center.getX() + 0.5f + inflation; float y0 = center.getY() + 0.5f - inflation; float y1 = center.getY() + 0.5f + inflation; float z0 = center.getZ() + 0.5f - inflation; float z1 = center.getZ() + 0.5f + inflation;
        renderQuad(entry, vertices, color, x0, y0, z1, x1, y0, z1, x1, y1, z1, x0, y1, z1);
        renderQuad(entry, vertices, color, x1, y0, z0, x0, y0, z0, x0, y1, z0, x1, y1, z0);
        renderQuad(entry, vertices, color, x0, y0, z0, x0, y0, z1, x0, y1, z1, x0, y1, z0);
        renderQuad(entry, vertices, color, x1, y0, z1, x1, y0, z0, x1, y1, z0, x1, y1, z1);
        renderQuad(entry, vertices, color, x0, y1, z1, x1, y1, z1, x1, y1, z0, x0, y1, z0);
        renderQuad(entry, vertices, color, x0, y0, z0, x1, y0, z0, x1, y0, z1, x0, y0, z1);
    }

    private static void renderQuad(MatrixStack.Entry matrix, VertexConsumer vertices, int color, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4) {
        renderVertex(matrix, vertices, color, x1, y1, z1);
        renderVertex(matrix, vertices, color, x2, y2, z2);
        renderVertex(matrix, vertices, color, x3, y3, z3);
        renderVertex(matrix, vertices, color, x4, y4, z4);
    }

    private static void renderVertex(MatrixStack.Entry matrix, VertexConsumer vertices, int color, float x, float y, float z) {
        int a = (color >> 24) & 0xFF;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        vertices.vertex(matrix, x, y, z).color(r, g, b, a).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(0xF000F0).normal(matrix, 0, 1, 0);
    }

    /**
     * Renders a solid, double-sided cube centered at the given BlockPos.
     *
     * @param matrices  The MatrixStack
     * @param vertices  The VertexConsumer
     * @param center    BlockPos to center the cube
     * @param inflation Half-size of the cube; 0.5 = normal block size
     * @param timeOffset Moves the textures UV map
     * @param tileSize Changes the tiling of the texture
     */
    public static void renderTexturedCube(MatrixStack matrices, VertexConsumer vertices, BlockPos center, float inflation, Vec2f timeOffset, float tileSize) {
        MatrixStack.Entry entry = matrices.peek();
        float x0 = center.getX() + 0.5f - inflation;
        float x1 = center.getX() + 0.5f + inflation;
        float y0 = center.getY() + 0.5f - inflation;
        float y1 = center.getY() + 0.5f + inflation;
        float z0 = center.getZ() + 0.5f - inflation;
        float z1 = center.getZ() + 0.5f + inflation;

        float u0 = 0.0f, u1 = 1.0f;
        float v0 = 0.0f, v1 = 1.0f;

        u0 *= tileSize;
        u1 *= tileSize;
        v0 *= tileSize;
        v1 *= tileSize;

        float textureOffsetX = timeOffset.x;
        v0 += textureOffsetX;
        v1 += textureOffsetX;

        float textureOffsetY = timeOffset.y;
        u0 += textureOffsetY;
        u1 += textureOffsetY;

        renderQuad(entry, vertices, x0, y0, z1, x1, y0, z1, x1, y1, z1, x0, y1, z1, u0, v0, u1, v1);
        renderQuad(entry, vertices, x1, y0, z0, x0, y0, z0, x0, y1, z0, x1, y1, z0, u0, v0, u1, v1);
        renderQuad(entry, vertices, x0, y0, z0, x0, y0, z1, x0, y1, z1, x0, y1, z0, u0, v0, u1, v1);
        renderQuad(entry, vertices, x1, y0, z1, x1, y0, z0, x1, y1, z0, x1, y1, z1, u0, v0, u1, v1);
        renderQuad(entry, vertices, x0, y1, z1, x1, y1, z1, x1, y1, z0, x0, y1, z0, u0, v0, u1, v1);
        renderQuad(entry, vertices, x0, y0, z0, x1, y0, z0, x1, y0, z1, x0, y0, z1, u0, v0, u1, v1);
    }

    private static void renderQuad(MatrixStack.Entry matrix, VertexConsumer vertices, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, float u1, float v1, float u2, float v2) {
        renderVertex(matrix, vertices, x1, y1, z1, u1, v1);
        renderVertex(matrix, vertices, x2, y2, z2, u2, v1);
        renderVertex(matrix, vertices, x3, y3, z3, u2, v2);
        renderVertex(matrix, vertices, x4, y4, z4, u1, v2);
    }

    private static void renderVertex(MatrixStack.Entry matrix, VertexConsumer vertices, float x, float y, float z, float u, float v) {
        vertices.vertex(matrix, x, y, z)
                .color(1.0f, 1.0f, 1.0f, 1.0f) // White color for textures
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(0xF000F0)
                .normal(matrix, 0, 1, 0);
    }

    /**
     * Renders a solid, double-sided textured sphere centered at the given BlockPos with rotation.
     *
     * @param matrices  The MatrixStack
     * @param vertices  The VertexConsumer
     * @param center    BlockPos to center the sphere
     * @param radius    The size of the sphere
     * @param quality   How many subdivisions the sphere has
     * @param rotation  Rotation in degrees around the Y-axis
     */
    public static void renderTexturedSphere(MatrixStack matrices, VertexConsumer vertices, BlockPos center, float radius, int quality, float rotation) {
        float inflation = 1.0f;
        radius *= inflation;

        matrices.translate(center.getX(), center.getY(), center.getZ());

        // Apply Y-axis rotation
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotation));

        for (int i = 0; i < quality; i++) {
            float lat1 = (float) Math.PI * (-0.5f + (float) i / quality);
            float lat2 = (float) Math.PI * (-0.5f + (float) (i + 1) / quality);

            for (int j = 0; j < quality; j++) {
                float lon1 = (float) (2 * Math.PI * j / quality);
                float lon2 = (float) (2 * Math.PI * (j + 1) / quality);

                float x1 = (float) (radius * Math.cos(lat1) * Math.cos(lon1));
                float y1 = (float) (radius * Math.sin(lat1));
                float z1 = (float) (radius * Math.cos(lat1) * Math.sin(lon1));
                float u1 = 1f - lon1 / (2f * (float)Math.PI);  // flip U
                float v1 = 1f - (lat1 + (float)Math.PI / 2f) / (float)Math.PI;  // flip V

                float x2 = (float) (radius * Math.cos(lat1) * Math.cos(lon2));
                float y2 = y1;
                float z2 = (float) (radius * Math.cos(lat1) * Math.sin(lon2));
                float u2 = 1f - lon2 / (2f * (float)Math.PI);
                float v2 = v1;

                float x3 = (float) (radius * Math.cos(lat2) * Math.cos(lon2));
                float y3 = (float) (radius * Math.sin(lat2));
                float z3 = (float) (radius * Math.cos(lat2) * Math.sin(lon2));
                float u3 = u2;
                float v3 = 1f - (lat2 + (float)Math.PI / 2f) / (float)Math.PI;

                float x4 = (float) (radius * Math.cos(lat2) * Math.cos(lon1));
                float y4 = y3;
                float z4 = (float) (radius * Math.cos(lat2) * Math.sin(lon1));
                float u4 = u1;
                float v4 = v3;

                renderQuad(matrices.peek(), vertices,
                        x1, y1, z1, x2, y2, z2, x3, y3, z3, x4, y4, z4,
                        u1, v1, u2, v2, u3, v3, u4, v4);
            }
        }
    }


    private static void renderQuad(MatrixStack.Entry matrix, VertexConsumer vertices,
                                   float x1, float y1, float z1,
                                   float x2, float y2, float z2,
                                   float x3, float y3, float z3,
                                   float x4, float y4, float z4,
                                   float u1, float v1,
                                   float u2, float v2,
                                   float u3, float v3,
                                   float u4, float v4) {
        renderVertex(matrix, vertices, x1, y1, z1, u1, v1);
        renderVertex(matrix, vertices, x2, y2, z2, u2, v2);
        renderVertex(matrix, vertices, x3, y3, z3, u3, v3);
        renderVertex(matrix, vertices, x4, y4, z4, u4, v4);
    }



    /**
     * Renders a vertical, textured beam from the given BlockPos to the top of the world.
     *
     * @param matrices    The MatrixStack
     * @param vertices    The VertexConsumer
     * @param center      The BlockPos origin of the beam
     * @param radius      Radius of the beam (e.g., 0.2f for thin beams)
     * @param height      Max beam height (e.g., world height - center.getY())
     * @param time        Used to animate the texture scroll
     */
    public static void renderSkyBeam(MatrixStack matrices, VertexConsumer vertices, BlockPos center, float radius, int height, float time) {
        MatrixStack.Entry entry = matrices.peek();

        float x = center.getX() + 0.5f;
        float z = center.getZ() + 0.5f;
        float yStart = center.getY();
        float yEnd = yStart + height;

        float angle = (time * 0.2f) % 1.0f;

        // 4 corners of the square beam around center
        float r = radius;
        float[][] corners = {
                { x - r, z - r },
                { x + r, z - r },
                { x + r, z + r },
                { x - r, z + r },
        };

        float u0 = 0.0f;
        float u1 = 1.0f;
        float v0 = angle;
        float v1 = angle + (height / 8.0f); // Scroll speed factor

        // Render 4 sides of the beam (quad between each corner and its next)
        for (int i = 0; i < 4; i++) {
            float[] corner1 = corners[i];
            float[] corner2 = corners[(i + 1) % 4];

            renderBeamQuad(entry, vertices,
                    corner1[0], yStart, corner1[1],
                    corner2[0], yStart, corner2[1],
                    corner2[0], yEnd,   corner2[1],
                    corner1[0], yEnd,   corner1[1],
                    u0, v0, u1, v1);
        }
    }

    private static void renderBeamQuad(MatrixStack.Entry matrix, VertexConsumer vertices,
                                       float x1, float y1, float z1,
                                       float x2, float y2, float z2,
                                       float x3, float y3, float z3,
                                       float x4, float y4, float z4,
                                       float u1, float v1, float u2, float v2) {
        renderVertex(matrix, vertices, x1, y1, z1, u1, v1);
        renderVertex(matrix, vertices, x2, y2, z2, u2, v1);
        renderVertex(matrix, vertices, x3, y3, z3, u2, v2);
        renderVertex(matrix, vertices, x4, y4, z4, u1, v2);
    }

    /**
     * Renders a textured vertical cone from the given BlockPos to a point at a specific height.
     *
     * @param matrices    The MatrixStack
     * @param vertices    The VertexConsumer
     * @param center      The BlockPos origin of the cone base
     * @param radius      Radius of the base of the cone
     * @param height      Height of the cone from base to tip
     * @param time        Used to animate the texture scroll
     */
    public static void renderCone(MatrixStack matrices, VertexConsumer vertices, BlockPos center, float radius, int height, float time) {
        MatrixStack.Entry entry = matrices.peek();

        float x = center.getX() + 0.5f;
        float z = center.getZ() + 0.5f;
        float yBase = center.getY();
        float yTip = yBase + height;

        float angleOffset = (time * 0.2f) % 1.0f;
        float u0 = 0.0f;
        float u1 = 1.0f;

        int segments = 16;
        for (int i = 0; i < segments; i++) {
            double angle1 = 2.0 * Math.PI * i / segments;
            double angle2 = 2.0 * Math.PI * (i + 1) / segments;

            float x1 = x + (float)Math.cos(angle1) * radius;
            float z1 = z + (float)Math.sin(angle1) * radius;
            float x2 = x + (float)Math.cos(angle2) * radius;
            float z2 = z + (float)Math.sin(angle2) * radius;

            float v0 = angleOffset;
            float v1 = angleOffset + (height / 8.0f);

            renderBeamQuad(entry, vertices,
                    x1, yBase, z1,
                    x2, yBase, z2,
                    x,  yTip,  z,
                    x,  yTip,  z,
                    u0, v0, u1, v1);
        }
    }
}
