package top.xfunny.mixin;

import org.mtr.core.data.LiftFloor;
import org.mtr.core.data.LiftInstruction;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = org.mtr.core.generated.data.LiftSchema.class, remap = false)
public interface MixinLiftSchema {
    @Accessor("speed")
    double getSpeed();

    @Accessor("speed")
    void setSpeed(double speed);

    @Accessor("railProgress")
    double getRailProgress();

    @Accessor("railProgress")
    void setRailProgress(double progress);

    @Accessor("stoppingCoolDown")
    long getStoppingCoolDown();

    @Accessor("stoppingCoolDown")
    void setStoppingCoolDown(long coolDown);

    @Accessor("instructions")
    ObjectArrayList<LiftInstruction> getInstructions();

    @Accessor("floors")
    ObjectArrayList<LiftFloor> getFloors();
}