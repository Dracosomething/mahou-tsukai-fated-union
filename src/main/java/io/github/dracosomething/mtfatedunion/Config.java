package io.github.dracosomething.mtfatedunion;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = mtfatedunion.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    static ForgeConfigSpec SPEC = BUILDER.build();

    public static final ForgeConfigSpec.ConfigValue<Integer> GAEBOLGDURABILITY;
    public static final ForgeConfigSpec.ConfigValue<Integer> GAEBOLGMANA;
    public static final ForgeConfigSpec.ConfigValue<Integer> GAEBOLGRADIUS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GAEBOLGBLOCK;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GAEBOLGENTITY;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GAEBOLGCOOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GAEBOLGPARTICLE;

    static {
        BUILDER.push("Weapons");
        BUILDER.push("Gae Bolg");

        GAEBOLGDURABILITY = BUILDER
                .comment("Change the durability stat of Gae Bolg")
                .comment("Range between 0 and 100000000")
                .define("GaeBolgDurability", 5000);
        BUILDER.push("throw ability");
        GAEBOLGMANA = BUILDER
                .comment("Change the mana cost")
                .comment("Range between 0 and 100000000")
                .define("GaeBolgManaThrow", 5000);
        GAEBOLGRADIUS = BUILDER
                .comment("Change the explosion radius")
                .comment("Range between 0 and 100000000")
                .define("GaeBolgRadius", 15);
        GAEBOLGBLOCK = BUILDER
                .comment("Change if it activates when hitting a block")
                .comment("True or False")
                .define("GaeBolgBlock", true);
        GAEBOLGENTITY = BUILDER
                .comment("Change if it activates when hitting an entity")
                .comment("True or False")
                .define("GaeBolgEntity", true);
        GAEBOLGCOOLDOWN = BUILDER
                .comment("Change if it has a cooldown")
                .comment("True or False")
                .define("GaeBolgCooldown", true);
        GAEBOLGPARTICLE = BUILDER
                .comment("Change if it has particles")
                .comment("True or False")
                .define("GaeBolgParticle", true);
        


        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static int GaeBolgDurability;
    public static int GaeBolgManaThrow;
    public static int GaeBolgRadius;
    public static boolean GaeBolgBlock;
    public static boolean GaeBolgEntity;
    public static boolean GaeBolgCooldown;
    public static boolean GaeBolgParticle;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        GaeBolgDurability = GAEBOLGDURABILITY.get();
        GaeBolgBlock = GAEBOLGBLOCK.get();
        GaeBolgEntity = GAEBOLGENTITY.get();
        GaeBolgCooldown = GAEBOLGCOOLDOWN.get();
        GaeBolgManaThrow = GAEBOLGMANA.get();
        GaeBolgRadius = GAEBOLGRADIUS.get();
        GaeBolgParticle = GAEBOLGPARTICLE.get();
    }
}
