package io.github.dracosomething.mtfatedunion.registry.tag;

import io.github.dracosomething.mtfatedunion.registry.TagInit;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class mtfatedunionTiers {
    public static final ForgeTier GAE_BOLG = new ForgeTier(
            7,
            5000,
            0F,
            0f,
            55,
            TagInit.NEEDS_GAE_BOLG,
            () -> Ingredient.EMPTY
    );
}
