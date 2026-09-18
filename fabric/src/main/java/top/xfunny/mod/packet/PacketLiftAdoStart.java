package top.xfunny.mod.packet;

import org.mtr.core.data.Lift;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.client.MinecraftClientData;
import top.xfunny.mixin.MixinLiftSchema;

public final class PacketLiftAdoStart extends PacketHandler {

    private final long liftId;
    private final long stoppingCoolDown;

    public PacketLiftAdoStart(PacketBufferReceiver packetBufferReceiver) {
        liftId = packetBufferReceiver.readLong();
        stoppingCoolDown = packetBufferReceiver.readLong();
    }

    public PacketLiftAdoStart(long liftId, long stoppingCoolDown) {
        this.liftId = liftId;
        this.stoppingCoolDown = stoppingCoolDown;
    }

    @Override
    public void write(PacketBufferSender packetBufferSender) {
        packetBufferSender.writeLong(liftId);
        packetBufferSender.writeLong(stoppingCoolDown);
    }

    @Override
    public void runClient() {
        final Lift lift = MinecraftClientData.getLift(liftId);
        if (lift != null) {
            ((MixinLiftSchema) lift).setStoppingCoolDown(stoppingCoolDown);
        }
    }
}
