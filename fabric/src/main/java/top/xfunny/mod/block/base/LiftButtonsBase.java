package top.xfunny.mod.block.base;

import org.mtr.core.data.Lift;
import org.mtr.core.data.LiftDirection;
import org.mtr.core.operation.PressLift;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.holder.Blocks;
import org.mtr.mapping.mapper.*;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.packet.PacketPressLiftButton;
import top.xfunny.mod.*;
import top.xfunny.mod.Items;
import top.xfunny.mod.keymapping.DefaultButtonsKeyMapping;
import top.xfunny.mod.util.TransformPositionX;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.mtr.core.data.LiftDirection.NONE;

public abstract class LiftButtonsBase extends BlockExtension implements DirectionHelper, BlockWithEntity, IBlock {
    public static final BooleanProperty UNLOCKED = BooleanProperty.of("unlocked");
    public static final BooleanProperty SINGLE = BooleanProperty.of("single");

    // [修复] 去掉 static，否则所有实例将共享同一个值，导致无法区分 Lantern 和 Button
    public final boolean allowPress;

    private final boolean isOdd;
    private double median = 0.25;//判定按下上、下按钮的分界线

    public LiftButtonsBase(boolean allowPress, boolean isOdd) {
        super(BlockHelper.createBlockSettings(true, true));
        this.isOdd = isOdd;
        this.allowPress = allowPress; // [修复] 赋值给实例变量
    }

    public LiftButtonsBase(boolean allowPress, boolean isOdd, double median) {//todo:即将弃用
        super(BlockHelper.createBlockSettings(true, true));
        this.isOdd = isOdd;
        this.allowPress = allowPress; // [修复] 赋值给实例变量
        this.median = median;
    }

    public static void hasButtonsClient(BlockPos trackPosition, LiftButtonDescriptor descriptor, FloorLiftCallback callback) {
        MinecraftClientData.getInstance().lifts.forEach(lift -> {
            final int floorIndex = lift.getFloorIndex(Init.blockPosToPosition(trackPosition));
            if (floorIndex > 0) {
                descriptor.setHasDownButton(true);
            }
            if (floorIndex >= 0 && floorIndex < lift.getFloorCount() - 1) {
                descriptor.setHasUpButton(true);
            }
            if (floorIndex >= 0) {
                callback.accept(floorIndex, lift);
            }
        });
    }

    @Nonnull
    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        final ActionResult result = IBlock.checkHoldingBrush(world, player, () -> {
            final boolean unlocked = !IBlock.getStatePropertySafe(state, UNLOCKED);
            world.setBlockState(pos, state.with(new Property<>(UNLOCKED.data), unlocked));
            player.sendMessage(Text.cast(Text.literal(unlocked ? "已解锁" : "已锁定")), true);
        });

        if (result == ActionResult.SUCCESS) {
            return ActionResult.SUCCESS;
        } else {
            if (player.isHolding(Items.YTE_LIFT_BUTTONS_LINK_CONNECTOR.get()) || player.isHolding(Items.YTE_LIFT_BUTTONS_LINK_REMOVER.get()) || player.isHolding(Items.YTE_GROUP_LIFT_BUTTONS_LINK_CONNECTOR.get()) || player.isHolding(Items.YTE_GROUP_LIFT_BUTTONS_LINK_REMOVER.get())) {
                return ActionResult.PASS;
            } else {
                final boolean unlocked = IBlock.getStatePropertySafe(state, UNLOCKED);
                final double hitY = MathHelper.fractionalPart(hit.getPos().getYMapped());
                final BlockEntity blockEntity = world.getBlockEntity(pos);
                final BlockEntityBase data = (BlockEntityBase) blockEntity.data;
                final DefaultButtonsKeyMapping keyMapping = data.getKeyMapping();
                final String focusButton = keyMapping.mapping(TransformPositionX.transform(MathHelper.fractionalPart(hit.getPos().getXMapped()), MathHelper.fractionalPart(hit.getPos().getZMapped()), IBlock.getStatePropertySafe(state, FACING)), hitY);

                Init.LOGGER.info(focusButton);

                if (unlocked) {
                    if (world.isClient() && !focusButton.equals("null")) {
                        ObjectOpenHashSet<BlockPos> connectedLanternPositions = data.getLiftButtonPositions();
                        LiftButtonDescriptor descriptor = new LiftButtonDescriptor(false, false);
                        data.trackPositions.forEach(trackPosition -> LiftButtonsBase.hasButtonsClient(trackPosition, descriptor, (floor, lift) -> {
                        }));

                        connectedLanternPositions.forEach(lanternPos -> {
                            BlockEntity lanternBlockEntity = world.getBlockEntity(lanternPos);
                            if (lanternBlockEntity != null && lanternBlockEntity.data instanceof BlockEntityBase) {
                                BlockEntityBase lanternData = (BlockEntityBase) lanternBlockEntity.data;
                                if (descriptor.hasDownButton() && descriptor.hasUpButton()) {
                                    if (focusButton.equals("down")) {
                                        lanternData.setPressedButtonDirection(LiftDirection.DOWN);
                                    } else if (focusButton.equals("up")) {
                                        lanternData.setPressedButtonDirection(LiftDirection.UP);
                                    }
                                } else {
                                    lanternData.setPressedButtonDirection(descriptor.hasDownButton() ? LiftDirection.DOWN : LiftDirection.UP);
                                }
                            }
                        });

                        if (descriptor.hasDownButton() && descriptor.hasUpButton()) {
                            data.liftDirection = focusButton.equals("up") ? LiftDirection.UP : focusButton.equals("down") ? LiftDirection.DOWN : NONE;
                        } else {
                            data.liftDirection = descriptor.hasDownButton() ? LiftDirection.DOWN : LiftDirection.UP;
                        }

                        final PressLift pressLift = new PressLift();
                        data.trackPositions.forEach(trackPosition -> pressLift.add(Init.blockPosToPosition(trackPosition), data.liftDirection));

                        InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketPressLiftButton(pressLift));
                        return ActionResult.SUCCESS;
                    }
                    return ActionResult.SUCCESS;
                } else {
                    System.out.println(this.allowPress); // [修复] 使用 this.allowPress
                    return ActionResult.FAIL;
                }
            }
        }
    }

    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        final Direction facing = ctx.getPlayerFacing();
        if (!isOdd) {
            return IBlock.isReplaceable(ctx, facing.rotateYClockwise(), 2) ? getDefaultState2().with(new Property<>(FACING.data), facing.data).with(new Property<>(SIDE.data), EnumSide.LEFT) : null;
        } else {
            return getDefaultState2().with(new Property<>(FACING.data), facing.data);
        }
    }

    @Override
    public void onPlaced2(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient()) {
            final Direction facing = IBlock.getStatePropertySafe(state, FACING);
            if (!isOdd) {
                world.setBlockState(pos.offset(facing.rotateYClockwise()), getDefaultState2().with(new Property<>(FACING.data), facing.data).with(new Property<>(SIDE.data), EnumSide.RIGHT), 3);
            }
            world.updateNeighbors(pos, Blocks.getAirMapped());
            state.updateNeighbors(new WorldAccess(world.data), pos, 3);
        }
    }

    @Override
    public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!isOdd) {
            if (IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT) {
                IBlock.onBreakCreative(world, player, pos.offset(IBlock.getSideDirection(state)));
            } else if (IBlock.getStatePropertySafe(state, SIDE) == EnumSide.LEFT) {
                IBlock.onBreakCreative(world, player, pos.offset(IBlock.getSideDirection(state)));
            }
        }
        super.onBreak2(world, pos, state, player);
    }

    @FunctionalInterface
    public interface FloorLiftCallback {
        void accept(int floor, Lift lift);
    }

    public static class LiftButtonDescriptor {
        private boolean hasUpButton;
        private boolean hasDownButton;

        public LiftButtonDescriptor(boolean hasUpButton, boolean hasDownButton) {
            this.hasDownButton = hasDownButton;
            this.hasUpButton = hasUpButton;
        }
        public boolean hasUpButton() { return hasUpButton; }
        public boolean hasDownButton() { return hasDownButton; }
        public void setHasUpButton(boolean hasUpButton) { this.hasUpButton = hasUpButton; }
        public void setHasDownButton(boolean hasDownButton) { this.hasDownButton = hasDownButton; }
    }

    public static class BlockEntityBase extends BlockEntityExtension implements LiftFloorRegistry, ButtonRegistry, LiftLanternController {
        private static final String KEY_TRACK_FLOOR_POS = "track_floor_pos";
        private static final String KEY_LIFT_BUTTON_POSITIONS = "lift_button_position";
        public final ObjectOpenHashSet<BlockPos> liftButtonPositions = new ObjectOpenHashSet<>();
        private final ObjectOpenHashSet<BlockPos> trackPositions = new ObjectOpenHashSet<>();
        public LiftDirection liftDirection = NONE;

        public BlockPos selfPos;
        public boolean lastUpActive = false;
        public boolean lastDownActive = false;
        private LiftDirection pressedButtonDirection;
        private DefaultButtonsKeyMapping keyMapping = new DefaultButtonsKeyMapping();

        public BlockEntityBase(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
            super(type, blockPos, blockState);
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            trackPositions.clear();
            liftButtonPositions.clear();
            for (final long position : compoundTag.getLongArray(KEY_TRACK_FLOOR_POS)) {
                trackPositions.add(BlockPos.fromLong(position));
            }
            for (final long position : compoundTag.getLongArray(KEY_LIFT_BUTTON_POSITIONS)) {
                liftButtonPositions.add(BlockPos.fromLong(position));
            }
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            final List<Long> trackPositionsList = new ArrayList<>();
            trackPositions.forEach(position -> trackPositionsList.add(position.asLong()));
            compoundTag.putLongArray(KEY_TRACK_FLOOR_POS, trackPositionsList);

            final List<Long> liftButtonPositionsList = new ArrayList<>();
            liftButtonPositions.forEach(position -> liftButtonPositionsList.add(position.asLong()));
            compoundTag.putLongArray(KEY_LIFT_BUTTON_POSITIONS, liftButtonPositionsList);
        }

        public DefaultButtonsKeyMapping getKeyMapping() { return keyMapping; }
        public void setKeyMapping(DefaultButtonsKeyMapping keyMapping) { this.keyMapping = keyMapping; }

        public void registerFloor(BlockPos selfPos, World world, BlockPos pos, boolean isAdd) {
            this.selfPos = selfPos;
            final boolean single = IBlock.getStatePropertySafe(world.getBlockState(selfPos), SINGLE);
            if (IBlock.getStatePropertySafe(world, getPos2(), SIDE) == EnumSide.RIGHT) {
                final BlockEntity blockEntity = world.getBlockEntity(getPos2().offset(IBlock.getStatePropertySafe(world, getPos2(), FACING).rotateYCounterclockwise()));
                if (blockEntity != null && blockEntity.data instanceof BlockEntityBase) {
                    ((BlockEntityBase) blockEntity.data).registerFloor(selfPos, world, pos, isAdd);
                }
            } else {
                if (isAdd) {
                    trackPositions.add(pos);
                    if (trackPositions.size() != 1 && single) {
                        final boolean single1 = !IBlock.getStatePropertySafe(world.getBlockState(selfPos), SINGLE);
                        world.setBlockState(selfPos, world.getBlockState(selfPos).with(new Property<>(SINGLE.data), single1));
                    }
                } else {
                    trackPositions.remove(pos);
                    if (trackPositions.size() == 1 && !single) {
                        final boolean single1 = !IBlock.getStatePropertySafe(world.getBlockState(selfPos), SINGLE);
                        world.setBlockState(selfPos, world.getBlockState(selfPos).with(new Property<>(SINGLE.data), single1));
                    }
                }
            }
            markDirty2();
        }

        @Override
        public void registerButton(World world, BlockPos blockPos, boolean isAdd) {
            if (IBlock.getStatePropertySafe(world, getPos2(), SIDE) == EnumSide.RIGHT) {
                final BlockEntity blockEntity = world.getBlockEntity(getPos2().offset(IBlock.getStatePropertySafe(world, getPos2(), FACING).rotateYCounterclockwise()));
                if (blockEntity != null && blockEntity.data instanceof BlockEntityBase) {
                    ((BlockEntityBase) blockEntity.data).registerButton(world, blockPos, isAdd);
                }
            } else {
                if (isAdd) {
                    liftButtonPositions.add(blockPos);
                } else {
                    liftButtonPositions.remove(blockPos);
                }
            }
            markDirty2();
        }

        public void forEachTrackPosition(Consumer<BlockPos> consumer) { trackPositions.forEach(consumer); }
        public void forEachLiftButtonPosition(Consumer<BlockPos> consumer) { liftButtonPositions.forEach(consumer); }
        public ObjectOpenHashSet<BlockPos> getLiftButtonPositions() { return liftButtonPositions; }
        public LiftDirection getPressedButtonDirection() { return pressedButtonDirection; }
        public void setPressedButtonDirection(LiftDirection direction) { this.pressedButtonDirection = direction; }
    }
}