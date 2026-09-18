package top.xfunny.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.IBlock;
import top.xfunny.mod.BlockEntityTypes;
import top.xfunny.mod.block.base.LiftPanelBase;

import javax.annotation.Nonnull;
import java.util.List;

public class ShanghaiMitsubishiLehy3Screen1WideEven extends LiftPanelBase {
    public ShanghaiMitsubishiLehy3Screen1WideEven() {
        super(false);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (IBlock.getStatePropertySafe(state, SIDE)) {
            case LEFT:
                return IBlock.getVoxelShapeByDirection(10.675, 9.5, 0, 16, 13, 0.1, IBlock.getStatePropertySafe(state, FACING));

            case RIGHT:
                return IBlock.getVoxelShapeByDirection(0, 9.5, 0, 5.325, 13, 0.1, IBlock.getStatePropertySafe(state, FACING));

        }
        return VoxelShapes.empty();
    }

    @Nonnull
    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ShanghaiMitsubishiLehy3Screen1WideEven.BlockEntity(blockPos, blockState);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        // 添加块的方向属性
        properties.add(FACING);
        properties.add(SIDE);
    }

    public static class BlockEntity extends BlockEntityBase {

        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.SHANGHAI_MITSUBISHI_LEHY_3_SCREEN_1_WIDE_EVEN.get(), pos, state);
        }
    }
}
