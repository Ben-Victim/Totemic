/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Totemic Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 *
 * File Created @ [Jan 19, 2014, 4:58:19 PM (GMT)]
 */
package totemic_commons.pokefenn.totempedia.page;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import totemic_commons.pokefenn.client.RenderHelper;
import totemic_commons.pokefenn.lib.Resources;
import vazkii.botania.totemic_custom.api.internal.IGuiLexiconEntry;
import vazkii.botania.totemic_custom.api.lexicon.LexiconEntry;
import vazkii.botania.totemic_custom.api.lexicon.LexiconRecipeMappings;

public class PageCraftingRecipe extends PageRecipe
{
    private static final ResourceLocation craftingOverlay = new ResourceLocation(Resources.GUI_CRAFTING_OVERLAY);

    private List<IRecipe> recipes;
    private int ticksElapsed = 0;
    private int recipeAt = 0;

    private boolean oreDictRecipe, shapelessRecipe;

    public PageCraftingRecipe(String unlocalizedName, List<IRecipe> recipes)
    {
        super(unlocalizedName);
        this.recipes = recipes;
    }

    public PageCraftingRecipe(String unlocalizedName, IRecipe... recipe)
    {
        this(unlocalizedName, Arrays.asList(recipe));
    }

    @Override
    public void onPageAdded(LexiconEntry entry, int index)
    {
        for(IRecipe recipe : recipes)
            LexiconRecipeMappings.map(recipe.getRecipeOutput(), entry, index);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderRecipe(IGuiLexiconEntry gui, int mx, int my)
    {
        oreDictRecipe = shapelessRecipe = false;

        IRecipe recipe = recipes.get(recipeAt);
        renderCraftingRecipe(gui, recipe);


        TextureManager render = Minecraft.getMinecraft().renderEngine;
        render.bindTexture(craftingOverlay);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        ((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());

        int iconX = gui.getLeft() + 115;
        int iconY = gui.getTop() + 12;

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if(shapelessRecipe)
        {
            ((GuiScreen) gui).drawTexturedModalRect(iconX, iconY, 240, 0, 16, 16);

            if(mx >= iconX && my >= iconY && mx < iconX + 16 && my < iconY + 16)
                RenderHelper.renderTooltip(mx, my, Collections.singletonList(I18n.format("totemicmisc.shapeless")));

            iconY += 20;
        }

        render.bindTexture(craftingOverlay);
        GL11.glEnable(GL11.GL_BLEND);

        if(oreDictRecipe)
        {
            ((GuiScreen) gui).drawTexturedModalRect(iconX, iconY, 240, 16, 16, 16);

            if(mx >= iconX && my >= iconY && mx < iconX + 16 && my < iconY + 16)
                RenderHelper.renderTooltip(mx, my, Collections.singletonList(I18n.format("totemicmisc.oredict")));
        }
        GL11.glDisable(GL11.GL_BLEND);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateScreen()
    {
        if(ticksElapsed % 20 == 0)
        {
            recipeAt++;

            if(recipeAt == recipes.size())
                recipeAt = 0;
        }
        ++ticksElapsed;
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    public void renderCraftingRecipe(IGuiLexiconEntry gui, IRecipe recipe)
    {
        if(recipe instanceof ShapedRecipes)
        {
            ShapedRecipes shaped = (ShapedRecipes) recipe;

            for(int y = 0; y < shaped.recipeHeight; y++)
                for(int x = 0; x < shaped.recipeWidth; x++)
                    renderItemAtGridPos(gui, 1 + x, 1 + y, shaped.recipeItems[y * shaped.recipeWidth + x], true);
        }
        else if(recipe instanceof ShapedOreRecipe)
        {
            ShapedOreRecipe shaped = (ShapedOreRecipe) recipe;

            for(int y = 0; y < shaped.getHeight(); y++)
                for(int x = 0; x < shaped.getWidth(); x++)
                {
                    Object input = shaped.getInput()[y * shaped.getWidth() + x];
                    if(input != null)
                        renderItemAtGridPos(gui, 1 + x, 1 + y, input instanceof ItemStack ? (ItemStack) input : ((List<ItemStack>) input).get(0), true);
                }

            oreDictRecipe = true;
        }
        else if(recipe instanceof ShapelessRecipes)
        {
            ShapelessRecipes shapeless = (ShapelessRecipes) recipe;

            drawGrid:
            {
                for(int y = 0; y < 3; y++)
                    for(int x = 0; x < 3; x++)
                    {
                        int index = y * 3 + x;

                        if(index >= shapeless.recipeItems.size())
                            break drawGrid;

                        renderItemAtGridPos(gui, 1 + x, 1 + y, shapeless.recipeItems.get(index), true);
                    }
            }

            shapelessRecipe = true;
        }
        else if(recipe instanceof ShapelessOreRecipe)
        {
            ShapelessOreRecipe shapeless = (ShapelessOreRecipe) recipe;

            drawGrid:
            for(int y = 0; y < 3; y++)
                for(int x = 0; x < 3; x++)
                {
                    int index = y * 3 + x;

                    if(index >= shapeless.getRecipeSize())
                        break drawGrid;

                    Object input = shapeless.getInput().get(index);
                    if(input != null)
                        renderItemAtGridPos(gui, 1 + x, 1 + y, input instanceof ItemStack ? (ItemStack) input : ((List<ItemStack>) input).get(0), true);
                }

            shapelessRecipe = true;
            oreDictRecipe = true;
        }

        renderItemAtGridPos(gui, 2, 0, recipe.getRecipeOutput(), false);
    }
}
