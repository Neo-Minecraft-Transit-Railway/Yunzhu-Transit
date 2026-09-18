package top.xfunny.core.servlet;

import org.mtr.core.serializer.JsonReader;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.tool.Utilities;
import top.xfunny.core.operation.YteDataRequest;
import top.xfunny.core.operation.YteUpdateDataRequest;
import top.xfunny.core.simulation.YteSimulator;

public final class YteOperationProcessor {

    public static SerializedDataBase process(String key, SerializedDataBase data, YteSimulator simulator) {
        final JsonReader jsonReader = new JsonReader(Utilities.getJsonObjectFromData(data));

        switch (key) {
            case YteOperationType.GET_DATA:
                return new YteDataRequest(jsonReader, simulator).getData();
            case YteOperationType.UPDATE_DATA:
                return new YteUpdateDataRequest(jsonReader, simulator).update();
            default:
                return null;
        }
    }
}
