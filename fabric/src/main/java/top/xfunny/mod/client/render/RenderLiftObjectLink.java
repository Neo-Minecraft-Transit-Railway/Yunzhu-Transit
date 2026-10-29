package top.xfunny.mod.client.render;

import org.mtr.mapping.holder.Vector3d;
import org.mtr.mod.render.RenderLiftLinks;
import org.mtr.mod.render.StoredMatrixTransformations;

/**
 * Elevator linker lines — delegate to MTR {@link RenderLiftLinks} segmented world-pass drawing
 * so lines do not vanish at glancing angles (BER cull / near-plane wipe of a single long GPU line).
 */
public class RenderLiftObjectLink {
    public static void RenderLiftObjectLink(StoredMatrixTransformations storedMatrixTransformations, Vector3d position1, Vector3d position2, boolean holdingLinker) {
        if (!holdingLinker) {
            return;
        }
        RenderLiftLinks.scheduleSegmentedLine(
                storedMatrixTransformations.getOriginX() + position1.getXMapped(),
                storedMatrixTransformations.getOriginY() + position1.getYMapped(),
                storedMatrixTransformations.getOriginZ() + position1.getZMapped(),
                storedMatrixTransformations.getOriginX() + position2.getXMapped(),
                storedMatrixTransformations.getOriginY() + position2.getYMapped(),
                storedMatrixTransformations.getOriginZ() + position2.getZMapped(),
                0xFF00FF00
        );
    }

    public static void RenderButtonObjectLink(StoredMatrixTransformations storedMatrixTransformations, Vector3d position1, Vector3d position2, boolean holdingLinker) {
        if (!holdingLinker) {
            return;
        }
        RenderLiftLinks.scheduleSegmentedLine(
                storedMatrixTransformations.getOriginX() + position1.getXMapped(),
                storedMatrixTransformations.getOriginY() + position1.getYMapped(),
                storedMatrixTransformations.getOriginZ() + position1.getZMapped(),
                storedMatrixTransformations.getOriginX() + position2.getXMapped(),
                storedMatrixTransformations.getOriginY() + position2.getYMapped(),
                storedMatrixTransformations.getOriginZ() + position2.getZMapped(),
                0xFFFFFF17
        );
    }
}
