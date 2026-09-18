package top.xfunny.core.operation;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.serializer.WriterBase;
import top.xfunny.core.data.YteData;

public final class YteDataRequest implements SerializedDataBase {

    private final YteData data;

    public YteDataRequest(YteData data) {
        this.data = data;
    }

    public YteDataRequest(ReaderBase readerBase, YteData data) {
        this.data = data;
        updateData(readerBase);
    }

    @Override
    public void updateData(ReaderBase readerBase) {
    }

    @Override
    public void serializeData(WriterBase writerBase) {
    }

    public YteDataResponse getData() {
        return new YteDataResponse(data);
    }
}
