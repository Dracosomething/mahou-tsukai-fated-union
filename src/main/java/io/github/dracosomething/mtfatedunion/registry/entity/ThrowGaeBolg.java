package io.github.dracosomething.mtfatedunion.registry.entity;

import io.github.dracosomething.mtfatedunion.registry.MobEffects;
import io.github.dracosomething.mtfatedunion.registry.ModItems;
import io.github.dracosomething.mtfatedunion.registry.effects.gaebolgcooldown;
import io.github.dracosomething.mtfatedunion.util.Explosion;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net. minecraft. world. level. Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import stepsword.mahoutsukai.capability.mahou.PlayerManaManager;
import stepsword.mahoutsukai.config.MTConfig;
import stepsword.mahoutsukai.potion.ModEffects;
import stepsword.mahoutsukai.util.EffectUtil;
import stepsword.mahoutsukai.util.Utils;
import stepsword.mahoutsukai.entity.GateWeaponProjectileEntity;

import javax.annotation.Nullable;

import static io.github.dracosomething.mtfatedunion.registry.MobEffects.BOLG_COOLDOWN;
import static io.github.dracosomething.mtfatedunion.util.Explosion.ExplosionDamage;


public class ThrowGaeBolg extends AbstractArrow {
    private static final EntityDataAccessor<Boolean> ID_FOIL;
    private ItemStack GAE_BOLG;
    private boolean dealtDamage;
    public int Gae_bolgreturn;

    @Override
    public boolean ignoreExplosion() {
        return super.ignoreExplosion();
    }

    public ThrowGaeBolg(EntityType<? extends AbstractArrow> type, Level level) {
        super(type, level);
        this.GAE_BOLG = new ItemStack((ItemLike) ModItems.GAE_BOLG.get());
    }

    public ThrowGaeBolg(EntityType<? extends AbstractArrow> type, Level world, LivingEntity entity, ItemStack stack) {
        super(type, entity, world);
        this.GAE_BOLG = new ItemStack((ItemLike) ModItems.GAE_BOLG.get());
        this.GAE_BOLG = stack.copy();
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
                if (this.Gae_bolgreturn == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.Gae_bolgreturn;
            }
        }

        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof LocalPlayer) || !entity.isSpectator();
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
        if (entity instanceof LivingEntity livingentity) {
            f += EnchantmentHelper.getDamageBonus(this.GAE_BOLG, livingentity.getMobType());

        }

        if (EnchantmentHelper.getTagEnchantmentLevel(Enchantments.FIRE_ASPECT, this.GAE_BOLG) > 0) {
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

        for (int i = 0; i < 21; i++) {
            if (i == 20){
                execute(this.level(), p_37573_.getEntity().getX(), p_37573_.getEntity().getY(), p_37573_.getEntity().getZ());
            }
        }
    }

    @Override
    public void onHitBlock(BlockHitResult blockHitResult) {
        for (int i = 0; i <= 1; i++) {
        super.onHitBlock(blockHitResult);
            execute(this.level(), blockHitResult.getBlockPos().getX(), blockHitResult.getBlockPos().getY(), blockHitResult.getBlockPos().getZ());
        }
    }

    public void execute(LevelAccessor world, double x, double y, double z) {
        Entity entity = this.getOwner();
        if (entity instanceof Player player){
            int radius = 15;
            final int manacost = 5000;
            if (!player.level().isClientSide){
                if (PlayerManaManager.drainMana(player, manacost, false, false, true, true) == manacost) {
                    if(!EffectUtil.hasBuff(player, BOLG_COOLDOWN)) {
                        if (world instanceof Level _level && !_level.isClientSide()) {
                            Explosion explosion = new Explosion(radius, (float) x, (float) y + (float) (radius / 2 + 2), (float) z, ExplosionDamage(false, Utils.getPlayerMahou(player)));
                            explosion.explosionA(_level, player);
                            EffectUtil.buff(player, BOLG_COOLDOWN, false, 500);
                        }
                    }
                }
            }
            if (world instanceof Level _level && _level.isClientSide()) {
                Explosion explosion = new Explosion(radius, (float)x, (float)y + (float)(radius / 2 + 2), (float)z, ExplosionDamage(false, Utils.getPlayerMahou(player)));
                explosion.explosionB(_level, player);
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
        return this.GAE_BOLG.copy();
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
        if (p_37578_.contains("Gáe Bolg", 10)) {
            this.GAE_BOLG = ItemStack.of(p_37578_.getCompound("Gáe Bolg"));
        }

        this.dealtDamage = p_37578_.getBoolean("DealtDamage");
    }

    public void addAdditionalSaveData(CompoundTag p_37582_) {
        super.addAdditionalSaveData(p_37582_);
        p_37582_.put("Gáe Bolg", this.GAE_BOLG.save(new CompoundTag()));
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
        ID_FOIL = SynchedEntityData.defineId(ThrowGaeBolg.class, EntityDataSerializers.BOOLEAN);
    }
}
