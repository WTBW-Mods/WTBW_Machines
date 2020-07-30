package com.wtbw.mods.machines.integration.jei.category;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wtbw.mods.lib.gui.util.EnergyBar;
import com.wtbw.mods.lib.gui.util.GuiUtil;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.lib.util.TextComponentBuilder;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.machines.ClientConstants;
import com.wtbw.mods.machines.block.ModBlocks;
import com.wtbw.mods.machines.gui.screen.CompressorScreen;
import com.wtbw.mods.machines.recipe.CompressingRecipe;
import com.wtbw.mods.machines.tile.machine.PoweredCompressorEntity;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
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

  EnergyBar energyBar = new EnergyBar(new BaseEnergyStorage(100000),  0, 0).setDimensions(16 , 54).cast();

  public CompressingCategory(IGuiHelper guiHelper)
  {
    super
    (
      CompressingRecipe.class,
      UID,
      "compressing",
      guiHelper,
      () -> guiHelper.createDrawable(ClientConstants.Jei.BACKGROUND,0, 54, 90, 54),
      () -> guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.COMPRESSOR))
    );
    
    progressLeft = guiHelper.drawableBuilder(ClientConstants.ICONS, 0, 30, 10, 10)
      .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    progressRight = guiHelper.drawableBuilder(ClientConstants.ICONS, 10, 30, 10, 10)
      .buildAnimated(200, IDrawableAnimated.StartDirection.RIGHT, false);
  }
  
  @Override
  public void setIngredients(CompressingRecipe recipe, IIngredients ingredients)
  {
    List<ItemStack> inputs = new ArrayList<>();
    for (Ingredient ingredient : recipe.getIngredients())
    {
      for (ItemStack matchingStack : ingredient.getMatchingStacks())
      {
        ItemStack copy;
        inputs.add(copy = matchingStack.copy());
        copy.setCount(recipe.ingredientCost);
      }
    }
    
//    ingredients.setInputIngredients(recipe.getIngredients());
    ingredients.setInputLists(VanillaTypes.ITEM, Util.make(new ArrayList<>(), l -> l.add(inputs)));
    ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
  }
  
  @Override
  public void setRecipe(IRecipeLayout recipeLayout, CompressingRecipe recipe, IIngredients ingredients)
  {
    IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
    
    guiItemStacks.init(INPUT_SLOT, true, halfX - 9, 0);
    guiItemStacks.init(OUTPUT_SLOT, false, halfX - 9, 36);

    guiItemStacks.set(ingredients);
  }
  
  @Override
  public void draw(CompressingRecipe recipe, MatrixStack stack, double mouseX, double mouseY)
  {
    CompressorScreen.PROGRESS_BACKGROUND_LEFT.render(stack, halfX - 10, halfY - 5);
    CompressorScreen.PROGRESS_BACKGROUND_RIGHT.render(stack, halfX, halfY - 5);
    progressLeft.draw(stack, halfX - 10, halfY - 5);
    progressRight.draw(stack, halfX, halfY - 5);
    energyBar.storage.setEnergy(recipe.powerCost);
    energyBar.update();
    energyBar.draw(stack, 0, 0);
//
//    if (recipe.ingredientCost > 1)
//    {
//      Minecraft.getInstance().fontRenderer.drawStringWithShadow(String.valueOf(recipe.ingredientCost), halfX + 9, 18 - 10, 0xffffffff);
//    }
  }

  @Override
  public List<ITextComponent> getTooltipStrings(CompressingRecipe recipe, double mouseX, double mouseY)
  {
    energyBar.storage.setEnergy(recipe.powerCost);
    energyBar.update();
    return energyBar.isHover((int) mouseX, (int) mouseY) ? TextComponentBuilder.strings(energyBar.getTooltip()) : Collections.emptyList();
  }
}
