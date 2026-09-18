package top.xfunny.mod.packet;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;

public final class PacketYTEOpenBlockEntityScreen extends PacketHandler {

    private final BlockPos blockPos;

    public PacketYTEOpenBlockEntityScreen(PacketBufferReceiver packetBufferReceiver) {
        blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
    }

    public PacketYTEOpenBlockEntityScreen(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeLong(blockPos.asLong());
    }

    @Override
    public void runClient() {
        System.out.println("PacketOpenBlockEntityScreen");
        YTEClientPacketHelper.openBlockEntityScreen(blockPos);
    }
}
