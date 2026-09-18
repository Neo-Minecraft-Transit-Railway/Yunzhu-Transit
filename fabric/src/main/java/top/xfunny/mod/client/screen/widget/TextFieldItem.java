package top.xfunny.mod.client.screen.widget;

import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.GuiDrawing;
import org.mtr.mapping.mapper.TextFieldWidgetExtension;

public class TextFieldItem extends BaseListItem {
    public final MutableText title;
    public final TextFieldWidgetExtension widget;

    public TextFieldItem(MutableText title, TextFieldWidgetExtension widget, int height) {
        super(height);
        this.title = title;
        this.widget = widget;
    }

    public TextFieldItem(MutableText title, TextFieldWidgetExtension widget) {
        this(title, widget, 26);
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
    }
}
