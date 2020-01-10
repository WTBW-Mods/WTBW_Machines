package com.wtbw.mods.machines.integration.jei.category;

import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.lib.gui.util.ProgressBar;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.lib.util.Cache;
import com.wtbw.mods.machines.ClientConstants;
import com.wtbw.mods.machines.block.ModBlocks;
import com.wtbw.mods.machines.gui.screen.CompressorScreen;
import com.wtbw.mods.machines.recipe.CompressingRecipe;
import com.wtbw.mods.machines.recipe.CrushingRecipe;
import com.wtbw.mods.machines.tile.machine.PoweredCompressorEntity;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.List;

/*
  @author: Naxanria
*/
public class CompressingCategory extends AbstractRecipeCategory<CompressingRecipe>
{
  public static final int INPUT_SLOT = PoweredCompressorEntity.INPUT_SLOT;
  public static final int OUTPUT_SLOT = PoweredCompressorEntity.OUTPUT_SLOT;
  
  public static final ResourceLocation UID = ClientConstants.getLocation("compressing_category");

  private final IDrawableAnimated progressLeft;
  private final IDrawableAnimated progressRight;

  EnergyBar energyBar;

  public CompressingCategory(IGuiHelper guiHelper)
  {
    super
    (
      CompressingRecipe.class,
      UID,
      "compressing",
      guiHelper,
      () -> guiHelper.createDrawable(ClientConstants.Jei.BACKGROUND,0, 54, 90, 54),
      () -> guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.POWERED_COMPRESSOR))
    );
    
    progressLeft = guiHelper.drawableBuilder(ClientConstants.ICONS, 0, 30, 10, 10)
      .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    progressRight = guiHelper.drawableBuilder(ClientConstants.ICONS, 10, 30, 10, 10)
      .buildAnimated(200, IDrawableAnimated.StartDirection.RIGHT, false);
  }
  
  @Override
  public void setIngredients(CompressingRecipe recipe, IIngredients ingredients)
  {
    ingredients.setInputIngredients(recipe.getIngredients());
    ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
  }
  
  @Override
  public void setRecipe(IRecipeLayout recipeLayout, CompressingRecipe recipe, IIngredients ingredients)
  {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    
    guiItemStacks.init(INPUT_SLOT, true, halfX - 9, 0);
    guiItemStacks.init(OUTPUT_SLOT, false, halfX - 9, 36);
    energyBar = new EnergyBar(new BaseEnergyStorage(100000), 0, 0).setDimensions(16, 54).cast();
    energyBar.storage.setEnergy(recipe.powerCost);
    energyBar.update();
    guiItemStacks.set(ingredients);
  }
  
  @Override
  public void draw(CompressingRecipe recipe, double mouseX, double mouseY)
  {
    CompressorScreen.PROGRESS_BACKGROUND_LEFT.render(halfX - 10, halfY - 5);
    CompressorScreen.PROGRESS_BACKGROUND_RIGHT.render(halfX, halfY - 5);
    progressLeft.draw(halfX - 10, halfY - 5);
    progressRight.draw(halfX, halfY - 5);
    energyBar.draw(0, 0);
  }

  @Override
  public List<String> getTooltipStrings(CompressingRecipe recipe, double mouseX, double mouseY) {
    return energyBar.isHover((int) mouseX, (int) mouseY) ? energyBar.getTooltip() : Collections.emptyList();
  }
}
