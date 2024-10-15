package io.github.dracosomething.mtfatedunion.util;

import io.github.dracosomething.mtfatedunion.capability.gae_bolg.GaeBolgMahouProvider;
import io.github.dracosomething.mtfatedunion.capability.gae_bolg.IGaeBolgMahou;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import stepsword.mahoutsukai.capability.caliburn.CaliburnMahouProvider;
import stepsword.mahoutsukai.util.Utils;

public class UtilsExtention extends Utils {
    public static IGaeBolgMahou getGaeBolgMahou(ItemStack stack) {
        try {
            LazyOptional<IGaeBolgMahou> lazy = stack.getCapability(GaeBolgMahouProvider.MAHOU, (Direction)null);
            return (IGaeBolgMahou)lazy.orElseThrow(RuntimeException::new);
        } catch (Exception var2) {
            return null;
        }
    }
}
