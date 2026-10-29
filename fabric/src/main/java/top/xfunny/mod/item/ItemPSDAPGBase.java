package top.xfunny.mod.item;


import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mod.block.BlockPSDAPGBase;
import org.mtr.mod.block.BlockPSDAPGDoorBase;
import org.mtr.mod.block.BlockPSDTop;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.block.TripleHorizontalBlock;
import org.mtr.mod.generated.lang.TranslationProvider;
import top.xfunny.mod.Blocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ItemPSDAPGBase extends ItemExtension implements IBlock {
    private final ItemPSDAPGBase.EnumPSDAPGItem item;
    private final ItemPSDAPGBase.EnumPSDAPGType type;

    public ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem item, ItemPSDAPGBase.EnumPSDAPGType type, ItemSettings itemSettings) {
        super(itemSettings);
        this.item = item;
        this.type = type;
    }

    public static boolean blocksNotReplaceable(ItemUsageContext context, int width, int height, @Nullable Block blacklistBlock) {
        final Direction facing = context.getPlayerFacing();
        final World world = context.getWorld();
        final BlockPos startingPos = context.getBlockPos().offset(context.getSide());

        for (int x = 0; x < width; x++) {
            final BlockPos offsetPos = startingPos.offset(facing.rotateYClockwise(), x);

            if (blacklistBlock != null) {
                final boolean isBlacklistedBelow = world.getBlockState(offsetPos.down()).isOf(blacklistBlock);
                final boolean isBlacklistedAbove = world.getBlockState(offsetPos.up(height)).isOf(blacklistBlock);
                if (isBlacklistedBelow || isBlacklistedAbove) {
                    return true;
                }
            }

            for (int y = 0; y < height; y++) {
                final BlockState existing = world.getBlockState(offsetPos.up(y));
                if (!existing.isAir() && !existing.getBlock().equals(org.mtr.mapping.holder.Blocks.getAirMapped())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Nonnull
    @Override
    public ActionResult useOnBlock2(ItemUsageContext context) {
        final int horizontalBlocks = item.isDoor ? type.isOdd ? 3 : 2 : 1;
        if (blocksNotReplaceable(context, horizontalBlocks, type.isPSD ? 3 : 2, getBlockStateFromItem().getBlock())) {
            return ActionResult.FAIL;
        }

        final World world = context.getWorld();
        if (!world.isClient()) {
            final Direction playerFacing = context.getPlayerFacing();
            final BlockPos pos = context.getBlockPos().offset(context.getSide());
            final int placeFlags = 2 | 16;
            final ArrayList<BlockPos> placed = new ArrayList<>();

            for (int x = 0; x < horizontalBlocks; x++) {
                final BlockPos newPos = pos.offset(playerFacing.rotateYClockwise(), x);

                for (int y = 0; y < 2; y++) {
                    BlockState state = getBlockStateFromItem()
                            .with(new Property<>(BlockPSDAPGBase.FACING.data), playerFacing.data)
                            .with(new Property<>(HALF.data), y == 1 ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER);
                    if (type.isLift) {
                        state = state.with(new Property<>(BlockPSDAPGDoorBase.UNLOCKED.data), true);
                    }
                    final BlockPos placePos = newPos.up(y);
                    if (item.isDoor) {
                        BlockState neighborState = state.with(new Property<>(SIDE.data), x == 0 ? EnumSide.LEFT : EnumSide.RIGHT);
                        if (type.isOdd) {
                            neighborState = neighborState.with(new Property<>(TripleHorizontalBlock.CENTER.data), x > 0 && x < horizontalBlocks - 1);
                        }
                        world.setBlockState(placePos, neighborState, placeFlags);
                    } else {
                        world.setBlockState(placePos, state.with(new Property<>(SIDE_EXTENDED.data), EnumSide.SINGLE), placeFlags);
                    }
                    placed.add(placePos);
                }

                if (type.isPSD) {
                    final BlockPos topPos = newPos.up(2);
                    world.setBlockState(topPos, BlockPSDTop.getActualState(WorldAccess.cast(world), topPos), placeFlags);
                    placed.add(topPos);
                }
            }

            for (final BlockPos placedPos : placed) {
                final BlockState placedState = world.getBlockState(placedPos);
                world.updateNeighbors(placedPos, placedState.getBlock());
            }

            context.getStack().decrement(1);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable World world, List<MutableText> tooltip, TooltipContext options) {
        tooltip.add((type.isLift ? type.isOdd ? TranslationProvider.TOOLTIP_MTR_RAILWAY_SIGN_ODD : TranslationProvider.TOOLTIP_MTR_RAILWAY_SIGN_EVEN : item.translationKey).getMutableText().formatted(TextFormatting.GRAY));
    }

    private BlockState getBlockStateFromItem() {
        switch (type) {
            case HITACHI_B85_DOOR_1:
                return Blocks.HITACHI_B85_DOOR_1.get().getDefaultState();
            case KONE_M_DOOR_1:
                return top.xfunny.mod.Blocks.KONE_M_DOOR_1.get().getDefaultState();
            case SCHINDLER_QKS9_DOOR_1:
                return top.xfunny.mod.Blocks.SCHINDLER_QKS9_DOOR_1.get().getDefaultState();
            case MITSUBISHI_NEXWAY_DOOR_1:
                return top.xfunny.mod.Blocks.MITSUBISHI_NEXWAY_DOOR_1.get().getDefaultState();
            case OTIS_E411_US_DOOR_1:
                return top.xfunny.mod.Blocks.OTIS_E411_US_DOOR_1.get().getDefaultState();
            default:
                return org.mtr.mapping.holder.Blocks.getAirMapped().getDefaultState();
        }
    }

    public enum EnumPSDAPGType {

        SCHINDLER_QKS9_DOOR_1(false, false, true), MITSUBISHI_NEXWAY_DOOR_1(false, false, true), KONE_M_DOOR_1(false, false, true), HITACHI_B85_DOOR_1(false, false, true), OTIS_E411_US_DOOR_1(false, false, true),
        ;


        private final boolean isPSD;
        private final boolean isOdd;
        private final boolean isLift;

        EnumPSDAPGType(boolean isPSD, boolean isOdd, boolean isLift) {
            this.isPSD = isPSD;
            this.isOdd = isOdd;
            this.isLift = isLift;
        }
    }

    public enum EnumPSDAPGItem implements StringIdentifiable {
        //todo
        PSD_APG_DOOR(TranslationProvider.TOOLTIP_MTR_PSD_APG_DOOR, "psd_apg_door", true),
        PSD_APG_GLASS(TranslationProvider.TOOLTIP_MTR_PSD_APG_GLASS, "psd_apg_glass", false),
        PSD_APG_GLASS_END(TranslationProvider.TOOLTIP_MTR_PSD_APG_GLASS_END, "psd_apg_glass_end", false);

        public final TranslationProvider.TranslationHolder translationKey;
        private final String name;
        private final boolean isDoor;

        EnumPSDAPGItem(TranslationProvider.TranslationHolder translationKey, String name, boolean isDoor) {
            this.translationKey = translationKey;
            this.name = name;
            this.isDoor = isDoor;
        }

        @Nonnull
        @Override
        public String asString2() {
            return name;
        }
    }
}
