package io.github.dracosomething.mtfatedunion.registry;

import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class mtfatedunionTiers {
    public static final ForgeTier GAE_BOLG = new ForgeTier(
            7,
            5000,
            1.1F,
            3f,
            55,
            TagInit.NEEDS_GAE_BOLG,
            () -> Ingredient.EMPTY
    );
}
