package top.xfunny.mod.client.sound;

import org.mtr.mapping.holder.BlockPos;
import top.xfunny.mod.client.InitClient;
import top.xfunny.mod.client.view.ButtonView;
import top.xfunny.mod.packet.PacketLanternSoundInstruction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SoundPlaybackManager {

    // 存储每个方块坐标上， lantern 的激活状态（代替原本 ButtonView 里的全局静态 Map）
    private static final Map<String, Boolean> LANTERN_ACTIVE_MAP = new ConcurrentHashMap<>();

    /**
     * 为 300+ 个旧渲染类提供无缝兼容的按钮声音绑定
     */
    public static void registerButtonSound(ButtonView buttonView, String soundName) {
        if (soundName == null) return;

        buttonView.setOnClickListener(pos -> {
            // 发包逻辑内聚在这里，渲染线程只管调用，不管具体网络实现
            InitClient.REGISTRY_CLIENT.sendPacketToServer(
                    new PacketLanternSoundInstruction(pos, soundName)
            );
        });
    }

    /**
     * 为 300+ 个旧渲染类提供无缝兼容的到站灯（Lantern）声音绑定
     */
    public static void registerLanternSound(ButtonView buttonView, String lanternSoundInstruction) {
        if (lanternSoundInstruction == null) return;

        buttonView.setOnStateChangeListener(new ButtonView.OnStateChangeListener() {
            @Override
            public void onActivate(BlockPos pos) {
                String key = pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + "_" + lanternSoundInstruction;

                Boolean already = LANTERN_ACTIVE_MAP.get(key);
                if (already != null && already) return;

                InitClient.REGISTRY_CLIENT.sendPacketToServer(
                        new PacketLanternSoundInstruction(pos, lanternSoundInstruction)
                );
                LANTERN_ACTIVE_MAP.put(key, true);
            }

            @Override
            public void onReset(BlockPos pos) {
                String key = pos.getX() + "_" + pos.getY() + "_" + pos.getZ() + "_" + lanternSoundInstruction;
                LANTERN_ACTIVE_MAP.put(key, false);
            }
        });
    }

    /**
     * 当区块卸载或玩家离开世界时调用，防止内存泄漏
     */
    public static void clearCache() {
        LANTERN_ACTIVE_MAP.clear();
    }
}