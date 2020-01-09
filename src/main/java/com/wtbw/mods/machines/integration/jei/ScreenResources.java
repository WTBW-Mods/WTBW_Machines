package com.wtbw.mods.machines.integration.jei;

import com.wtbw.mods.machines.WTBWMachines;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;

/*
  @author: Sunekaer
*/
public enum ScreenResources {

    CRUSHING_RECIPE("recipe_crushing.png", 82, 54)

    ;

    public static final int FONT_COLOR = 0x575F7A;

    public final ResourceLocation location;
    public int width, height;
    public int startX, startY;

    private ScreenResources(String location, int width, int height) {
        this(location, 0, 0, width, height);
    }

    private ScreenResources(String location, int startX, int startY, int width, int height) {
        this.location = new ResourceLocation(WTBWMachines.MODID, "textures/gui/" + location);
        this.width = width; this.height = height;
        this.startX = startX; this.startY = startY;
    }

    public void bind() {
        Minecraft.getInstance().getTextureManager().bindTexture(location);
    }

    public void draw(AbstractGui screen, int i, int j) {
        bind();
        screen.blit(i, j, startX, startY, width, height);
    }
}
