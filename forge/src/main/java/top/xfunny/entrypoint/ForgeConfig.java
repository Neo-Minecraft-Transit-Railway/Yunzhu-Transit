package top.xfunny.entrypoint;

/*import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.Screen;
import top.xfunny.mod.client.screen.ClientConfigScreen;

#if MC_VERSION <= "11605"
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.ExtensionPoint;
#elif MC_VERSION <= "11701"
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fmlclient.ConfigGuiHandler;
#elif MC_VERSION <= "11802"
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.client.ConfigGuiHandler;
#else
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
#endif

public class ForgeConfig {
    public static void registerConfig() {
        #if MC_VERSION <= "11605"
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> {
            return new ClientConfigScreen(new BlockPos(0, 0, 0)).withPreviousScreen(new Screen(screen));
        });
        #elif MC_VERSION <= "11802"
        ModLoadingContext.get().registerExtensionPoint(ConfigGuiHandler.ConfigGuiFactory.class, () -> new ConfigGuiHandler.ConfigGuiFactory((minecraft, screen) -> new ClientConfigScreen(new BlockPos(0, 0, 0)).withPreviousScreen(new Screen(screen))));
        #elif MC_VERSION <= "12004"
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new ClientConfigScreen(new BlockPos(0, 0, 0)).withPreviousScreen(new Screen(screen))));
        #endif
    }
}
*/
