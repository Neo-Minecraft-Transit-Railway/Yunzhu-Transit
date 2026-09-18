package top.xfunny.core.operation;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.serializer.WriterBase;
import org.mtr.core.tool.Utilities;
import top.xfunny.core.data.YteData;
import top.xfunny.core.data.YteLiftConfig;

public final class YteUpdateDataRequest implements SerializedDataBase {

    private YteLiftConfig liftConfig;
    private final YteData data;

    private static final String KEY_LIFT_CONFIG = "lift_config";

    public YteUpdateDataRequest(YteData data) {
        this.data = data;
    }

    public YteUpdateDataRequest(ReaderBase readerBase, YteData data) {
        this.data = data;
        updateData(readerBase);
    }

    public YteUpdateDataRequest(YteLiftConfig liftConfig, YteData data) {
        this.liftConfig = liftConfig;
        this.data = data;
    }

    @Override
    public void updateData(ReaderBase readerBase) {
        final ReaderBase child = readerBase.getChild(KEY_LIFT_CONFIG);
        if (child != null) {
            liftConfig = new YteLiftConfig(child);
        }
    }

    @Override
    public void serializeData(WriterBase writerBase) {
        if (liftConfig != null) {
            liftConfig.serializeData(writerBase.writeChild(KEY_LIFT_CONFIG));
        }
    }

    public YteUpdateDataResponse update() {
        if (liftConfig == null) {
            return new YteUpdateDataResponse(data);
        }

        final long id = liftConfig.getId();
        final YteLiftConfig existing = data.liftConfigIdMap.get(id);
        if (existing != null) {
            data.liftConfigs.remove(existing);
        }
        data.liftConfigs.add(liftConfig);
        data.sync();

        return new YteUpdateDataResponse(data);
    }

    public YteLiftConfig getLiftConfig() {
        return liftConfig;
    }
}
