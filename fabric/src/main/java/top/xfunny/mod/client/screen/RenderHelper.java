package top.xfunny.mod.client.screen;

import org.mtr.mapping.mapper.GraphicsHolder;

/**
 * @deprecated 或将在以后版本中删除此接口。
 */
@Deprecated
public interface RenderHelper {
    int lineHeight = 9;

    static void scaleToFit(GraphicsHolder graphicsHolder, int targetW, double maxW, boolean keepAspectRatio) {
        scaleToFit(graphicsHolder, targetW, maxW, keepAspectRatio, 0);
    }

    static void scaleToFit(GraphicsHolder graphicsHolder, double targetW, double maxW, boolean keepAspectRatio, double height) {
        height = height / 2;
        double scaleX = Math.min(1, maxW / targetW);
        if (scaleX < 1) {
            if (keepAspectRatio) {
                graphicsHolder.translate(0, height / 2.0, 0);
                graphicsHolder.scale((float) scaleX, (float) scaleX, (float) scaleX);
                graphicsHolder.translate(0, -height / 2.0, 0);
            } else {
                graphicsHolder.scale((float) scaleX, 1, 1);
            }
        }
    }
}