package top.xfunny.mod.client;

import top.xfunny.core.data.YteClientData;
import top.xfunny.core.data.YteLiftConfig;

public final class YteMinecraftClientData extends YteClientData {

    private static YteMinecraftClientData instance = new YteMinecraftClientData();

    @Override
    public void sync() {
        super.sync();
    }

    /**
     * 根据 liftId (MTR 的 getId() 返回值) 获取配置，如果不存在则返回 null
     */
    public YteLiftConfig getConfig(long liftId) {
        return liftConfigIdMap.get(liftId);
    }

    public static YteMinecraftClientData getInstance() {
        return instance;
    }

    public static void reset() {
        instance = new YteMinecraftClientData();
    }
}
