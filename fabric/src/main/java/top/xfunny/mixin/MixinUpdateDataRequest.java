package top.xfunny.mixin;

import org.mtr.core.data.Lift;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.operation.UpdateDataResponse;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.xfunny.core.YteMain;
import top.xfunny.core.data.YteCoreLogger;
import top.xfunny.mod.Init;

@Mixin(value = UpdateDataRequest.class, remap = false)
public class MixinUpdateDataRequest {

    @Shadow
    private org.mtr.core.data.Data data;

    /**
     * 在 MTR 电梯数据更新后，清理 YTE 侧孤儿配置
     * 当 UpdateDataRequest 包含 lift 数据时触发清理
     */
    @Inject(method = "update", at = @At("TAIL"), remap = false)
    private void afterUpdate(CallbackInfoReturnable<UpdateDataResponse> cir) {
        try {
            final YteMain yteMain = Init.getYteMain();
            if (yteMain != null && data != null) {
                final ObjectSet<Lift> lifts = data.lifts;
                if (lifts != null && !lifts.isEmpty()) {
                    yteMain.reconcileAll(lifts);
                }
            }
        } catch (Exception e) {
            YteCoreLogger.error("Failed to reconcile YTE lift configs after MTR update", e);
        }
    }
}
