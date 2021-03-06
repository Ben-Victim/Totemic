package totemic_commons.pokefenn.item.equipment.music;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import totemic_commons.pokefenn.ModSounds;
import totemic_commons.pokefenn.Totemic;
import totemic_commons.pokefenn.api.music.ItemInstrument;
import totemic_commons.pokefenn.lib.Strings;

public class ItemRattle extends ItemInstrument
{
    public ItemRattle()
    {
        setSound(ModSounds.rattle);

        setRegistryName(Strings.RATTLE_NAME);
        setUnlocalizedName(Strings.RESOURCE_PREFIX + Strings.RATTLE_NAME);
        setCreativeTab(Totemic.tabsTotem);
        setMaxStackSize(1);
    }

    @Override
    public boolean onEntitySwing(EntityLivingBase entity, ItemStack stack)
    {
        if(!entity.world.isRemote)
            useInstrument(stack, entity, 16, 0, 0);
        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack stack = player.getHeldItem(hand);
        if(!player.isSwingInProgress)
            player.swingArm(hand);
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }
}
