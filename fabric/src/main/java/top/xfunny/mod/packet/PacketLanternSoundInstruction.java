package top.xfunny.mod.packet;

import org.jetbrains.annotations.NotNull;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;


public final class PacketLanternSoundInstruction extends PacketHandler {
    private final String soundInstruction;
    private final BlockPos blockPos;

    public PacketLanternSoundInstruction(PacketBufferReceiver packetBufferReceiver) {
        blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
        soundInstruction = packetBufferReceiver.readString();
    }

    public PacketLanternSoundInstruction(BlockPos blockPos, String soundInstruction) {
        this.blockPos = blockPos;
        this.soundInstruction = soundInstruction;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeLong(blockPos.asLong());
        packetBufferSender.writeString(soundInstruction);
    }

    @Override
    public void runServer(@NotNull MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
        if (!Init.isChunkLoaded(serverPlayerEntity.getEntityWorld(), blockPos)) {
            return;
        }
        SoundsHelper.playSound(blockPos, serverPlayerEntity, soundInstruction);
    }
}
