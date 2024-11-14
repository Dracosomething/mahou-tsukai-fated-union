package io.github.dracosomething.mtfatedunion.registry.entity.morgan;

import io.github.dracosomething.mtfatedunion.util.Explosion;
import io.github.dracosomething.mtfatedunion.util.GaeMorganPain;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import stepsword.mahoutsukai.capability.mahou.PlayerManaManager;
import stepsword.mahoutsukai.util.Utils;

import javax.annotation.Nullable;

import static io.github.dracosomething.mtfatedunion.util.Explosion.ExplosionDamage;


public class ThrowGaeBolgMorgan extends AbstractArrow {
    private static final EntityDataAccessor<Boolean> ID_FOIL;
    private ItemStack GaeMorganItem;
    private boolean dealtDamage;
    public int clientSideReturnGaeMorganTickCount;
    int check = 0;

    public ThrowGaeBolgMorgan(EntityType<ThrowGaeBolgMorgan> type, Level level) {
        super(type, level);
        this.GaeMorganItem = new ItemStack((ItemLike) io.github.dracosomething.mtfatedunion.registry.ModItems.GAE_BOLG_MORGAN.get());
    }

    public ThrowGaeBolgMorgan(EntityType<? extends AbstractArrow> type, Level world, LivingEntity entity, ItemStack stack) {
        super(type, entity, world);
        this.GaeMorganItem = new ItemStack((ItemLike) io.github.dracosomething.mtfatedunion.registry.ModItems.GAE_BOLG_MORGAN.get());
        this.GaeMorganItem = stack.copy();
        this.entityData.set(ID_FOIL, stack.hasFoil());
    }

    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        int i = 3;
        if (i > 0 && (this.dealtDamage || this.isNoPhysics()) && entity != null) {
            if (!this.isAcceptibleReturnOwner()) {
                if (!this.level().isClientSide && this.pickup == Pickup.ALLOWED) {
                    this.spawnAtLocation(this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else {
                this.setNoPhysics(true);
                Vec3 vec3 = entity.getEyePosition().subtract(this.position());
                this.setPosRaw(this.getX(), this.getY() + vec3.y * 0.015 * (double)i, this.getZ());
                if (this.level().isClientSide) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05 * (double)i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95).add(vec3.normalize().scale(d0)));
                if (this.clientSideReturnGaeMorganTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.clientSideReturnGaeMorganTickCount;
            }
        }

        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    public boolean isFoil() {
        return (Boolean)this.entityData.get(ID_FOIL);
    }

    @Nullable
    protected EntityHitResult findHitEntity(Vec3 p_37575_, Vec3 p_37576_) {
        return this.dealtDamage ? null : super.findHitEntity(p_37575_, p_37576_);
    }

    protected void onHitEntity(EntityHitResult p_37573_) {
        Entity entity = p_37573_.getEntity();
        float f = 14.0F;

        if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FIRE_ASPECT, this.GaeMorganItem) > 0) {
            entity.setSecondsOnFire(5);
        }

        Entity entity1 = this.getOwner();
        DamageSource damagesource = this.damageSources().trident(this, (Entity)(entity1 == null ? this : entity1));
        this.dealtDamage = true;
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        if (entity.hurt(damagesource, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity1 = (LivingEntity)entity;
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity1, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity)entity1, livingentity1);
                }

                this.doPostHurtEffects(livingentity1);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        this.playSound(soundevent, 1.0F, 1.0F);

                execute(this.level(), p_37573_.getEntity().getX(), p_37573_.getEntity().getY(), p_37573_.getEntity().getZ());
    }

    @Override
    public void onHitBlock(BlockHitResult blockHitResult) {
        for (int i = 0; i <= 21; i++) {
            if (i == 20) {
                super.onHitBlock(blockHitResult);
                execute(this.level(), blockHitResult.getBlockPos().getX(), blockHitResult.getBlockPos().getY(), blockHitResult.getBlockPos().getZ());
            }
        }
    }

    public void execute(LevelAccessor world, double x, double y, double z) {
        if (check == 0) {
            check = 1;
            Entity entity = this.getOwner();
            if (entity instanceof Player player) {
                int radius = 15;
                final int manacost = 30000;
                if (!player.level().isClientSide && PlayerManaManager.drainMana(player, manacost, false, false, true, true) == manacost) {
                    if (world instanceof Level _level && !_level.isClientSide()) {
                        GaeMorganPain gaemorganpain = new GaeMorganPain(radius, (float) x, (float) y + (float) (radius / 2 + 2), (float) z, ExplosionDamage(false, Utils.getPlayerMahou(player)));
                        gaemorganpain.morganA(_level, player, entity);
                    }
                }
                if (world instanceof Level _level && _level.isClientSide()) {
                    GaeMorganPain gaemorganpain = new GaeMorganPain(radius, (float) x, (float) y + (float) (radius / 2 + 2), (float) z, ExplosionDamage(false, Utils.getPlayerMahou(player)));
                    gaemorganpain.explosionB(_level, player);
                }
            }
        }
    }

    protected boolean tryPickup(Player player) {
        return super.tryPickup(player) || this.isNoPhysics() && this.ownedBy(player) && player.getInventory().add(this.getPickupItem());
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ID_FOIL, false);
    }

    protected ItemStack getPickupItem() {
        return this.GaeMorganItem.copy();
    }

    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    public void playerTouch(Player p_37580_) {
        if (this.ownedBy(p_37580_) || this.getOwner() == null) {
            super.playerTouch(p_37580_);
        }

    }

    public void readAdditionalSaveData(CompoundTag p_37578_) {
        super.readAdditionalSaveData(p_37578_);
        if (p_37578_.contains("Gáe Bolg Morgan", 10)) {
            this.GaeMorganItem = ItemStack.of(p_37578_.getCompound("Gáe Bolg Morgan"));
        }

        this.dealtDamage = p_37578_.getBoolean("DealtDamage");
    }

    public void addAdditionalSaveData(CompoundTag p_37582_) {
        super.addAdditionalSaveData(p_37582_);
        p_37582_.put("Gáe Bolg Morgan", this.GaeMorganItem.save(new CompoundTag()));
        p_37582_.putBoolean("DealtDamage", this.dealtDamage);
    }

    public void setEnchantmentEffectsFromEntity(LivingEntity p_36746_, float p_36747_) {
        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, p_36746_) > 0) {
            this.setSecondsOnFire(100);
        }

    }

    protected float getWaterInertia() {
        return 0.99F;
    }

    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    static {
        ID_FOIL = SynchedEntityData.defineId(ThrowGaeBolgMorgan.class, EntityDataSerializers.BOOLEAN);
    }

}
