package top.xfunny.mod.block;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mod.block.BlockLiftTrackBase;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketDeleteData;
import top.xfunny.mod.BlockEntityTypes;
import top.xfunny.mod.Init;
import top.xfunny.mod.packet.PacketYTEOpenBlockEntityScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class EmptyFloor extends BlockLiftTrackBase implements BlockWithEntity {
    public EmptyFloor() {
        super();
    }

    @Nonnull
    @Override
    // Handling the use of brush, if yes, open the editing interface of floor name and description.
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlock.checkHoldingBrush(world, player, () -> {
            final org.mtr.mapping.holder.BlockEntity entity = world.getBlockEntity(pos);
            if (entity != null && entity.data instanceof BlockEntity) {
                ((BlockEntity) entity.data).markDirty2();
                Init.REGISTRY.sendPacketToClient(ServerPlayerEntity.cast(player), new PacketYTEOpenBlockEntityScreen(pos));
            }
        });
    }

    @Nonnull
    @Override
    public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new EmptyFloor.BlockEntity(blockPos, blockState);
    }

    @Override
    public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient()) {
            PacketDeleteData.sendDirectlyToServerLiftFloorPosition(ServerWorld.cast(world), Init.blockPosToPosition(pos));
        }
    }

    @Nonnull
    @Override
    public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 1, IBlock.getStatePropertySafe(state, FACING));
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
        tooltip.add(TranslationProvider.TOOLTIP_MTR_LIFT_TRACK_FLOOR.getMutableText().formatted(TextFormatting.GRAY));
    }

    @Override
    public ObjectArrayList<Direction> getConnectingDirections(BlockState blockState) {
        // Get the players' facing direction
        final Direction facing = IBlock.getStatePropertySafe(blockState, FACING);
        // Get the players' facing direction, and return
        return ObjectArrayList.of(Direction.UP, Direction.DOWN, facing.rotateYClockwise(), facing.rotateYCounterclockwise());
    }

    public static class BlockEntity extends BlockEntityExtension {

        private static final String KEY_FLOOR_NUMBER = "floor_number";
        private static final String KEY_FLOOR_DESCRIPTION = "floor_description";
        private static final String KEY_SHOULD_DING = "should_ding";
        private String floorNumber = "EZ";
        private String floorDescription = "";
        private boolean shouldDing;

        public BlockEntity(BlockPos pos, BlockState state) {
            super(BlockEntityTypes.LIFT_TRACK_EMPTY_FLOOR.get(), pos, state);
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            floorNumber = compoundTag.getString(KEY_FLOOR_NUMBER);
            floorDescription = compoundTag.getString(KEY_FLOOR_DESCRIPTION);
            shouldDing = compoundTag.getBoolean(KEY_SHOULD_DING);
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            compoundTag.putString(KEY_FLOOR_NUMBER, floorNumber);
            compoundTag.putString(KEY_FLOOR_DESCRIPTION, floorDescription);
            compoundTag.putBoolean(KEY_SHOULD_DING, shouldDing);
        }

        public void setData(String floorNumber, String floorDescription) {
            setData(floorNumber, floorDescription, shouldDing);
        }

        public void setData(String floorNumber, String floorDescription, boolean shouldDing) {
            this.floorNumber = floorNumber;
            this.floorDescription = floorDescription;
            this.shouldDing = shouldDing;
            markDirty2();
        }

        public String getFloorNumber() {
            return floorNumber;
        }

        public String getFloorDescription() {
            return floorDescription;
        }

        public boolean getShouldDing() {
            return shouldDing;
        }
    }
}
