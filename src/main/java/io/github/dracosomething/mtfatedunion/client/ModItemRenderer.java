package io.github.dracosomething.mtfatedunion.client;

import com.doodle.client.entity.ThrownDiamondSpearRenderer;
import com.doodle.client.entity.ThrownGoldenSpearRenderer;
import com.doodle.client.entity.ThrownIronSpearRenderer;
import com.doodle.client.entity.ThrownNetheriteSpearRenderer;
import com.doodle.client.entity.ThrownStoneSpearRenderer;
import com.doodle.client.entity.ThrownWoodenSpearRenderer;
import com.doodle.client.model.DiamondSpearModel;
import com.doodle.client.model.GoldenSpearModel;
import com.doodle.client.model.IronSpearModel;
import com.doodle.client.model.NetheriteSpearModel;
import com.doodle.client.model.StoneSpearModel;
import com.doodle.client.model.WoodenSpearModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import io.github.dracosomething.mtfatedunion.client.model.ThrownGaeBolgModel;
import io.github.dracosomething.mtfatedunion.client.renderer.GaeBolgThrownEntityRenderer;
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
    private final EntityModelSet entityModelSet = Minecraft.getInstance().getEntityModels();

    public ModItemRenderer() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.ThrownGaeBolgModel = new ThrownGaeBolgModel(this.entityModelSet.bakeLayer(io.github.dracosomething.mtfatedunion.client.model.ThrownGaeBolgModel.LAYER_LOCATION));
    }

    public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
        this.ThrownGaeBolgModel = new ThrownGaeBolgModel(this.entityModelSet.bakeLayer(io.github.dracosomething.mtfatedunion.client.model.ThrownGaeBolgModel.LAYER_LOCATION));
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
}}
