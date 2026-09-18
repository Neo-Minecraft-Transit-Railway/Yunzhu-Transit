package top.xfunny.mod.packet;

import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.tool.PacketBufferReceiver;
import top.xfunny.core.operation.YteDataRequest;
import top.xfunny.core.operation.YteDataResponse;
import top.xfunny.core.servlet.YteOperationType;
import top.xfunny.mod.client.YteMinecraftClientData;

import javax.annotation.Nonnull;

public final class YtePacketRequestData extends YtePacketRequestResponseBase {

    public YtePacketRequestData(PacketBufferReceiver packetBufferReceiver) {
        super(packetBufferReceiver);
    }

    public YtePacketRequestData() {
        super("{}");
    }

    private YtePacketRequestData(String content) {
        super(content);
    }

    @Override
    protected void runServerInbound(ServerWorld serverWorld, JsonObject jsonObject) {
    }

    @Override
    protected void runClientInbound(JsonReader jsonReader) {
        new YteDataResponse(jsonReader, YteMinecraftClientData.getInstance()).write();
    }

    @Override
    protected YtePacketRequestResponseBase getInstance(String content) {
        return new YtePacketRequestData(content);
    }

    @Override
    protected SerializedDataBase getDataInstance(JsonReader jsonReader) {
        return new YteDataRequest(jsonReader, YteMinecraftClientData.getInstance());
    }

    @Nonnull
    @Override
    protected String getKey() {
        return YteOperationType.GET_DATA;
    }

    @Override
    protected ResponseType responseType() {
        return ResponseType.PLAYER;
    }
}
