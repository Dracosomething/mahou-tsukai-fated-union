package io.github.dracosomething.mtfatedunion.registry;

import net.minecraft.tags.TagKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.resources.ResourceLocation;

public class TagInit {
    public static final TagKey<Block> NEEDS_GAE_BOLG = tag("needs_netherite_tool");

    private static TagKey<Block> tag(String name)
    {
        return BlockTags.create(new ResourceLocation("forge", name));
    }
}
