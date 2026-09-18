package top.xfunny.mod.config;

import com.google.gson.JsonObject;
import org.mtr.mapping.holder.MinecraftClient;

import java.nio.file.Path;

public class ClientConfig extends Config {
    private static final Path CONFIG_PATH = MinecraftClient.getInstance().getRunDirectoryMapped().toPath().resolve("config").resolve("yunzhu_transit_extension.json");

    //隐藏测试水印
    public boolean hideTestWatermark;

    public ClientConfig() {
        super(CONFIG_PATH);
    }

    @Override
    protected JsonObject getTempConfigItems() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("hideTestWatermark", this.hideTestWatermark);
        return jsonObject;
    }

    @Override
    protected void setTempConfigItems(JsonObject jsonObject) {
        this.hideTestWatermark = jsonObject.get("hideTestWatermark").getAsBoolean();
    }
}
