package top.xfunny.mod.client.client_data;

import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.NativeImage;
import org.mtr.mapping.holder.NativeImageBackedTexture;
import org.mtr.mod.render.MainRenderer;

import javax.annotation.Nullable;

public class DynamicResource {// 使用AI优化
    public final int width;
    public final int height;
    public final Identifier identifier;
    // 持有引用以便后续释放
    private final NativeImageBackedTexture nativeImageBackedTexture;
    public long expiryTime;
    public boolean needsRefresh;

    public DynamicResource(Identifier identifier, @Nullable NativeImageBackedTexture nativeImageBackedTexture) {
        this.identifier = identifier;
        this.nativeImageBackedTexture = nativeImageBackedTexture;
        if (nativeImageBackedTexture != null) {
            final NativeImage nativeImage = nativeImageBackedTexture.getImage();
            if (nativeImage != null) {
                width = nativeImage.getWidth();
                height = nativeImage.getHeight();
            } else {
                width = 16;
                height = 16;
            }
        } else {
            width = 16;
            height = 16;
        }
    }

    // 【修改】将原来 remove 的逻辑改名为 dispose，且只负责清理，不负责逻辑
    public void dispose() {
        // 1. 销毁 GL 纹理
        MinecraftClient.getInstance().getTextureManager().destroyTexture(identifier);
        // 2. 通知 MTR 取消渲染
        MainRenderer.cancelRender(identifier);
        // 3. 释放内存 (关键！)
        if (this.nativeImageBackedTexture != null) {
            this.nativeImageBackedTexture.close();
        }
    }
}