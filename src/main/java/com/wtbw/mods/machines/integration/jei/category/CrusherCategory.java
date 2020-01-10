package com.wtbw.mods.machines.integration.jei.category;

import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.lib.util.BiValue;
import com.wtbw.mods.lib.util.Cache;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.machines.ClientConstants;
import com.wtbw.mods.machines.Constants;
import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.block.ModBlocks;
import com.wtbw.mods.machines.gui.screen.CrusherScreen;
import com.wtbw.mods.machines.recipe.CrushingRecipe;
import com.wtbw.mods.machines.tile.machine.PoweredCrusherEntity;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.gui.ingredient.ITooltipCallback;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
  @author: Sunekaer, Naxanria
*/
public class CrusherCategory extends AbstractRecipeCategory<CrushingRecipe>
{
  protected static final int inputSlot = PoweredCrusherEntity.INPUT_SLOT;
  
  public static final ResourceLocation UID = new ResourceLocation(WTBWMachines.MODID, "crusher_category");
  
  protected final IDrawableAnimated progress;

  EnergyBar energyBar;
  
  public CrusherCategory(IGuiHelper guiHelper)
  {
    super
    (
      CrushingRecipe.class,
      UID,
      "crushing",
      guiHelper,
      () -> guiHelper.createDrawable(ClientConstants.Jei.BACKGROUND, 0, 0, 162, 54),
      () -> guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.POWERED_CRUSHER))
    );
    
    this.progress = guiHelper.drawableBuilder(ClientConstants.ICONS, 10, 10, 10, 10).buildAnimated(300, IDrawableAnimated.StartDirection.TOP, false);
  }
  
  @Override
  public void setIngredients(CrushingRecipe crushingRecipe, IIngredients ingredients)
  {
    ingredients.setInputIngredients(crushingRecipe.getIngredients());
    List<ItemStack> outputs = new ArrayList<>();
    crushingRecipe.itemChances.forEach(value -> outputs.add(value.a));
    ingredients.setOutputs(VanillaTypes.ITEM, outputs);
  }
  
  @Override
  public void setRecipe(IRecipeLayout recipeLayout, CrushingRecipe crushingRecipe, IIngredients ingredients)
  {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    guiItemStacks.addTooltipCallback(new TooltipCallback(crushingRecipe));
    guiItemStacks.init(inputSlot, true, halfX - 9, 0);
    
    int x = 18;
    int y = 36;
    int count = 6;
    int dx = 18;
    
    for (int i = 0; i < count; i++)
    {
      guiItemStacks.init(i + 1, false, x, y);
      x += dx;
    }

    energyBar = new EnergyBar(new BaseEnergyStorage(100000),  0, 0).setDimensions(16 , 54).cast();
    energyBar.storage.setEnergy(crushingRecipe.powerCost);
    energyBar.update();
    guiItemStacks.set(ingredients);
  }
  
  private String convert(float chance)
  {
    return Utilities.df_2.format(chance * 100f) + "%";
  }
  
  @Override
  public void draw(CrushingRecipe recipe, double mouseX, double mouseY)
  {
    CrusherScreen.PROGRESS_BACKGROUND.render(halfX - 5, 22);
    progress.draw(halfX - 5, 22);
    energyBar.draw(0, 0);
  }
  
  private class TooltipCallback implements ITooltipCallback<ItemStack>
  {
    private final CrushingRecipe recipe;
    
    TooltipCallback(CrushingRecipe recipe)
    {
      this.recipe = recipe;
    }
    
    @Override
    public void onTooltip(int index, boolean bool, @Nonnull ItemStack stack, @Nonnull List<String> list)
    {
      if (index > 0)
      {
        if (--index < recipe.itemChances.size())
        {
          BiValue<ItemStack, Float> value = recipe.itemChances.get(index);
          Float chance = value.b;
          list.add(convert(chance));
        }
      }
    }
  }

  @Override
  public List<String> getTooltipStrings(CrushingRecipe recipe, double mouseX, double mouseY) {
    return energyBar.isHover((int) mouseX, (int) mouseY) ? energyBar.getTooltip() : Collections.emptyList();
  }
}
