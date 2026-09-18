package top.xfunny.mod.client.render;

import org.mtr.core.data.Lift;
import org.mtr.core.data.LiftDirection;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityRenderer;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.PlayerHelper;
import org.mtr.mod.block.IBlock;
import org.mtr.mod.data.IGui;
import org.mtr.mod.render.RenderLifts;
import org.mtr.mod.render.StoredMatrixTransformations;
import top.xfunny.mod.Init;
import top.xfunny.mod.block.OtisSeries1LanternScreen1HorizontalEven;
import top.xfunny.mod.block.OtisSeries1LanternScreen2HorizontalEven;
import top.xfunny.mod.block.base.LiftButtonsBase;
import top.xfunny.mod.client.InitClient;
import top.xfunny.mod.client.resource.FontList;
import top.xfunny.mod.client.view.*;
import top.xfunny.mod.client.view.view_group.LinearLayout;
import top.xfunny.mod.item.YteGroupLiftButtonsLinker;
import top.xfunny.mod.item.YteLiftButtonsLinker;
import top.xfunny.mod.packet.PacketLanternSoundInstruction;
import top.xfunny.mod.util.ClientGetLiftDetails;

import java.util.Comparator;

import static org.mtr.core.data.LiftDirection.NONE;

public class RenderOtisSeries1LanternScreen2Horizontal<T extends LiftButtonsBase.BlockEntityBase> extends BlockEntityRenderer<T> implements DirectionHelper, IGui, IBlock {
    private static final int PRESSED_COLOR = 0xFF1D953F;
    private static final int DEFAULT_COLOR = 0xFF0D441D;
    private static final Identifier ARROW_TEXTURE_END = new Identifier(Init.MOD_ID, "textures/block/otis_series_1_lantern_arrow_end.png");
    private static final Identifier ARROW_TEXTURE_MIDDLE = new Identifier(Init.MOD_ID, "textures/block/otis_series_1_lantern_arrow_middle.png");
    private final boolean isOdd;

    public RenderOtisSeries1LanternScreen2Horizontal(Argument dispatcher, Boolean isOdd) {
        super(dispatcher);
        this.isOdd = isOdd;
    }

    @Override
    public void render(T blockEntity, float tickDelta, GraphicsHolder graphicsHolder1, int light, int overlay) {
        final World world = blockEntity.getWorld2();


        if (world == null) {
            return;
        }

        final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
        if (clientPlayerEntity == null) {
            return;
        }

        final boolean holdingLinker = PlayerHelper.isHolding(PlayerEntity.cast(clientPlayerEntity), item -> item.data instanceof YteLiftButtonsLinker || item.data instanceof YteGroupLiftButtonsLinker);
        final BlockPos blockPos = blockEntity.getPos2();
        final BlockState blockState = world.getBlockState(blockPos);
        final Direction facing = IBlock.getStatePropertySafe(blockState, FACING);
        LiftButtonsBase.LiftButtonDescriptor buttonDescriptor = new LiftButtonsBase.LiftButtonDescriptor(false, false);

        final StoredMatrixTransformations storedMatrixTransformations = new StoredMatrixTransformations(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
        StoredMatrixTransformations storedMatrixTransformations1 = storedMatrixTransformations.copy();
        storedMatrixTransformations1.add(graphicsHolder -> {
            graphicsHolder.rotateYDegrees(-facing.asRotation());
            graphicsHolder.translate(0, 0, 7.8F / 16 - SMALL_OFFSET);
        });

        LinearLayout parentLayout = new LinearLayout(false);
        parentLayout.setBasicsAttributes(world, blockPos);
        parentLayout.setStoredMatrixTransformations(storedMatrixTransformations1);
        parentLayout.setParentDimensions(2.5F / 16, 3.75F / 16);
        parentLayout.setPosition(isOdd ? -0.41F / 16 : -8.41F / 16, 9.75F / 16);
        parentLayout.setWidth(LayoutSize.MATCH_PARENT);
        parentLayout.setHeight(LayoutSize.MATCH_PARENT);

        LinearLayout backgroundLayout = new LinearLayout(true);
        backgroundLayout.setBasicsAttributes(world, blockPos);
        backgroundLayout.setWidth(LayoutSize.WRAP_CONTENT);
        backgroundLayout.setHeight(LayoutSize.WRAP_CONTENT);
        backgroundLayout.setGravity(Gravity.CENTER);
        backgroundLayout.setBackgroundColor(0xFF000000);

        LinearLayout backgroundLayout1 = new LinearLayout(false);
        backgroundLayout1.setBasicsAttributes(world, blockPos);
        backgroundLayout1.setWidth(LayoutSize.WRAP_CONTENT);
        backgroundLayout1.setHeight(LayoutSize.WRAP_CONTENT);
        backgroundLayout1.setGravity(Gravity.CENTER);
        backgroundLayout1.setBackgroundColor(0xFF000000);
        backgroundLayout1.setMargin(0.5F / 16, 0.45F / 16, 0.0F / 16, 0.0F / 16);

        ButtonView upLantern = new ButtonView();
        upLantern.setBasicsAttributes(world, blockPos);
        upLantern.setTexture(ARROW_TEXTURE_END);
        upLantern.setDimension(1.8F / 16);
        upLantern.setLight(light);
        upLantern.setDefaultColor(DEFAULT_COLOR);
        upLantern.setPressedColor(PRESSED_COLOR);
        upLantern.setMargin(0, 0.1F / 16, 0, -1.8F / 16); // Changed top and bottom margins

        ButtonView downLantern = new ButtonView();
        downLantern.setBasicsAttributes(world, blockPos);
        downLantern.setTexture(ARROW_TEXTURE_END);
        downLantern.setDimension(1.8F / 16);
        downLantern.setLight(light);
        downLantern.setDefaultColor(DEFAULT_COLOR);
        downLantern.setPressedColor(PRESSED_COLOR);
        downLantern.setFlip(false, true);
        downLantern.setMargin(0, -1.8F / 16, 0, 0.1F / 16); // Changed top and bottom margins

        ButtonView middleLantern = new ButtonView();
        middleLantern.setBasicsAttributes(world, blockPos);
        middleLantern.setTexture(ARROW_TEXTURE_MIDDLE);
        middleLantern.setDimension(1.8F / 16);
        middleLantern.setLight(light);
        middleLantern.setDefaultColor(DEFAULT_COLOR);
        middleLantern.setPressedColor(PRESSED_COLOR);

        final LineComponent line = new LineComponent();
        line.setBasicsAttributes(world, blockPos);

        final LineComponent buttonLine = new LineComponent();
        buttonLine.setBasicsAttributes(world, blockPos);

        final ObjectArrayList<ObjectObjectImmutablePair<BlockPos, Lift>> sortedPositionsAndLifts = new ObjectArrayList<>();


        blockEntity.forEachTrackPosition(trackPosition -> {
            line.RenderLine(holdingLinker, trackPosition);


            OtisSeries1LanternScreen2HorizontalEven.hasButtonsClient(trackPosition, buttonDescriptor, (floorIndex, lift) -> {
                sortedPositionsAndLifts.add(new ObjectObjectImmutablePair<>(trackPosition, lift));

                LiftDirection pressedButtonDirection = blockEntity.getPressedButtonDirection();

                ObjectObjectImmutablePair<LiftDirection, ObjectObjectImmutablePair<String, String>> liftDetails = ClientGetLiftDetails.getLiftDetails(world, lift, org.mtr.mod.Init.positionToBlockPos(lift.getCurrentFloor().getPosition()));
                String floorNumber = liftDetails.right().left();
                String currentFloorNumber = RenderLifts.getLiftDetails(world, lift, trackPosition).right().left();

                final ObjectArraySet<LiftDirection> instructionDirections = lift.hasInstruction(floorIndex);

                if (lift.getDoorValue() == 0) {
                    blockEntity.lastUpActive = false;
                    blockEntity.lastDownActive = false;
                }


                if (instructionDirections.isEmpty() && pressedButtonDirection != null && lift.getDoorValue() != 0 && floorNumber.equals(currentFloorNumber)) {
                    switch (pressedButtonDirection) {
                        case DOWN:
                            downLantern.activate();
                            middleLantern.activate();
                            if (!blockEntity.lastDownActive) {
                                InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketLanternSoundInstruction(blockPos, "otis_series_1_lantern_down_2"));
                                blockEntity.lastDownActive = true;
                                blockEntity.lastUpActive = true;
                            }

                            break;
                        case UP:
                            upLantern.activate();
                            middleLantern.activate();
                            if (!blockEntity.lastUpActive) {
                                InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketLanternSoundInstruction(blockPos, "otis_series_1_lantern_up_2"));
                                blockEntity.lastUpActive = true;
                                blockEntity.lastDownActive = true;
                            }

                            break;
                    }
                }

                instructionDirections.forEach(liftDirection -> {
                    if (lift.getDoorValue() != 0 && floorNumber.equals(currentFloorNumber)) {
                        if (liftDirection == NONE) {
                            if (pressedButtonDirection != null) {
                                switch (pressedButtonDirection) {
                                    case DOWN:
                                        downLantern.activate();
                                        middleLantern.activate();
                                        if (!blockEntity.lastDownActive) {
                                            InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketLanternSoundInstruction(blockPos, "otis_series_1_lantern_down_2"));
                                            blockEntity.lastDownActive = true;
                                            blockEntity.lastUpActive = true;
                                        }

                                        break;
                                    case UP:
                                        upLantern.activate();
                                        middleLantern.activate();
                                        if (!blockEntity.lastUpActive) {
                                            InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketLanternSoundInstruction(blockPos, "otis_series_1_lantern_up_2"));
                                            blockEntity.lastUpActive = true;
                                            blockEntity.lastDownActive = true;
                                        }

                                        break;
                                }
                            }
                        } else {
                            switch (liftDirection) {
                                case DOWN:
                                    downLantern.activate();
                                    middleLantern.activate();
                                    if (!blockEntity.lastDownActive) {
                                        InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketLanternSoundInstruction(blockPos, "otis_series_1_lantern_down_2"));
                                        blockEntity.lastDownActive = true;
                                        blockEntity.lastUpActive = true;
                                    }

                                    break;
                                case UP:
                                    upLantern.activate();
                                    middleLantern.activate();
                                    if (!blockEntity.lastUpActive) {
                                        InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketLanternSoundInstruction(blockPos, "otis_series_1_lantern_up_2"));
                                        blockEntity.lastUpActive = true;
                                        blockEntity.lastDownActive = true;
                                    }

                                    break;
                            }
                        }
                    }
                });
            });
        });

        blockEntity.forEachLiftButtonPosition(buttonPosition -> {
            buttonLine.RenderLine(holdingLinker, buttonPosition, true);
        });

        final LineComponent line1 = new LineComponent();
        line.setBasicsAttributes(world, blockPos);

        blockEntity.forEachTrackPosition(trackPosition -> {
            line.RenderLine(holdingLinker, trackPosition);
            OtisSeries1LanternScreen1HorizontalEven.hasButtonsClient(trackPosition, buttonDescriptor, (floorIndex, lift) -> {
                sortedPositionsAndLifts.add(new ObjectObjectImmutablePair<>(trackPosition, lift));
            });
        });

        sortedPositionsAndLifts.sort(Comparator.comparingInt(sortedPositionAndLift -> blockPos.getManhattanDistance(new Vector3i(sortedPositionAndLift.left().data))));

        if (!sortedPositionsAndLifts.isEmpty()) {
            final int count = 1;

            for (int i = 0; i < count; i++) {
                final LiftFloorDisplayView liftFloorDisplayView = new LiftFloorDisplayView();
                liftFloorDisplayView.setBasicsAttributes(world,
                        blockPos,
                        sortedPositionsAndLifts.get(i).right(),
                        FontList.instance.getFont("otis_series1_x11"),
                        4.5F,
                        0xFF1D953F);
                liftFloorDisplayView.setTextureId(String.format("otis_series1_x11_screen_display_%d", i));
                liftFloorDisplayView.setWidth(1.3F / 16);
                liftFloorDisplayView.setHeight(1.75F / 16);
                liftFloorDisplayView.setMargin(0.15F / 16, 0.1F / 16, 0.35F / 16, 0.1F / 16);
                liftFloorDisplayView.setTextAlign(TextView.HorizontalTextAlign.RIGHT);
                liftFloorDisplayView.setDisplayLength(2, 0);
                liftFloorDisplayView.setGravity(Gravity.CENTER_VERTICAL);

                backgroundLayout1.addChild(liftFloorDisplayView);
            }
        }

        if (buttonDescriptor.hasUpButton() || buttonDescriptor.hasDownButton()) {
            backgroundLayout.addChild(upLantern);
            backgroundLayout.addChild(middleLantern);
            backgroundLayout.addChild(downLantern);
        }

        parentLayout.addChild(backgroundLayout);
        parentLayout.addChild(backgroundLayout1);
        parentLayout.render();
    }
}