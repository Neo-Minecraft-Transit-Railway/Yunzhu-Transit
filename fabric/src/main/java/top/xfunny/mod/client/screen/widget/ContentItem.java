package top.xfunny.mod.client.screen.widget;

import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.ButtonWidgetExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.GuiDrawing;
import top.xfunny.mod.client.screen.GuiHelper;

import static top.xfunny.mod.client.screen.widget.ListViewWidget.ENTRY_PADDING;

public class ContentItem extends BaseListItem {
    public final MutableText title;
    public final ButtonWidgetExtension widget;
    private Identifier textureResource;
    private boolean hasIcon;
    private double hoverOpacity = 0;

    public ContentItem(MutableText title, ButtonWidgetExtension widget, int height) {
        super(height);
        this.title = title;
        this.widget = widget;
    }

    public ContentItem(MutableText title, ButtonWidgetExtension widget) {
        this(title, widget, 26);
    }

    public void setIcon(Identifier textureResource) {
        if (textureResource != null) {
            this.textureResource = textureResource;
            hasIcon = true;
        } else {
            hasIcon = false;
        }
    }

    public boolean hasIcon() {
        return hasIcon;
    }

    @Override
    public void positionChanged(int entryX, int entryY) {
        if (widget != null) {
            int offsetY = (height - widget.getHeight2()) / 2;
            widget.setX2(entryX - widget.getWidth2());
            widget.setY2(entryY + offsetY);
        }
    }

    @Override
    public void draw(GraphicsHolder graphicsHolder, GuiDrawing guiDrawing, int entryX, int entryY, int width, int height, int mouseX, int mouseY, boolean widgetVisible, float tickDelta) {
        super.draw(graphicsHolder, guiDrawing, entryX, entryY, width, height, mouseX, mouseY, widgetVisible, tickDelta);
        drawBackground(guiDrawing, entryX, entryY, width, mouseX, mouseY, tickDelta);
        drawListEntry(graphicsHolder, entryX, entryY, mouseX, mouseY, widgetVisible, tickDelta);
    }

    private void drawListEntry(GraphicsHolder graphicsHolder, int entryX, int entryY, int mouseX, int mouseY, boolean widgetVisible, float tickDelta) {
        if (title != null)
            drawListEntryDescription(graphicsHolder, entryX, entryY);

        if (widget != null) {
            widget.visible = widgetVisible;
            widget.render(graphicsHolder, mouseX, mouseY, tickDelta);
        }
    }

    private void drawListEntryDescription(GraphicsHolder graphicsHolder, int entryX, int entryY) {
        int textHeight = 9;
        int iconSize = hasIcon() ? height - ENTRY_PADDING : 0;
        int textY = (height / 2) - (textHeight / 2) - (ENTRY_PADDING / 2);

        graphicsHolder.push();
        graphicsHolder.translate(entryX, entryY, 0);
        graphicsHolder.translate(ENTRY_PADDING, ENTRY_PADDING / 2.0, 0);

        if (hasIcon()) {
            GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
            guiDrawing.beginDrawingTexture(textureResource);
            guiDrawing.drawTexture(0F, 0F, iconSize, iconSize, 0F, 0F, 1F, 1F);
            guiDrawing.finishDrawingTexture();
            graphicsHolder.translate(iconSize + ENTRY_PADDING, 0, 0);
        }

        graphicsHolder.drawText(title, 0, textY, 0xFFFFFFFF, true, GraphicsHolder.getDefaultLight());
        graphicsHolder.pop();
    }

    private void drawBackground(GuiDrawing guiDrawing, int entryX, int entryY, int width, int mouseX, int mouseY, float tickDelta) {
        double highlightFadeSpeed = (tickDelta / 4);
        boolean entryHovered = mouseX >= entryX && mouseY >= entryY && mouseX < entryX + width && mouseY < entryY + this.height;
        hoverOpacity = entryHovered ? Math.min(1, hoverOpacity + highlightFadeSpeed) : Math.max(0, hoverOpacity - highlightFadeSpeed);

        if (hoverOpacity > 0) drawListEntryHighlight(guiDrawing, entryX, entryY, width, height);
    }

    private void drawListEntryHighlight(GuiDrawing guiDrawing, int x, int y, int width, int height) {
        int highlightAlpha = (int) (100 * hoverOpacity);
        int highlightColor = (highlightAlpha << 24) | (150 << 16) | (150 << 8) | 150;

        GuiHelper.drawRectangle(guiDrawing, x, y, width, height, highlightColor);
    }

}

