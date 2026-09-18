package top.xfunny.mod.client.screen.widget;

import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.GuiDrawing;
import top.xfunny.mod.client.screen.GuiHelper;

public class CategoryItem extends BaseListItem {
    public final MutableText title;

    public CategoryItem(MutableText title, int height) {
        super(height);
        this.title = title;
    }

    public CategoryItem(MutableText title) {
        super();
        this.title = title;
    }

    @Override
    public void draw(GraphicsHolder graphicsHolder, GuiDrawing guiDrawing, int entryX, int entryY, int width, int height, int mouseX, int mouseY, boolean widgetVisible, float tickDelta) {
        GuiHelper.drawRectangle(guiDrawing, entryX, entryY, width, this.height, 0x99999999);
        graphicsHolder.drawCenteredText(title, (entryX + width / 2), entryY - (8 / 2) + (this.height / 2), 0xFFFFFFFF);
    }

    @Override
    public void positionChanged(int entryX, int entryY) {
    }
}
