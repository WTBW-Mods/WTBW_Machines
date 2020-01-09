package com.wtbw.mods.machines.integration.jei.category;

import com.wtbw.mods.lib.gui.util.GuiUtil;
import com.wtbw.mods.lib.gui.util.ProgressBar;
import com.wtbw.mods.lib.gui.util.SpriteProgressBar;
import com.wtbw.mods.lib.gui.util.sprite.Sprite;
import com.wtbw.mods.lib.gui.util.sprite.SpriteMap;
import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.block.ModBlocks;
import com.wtbw.mods.machines.integration.jei.AnimationTickHolder;
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
import mezz.jei.config.Constants;
import mezz.jei.util.Translator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
/*
  @author: Sunekaer
*/
public class CrusherCategory implements IRecipeCategory<CrushingRecipe> {
    protected static final int inputSlot = PoweredCrusherEntity.INPUT_SLOT;
    protected static final int outputSlot = PoweredCrusherEntity.OUTPUT_SLOT;
    protected static final int outputSlot2 = PoweredCrusherEntity.OUTPUT_SLOT2;
    protected static final int outputSlot3 = PoweredCrusherEntity.OUTPUT_SLOT3;


    public static final ResourceLocation GUI = new ResourceLocation("wtbw_machines:textures/gui/recipe_crushing.png");
    public static final ResourceLocation ICONS = new ResourceLocation("wtbw_machines:textures/gui/icons.png");

    //protected final IDrawableAnimated animatedFlame;
    protected final IDrawableAnimated arrow;


//     new SpriteProgressBar(0 , 0, PROGRESS, PROGRESS_BACKGROUND, () -> 20, () -> AnimationTickHolder.getTicks()).setFillDirection(ProgressBar.FillDirection.TOP_BOTTOM).cast();

    protected final IDrawableStatic staticFlame;
    private final String localizedName;
    private IGuiHelper guiHelper;

    public static final ResourceLocation UID = new ResourceLocation(WTBWMachines.MODID, "crusher_category");

    public CrusherCategory(IGuiHelper guiHelper){
        this.guiHelper = guiHelper;
        this.staticFlame = guiHelper.createDrawable(GUI, 0, 0, 82, 54);
        this.localizedName = Translator.translateToLocal("wtbw_machines.jei.crushing");
        //this.animatedFlame = guiHelper.createAnimatedDrawable(this.staticFlame, 300, IDrawableAnimated.StartDirection.TOP, true);
        this.arrow = guiHelper.drawableBuilder(ICONS, 10, 10, 10, 10).buildAnimated(300, IDrawableAnimated.StartDirection.TOP, false);
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
        return staticFlame;
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
        guiItemStacks.init(inputSlot, true, 31, 0);
        guiItemStacks.init(outputSlot, false, 13, 36);
        guiItemStacks.init(outputSlot2, false, 31, 36);
        guiItemStacks.init(outputSlot3, false, 49, 36);
        guiItemStacks.set(ingredients);
    }

    @Override
    public void draw(CrushingRecipe recipe, double mouseX, double mouseY) {

        //this.animatedFlame.draw(1, 20);
        this.arrow.draw(35, 22);

//        GuiUtil.renderSlotBackground(32, 0);
//        GuiUtil.renderSlotBackground(14, 36);
//        GuiUtil.renderSlotBackground(32, 36);
//        GuiUtil.renderSlotBackground(50, 36);

    }


}
