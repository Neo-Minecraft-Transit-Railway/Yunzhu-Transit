package top.xfunny.entrypoint;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import top.xfunny.mod.Init;
import top.xfunny.mod.client.InitClient;

@Mod(Init.MOD_ID)
public class MainForge {

    public MainForge() {
        Init.init();
        //ForgeConfig.registerConfig();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> InitClient::init);
    }
}
