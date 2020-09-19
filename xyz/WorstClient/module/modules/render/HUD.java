package xyz.WorstClient.module.modules.render;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import xyz.WorstClient.Client;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.rendering.EventRender2D;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.management.ModuleManager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.module.modules.combat.Killaura;
import xyz.WorstClient.ui.Notification;
import xyz.WorstClient.ui.fontRenderer.UnicodeFontRenderer;
import xyz.WorstClient.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import xyz.WorstClient.utils.TimerUtil;
import xyz.WorstClient.utils.render.RenderUtil;

public class HUD
  extends Module
{
  private static final String PotionEffect = null;
  private Numbers<Double> customrainbow = new Numbers("RainbowSpeed", "RainbowSpeed", Double.valueOf(0.5D), Double.valueOf(0.0D), Double.valueOf(1.0D), Double.valueOf(0.1D));
  public static Numbers<Double> red = new Numbers("Red", "Red", Double.valueOf(0.0D), Double.valueOf(0.0D), Double.valueOf(255.0D), Double.valueOf(1.0D));
  public static Numbers<Double> green = new Numbers("Green", "Green", Double.valueOf(125.0D), Double.valueOf(0.0D), Double.valueOf(255.0D), Double.valueOf(1.0D));
  public static Numbers<Double> blue = new Numbers("Blue", "Blue", Double.valueOf(255.0D), Double.valueOf(0.0D), Double.valueOf(255.0D), Double.valueOf(1.0D));
  public static Numbers<Double> bg = new Numbers("background", "background", Double.valueOf(50.0D), Double.valueOf(0.0D), Double.valueOf(254.0D), Double.valueOf(1.0D));
  private Option<Boolean> info = new Option("Information", "information", Boolean.valueOf(true));
  private Option<Boolean> drawPotion = new Option("Potion", "Potion", Boolean.valueOf(true));
  public static Option<Boolean> blur = new Option("blur", "blur", Boolean.valueOf(true));
  public static Mode<Enum> mode = new Mode("TitleMode", "TitleMode", HUDMode.values(), HUDMode.Blue);
  public static Mode<Enum> mode2 = new Mode("RectMode", "RectMode", RectMode.values(), RectMode.Remix);
  public static Mode<Enum> FontMod = new Mode("RectMode", "RectMode", Font.values(), Font.asu);
  public static Mode<Enum> ArrayList = new Mode("ArrayList", "ArrayList", ArrayListMode.values(), ArrayListMode.Rainbow);
  private TimerUtil time = new TimerUtil();
UnicodeFontRenderer KeyFont = Client.fontManager.googlesans17;
  public static boolean shouldMove;
  public static boolean useFont;
  private double anima;
  private int logoint;
  float hue;
  UnicodeFontRenderer  font = Client.fontManager.jigsaw18;

  
  public HUD() {
    super("HUD", new String[] { "gui" }, ModuleType.Render);
    setColor((new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255))).getRGB());
    addValues(new Value[] { blur,red, green, blue, this.customrainbow , this.bg,this.info,this.drawPotion,mode,this.mode2 ,this.FontMod , this.ArrayList});
  }
  ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
  @EventHandler
  private void renderHud(EventRender2D event) {
	  this.setSuffix(mode.getValue().name());
		
      final ScaledResolution sr = new ScaledResolution(mc);
      if (Killaura.curTarget != null) {
              GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
              FontRenderer font = mc.fontRendererObj;
              font.drawStringWithShadow(Killaura.curTarget.getName(), sr.getScaledWidth() / 2 - font.getStringWidth(Killaura.curTarget.getName()) / 2, sr.getScaledHeight() / 2 - 50, 16777215);
              mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/icons.png"));
              int i2 = 0;
              int i3 = 0;
              while ((float)i2 < (Killaura.curTarget).getMaxHealth() / 2.0f) {
              	mc.ingameGUI.drawTexturedModalRect((float)(sr.getScaledWidth() / 2) - (Killaura.curTarget).getMaxHealth() / 2.0f * 10.0f / 2.0f + (float)(i2 * 10), (float)(sr.getScaledHeight() / 2 - 20), 16, 0, 9, 9);
                  ++i2;
              }
              i2 = 0;
              while ((float)i2 < (Killaura.curTarget).getHealth() / 2.0f) {
              	mc.ingameGUI.drawTexturedModalRect((float)(sr.getScaledWidth() / 2) - (Killaura.curTarget).getMaxHealth() / 2.0f * 10.0f / 2.0f + (float)(i2 * 10), (float)(sr.getScaledHeight() / 2 - 20), 52, 0, 9, 9);
                  ++i2;
              }
              while ((float)i3 < 20 / 2.0f) {
              	mc.ingameGUI.drawTexturedModalRect((float)(sr.getScaledWidth() / 2) - (Killaura.curTarget).getMaxHealth() / 2.0f * 10.0f / 2.0f + (float)(i3 * 10), (float)(sr.getScaledHeight() / 2 - 30), 16, 9, 9, 9);
                  ++i3;
              }
              i3 = 0;
              while ((float)i3 < (Killaura.curTarget).getTotalArmorValue() / 2.0f) {
              	mc.ingameGUI.drawTexturedModalRect((float)(sr.getScaledWidth() / 2) - (Killaura.curTarget).getMaxHealth() / 2.0f * 10.0f / 2.0f + (float)(i3 * 10), (float)(sr.getScaledHeight() / 2 - 30), 34, 9, 9, 9);
                  ++i3;
              }

      }
	  UnicodeFontRenderer font2 = Client.fontManager.getFont(FontMod.getValue().name()+".ttf", 22);
	  if(FontMod.getValue()==Font.Jigsaw | FontMod.getValue()==Font.simpleton) {
		  font2 = Client.fontManager.getFont(FontMod.getValue().name()+".otf", 22);
	  }
	  
	  font = Client.fontManager.getFont(FontMod.getValue().name()+".ttf", 18);
	  if(FontMod.getValue()==Font.Jigsaw | FontMod.getValue()==Font.simpleton) {
		  font = Client.fontManager.getFont(FontMod.getValue().name()+".otf", 18);
	  }
	  
	  Client.instance.drawNotifications();
	  if (((Boolean)this.drawPotion.getValue()).booleanValue()) {
		  	this.drawPotionStatus(new ScaledResolution(this.mc));
	  }
	  if (!mc.gameSettings.showDebugInfo) {
      ArrayList<Module> sorted = new ArrayList<Module>();
      Client.instance.getModuleManager();
      for (Module m : ModuleManager.getModules()) {
        if (!m.isEnabled() || m.wasRemoved())
          continue;  sorted.add(m);
      } 
      sorted.sort((o1, o2) -> font.getStringWidth(o2.getSuffix().isEmpty() ? o2.getName() : String.format("%s%s", new Object[] { o2.getName(), o2.getSuffix() })) - font.getStringWidth(o1.getSuffix().isEmpty() ? o1.getName() : String.format("%s%s", new Object[] { o1.getName(), o1.getSuffix() })));
      int y = 1;
      int rainbowTick = 0;
      int countMod = 0;
      Color customrainbow = new Color(Color.HSBtoRGB((float)(Minecraft.thePlayer.ticksExisted / 50.0D + Math.sin(rainbowTick / 50.0D * 1.6D)) % 1.0F, ((Double)this.customrainbow.getValue()).floatValue(), 1.0F));
      String time = (new SimpleDateFormat("HH:mm:ss")).format(new Date());
      UnicodeFontRenderer LogoFont = Client.fontManager.comfortaa24;
            
      
      if (mode.getValue() == HUDMode.Title) {
      	String Fname = Display.getTitle().substring(0,1);
      	String Bname = Display.getTitle().substring(1);
          final StringBuilder append5 = new StringBuilder(Fname);
          Client.instance.getClass();
              LogoFont.drawStringWithShadow(append5.toString(), (float)7, (float)5.9, (new Color(((Double)red.getValue()).intValue(), ((Double)green.getValue()).intValue(), ((Double)blue.getValue()).intValue())).getRGB());
              //LogoFont.drawStringWithShadow(Bname, (float) ((float)LogoFont.getStringWidth(append5.toString()) + 8.5), (float)6, new Color(255, 255, 255).getRGB());
          }
      if(this.mode.getValue()==HUDMode.Shadow) {
    LogoFont.drawStringWithShadow(Client.name, 4, (float)3, (new Color(255,255,255)).getRGB());
//    LogoFont.drawStringWithShadow("orst", LogoFont.getStringWidth("W")+5, (float)3, new Color(255,255,255).getRGB());
      }       
      if (mode.getValue() == HUDMode.Blue) {
    	    LogoFont.drawStringWithShadow(Client.name, 4, (float)3,  new Color(5,5,255).getRGB());
//    	    LogoFont.drawStringWithShadow("orst", LogoFont.getStringWidth("W")+5, (float)3, new Color(255,255,255).getRGB());
      }
      if(mode.getValue() == HUDMode.Red) {
    	    LogoFont.drawStringWithShadow(Client.name, 4, (float)3, new Color(255,55,55).getRGB());
//   	    LogoFont.drawStringWithShadow("orst", LogoFont.getStringWidth("W")+5, (float)3, new Color(255,255,255).getRGB());
      	}
      if(mode.getValue() == HUDMode.Green) {
    	    LogoFont.drawStringWithShadow(Client.name, 4, (float)3, new Color(55,255,55).getRGB());
//    	    LogoFont.drawStringWithShadow("orst", LogoFont.getStringWidth("W")+5, (float)3, new Color(255,255,255).getRGB());
        	}
      if(mode.getValue() == HUDMode.White) {
    	    LogoFont.drawStringWithShadow(Client.name, 4, (float)3, (new Color(255,255,255).getRGB()));
//    	    LogoFont.drawStringWithShadow("orst", LogoFont.getStringWidth("W")+5, (float)3, new Color(255,255,255).getRGB());
        	}

      
      if (((Boolean)this.info.getValue()).booleanValue()) {
    	  ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
    	  if(mc.ingameGUI.getChatGUI().getChatOpen() == false) {
        String FPS = " FPS:" + Minecraft.debugFPS + "";
        int ychat = mc.ingameGUI.getChatGUI().getChatOpen() ? 24 : 10, n = ychat;
        font2.drawStringWithShadow(FPS, 5.0F, (RenderUtil.height() - mc.fontRendererObj.FONT_HEIGHT - 8), (new Color(255, 255, 255)).getRGB());
        font2.drawStringWithShadow("Ping: " + mc.getNetHandler().getPlayerInfo(Minecraft.thePlayer.getUniqueID()).getResponseTime() + "ms", 80.0F, (RenderUtil.height()-18), (new Color(255, 255, 255)).getRGB());
        String date = (new SimpleDateFormat("HH:mm:ss")).format(new Date());
    	  }
    	  font2.drawStringWithShadow(Client.name+" "+Client.version+"-ChaoJiG", (float)res.getScaledWidth_double()-font2.getStringWidth("Worst  "+Client.version+"-SuperSkidder"), (RenderUtil.height()-16), (new Color(255, 255, 255)).getRGB());
      } 
      
     
      
      for (Module m : sorted) {
        float x = (RenderUtil.width() - font.getStringWidth(String.valueOf(m.getName()) + m.getSuffix()+1));
        if(mode2.getValue() == RectMode.None)  {
            if(ArrayList.getValue() == ArrayListMode.Rainbow)  {
                customrainbow = new Color(Color.HSBtoRGB((float)(Minecraft.thePlayer.ticksExisted / 50.0D + Math.sin(rainbowTick / 50.0D * 1.6D)) % 1.0F, ((Double)this.customrainbow.getValue()).floatValue(), 1.0F));
            } 
            if(ArrayList.getValue() == ArrayListMode.BlueRainbow)  {
              final int rainbowCol = Gui.rainbow(System.nanoTime() * 5L, (float)countMod, 0.5f).getRGB();
              ++countMod;
              final int c = rainbowCol;
              final Color col = new Color(c);
              final Color Ranbow = new Color(0.0f, col.getGreen()/ 255.0f, 1.0f);
              customrainbow = Ranbow;
            } 
            if(ArrayList.getValue() == ArrayListMode.GreenRainbow)  {
                final int rainbowCol = Gui.rainbow(System.nanoTime() * 5L, (float)countMod, 0.5f).getRGB();
                ++countMod;
                final int c = rainbowCol;
                final Color col = new Color(c);
                final Color Ranbow = new Color(0.0f, 1.0f, col.getBlue()/ 255.0f);
                customrainbow = Ranbow;
              } 
            if(ArrayList.getValue() == ArrayListMode.RadRainbow)  {
                final int rainbowCol = Gui.rainbow(System.nanoTime() * 1L, (float)countMod, 0.5f).getRGB();
                ++countMod;
                final int c = rainbowCol;
                final Color col = new Color(c);
                final Color Ranbow = new Color(col.getRed()/150f, 25/ 255.0f, 25/ 255.0f);
                customrainbow = Ranbow;
          	  
              } 
            if(ArrayList.getValue() == ArrayListMode.WhiteRainbow)  {
                final int rainbowCol = Gui.rainbow(System.nanoTime() * 5L, (float)countMod, 1.0f).getRGB();
                ++countMod;
                final int c = rainbowCol;
                final Color col = new Color(c);
                final Color Ranbow = new Color(col.getRed()/ 255.0f, col.getRed()/ 255.0f, col.getRed()/ 255.0f);
                customrainbow = Ranbow;
              } 
            if(ArrayList.getValue() == ArrayListMode.CustomColor)  {
                customrainbow = new Color(red.getValue().intValue(),green.getValue().intValue(),blue.getValue().intValue());
              } 
            
          //Gui.drawRect(RenderUtil.width(), (y - 3), (x - 3.0F), (y + 11), (new Color(0, 0, 0, ((Double)this.bg.getValue()).intValue())).getRGB());
          font.drawString(m.getName(), x - 1.0F, (y - 1), customrainbow.getRGB());
          font.drawString(m.getSuffix(), (RenderUtil.width() - font.getStringWidth(m.getSuffix()) - 2), (y - 1), (new Color(180, 180, 180)).getRGB());
        } 
        
        
        if(mode2.getValue() == RectMode.Normal)  {
            if(ArrayList.getValue() == ArrayListMode.Rainbow)  {
                customrainbow = new Color(Color.HSBtoRGB((float)(Minecraft.thePlayer.ticksExisted / 50.0D + Math.sin(rainbowTick / 50.0D * 1.6D)) % 1.0F, ((Double)this.customrainbow.getValue()).floatValue(), 1.0F));
            } 
            if(ArrayList.getValue() == ArrayListMode.BlueRainbow)  {
              final int rainbowCol = Gui.rainbow(System.nanoTime() * 5L, (float)countMod, 0.5f).getRGB();
              ++countMod;
              final int c = rainbowCol;
              final Color col = new Color(c);
              final Color Ranbow = new Color(0.0f, col.getGreen()/ 255.0f, 1.0f);
              customrainbow = Ranbow;
            } 
            if(ArrayList.getValue() == ArrayListMode.GreenRainbow)  {
                final int rainbowCol = Gui.rainbow(System.nanoTime() * 5L, (float)countMod, 0.5f).getRGB();
                ++countMod;
                final int c = rainbowCol;
                final Color col = new Color(c);
                final Color Ranbow = new Color(0.0f, 1.0f, col.getBlue()/ 255.0f);
                customrainbow = Ranbow;
              } 
            if(ArrayList.getValue() == ArrayListMode.RadRainbow)  {
                final int rainbowCol = Gui.rainbow(System.nanoTime() * 1L, (float)countMod, 0.5f).getRGB();
                ++countMod;
                final int c = rainbowCol;
                final Color col = new Color(c);
                final Color Ranbow = new Color(col.getRed()/150f, 25/ 255.0f, 25/ 255.0f);
                customrainbow = Ranbow;
          	  
              } 
            if(ArrayList.getValue() == ArrayListMode.WhiteRainbow)  {
                final int rainbowCol = Gui.rainbow(System.nanoTime() * 5L, (float)countMod, 1.0f).getRGB();
                ++countMod;
                final int c = rainbowCol;
                final Color col = new Color(c);
                final Color Ranbow = new Color(col.getRed()/ 255.0f, col.getRed()/ 255.0f, col.getRed()/ 255.0f);
                customrainbow = Ranbow;
              } 
            if(ArrayList.getValue() == ArrayListMode.CustomColor)  {
                customrainbow = new Color(red.getValue().intValue(),green.getValue().intValue(),blue.getValue().intValue());
              } 
            
          Gui.drawRect(RenderUtil.width(), (y - 3), (x - 3.0F), (y + 11), (new Color(0, 0, 0, ((Double)this.bg.getValue()).intValue())).getRGB());
          font.drawStringWithShadow(m.getName(), x - 1.0F, (y - 1), customrainbow.getRGB());
          font.drawStringWithShadow(m.getSuffix(), (RenderUtil.width() - font.getStringWidth(m.getSuffix()) - 2), (y - 1), (new Color(180, 180, 180)).getRGB());
        } 
        
        if(mode2.getValue() == RectMode.right)  {
            if(ArrayList.getValue() == ArrayListMode.Rainbow)  {
                customrainbow = new Color(Color.HSBtoRGB((float)(Minecraft.thePlayer.ticksExisted / 50.0D + Math.sin(rainbowTick / 50.0D * 1.6D)) % 1.0F, ((Double)this.customrainbow.getValue()).floatValue(), 1.0F));
            } 
            if(ArrayList.getValue() == ArrayListMode.BlueRainbow)  {
              final int rainbowCol = Gui.rainbow(System.nanoTime() * 5L, (float)countMod, 0.5f).getRGB();
              ++countMod;
              final int c = rainbowCol;
              final Color col = new Color(c);
              final Color Ranbow = new Color(0.0f, col.getGreen()/ 255.0f, 1.0f);
              customrainbow = Ranbow;
            } 
            if(ArrayList.getValue() == ArrayListMode.GreenRainbow)  {
                final int rainbowCol = Gui.rainbow(System.nanoTime() * 5L, (float)countMod, 0.5f).getRGB();
                ++countMod;
                final int c = rainbowCol;
                final Color col = new Color(c);
                final Color Ranbow = new Color(0.0f, 1.0f, col.getBlue()/ 255.0f);
                customrainbow = Ranbow;
              } 
            if(ArrayList.getValue() == ArrayListMode.RadRainbow)  {
                final int rainbowCol = Gui.rainbow(System.nanoTime() * 1L, (float)countMod, 0.5f).getRGB();
                ++countMod;
                final int c = rainbowCol;
                final Color col = new Color(c);
                final Color Ranbow = new Color(col.getRed()/150f, 25/ 255.0f, 25/ 255.0f);
                customrainbow = Ranbow;
          	  
              } 
            if(ArrayList.getValue() == ArrayListMode.WhiteRainbow)  {
                final int rainbowCol = Gui.rainbow(System.nanoTime() * 5L, (float)countMod, 1.0f).getRGB();
                ++countMod;
                final int c = rainbowCol;
                final Color col = new Color(c);
                final Color Ranbow = new Color(col.getRed()/ 255.0f, col.getRed()/ 255.0f, col.getRed()/ 255.0f);
                customrainbow = Ranbow;
              } 
            if(ArrayList.getValue() == ArrayListMode.CustomColor)  {
                customrainbow = new Color(red.getValue().intValue(),green.getValue().intValue(),blue.getValue().intValue());
              } 
            
          Gui.drawRect((x - 1.0F), (y - 3), (x - 3.0F), (y + 11), (new Color(((Double)red.getValue()).intValue(), ((Double)green.getValue()).intValue(), ((Double)blue.getValue()).intValue())).getRGB());
          Gui.drawRect(RenderUtil.width(), (y -3), (x - 1.0F), (y + 11), (new Color(0, 0, 0, ((Double)this.bg.getValue()).intValue())).getRGB());
          font.drawStringWithShadow(m.getName(), x +1.0F, (y - 1), customrainbow.getRGB());
          font.drawStringWithShadow(m.getSuffix(), (RenderUtil.width() - font.getStringWidth(m.getSuffix()) - 2), (y - 1), (new Color(180, 180, 180)).getRGB());
        } 
        if(mode2.getValue() == RectMode.left)  {
            if(ArrayList.getValue() == ArrayListMode.Rainbow)  {
                customrainbow = new Color(Color.HSBtoRGB((float)(Minecraft.thePlayer.ticksExisted / 50.0D + Math.sin(rainbowTick / 50.0D * 1.6D)) % 1.0F, ((Double)this.customrainbow.getValue()).floatValue(), 1.0F));
            } 
            if(ArrayList.getValue() == ArrayListMode.BlueRainbow)  {
              final int rainbowCol = Gui.rainbow(System.nanoTime() * 5L, (float)countMod, 0.5f).getRGB();
              ++countMod;
              final int c = rainbowCol;
              final Color col = new Color(c);
              final Color Ranbow = new Color(0.0f, col.getGreen()/ 255.0f, 1.0f);
              customrainbow = Ranbow;
            } 
            if(ArrayList.getValue() == ArrayListMode.GreenRainbow)  {
                final int rainbowCol = Gui.rainbow(System.nanoTime() * 5L, (float)countMod, 0.5f).getRGB();
                ++countMod;
                final int c = rainbowCol;
                final Color col = new Color(c);
                final Color Ranbow = new Color(0.0f, 1.0f, col.getBlue()/ 255.0f);
                customrainbow = Ranbow;
              } 
            if(ArrayList.getValue() == ArrayListMode.RadRainbow)  {
                final int rainbowCol = Gui.rainbow(System.nanoTime() * 1L, (float)countMod, 0.5f).getRGB();
                ++countMod;
                final int c = rainbowCol;
                final Color col = new Color(c);
                final Color Ranbow = new Color(col.getRed()/150f, 25/ 255.0f, 25/ 255.0f);
                customrainbow = Ranbow;
          	  
              } 
            if(ArrayList.getValue() == ArrayListMode.WhiteRainbow)  {
                final int rainbowCol = Gui.rainbow(System.nanoTime() * 5L, (float)countMod, 1.0f).getRGB();
                ++countMod;
                final int c = rainbowCol;
                final Color col = new Color(c);
                final Color Ranbow = new Color(col.getRed()/ 255.0f, col.getRed()/ 255.0f, col.getRed()/ 255.0f);
                customrainbow = Ranbow;
              } 
            if(ArrayList.getValue() == ArrayListMode.CustomColor)  {
                customrainbow = new Color(red.getValue().intValue(),green.getValue().intValue(),blue.getValue().intValue());
              } 
            
          Gui.drawRect(RenderUtil.width(), (y - 3), (x - 3.0F), (y + 11), (new Color(0, 0, 0, ((Double)this.bg.getValue()).intValue())).getRGB());
          font.drawStringWithShadow(m.getName(), x -1.0F, (y - 1), customrainbow.getRGB());
          font.drawStringWithShadow( m.getSuffix(), (RenderUtil.width() - font.getStringWidth(m.getSuffix()) - 3), (y - 1), (new Color(180, 180, 180)).getRGB());
          Gui.drawRect(RenderUtil.width(), (y - 3), (RenderUtil.width() - 2), (y + 11), (new Color(((Double)red.getValue()).intValue(), ((Double)green.getValue()).intValue(), ((Double)blue.getValue()).intValue())).getRGB());
        } 
        if(mode2.getValue() == RectMode.Remix)  {
            if(ArrayList.getValue() == ArrayListMode.Rainbow)  {
                customrainbow = new Color(Color.HSBtoRGB((float)(Minecraft.thePlayer.ticksExisted / 50.0D + Math.sin(rainbowTick / 50.0D * 1.6D)) % 1.0F, ((Double)this.customrainbow.getValue()).floatValue(), 1.0F));
            } 
            if(ArrayList.getValue() == ArrayListMode.BlueRainbow)  {
              final int rainbowCol = Gui.rainbow(System.nanoTime() * 5L, (float)countMod, 0.5f).getRGB();
              ++countMod;
              final int c = rainbowCol;
              final Color col = new Color(c);
              final Color Ranbow = new Color(0.0f, col.getGreen()/ 255.0f, 1.0f);
              customrainbow = Ranbow;
            } 
            if(ArrayList.getValue() == ArrayListMode.GreenRainbow)  {
                final int rainbowCol = Gui.rainbow(System.nanoTime() * 5L, (float)countMod, 0.5f).getRGB();
                ++countMod;
                final int c = rainbowCol;
                final Color col = new Color(c);
                final Color Ranbow = new Color(0.0f, 1.0f, col.getBlue()/ 255.0f);
                customrainbow = Ranbow;
              } 
            if(ArrayList.getValue() == ArrayListMode.RadRainbow)  {
                final int rainbowCol = Gui.rainbow(System.nanoTime() * 1L, (float)countMod, 0.5f).getRGB();
                ++countMod;
                final int c = rainbowCol;
                final Color col = new Color(c);
                final Color Ranbow = new Color(col.getRed()/150f, 25/ 255.0f, 25/ 255.0f);
                customrainbow = Ranbow;
          	  
              } 
            if(ArrayList.getValue() == ArrayListMode.WhiteRainbow)  {
                final int rainbowCol = Gui.rainbow(System.nanoTime() * 5L, (float)countMod, 1.0f).getRGB();
                ++countMod;
                final int c = rainbowCol;
                final Color col = new Color(c);
                final Color Ranbow = new Color(col.getRed()/ 255.0f, col.getRed()/ 255.0f, col.getRed()/ 255.0f);
                customrainbow = Ranbow;
              } 
            if(ArrayList.getValue() == ArrayListMode.CustomColor)  {
                customrainbow = new Color(red.getValue().intValue(),green.getValue().intValue(),blue.getValue().intValue());
              } 
            
            Gui.drawRect(RenderUtil.width(), (y -3), (x - 4.0F), (y + 11), (new Color(0, 0, 0, ((Double)this.bg.getValue()).intValue())).getRGB());
            Gui.drawRect((x - 1.0F), (y ), (x - 2.0F), (y + 9), (new Color(((Double)red.getValue()).intValue(), ((Double)green.getValue()).intValue(), ((Double)blue.getValue()).intValue())).getRGB());
            font.drawStringWithShadow(m.getName(), x +1.0F, (y - 1),customrainbow.getRGB());
            font.drawStringWithShadow( m.getSuffix(), (RenderUtil.width() - font.getStringWidth(m.getSuffix()) - 3), (y - 1), (new Color(180, 180, 180)).getRGB());
          } 
        if(mode2.getValue() == RectMode.Remix2)  {
            if(ArrayList.getValue() == ArrayListMode.Rainbow)  {
                customrainbow = new Color(Color.HSBtoRGB((float)(Minecraft.thePlayer.ticksExisted / 50.0D + Math.sin(rainbowTick / 50.0D * 1.6D)) % 1.0F, ((Double)this.customrainbow.getValue()).floatValue(), 1.0F));
            } 
            if(ArrayList.getValue() == ArrayListMode.BlueRainbow)  {
              final int rainbowCol = Gui.rainbow(System.nanoTime() * 5L, (float)countMod, 0.5f).getRGB();
              ++countMod;
              final int c = rainbowCol;
              final Color col = new Color(c);
              final Color Ranbow = new Color(0.0f, col.getGreen()/ 255.0f, 1.0f);
              customrainbow = Ranbow;
            } 
            if(ArrayList.getValue() == ArrayListMode.GreenRainbow)  {
                final int rainbowCol = Gui.rainbow(System.nanoTime() * 5L, (float)countMod, 0.5f).getRGB();
                ++countMod;
                final int c = rainbowCol;
                final Color col = new Color(c);
                final Color Ranbow = new Color(0.0f, 1.0f, col.getBlue()/ 255.0f);
                customrainbow = Ranbow;
              } 
            if(ArrayList.getValue() == ArrayListMode.RadRainbow)  {
                final int rainbowCol = Gui.rainbow(System.nanoTime() * 1L, (float)countMod, 0.5f).getRGB();
                ++countMod;
                final int c = rainbowCol;
                final Color col = new Color(c);
                final Color Ranbow = new Color(col.getRed()/100f, 25/ 255.0f, 25/ 255.0f);
                customrainbow = Ranbow;
          	  
              } 
            if(ArrayList.getValue() == ArrayListMode.WhiteRainbow)  {
                final int rainbowCol = Gui.rainbow(System.nanoTime() * 5L, (float)countMod, 1.0f).getRGB();
                ++countMod;
                final int c = rainbowCol;
                final Color col = new Color(c);
                final Color Ranbow = new Color(col.getRed()/ 255.0f, col.getRed()/ 255.0f, col.getRed()/ 255.0f);
                customrainbow = Ranbow;
              } 
            if(ArrayList.getValue() == ArrayListMode.CustomColor)  {
                customrainbow = new Color(red.getValue().intValue(),green.getValue().intValue(),blue.getValue().intValue());
              } 
            
            Gui.drawRect(RenderUtil.width(), y+11,x-4, y+13,(new Color(((Double)red.getValue()).intValue(), ((Double)green.getValue()).intValue(), ((Double)blue.getValue()).intValue())).getRGB());
            Gui.drawRect(RenderUtil.width(), (y -3), (x - 4.0F), (y + 11), (new Color(0, 0, 0,255).getRGB()));
            Gui.drawRect((x - 3.0F), (y - 3), (x - 4.0F), (y + 11), (new Color(((Double)red.getValue()).intValue(), ((Double)green.getValue()).intValue(), ((Double)blue.getValue()).intValue())).getRGB());
            font.drawStringWithShadow(m.getName(), x +1.0F, (y - 1), customrainbow.getRGB());
            font.drawStringWithShadow( m.getSuffix(), (RenderUtil.width() - font.getStringWidth(m.getSuffix()) - 3), (y - 1), (new Color(180, 180, 180)).getRGB());
          } 
       
        
        y += mc.fontRendererObj.FONT_HEIGHT + 5;
        if (++rainbowTick > 50)
          rainbowTick = 0; 
      }
    }

    } 




  private void drawPotionStatus(ScaledResolution sr) {
      int y = 0;
      for (PotionEffect effect : this.mc.thePlayer.getActivePotionEffects()) {
          int ychat;
          Potion potion = Potion.potionTypes[effect.getPotionID()];
          String PType = I18n.format(potion.getName(), new Object[0]);
          switch (effect.getAmplifier()) {
              case 1: {
                  PType = String.valueOf(PType) + " II";
                  break;
              }
              case 2: {
                  PType = String.valueOf(PType) + " III";
                  break;
              }
              case 3: {
                  PType = String.valueOf(PType) + " IV";
                  break;
              }
          }
          int n = ychat = this.mc.ingameGUI.getChatGUI().getChatOpen() ? 10 : -5;
          Gui.drawRect(sr.getScaledWidth()-80, sr.getScaledHeight() - 52- font.FONT_HEIGHT + y - ychat, sr.getScaledWidth()-5, sr.getScaledHeight()- font.FONT_HEIGHT + y - ychat-28, (new Color(0, 0, 0, ((Double)this.bg.getValue()).intValue())).getRGB());
          font.drawStringWithShadow(PType, sr.getScaledWidth()-80, sr.getScaledHeight() - 27- font.FONT_HEIGHT + y - ychat-26, (new Color(((Double)red.getValue()).intValue(), ((Double)green.getValue()).intValue(), ((Double)blue.getValue()).intValue())).getRGB());             
          Client.fontManager.verdana16.drawStringWithShadow(Potion.getDurationString(effect), sr.getScaledWidth() -80, sr.getScaledHeight() - 24- font.FONT_HEIGHT + y - ychat-15, new Color(255,255,255).getRGB());
          y -= 25;    
          } 
      }
  }

  enum RectMode {
	  	None,
	    left,
	    right,
	    Remix,
	    Remix2,
	    Normal
  }
  
  enum ArrayListMode{
	  Rainbow,
	  RadRainbow,
	  GreenRainbow,
	  BlueRainbow,
	  WhiteRainbow,
	  CustomColor
  }
  
  
  enum Font{
	  asu,
	  comfortaa,
	  consolasbold,
	  googlesans,
	  Jigsaw,
	  Sigma,
	  simpleton,
	  tahoma,
	  verdana
	  
  }
  
  enum HUDMode {
    White,
    Red,
    Green,
    Blue,
    Shadow,
    Title,
    NONE;
  }


