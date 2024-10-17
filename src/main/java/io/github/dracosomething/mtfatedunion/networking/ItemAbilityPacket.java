package io.github.dracosomething.mtfatedunion.networking;

import io.github.dracosomething.mtfatedunion.capability.gae_bolg.IGaeBolgMahou;
import io.github.dracosomething.mtfatedunion.registry.item.gae_bolg;
import io.github.dracosomething.mtfatedunion.util.UtilsExtention;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkEvent;
import stepsword.mahoutsukai.capability.caliburn.ICaliburnMahou;
import stepsword.mahoutsukai.capability.mahou.PlayerManaManager;
import stepsword.mahoutsukai.config.MTConfig;
import stepsword.mahoutsukai.entity.SmiteEntity;
import stepsword.mahoutsukai.item.spells.projection.PowerConsolidation.Caliburn;
import stepsword.mahoutsukai.potion.ModEffects;
import stepsword.mahoutsukai.util.EffectUtil;
import stepsword.mahoutsukai.util.Utils;

import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class ItemAbilityPacket {
    ABILITY ability;

    public ItemAbilityPacket() {
    }

    public ItemAbilityPacket(ABILITY a) {
        this.ability = a;
    }

    public void fromBytes(ByteBuf buf) {
        int n = buf.readInt();
        if (n < ABILITY.values().length) {
            this.ability = ABILITY.values()[n];
        } else {
            this.ability = ABILITY.GAE_BOLG;
        }

    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.ability.ordinal());
    }

    public static void encode(io.github.dracosomething.mtfatedunion.networking.ItemAbilityPacket msg, FriendlyByteBuf buf) {
        msg.toBytes(buf);
    }

    public static io.github.dracosomething.mtfatedunion.networking.ItemAbilityPacket decode(FriendlyByteBuf buf) {
        io.github.dracosomething.mtfatedunion.networking.ItemAbilityPacket msg = new io.github.dracosomething.mtfatedunion.networking.ItemAbilityPacket();
        msg.fromBytes(buf);
        return msg;
    }

    public static void handle(final io.github.dracosomething.mtfatedunion.networking.ItemAbilityPacket message, final Supplier<NetworkEvent.Context> context) {
        ((NetworkEvent.Context)context.get()).enqueueWork(new Runnable() {
            public void run() {
                if (((NetworkEvent.Context)context.get()).getSender() instanceof ServerPlayer) {
                    if (message.ability == ABILITY.GAE_BOLG) {
                        handleAbilityCaliburn(((NetworkEvent.Context)context.get()).getSender());
                    }
                }
            }
        });
        ((NetworkEvent.Context)context.get()).setPacketHandled(true);
    }

    public static void handleAbilityCaliburn(ServerPlayer player) {
        int radius = MTConfig.POWER_CONSOLIDATION_SMITE_RADIUS;
        AABB aabb = new AABB(player.position().add((double)(-radius), -4.0, (double)(-radius)), player.position().add((double)radius, 4.0, (double)radius));
        ItemStack stack = player.getMainHandItem();
        if (stack.getItem() instanceof gae_bolg && !EffectUtil.hasBuff(player, ModEffects.CM_COOLDOWN) && PlayerManaManager.drainMana(player, MTConfig.CALIBURN_MORGAN_ABILITY_MANA_COST, false, false) == MTConfig.CALIBURN_MORGAN_ABILITY_MANA_COST) {
            IGaeBolgMahou mahou = UtilsExtention.getGaeBolgMahou(stack);
            if (mahou != null) {
                int targets = 0;
                List<LivingEntity> lst = player.level().getEntitiesOfClass(LivingEntity.class, aabb);
                Iterator var7 = lst.iterator();

                LivingEntity target;
                while(var7.hasNext()) {
                    target = (LivingEntity)var7.next();
                    if (!target.getUUID().equals(player.getUUID()) && Caliburn.specialTarget(target)) {
                        ++targets;
                    }
                }

                var7 = lst.iterator();

                while(var7.hasNext()) {
                    target = (LivingEntity)var7.next();
                    if (!target.getUUID().equals(player.getUUID()) && Caliburn.specialTarget(target)) {
                        SmiteEntity smite = new SmiteEntity(target.level(), target, 0.9019608F, 0.9019608F, 0.16470589F, 1.0F, 0.1F, mahou.getAttackDamage() / (float)targets);
                        smite.setPos(target.getX(), target.getY(), target.getZ());
                        target.level().addFreshEntity(smite);
                    }
                }

                EffectUtil.buff(player, ModEffects.CM_COOLDOWN, false, MTConfig.MORGAN_CALIBURN_POWER_COOLDOWN);
            }
        }

    }
    public static enum ABILITY {
        MORGAN,
        GAE_BOLG,
        REPLICA;

        private ABILITY() {
        }
    }
}
