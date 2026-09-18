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

public class HitachiVIB676Button extends LiftButtonsBase {
    public HitachiVIB676Button() {
        super(true, true);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        final boolean single = !IBlock.getStatePropertySafe(world.getBlockState(pos), SINGLE);
        return IBlock.getVoxelShapeByDirection(single ? 6.15 : 6.9, 2.775, 0, single ? 9.85 : 9.1, 12.275, 0.45, IBlock.getStatePropertySafe(state, FACING));
    }

    @Nonnull
    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new HitachiVIB676Button.BlockEntity(blockPos, blockState);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        properties.add(FACING);
        properties.add(UNLOCKED);
        properties.add(SINGLE);
    }

    @Override
    public void addTooltips(@NotNull ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, @NotNull TooltipContext options) {
        tooltip.add(TextHelper.translatable("tooltip.hitachi_display_hip_43").formatted(TextFormatting.GRAY));
        tooltip.add(TextHelper.translatable("tooltip.hitachi_button_ml_mw").formatted(TextFormatting.GRAY));
    }

    public static class BlockEntity extends BlockEntityBase {
        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.HITACHI_VIB676_BUTTON.get(), pos, state);
        }
    }
}
