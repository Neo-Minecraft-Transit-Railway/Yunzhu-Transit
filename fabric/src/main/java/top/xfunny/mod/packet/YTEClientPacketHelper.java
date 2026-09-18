package top.xfunny.mod.packet;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ScreenExtension;
import top.xfunny.mod.block.PATRS01RailwaySign;
import top.xfunny.mod.block.EmptyFloor;
import top.xfunny.mod.block.TestLiftButtons;
import top.xfunny.mod.client.screen.GuangzhouRailwaySignScreen;
import top.xfunny.mod.client.screen.PATRS01RailwaySignScreen;
import top.xfunny.mod.client.screen.LiftEmptyFloorScreen;

import java.util.function.Consumer;
import java.util.function.Predicate;

public final class YTEClientPacketHelper {

    public static void openBlockEntityScreen(BlockPos blockPos) {
        getBlockEntity(blockPos, blockEntity -> {
            if (blockEntity.data instanceof EmptyFloor.BlockEntity floor) {
                openScreen(new LiftEmptyFloorScreen(blockPos, floor),
                        screenExtension -> screenExtension instanceof LiftEmptyFloorScreen);
            } else if (blockEntity.data instanceof PATRS01RailwaySign.BlockEntity) {
                openScreen(new PATRS01RailwaySignScreen(blockPos),
                        screenExtension -> screenExtension instanceof PATRS01RailwaySignScreen);
            }
            // 测试新屏幕
            else if (blockEntity.data instanceof TestLiftButtons.BlockEntity) {
                openScreen(new GuangzhouRailwaySignScreen(blockPos),
                        screenExtension -> screenExtension instanceof GuangzhouRailwaySignScreen);
            }
        });
    }

    private static void openScreen(ScreenExtension screenExtension, Predicate<ScreenExtension> isInstance) {
        final MinecraftClient minecraftClient = MinecraftClient.getInstance();
        final Screen screen = minecraftClient.getCurrentScreenMapped();
        if (screen == null || screen.data instanceof ScreenExtension && !isInstance.test((ScreenExtension) screen.data)) {
            minecraftClient.openScreen(new Screen(screenExtension));
        }
    }

    private static void getBlockEntity(BlockPos blockPos, Consumer<BlockEntity> consumer) {
        final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
        if (clientWorld != null) {
            final BlockEntity blockEntity = clientWorld.getBlockEntity(blockPos);
            if (blockEntity != null) {
                consumer.accept(blockEntity);
            }
        }
    }
}
