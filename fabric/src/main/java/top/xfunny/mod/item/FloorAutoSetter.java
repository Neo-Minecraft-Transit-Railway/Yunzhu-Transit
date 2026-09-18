package top.xfunny.mod.item;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.block.BlockLiftTrackBase;
import org.mtr.mod.block.BlockLiftTrackFloor;

import javax.annotation.Nonnull;

public class FloorAutoSetter extends ItemExtension implements DirectionHelper {
    public FloorAutoSetter(ItemSettings itemSettings) {
        super(itemSettings.maxCount(1));
    }

    @Nonnull
    public ActionResult useOnBlock2(ItemUsageContext context) {
        if (!context.getWorld().isClient()) {
            if (this.clickCondition(context)) {
                CompoundTag compoundTag = context.getStack().getOrCreateTag();
                compoundTag.putLong("pos", context.getBlockPos().asLong());
                this.onClick(context, compoundTag);
                return ActionResult.SUCCESS;
            } else {
                return ActionResult.FAIL;
            }
        } else {
            return super.useOnBlock2(context);
        }
    }

    protected void onClick(ItemUsageContext context, CompoundTag compoundTag) {
        PathFinder pathFinder = new PathFinder();
        final PlayerEntity playerEntity = context.getPlayer();

        final World world = context.getWorld();
        BlockPos pos = context.getBlockPos();
        int number = 0;
        int floorCount = 0; // 记录实际成功设置的楼层数量
        int floorNumber = 0;
        boolean ding = false;
        String floorNumber2 = "";
        final BlockEntity floorEntity = world.getBlockEntity(pos);

        // 确定初始楼层
        if (floorEntity != null && floorEntity.data instanceof BlockLiftTrackFloor.BlockEntity) {
            String checkFloorNumber = ((BlockLiftTrackFloor.BlockEntity) floorEntity.data).getFloorNumber();
            if (checkFloorNumber != null && !checkFloorNumber.isEmpty()) {
                floorNumber2 = checkFloorNumber;
            } else {
                floorNumber2 = "1";
            }
            ding = ((BlockLiftTrackFloor.BlockEntity) floorEntity.data).getShouldDing();
        }

        // 判断初始楼层是否为整数
        try {
            floorNumber = Integer.parseInt(floorNumber2.trim());
        } catch (NumberFormatException exception) {
            if (playerEntity != null) {
                playerEntity.sendMessage(Text.cast(TextHelper.translatable("message.floor_auto_setter_status_failed")), true);
            }
            return;
        }

        while (true) {
            if (world.getBlockState(pos).getBlock().data instanceof BlockLiftTrackBase) {
                final BlockEntity currentEntity = world.getBlockEntity(pos);

                // 1. 如果当前位置是楼层，设置数据并增加成功计数
                if (currentEntity != null && currentEntity.data instanceof BlockLiftTrackFloor.BlockEntity) {
                    ((BlockLiftTrackFloor.BlockEntity) currentEntity.data).setData(String.valueOf(floorNumber), "", ding);
                    floorCount++; // 成功设置一个楼层，计数+1
                }

                // 2. 寻路获取下一个位置
                Object[] apos = pathFinder.findPath(context, pos);
                if (apos == null || apos.length == 0) break; // 安全校验
                pos = (BlockPos) apos[0];

                // 3. 判断是否到达终点
                if (number == pathFinder.getMark().size()) {
                    if (playerEntity != null) {
                        // 直接输出 floorCount，不需要再 +1
                        playerEntity.sendMessage(Text.cast(TextHelper.translatable("message.floor_auto_setter_status_finished", floorCount)), true);
                    }
                    break;
                }

                // 4. [修复核心] 如果下一个位置是楼层，说明即将进入新楼层，编号+1
                if (world.getBlockState(pos).getBlock().data instanceof BlockLiftTrackFloor) {
                    if (floorNumber == Integer.MAX_VALUE) {
                        if (playerEntity != null) {
                            playerEntity.sendMessage(Text.cast(TextHelper.translatable("message.floor_auto_setter_status_failed")), true);
                        }
                        return;
                    }
                    floorNumber++;
                }

            } else {
                if (playerEntity != null) {
                    playerEntity.sendMessage(Text.cast(TextHelper.translatable("message.floor_auto_setter_status_failed")), true);
                }
                break;
            }

            number++; // 循环计数器
        }
    }

    protected boolean clickCondition(ItemUsageContext context) {
        final Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();
        return block.data instanceof BlockLiftTrackFloor;
    }
}
