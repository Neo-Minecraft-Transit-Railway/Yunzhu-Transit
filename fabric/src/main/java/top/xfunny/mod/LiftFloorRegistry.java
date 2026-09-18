package top.xfunny.mod;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.World;

public interface LiftFloorRegistry {
    void registerFloor(BlockPos selfPos, World world, BlockPos blockPos, boolean isAdd);
}
