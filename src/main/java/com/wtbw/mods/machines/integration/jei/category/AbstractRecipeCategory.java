package com.wtbw.mods.machines.integration.jei.category;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wtbw.mods.lib.util.Cache;
import com.wtbw.mods.machines.WTBWMachines;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import java.util.function.Supplier;

/*
  @author: Naxanria
*/
public abstract class AbstractRecipeCategory<T> implements IRecipeCategory<T>
{
  public final ResourceLocation uid;
  
  protected final Cache<IDrawable> background;
  protected final Cache<IDrawable> icon;
  
  protected final String localizedName;
  protected final IGuiHelper guiHelper;
  
  private final Class<? extends T> recipeClass;
  
  protected final int halfX;
  protected final int halfY;
  
  protected static String key(String name)
  {
    return WTBWMachines.MODID + ".jei." + name;
  }
  
  public AbstractRecipeCategory(Class<? extends T> recipeClass, ResourceLocation uid, String unlocalizedName,
                                IGuiHelper guiHelper, Supplier<IDrawable> background, Supplier<IDrawable> icon)
  {
    this.uid = uid;
    this.background = Cache.create(background);
    this.icon = Cache.create(icon);
    this.localizedName = Translator.translateToLocal(key(unlocalizedName));
    this.guiHelper = guiHelper;
    this.recipeClass = recipeClass;
    
    halfX = background.get().getWidth() / 2;
    halfY = background.get().getHeight() / 2;
  }
  
  @Override
  public ResourceLocation getUid()
  {
    return uid;
  }
  
  @Override
  public Class<? extends T> getRecipeClass()
  {
    return recipeClass;
  }
  
  @Override
  public String getTitle()
  {
    return localizedName;
  }
  
  @Override
  public IDrawable getBackground()
  {
    return background.get();
  }
  
  @Override
  public IDrawable getIcon()
  {
    return icon.get();
  }
  
  @Override
  public abstract void setIngredients(T recipe, IIngredients ingredients);

  @Override
  public abstract void setRecipe(IRecipeLayout recipeLayout, T recipe, IIngredients ingredients);
  
  @Override
  public abstract void draw(T recipe, MatrixStack stack, double mouseX, double mouseY);
  
}
