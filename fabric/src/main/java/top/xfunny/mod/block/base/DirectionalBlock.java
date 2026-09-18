package top.xfunny.mod.block.base;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.HolderBase;
import top.xfunny.mod.data.BlockProperties;

import java.util.List;

public abstract class DirectionalBlock extends YTEBlock {
    public static final DirectionProperty FACING = BlockProperties.FACING;

    public DirectionalBlock(BlockSettings settings) {
        super(settings);
        setDefaultState2(getDefaultState2().with(new Property<>(FACING.data), Direction.NORTH.data));
    }

    @Override
    public void addBlockProperties(List<HolderBase<?>> properties) {
        super.addBlockProperties(properties);
        properties.add(FACING);
    }

    @Override
    public BlockState getPlacementState2(ItemPlacementContext ctx) {
        BlockState superState = super.getPlacementState2(ctx);
        if (superState == null) return null;
        return superState.with(new Property<>(FACING.data), Direction.fromHorizontal(ctx.getPlayerFacing().getHorizontal()).data);
    }
}