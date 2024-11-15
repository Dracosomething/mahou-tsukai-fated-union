package io.github.dracosomething.mtfatedunion;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@Mod.EventBusSubscriber(modid = mtfatedunion.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    static ForgeConfigSpec SPEC = BUILDER.build();

    // Gae Bolg values
    public static final ForgeConfigSpec.ConfigValue<Integer> GAEBOLGDURABILITY;
    public static final ForgeConfigSpec.ConfigValue<Integer> GAEBOLGMANA;
    public static final ForgeConfigSpec.ConfigValue<Integer> GAEBOLGDURATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> GAEBOLGRADIUS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GAEBOLGBLOCK;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GAEBOLGENTITY;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GAEBOLGCOOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GAEBOLGPARTICLE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GAEBOLGENABLE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> DROPBLOCK;
    public static final ForgeConfigSpec.ConfigValue<Double> DAMAGESCALEFACTOR;
//    public static final ForgeConfigSpec.ConfigValue<String> GAEITEM;
    public static final ForgeConfigSpec.ConfigValue<Integer> PCBOLGMANA;

    // Gae Bolg Morgan values
    public static final ForgeConfigSpec.ConfigValue<Integer> GAEMORGANDURABILITY;
    public static final ForgeConfigSpec.ConfigValue<Integer> GAEMORGANMANA;
    public static final ForgeConfigSpec.ConfigValue<Integer> GAEMORGANDURATION;
    public static final ForgeConfigSpec.ConfigValue<Integer> GAEMORGANRADIUS;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GAEMORGANBLOCK;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GAEMORGANENTITY;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GAEMORGANCOOLDOWN;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GAEMORGANPARTICLE;
    public static final ForgeConfigSpec.ConfigValue<Boolean> GAEMORGANENABLE;
    public static final ForgeConfigSpec.ConfigValue<Float> DAMAGEFACTORMORGAN;
    public static final ForgeConfigSpec.ConfigValue<Float> MORGANDAMAGE;
//    public static final ForgeConfigSpec.ConfigValue<String> MORGANITEM;
    public static final ForgeConfigSpec.ConfigValue<Integer> PCMORGANMANA;

    static {
        BUILDER.push("Weapons");
        BUILDER.push("Gae Bolg");

        GAEBOLGENABLE = BUILDER
                .comment("Enable Gae Bolg being obtainable through it's normal way")
                .comment("True or False")
                .define("GaeBolgDisable", false);
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
        GAEBOLGDURATION = BUILDER
                .comment("Change cooldown length in ticks, 20 ticks per second")
                .comment("Range between 0 and 100000000")
                .define("GaeBolgcooldown", 500);
        GAEBOLGPARTICLE = BUILDER
                .comment("Change if it has particles")
                .comment("True or False")
                .define("GaeBolgParticle", true);
        DAMAGESCALEFACTOR = BUILDER
                .comment("Change with what factor the damage scales")
                .comment("Formula is max mana * factor / facter")
                .comment("Range between 0.0 and 100000000.0")
                .define("DamageScaleFactor", 0.00025);
        DROPBLOCK = BUILDER
                .comment("Enable block drops")
                .comment("True or False")
                .define("DropResources", true);
        BUILDER.pop();
        BUILDER.pop();

        BUILDER.push("Gae Bolg Morgan");

        GAEMORGANENABLE = BUILDER
                .comment("Enable Gae Bolg being obtainable through it's normal way")
                .comment("True or False")
                .define("GaeMorganDisable", false);
        GAEMORGANDURABILITY = BUILDER
                .comment("Change the durability stat of Gae Bolg")
                .comment("Range between 0 and 100000000")
                .define("GaeMorganDurability", 5000);
        BUILDER.push("throw ability");
        GAEMORGANMANA = BUILDER
                .comment("Change the mana cost")
                .comment("Range between 0 and 100000000")
                .define("GaeMorganManaThrow", 30000);
        GAEMORGANRADIUS = BUILDER
                .comment("Change the explosion radius")
                .comment("Range between 0 and 100000000")
                .define("GaeMorganRadius", 15);
        GAEMORGANBLOCK = BUILDER
                .comment("Change if it activates when hitting a block")
                .comment("True or False")
                .define("GaeMorganBlock", true);
        GAEMORGANENTITY = BUILDER
                .comment("Change if it activates when hitting an entity")
                .comment("True or False")
                .define("GaeMorganEntity", true);
        GAEMORGANCOOLDOWN = BUILDER
                .comment("Change if it has a cooldown")
                .comment("True or False")
                .define("GaeMorganCooldown", false);
        GAEMORGANDURATION = BUILDER
                .comment("Change cooldown length in ticks, 20 ticks per second")
                .comment("Range between 0 and 100000000")
                .define("GaeMorgancooldown", 0);
        GAEMORGANPARTICLE = BUILDER
                .comment("Change if it has particles")
                .comment("True or False")
                .define("GaeMorganParticle", true);
        DAMAGEFACTORMORGAN = BUILDER
                .comment("Change how much percent damage it deals")
                .comment("Range between 0 and 100000000")
                .define("DamageFactorMorgan", 55F);
        MORGANDAMAGE = BUILDER
                .comment("Change how much damage it deals when bellow 40 hp")
                .comment("Range between 0 and 100000000")
                .define("MorganDamage", 10F);
        BUILDER.pop();
        BUILDER.pop();
        BUILDER.pop();

        BUILDER.push("Power Consolidation");
        BUILDER.push("Gae Bolg");

//        GAEITEM = BUILDER
//                .comment("Item used to craft Gae Bolg")
//                .define("GaeBolgItem", "curruid_bone");
        PCBOLGMANA = BUILDER
                .comment("The amount of mana it costs to get Gae Bolg")
                .comment("Range between 0 and 100000000")
                .define("PCGaeBolgMana", 10000);
        BUILDER.pop();

        BUILDER.push("Gae Bolg Morgan");

//        MORGANITEM = BUILDER
//                .comment("Item used to craft Gae Bolg")
//                .define("GaeMorganItem", "curruid_alter_bone");
        PCMORGANMANA = BUILDER
                .comment("The amount of mana it costs to get Gae Bolg")
                .comment("Range between 0 and 100000000")
                .define("PCGaeMorganMana", 10000);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    public static int GaeBolgDurability;
    public static int GaeBolgManaThrow;
    public static int GaeBolgRadius;
    public static int GaeBolgcooldown;
    public static boolean GaeBolgBlock;
    public static boolean GaeBolgEntity;
    public static boolean GaeBolgCooldown;
    public static boolean GaeBolgParticle;
    public static boolean GaeBolgEnable;
    public static boolean DropResources;
    public static double DamageScaleFactor;
    public static String GaeBolgItem;
    public static int PCGaeBolgMana;

    public static int GaeMorganDurability;
    public static int GaeMorganManaThrow;
    public static int GaeMorganRadius;
    public static int GaeMorgancooldown;
    public static boolean GaeMorganBlock;
    public static boolean GaeMorganEntity;
    public static boolean GaeMorganCooldown;
    public static boolean GaeMorganParticle;
    public static boolean GaeMorganEnable;
    public static float DamageFactorMorgan;
    public static float MorganDamage;
    public static String GaeMorganItem;
    public static int PCGaeMorganMana;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        GaeBolgDurability = GAEBOLGDURABILITY.get();
        GaeBolgEnable = GAEBOLGENABLE.get();
        GaeBolgBlock = GAEBOLGBLOCK.get();
        GaeBolgEntity = GAEBOLGENTITY.get();
        GaeBolgCooldown = GAEBOLGCOOLDOWN.get();
        GaeBolgManaThrow = GAEBOLGMANA.get();
        GaeBolgRadius = GAEBOLGRADIUS.get();
        GaeBolgParticle = GAEBOLGPARTICLE.get();
        DamageScaleFactor = DAMAGESCALEFACTOR.get();
        DropResources = DROPBLOCK.get();
        GaeBolgcooldown = GAEBOLGDURATION.get();
//        GaeBolgItem = GAEITEM.get();
        PCGaeBolgMana = PCBOLGMANA.get();

        GaeMorganDurability = GAEMORGANDURABILITY.get();
        GaeMorganEnable = GAEMORGANENABLE.get();
        GaeMorganBlock = GAEMORGANBLOCK.get();
        GaeMorganEntity = GAEMORGANENTITY.get();
        GaeMorganCooldown = GAEMORGANCOOLDOWN.get();
        GaeMorganManaThrow = GAEMORGANMANA.get();
        GaeMorganRadius = GAEMORGANRADIUS.get();
        GaeMorganParticle = GAEMORGANPARTICLE.get();
        DamageFactorMorgan = DAMAGEFACTORMORGAN.get() / 100;
        GaeMorgancooldown = GAEMORGANDURATION.get();
        MorganDamage = MORGANDAMAGE.get();
//        GaeMorganItem = MORGANITEM.get();
        PCGaeMorganMana = PCMORGANMANA.get();
    }
}
