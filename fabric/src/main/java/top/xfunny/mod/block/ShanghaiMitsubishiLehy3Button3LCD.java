package top.xfunny.mod.block;

import org.jetbrains.annotations.NotNull;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.IBlock;
import top.xfunny.mod.BlockEntityTypes;
import top.xfunny.mod.block.base.LiftButtonsBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ShanghaiMitsubishiLehy3Button3LCD extends LiftButtonsBase {
    public ShanghaiMitsubishiLehy3Button3LCD() {
        super(true, true);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final boolean single = !IBlock.getStatePropertySafe(world.getBlockState(pos), SINGLE);
        return IBlock.getVoxelShapeByDirection(single ? 6.25 : 7, 2.785, 0, single ? 9.75 : 9, 12.275, 0.25, IBlock.getStatePropertySafe(state, FACING));
    }

    @Nonnull
    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ShanghaiMitsubishiLehy3Button3LCD.BlockEntity(blockPos, blockState);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(UNLOCKED);
        properties.add(SINGLE);
    }

    public void addTooltips(@NotNull ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, @NotNull TooltipContext options) {
        tooltip.add(TextHelper.translatable("tooltip.shanghai_mitsubishi_display_zlhh-101ag02").formatted(TextFormatting.GRAY));
        tooltip.add(TextHelper.translatable("tooltip.shanghai_mitsubishi_button_a11").formatted(TextFormatting.GRAY));
    }

    public static class BlockEntity extends BlockEntityBase {
        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.SHANGHAI_MITSUBISHI_LEHY_3_BUTTON_3_LCD.get(), pos, state);
        }
    }
}
