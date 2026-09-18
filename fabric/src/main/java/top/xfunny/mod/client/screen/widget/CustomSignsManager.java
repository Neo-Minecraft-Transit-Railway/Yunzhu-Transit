package top.xfunny.mod.client.screen.widget;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.ResourceManagerHelper;
import top.xfunny.mod.Init;

import java.io.File;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class CustomSignsManager {
    private static final Set<String> builtinFileNames;
    // 内置非图标列表
    private static final Set<String> NAFileNames;
    // 存储所有标志牌资源的列表
    private static ObjectArrayList<String> allSigns = new ObjectArrayList<>();
    private static ObjectArrayList<String> defaultSigns = new ObjectArrayList<>();

    static {
        Set<String> strings = new HashSet<>();
        strings.add("");
        builtinFileNames = new HashSet<>(strings);
    }

    static {
        NAFileNames = new HashSet<>();
        NAFileNames.add("seven_segment.png");
        NAFileNames.add("qr_code.png");
        NAFileNames.add("lift_arrow.png");
        NAFileNames.add("exit_letter_blank.png");
        NAFileNames.add("circle.png");
        NAFileNames.add("door_not_in_use.png");
        NAFileNames.add("gap_small.png");
        NAFileNames.add("logo_grayscale.png");
        NAFileNames.add("rubbish.png");
        NAFileNames.add("spit.png");
    }

    public static void loader() {
        defaultSigns.clear();
        builtinFileNames.clear();
        allSigns.clear();

        ResourceManagerHelper.readAllResources(new Identifier("mtr", "mtr_custom_resources.json"), (inputStream) -> {
            try (InputStreamReader reader = new InputStreamReader(inputStream)) {
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = parser.parse(reader).getAsJsonObject();
                JsonArray signsArray = jsonObject.getAsJsonArray("signs");

                for (JsonElement signElement : signsArray) {
                    JsonObject signObject = signElement.getAsJsonObject();
                    String id = signObject.get("id").getAsString();
                    String textureResource = signObject.get("textureResource").getAsString();

                    String fileName = new File(textureResource).getName();

                    if (id.startsWith("!")) {
                        builtinFileNames.add(fileName);
                    }
                }
            } catch (Exception e) {
                Init.LOGGER.error("Fake MTR", e);
            }
        });

        // 遍历 textures/block/sign 目录，判断是否为内置文件
        ResourceManagerHelper.readDirectory("textures/block/sign", (identifier, inputStream) -> {
            try {
                if (identifier.getNamespace().equals("mtr") && identifier.getPath().endsWith(".png")) {
                    String fileName = new File(identifier.getPath()).getName();
                    String substring = fileName.substring(0, fileName.length() - 4);

                    if (builtinFileNames.contains(fileName)) {
                        defaultSigns.add(substring);
                    } else if (!NAFileNames.contains(fileName)) {
                        allSigns.add(substring);
                    }
                }
            } catch (Exception e) {
                Init.LOGGER.error("Fake MTR", e);
            }
        });

        defaultSigns = new ObjectArrayList<>(new LinkedHashSet<>(defaultSigns));
        allSigns.addAll(0, defaultSigns);
        allSigns = new ObjectArrayList<>(new LinkedHashSet<>(allSigns));
        Init.LOGGER.info("Found {} Icon Files", allSigns.size());
    }

    public static ObjectArrayList<String> getSignList() {
        return allSigns;
    }

    public static int getDefaultSignListSize() {
        return defaultSigns.size();
    }
}