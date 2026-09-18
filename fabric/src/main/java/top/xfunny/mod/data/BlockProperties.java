package top.xfunny.mod.data;

import org.mtr.mapping.holder.BooleanProperty;
import org.mtr.mapping.holder.DirectionProperty;
import org.mtr.mapping.holder.IntegerProperty;
import org.mtr.mapping.mapper.DirectionHelper;

/**
 * Stores all block properties JCM uses. Block classes from JCM should reference the block properties in here
 */
public interface BlockProperties {
    DirectionProperty FACING = DirectionHelper.FACING;
    IntegerProperty BARRIER_FENCE_TYPE = IntegerProperty.of("type", 0, 10);
    BooleanProperty BARRIER_FLIPPED = BooleanProperty.of("flipped");
    BooleanProperty HORIZONTAL_IS_LEFT = BooleanProperty.of("left");
}
