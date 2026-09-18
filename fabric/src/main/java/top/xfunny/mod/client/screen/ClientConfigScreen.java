package top.xfunny.mod.client.screen;

import org.jetbrains.annotations.NotNull;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.ButtonWidget;
import org.mtr.mapping.holder.ClickableWidget;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.ButtonWidgetExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.generated.lang.TranslationProvider;
import top.xfunny.mod.Keys;
import top.xfunny.mod.client.InitClient;
import top.xfunny.mod.client.screen.base.BaseConfigScreen;
import top.xfunny.mod.client.screen.widget.ContentItem;

import static top.xfunny.mod.client.screen.RenderHelper.lineHeight;

public class ClientConfigScreen extends BaseConfigScreen {
    private static final int BUTTON_HEIGHT = lineHeight * 2;
    private static final int BUTTON_WIDTH = 60;
    private final ButtonWidgetExtension hideTestWatermark;

    public ClientConfigScreen(BlockPos blockPos) {
        super(blockPos);
        hideTestWatermark = new ButtonWidgetExtension(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, TextHelper.literal(""), button -> {
            InitClient.getConfig().hideTestWatermark = !InitClient.getConfig().hideTestWatermark;
            setButtonText(button, InitClient.getConfig().hideTestWatermark);
        });
    }

    private static void setButtonText(ButtonWidget button, boolean state) {
        button.setMessage((state ? TranslationProvider.OPTIONS_MTR_ON : TranslationProvider.OPTIONS_MTR_OFF).getText());
    }

    @Override
    protected void init2() {
        super.init2();
        setButtonText(new ButtonWidget(hideTestWatermark), InitClient.getConfig().hideTestWatermark);
    }

    @Override
    public void render(@NotNull GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
        renderBackground(graphicsHolder);
        super.render(graphicsHolder, mouseX, mouseY, delta);
    }

    @Override
    public void onClose2() {
        super.onClose2();
        InitClient.getConfig().writeConfig();
    }

    public MutableText getScreenTitle() {
        return TextHelper.literal("Yunzhu Transit Extension");
    }

    public MutableText getScreenSubtitle() {
        return TextHelper.translatable("%s.%s", Keys.MOD_VERSION, Keys.BUILD_TIME);
    }

    public void addItemConfig() {
        addChild(new ClickableWidget(hideTestWatermark));
        ContentItem hideWatermark = new ContentItem(TextHelper.translatable("gui.yte.hide_watermark"), hideTestWatermark);
        listViewWidget.add(hideWatermark);
    }
}
