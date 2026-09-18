package top.xfunny.mod.client.render;

import org.mtr.mapping.mapper.GraphicsHolder;

public class RenderWatermark {
    private static final int a = 10;

    public static void render(GraphicsHolder b) {
        /*
        final MinecraftClient c = MinecraftClient.getInstance();
        final Window x = c.getWindow();
        int h = x.getScaledWidth() - 1;
        int w = x.getScaledHeight();
//        if(!InitClient.getConfig().hideTestWatermark || Init.HAS_UPDATE == 1){
            int j = -1;
            int z = 0;
            String e = "Yunzhu Transit Extension Beta";
            MutableText g = TextHelper.translatable("gui.yte.watermark");
            MutableText q = TextHelper.translatable("gui.yte.watermark_1", Keys.MOD_VERSION, Keys.BUILD_TIME);
            final ClientPlayerEntity p = c.getPlayerMapped();
            if (p != null) {
                b.push();
                b.translate(h, w, z);
                b.drawText(e, -GraphicsHolder.getTextWidth(e), -a * 3, j, true, GraphicsHolder.getDefaultLight());
                b.drawText(g, -GraphicsHolder.getTextWidth(g), -a * 2, j, true, GraphicsHolder.getDefaultLight());
                b.drawText(q, -GraphicsHolder.getTextWidth(q), -a, j, true, GraphicsHolder.getDefaultLight());
                b.pop();
            }
//        }*/
    }
}
