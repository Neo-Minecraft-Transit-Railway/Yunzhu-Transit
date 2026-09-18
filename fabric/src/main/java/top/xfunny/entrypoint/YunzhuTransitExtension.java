package top.xfunny.entrypoint;

import net.fabricmc.api.ModInitializer;
import top.xfunny.mod.Init;


public class YunzhuTransitExtension implements ModInitializer {


    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        Init.init();


    }
}