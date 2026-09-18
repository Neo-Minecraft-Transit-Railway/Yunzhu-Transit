package top.xfunny.mod.client.client_data;

import org.mtr.core.data.Lift;
import top.xfunny.mixin.MixinLiftSchema;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LiftSpeed {
    private static final float UPDATE_INTERVAL_TICKS = 1; // 20tick/s * 0.5s = 10 ticks
    private final Map<Long, Double> speedCache = new ConcurrentHashMap<>();
    private final Map<Long, Float> tickCache = new ConcurrentHashMap<>();
    private double rawSpeed;
    private float publicLatestGameTick = -1;


    public double getSpeed(Lift lift) {
        float gameTick = org.mtr.mod.InitClient.getGameTick();

        long liftId = lift.getId();

        float lastUpdateTick = tickCache.getOrDefault(liftId, 0f);

        if (gameTick < publicLatestGameTick) {//重新进存档，则清除缓存
            publicLatestGameTick = gameTick;
            speedCache.clear();
            tickCache.clear();
        }

        if (lastUpdateTick - publicLatestGameTick >= UPDATE_INTERVAL_TICKS) {
            publicLatestGameTick = gameTick;
        }

        if (gameTick - lastUpdateTick >= UPDATE_INTERVAL_TICKS) {
            rawSpeed = ((MixinLiftSchema) lift).getSpeed() * 1000;
            speedCache.put(liftId, rawSpeed);
            tickCache.put(liftId, gameTick);
        }

        return speedCache.getOrDefault(liftId, 0.0);
    }
}
