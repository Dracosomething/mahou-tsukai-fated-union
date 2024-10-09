package io.github.dracosomething.mtfatedunion.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.dracosomething.mtfatedunion.client.model.ThrownGaeBolgModel;
import io.github.dracosomething.mtfatedunion.mtfatedunion;
import io.github.dracosomething.mtfatedunion.registry.entity.ThrowGaeBolg;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class GaeMorganThrownEntityRenderer extends EntityRenderer<ThrowGaeBolg> {
    public static final ResourceLocation SPEAR_LOCATION = new ResourceLocation(mtfatedunion.MODID, "textures/entity/gae_bolg_morgan.png");
    private final ThrownGaeBolgModel model;

    public GaeMorganThrownEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ThrownGaeBolgModel(context.bakeLayer(ThrownGaeBolgModel.LAYER_LOCATION));
    }

    public void render(ThrowGaeBolg p_116111_, float p_116112_, float p_116113_, PoseStack p_116114_, MultiBufferSource p_116115_, int p_116116_) {
        p_116114_.pushPose();
        p_116114_.mulPose(Axis.YP.rotationDegrees(Mth.lerp(p_116113_, p_116111_.yRotO, p_116111_.getYRot()) - 90.0F));
        p_116114_.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(p_116113_, p_116111_.xRotO, p_116111_.getXRot()) + 90.0F));
        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(p_116115_, this.model.renderType(this.getTextureLocation(p_116111_)), false, p_116111_.isFoil());
        this.model.renderToBuffer(p_116114_, vertexconsumer, p_116116_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        p_116114_.popPose();
        super.render(p_116111_, p_116112_, p_116113_, p_116114_, p_116115_, p_116116_);
    }

    @Override
    public ResourceLocation getTextureLocation(ThrowGaeBolg p_116109_) {
        return SPEAR_LOCATION;
    }
}
