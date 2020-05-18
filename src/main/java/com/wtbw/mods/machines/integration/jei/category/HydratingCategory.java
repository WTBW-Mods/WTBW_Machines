package com.wtbw.mods.machines.integration.jei.category;

import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.lib.gui.util.FluidBar;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.machines.ClientConstants;
import com.wtbw.mods.machines.block.ModBlocks;
import com.wtbw.mods.machines.gui.screen.DehydratorScreen;
import com.wtbw.mods.machines.gui.screen.HydratorScreen;
import com.wtbw.mods.machines.recipe.DehydratingRecipe;
import com.wtbw.mods.machines.recipe.HydratingRecipe;
import com.wtbw.mods.machines.tile.machine.DehydratorTileEntity;
import com.wtbw.mods.machines.tile.machine.HydratorEntity;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
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
public class HydratingCategory extends AbstractRecipeCategory<HydratingRecipe>
{
  public static final int INPUT_SLOT = HydratorEntity.INPUT_SLOT;
  public static final int OUTPUT_SLOT = HydratorEntity.OUTPUT_SLOT;
  public static final ResourceLocation UID = ClientConstants.getLocation("hydrating_category");
  
  private final IDrawableAnimated progress;
  
  EnergyBar energyBar = new EnergyBar(new BaseEnergyStorage(100000), 0, 0).setDimensions(16, 54).cast();
  FluidBar fluidBar = new FluidBar(new FluidTank(5000), 0, 0).setDimensions(16, 54).cast();
  
  
  public HydratingCategory(IGuiHelper guiHelper)
  {
    super
      (
        HydratingRecipe.class,
        UID,
        "hydrating",
        guiHelper,
        () -> guiHelper.createDrawable(ClientConstants.Jei.BACKGROUND, 0, 54, 90, 54),
        () -> guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.HYDRATOR))
      );
    
    progress = guiHelper.drawableBuilder(ClientConstants.ICONS, 48, 18, 16, 16).buildAnimated(200, IDrawableAnimated.StartDirection.TOP, false);
  }
  
  @Override
  public void setIngredients(HydratingRecipe recipe, IIngredients ingredients)
  {
    ingredients.setInputIngredients(recipe.getIngredients());
    ingredients.setInput(VanillaTypes.FLUID, new FluidStack(Fluids.WATER, recipe.waterCost));
    ingredients.setOutput(VanillaTypes.ITEM, recipe.output);
  }
  
  @Override
  public void setRecipe(IRecipeLayout recipeLayout, HydratingRecipe recipe, IIngredients ingredients)
  {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    guiItemStacks.init(INPUT_SLOT, true, halfX - 9, 0);
    guiItemStacks.init(OUTPUT_SLOT, false, halfX - 9, 36);
  
    guiItemStacks.set(ingredients);
  }
  
  @Override
  public void draw(HydratingRecipe recipe, double mouseX, double mouseY)
  {
    int x = halfX - 8;
    int y = halfY - 8;
    HydratorScreen.PROGRESS.render(x, y);
    progress.draw(x, y);
    energyBar.storage.setEnergy(recipe.powerCost);
    energyBar.update();
    energyBar.draw(0, 0);
  
    setFluidAmount(recipe.waterCost);
    fluidBar.update();
    fluidBar.setLocation(background.get().getWidth() - fluidBar.getWidth() - 2, 0);
    fluidBar.draw(0, 0);
  }
  
  @Override
  public List<String> getTooltipStrings(HydratingRecipe recipe, double mouseX, double mouseY)
  {
    energyBar.storage.setEnergy(recipe.powerCost);
    energyBar.update();
  
    setFluidAmount(recipe.waterCost);
    fluidBar.update();
  
    if (energyBar.isHover((int) mouseX, (int) mouseY))
    {
      return energyBar.getTooltip();
    }
    else if (fluidBar.isHover((int) mouseX, (int) mouseY))
    {
      return fluidBar.getTooltip();
    }
    
    return Collections.emptyList();
  }
  
  private void setFluidAmount(int fluidAmount)
  {
    ((FluidTank) fluidBar.tank).setFluid(new FluidStack(Fluids.WATER, fluidAmount));
  }
}
