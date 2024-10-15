package io.github.dracosomething.mtfatedunion.util;

import io.github.dracosomething.mtfatedunion.registry.entity.ThrowGaeBolg;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import stepsword.mahoutsukai.capability.mahou.IMahou;
import stepsword.mahoutsukai.util.Utils;

import java.util.*;

import static stepsword.mahoutsukai.util.EffectUtil.unchangableBlock;

public class GaeMorganPain {
        private int radius;
        private float posX;
        private float posY;
        private float posZ;
        public HashMap<Player, Vec3> knockback;
        private float damage;

        public GaeMorganPain() {
            this.knockback = new HashMap();
        }

        public GaeMorganPain(int r, float x, float y, float z, float damage) {
            this();
            this.radius = r;
            this.posX = x;
            this.posY = y;
            this.posZ = z;
            this.damage = damage;
        }

        public GaeMorganPain(int r, BlockPos pos) {
            this();
            this.radius = r;
            this.posX = (float)pos.getX();
            this.posY = (float)pos.getY();
            this.posZ = (float)pos.getZ();
        }

        public void morganA(Level world, Player entity, Entity caster) {
            List<BlockPos> affected = new ArrayList();
            List<Entity> lst = this.playerKnockback(world, entity, caster);
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
            }

            Vec3 boom = new Vec3((double)this.posX, (double)this.posY, (double)this.posZ);
            Iterator var13 = lst.iterator();

            while(var13.hasNext()) {
                Entity e = (Entity)var13.next();
                this.hurt(e, boom, entity, caster);
            }
        }

        public List<Entity> playerKnockback(Level world, Player player, Entity caster) {
            AABB aabb = new AABB((double)(this.posX - (float)this.radius), (double)(this.posY - (float)this.radius), (double)(this.posZ - (float)this.radius), (double)(this.posX + (float)this.radius), (double)(this.posY + (float)this.radius), (double)(this.posZ + (float)this.radius));
            List<Entity> entities = world.getEntities((Entity)null, aabb, Entity::isAlive);
            List<Entity> ret = new ArrayList();
            new Vec3((double)this.posX, (double)this.posY, (double)this.posZ);
            Iterator var16 = entities.iterator();

            while(var16.hasNext()) {
                Entity entity = (Entity)var16.next();
                if (!entity.ignoreExplosion() && !(entity instanceof ThrowGaeBolg) && !(entity == caster)) {
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

        public void hurt(Entity entity, Vec3 boom, Player player, Entity caster) {
            double x = entity.getX();
            double y = entity.getY();
            double z = entity.getZ();
            double cmp = (double)(this.radius * this.radius) - ((double)this.posX - x) * ((double)this.posX - x) - ((double)this.posY - y) * ((double)this.posY - y) - ((double)this.posZ - z) * ((double)this.posZ - z);
            if (!(entity instanceof ThrowGaeBolg)) {
                if (entity instanceof LivingEntity) {
                    float Health = ((LivingEntity) entity).getHealth();
                    float half = Health / 2F;
                    float damage = Health - 10F;
                    ((LivingEntity) entity).setLastHurtByPlayer(player);
                    ((LivingEntity) entity).hurtTime = 100;
                    if (Health > 40){
                        ((LivingEntity) entity).setHealth(half);
                    }
                    else {
                        ((LivingEntity) entity).setHealth(damage);
                    }
                }
                caster.hurt(player.damageSources(). magic(), 10);
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
                        }
                    }
                }
            }
        }
}
