package io.github.dracosomething.mtfatedunion.client;

import io.github.dracosomething.mtfatedunion.client.model.ThrownGaeBolgModel;
import io.github.dracosomething.mtfatedunion.client.model.ThrownGaeMorganModel;
import io.github.dracosomething.mtfatedunion.client.renderer.entity.GaeBolgThrownEntityRenderer;
import io.github.dracosomething.mtfatedunion.client.renderer.entity.GaeMorganThrownEntityRenderer;
import io.github.dracosomething.mtfatedunion.registry.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(
        modid = "mtfatedunion",
        bus = Bus.MOD,
        value = {Dist.CLIENT}
)
public class ClientSetup {
    public ClientSetup() {
    }

    @SubscribeEvent
    public static void doSetup(FMLClientSetupEvent event) {
        EntityRenderers.register((EntityType) ModEntities.THROW_GEA_BOLG.get(), GaeBolgThrownEntityRenderer::new);
        EntityRenderers.register((EntityType) ModEntities.THROW_GEA_MORGAN.get(), GaeMorganThrownEntityRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ThrownGaeBolgModel.LAYER_LOCATION, ThrownGaeBolgModel::createBodyLayer);
        event.registerLayerDefinition(ThrownGaeMorganModel.LAYER_LOCATION, ThrownGaeMorganModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onLayerDefinitionsRegister(EntityRenderersEvent.RegisterLayerDefinitions event) {
        ModEntities.registerLayerDefinitions(event);
    }
}
