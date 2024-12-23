package io.github.dracosomething.mtfatedunion;

import com.mojang.logging.LogUtils;
import io.github.dracosomething.mtfatedunion.client.ModEntityRenderer;
import io.github.dracosomething.mtfatedunion.effects.PowerConsolidationExtention;
import io.github.dracosomething.mtfatedunion.registry.ModEntities;
import io.github.dracosomething.mtfatedunion.registry.creativetabs.MahouAddonTab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import stepsword.mahoutsukai.block.FogProjector;
import stepsword.mahoutsukai.effects.projection.PowerConsolidationSpellEffect;
import stepsword.mahoutsukai.entity.butterfly.ButterflyEntity;
import stepsword.mahoutsukai.fluids.MurkyWaterBlock;

import static io.github.dracosomething.mtfatedunion.registry.MobEffects.EFFECTS;
import static io.github.dracosomething.mtfatedunion.registry.ModEntities.*;
import static io.github.dracosomething.mtfatedunion.registry.ModItems.GAE_BOLG;
import static io.github.dracosomething.mtfatedunion.registry.ModItems.ITEMS;
import static io.github.dracosomething.mtfatedunion.registry.creativetabs.MahouAddonTab.TABS;

@Mod(mtfatedunion.MODID)
public class mtfatedunion
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "mtfatedunion";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static long tickCounter = 0L;
    private static BlockEntityWithoutLevelRenderer ITEMS_RENDERER;

    public mtfatedunion(){}

    public mtfatedunion(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading

        ENTITY_TYPES.register(modEventBus);
        ITEMS.register(modEventBus);
        TABS.register(modEventBus);
        EFFECTS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        context.registerConfig(ModConfig.Type.SERVER, Config.SPEC);
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

    @SubscribeEvent(
            priority = EventPriority.NORMAL,
            receiveCanceled = true
    )
    public void worldTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.START && !event.level.isClientSide) {
            tickCounter = event.level.getGameTime() % 50000L;
        }

        PowerConsolidationExtention.PCWorldTick(event);
        PowerConsolidationExtention.PCWorldTick2(event);
    }
}
