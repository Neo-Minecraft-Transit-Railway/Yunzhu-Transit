package top.xfunny.mixin;

import org.mtr.mapping.holder.Item;
import org.mtr.mapping.holder.PlayerEntity;
import org.mtr.mod.block.BlockLiftButtons;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import top.xfunny.mod.Items;

@Mixin(value = BlockLiftButtons.class, remap = false)
public abstract class MixinBlockLiftButtons {

    @Redirect(
            method = "onUse2",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/mtr/mapping/holder/PlayerEntity;isHolding(Lorg/mtr/mapping/holder/Item;)Z"
            )
    )
    private boolean redirectIsHolding(PlayerEntity player, Item item) {
        // 先执行原版检查
        final boolean original = player.isHolding(item);

        // 如果原版检查通过则直接返回
        if (original) {
            return true;
        }

        // 否则检查我们的特殊物品
        return player.isHolding(Items.YTE_LIFT_BUTTONS_LINK_CONNECTOR.get()) ||
                player.isHolding(Items.YTE_LIFT_BUTTONS_LINK_REMOVER.get()) ||
                player.isHolding(Items.YTE_GROUP_LIFT_BUTTONS_LINK_CONNECTOR.get()) ||
                player.isHolding(Items.YTE_GROUP_LIFT_BUTTONS_LINK_REMOVER.get());
    }
}