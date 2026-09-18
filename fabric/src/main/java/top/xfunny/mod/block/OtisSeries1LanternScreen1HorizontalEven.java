package top.xfunny.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.IBlock;
import top.xfunny.mod.BlockEntityTypes;
import top.xfunny.mod.block.base.LiftButtonsBase;

import javax.annotation.Nonnull;
import java.util.List;

public class OtisSeries1LanternScreen1HorizontalEven extends LiftButtonsBase {
    public OtisSeries1LanternScreen1HorizontalEven() {
        super(false, false);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (IBlock.getStatePropertySafe(state, SIDE)) {
            case LEFT:
                return IBlock.getVoxelShapeByDirection(13.15, 9.525, 0, 16, 12.025, 0.25, IBlock.getStatePropertySafe(state, FACING));

            case RIGHT:
                return IBlock.getVoxelShapeByDirection(0, 9.525, 0, 2.85, 12.025, 0.25, IBlock.getStatePropertySafe(state, FACING));
        }
        return VoxelShapes.empty();
    }

    @Nonnull
    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new OtisSeries1LanternScreen1HorizontalEven.BlockEntity(blockPos, blockState);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        // 添加块的方向属性
        properties.add(FACING);
        properties.add(SIDE);
    }

    public static class BlockEntity extends BlockEntityBase {

        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.OTIS_SERIES_1_LANTERN_SCREEN_1_HORIZONTAL_EVEN.get(), pos, state);
        }
    }

}
