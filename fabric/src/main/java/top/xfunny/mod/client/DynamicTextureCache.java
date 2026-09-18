package top.xfunny.mod.client;

import org.mtr.core.servlet.MessageQueue;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mod.render.MainRenderer;
import top.xfunny.mod.Init;
import top.xfunny.mod.client.client_data.DynamicResource;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class DynamicTextureCache {// 使用AI优化
    private static final int COOLDOWN_TIME = 10000; // 10s
    private static final Identifier DEFAULT_TRANSPARENT_RESOURCE = new Identifier(Init.MOD_ID, "textures/block/transparent.png");
    public static DynamicTextureCache instance = new DynamicTextureCache();
    private final Object2ObjectLinkedOpenHashMap<String, DynamicResource> dynamicResources = new Object2ObjectLinkedOpenHashMap<>();

    // 强引用兜底
    private final Map<String, DynamicResource> lastSuccessfulResource = new ConcurrentHashMap<>();

    // 待删除队列：Key=资源对象本身 (确保对象存活), Value=物理销毁时间戳
    private final Map<DynamicResource, Long> resourcesToDispose = new ConcurrentHashMap<>();

    private final Set<String> generatingScreens = ConcurrentHashMap.newKeySet();
    private final MessageQueue<Runnable> resourceRegistryQueue = new MessageQueue<>();

    public DynamicResource getResource(String textureId, Long blockPos, Supplier<NativeImage> supplier) {
        resourceRegistryQueue.process(Runnable::run);

        // 优化：screenUniqueId 计算
        int separatorIndex = textureId.indexOf('$');
        String screenUniqueId;
        if (separatorIndex != -1) {
            screenUniqueId = textureId.substring(0, separatorIndex) + "_" + blockPos;
        } else {
            screenUniqueId = textureId + "_" + blockPos;
        }

        // 1. 缓存命中
        DynamicResource currentRes = dynamicResources.get(textureId);
        if (currentRes != null && !currentRes.needsRefresh) {
            currentRes.expiryTime = System.currentTimeMillis() + COOLDOWN_TIME;
            lastSuccessfulResource.put(screenUniqueId, currentRes);
            return currentRes;
        }

        // 2. 触发生成 (限制队列长度防止溢出，但给足空间)
        if (!generatingScreens.contains(screenUniqueId) && generatingScreens.size() <= 1000) {
            registerResource(supplier, textureId, screenUniqueId);
        }

        // 3. 兜底逻辑 (修复透明问题的关键)
        // 只要这里取到的对象还没被 dispose()，它就是完全可用的
        DynamicResource fallback = lastSuccessfulResource.get(screenUniqueId);
        if (fallback != null) {
            fallback.expiryTime = System.currentTimeMillis() + COOLDOWN_TIME; // 续命
            return fallback;
        }

        // 4. 如果当前有资源但被标记刷新，强行暂用
        if (currentRes != null) {
            return currentRes;
        }

        // 5. 只有第一次加载才会运行到这里
        return DefaultRenderingColor.TRANSPARENT.dynamicResource;
    }

    private void registerResource(Supplier<NativeImage> supplier, String textureId, String screenUniqueId) {
        generatingScreens.add(screenUniqueId);

        MainRenderer.WORKER_THREAD.scheduleDynamicTextures(() -> {
            final NativeImage nativeImage = supplier.get();
            // 此时仍在异步线程

            resourceRegistryQueue.put(() -> {
                // 回到主线程
                try {
                    if (nativeImage != null) {
                        final NativeImageBackedTexture nativeImageBackedTexture = new NativeImageBackedTexture(nativeImage);
                        final Identifier identifier = new Identifier(Init.MOD_ID, "id_" + org.mtr.mod.Init.randomString());
                        MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, new AbstractTexture(nativeImageBackedTexture.data));

                        final DynamicResource dynamicResourceNew = new DynamicResource(identifier, nativeImageBackedTexture);

                        // 替换 Map 中的资源
                        DynamicResource oldRes = dynamicResources.put(textureId, dynamicResourceNew);

                        // 【修改】旧资源不要立即 dispose，而是放入死亡队列，给予 10s 缓冲期
                        // 这防止了如果 oldRes 恰好是当前的兜底资源，被瞬间销毁导致闪烁
                        if (oldRes != null && oldRes != dynamicResourceNew) {
                            resourcesToDispose.put(oldRes, System.currentTimeMillis() + COOLDOWN_TIME);
                        }

                        // 更新兜底
                        lastSuccessfulResource.put(screenUniqueId, dynamicResourceNew);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    generatingScreens.remove(screenUniqueId);
                }
            });
        });
    }

    public void reload() {
        dynamicResources.values().forEach(dynamicResource -> dynamicResource.needsRefresh = true);
        generatingScreens.clear();
        // reload 不清空 lastSuccessfulResource，防止瞬间全屏透明
    }

    public void tick() {
        long now = System.currentTimeMillis();

        // 1. 清理主缓存中的过期逻辑
        final ObjectArrayList<String> keysToRemove = new ObjectArrayList<>();
        dynamicResources.forEach((key, res) -> {
            // 如果过期了，且不是当前该屏幕的“最新成功资源”，则移出主缓存
            // (如果是最新成功资源，即使过期了也不移出，直到新图生成替换它)
            if (res.expiryTime < now && !lastSuccessfulResource.containsValue(res)) {
                keysToRemove.add(key);
                // 放入死亡队列，等待物理销毁
                resourcesToDispose.put(res, now + COOLDOWN_TIME);
            }
        });
        keysToRemove.forEach(dynamicResources::remove);

        // 2. 处理过期队列（真正的物理销毁）
        final ObjectArrayList<DynamicResource> toActuallyDispose = new ObjectArrayList<>();

        resourcesToDispose.forEach((res, disposeTime) -> {
            if (disposeTime < now) {
                // 双重保险：如果这个资源竟然又是“最新成功资源”（极小概率），先别杀
                if (!lastSuccessfulResource.containsValue(res)) {
                    toActuallyDispose.add(res);
                } else {
                    // 这种情况给它续命一下，虽然逻辑上不该发生
                    // res.expiryTime = now + COOLDOWN_TIME;
                }
            }
        });

        // 执行物理销毁
        toActuallyDispose.forEach(res -> {
            res.dispose(); // 关闭 NativeImage, Destroy Texture
            resourcesToDispose.remove(res);
        });
    }

    private enum DefaultRenderingColor {
        TRANSPARENT(DEFAULT_TRANSPARENT_RESOURCE);
        private final DynamicResource dynamicResource;

        DefaultRenderingColor(Identifier identifier) {
            dynamicResource = new DynamicResource(identifier, null);
        }
    }
}