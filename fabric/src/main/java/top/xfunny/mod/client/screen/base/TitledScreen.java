package top.xfunny.mod.client.screen.base;

import org.jetbrains.annotations.NotNull;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.GraphicsHolder;
import top.xfunny.mod.client.screen.RenderHelper;

public abstract class TitledScreen extends BaseScreen {
    public static final int TEXT_PADDING = 10;
    public static final int TITLE_SCALE = 2;

    @Override
    public void render(@NotNull GraphicsHolder graphicsHolder, int mouseX, int mouseY, float tickDelta) {
        drawTitle(graphicsHolder);
        drawSubtitle(graphicsHolder);

        super.render(graphicsHolder, mouseX, mouseY, tickDelta);
    }

    private void drawTitle(GraphicsHolder graphicsHolder) {
        MutableText titleText = getScreenTitle();
        graphicsHolder.push();
        graphicsHolder.translate(width / 2.0, TEXT_PADDING, 0);
        graphicsHolder.scale(TITLE_SCALE, TITLE_SCALE, TITLE_SCALE);
        RenderHelper.scaleToFit(graphicsHolder, GraphicsHolder.getTextWidth(titleText), width / (float) TITLE_SCALE, true);
        graphicsHolder.drawCenteredText(titleText, 0, 0, 0xFFFFFFFF);
        graphicsHolder.pop();
    }

    private void drawSubtitle(GraphicsHolder graphicsHolder) {
        double titleHeight = (RenderHelper.lineHeight * TITLE_SCALE);
        MutableText subtitleText = getScreenSubtitle();
        graphicsHolder.push();
        graphicsHolder.translate(width / 2.0, (TEXT_PADDING * 1.5) + titleHeight, 0);
        RenderHelper.scaleToFit(graphicsHolder, GraphicsHolder.getTextWidth(subtitleText), width, true);
        graphicsHolder.drawCenteredText(subtitleText, 0, 0, 0xFFFFFFFF);
        graphicsHolder.pop();
    }

    public abstract MutableText getScreenTitle();

    public abstract MutableText getScreenSubtitle();
}
