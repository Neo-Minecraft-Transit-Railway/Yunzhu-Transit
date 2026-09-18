package top.xfunny.mod.util;

import org.mtr.mapping.holder.NativeImage;

import java.io.File;
import java.io.IOException;

public class ImageGenerator {

    private static final int MAX_FILES = 10;

    public static void saveNativeImageAsPng(NativeImage image, String outputPath) {
        try {
            File file = new File(outputPath);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            if (parent != null) {
                File[] files = parent.listFiles((dir, name) ->
                        name.toLowerCase().endsWith(".png")
                );

                if (files != null && files.length >= MAX_FILES) {
                    // 超过上限，直接放弃生成
                    return;
                }
            }

            image.writeToFile(file);

            System.out.println("PNG 图像已成功保存到: " + file.getAbsolutePath());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            image.close();
        }
    }
}
