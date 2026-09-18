package top.xfunny.mixin;

import org.mtr.core.data.Data;
import org.mtr.core.data.Position;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = org.mtr.core.data.Lift.class, remap = false)
public interface MixinLiftFields {
    @Accessor("needsUpdate")
    boolean getNeedsUpdate();

    @Accessor("needsUpdate")
    void setNeedsUpdate(boolean value);

    @Accessor("isClientside")
    boolean isClientside();

    @Accessor("minPosition")
    Position getMinPosition();

    @Accessor("maxPosition")
    Position getMaxPosition();

    @Invoker("getProgress")
    long invokeGetProgress(int floor);
}
