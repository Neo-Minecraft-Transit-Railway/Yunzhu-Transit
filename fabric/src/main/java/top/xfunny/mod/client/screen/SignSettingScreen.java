package top.xfunny.mod.client.screen;

import org.jetbrains.annotations.NotNull;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ButtonWidgetExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.TextHelper;

import top.xfunny.mod.client.screen.base.BaseConfigScreen;
import top.xfunny.mod.client.screen.widget.ContentItem;
import top.xfunny.mod.client.screen.widget.CustomSignsManager;

import java.util.function.Consumer;

import static top.xfunny.mod.client.screen.widget.CustomSignsManager.getDefaultSignListSize;

public class SignSettingScreen extends BaseConfigScreen {
    private final ObjectArrayList<String> allSignIds = CustomSignsManager.getSignList();
    private final ButtonWidgetExtension[] chooseSignButton;
    private final Consumer<String> callback;
    private String selectedSignId;

    public SignSettingScreen(BlockPos blockPos, String selectedSignId, Consumer<String> callback) {
        super(blockPos);
        this.selectedSignId = selectedSignId;
        this.callback = callback;
        CustomSignsManager.loader();

        chooseSignButton = new ButtonWidgetExtension[allSignIds.size()];
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

    private void drawSignItems() {
        listViewWidget.addCategory(TextHelper.translatable("gui.yte.built_in_style"));

        for (int i = 0; i < allSignIds.size(); i++) {
            String signId = allSignIds.get(i);
            if (signId == null || signId.isEmpty()) continue;

            if (chooseSignButton[i] == null) {
                final int index = i;
                chooseSignButton[i] = new ButtonWidgetExtension(
                        0, 0, 60, 20,
                        TextHelper.translatable("gui.yte.select"),
                        button -> setSelectedSign(allSignIds.get(index))
                );
            }

            if (signId.equals(selectedSignId)) {
                chooseSignButton[i].setMessage2(Text.cast(TextHelper.translatable("gui.yte.selected")));
                chooseSignButton[i].active = false;
            } else {
                chooseSignButton[i].setMessage2(Text.cast(TextHelper.translatable("gui.yte.select")));
                chooseSignButton[i].active = true;
            }

            Identifier SIGN_ICON = new Identifier("mtr:textures/block/sign/" + signId + ".png");
            String displaySignId = signId.replace("_", " ");

            if (i == getDefaultSignListSize()) {
                listViewWidget.addCategory(TextHelper.translatable("gui.yte.extended_style"));
            }

            addChild(new ClickableWidget(chooseSignButton[i]));
            ContentItem chooseSignItem = new ContentItem(TextHelper.translatable(displaySignId), chooseSignButton[i]);
            chooseSignItem.setIcon(SIGN_ICON);
            listViewWidget.add(chooseSignItem);
        }
    }

    public MutableText getScreenTitle() {
        return TextHelper.translatable("gui.yte.signs.list.signs_setting");
    }

    public MutableText getScreenSubtitle() {
        return TextHelper.translatable("gui.yte.signs.subtitle", selectedSignId);
    }

    @Override
    public void addItemConfig() {
        drawSignItems();
        listViewWidget.addCategory(TextHelper.translatable("更多设置"));
    }

    public void setSelectedSign(String signId) {
        callback.accept(signId);
        selectedSignId = signId;
        for (int i = 0; i < allSignIds.size(); i++) {
            if (allSignIds.get(i).equals(selectedSignId)) {
                chooseSignButton[i].setMessage2(Text.cast(TextHelper.translatable("gui.yte.selected")));
                chooseSignButton[i].active = false;
            } else {
                chooseSignButton[i].setMessage2(Text.cast(TextHelper.translatable("gui.yte.select")));
                chooseSignButton[i].active = true;
            }
        }
    }
}