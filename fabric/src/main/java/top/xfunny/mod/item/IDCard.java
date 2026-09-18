package top.xfunny.mod.item;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mapping.mapper.TextHelper;
import top.xfunny.mod.block.SchindlerZLine3Keypad1;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class IDCard extends ItemExtension {

    public static final String FLOOR_TAG = "floor";
    public static final String ACCESS_TAG = "access_level";

    public IDCard(ItemSettings itemSettings) {
        super(itemSettings.maxCount(1)); // 限制堆叠数为1
    }

    /* 静态工具方法 */
    public static void setFloor(ItemStack stack, int floor) {
        stack.getOrCreateTag().putInt(FLOOR_TAG, floor);
    }

    public static void setAccessLevel(ItemStack stack, int level) {
        stack.getOrCreateTag().putInt(ACCESS_TAG, level);
    }

    public static int getFloor(ItemStack stack) {
        return stack.getOrCreateTag().getInt(FLOOR_TAG);
    }

    public static int getAccessLevel(ItemStack stack) {
        return stack.getOrCreateTag().getInt(ACCESS_TAG);
    }

    @Nonnull
    @Override
    public ActionResult useOnBlock2(ItemUsageContext context) {
        if (!context.getWorld().isClient()) {
            if (isValidBlock(context)) {
                CompoundTag tag = context.getStack().getOrCreateTag();

                // 默认值设置
                if (!tag.contains(FLOOR_TAG)) {
                    tag.putInt(FLOOR_TAG, 1);
                }
                if (!tag.contains(ACCESS_TAG)) {
                    tag.putInt(ACCESS_TAG, 1);
                }

                // 发送当前卡片信息
                PlayerEntity player = context.getPlayer();
                if (player != null) {
                    player.sendMessage(
                            Text.cast(TextHelper.translatable(
                                    "message.id_card.current_status",
                                    tag.getInt(FLOOR_TAG),
                                    tag.getInt(ACCESS_TAG)
                            )),
                            true
                    );
                }

                return ActionResult.SUCCESS;
            }
            return ActionResult.FAIL;
        }
        return super.useOnBlock2(context);
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable World world, List<MutableText> tooltip, TooltipContext options) {
        CompoundTag tag = stack.getOrCreateTag();
        int floor = tag.contains(FLOOR_TAG) ? tag.getInt(FLOOR_TAG) : 0;
        int accessLevel = tag.contains(ACCESS_TAG) ? tag.getInt(ACCESS_TAG) : 1;

        tooltip.add(TextHelper.translatable("tooltip.id_card.access", floor).formatted(TextFormatting.GRAY));
        tooltip.add(TextHelper.translatable("tooltip.id_card.floors", accessLevel).formatted(TextFormatting.GRAY));
    }

    private boolean isValidBlock(ItemUsageContext context) {
        Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();
        return block.data instanceof SchindlerZLine3Keypad1;
    }
}