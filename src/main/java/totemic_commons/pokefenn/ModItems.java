package totemic_commons.pokefenn;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import totemic_commons.pokefenn.item.*;
import totemic_commons.pokefenn.item.equipment.ItemBarkStripper;
import totemic_commons.pokefenn.item.equipment.ItemMedicineBag;
import totemic_commons.pokefenn.item.equipment.ItemTotemWhittlingKnife;
import totemic_commons.pokefenn.item.equipment.ItemTotemicStaff;
import totemic_commons.pokefenn.item.equipment.music.ItemFlute;
import totemic_commons.pokefenn.item.equipment.music.ItemJingleDress;
import totemic_commons.pokefenn.item.equipment.music.ItemRattle;
import totemic_commons.pokefenn.item.equipment.weapon.ItemBaykokBow;
import totemic_commons.pokefenn.lib.Strings;
import totemic_commons.pokefenn.lib.WoodVariant;

@EventBusSubscriber(modid = Totemic.MOD_ID)
@ObjectHolder(Totemic.MOD_ID)
public final class ModItems
{
    public static final ItemFlute flute = null;
    public static final ItemRattle rattle = null;
    public static final ItemJingleDress jingle_dress = null;
    public static final ItemTotemWhittlingKnife totem_whittling_knife = null;
    public static final ItemBarkStripper bark_stripper = null;
    public static final ItemTotemicStaff totemic_staff = null;
    public static final ItemTotemicItems sub_items = null;
    public static final ItemTotempedia totempedia = null;
    public static final ItemBuffaloDrops buffalo_items = null;
    public static final ItemFood buffalo_meat = null;
    public static final ItemFood cooked_buffalo_meat = null;
    public static final ItemBaykokBow baykok_bow = null;
    public static final ItemMedicineBag medicine_bag = null;
    public static final ItemCeremonyCheat ceremony_cheat = null;

    @SubscribeEvent
    public static void init(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll(
            makeItemBlock(ModBlocks.cedar_log),
            makeItemBlock(ModBlocks.stripped_cedar_log),
            makeItemBlock(ModBlocks.cedar_plank),
            makeItemBlock(ModBlocks.cedar_sapling),
            makeItemBlock(ModBlocks.cedar_leaves),
            new ItemBlockVariants(ModBlocks.totem_base).setRegistryName(ModBlocks.totem_base.getRegistryName()),
            new ItemBlockVariants(ModBlocks.totem_pole).setRegistryName(ModBlocks.totem_pole.getRegistryName()),
            makeItemBlock(ModBlocks.totem_torch),
            makeItemBlock(ModBlocks.drum),
            makeItemBlock(ModBlocks.wind_chime),
            new ItemTipi(ModBlocks.tipi).setRegistryName(ModBlocks.tipi.getRegistryName()),

            new ItemFlute(),
            new ItemRattle(),
            new ItemJingleDress(),
            new ItemTotemWhittlingKnife(),
            new ItemBarkStripper(),
            new ItemTotemicStaff(),
            new ItemTotemicItems(),
            new ItemTotempedia(),
            new ItemBuffaloDrops(),
            new ItemFood(3, 0.35F, true).setRegistryName(Strings.BUFFALO_MEAT_NAME).setUnlocalizedName(Strings.RESOURCE_PREFIX + Strings.BUFFALO_MEAT_NAME).setCreativeTab(Totemic.tabsTotem),
            new ItemFood(9, 0.9F, true).setRegistryName(Strings.COOKED_BUFFALO_MEAT_NAME).setUnlocalizedName(Strings.RESOURCE_PREFIX + Strings.COOKED_BUFFALO_MEAT_NAME).setCreativeTab(Totemic.tabsTotem),
            new ItemBaykokBow(),
            new ItemMedicineBag(),
            new ItemCeremonyCheat());
    }

    private static ItemBlock makeItemBlock(Block block)
    {
        return (ItemBlock) new ItemBlock(block).setRegistryName(block.getRegistryName());
    }

    @SideOnly(Side.CLIENT)
    public static void setItemModels()
    {
        setDefaultModel(ModBlocks.cedar_log);
        setDefaultModel(ModBlocks.stripped_cedar_log);
        setDefaultModel(ModBlocks.cedar_plank);
        setDefaultModel(ModBlocks.cedar_sapling);
        setDefaultModel(ModBlocks.cedar_leaves);
        setDefaultModel(ModBlocks.totem_torch);
        setDefaultModel(ModBlocks.drum);
        setDefaultModel(ModBlocks.wind_chime);
        setDefaultModel(ModBlocks.tipi);

        for(WoodVariant var: WoodVariant.values())
        {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.totem_base), var.ordinal(),
                    new ModelResourceLocation(ModBlocks.totem_base.getRegistryName(), "wood=" + var.getName()));
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(ModBlocks.totem_pole), var.ordinal(),
                    new ModelResourceLocation(ModBlocks.totem_pole.getRegistryName(), "wood=" + var.getName()));
        }

        setDefaultModel(flute);
        setModel(flute, 1, flute.getRegistryName().toString());
        setDefaultModel(rattle);
        setDefaultModel(jingle_dress);
        setDefaultModel(totem_whittling_knife);
        setDefaultModel(bark_stripper);
        setDefaultModel(totemic_staff);
        setDefaultModel(totempedia);
        setDefaultModel(buffalo_meat);
        setDefaultModel(cooked_buffalo_meat);
        setDefaultModel(baykok_bow);
        setModel(medicine_bag, 0, medicine_bag.getRegistryName().toString() + "_closed");
        setModel(medicine_bag, 1, medicine_bag.getRegistryName().toString() + "_open");
        setDefaultModel(ceremony_cheat);

        for(ItemTotemicItems.Type t: ItemTotemicItems.Type.values())
            setModel(sub_items, t.ordinal(), Strings.RESOURCE_PREFIX + t.toString());

        for(ItemBuffaloDrops.Type t: ItemBuffaloDrops.Type.values())
            setModel(buffalo_items, t.ordinal(), Strings.RESOURCE_PREFIX + t.toString());
    }

    @SideOnly(Side.CLIENT)
    private static void setModel(Item item, int meta, String modelName)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(modelName, "inventory"));
    }

    @SideOnly(Side.CLIENT)
    private static void setDefaultModel(Item item)
    {
        setModel(item, 0, item.getRegistryName().toString());
    }

    @SideOnly(Side.CLIENT)
    private static void setDefaultModel(Block block)
    {
        setDefaultModel(Item.getItemFromBlock(block));
    }

}
