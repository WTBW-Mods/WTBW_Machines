package com.wtbw.mods.machines.integration.jei.category;

import com.wtbw.mods.lib.util.BiValue;
import com.wtbw.mods.lib.util.Utilities;
import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.block.ModBlocks;
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
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/*
  @author: Sunekaer, Naxanria
*/
public class CrusherCategory implements IRecipeCategory<CrushingRecipe>
{
  protected static final int inputSlot = PoweredCrusherEntity.INPUT_SLOT;
  
  public static final ResourceLocation GUI = new ResourceLocation("wtbw_machines:textures/gui/recipe_crushing.png");
  public static final ResourceLocation ICONS = new ResourceLocation("wtbw_machines:textures/gui/icons.png");
  
  protected final IDrawableAnimated progress;
  protected final IDrawableStatic background;
  
  private final String localizedName;
  private final IGuiHelper guiHelper;
  
  public static final ResourceLocation UID = new ResourceLocation(WTBWMachines.MODID, "crusher_category");
  public CrusherCategory(IGuiHelper guiHelper)
  {
    this.guiHelper = guiHelper;
    this.background = guiHelper.createDrawable(GUI, 0, 0, 126, 54);
    this.localizedName = Translator.translateToLocal("wtbw_machines.jei.crushing");
    this.progress = guiHelper.drawableBuilder(ICONS, 10, 10, 10, 10).buildAnimated(300, IDrawableAnimated.StartDirection.TOP, false);
  }
  
  @Override
  public ResourceLocation getUid()
  {
    return UID;
  }
  
  @Override
  public Class<? extends CrushingRecipe> getRecipeClass()
  {
    return CrushingRecipe.class;
  }
  
  @Override
  public String getTitle()
  {
    return this.localizedName;
  }
  
  @Override
  public IDrawable getBackground()
  {
    return background;
  }
  
  @Override
  public IDrawable getIcon()
  {
    return guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.POWERED_CRUSHER));
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
    guiItemStacks.init(inputSlot, true, background.getWidth() / 2 - 9, 0);
    
    int x = 0;
    int y = 36;
    int count = 6;
    int dx = 18;
    
    for (int i = 0; i < count; i++)
    {
      guiItemStacks.init(i + 1, false, x, y);
      x += dx;
    }

    guiItemStacks.set(ingredients);
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
  
  private String convert(float chance)
  {
    return Utilities.df_2.format(chance * 100f) + "%";
  }
  
  @Override
  public void draw(CrushingRecipe recipe, double mouseX, double mouseY)
  {
    this.progress.draw(background.getWidth() / 2 - 5, 22);
  }
}
