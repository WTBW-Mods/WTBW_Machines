package com.wtbw.mods.machines.integration.jei.category;

import com.wtbw.mods.lib.util.Cache;
import com.wtbw.mods.machines.ClientConstants;
import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.block.ModBlocks;
import com.wtbw.mods.machines.gui.screen.DehydratorScreen;
import com.wtbw.mods.machines.recipe.DehydratingRecipe;
import com.wtbw.mods.machines.tile.machine.DehydratorTileEntity;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/*
  @author: Naxanria
*/
public class DehydratingCategory extends AbstractRecipeCategory<DehydratingRecipe>
{
  public static final int INPUT_SLOT = DehydratorTileEntity.INPUT_SLOT;
  public static final int OUTPUT_SLOT = DehydratorTileEntity.OUTPUT_SLOT;
  public static final ResourceLocation UID = ClientConstants.getLocation("dehydrating_category");
  
  private final IDrawableAnimated progress;
  
  public DehydratingCategory(IGuiHelper guiHelper)
  {
    super
    (
      DehydratingRecipe.class,
      UID,
      key("dehydrating"),
      guiHelper,
      () -> guiHelper.createDrawable(ClientConstants.Jei.BACKGROUND, 0, 54, 54, 54),
      () -> guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.DEHYDRATOR))
    );
    
    progress = guiHelper.drawableBuilder(ClientConstants.ICONS, 0, 10, 10, 10).buildAnimated(200, IDrawableAnimated.StartDirection.TOP, false);
  }
  
  @Override
  public void setIngredients(DehydratingRecipe recipe, IIngredients ingredients)
  {
    ingredients.setInputIngredients(recipe.getIngredients());
    ingredients.setOutput(VanillaTypes.ITEM, recipe.output);
  }
  
  @Override
  public void setRecipe(IRecipeLayout recipeLayout, DehydratingRecipe recipe, IIngredients ingredients)
  {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    guiItemStacks.init(INPUT_SLOT, true, halfX - 9, 0);
    guiItemStacks.init(OUTPUT_SLOT, false, halfX - 9, 36);
    guiItemStacks.set(ingredients);
  }
  
  @Override
  public void draw(DehydratingRecipe recipe, double mouseX, double mouseY)
  {
    int x = halfX - 5;
    int y = halfY - 5;
    DehydratorScreen.PROGRESS_BACKGROUND.render(x, y);
    progress.draw(x, y);
  }
}
