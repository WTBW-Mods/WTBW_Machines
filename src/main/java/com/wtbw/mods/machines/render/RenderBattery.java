package com.wtbw.mods.machines.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.wtbw.mods.lib.tile.util.energy.BaseEnergyStorage;
import com.wtbw.mods.machines.tile.SimpleBatteryTileEntity;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;

/*
  @author: Sunekaer
*/
public class RenderBattery extends TileEntityRenderer<SimpleBatteryTileEntity> {
    public RenderBattery(TileEntityRendererDispatcher renderer){
        super(renderer);
    }

    @Override
    public void render(SimpleBatteryTileEntity tile, float partialTick, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int light, int otherlight){
        BaseEnergyStorage storage = tile.getStorage();
        float p = storage.getPercentageFilled();

        if (p <= 0) {
            return;
        }

        float h = (3F + 10F * p) / 16F;
        int red = (int)(255F - p * 255F);
        int green = (int)(p * 255F);

        Tessellator t = Tessellator.getInstance();
        BufferBuilder buffer =t.getBuffer();

        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        RenderSystem.color4f(1,1,1, 1);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            matrixStackIn.push();
            matrixStackIn.translate(0.5F, 0.5F, 0.5F);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(direction.rotateYCCW().getHorizontalAngle()));
            matrixStackIn.translate(-0.5F, -0.5F, -0.5F);

            Matrix4f m = matrixStackIn.getLast().getMatrix();

            buffer.pos(m, 6F/16F, 3F/16F, -0.001F).color(red, green, 0, 150).endVertex();
            buffer.pos(m, 10F/16F, 3F/16F, -0.001F).color(red, green, 0, 150).endVertex();
            buffer.pos(m, 10F/16F, h, -0.001F).color(red, green, 0, 150).endVertex();
            buffer.pos(m, 6F/16F, h, -0.001F).color(red, green, 0, 150).endVertex();

            matrixStackIn.pop();
        }

        t.draw();

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();

    }
}
