package top.xfunny.mod;

import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.Item;
import org.mtr.mapping.registry.ItemRegistryObject;
import top.xfunny.mod.item.*;

public class Items {
    public static final ItemRegistryObject YTE_LIFT_BUTTONS_LINK_CONNECTOR;
    public static final ItemRegistryObject YTE_LIFT_BUTTONS_LINK_REMOVER;
    public static final ItemRegistryObject YTE_GROUP_LIFT_BUTTONS_LINK_CONNECTOR;
    public static final ItemRegistryObject YTE_GROUP_LIFT_BUTTONS_LINK_REMOVER;
    public static final ItemRegistryObject FLOOR_AUTO_SETTER;
    //    public static final ItemRegistryObject ID_CARD;
    public static final ItemRegistryObject AUTH_QRCODE;

    public static final ItemRegistryObject HITACHI_B85_DOOR_1;
    public static final ItemRegistryObject KONE_M_DOOR_1;
    public static final ItemRegistryObject MITSUBISHI_NEXWAY_DOOR_1;
    public static final ItemRegistryObject OTIS_E411_US_DOOR_1;
    public static final ItemRegistryObject SCHINDLER_QKS9_DOOR_1;


    static {

        YTE_LIFT_BUTTONS_LINK_CONNECTOR = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "yte_lift_buttons_link_connector"), itemSettings -> new Item(new YteLiftButtonsLinker(true, itemSettings)), CreativeModeTabs.YTE_TOOLS);
        YTE_LIFT_BUTTONS_LINK_REMOVER = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "yte_lift_buttons_link_remover"), itemSettings -> new Item(new YteLiftButtonsLinker(false, itemSettings)), CreativeModeTabs.YTE_TOOLS);
        YTE_GROUP_LIFT_BUTTONS_LINK_CONNECTOR = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "yte_group_lift_buttons_link_connector"), itemSettings -> new Item(new YteGroupLiftButtonsLinker(true, itemSettings)), CreativeModeTabs.YTE_TOOLS);
        YTE_GROUP_LIFT_BUTTONS_LINK_REMOVER = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "yte_group_lift_buttons_link_remover"), itemSettings -> new Item(new YteGroupLiftButtonsLinker(false, itemSettings)), CreativeModeTabs.YTE_TOOLS);
        FLOOR_AUTO_SETTER = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "floor_auto_setter"), itemSettings -> new Item(new FloorAutoSetter(itemSettings)), CreativeModeTabs.YTE_TOOLS);
//        ID_CARD = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "id_card"), itemSettings -> new Item(new FloorAutoSetter(itemSettings)));
        AUTH_QRCODE = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "auth_qrcode"), itemSettings -> new Item(new AuthQRCode(itemSettings)));

        HITACHI_B85_DOOR_1 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "hitachi_b85_door_1"), itemSettings -> new Item(new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.HITACHI_B85_DOOR_1, itemSettings)), CreativeModeTabs.YTE_LIFT_DOORS);
        KONE_M_DOOR_1 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "kone_m_door_1"), itemSettings -> new Item(new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.KONE_M_DOOR_1, itemSettings)), CreativeModeTabs.YTE_LIFT_DOORS);
        MITSUBISHI_NEXWAY_DOOR_1 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "mitsubishi_nexway_door_1"), itemSettings -> new Item(new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.MITSUBISHI_NEXWAY_DOOR_1, itemSettings)), CreativeModeTabs.YTE_LIFT_DOORS);
        OTIS_E411_US_DOOR_1 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "otis_e411_us_door_1"), itemSettings -> new Item(new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.OTIS_E411_US_DOOR_1, itemSettings)), CreativeModeTabs.YTE_LIFT_DOORS);
        SCHINDLER_QKS9_DOOR_1 = Init.REGISTRY.registerItem(new Identifier(Init.MOD_ID, "schindler_qks9_door_1"), itemSettings -> new Item(new ItemPSDAPGBase(ItemPSDAPGBase.EnumPSDAPGItem.PSD_APG_DOOR, ItemPSDAPGBase.EnumPSDAPGType.SCHINDLER_QKS9_DOOR_1, itemSettings)), CreativeModeTabs.YTE_LIFT_DOORS);
    }

    public static void init() {
    }

}
