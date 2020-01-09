package com.wtbw.mods.machines.integration.jei.category;

import com.wtbw.mods.machines.WTBWMachines;
import com.wtbw.mods.machines.block.ModBlocks;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.plugins.vanilla.cooking.AbstractCookingCategory;
import net.minecraft.util.ResourceLocation;

/*
  @author: Sunekaer
*/
public class CrusherCategory extends AbstractCookingCategory {
    private static final ResourceLocation UID = new ResourceLocation(WTBWMachines.MODID, "crusher_category");

    public CrusherCategory(IGuiHelper helper){
        super(helper, ModBlocks.CRUSHER, "wtbw_machines.jei.crusher", 100);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends CrusherRecipe> getRecipeClass() {
        return CrusherRecipe.class;
    }
}
