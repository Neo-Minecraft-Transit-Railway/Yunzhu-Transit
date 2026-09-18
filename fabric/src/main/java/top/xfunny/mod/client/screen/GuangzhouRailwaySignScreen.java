package top.xfunny.mod.client.screen;

import org.jetbrains.annotations.NotNull;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ButtonWidgetExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.TextHelper;
import top.xfunny.mod.client.screen.base.BaseConfigScreen;
import top.xfunny.mod.client.screen.widget.ContentItem;

public class GuangzhouRailwaySignScreen extends BaseConfigScreen {
    private final ButtonWidgetExtension signSelected;
    protected String signId;

    public GuangzhouRailwaySignScreen(BlockPos blockPos) {
        super(blockPos);
        this.signId = null;

        signSelected = new ButtonWidgetExtension(0, 0, 60, 20, TextHelper.translatable("selectWorld.edit"), (btn) ->
                MinecraftClient.getInstance().openScreen(
                        new Screen(new SignSettingScreen(blockPos, signId, (str) ->
                                this.signId = str).withPreviousScreen(new Screen(this)))
                ));
    }

    @Override
    protected void init2() {
        super.init2();
    }

    @Override
    public void render(@NotNull GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
        renderBackground(graphicsHolder);
        super.render(graphicsHolder, mouseX, mouseY, delta);
    }

    @Override
    public void onClose2() {
        super.onClose2();
    }

    public MutableText getScreenTitle() {
        return TextHelper.translatable("测试屏幕");
    }

    @Override
    public void addItemConfig() {
        addChild(new ClickableWidget(signSelected));
        ContentItem chooseSignItem = new ContentItem(TextHelper.translatable(signId), signSelected);

        if (signId != null) {
            chooseSignItem.setIcon(new Identifier("mtr", "textures/block/sign/" + signId + ".png"));
        }

        listViewWidget.addCategory(TextHelper.translatable("gui.yte.signs.list.signs_setting"));
        listViewWidget.add(chooseSignItem);
        listViewWidget.addCategory(TextHelper.translatable("gui.yte.signs.list.signs_display"));
    }

}