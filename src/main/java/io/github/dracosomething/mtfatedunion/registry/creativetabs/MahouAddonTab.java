package io.github.dracosomething.mtfatedunion.registry.creativetabs;

import io.github.dracosomething.mtfatedunion.mtfatedunion;
import io.github.dracosomething.mtfatedunion.registry.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;
import stepsword.mahoutsukai.creativetabs.MahouTab;

public class MahouAddonTab {
    public static final DeferredRegister<CreativeModeTab> TABS= DeferredRegister.create(Registries.CREATIVE_MODE_TAB, mtfatedunion.MODID);
    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = TABS.register("mtfatedunion_tab", () -> CreativeModeTab.builder()
            // Set name of tab to display
            .title(Component.translatable("item_group." + mtfatedunion.MODID + ".mtfatedunion"))
            // Set icon of creative tab
            .icon(() -> new ItemStack(ModItems.GAE_BOLG.get()))
            // Add default items to tab
            .displayItems((params, output) -> {
                output.accept(ModItems.GAE_BOLG.get());
            })
            .build()
    );


    public MahouAddonTab(CreativeModeTab.Builder title) {
    }

    public static void registerCreative(RegisterEvent event) {
    }
}