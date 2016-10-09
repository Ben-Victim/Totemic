package totemic_commons.pokefenn.block.totem;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import totemic_commons.pokefenn.Totemic;
import totemic_commons.pokefenn.api.TotemicStaffUsage;
import totemic_commons.pokefenn.block.BlockTileTotemic;
import totemic_commons.pokefenn.lib.Strings;
import totemic_commons.pokefenn.lib.WoodVariant;
import totemic_commons.pokefenn.tileentity.totem.TileTotemBase;
import totemic_commons.pokefenn.tileentity.totem.TileTotemPole;

/**
 * Created with IntelliJ IDEA.
 * User: Pokefenn
 * Date: 02/02/14
 * Time: 13:03
 */
public class BlockTotemPole extends BlockTileTotemic implements TotemicStaffUsage
{
    public static final PropertyEnum<WoodVariant> WOOD = PropertyEnum.create("wood", WoodVariant.class);

    public BlockTotemPole()
    {
        super(Material.WOOD);
        setRegistryName(Strings.TOTEM_POLE_NAME);
        setUnlocalizedName(Strings.TOTEM_POLE_NAME);
        setCreativeTab(Totemic.tabsTotem);
        setSoundType(SoundType.WOOD);
    }

    @Override
    public EnumActionResult onTotemicStaffRightClick(World world, BlockPos pos, EntityPlayer player, ItemStack itemStack)
    {
        if(world.isRemote)
        {
            TileTotemPole tileTotemSocket = (TileTotemPole) world.getTileEntity(pos);
            if(tileTotemSocket.getEffect() != null)
            {
                player.addChatComponentMessage(new TextComponentTranslation("totemicmisc.activeEffect", I18n.format(tileTotemSocket.getEffect().getUnlocalizedName())));
            }
        }
        return EnumActionResult.SUCCESS;
    }

    //FIXME: This method of notifying is not working correctly on the client side yet
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        super.onBlockPlacedBy(world, pos, state, entityLiving, itemStack);
        notifyTotemBase(world, pos);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        super.breakBlock(world, pos, state);
        notifyTotemBase(world, pos);
    }

    private void notifyTotemBase(World world, BlockPos pos)
    {
        for(int i = 0; i < TileTotemBase.MAX_HEIGHT; i++)
        {
            Block block = world.getBlockState(pos.down(i + 1)).getBlock();
            if(block instanceof BlockTotemBase)
            {
                ((TileTotemBase) world.getTileEntity(pos.down(i + 1))).onPoleChange();
                break;
            }
            else if(!(block instanceof BlockTotemPole))
                break;
        }
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }

    @Override
    public int quantityDropped(Random rand)
    {
        return 0;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list)
    {
        for(int i = 0; i < WoodVariant.values().length; i++)
            list.add(new ItemStack(item, 1, i));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, WOOD);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(WOOD).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(WOOD, WoodVariant.values()[meta]);
    }

    @Override
    public TileEntity createNewTileEntity(World var1, int var2)
    {
        return new TileTotemPole();
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return new AxisAlignedBB(0.1875F, 0.0F, 0.1875F, 0.8125F, 1.0F, 0.8125F);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
}
