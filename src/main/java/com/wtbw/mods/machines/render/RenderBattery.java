package com.wtbw.mods.machines.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.machines.tile.SimpleBatteryTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

/*
  @author: Sunekaer
*/
public class RenderBattery extends TileEntityRenderer<SimpleBatteryTileEntity> {
    public RenderBattery(TileEntityRendererDispatcher renderer){
        super(renderer);
    }

    @Override
    public void func_225616_a_(SimpleBatteryTileEntity tile, float partialTick, MatrixStack matrix, IRenderTypeBuffer buffer, int int1, int int2){
        BaseEnergyStorage storage = tile.getStorage();
//        storage.getPercentageFilled();

    }
}
