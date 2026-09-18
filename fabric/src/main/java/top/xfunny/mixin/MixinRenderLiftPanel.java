package top.xfunny.mixin;

import org.mtr.mapping.holder.PlayerEntity;
import org.mtr.mapping.mapper.PlayerHelper;
import org.mtr.mod.render.RenderLiftPanel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import top.xfunny.mod.item.YteGroupLiftButtonsLinker;
import top.xfunny.mod.item.YteLiftButtonsLinker;

@Mixin(value = RenderLiftPanel.class, remap = false)
public class MixinRenderLiftPanel {
    @ModifyVariable(
            method = "render*",
            at = @At(value = "STORE", ordinal = 0),
            name = "holdingLinker"
    )
    private boolean modifyHoldingLinker(boolean original) {
        if (original) {
            return true;
        }
        final org.mtr.mapping.holder.ClientPlayerEntity clientPlayerEntity = org.mtr.mapping.holder.MinecraftClient.getInstance().getPlayerMapped();
        if (clientPlayerEntity == null) {
            return false;
        }
        return PlayerHelper.isHolding(PlayerEntity.cast(clientPlayerEntity), item ->
                item.data instanceof YteLiftButtonsLinker || item.data instanceof YteGroupLiftButtonsLinker
        );
    }
}
