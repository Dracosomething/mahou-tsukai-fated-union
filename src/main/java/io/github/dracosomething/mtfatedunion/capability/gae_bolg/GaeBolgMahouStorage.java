package io.github.dracosomething.mtfatedunion.capability.gae_bolg;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import stepsword.mahoutsukai.capability.caliburn.ICaliburnMahou;

public class GaeBolgMahouStorage{
    private static String ATTACK_DAMAGE = "MAHOUTSUKAI_ATTACK_DAMAGE";
    private static String ATTACK_CAP = "MAHOUTSUKAI_ATTACK_CAP";

    public GaeBolgMahouStorage() {
    }

    public static Tag writeNBT(IGaeBolgMahou instance) {
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat(ATTACK_DAMAGE, instance.getAttackDamage());
        nbt.putDouble(ATTACK_CAP, instance.getInnateCap());
        return nbt;
    }

    public static void readNBT(IGaeBolgMahou instance, Tag nbtb) {
        CompoundTag nbt = (CompoundTag)nbtb;
        instance.setAttackDamage(nbt.getFloat(ATTACK_DAMAGE));
        instance.setInnateCap(nbt.getDouble(ATTACK_CAP));
    }
}
