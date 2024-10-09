package io.github.dracosomething.mtfatedunion.registry;

import com.doodle.client.entity.*;
import com.doodle.client.model.*;
import io.github.dracosomething.mtfatedunion.client.model.ThrownGaeBolgModel;
import io.github.dracosomething.mtfatedunion.client.model.ThrownGaeMorganModel;
import io.github.dracosomething.mtfatedunion.client.renderer.GaeBolgThrownEntityRenderer;
import io.github.dracosomething.mtfatedunion.client.renderer.GaeMorganThrownEntityRenderer;
import io.github.dracosomething.mtfatedunion.mtfatedunion;
import io.github.dracosomething.mtfatedunion.registry.entity.ThrowGaeBolg;
import io.github.dracosomething.mtfatedunion.registry.entity.ThrowGaeBolgMorgan;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static io.github.dracosomething.mtfatedunion.registry.ModItems.GAE_BOLG;

public class ModEntities {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, mtfatedunion.MODID);

    public static void registerRenderers() {
        EntityRenderers.register((EntityType)THROW_GEA_BOLG.get(), GaeBolgThrownEntityRenderer::new);
        EntityRenderers.register((EntityType)THROW_GEA_MORGAN.get(), GaeMorganThrownEntityRenderer::new);
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ThrownGaeBolgModel.LAYER_LOCATION, ThrownGaeBolgModel::createBodyLayer);
        event.registerLayerDefinition(ThrownGaeMorganModel.LAYER_LOCATION, ThrownGaeMorganModel::createBodyLayer);
    }

    public static final RegistryObject<EntityType<ThrowGaeBolg>> THROW_GEA_BOLG = ENTITY_TYPES.register("throwgaebolg",
            () ->EntityType.Builder.<ThrowGaeBolg>of(ThrowGaeBolg::new, MobCategory.MISC)
                    .sized(1.0f,1.0f)
                    .build(new ResourceLocation(mtfatedunion.MODID, "throwgeabolg").toString())
    );
    public static final RegistryObject<EntityType<ThrowGaeBolgMorgan>> THROW_GEA_MORGAN = ENTITY_TYPES.register("throwgaemorgan",
            () ->EntityType.Builder.<ThrowGaeBolgMorgan>of(ThrowGaeBolgMorgan::new, MobCategory.MISC)
                    .sized(1.0f, 1.0f)
                    .build(new ResourceLocation(mtfatedunion.MODID, "throwgaemorgan").toString())
    );
}
