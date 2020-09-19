/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  org.lwjgl.opengl.GL11
 */
package xyz.WorstClient.module.modules.render;

import java.util.Iterator;
import java.awt.Color;
import java.util.List;
import javax.vecmath.Vector3d;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.rendering.EventRender3D;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.utils.math.Vec4f;
import xyz.WorstClient.utils.render.GLUProjection;
import xyz.WorstClient.utils.render.RenderUtil;

import org.lwjgl.opengl.GL11;

public class ChestESP
extends Module {
	public static Option outlinedboundingBox = new Option("OutlinedBoundingBox", "OutlinedBoundingBox", true);
	public static Option boundingBox = new Option("BoundingBox", "BoundingBox", true);
    public ChestESP() {
        super("ChestESP", new String[]{"chesthack"}, ModuleType.Render);
        this.addValues(this.outlinedboundingBox,this.boundingBox);
    }

    @EventHandler
    public void onRender(EventRender3D eventRender) {
        Iterator var3;
    	var3 = this.mc.theWorld.loadedTileEntityList.iterator();

        while(true) {
           TileEntity ent;
           do {
              if (!var3.hasNext()) {
                 return;
              }

              ent = (TileEntity)var3.next();
           } while(!(ent instanceof TileEntityChest) && !(ent instanceof TileEntityEnderChest));

           this.mc.getRenderManager();
           double x = (double)ent.getPos().getX() - RenderManager.renderPosX;
           this.mc.getRenderManager();
           double y = (double)ent.getPos().getY() - RenderManager.renderPosY;
           this.mc.getRenderManager();
           double z = (double)ent.getPos().getZ() - RenderManager.renderPosZ;
           GL11.glPushMatrix();
           GL11.glEnable(3042);
           GL11.glBlendFunc(770, 771);
           GL11.glDisable(3553);
           GL11.glEnable(2848);
           GL11.glDisable(2929);
           GL11.glDepthMask(false);
           Color rainbow = Gui.rainbow(System.nanoTime(), 1.0F, 1.0F);
           GL11.glColor4f(setColor.r.getValue().floatValue()/255.0f,setColor.g.getValue().floatValue()/255.0f,setColor.b.getValue().floatValue()/255.0f,setColor.a.getValue().floatValue()/255.0f);
           if(((Boolean) this.boundingBox.getValue()).booleanValue()) {
           RenderUtil.drawBoundingBox(new AxisAlignedBB(x + ent.getBlockType().getBlockBoundsMinX(), y + ent.getBlockType().getBlockBoundsMinY(), z + ent.getBlockType().getBlockBoundsMinZ(), x + ent.getBlockType().getBlockBoundsMaxX(), y + ent.getBlockType().getBlockBoundsMaxY(), z + ent.getBlockType().getBlockBoundsMaxZ()));
           }
           if(((Boolean) this.outlinedboundingBox.getValue()).booleanValue()) {
           RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x + ent.getBlockType().getBlockBoundsMinX(), y + ent.getBlockType().getBlockBoundsMinY(), z + ent.getBlockType().getBlockBoundsMinZ(), x + ent.getBlockType().getBlockBoundsMaxX(), y + ent.getBlockType().getBlockBoundsMaxY(), z + ent.getBlockType().getBlockBoundsMaxZ()));
           }
           GL11.glDisable(2848);
           GL11.glEnable(3553);
           GL11.glEnable(2929);
           GL11.glDepthMask(true);
           GL11.glDisable(3042);
           GL11.glPopMatrix();
}
    }
    }
