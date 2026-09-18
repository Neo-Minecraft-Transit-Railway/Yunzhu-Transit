package top.xfunny.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.IBlock;
import top.xfunny.mod.BlockEntityTypes;
import top.xfunny.mod.block.base.LiftPanelBase;

import javax.annotation.Nonnull;
import java.util.List;

public class MitsubishiNexWayScreen3WideEven extends LiftPanelBase {
    public MitsubishiNexWayScreen3WideEven() {
        super(false);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        switch (IBlock.getStatePropertySafe(state, SIDE)) {
            case LEFT:
                return IBlock.getVoxelShapeByDirection(6.75, 9.5, 0, 16, 13, 0.6, IBlock.getStatePropertySafe(state, FACING));
            case RIGHT:
                return IBlock.getVoxelShapeByDirection(0, 9.5, 0, 9.25, 13, 0.6, IBlock.getStatePropertySafe(state, FACING));
            default:
                return VoxelShapes.empty();
        }
    }


    @Nonnull
    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new MitsubishiNexWayScreen3WideEven.BlockEntity(blockPos, blockState);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        // 添加块的方向属性
        properties.add(FACING);
        properties.add(SIDE);
    }

    public static class BlockEntity extends BlockEntityBase {

        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.MITSUBISHI_NEXWAY_SCREEN_3_WIDE_EVEN.get(), pos, state);
        }
    }

}
