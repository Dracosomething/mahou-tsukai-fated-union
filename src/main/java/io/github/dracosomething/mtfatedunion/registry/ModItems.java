package io.github.dracosomething.mtfatedunion.registry;

import io.github.dracosomething.mtfatedunion.registry.item.curruid_bone;
import io.github.dracosomething.mtfatedunion.registry.item.gae_bolg;
import io.github.dracosomething.mtfatedunion.mtfatedunion;
import io.github.dracosomething.mtfatedunion.registry.item.gae_bolg_morgan;
import io.github.dracosomething.mtfatedunion.registry.tag.mtfatedunionTiers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS;
    public static final RegistryObject<Item> GAE_BOLG;
    public static final RegistryObject<Item> GAE_BOLG_MORGAN;
    public static final RegistryObject<Item> CURRUID_BONE;
    public static final RegistryObject<Item> CURRUID_ALTER_BONE;

    public ModItems(){

    }

    private static void registerUseItemProperty(RegistryObject<Item> item, String name) {
        ItemProperties.register((Item)item.get(), new ResourceLocation(name), (stack, level, entity, seed) ->
                (entity != null && entity.isUsingItem() && entity.getUseItem() == stack) ? 1.0F : 0.0F);
    }

    public static void registeritemproperties() {
        registerUseItemProperty(GAE_BOLG, "throwing");
        registerUseItemProperty(GAE_BOLG_MORGAN, "throwing");
    }

    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, mtfatedunion.MODID);
        GAE_BOLG = ITEMS.register("gae_bolg", () ->{return new gae_bolg(mtfatedunionTiers.GAE_BOLG, 15, 2f, (new Item.Properties())
                .stacksTo(1)
                .durability(5000)
                .fireResistant()
        );
        });
        GAE_BOLG_MORGAN = ITEMS.register("gae_bolg_morgan", () -> {
            return new gae_bolg_morgan(mtfatedunionTiers.GAE_BOLG, 15, 2f, (new Item.Properties())
                .stacksTo(1)
                .durability(5000)
                .fireResistant()
            );
        });
        CURRUID_BONE = ITEMS.register("curruid_bone", curruid_bone::new);
        CURRUID_ALTER_BONE = ITEMS.register("curruid_alter_bone", curruid_bone::new);
    }
}
