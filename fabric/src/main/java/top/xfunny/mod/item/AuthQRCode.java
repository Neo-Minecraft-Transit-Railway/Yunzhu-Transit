package top.xfunny.mod.item;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mapping.mapper.TextHelper;

import javax.annotation.Nullable;
import java.util.List;

public class AuthQRCode extends ItemExtension {

    public AuthQRCode(ItemSettings itemSettings) {
        super(itemSettings.maxCount(1));
    }

    @Override
    public void addTooltips(ItemStack stack, @Nullable World world, List<MutableText> tooltip, TooltipContext options) {
        tooltip.add(TextHelper.translatable("未使用").formatted(TextFormatting.GRAY));
    }
}