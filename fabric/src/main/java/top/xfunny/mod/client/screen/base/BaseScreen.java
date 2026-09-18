package top.xfunny.mod.client.screen.base;

import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.Screen;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mod.data.IGui;

public abstract class BaseScreen extends ScreenExtension implements IGui {
    private Screen previousScreen = null;

    public BaseScreen() {
        super();
    }

    public BaseScreen withPreviousScreen(Screen screen) {
        this.previousScreen = screen;
        return this;
    }

    @Override
    public void onClose2() {
        MinecraftClient.getInstance().openScreen(previousScreen);
    }
}
