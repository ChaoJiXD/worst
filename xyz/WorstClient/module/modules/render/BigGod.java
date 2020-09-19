package xyz.WorstClient.module.modules.render;

 import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Timer;
import org.lwjgl.opengl.GL11;

import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.rendering.EventRender2D;
import xyz.WorstClient.api.events.rendering.EventRender3D;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.utils.RenderUtils;
import xyz.WorstClient.utils.render.RenderUtil;

public class BigGod
extends Module {
    public Mode<Enum> mode = new Mode("Mode","Mode",(Enum[])EmojiMode.values(),(Enum)EmojiMode.Yaoer);

    public BigGod() {
        super("Emoji", new String[] {"PlayerFace"}, ModuleType.Render);
        this.addValues(mode);
        this.setRemoved(true);
    }

    private boolean isValid(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer && entity.getHealth() >= 0.0f && entity != Minecraft.thePlayer) {
            return true;
        }
        return false;
    }

    @EventHandler
    public void onpre(EventPreUpdate event) {
    		this.setSuffix(this.mode.getValue());
    }
    @EventHandler
    public void onRender(EventRender3D event) {
        for (EntityPlayer entity : this.mc.theWorld.playerEntities) {
            if (!this.isValid((EntityLivingBase)entity)) continue;
            GL11.glPushMatrix();
            GL11.glEnable((int)3042);
            GL11.glDisable((int)2929);
            GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
            GlStateManager.enableBlend();
            GL11.glBlendFunc((int)770, (int)771);
            GL11.glDisable((int)3553);
            float partialTicks = this.mc.timer.renderPartialTicks;
            this.mc.getRenderManager();
            double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)partialTicks - RenderManager.renderPosX;
            this.mc.getRenderManager();
            double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * (double)partialTicks - RenderManager.renderPosY;
            this.mc.getRenderManager();
            double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)partialTicks - RenderManager.renderPosZ;
            float DISTANCE = Minecraft.thePlayer.getDistanceToEntity((Entity)entity);
            float DISTANCE_SCALE = Math.min((float)(DISTANCE * 0.15f), (float)0.15f);
            float SCALE = 0.035f;
            float xMid = (float)x;
            float yMid = (float)y + entity.height + 0.5f - (entity.isChild() ? entity.height / 2.0f : 0.0f);
            float zMid = (float)z;
            GlStateManager.translate((float)((float)x), (float)((float)y + entity.height + 0.5f - (entity.isChild() ? entity.height / 2.0f : 0.0f)), (float)((float)z));
            GL11.glNormal3f((float)0.0f, (float)1.0f, (float)0.0f);
            this.mc.getRenderManager();
            GlStateManager.rotate((float)(- RenderManager.playerViewY), (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glScalef((float)(- SCALE), (float)(- SCALE), (float)(- (SCALE /= 2.0f)));
            Tessellator tesselator = Tessellator.getInstance();
            WorldRenderer worldRenderer = tesselator.getWorldRenderer();
            Color gray = new Color(0, 0, 0);
            double thickness = 1.5f + DISTANCE * 0.01f;
            double xLeft = -20.0;
            double xRight = 20.0;
            double yUp = 27.0;
            double yDown = 130.0;
            double size = 10.0;
            GL11.glEnable((int)3553);
            GL11.glEnable((int)2929);
            GlStateManager.disableBlend();
            GL11.glDisable((int)3042);
            if (this.mode.getValue() == EmojiMode.Yaoer) {
                RenderUtils.drawImage(new ResourceLocation("Worst/yaoer.png"), (int)((int)xLeft + 9), (int)((int)yUp - 20), (int)20, (int)25);
            } else if (this.mode.getValue() == EmojiMode.Ganga) {
                RenderUtils.drawImage(new ResourceLocation("Worst/ganga.png"), (int)((int)xLeft + 7), (int)((int)yUp - 28), (int)27, (int)32);
            } else if (this.mode.getValue() == EmojiMode.Taijun) {
                RenderUtils.drawImage(new ResourceLocation("Worst/taijun.png"), (int)((int)xLeft + 7), (int)((int)yUp - 28), (int)27, (int)32);
            }else if(this.mode.getValue() == EmojiMode.All) {
            	if(1+Math.random()*(10-1+1) == 1) {
            		RenderUtils.drawImage(new ResourceLocation("Worst/yaoer.png"), (int)((int)xLeft + 9), (int)((int)yUp - 20), (int)20, (int)25);
            	}else if(1+Math.random()*(10-1+1) == 2){
            		RenderUtils.drawImage(new ResourceLocation("Worst/ganga.png"), (int)((int)xLeft + 7), (int)((int)yUp - 28), (int)27, (int)32);
            	}else if(1+Math.random()*(10-1+1) == 3){
            		RenderUtils.drawImage(new ResourceLocation("Worst/taijun.png"), (int)((int)xLeft + 7), (int)((int)yUp - 28), (int)27, (int)32);
            	}
            }
            GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glNormal3f((float)1.0f, (float)1.0f, (float)1.0f);
            GL11.glPopMatrix();
        }
    }
    
    enum EmojiMode{
    	Yaoer,
    	Taijun,
    	Ganga,
    	All
    }
}
