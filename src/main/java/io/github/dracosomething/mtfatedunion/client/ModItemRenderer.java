package io.github.dracosomething.mtfatedunion.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.dracosomething.mtfatedunion.client.model.ThrownGaeBolgModel;
import io.github.dracosomething.mtfatedunion.client.model.ThrownGaeMorganModel;
import io.github.dracosomething.mtfatedunion.client.renderer.entity.GaeBolgThrownEntityRenderer;
import io.github.dracosomething.mtfatedunion.client.renderer.entity.GaeMorganThrownEntityRenderer;
import io.github.dracosomething.mtfatedunion.registry.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ModItemRenderer extends BlockEntityWithoutLevelRenderer {
    private ThrownGaeBolgModel ThrownGaeBolgModel;
    private ThrownGaeMorganModel ThrownGaeMorganModel;
    private final EntityModelSet entityModelSet = Minecraft.getInstance().getEntityModels();

    public ModItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.ThrownGaeBolgModel = new ThrownGaeBolgModel(this.entityModelSet.bakeLayer(io.github.dracosomething.mtfatedunion.client.model.ThrownGaeBolgModel.LAYER_LOCATION));
        this.ThrownGaeMorganModel = new ThrownGaeMorganModel(this.entityModelSet.bakeLayer(io.github.dracosomething.mtfatedunion.client.model.ThrownGaeMorganModel.LAYER_LOCATION));
    }

    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
        this.ThrownGaeBolgModel = new ThrownGaeBolgModel(this.entityModelSet.bakeLayer(io.github.dracosomething.mtfatedunion.client.model.ThrownGaeBolgModel.LAYER_LOCATION));
        this.ThrownGaeMorganModel = new ThrownGaeMorganModel(this.entityModelSet.bakeLayer(io.github.dracosomething.mtfatedunion.client.model.ThrownGaeMorganModel.LAYER_LOCATION));
    }

    public void renderByItem(ItemStack stack, @NotNull ItemDisplayContext context, @NotNull PoseStack pose, @NotNull MultiBufferSource source, int light, int overlay) {
        ResourceLocation layerLocation;
        VertexConsumer consumer;
        if (stack.is((Item) ModItems.GAE_BOLG.get())) {
            pose.pushPose();
            pose.scale(1.0F, -1.0F, -1.0F);
            layerLocation = GaeBolgThrownEntityRenderer.SPEAR_LOCATION;
            consumer = ItemRenderer.getFoilBufferDirect(source, this.ThrownGaeBolgModel.renderType(GaeBolgThrownEntityRenderer.SPEAR_LOCATION), false, stack.hasFoil());
            this.ThrownGaeBolgModel.renderToBuffer(pose, consumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            pose.popPose();
        }
        else if (stack.is((Item) ModItems.GAE_BOLG_MORGAN.get())) {
            pose.pushPose();
            pose.scale(1.0F, -1.0F, -1.0F);
            layerLocation = GaeMorganThrownEntityRenderer.SPEAR_LOCATION;
            consumer = ItemRenderer.getFoilBufferDirect(source, this.ThrownGaeMorganModel.renderType(GaeMorganThrownEntityRenderer.SPEAR_LOCATION), false, stack.hasFoil());
            this.ThrownGaeMorganModel.renderToBuffer(pose, consumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
            pose.popPose();
        }
    }
}
