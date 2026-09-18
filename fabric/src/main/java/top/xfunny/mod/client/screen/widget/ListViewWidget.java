package top.xfunny.mod.client.screen.widget;

import org.jetbrains.annotations.NotNull;
import org.mtr.mapping.holder.MathHelper;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.ButtonWidgetExtension;
import org.mtr.mapping.mapper.ClickableWidgetExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.GuiDrawing;
import top.xfunny.mod.client.screen.GuiHelper;

import java.util.ArrayList;
import java.util.List;

public class ListViewWidget extends ClickableWidgetExtension {
    public static final int ENTRY_PADDING = 5;
    public static final int SCROLLBAR_WIDTH = 5;
    private final List<BaseListItem> displayedEntryList = new ArrayList<>();
    private final List<BaseListItem> entryList = new ArrayList<>();
    protected double currentScroll = 0;
    private final String searchTerm = "";
    private boolean scrollbarDragging = false;

    public ListViewWidget() {
        super(0, 0, 0, 0);
    }


    public ListViewWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void setXYSize(int x, int y, int width, int height) {
        setX2(x);
        setY2(y);
        setWidth2(width);
        setHeightMapped(height);
        refreshDisplay();
    }

    public void add(MutableText text, ButtonWidgetExtension widget) {
        add(new ContentItem(text, widget));
        refreshDisplay();
    }

    public void add(BaseListItem listItem) {
        entryList.add(listItem);
        refreshDisplay();
    }

    public void addCategory(MutableText text) {
        entryList.add(new CategoryItem(text));
        refreshDisplay();
    }

    public void clear() {
        entryList.clear();
        refreshDisplay();
    }

    public void refreshDisplay() {
        displayedEntryList.addAll(entryList);
        entryList.clear();
        entryList.addAll(displayedEntryList);
        displayedEntryList.clear();
        updateItemPositions();
        setScroll(currentScroll);
    }

    private void updateItemPositions() {
        int incY = 0;

        for (BaseListItem listItem : entryList) {
            int entryX = getX2();
            int entryY = getY2() + incY - (int) currentScroll;
            listItem.positionChanged(entryX + width - ENTRY_PADDING, entryY);
            incY += listItem.height;
        }
    }

    @Override
    public void render(@NotNull GraphicsHolder graphicsHolder, int mouseX, int mouseY, float tickDelta) {
        GuiHelper.drawRectangle(new GuiDrawing(graphicsHolder), getX2(), getY2(), width, height, 0x4C4C4C4C);
        renderContent(graphicsHolder, mouseX, mouseY, tickDelta);
//        super.render(graphicsHolder, mouseX, mouseY, tickDelta);
        renderScrollBar(graphicsHolder, mouseX, mouseY, tickDelta);
    }

    public void renderContent(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float tickDelta) {
        GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
        int incY = 0;

        for (BaseListItem listItem : entryList) {
            int scrollbarWidth = contentOverflowed() ? SCROLLBAR_WIDTH : 0;
            int listItemWidth = width - scrollbarWidth;
            int entryX = getX2();
            int entryY = getY2() + incY - (int) currentScroll;


            listItem.draw(graphicsHolder, guiDrawing, entryX, entryY, listItemWidth, height, mouseX, mouseY, true, tickDelta);
            listItem.positionChanged(entryX + listItemWidth - ENTRY_PADDING, entryY);
            incY += listItem.height;
        }
    }

    //TODO: 添加滚动
    @Override
    public boolean mouseScrolled2(double mouseX, double mouseY, double amount) {
        double oldScroll = currentScroll;
        if (contentOverflowed()) {
            amount *= 26;
            setScroll(oldScroll - amount);
        }
        return oldScroll != currentScroll;
    }

    @Override
    public boolean mouseClicked2(double mouseX, double mouseY, int button) {
        scrollbarDragging = button == 0 && isScrollbarHover(mouseX, mouseY);
        return true;
    }

    public void renderScrollBar(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float tickDelta) {
        if (!contentOverflowed()) return;

        GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
        int entryHeight = getContentHeight();
        int visibleHeight = getHeight2();
        // 计算滚动条滑块高度
        double scrollbarHeight = visibleHeight * ((double) visibleHeight / entryHeight);
        double bottomOffset = currentScroll / (entryHeight - visibleHeight);
        double yOffset = bottomOffset * (visibleHeight - scrollbarHeight);

        GuiHelper.drawRectangle(guiDrawing, getX2() + getWidth2() - SCROLLBAR_WIDTH, getY2() + yOffset, SCROLLBAR_WIDTH, scrollbarHeight, isScrollbarHover(mouseX, mouseY) ? 0xFFD1D1D1 : 0xFF9F9F9F);
    }

    protected int getContentHeight() {
        int entryHeight = 0;
        for (BaseListItem listItem : entryList) {
            entryHeight += listItem.height;
        }
        return entryHeight;
    }

    protected boolean contentOverflowed() {
        return getContentHeight() > getHeight2();
    }

    private boolean isScrollbarHover(double mouseX, double mouseY) {
        return mouseX >= getX2() + getWidth2() - SCROLLBAR_WIDTH && mouseY >= getY2() && mouseX < getX2() + getWidth2() && mouseY < getY2() + getContentHeight();
    }

    public void setScroll(double scroll) {
        int maxScroll = Math.max(0, getContentHeight() - getHeight2());
        currentScroll = MathHelper.clamp(scroll, 0, maxScroll);
    }
}
