package top.xfunny.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.xfunny.mod.client.DynamicTextureCache;

@Mixin(value = org.mtr.mod.client.DynamicTextureCache.class, remap = false)
public abstract class MixinDynamicTextureCache {
    @Inject(at = @At("TAIL"),
            method = "tick",
            remap = false)
    private void afterTick(CallbackInfo ci) {
        DynamicTextureCache.instance.tick();
    }

}
