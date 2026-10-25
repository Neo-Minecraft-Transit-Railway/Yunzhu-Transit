package top.xfunny.mod.item;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mapping.mapper.ItemStackNbtHelper;
import org.mtr.mod.generated.lang.TranslationProvider;
import top.xfunny.mod.block.base.LiftButtonsBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class YTEItemBlockClickingBase extends ItemExtension {
	public static final String TAG_POS = "pos";
	public static final String TAG_SECOND_POS = "secondPos";

	public YTEItemBlockClickingBase(ItemSettings itemSettings) {
		super(itemSettings);
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock2(ItemUsageContext context) {
		final ItemStack stack = context.getStack();
		final CompoundTag compoundTag = ItemStackNbtHelper.getOrCreateTag(stack);
		final boolean isClient = context.getWorld().isClient();

		if (compoundTag.contains(TAG_SECOND_POS)) {
			if (clickCondition(context)) {
				final BlockPos posEnd = BlockPos.fromLong(compoundTag.getLong(TAG_POS));
				final BlockPos posStart = BlockPos.fromLong(compoundTag.getLong(TAG_SECOND_POS));
				final BlockPos posThird = context.getBlockPos();
				if (!isClient) {
					onThirdClick(context, posStart, posEnd, posThird, compoundTag);
				}
				clearSelection(stack, compoundTag);
				return ActionResult.SUCCESS;
			}
			clearSelection(stack, compoundTag);
			return ActionResult.FAIL;
		}

		if (compoundTag.contains(TAG_POS)) {
			if (!clickCondition(context)) {
				clearSelection(stack, compoundTag);
				return ActionResult.FAIL;
			}

			final BlockPos posEnd = BlockPos.fromLong(compoundTag.getLong(TAG_POS));
			final BlockPos posStart = context.getBlockPos();
			final World world = context.getWorld();

			final Block blockEnd = world.getBlockState(posEnd).getBlock();
			final Block blockStart = world.getBlockState(posStart).getBlock();

			boolean triggerThirdClick = false;
			if (blockEnd.data instanceof LiftButtonsBase && blockStart.data instanceof LiftButtonsBase) {
				final boolean endAllowPress = ((LiftButtonsBase) blockEnd.data).allowPress;
				final boolean startAllowPress = ((LiftButtonsBase) blockStart.data).allowPress;
				if (endAllowPress != startAllowPress) {
					triggerThirdClick = true;
				}
			}

			if (triggerThirdClick) {
				compoundTag.putLong(TAG_SECOND_POS, posStart.asLong());
				ItemStackNbtHelper.saveTag(stack, compoundTag);
				return ActionResult.SUCCESS;
			}

			if (!isClient) {
				onEndClick(context, posEnd, compoundTag);
			}
			clearSelection(stack, compoundTag);
			return ActionResult.SUCCESS;
		}

		if (clickCondition(context)) {
			if (!isClient) {
				onStartClick(context, compoundTag);
			}
			compoundTag.putLong(TAG_POS, context.getBlockPos().asLong());
			ItemStackNbtHelper.saveTag(stack, compoundTag);
			return ActionResult.SUCCESS;
		}

		return isClient ? super.useOnBlock2(context) : ActionResult.FAIL;
	}

	private static void clearSelection(ItemStack stack, CompoundTag compoundTag) {
		compoundTag.remove(TAG_SECOND_POS);
		compoundTag.remove(TAG_POS);
		ItemStackNbtHelper.saveTag(stack, compoundTag);
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable World world, List<MutableText> tooltip, TooltipContext options) {
		final CompoundTag compoundTag = ItemStackNbtHelper.getTag(stack);
		if (compoundTag.contains(TAG_POS)) {
			final long posLong = compoundTag.getLong(TAG_POS);
			tooltip.add(TranslationProvider.TOOLTIP_MTR_SELECTED_BLOCK.getMutableText(BlockPos.fromLong(posLong).toShortString()).formatted(TextFormatting.GOLD));
		}
	}

	protected abstract void onStartClick(ItemUsageContext context, CompoundTag compoundTag);

	protected abstract void onEndClick(ItemUsageContext context, BlockPos posEnd, CompoundTag compoundTag);

	protected abstract void onThirdClick(ItemUsageContext context, BlockPos pos1, BlockPos pos2, BlockPos pos3, CompoundTag compoundTag);

	protected abstract boolean clickCondition(ItemUsageContext context);
}
