package top.xfunny.mod.util;

import org.mtr.mapping.holder.Direction;

public class TransformPositionX {
    public static double transform(double x, double z, Direction facing) {
        switch (facing) {
            case NORTH:
                return x;
            case SOUTH:
                return 1 - x;
            case EAST:
                return z;
            case WEST:
                return 1 - z;
            default:
                return 0;
        }
    }
}

