package io.github.dracosomething.mtfatedunion.registry;

import io.github.dracosomething.mtfatedunion.mtfatedunion;
import io.github.dracosomething.mtfatedunion.registry.entity.ThrowGaeBolg;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static io.github.dracosomething.mtfatedunion.registry.ModItems.GAE_BOLG;

public class ModEntities {
    public static DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, mtfatedunion.MODID);

    public static final RegistryObject<EntityType<ThrowGaeBolg>> THROW_GEA_BOLG = ENTITY_TYPES.register("throwgaebolg",
            () ->EntityType.Builder.<ThrowGaeBolg>of(ThrowGaeBolg::new, MobCategory.MISC)
                    .sized(1.0f,1.0f)
                    .build(new ResourceLocation(mtfatedunion.MODID, "throwgeabolg").toString())
    );
}
