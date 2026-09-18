package top.xfunny.mod.util;

import org.mtr.core.data.Lift;
import org.mtr.core.data.LiftDirection;
import org.mtr.core.data.LiftFloor;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.World;
import org.mtr.mod.block.BlockLiftTrackFloor;

public class ClientGetLiftDetails {
    public static ObjectObjectImmutablePair<LiftDirection, ObjectObjectImmutablePair<String, String>> getLiftDetails(World world, Lift lift, BlockPos blockPos) {
        final LiftFloor liftFloor = lift.getCurrentFloor();
        final BlockEntity floorEntity = world.getBlockEntity(blockPos);
        final String floorNumber;
        final String floorDescription;
        if (floorEntity != null && floorEntity.data instanceof BlockLiftTrackFloor.BlockEntity) {
            floorNumber = ((BlockLiftTrackFloor.BlockEntity) floorEntity.data).getFloorNumber();
            floorDescription = ((BlockLiftTrackFloor.BlockEntity) floorEntity.data).getFloorDescription();
        } else {
            floorNumber = liftFloor.getNumber();
            floorDescription = liftFloor.getDescription();
        }
        return new ObjectObjectImmutablePair<>(lift.getDirection(), new ObjectObjectImmutablePair<>(floorNumber, floorDescription));
    }
}
