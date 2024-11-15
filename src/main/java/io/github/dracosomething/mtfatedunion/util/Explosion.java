package io.github.dracosomething.mtfatedunion.util;

import io.github.dracosomething.mtfatedunion.Config;
import io.github.dracosomething.mtfatedunion.registry.entity.ThrowGaeBolg;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.level.ExplosionEvent;
import stepsword.mahoutsukai.capability.mahou.IMahou;
import stepsword.mahoutsukai.entity.butterfly.SafeFakePlayer;
import stepsword.mahoutsukai.item.spells.mystic.MysticStaff.FakeExplosion;
import stepsword.mahoutsukai.util.EffectUtil;
import stepsword.mahoutsukai.util.Utils;

import java.util.*;

import static stepsword.mahoutsukai.util.EffectUtil.unchangableBlock;

public class Explosion {
    private int radius;
    private float posX;
    private float posY;
    private float posZ;
    public HashMap<Player, Vec3> knockback;
    private float damage;

    public Explosion() {
        this.knockback = new HashMap();
    }

    public Explosion(int r, float x, float y, float z, float damage) {
        this();
        this.radius = r;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.damage = damage;
    }

    public Explosion(int r, BlockPos pos) {
        this();
        this.radius = r;
        this.posX = (float)pos.getX();
        this.posY = (float)pos.getY();
        this.posZ = (float)pos.getZ();
    }

    public void explosionA(Level world, Player entity) {
        List<BlockPos> affected = new ArrayList();
        List<Entity> lst = this.playerKnockback(world, entity);
            for(float x = this.posX - (float)this.radius; x < this.posX + (float)this.radius + 1.0F; ++x) {
                for(float y = this.posY - (float)this.radius; y < this.posY + (float)this.radius + 1.0F; ++y) {
                    for(float z = this.posZ - (float)this.radius; z < this.posZ + (float)this.radius + 1.0F; ++z) {
                        float cmp = (float)(this.radius * this.radius) - (this.posX - x) * (this.posX - x) - (this.posY - y) * (this.posY - y) - (this.posZ - z) * (this.posZ - z);
                        if (cmp > 0.0F) {
                            BlockPos tmp = Utils.toBlockPos(x, y, z);
                            Block b = world.getBlockState(tmp).getBlock();
                            if (!unchangableBlock(b, Arrays.asList(Blocks.CAVE_AIR, Blocks.VOID_AIR, Blocks.OBSIDIAN, Blocks.AIR)) && b.getExplosionResistance(world.getBlockState(tmp), world, tmp, (net.minecraft.world.level.Explosion) null) < 4000.0F) {
                                affected.add(tmp);
                            }
                        }
                    }
                }

            FakeExplosion(affected, entity, entity, "bakuretsu", Config.DropResources, lst);
        }

        Vec3 boom = new Vec3((double)this.posX, (double)this.posY, (double)this.posZ);
        Iterator var13 = lst.iterator();

        while(var13.hasNext()) {
            Entity e = (Entity)var13.next();
            this.hurt(e, boom, entity);
        }

    }

    public List<Entity> playerKnockback(Level world, Player player) {
        AABB aabb = new AABB((double)(this.posX - (float)this.radius), (double)(this.posY - (float)this.radius), (double)(this.posZ - (float)this.radius), (double)(this.posX + (float)this.radius), (double)(this.posY + (float)this.radius), (double)(this.posZ + (float)this.radius));
        List<Entity> entities = world.getEntities((Entity)null, aabb, Entity::isAlive);
        List<Entity> ret = new ArrayList();
        new Vec3((double)this.posX, (double)this.posY, (double)this.posZ);
        Iterator var16 = entities.iterator();

        while(var16.hasNext()) {
            Entity entity = (Entity)var16.next();
            if (!entity.ignoreExplosion() && !(entity instanceof ThrowGaeBolg)) {
                double x = entity.getX();
                double y = entity.getY();
                double z = entity.getZ();
                double cmp = (double)(this.radius * this.radius) - ((double)this.posX - x) * ((double)this.posX - x) - ((double)this.posY - y) * ((double)this.posY - y) - ((double)this.posZ - z) * ((double)this.posZ - z);
                if (cmp > 0.0) {
                    ret.add(entity);
                }
            }
        }

        return ret;
    }

    public void hurt(Entity entity, Vec3 boom, Player player) {
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        double cmp = (double)(this.radius * this.radius) - ((double)this.posX - x) * ((double)this.posX - x) - ((double)this.posY - y) * ((double)this.posY - y) - ((double)this.posZ - z) * ((double)this.posZ - z);
        float density = getBlockDensity(boom, entity);
        if (!(entity instanceof ThrowGaeBolg)) {
            entity.hurt(player.damageSources().explosion(player, (Entity) null), this.damage * density);
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).setLastHurtByPlayer(player);
                ((LivingEntity) entity).hurtTime = 100;
            }
        }

        Vec3 kb = (new Vec3(x, y, z)).add(new Vec3((double)(-this.posX), (double)(-this.posY), (double)(-this.posZ))).normalize().scale(Math.sqrt(cmp) / 4.0);
        double motionX = entity.getDeltaMovement().x;
        double motionY = entity.getDeltaMovement().y;
        double motionZ = entity.getDeltaMovement().z;
        motionX += kb.x;
        motionY += kb.y;
        motionZ += kb.z;
        entity.setDeltaMovement(motionX, motionY, motionZ);
        if (entity instanceof Player) {
            this.knockback.put((Player)entity, kb);
        }

    }

    public void explosionB(Level world, Player player) {
        Random rand = Utils.getRandom(world);
        world.playSound((Player)null, (double)this.posX, (double)this.posY, (double)this.posZ, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (rand.nextFloat() - rand.nextFloat()) * 0.2F) * 0.7F);

        for(float x = this.posX - (float)this.radius; x < this.posX + (float)this.radius + 1.0F; ++x) {
            for(float y = this.posY - (float)this.radius; y < this.posY + (float)this.radius + 1.0F; ++y) {
                for(float z = this.posZ - (float)this.radius; z < this.posZ + (float)this.radius + 1.0F; ++z) {
                    float cmp = (float)(this.radius * this.radius) - (this.posX - x) * (this.posX - x) - (this.posY - y) * (this.posY - y) - (this.posZ - z) * (this.posZ - z);
                    if (cmp > 0.0F && cmp < 6.1F && world instanceof ClientLevel) {
                        world.addParticle(ParticleTypes.SONIC_BOOM, true, (double) x, (double) y, (double) z, 5.0, 5.0, 5.0);
                        world.addParticle(ParticleTypes.EXPLOSION_EMITTER, true, (double) x, (double) y, (double) z, 5.0, 2.0, 2.0);
                    }
                }
            }
        }
    }

    public static float getBlockDensity(Vec3 explosionVector, Entity entity) {
        AABB AABB = entity.getBoundingBox();
        double d0 = 1.0 / ((AABB.maxX - AABB.minX) * 2.0 + 1.0);
        double d1 = 1.0 / ((AABB.maxY - AABB.minY) * 2.0 + 1.0);
        double d2 = 1.0 / ((AABB.maxZ - AABB.minZ) * 2.0 + 1.0);
        double d3 = (1.0 - Math.floor(1.0 / d0) * d0) / 2.0;
        double d4 = (1.0 - Math.floor(1.0 / d2) * d2) / 2.0;
        if (!(d0 < 0.0) && !(d1 < 0.0) && !(d2 < 0.0)) {
            int i = 0;
            int j = 0;

            for(float f = 0.0F; f <= 1.0F; f = (float)((double)f + d0)) {
                for(float f1 = 0.0F; f1 <= 1.0F; f1 = (float)((double)f1 + d1)) {
                    for(float f2 = 0.0F; f2 <= 1.0F; f2 = (float)((double)f2 + d2)) {
                        double d5 = Mth.lerp((double)f, AABB.minX, AABB.maxX);
                        double d6 = Mth.lerp((double)f1, AABB.minY, AABB.maxY);
                        double d7 = Mth.lerp((double)f2, AABB.minZ, AABB.maxZ);
                        Vec3 Vec3 = new Vec3(d5 + d3, d6, d7 + d4);
                        if (entity.level().clip(new ClipContext(Vec3, explosionVector, net.minecraft.world.level.ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, entity)).getType() == HitResult.Type.MISS) {
                            ++i;
                        }

                        ++j;
                    }
                }
            }

            return (float)i / (float)j;
        } else {
            return 0.0F;
        }
    }

    public static float ExplosionDamage(boolean aoe, IMahou mahou) {
        float factor;
        boolean scales;
            factor = (float) Config.DamageScaleFactor;
            scales = true;

        return scales && mahou != null ? (float)mahou.getMaxMana() * factor : factor;
    }

    public static void FakeExplosion(List<BlockPos> affected, Entity target, Player caster, String name, boolean drop, List<Entity> entities) {
        if (!target.level().isClientSide) {
            Object player;
            if (caster == null) {
                player = new SafeFakePlayer((ServerLevel)target.level(), name);
            } else {
                player = caster;
            }

            FakeExplosion explosion = new FakeExplosion(target.level(), (Entity)player, target.getX(), target.getY(), target.getZ(), 10.0F, affected);
            ExplosionEvent.Detonate ee = new ExplosionEvent.Detonate(target.level(), explosion, entities);
            ForgeEventFactory.onExplosionDetonate(target.level(), explosion, entities, 10.0);
            if (!ee.isCanceled()) {
                Iterator var9 = ee.getAffectedBlocks().iterator();

                while(var9.hasNext()) {
                    BlockPos p = (BlockPos)var9.next();
                    BlockState bs = target.level().getBlockState(p);
                    if (!unchangableBlock(bs.getBlock()) && !bs.isAir()) {
                        if (drop) {
                            Block.dropResources(bs, target.level(), p, target.level().getBlockEntity(p));
                        }

                        target.level().destroyBlock(p, false);
                        target.level().setBlockAndUpdate(p, Blocks.AIR.defaultBlockState());
                    }
                }
            }
        }
    }
}
