package top.xfunny.mod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mtr.core.data.Position;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.servlet.QueueObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.holder.WorldSavePath;
import org.mtr.mapping.mapper.MinecraftServerHelper;
import org.mtr.mapping.registry.Registry;
import top.xfunny.core.YteMain;
import top.xfunny.mod.lift.LiftDoorControlState;
import top.xfunny.mod.packet.PacketLanternSoundInstruction;
import top.xfunny.mod.packet.PacketLiftAdoStart;
import top.xfunny.mod.packet.PacketLiftDoorControl;
import top.xfunny.mod.packet.PacketLiftFloorCancel;
import top.xfunny.mod.packet.PacketLiftHoldState;
import top.xfunny.mod.packet.PacketUpdateLiftEmptyFloorConfig;
import top.xfunny.mod.packet.PacketUpdatePATRS01RailwaySignConfig;
import top.xfunny.mod.packet.PacketYTEOpenBlockEntityScreen;
import top.xfunny.mod.packet.YtePacketRequestData;
import top.xfunny.mod.packet.YtePacketUpdateData;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class Init {
    public static final String MOD_ID = "yte";
    public static final Logger LOGGER = LogManager.getLogger("Yunzhu Transit Extension");
    public static final Registry REGISTRY = new Registry();
    public static int HAS_UPDATE = -1;
    public static final int AUTOSAVE_INTERVAL = 30000;

    private static YteMain yteMain;
    @Nullable
    private static MinecraftServer minecraftServer;
    private static long lastSavedMillis;
    private static final ObjectArrayList<String> WORLD_ID_LIST = new ObjectArrayList<>();

    @Nullable
    public static YteMain getYteMain() {
        return yteMain;
    }


    public static void init() {

        long startTime = System.currentTimeMillis();
        Map<String, Runnable> initSteps = new LinkedHashMap<>();

        //UpdateCheckerUtil.init();
        initSteps.put("Sound Events", SoundEvents::init);
        initSteps.put("Blocks", Blocks::init);
        initSteps.put("Block Entity Types", BlockEntityTypes::init);
        initSteps.put("Items", Items::init);
        initSteps.put("Creative Mode Tabs", CreativeModeTabs::init);
        initSteps.put("MTR Packet", () -> {
            REGISTRY.setupPackets(new Identifier(MOD_ID, "packet"));
            REGISTRY.registerPacket(PacketYTEOpenBlockEntityScreen.class, PacketYTEOpenBlockEntityScreen::new);
            REGISTRY.registerPacket(PacketUpdateLiftEmptyFloorConfig.class, PacketUpdateLiftEmptyFloorConfig::new);
            REGISTRY.registerPacket(PacketUpdatePATRS01RailwaySignConfig.class, PacketUpdatePATRS01RailwaySignConfig::new);
            REGISTRY.registerPacket(PacketLanternSoundInstruction.class, PacketLanternSoundInstruction::new);
            REGISTRY.registerPacket(YtePacketRequestData.class, YtePacketRequestData::new);
            REGISTRY.registerPacket(YtePacketUpdateData.class, YtePacketUpdateData::new);
            REGISTRY.registerPacket(PacketLiftAdoStart.class, PacketLiftAdoStart::new);
            REGISTRY.registerPacket(PacketLiftDoorControl.class, PacketLiftDoorControl::new);
            REGISTRY.registerPacket(PacketLiftHoldState.class, PacketLiftHoldState::new);
            REGISTRY.registerPacket(PacketLiftFloorCancel.class, PacketLiftFloorCancel::new);
        });

        int currentStep = 1;

        for (Map.Entry<String, Runnable> step : initSteps.entrySet()) {
            LOGGER.info("Registering {} ({}/{})", step.getKey(), currentStep, initSteps.size());
            step.getValue().run();
            currentStep++;
        }

        REGISTRY.eventRegistry.registerServerStarted(server -> {
            minecraftServer = server;
            WORLD_ID_LIST.clear();
            MinecraftServerHelper.iterateWorlds(server, serverWorld ->
                    WORLD_ID_LIST.add(getWorldId(new World(serverWorld.data))));
            lastSavedMillis = System.currentTimeMillis();
            yteMain = new YteMain(
                    server.getSavePath(WorldSavePath.getRootMapped()).resolve("yte"),
                    false,
                    WORLD_ID_LIST.toArray(new String[0]));
        });

        REGISTRY.eventRegistry.registerStartServerTick(() -> {
            if (yteMain != null) {
                yteMain.manualTick();
                final long currentMillis = System.currentTimeMillis();
                if (currentMillis - lastSavedMillis > AUTOSAVE_INTERVAL) {
                    yteMain.save();
                    lastSavedMillis = currentMillis;
                }
            }
        });

        REGISTRY.eventRegistry.registerPlayerDisconnect((server, serverPlayerEntity) -> {
            if (yteMain != null) {
                yteMain.save();
            }
        });

        REGISTRY.eventRegistry.registerServerStopping(server -> {
            minecraftServer = null;
            if (yteMain != null) {
                yteMain.stop();
                yteMain = null;
            }
        });

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        LOGGER.info("Yunzhu Transit Extension initialized successfully in {} ms.", duration);
        REGISTRY.init();
    }

    public static void sendLiftAdoStart(long liftId, long stoppingCoolDown) {
        if (minecraftServer != null) {
            MinecraftServerHelper.iteratePlayers(minecraftServer, player ->
                    REGISTRY.sendPacketToClient(player, new PacketLiftAdoStart(liftId, stoppingCoolDown)));
        }
    }

    public static void sendLiftDoorOpen(long liftId, long stoppingCoolDown, boolean resetIdleDirection) {
        if (minecraftServer != null) {
            MinecraftServerHelper.iteratePlayers(minecraftServer, player ->
                    REGISTRY.sendPacketToClient(player,
                            new PacketLiftDoorControl(liftId, LiftDoorControlState.Command.OPEN,
                                    stoppingCoolDown, resetIdleDirection)));
        }
    }

    public static void sendLiftHoldState(long liftId, boolean active) {
        if (minecraftServer != null) {
            final long remainingMillis = active ? LiftDoorControlState.getHoldRemainingMillis(liftId) : 0;
            MinecraftServerHelper.iteratePlayers(minecraftServer, player ->
                    REGISTRY.sendPacketToClient(player,
                            PacketLiftHoldState.update(liftId, active && remainingMillis > 0, remainingMillis)));
        }
    }

    public static <T extends SerializedDataBase> void sendMessageC2S(String key,
            @Nullable MinecraftServer minecraftServer, @Nullable World world,
            SerializedDataBase data, @Nullable Consumer<T> consumer,
            @Nullable Class<T> responseDataClass) {
        if (yteMain != null) {
            yteMain.sendMessageC2S(
                    world == null ? null : WORLD_ID_LIST.indexOf(getWorldId(world)),
                    new QueueObject(key, data,
                            consumer == null || minecraftServer == null ? null
                                    : responseData -> minecraftServer.execute(() -> consumer.accept(responseData)),
                            responseDataClass));
        }
    }

    private static String getWorldId(World world) {
        final Identifier identifier = MinecraftServerHelper.getWorldId(world);
        return String.format("%s/%s", identifier.getNamespace(), identifier.getPath());
    }

    public static Position blockPosToPosition(BlockPos blockPos) {
        return new Position(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static BlockPos positionToBlockPos(Position position) {
        return new BlockPos((int) position.getX(), (int) position.getY(), (int) position.getZ());
    }
}
