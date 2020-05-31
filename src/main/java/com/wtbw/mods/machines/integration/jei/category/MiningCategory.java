package com.wtbw.mods.machines.integration.jei.category;

import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.lib.gui.util.FluidBar;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.machines.ClientConstants;
import com.wtbw.mods.machines.block.ModBlocks;
import com.wtbw.mods.machines.recipe.MiningRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.Collections;
import java.util.List;

/*
  @author: Naxanria
*/
public class MiningCategory extends AbstractRecipeCategory<MiningRecipe>
{
  public static final ResourceLocation UID = ClientConstants.getLocation("mining_category");
  
  private EnergyBar energyBar = new EnergyBar(new BaseEnergyStorage(1000000), 0, 0).setDimensions(16, 54).cast();
  private FluidBar fluidBar = new FluidBar(new FluidTank(10000), 0, 0).setDimensions(16, 54).cast();
  
  public MiningCategory(IGuiHelper guiHelper)
  {
    super(MiningRecipe.class, UID, "mining", guiHelper,
      () -> guiHelper.createDrawable(ClientConstants.Jei.BACKGROUND, 0, 108, 156, 57),
      () -> guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.MICRO_MINER)));
  }
  
  @Override
  public void setIngredients(MiningRecipe recipe, IIngredients ingredients)
  {
    ingredients.setInputIngredients(Collections.singletonList(recipe.miner));
    ingredients.setInput(VanillaTypes.FLUID, new FluidStack(Fluids.WATER, recipe.coolantCost));
    ingredients.setOutputs(VanillaTypes.ITEM, recipe.output);
  }
  
  @Override
  public void setRecipe(IRecipeLayout recipeLayout, MiningRecipe recipe, IIngredients ingredients)
  {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    guiItemStacks.init(0, true, 53, 3);
    
    int x = 98;
    int y = 3;
    int d = 18;
  
    for (int i = 0; i < 3; i++)
    {
      int yp = y + i * d;
      for (int j = 0; j < 3; j++)
      {
        int xp = x + j * d;
        guiItemStacks.init(i + j * 3 + 1, false, xp, yp);
      }
    }
    guiItemStacks.set(ingredients);
  }
  
  @Override
  public void draw(MiningRecipe recipe, double mouseX, double mouseY)
  {
    updateEnergy(recipe);
    energyBar.draw();
  
    updateCoolant(recipe);
    fluidBar.setLocation(energyBar.getWidth() + 2, 0);
    fluidBar.draw();
  }
  
  @Override
  public List<String> getTooltipStrings(MiningRecipe recipe, double mouseX, double mouseY)
  {
    if (energyBar.isHover((int) mouseX, (int) mouseY))
    {
      updateEnergy(recipe);
      return energyBar.getTooltip();
    }
    
    if (fluidBar.isHover((int) mouseX, (int) mouseY))
    {
      updateCoolant(recipe);
      return fluidBar.getTooltip();
    }
    
    return Collections.emptyList();
  }
  
  private void updateEnergy(MiningRecipe recipe)
  {
    energyBar.storage.setEnergy(recipe.powerCost);
    energyBar.update();
  }
  
  private void updateCoolant(MiningRecipe recipe)
  {
    ((FluidTank) fluidBar.tank).setFluid(new FluidStack(Fluids.WATER, recipe.coolantCost));
    fluidBar.update();
  }
}
