package top.xfunny.mixin;

import org.mtr.mapping.holder.*;
import org.mtr.mod.block.BlockLiftButtons;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.xfunny.mod.Items;

/**
 * Allow YTE (and MTR) lift linkers to reach the item use path on 1.21.
 * Uses HEAD inject instead of redirecting {@code PlayerEntity.isHolding}, so MTR refactors
 * that drop those call sites no longer crash the game.
 */
@Mixin(value = BlockLiftButtons.class, remap = false)
public abstract class MixinBlockLiftButtons {

	@Inject(method = "onUse2", at = @At("HEAD"), cancellable = true)
	private void injectOnUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
		if (player.isHolding(org.mtr.mod.Items.LIFT_BUTTONS_LINK_CONNECTOR.get())
				|| player.isHolding(org.mtr.mod.Items.LIFT_BUTTONS_LINK_REMOVER.get())
				|| player.isHolding(Items.YTE_LIFT_BUTTONS_LINK_CONNECTOR.get())
				|| player.isHolding(Items.YTE_LIFT_BUTTONS_LINK_REMOVER.get())
				|| player.isHolding(Items.YTE_GROUP_LIFT_BUTTONS_LINK_CONNECTOR.get())
				|| player.isHolding(Items.YTE_GROUP_LIFT_BUTTONS_LINK_REMOVER.get())) {
			// FAIL → MTR BlockExtension.useItemOn remaps to PASS so the held linker item runs.
			cir.setReturnValue(ActionResult.FAIL);
		}
	}
}
