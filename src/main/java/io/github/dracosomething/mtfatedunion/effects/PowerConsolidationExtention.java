package io.github.dracosomething.mtfatedunion.effects;

import io.github.dracosomething.mtfatedunion.registry.creativetabs.MahouAddonTab;
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
import stepsword.mahoutsukai.capability.mahou.PlayerManaManager;
import stepsword.mahoutsukai.config.MTConfig;
import stepsword.mahoutsukai.config.MandatoryFun;
import stepsword.mahoutsukai.entity.WeaponProjectileEntity;
import stepsword.mahoutsukai.handlers.ServerHandler;
import stepsword.mahoutsukai.item.ModItems;
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

    public static void powerConsolidationWorldTick(final TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.level.isClientSide && ServerHandler.tickCounter % (long) MTConfig.POWER_CONSOLIDATION_LAKE_CYCLE == 0L) {
            Entity failed = null;

            try {
                final int manaCost = 5000;
                Iterator<Entity> iter = ((ServerLevel) event.level).getEntities().getAll().iterator();
                final List<ItemEntity> toDelete = new ArrayList();
                final List<WeaponProjectileEntity> toSpawn = new ArrayList();

                while (true) {
                    Entity e;
                    do {
                        if (!iter.hasNext()) {
                            if (toDelete.size() > 0) {
                                ((ServerLevel) event.level).getServer().execute(new Runnable() {
                                    public void run() {
                                        for (int i = 0; i < toDelete.size(); ++i) {
                                            ItemEntity e = (ItemEntity) toDelete.get(i);
                                            boolean var10000 = e.isAlive();
                                            Utils.debug("Item alive:" + var10000 + "... Thrower:" + e.getOwner());
                                            if (e.isAlive() && e.getOwner() instanceof Player && i < toSpawn.size() && PlayerManaManager.drainMana((Player) e.getOwner(), manaCost, false, false) == manaCost) {
                                                Utils.debug("Mana charged and item spawning.");
                                                event.level.addFreshEntity((Entity) toSpawn.get(i));
                                                e.discard();
                                            }
                                        }

                                    }
                                });
                            }

                            return;
                        }

                        e = (Entity) iter.next();
                    } while (!(e instanceof ItemEntity));

                    ItemEntity item = (ItemEntity) e;
                    if (item.getItem().getItem() instanceof SwordItem) {
                        boolean inMurkyWater = Utils.isInMurkyWater(e);
                        boolean isAllowed = isItemAllowed(item.getItem());
                        boolean isEnchanted = item.getItem().isEnchanted();
                        boolean hasOwner = item.getOwner() != null && item.getOwner() instanceof Player;
                        if (!hasOwner && inMurkyWater) {
                            PlayerHelp.sendHelpMessageNearby(e.blockPosition(), e.level(), PlayerHelp.Message.CALIBURN_NO_OWNER);
                        }

                        if (hasOwner && inMurkyWater) {
                            Player player = (Player) item.getOwner();
                            if (!isAllowed) {
                                PlayerHelp.sendHelpMessage(player, PlayerHelp.Message.CALIBURN_SWORD_BANNED);
                            } else if (!isEnchanted) {
                                PlayerHelp.sendHelpMessage(player, PlayerHelp.Message.CALIBURN_NOT_ENCHANTED);
                            } else {
                                ConcurrentSet<BlockPos> connected = new ConcurrentSet();
                                boolean lt = lakeThreshhold(item.blockPosition(), connected, event.level);
                                if (!lt) {
                                    PlayerHelp.sendHelpMessage(player, PlayerHelp.Message.CALIBURN_LAKE_TOO_SMALL);
                                } else {
                                    ItemStack stackCopy;
                                    if (MandatoryFun.meetsAllFunReqs(player)) {
                                        if (PlayerManaManager.hasMana(player, manaCost)) {
                                            BlockPos p = findCenter(connected);
                                            if (!Utils.isBlockAir(event.level, p)) {
                                                p = p.below();
                                            }

                                            ItemStack stack = new ItemStack((ItemLike) io.github.dracosomething.mtfatedunion.registry.ModItems.GAE_BOLG.get());
                                            ICaliburnMahou mahou = Utils.getCaliburnMahou(stack);
                                            if (mahou != null) {
                                                double lb = getLimitBreakInArea(item);

                                                WeaponProjectileEntity wpe = new WeaponProjectileEntity(event.level, (double) p.getX(), (double) p.getY(), (double) p.getZ(), stack);
                                                wpe.setDeltaMovement(0.0, -1.0, 0.0);
                                                wpe.setOwner(player);
                                                toSpawn.add(wpe);
                                                toDelete.add(item);
                                            } else {
                                                PlayerHelp.sendHelpMessage(player, PlayerHelp.Message.NOT_ENOUGH_MANA);
                                            }
                                        } else {
                                            IItemHandler inventory = Utils.getInventory(player);
                                            int sizeInventory = inventory.getSlots();
                                            ItemStack stack = new ItemStack((ItemLike) ModItems.funbook.get());
                                            boolean hasBook = false;
                                            boolean hasSpace = false;

                                            for (int i = 0; i < sizeInventory; ++i) {
                                                stackCopy = inventory.getStackInSlot(i);
                                                if (ItemStack.isSameItem(stackCopy, stack)) {
                                                    hasBook = true;
                                                }

                                                if (stackCopy.isEmpty()) {
                                                    hasSpace = true;
                                                }
                                            }

                                            if (!hasBook && hasSpace) {
                                                player.addItem(stack);
                                            } else if (!hasSpace && !hasBook) {
                                                PlayerHelp.sendHelpMessage(player, PlayerHelp.Message.NO_FUN_NO_SPACE);
                                            }

                                            PlayerHelp.sendHelpMessage(player, PlayerHelp.Message.NOT_ENOUGH_FUN);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception var26) {
                Exception e = var26;
                Utils.err(e.toString());
                if (failed != null) {
                    PlayerHelp.sendHelpMessageNearby(((Entity)failed).blockPosition(), ((Entity)failed).level(), PlayerHelp.Message.CALIBURN_INTERNAL_ERROR);
                }
            }
        }
    }
}