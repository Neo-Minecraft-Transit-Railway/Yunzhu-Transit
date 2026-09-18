package top.xfunny.core.operation;

import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.serializer.WriterBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import top.xfunny.core.data.YteData;
import top.xfunny.core.data.YteLiftConfig;

public final class YteUpdateDataResponse implements SerializedDataBase {

    private final YteData data;
    private final ObjectArrayList<YteLiftConfig> responseConfigs = new ObjectArrayList<>();

    private static final String KEY_LIFT_CONFIGS = "lift_configs";

    public YteUpdateDataResponse(YteData data) {
        this.data = data;
        data.liftConfigs.forEach(config -> responseConfigs.add(config));
    }

    public YteUpdateDataResponse(ReaderBase readerBase, YteData data) {
        this.data = data;
        updateData(readerBase);
    }

    @Override
    public void updateData(ReaderBase readerBase) {
        readerBase.iterateReaderArray(KEY_LIFT_CONFIGS, responseConfigs::clear,
                readerBaseChild -> responseConfigs.add(new YteLiftConfig(readerBaseChild)));
    }

    @Override
    public void serializeData(WriterBase writerBase) {
        writerBase.writeDataset(responseConfigs, KEY_LIFT_CONFIGS);
    }

    public void write() {
        data.liftConfigs.clear();
        data.liftConfigs.addAll(responseConfigs);
        data.sync();
    }
}
