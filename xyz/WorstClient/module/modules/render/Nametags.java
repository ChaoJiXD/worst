package xyz.WorstClient.module.modules.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import xyz.WorstClient.Client;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.rendering.EventRender3D;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.module.modules.combat.AntiBot;
import xyz.WorstClient.module.modules.player.Teams;
import xyz.WorstClient.utils.ClientUtil;
import xyz.WorstClient.utils.RenderUtils;

public class Nametags extends Module {
    private Numbers<Double> scale = new Numbers<Double>("Scale", "scale", 3.0, 1.0, 5.0, 0.1);
    private Option<Boolean> drawarror = new Option("drawarror", "drawarror", Boolean.valueOf(true));
	
    public Nametags() {
        super("NameTags", new String[]{"tags"}, ModuleType.World);
        this.setColor(new Color(29, 187, 102).getRGB());
        this.addValues(this.scale);
    }

    @EventHandler
    public void onRender(EventRender3D e) {
    for (Object o : this.mc.theWorld.playerEntities) {
			EntityPlayer p = (EntityPlayer) o;
			if(p != mc.thePlayer) {
				double pX = p.lastTickPosX + (p.posX - p.lastTickPosX) * mc.timer.renderPartialTicks
						- mc.getRenderManager().renderPosX;
				double pY = p.lastTickPosY + (p.posY - p.lastTickPosY) * mc.timer.renderPartialTicks
						- mc.getRenderManager().renderPosY;
				double pZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * mc.timer.renderPartialTicks
						- mc.getRenderManager().renderPosZ;
				renderNameTag(p, String.valueOf(p.getDisplayName()) , pX, pY, pZ);
			}
		}
   }

  public void renderNameTag(EntityPlayer entity, String tag, double pX, double pY, double pZ) {
      FontRenderer fr = super.mc.fontRendererObj;
      float var10 = mc.thePlayer.getDistanceToEntity(entity) / 6.0F;
      if(var10 < 0.8F) {
         var10 = 0.8F;
      }
      pY += entity.isSneaking()?0.5D:0.7D;
      float var11 = (float) (var10 * this.scale.getValue().doubleValue());
      var11 /= 100.0F;
      tag = entity.getName();
      String var12 = "";
		AntiBot ab = (AntiBot) Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
      if(ab.isServerBot(entity)) { //AntiBot
          var12 = "¡ìc[BOT]¡ì7";
      } else {
         var12 = "";
      }
      String var13 = "";
      if(!Teams.isOnSameTeam(entity)) { //Teams ºÍ ClientFriend
         var13 = "";
      } else {
         var13 = "¡ìb[TEAM]";
      }

      if((var13 + var12).equals("")) {
         var13 = "";
      }

      String var14 = var13 + var12 + tag;
      GL11.glPushMatrix();
      GL11.glTranslatef((float)pX, (float)pY + 1.4F, (float)pZ);
      GL11.glNormal3f(0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-RenderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(RenderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      GL11.glScalef(-var11, -var11, var11);
      setGLCap(2896, false);
      setGLCap(2929, false);
      int var16 = super.mc.fontRendererObj.getStringWidth(var14) / 2;
      setGLCap(3042, true);
      GL11.glBlendFunc(770, 771);
      RenderUtils.drawRect((float)(-var16 - 2), (float)(-(super.mc.fontRendererObj.FONT_HEIGHT + 3)), (float)(var16 + 2), 2.0F, ClientUtil.reAlpha(Color.BLACK.getRGB(), 0.3F));
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
      fr.drawString(var14, -var16, -(super.mc.fontRendererObj.FONT_HEIGHT ), -1);
      //Client.fontManager.verdana16.drawString(var15, -super.mc.fontRendererObj.getStringWidth(var15) / 2, -(super.mc.fontRendererObj.FONT_HEIGHT-1), -1);
      /*
      int var17 = new Color(225,200,150).getRGB();
      if(entity.getHealth()<15) {
    	  var17 = new Color(225,255,0).getRGB();
      }else if(entity.getHealth()<10) {
    	  var17 = new Color(225,0,0).getRGB();
      }else if(entity.getHealth()>15) {
    	  var17 = new Color(150,255,50).getRGB();
      }
*/
      int var17 = new Color(225,255,255).getRGB();
      
      if(entity.getHealth() > 20.0F) {
         var17 = new Color(250,155,0).getRGB();
      }

      float var18 = (float)Math.ceil((double)(entity.getHealth() + entity.getAbsorptionAmount()));
      float var19 = var18 / (entity.getMaxHealth() + entity.getAbsorptionAmount());
      Gui.drawRect((float)var16 + var19 * 40.0F - 40.0F + 2.0F, 2.0F, (float)(-var16) - 1.98F, 0.9F, var17);
      GL11.glPushMatrix();
      int var20 = 0;
      ItemStack[] var24 = entity.inventory.armorInventory;
      int var23 = entity.inventory.armorInventory.length;

      ItemStack var21;
      for(int var22 = 0; var22 < var23; ++var22) {
         var21 = var24[var22];
         if(var21 != null) {
            var20 -= 11;
         }
      }

      if(entity.getHeldItem() != null) {
         var20 -= 8;
         var21 = entity.getHeldItem().copy();
         if(((ItemStack)var21).hasEffect() && (((ItemStack)var21).getItem() instanceof ItemTool || ((ItemStack)var21).getItem() instanceof ItemArmor)) {
            ((ItemStack)var21).stackSize = 1;
         }


         var20 += 20;
      }

      ItemStack[] var25 = entity.inventory.armorInventory;
      int var28 = entity.inventory.armorInventory.length;

      for(var23 = 0; var23 < var28; ++var23) {
         ItemStack var27 = var25[var23];
      }
      GL11.glPopMatrix();
      revertAllCaps();
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }

  public static void revertAllCaps()
  {
    for (Iterator localIterator = glCapMap.keySet().iterator(); localIterator.hasNext();)
    {
      int cap = ((Integer)localIterator.next()).intValue();
      revertGLCap(cap);
    }
  }

  public static void revertGLCap(int cap)
  {
    Boolean origCap = (Boolean)glCapMap.get(Integer.valueOf(cap));
    if (origCap != null) {
      if (origCap.booleanValue()) {
        GL11.glEnable(cap);
      } else {
        GL11.glDisable(cap);
      }
    }
  }

public void renderItemStack(ItemStack var1, int var2, int var3) {
      GL11.glPushMatrix();
      GL11.glDepthMask(true);
      GlStateManager.clear(256);
      RenderHelper.enableStandardItemLighting();
      super.mc.getRenderItem().zLevel = -150.0F;
      whatTheFuckOpenGLThisFixesItemGlint();
      super.mc.getRenderItem().renderItemAndEffectIntoGUI(var1, var2, var3);
      super.mc.getRenderItem().renderItemOverlays(super.mc.fontRendererObj, var1, var2, var3);
      super.mc.getRenderItem().zLevel = 0.0F;
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableCull();
      GlStateManager.enableAlpha();
      GlStateManager.disableBlend();
      GlStateManager.disableLighting();
      GlStateManager.scale(0.5D, 0.5D, 0.5D);
      GlStateManager.disableDepth();
      GlStateManager.enableDepth();
      GlStateManager.scale(2.0F, 2.0F, 2.0F);
      GL11.glPopMatrix();
   }

   public void whatTheFuckOpenGLThisFixesItemGlint() {
      GlStateManager.disableLighting();
      GlStateManager.disableDepth();
      GlStateManager.disableBlend();
      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
      GlStateManager.disableLighting();
      GlStateManager.disableDepth();
      GlStateManager.disableTexture2D();
      GlStateManager.disableAlpha();
      GlStateManager.disableBlend();
      GlStateManager.enableBlend();
      GlStateManager.enableAlpha();
      GlStateManager.enableTexture2D();
      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
   }
	
   private static Map<Integer, Boolean> glCapMap = new HashMap();
   
	public static void setGLCap(int cap, boolean flag)
	  {
	    glCapMap.put(Integer.valueOf(cap), Boolean.valueOf(GL11.glGetBoolean(cap)));
	    if (flag) {
	      GL11.glEnable(cap);
	    } else {
	      GL11.glDisable(cap);
	    }
	  }
	
   public void drawBorderedRectNameTag(float var1, float var2, float var3, float var4, float var5, int var6, int var7) {
      Gui.drawRect(var1, var2, var3, var4, var7);
      float var8 = (float)(var6 >> 24 & 255) / 255.0F;
      float var9 = (float)(var6 >> 16 & 255) / 255.0F;
      float var10 = (float)(var6 >> 8 & 255) / 255.0F;
      float var11 = (float)(var6 & 255) / 255.0F;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glEnable(2848);
      GL11.glPushMatrix();
      GL11.glColor4f(var9, var10, var11, var8);
      GL11.glLineWidth(var5);
      GL11.glBegin(1);
      GL11.glVertex2d((double)var1, (double)var2);
      GL11.glVertex2d((double)var1, (double)var4);
      GL11.glVertex2d((double)var3, (double)var4);
      GL11.glVertex2d((double)var3, (double)var2);
      GL11.glVertex2d((double)var1, (double)var2);
      GL11.glVertex2d((double)var3, (double)var2);
      GL11.glVertex2d((double)var1, (double)var4);
      GL11.glVertex2d((double)var3, (double)var4);
      GL11.glEnd();
      GL11.glPopMatrix();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glDisable(2848);
   }
}
