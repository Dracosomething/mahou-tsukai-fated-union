package io.github.dracosomething.mtfatedunion.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.dracosomething.mtfatedunion.registry.item.gae_bolg;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import stepsword.mahoutsukai.client.ClientHandler;
import stepsword.mahoutsukai.config.MTConfig;
import stepsword.mahoutsukai.render.RenderBaseItem;
import stepsword.mahoutsukai.render.RenderUtils;
import stepsword.mahoutsukai.util.Utils;

public class GaeBolgRenderer extends BlockEntityWithoutLevelRenderer {
    public static final ResourceLocation gae_bolg = new ResourceLocation("mtfatedunion", "item/gae_bolg");

    public GaeBolgRenderer(BlockEntityRenderDispatcher p_172550_, EntityModelSet p_172551_) {
        super(p_172550_, p_172551_);
    }

    public void renderByItem(ItemStack itemStackIn, ItemDisplayContext transform, PoseStack matrix, MultiBufferSource buffer, int var1, int var2) {
        if (itemStackIn != null && itemStackIn.getItem() instanceof gae_bolg) {
            matrix.pushPose();
            RenderBaseItem.render(itemStackIn, matrix, buffer, 240, var2, gae_bolg, true);
            float offsetx = 0.0F;
            float offsety = 0.0F;
            float offsetz = 0.5F;
            float r = 255.0F;
            float g = 50.0F;
            float b = 50.0F;
            float a = 1.0F;
            float[] colors = Utils.getColor(itemStackIn);
            if (colors != null && colors.length > 2) {
                r = colors[0];
                g = colors[1];
                b = colors[2];
            }

            float rotation = 0.0F;
            float yaw = 90.0F;
            float pitch = 45.0F;
            double speed = MTConfig.POWER_CONSOLIDATION_CALIBURN_RING_SPEED;
            float ring = 90.0F;
            float size = 1.0F;
            float pull = Minecraft.getInstance().level != null ? (float)(ClientHandler.clientTickCounter % 360L) : 0.0F;
            rotation = (float)((double)rotation + (double)pull * speed);
            float secondrotation = (float)(360.0 - (double)pull * speed % 360.0);
            rotation %= 360.0F;
            secondrotation %= 360.0F;
            matrix.translate(offsetx, offsety, offsetz);
            RenderUtils.rotateQ(yaw, 0.0F, 1.0F, 0.0F, matrix);
            RenderUtils.rotateQ(pitch, 1.0F, 0.0F, 0.0F, matrix);
            RenderUtils.rotateQ(rotation, 0.0F, 1.0F, 0.0F, matrix);
            int lightmapX = 240;
            int lightmapY = 240;
            r /= 255.0F;
            g /= 255.0F;
            b /= 255.0F;
            RenderUtils.renderRing(matrix, buffer, 0.9070000052452087, ring - 20.0F, size / 8.9F, 0.05F, 48, lightmapX, lightmapY, r, g, b, a, 0, transform);
            RenderUtils.rotateQ(rotation, 0.0F, -1.0F, 0.0F, matrix);
            RenderUtils.rotateQ(secondrotation, 0.0F, 1.0F, 0.0F, matrix);
            RenderUtils.renderRing(matrix, buffer, 0.7, ring - 10.0F, size / 6.8F, 0.05F, 48, lightmapX, lightmapY, r, g, b, a, 1, transform);
            RenderUtils.rotateQ(secondrotation, 0.0F, -1.0F, 0.0F, matrix);
            RenderUtils.rotateQ(rotation, 0.0F, 1.0F, 0.0F, matrix);
            RenderUtils.renderRing(matrix, buffer, 0.5, ring - 4.0F, size / 3.7F, 0.09F, 48, lightmapX, lightmapY, r, g, b, a, 2, transform);
            matrix.popPose();
            Minecraft.getInstance().renderBuffers().bufferSource().endBatch();
        }

    }
}
