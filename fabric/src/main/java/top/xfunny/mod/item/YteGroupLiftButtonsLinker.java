package top.xfunny.mod.item;

import org.jetbrains.annotations.NotNull;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.block.BlockLiftButtons;
import org.mtr.mod.block.BlockLiftPanelBase;
import org.mtr.mod.block.BlockLiftTrackBase;
import org.mtr.mod.block.BlockLiftTrackFloor;
import top.xfunny.mod.ButtonRegistry;
import top.xfunny.mod.Init;
import top.xfunny.mod.LiftFloorRegistry;
import top.xfunny.mod.block.base.LiftButtonsBase;
import top.xfunny.mod.block.base.LiftDestinationDispatchTerminalBase;
import top.xfunny.mod.block.base.LiftPanelBase;

import static top.xfunny.mod.item.LinkerValidTypes.VALID_TYPES;

public class YteGroupLiftButtonsLinker extends YTEItemBlockClickingBase implements DirectionHelper {
    private final boolean isConnector;
    BlockPos posStart;
    int number;

    public YteGroupLiftButtonsLinker(boolean isConnector, ItemSettings itemSettings) {
        super(itemSettings.maxCount(1));
        this.isConnector = isConnector;
    }

    private static void connect(World world, BlockPos blockPos1, BlockPos blockPos2, boolean isAdd) {
        final BlockEntity blockEntity1 = world.getBlockEntity(blockPos1);
        final BlockEntity blockEntity2 = world.getBlockEntity(blockPos2);

        if (blockEntity1 != null && blockEntity2 != null) {
            if (blockEntity2.data instanceof BlockLiftTrackFloor.BlockEntity) {
                if (blockEntity1.data instanceof LiftFloorRegistry) {
                    ((LiftFloorRegistry) blockEntity1.data).registerFloor(blockPos1, world, blockPos2, isAdd);
                } else if (blockEntity1.data instanceof BlockLiftButtons.BlockEntity) {
                    ((BlockLiftButtons.BlockEntity) blockEntity1.data).registerFloor(blockPos2, isAdd);
                } else if (blockEntity1.data instanceof BlockLiftPanelBase.BlockEntityBase) {
                    ((BlockLiftPanelBase.BlockEntityBase) blockEntity1.data).registerFloor(world, blockPos2, isAdd);
                }
            } else if (blockEntity2.data instanceof LiftButtonsBase.BlockEntityBase || blockEntity2.data instanceof LiftDestinationDispatchTerminalBase.BlockEntityBase) {
                if (blockEntity1.data instanceof ButtonRegistry) {
                    ((ButtonRegistry) blockEntity1.data).registerButton(world, blockPos2, isAdd);
                }
            }
        }
    }

    @Override
    protected void onStartClick(@NotNull ItemUsageContext context, @NotNull CompoundTag compoundTag) {
    }

    @Override
    protected void onEndClick(ItemUsageContext context, @NotNull BlockPos posEnd, @NotNull CompoundTag compoundTag) {
        final PathFinder pathFinder = new PathFinder();
        final PlayerEntity playerEntity = context.getPlayer();
        int floorCount = 0;
        final World world = context.getWorld();
        posStart = context.getBlockPos();
        number = 0;

        while (true) {
            if (posStart == null) {
                if (playerEntity != null) {
                    playerEntity.sendMessage(Text.cast(TextHelper.translatable("message.floor_auto_setter_status_need_track_floor_position")), true);
                }
                break;
            }

            final BlockState startState = world.getBlockState(posStart);
            final BlockState endState = world.getBlockState(posEnd);

            final boolean isStartTrackBase = startState.getBlock().data instanceof BlockLiftTrackBase;
            final boolean isEndTrackBase = endState.getBlock().data instanceof BlockLiftTrackBase;

            if (isStartTrackBase && isEndTrackBase) {
                if (playerEntity != null) {
                    playerEntity.sendMessage(isConnector ? Text.cast(TextHelper.translatable("message.linker_status_failed")) : Text.cast(TextHelper.translatable("message.linker_status_failed_remove")), true);
                }
                break;
            } else if (isStartTrackBase ^ isEndTrackBase) {
                Init.LOGGER.info(posEnd.toShortString());

                connect(world, posStart, posEnd, isConnector);
                connect(world, posEnd, posStart, isConnector);

                Object[] pos = null;
                try {
                    pos = pathFinder.findPath(context, posStart, posEnd);
                } catch (Exception e) {
                    Init.LOGGER.error("PathFinder error during single connection", e);
                }

                if (pos == null || pos.length < 2) {
                    if (playerEntity != null) {
                        int displayCount = Math.max(1, floorCount);
                        playerEntity.sendMessage(isConnector ? Text.cast(TextHelper.translatable("message.linker_status_finished", displayCount)) : Text.cast(TextHelper.translatable("message.linker_status_finished_remove", displayCount)), true);
                    }
                    break;
                }

                posStart = (BlockPos) pos[0];
                posEnd = (BlockPos) pos[1];

                final BlockState newPosEndState = world.getBlockState(posEnd);
                final BlockState newPosStartState = world.getBlockState(posStart);

                final boolean isPosEndValid =
                        newPosEndState.getBlock().data instanceof LiftButtonsBase ||
                                newPosEndState.getBlock().data instanceof LiftDestinationDispatchTerminalBase ||
                                newPosEndState.getBlock().data instanceof LiftPanelBase ||
                                newPosEndState.getBlock().data instanceof BlockLiftButtons ||
                                newPosEndState.getBlock().data instanceof BlockLiftPanelBase;

                final boolean isPosStartTrackFloor =
                        newPosStartState.getBlock().data instanceof BlockLiftTrackFloor;

                if (isPosEndValid && isPosStartTrackFloor) {
                    floorCount++;
                }
            } else {
                if (playerEntity != null) {
                    playerEntity.sendMessage(isConnector ? Text.cast(TextHelper.translatable("message.linker_status_failed")) : Text.cast(TextHelper.translatable("message.linker_status_failed_remove")), true);
                }
                break;
            }

            if (number == pathFinder.getMark().size() || number > 1000) {
                if (playerEntity != null) {
                    playerEntity.sendMessage(isConnector ? Text.cast(TextHelper.translatable("message.linker_status_finished", floorCount)) : Text.cast(TextHelper.translatable("message.linker_status_finished_remove", floorCount)), true);
                }
                break;
            }
            number++;
        }
    }

    @Override
    public void onThirdClick(ItemUsageContext context, BlockPos pos1, BlockPos pos2, BlockPos pos3, CompoundTag compoundTag) {
        final PlayerEntity playerEntity = context.getPlayer();
        int floorCount = 0;
        final PathFinder pathFinder = new PathFinder();
        final World world = context.getWorld();
        number = 0;

        while (true) {
            if (pos1 != null && pos2 != null && world.getBlockState(pos3).getBlock().data instanceof BlockLiftTrackBase) {

                connect(world, pos1, pos2, isConnector);
                connect(world, pos2, pos1, isConnector);

                Object[] pos = null;
                try {
                    pos = pathFinder.findPath(context, pos1, pos2, pos3);
                } catch (Exception e) {
                    Init.LOGGER.error("PathFinder error during third click connection", e);
                }

                if (pos == null || pos.length < 3) {
                    if (playerEntity != null) {
                        int displayCount = Math.max(1, floorCount);
                        playerEntity.sendMessage(isConnector ? Text.cast(TextHelper.translatable("message.linker_status_finished", displayCount)) : Text.cast(TextHelper.translatable("message.linker_status_finished_remove", displayCount)), true);
                    }
                    break;
                }

                pos1 = (BlockPos) pos[1];
                pos2 = (BlockPos) pos[2];
                pos3 = (BlockPos) pos[0];//轨道

                final Block blockEnd = world.getBlockState(pos2).getBlock();
                final Block blockStart = world.getBlockState(pos1).getBlock();

                // [修复] 同样严格判断两个点是否都是 LiftButtonsBase 且 allowPress 相反
                boolean isValidPair = false;
                if (blockEnd.data instanceof LiftButtonsBase && blockStart.data instanceof LiftButtonsBase) {
                    boolean endAllowPress = ((LiftButtonsBase) blockEnd.data).allowPress;
                    boolean startAllowPress = ((LiftButtonsBase) blockStart.data).allowPress;
                    if (endAllowPress != startAllowPress) {
                        isValidPair = true;
                    }
                }

                if (isValidPair) {
                    floorCount++;
                }
            } else {
                if (playerEntity != null) {
                    playerEntity.sendMessage(isConnector ? Text.cast(TextHelper.translatable("message.linker_status_failed")) : Text.cast(TextHelper.translatable("message.linker_status_failed_remove")), true);
                }
                break;
            }

            if (number == pathFinder.getMark().size() || number > 1000) {
                if (playerEntity != null) {
                    playerEntity.sendMessage(isConnector ? Text.cast(TextHelper.translatable("message.linker_status_finished", floorCount)) : Text.cast(TextHelper.translatable("message.linker_status_finished_remove", floorCount)), true);
                }
                break;
            }
            number++;
        }
    }

    private boolean isValidType(Object data) {
        return VALID_TYPES.contains(data.getClass());
    }

    @Override
    protected boolean clickCondition(ItemUsageContext context) {
        final Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();
        return isValidType(block.data);
    }
}