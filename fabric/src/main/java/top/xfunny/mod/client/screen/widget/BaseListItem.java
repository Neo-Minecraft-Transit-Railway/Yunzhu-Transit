package top.xfunny.mod.client.screen.widget;

import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.GuiDrawing;

public abstract class BaseListItem {
    public final int height;

    public BaseListItem(int height) {
        super();
        this.height = height;
    }

    public BaseListItem() {
        this(26);
    }

    public void draw(GraphicsHolder graphicsHolder, GuiDrawing guiDrawing, int entryX, int entryY, int width, int height, int mouseX, int mouseY, boolean widgetVisible, float tickDelta) {
    }

    public abstract void positionChanged(int i, int entryY);
}
