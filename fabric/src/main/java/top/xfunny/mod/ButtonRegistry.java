package top.xfunny.mod;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.World;

public interface ButtonRegistry {
    void registerButton(World world, BlockPos blockPos, boolean isAdd);
}
