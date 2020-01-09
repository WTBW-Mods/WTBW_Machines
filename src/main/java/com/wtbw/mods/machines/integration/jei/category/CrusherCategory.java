package com.wtbw.mods.machines.integration.jei.category;

import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.block.ModBlocks;
import com.wtbw.mods.machines.integration.jei.ScreenResourceWrapper;
import com.wtbw.mods.machines.integration.jei.ScreenResources;
import com.wtbw.mods.machines.recipe.CrushingRecipe;
import com.wtbw.mods.machines.tile.machine.PoweredCrusherEntity;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.util.Translator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.Array;
import java.util.Arrays;

/*
  @author: Sunekaer
*/
public class CrusherCategory implements IRecipeCategory<CrushingRecipe> {
    protected static final int inputSlot = PoweredCrusherEntity.INPUT_SLOT;
    protected static final int outputSlot = PoweredCrusherEntity.OUTPUT_SLOT;
    protected static final int outputSlot2 = PoweredCrusherEntity.OUTPUT_SLOT2;
    protected static final int outputSlot3 = PoweredCrusherEntity.OUTPUT_SLOT3;

    private final String localizedName;
    private IGuiHelper guiHelper;

    public static final ResourceLocation UID = new ResourceLocation(WTBWMachines.MODID, "crusher_category");

    public CrusherCategory(IGuiHelper guiHelper){
        this.guiHelper = guiHelper;
        this.localizedName = Translator.translateToLocal("wtbw_machines.jei.crushing");
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends CrushingRecipe> getRecipeClass() {
        return CrushingRecipe.class;
    }

    @Override
    public String getTitle() {
        return this.localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return new ScreenResourceWrapper(ScreenResources.CRUSHING_RECIPE);
    }

    @Override
    public IDrawable getIcon() {
        return guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.POWERED_CRUSHER));
    }

    @Override
    public void setIngredients(CrushingRecipe crushingRecipe, IIngredients ingredients) {
        ingredients.setInputIngredients(crushingRecipe.getIngredients());
        ingredients.setOutputs(VanillaTypes.ITEM, crushingRecipe.getRecipeOutputMaxList());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CrushingRecipe crushingRecipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
//        guiItemStacks.init(inputSlot, true, 32, 0);
////        guiItemStacks.init(outputSlot, false, 14, 36);
////        guiItemStacks.init(outputSlot2, false, 32, 36);
////        guiItemStacks.init(outputSlot3, false, 50, 36);
////        guiItemStacks.set(ingredients);
        guiItemStacks.init(0, true, 32,0);
        guiItemStacks.set(0, Arrays.asList(crushingRecipe.getIngredients().get(0).getMatchingStacks()));
    }

    @Override
    public void draw(CrushingRecipe recipe, double mouseX, double mouseY) {

    }
}
