package io.github.dracosomething.mtfatedunion.effects;

import io.github.dracosomething.mtfatedunion.capability.gae_bolg.IGaeBolgMahou;
import io.github.dracosomething.mtfatedunion.registry.ModItems;
import io.github.dracosomething.mtfatedunion.registry.creativetabs.MahouAddonTab;
import io.github.dracosomething.mtfatedunion.registry.item.curruid_bone;
import io.github.dracosomething.mtfatedunion.util.UtilsExtention;
import io.netty.util.internal.ConcurrentSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.items.IItemHandler;
import stepsword.mahoutsukai.MahouTsukaiMod;
import stepsword.mahoutsukai.advancements.ModTriggers;
import stepsword.mahoutsukai.capability.caliburn.ICaliburnMahou;
import stepsword.mahoutsukai.capability.lance.ILanceMahou;
import stepsword.mahoutsukai.capability.mahou.PlayerManaManager;
import stepsword.mahoutsukai.config.MTConfig;
import stepsword.mahoutsukai.config.MandatoryFun;
import stepsword.mahoutsukai.effects.projection.PowerConsolidationSpellEffect;
import stepsword.mahoutsukai.effects.projection.ProjectionSpellEffect;
import stepsword.mahoutsukai.entity.WeaponProjectileEntity;
import stepsword.mahoutsukai.handlers.ServerHandler;
import stepsword.mahoutsukai.item.ItemBase;
import stepsword.mahoutsukai.item.lance.Rhongomyniad;
import stepsword.mahoutsukai.item.spells.projection.PowerConsolidation.Caliburn;
import stepsword.mahoutsukai.util.PlayerHelp;
import stepsword.mahoutsukai.util.Utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static stepsword.mahoutsukai.effects.projection.PowerConsolidationSpellEffect.*;

public class PowerConsolidationExtention {
    public PowerConsolidationExtention(){
    }
    public void powerConsolidation(TickEvent.LevelTickEvent event, ItemEntity item, List<ItemEntity> toDelete, List<WeaponProjectileEntity> toSpawn, int manaCost) {
        if (item.getItem().getItem() instanceof ItemBase && PowerConsolidationSpellEffect.isItemAllowed(item.getItem())) {
            boolean inMurkyWater = Utils.isInMurkyWater(item);
            boolean isAllowed = PowerConsolidationSpellEffect.isItemAllowed(item.getItem());
            boolean hasOwner = item.getOwner() != null && item.getOwner() instanceof Player;
            if (!hasOwner && inMurkyWater) {
                PlayerHelp.sendHelpMessageNearby(item.blockPosition(), item.level(), PlayerHelp.Message.RHONGOMYNIAD_NO_OWNER);
            }

            if (hasOwner && inMurkyWater) {
                Player player = (Player)item.getOwner();
                if (isAllowed) {
                        ConcurrentSet<BlockPos> connected = new ConcurrentSet();
                        boolean lt = PowerConsolidationSpellEffect.lakeThreshhold(item.blockPosition(), connected, event.level);
                        if (lt) {
                            if (PlayerManaManager.hasMana(player, manaCost)) {
                                BlockPos p = PowerConsolidationSpellEffect.findCenter(connected);
                                if (!Utils.isBlockAir(event.level, p)) {
                                    p = p.below();
                                }

                                ItemStack stack = new ItemStack((ItemLike) ModItems.GAE_BOLG.get());
                                IGaeBolgMahou mahou = UtilsExtention.getGaeBolgMahou(stack);

                                Utils.debug("Advancement Time");
                                WeaponProjectileEntity wpe = new WeaponProjectileEntity(event.level, (double)p.getX(), (double)p.getY(), (double)p.getZ(), stack);
                                wpe.setDeltaMovement(0.0, -1.0, 0.0);
                                wpe.setOwner(player);
                                toSpawn.add(wpe);
                                toDelete.add(item);
                            } else {
                                PlayerHelp.sendHelpMessage(player, PlayerHelp.Message.NOT_ENOUGH_MANA);
                            }
                        } else {
                            PlayerHelp.sendHelpMessage(player, PlayerHelp.Message.CALIBURN_LAKE_TOO_SMALL);
                        }
                } else {
                    PlayerHelp.sendHelpMessage(player, PlayerHelp.Message.RHONGOMYNIAD_LANCE_BANNED);
                }
            }
        }
    }
}