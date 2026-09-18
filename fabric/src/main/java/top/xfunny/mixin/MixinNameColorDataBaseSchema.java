package top.xfunny.mixin;

import org.mtr.core.data.Data;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = org.mtr.core.generated.data.NameColorDataBaseSchema.class, remap = false)
public interface MixinNameColorDataBaseSchema {
    @Accessor("data")
    Data getData();
}
