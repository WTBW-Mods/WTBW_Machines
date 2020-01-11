package com.wtbw.mods.machines.integration.jei.category;

import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.machines.ClientConstants;
import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.block.ModBlocks;
import com.wtbw.mods.machines.gui.screen.PoweredFurnaceScreen;
import com.wtbw.mods.machines.recipe.PoweredFurnaceRecipe;
import com.wtbw.mods.machines.tile.machine.PoweredFurnaceEntity;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
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
public class PoweredFurnaceCategory extends AbstractRecipeCategory<PoweredFurnaceRecipe>
{
  public static final ResourceLocation UID = ClientConstants.getLocation("powered_furnace_category");
  
  public static final int INPUT_SLOT = PoweredFurnaceEntity.INPUT_SLOT;
  public static final int OUTPUT_SLOT = PoweredFurnaceEntity.OUTPUT_SLOT;
  
  private final IDrawableAnimated progress;

  EnergyBar energyBar = new EnergyBar(new BaseEnergyStorage(100000),  0, 0).setDimensions(16 , 54).cast();


  public PoweredFurnaceCategory(IGuiHelper guiHelper)
  {
    super
      (
        PoweredFurnaceRecipe.class,
        UID,
        "powered_furnace",
        guiHelper,
        () -> guiHelper.createDrawable(ClientConstants.Jei.BACKGROUND, 0, 54, 90, 54),
        () -> guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.POWERED_FURNACE))
      );
    
    WTBWMachines.LOGGER.info("Created!");
    progress = guiHelper.drawableBuilder(ClientConstants.ICONS, 34, 15, 14, 15)
      .buildAnimated(200, IDrawableAnimated.StartDirection.TOP, false);
  }
  
  @Override
  public void setIngredients(PoweredFurnaceRecipe recipe, IIngredients ingredients)
  {
    ingredients.setInputIngredients(recipe.getIngredients());
    ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
  }
  
  @Override
  public void setRecipe(IRecipeLayout recipeLayout, PoweredFurnaceRecipe recipe, IIngredients ingredients)
  {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
  
    guiItemStacks.init(INPUT_SLOT, true, halfX - 9, 0);
    guiItemStacks.init(OUTPUT_SLOT, false, halfX - 9, 36);

    guiItemStacks.set(ingredients);
  }
  
  @Override
  public void draw(PoweredFurnaceRecipe recipe, double mouseX, double mouseY)
  {
    PoweredFurnaceScreen.ICONS.getSprite(34, 0, 14, 14).render(halfX - 7, halfY - 7);
    progress.draw(halfX - 7, halfY - 7);
    energyBar.storage.setEnergy(recipe.powerCost);
    energyBar.update();
    energyBar.draw(0, 0);
  }

  @Override
  public List<String> getTooltipStrings(PoweredFurnaceRecipe recipe, double mouseX, double mouseY) {
    energyBar.storage.setEnergy(recipe.powerCost);
    energyBar.update();
    return energyBar.isHover((int) mouseX, (int) mouseY) ? energyBar.getTooltip() : Collections.emptyList();
  }
}
