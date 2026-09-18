package top.xfunny.mod;

import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.ItemConvertible;
import org.mtr.mapping.holder.ItemStack;
import org.mtr.mapping.registry.CreativeModeTabHolder;


public class CreativeModeTabs {
    public static final CreativeModeTabHolder YTE_LIFT_FIXTURES;
    public static final CreativeModeTabHolder YTE_LIFT_DOORS;
    public static final CreativeModeTabHolder YTE_RAILWAY_FACILITIES;
    public static final CreativeModeTabHolder YTE_TOOLS;

    static {
        YTE_LIFT_FIXTURES = Init.REGISTRY.createCreativeModeTabHolder(new Identifier(Init.MOD_ID, "yte_lift_fixtures"), () -> new ItemStack(new ItemConvertible(Blocks.SCHINDLER_D_SERIES_D2BUTTON.get().data)));
        YTE_LIFT_DOORS = Init.REGISTRY.createCreativeModeTabHolder(new Identifier(Init.MOD_ID, "yte_lift_doors"), () -> new ItemStack(new ItemConvertible(Blocks.SCHINDLER_QKS9_DOOR_1.get().data)));
        YTE_RAILWAY_FACILITIES = Init.REGISTRY.createCreativeModeTabHolder(new Identifier(Init.MOD_ID, "yte_railway_facilities"), () -> new ItemStack(new ItemConvertible(Blocks.PAT_P01_TICKET_BARRIER_ENTRANCE.get().data)));
        YTE_TOOLS = Init.REGISTRY.createCreativeModeTabHolder(new Identifier(Init.MOD_ID, "yte_tools"), () -> new ItemStack(new ItemConvertible(Items.YTE_GROUP_LIFT_BUTTONS_LINK_CONNECTOR.get().data)));
    }

    public static void init() {

    }
}
