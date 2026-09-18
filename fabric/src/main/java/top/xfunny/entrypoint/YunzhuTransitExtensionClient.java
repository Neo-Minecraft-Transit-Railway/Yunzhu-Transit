package top.xfunny.entrypoint;

import net.fabricmc.api.ClientModInitializer;

public class YunzhuTransitExtensionClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        top.xfunny.mod.client.InitClient.init();
    }
}