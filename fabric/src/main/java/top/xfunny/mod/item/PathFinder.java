package top.xfunny.mod.item;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.Direction;
import org.mtr.mapping.holder.ItemUsageContext;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mod.block.BlockLiftTrackBase;
import org.mtr.mod.block.IBlock;
import top.xfunny.mod.Init;

import java.util.ArrayList;


public class PathFinder implements DirectionHelper {
    ArrayList<BlockPos> mark = new ArrayList<>();
    Object[] array = new Object[3];

    public Object[] findPath(ItemUsageContext context, BlockPos pos) {
        final World world = context.getWorld();
        checkPosition(world, pos, new BlockPos(0, 0, 0), new BlockPos(0, 0, 0), facingHelper(context, pos));
        return array;
    }

    public Object[] findPath(ItemUsageContext context, BlockPos pos1, BlockPos pos2) {
        final World world = context.getWorld();
        if (world.getBlockState(pos1).getBlock().data instanceof BlockLiftTrackBase) {
            checkPosition(world, pos1, pos2, new BlockPos(0, 0, 0), facingHelper(context, pos1, pos2));
        } else {
            checkPosition(world, pos2, pos1, new BlockPos(0, 0, 0), facingHelper(context, pos1, pos2));
        }
        return array;
    }

    public Object[] findPath(ItemUsageContext context, BlockPos pos1, BlockPos pos2, BlockPos pos3) {
        final World world = context.getWorld();
        checkPosition(world, pos3, pos1, pos2, facingHelper(context, pos3, pos3));//pos3为轨道
        return array;
    }

    private void checkPosition(World world, BlockPos pos, BlockPos otherPos, BlockPos thirdPos, boolean facing) {
        if (world.getBlockState(pos.up(1)).getBlock().data instanceof BlockLiftTrackBase) {
            if (!findMark(pos.up(1))) {
                array[0] = pos.up(1);
                array[1] = otherPos.up(1);
                array[2] = thirdPos.up(1);
                Init.LOGGER.info("上" + array[0]);
                mark.add(pos);
                return;
            }
        }
        if (world.getBlockState(pos.down(1)).getBlock().data instanceof BlockLiftTrackBase) {
            if (!findMark(pos.down(1))) {
                array[0] = pos.down(1);
                array[1] = otherPos.down(1);
                array[2] = thirdPos.down(1);
                Init.LOGGER.info("下" + array[0]);
                mark.add(pos);
                return;
            }
        }
        if (world.getBlockState(pos.south(1)).getBlock().data instanceof BlockLiftTrackBase && facing) {
            if (!findMark(pos.south(1))) {
                Init.LOGGER.info("南");
                array[0] = pos.south(1);
                array[1] = otherPos.south(1);
                array[2] = thirdPos.south(1);
                mark.add(pos);
                return;
            }
        }
        if (world.getBlockState(pos.north(1)).getBlock().data instanceof BlockLiftTrackBase && facing) {
            if (!findMark(pos.north(1))) {
                Init.LOGGER.info("北");
                array[0] = pos.north(1);
                array[1] = otherPos.north(1);
                array[2] = thirdPos.north(1);
                mark.add(pos);
                return;
            }
        }
        if (world.getBlockState(pos.east(1)).getBlock().data instanceof BlockLiftTrackBase && !facing) {
            if (!findMark(pos.east(1))) {
                Init.LOGGER.info("东");
                array[0] = pos.east(1);
                array[1] = otherPos.east(1);
                array[2] = thirdPos.east(1);
                mark.add(pos);
                return;
            }
        }
        if (world.getBlockState(pos.west(1)).getBlock().data instanceof BlockLiftTrackBase && !facing) {
            if (!findMark(pos.west(1))) {
                Init.LOGGER.info("西");
                array[0] = pos.west(1);
                array[1] = otherPos.west(1);
                array[2] = thirdPos.west(1);
                mark.add(pos);
            }
        }
    }

    public Boolean findMark(BlockPos pos) {
        for (BlockPos blockPos : mark) {
            if (blockPos.equals(pos)) {
                return true;
            }
        }
        return false;
    }

    private boolean facingHelper(ItemUsageContext context, BlockPos pos) {
        final World world = context.getWorld();
        if (world.getBlockState(pos).getBlock().data instanceof BlockLiftTrackBase) {
            return IBlock.getStatePropertySafe(world.getBlockState(pos), FACING) == Direction.EAST || IBlock.getStatePropertySafe(world.getBlockState(pos), FACING) == Direction.WEST;
        }
        return false;
    }

    private boolean facingHelper(ItemUsageContext context, BlockPos pos1, BlockPos pos2) {
        final World world = context.getWorld();
        if (world.getBlockState(pos1).getBlock().data instanceof BlockLiftTrackBase) {
            return IBlock.getStatePropertySafe(world.getBlockState(pos1), FACING) == Direction.EAST || IBlock.getStatePropertySafe(world.getBlockState(pos1), FACING) == Direction.WEST;//东西

        } else if (world.getBlockState(pos2).getBlock().data instanceof BlockLiftTrackBase) {
            return IBlock.getStatePropertySafe(world.getBlockState(pos2), FACING) == Direction.EAST || IBlock.getStatePropertySafe(world.getBlockState(pos2), FACING) == Direction.WEST;//东西
        }
        return false;
    }

    public ArrayList<BlockPos> getMark() {
        return mark;
    }
}
