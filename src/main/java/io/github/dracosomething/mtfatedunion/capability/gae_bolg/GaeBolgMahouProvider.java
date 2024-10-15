
package io.github.dracosomething.mtfatedunion.capability.gae_bolg;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import stepsword.mahoutsukai.capability.caliburn.CaliburnMahou;
import stepsword.mahoutsukai.capability.caliburn.CaliburnMahouStorage;
import stepsword.mahoutsukai.capability.caliburn.ICaliburnMahou;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GaeBolgMahouProvider implements ICapabilitySerializable<Tag> {
    private final LazyOptional<IGaeBolgMahou> mahouImpl = LazyOptional.of(GaeBolgMahou::new);
    public static Capability<IGaeBolgMahou> MAHOU = CapabilityManager.get(new CapabilityToken<IGaeBolgMahou>() {
    });

    public GaeBolgMahouProvider() {
    }

    public Tag serializeNBT() {
        return MAHOU == null ? null : GaeBolgMahouStorage.writeNBT((IGaeBolgMahou) this.mahouImpl.orElse(new GaeBolgMahou()));
    }

    public void deserializeNBT(Tag nbt) {
        this.mahouImpl.ifPresent((c) -> {
            GaeBolgMahouStorage.readNBT(c, nbt);
        });
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == MAHOU ? this.mahouImpl.cast() : LazyOptional.empty();
    }
}
