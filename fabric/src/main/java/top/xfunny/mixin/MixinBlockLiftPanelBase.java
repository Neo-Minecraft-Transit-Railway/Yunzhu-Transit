package top.xfunny.mixin;

import org.mtr.mapping.holder.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.xfunny.mod.Items;

@Mixin(value = org.mtr.mod.block.BlockLiftPanelBase.class, remap = false)
public abstract class MixinBlockLiftPanelBase {

    @Inject(
            method = "onUse2",
            at = @At("HEAD"),
            cancellable = true
    )
    private void injectOnUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (world.isClient()) {
            cir.setReturnValue(ActionResult.SUCCESS);
        } else {
            if (player.isHolding(org.mtr.mod.Items.LIFT_BUTTONS_LINK_CONNECTOR.get())
                    || player.isHolding(org.mtr.mod.Items.LIFT_BUTTONS_LINK_REMOVER.get())
                    || player.isHolding(Items.YTE_LIFT_BUTTONS_LINK_CONNECTOR.get())
                    || player.isHolding(Items.YTE_LIFT_BUTTONS_LINK_REMOVER.get())
                    || player.isHolding(Items.YTE_GROUP_LIFT_BUTTONS_LINK_CONNECTOR.get())
                    || player.isHolding(Items.YTE_GROUP_LIFT_BUTTONS_LINK_REMOVER.get())) {
                cir.setReturnValue(ActionResult.PASS);
            } else {
                cir.setReturnValue(ActionResult.FAIL);
            }
        }
    }
}
