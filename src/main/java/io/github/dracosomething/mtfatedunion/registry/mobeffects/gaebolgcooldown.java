package io.github.dracosomething.mtfatedunion.registry.mobeffects;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import stepsword.mahoutsukai.potion.CooldownPotion;
import stepsword.mahoutsukai.potion.NoShowMobEffectExtensions;

import java.util.function.Consumer;

public class gaebolgcooldown extends CooldownPotion {
    public gaebolgcooldown(){}

    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        consumer.accept(new NoShowMobEffectExtensions() {
            public boolean isVisibleInInventory(MobEffectInstance instance) {
                return true;
            }

            public boolean isVisibleInGui(MobEffectInstance instance) {
                return true;
            }
        });
    }
}
