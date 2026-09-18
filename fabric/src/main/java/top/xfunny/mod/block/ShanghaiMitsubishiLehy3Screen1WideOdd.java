package top.xfunny.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.block.IBlock;
import top.xfunny.mod.BlockEntityTypes;
import top.xfunny.mod.block.base.LiftPanelBase;

import javax.annotation.Nonnull;
import java.util.List;

public class ShanghaiMitsubishiLehy3Screen1WideOdd extends LiftPanelBase {
    public ShanghaiMitsubishiLehy3Screen1WideOdd() {
        super(true);
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IBlock.getVoxelShapeByDirection(2.725, 9.5, 0, 13.275, 13, 0.1, IBlock.getStatePropertySafe(state, FACING));
    }

    @Nonnull
    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ShanghaiMitsubishiLehy3Screen1WideOdd.BlockEntity(blockPos, blockState);
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        // 添加块的方向属性
        properties.add(FACING);
    }

    public static class BlockEntity extends BlockEntityBase {

        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.SHANGHAI_MITSUBISHI_LEHY_3_SCREEN_1_WIDE_ODD.get(), pos, state);
        }
    }

}
