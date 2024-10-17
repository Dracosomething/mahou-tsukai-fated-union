package io.github.dracosomething.mtfatedunion.effects;

import io.github.dracosomething.mtfatedunion.capability.gae_bolg.IGaeBolgMahou;
import io.github.dracosomething.mtfatedunion.registry.item.curruid_bone;
import io.github.dracosomething.mtfatedunion.registry.item.gae_bolg;
import io.github.dracosomething.mtfatedunion.util.UtilsExtention;
import io.netty.util.internal.ConcurrentSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.IItemHandler;
import stepsword.mahoutsukai.advancements.ModTriggers;
import stepsword.mahoutsukai.block.ModBlocks;
import stepsword.mahoutsukai.capability.chunks.IChunkMahou;
import stepsword.mahoutsukai.capability.mahou.PlayerManaManager;
import stepsword.mahoutsukai.config.MTConfig;
import stepsword.mahoutsukai.config.MandatoryFun;
import stepsword.mahoutsukai.effects.projection.ProjectionSpellEffect;
import stepsword.mahoutsukai.entity.WeaponProjectileEntity;
import stepsword.mahoutsukai.fluids.MurkyWaterBlock;
import stepsword.mahoutsukai.handlers.ServerHandler;
import stepsword.mahoutsukai.item.ModItems;
import stepsword.mahoutsukai.item.morgan.Morgan;
import stepsword.mahoutsukai.item.spells.projection.PowerConsolidation.Caliburn;
import stepsword.mahoutsukai.networking.ChunkMahouRequestPacket;
import stepsword.mahoutsukai.networking.PacketHandler;
import stepsword.mahoutsukai.networking.SweepAttackPacket;
import stepsword.mahoutsukai.util.EffectUtil;
import stepsword.mahoutsukai.util.PlayerHelp;
import stepsword.mahoutsukai.util.Utils;
import stepsword.mahoutsukai.util.PlayerHelp.Message;

public class PowerConsolidationExtention {
    public PowerConsolidationExtention() {
    }

    @SubscribeEvent
    public static void PCWorldTick(final TickEvent.LevelTickEvent event) {
        if (event.phase == Phase.END && !event.level.isClientSide && ServerHandler.tickCounter % (long)MTConfig.POWER_CONSOLIDATION_LAKE_CYCLE == 0L) {
            Entity failed = null;

            try {
                final int manaCost = MTConfig.POWER_CONSOLIDATION_SWORD_MANA_COST;
                double nerfFactor = MTConfig.POWER_CONSOLIDATION_NERF_FACTOR;
                Iterator<Entity> iter = ((ServerLevel)event.level).getEntities().getAll().iterator();
                final List<ItemEntity> toDelete = new ArrayList();
                final List<WeaponProjectileEntity> toSpawn = new ArrayList();

                while(true) {
                    Entity e;
                    do {
                        if (!iter.hasNext()) {
                            if (!toDelete.isEmpty()) {
                                ((ServerLevel)event.level).getServer().execute(new Runnable() {
                                    public void run() {
                                        for(int i = 0; i < toDelete.size(); ++i) {
                                            ItemEntity e = (ItemEntity)toDelete.get(i);
                                            boolean var10000 = e.isAlive();
                                            Utils.debug("Item alive:" + var10000 + "... Thrower:" + e.getOwner());
                                            if (e.isAlive() && e.getOwner() instanceof Player && i < toSpawn.size() && PlayerManaManager.drainMana((Player)e.getOwner(), manaCost, false, false) == manaCost) {
                                                Utils.debug("Mana charged and item spawning.");
                                                event.level.addFreshEntity((Entity)toSpawn.get(i));
                                                e.discard();
                                            }
                                        }

                                    }
                                });
                            }

                            return;
                        }

                        e = (Entity)iter.next();
                    } while(!(e instanceof ItemEntity));

                    ItemEntity item = (ItemEntity)e;
                    if (item.getItem().getItem() instanceof curruid_bone) {
                        boolean inMurkyWater = Utils.isInMurkyWater(e);
                        boolean isAllowed = isItemAllowed(item.getItem());
                        boolean isEnchanted = item.getItem().isEnchanted();
                        boolean hasOwner = item.getOwner() != null && item.getOwner() instanceof Player;
                        if (!hasOwner && inMurkyWater) {
                            PlayerHelp.sendHelpMessageNearby(e.blockPosition(), e.level(), Message.CALIBURN_NO_OWNER);
                        }

                        if (hasOwner && inMurkyWater) {
                            Player player = (Player)item.getOwner();
                            if (!isAllowed) {
                                PlayerHelp.sendHelpMessage(player, Message.CALIBURN_SWORD_BANNED);
                            } else {
                                ConcurrentSet<BlockPos> connected = new ConcurrentSet();
                                boolean lt = lakeThreshhold(item.blockPosition(), connected, event.level);
                                if (!lt) {
                                    PlayerHelp.sendHelpMessage(player, Message.CALIBURN_LAKE_TOO_SMALL);
                                } else {
                                    ItemStack stackCopy;
                                    if (MandatoryFun.meetsAllFunReqs(player)) {
                                        if (PlayerManaManager.hasMana(player, manaCost)) {
                                            BlockPos p = findCenter(connected);
                                            if (!Utils.isBlockAir(event.level, p)) {
                                                p = p.below();
                                            }

                                            ItemStack stack = new ItemStack((ItemLike) io.github.dracosomething.mtfatedunion.registry.ModItems.GAE_BOLG.get());
                                            IGaeBolgMahou mahou = UtilsExtention.getGaeBolgMahou(stack);

                                            WeaponProjectileEntity wpe = new WeaponProjectileEntity(event.level, (double)p.getX(), (double)p.getY(), (double)p.getZ(), stack);
                                            wpe.setDeltaMovement(0.0, -1.0, 0.0);
                                            wpe.setOwner(player);
                                            toSpawn.add(wpe);
                                            toDelete.add(item);
                                        } else {
                                            PlayerHelp.sendHelpMessage(player, Message.NOT_ENOUGH_MANA);
                                        }
                                    } else {
                                        IItemHandler inventory = Utils.getInventory(player);
                                        int sizeInventory = inventory.getSlots();
                                        ItemStack stack = new ItemStack((ItemLike) ModItems.funbook.get());
                                        boolean hasBook = false;
                                        boolean hasSpace = false;

                                        for(int i = 0; i < sizeInventory; ++i) {
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
                                            PlayerHelp.sendHelpMessage(player, Message.NO_FUN_NO_SPACE);
                                        }

                                        PlayerHelp.sendHelpMessage(player, Message.NOT_ENOUGH_FUN);
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
                    PlayerHelp.sendHelpMessageNearby(((Entity)failed).blockPosition(), ((Entity)failed).level(), Message.CALIBURN_INTERNAL_ERROR);
                }
            }
        }

    }

    public static double getLimitBreakInArea(ItemEntity entity) {
        Vec3 vec = entity.position();
        int radius = 10;
        AABB aabb = new AABB(vec.x - (double)radius, vec.y - (double)radius, vec.z - (double)radius, vec.x + (double)radius, vec.y + (double)radius, vec.z + (double)radius);
        int limit_breaks = 0;
        List<ItemEntity> entities = entity.level().getEntitiesOfClass(ItemEntity.class, aabb);
        Iterator var6 = entities.iterator();

        while(var6.hasNext()) {
            ItemEntity e = (ItemEntity)var6.next();
            if (EffectUtil.inItemBlacklist(e.getItem().getItem(), MTConfig.POWER_CONSOLIDATION_TIER_UPGRADE_ITEM)) {
                limit_breaks += e.getItem().getCount();
                e.discard();
            }
        }

        double ret = (double)MTConfig.POWER_CONSOLIDATION_ATTACK_CAP;
        if (MTConfig.POWER_CONSOLIDATION_TIERS.size() > 0) {
            ret = (Double)MTConfig.POWER_CONSOLIDATION_TIERS.get(0);
            if (limit_breaks > 0) {
                ret = (Double)MTConfig.POWER_CONSOLIDATION_TIERS.get(Math.min(MTConfig.POWER_CONSOLIDATION_TIERS.size() - 1, limit_breaks));
            }
        }

        return ret;
    }

    public static boolean isItemAllowed(ItemStack s) {
        boolean blacklist = false;
        ArrayList<String> list = (ArrayList)MTConfig.POWER_CONSOLIDATION_BLACKLIST;
        boolean match = false;

        for(int i = 0; i < list.size(); ++i) {
            if (list.get(i) != null && s != null && Utils.getRegistryKey(s.getItem()) != null && ((String)list.get(i)).equals(Utils.getRegistryName(s.getItem()))) {
                match = true;
                break;
            }
        }

        if (s != null && !MTConfig.POWER_CONSOLIDATION_PROJECTIONS_ALLOWED && ProjectionSpellEffect.getProjected(s) && s.getItem() instanceof gae_bolg) {
            return false;
        } else if (match && blacklist) {
            return false;
        } else if (match && !blacklist) {
            return true;
        } else {
            return !match && blacklist;
        }
    }

    public static BlockPos findCenter(ConcurrentSet<BlockPos> connected) {
        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int minZ = Integer.MAX_VALUE;
        int maxZ = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;
        Iterator var6 = connected.iterator();

        while(var6.hasNext()) {
            BlockPos p = (BlockPos)var6.next();
            if (p.getX() < minX) {
                minX = p.getX();
            }

            if (p.getX() > maxX) {
                maxX = p.getX();
            }

            if (p.getZ() < minZ) {
                minZ = p.getZ();
            }

            if (p.getZ() > maxZ) {
                maxZ = p.getZ();
            }

            if (p.getY() > maxY) {
                maxY = p.getY();
            }
        }

        return new BlockPos(average(maxX, minX), maxY + 1, average(maxZ, minZ));
    }

    public static int average(int a, int b) {
        return (a + b) / 2;
    }

    public static boolean lakeThreshhold(BlockPos p, ConcurrentSet<BlockPos> connected, Level world) {
        int threshhold = MTConfig.POWER_CONSOLIDATION_LAKE_THRESHHOLD;
        if (connected.size() > threshhold) {
            return true;
        } else if (connected.contains(p)) {
            return false;
        } else {
            boolean ret = false;
            if (world != null) {
                if (world.getBlockState(p).getBlock() instanceof MurkyWaterBlock) {
                    connected.add(p);
                }

                if (world.getBlockState(p.above()).getBlock() instanceof MurkyWaterBlock) {
                    ret = lakeThreshhold(p.above(), connected, world);
                    if (ret) {
                        return true;
                    }
                }

                if (world.getBlockState(p.below()).getBlock() instanceof MurkyWaterBlock) {
                    ret = lakeThreshhold(p.below(), connected, world);
                    if (ret) {
                        return true;
                    }
                }

                if (world.getBlockState(p.south()).getBlock() instanceof MurkyWaterBlock) {
                    ret = lakeThreshhold(p.south(), connected, world);
                    if (ret) {
                        return true;
                    }
                }

                if (world.getBlockState(p.west()).getBlock() instanceof MurkyWaterBlock) {
                    ret = lakeThreshhold(p.west(), connected, world);
                    if (ret) {
                        return true;
                    }
                }

                if (world.getBlockState(p.east()).getBlock() instanceof MurkyWaterBlock) {
                    ret = lakeThreshhold(p.east(), connected, world);
                    if (ret) {
                        return true;
                    }
                }

                if (world.getBlockState(p.north()).getBlock() instanceof MurkyWaterBlock) {
                    ret = lakeThreshhold(p.north(), connected, world);
                    if (ret) {
                        return true;
                    }
                }
            }

            return ret;
        }
    }

    public static void caliburnLeftClickSpace(PlayerInteractEvent.LeftClickEmpty event) {
        sweepAttack(event);
    }

    public static void caliburnLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        sweepAttack(event);
    }

    public static void sweepAttack(PlayerInteractEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide && (player.getMainHandItem().getItem() instanceof Caliburn || player.getMainHandItem().getItem() instanceof Morgan)) {
            int r = MTConfig.POWER_CONSOLIDATION_SWEEP_RADIUS;
            int r2 = MTConfig.POWER_CONSOLIDATION_SWEEP_LOOK_RADIUS;
            Vec3 p = player.position();
            AABB aabb = new AABB(p.add((double)(-r), (double)(-r), (double)(-r)), p.add((double)r, (double)r, (double)r));
            List<LivingEntity> lst = player.level().getEntitiesOfClass(LivingEntity.class, aabb);
            Iterator var7 = lst.iterator();

            while(var7.hasNext()) {
                LivingEntity e = (LivingEntity)var7.next();
                if (!e.getUUID().equals(player.getUUID()) && EffectUtil.isLookingAtMe(e, player) && Math.abs(EffectUtil.pointToLineDistance(e.position(), player.getEyePosition(1.0F), player.getLookAngle().add(player.getEyePosition(1.0F)))) < (double)r2) {
                    PacketHandler.sendToServer(new SweepAttackPacket(e));
                }
            }
        }

    }

    public static void powerConsolidationChunkLoad(ChunkEvent.Load event) {
        if (event.getChunk() instanceof LevelChunk) {
            LevelChunk c = (LevelChunk)event.getChunk();
            if (event.getLevel().isClientSide()) {
                PacketHandler.sendToServer(new ChunkMahouRequestPacket(c.getPos().x, c.getPos().z));
            } else if (MTConfig.POWER_CONSOLIDATION_RETRO_FOG_FIX) {
                IChunkMahou mahou = Utils.getChunkMahou(c);
                if (mahou != null && mahou.needsRetroFixLake()) {
                    ConcurrentSet<BlockPos> lakeBlocks = mahou.getLakeBlocks();

                    for(int i = c.getPos().getMinBlockX(); i <= c.getPos().getMaxBlockX(); ++i) {
                        for(int j = c.getPos().getMinBlockZ(); j <= c.getPos().getMaxBlockZ(); ++j) {
                            for(int y = c.getLevel().getMinBuildHeight(); y < c.getLevel().getMaxBuildHeight(); ++y) {
                                BlockPos p = new BlockPos(i, y, j);
                                if (c.getBlockState(p).getBlock() == ModBlocks.murkyFluid.get()) {
                                    lakeBlocks.add(p);
                                }
                            }
                        }
                    }

                    mahou.setNeedsRetroFixLake(false);
                }
            }
        }

    }
}