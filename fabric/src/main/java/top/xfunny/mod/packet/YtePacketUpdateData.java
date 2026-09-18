package top.xfunny.mod.packet;

import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.mapping.holder.ServerWorld;
import org.mtr.mapping.tool.PacketBufferReceiver;
import top.xfunny.core.data.YteLiftConfig;
import top.xfunny.core.operation.YteUpdateDataRequest;
import top.xfunny.core.operation.YteUpdateDataResponse;
import top.xfunny.core.servlet.YteOperationType;
import top.xfunny.mod.client.YteMinecraftClientData;

import javax.annotation.Nonnull;

public final class YtePacketUpdateData extends YtePacketRequestResponseBase {

    public YtePacketUpdateData(PacketBufferReceiver packetBufferReceiver) {
        super(packetBufferReceiver);
    }

    public YtePacketUpdateData(YteUpdateDataRequest updateDataRequest) {
        super(Utilities.getJsonObjectFromData(updateDataRequest).toString());
    }

    private YtePacketUpdateData(String content) {
        super(content);
    }

    @Override
    protected void runServerInbound(ServerWorld serverWorld, JsonObject jsonObject) {
    }

    @Override
    protected void runClientInbound(JsonReader jsonReader) {
        new YteUpdateDataResponse(jsonReader, YteMinecraftClientData.getInstance()).write();
    }

    @Override
    protected YtePacketRequestResponseBase getInstance(String content) {
        return new YtePacketUpdateData(content);
    }

    @Override
    protected SerializedDataBase getDataInstance(JsonReader jsonReader) {
        return new YteUpdateDataRequest(jsonReader, YteMinecraftClientData.getInstance());
    }

    @Nonnull
    @Override
    protected String getKey() {
        return YteOperationType.UPDATE_DATA;
    }

    @Override
    protected ResponseType responseType() {
        return ResponseType.ALL;
    }
}
