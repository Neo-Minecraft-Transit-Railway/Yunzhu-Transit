package top.xfunny.mod.util;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.Direction;

public class ReverseRendering {
    public static boolean reverseRendering(Direction direction, BlockPos blockPos1, BlockPos blockPos2) {
        if (direction.getOffsetX() != 0) {
            return Math.signum(blockPos2.getX() - blockPos1.getX()) == direction.getOffsetX();
        } else if (direction.getOffsetZ() != 0) {
            return Math.signum(blockPos2.getZ() - blockPos1.getZ()) == direction.getOffsetZ();
        } else {
            return false;
        }
    }
}
