package com.wtbw.mods.machines.integration.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.AbstractGui;

/*
  @author: Sunekaer
*/
public class ScreenResourceWrapper implements IDrawable {
    private ScreenResources resource;

    public ScreenResourceWrapper(ScreenResources resource) {
        this.resource = resource;
    }

    @Override
    public int getWidth() {
        return resource.width;
    }

    @Override
    public int getHeight() {
        return resource.height;
    }

    @Override
    public void draw(int xOffset, int yOffset) {
        resource.bind();
        AbstractGui.blit(xOffset, yOffset, 0, resource.startX, resource.startY, resource.width, resource.height, 256,
                256);
    }
}
