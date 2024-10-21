package io.github.dracosomething.mtfatedunion.registry;

import io.github.dracosomething.mtfatedunion.mtfatedunion;
import io.github.dracosomething.mtfatedunion.registry.effects.gaebolgcooldown;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import stepsword.mahoutsukai.MahouRegistry;
import stepsword.mahoutsukai.potion.CaliburnMorganCooldownPotion;

public class MobEffects {
    public static final DeferredRegister<MobEffect> EFFECTS;
    public static final RegistryObject<MobEffect> BOLG_COOLDOWN;

    static {
        EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, mtfatedunion.MODID);
        BOLG_COOLDOWN = EFFECTS.register("gae_bolg_cooldown", gaebolgcooldown::new);
    }
}
