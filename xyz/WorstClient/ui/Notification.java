package xyz.WorstClient.ui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import xyz.WorstClient.Client;
import xyz.WorstClient.module.modules.render.setColor;
import xyz.WorstClient.ui.fontRenderer.UnicodeFontRenderer;
import xyz.WorstClient.utils.ClientUtil;
import xyz.WorstClient.utils.RenderUtils;
import xyz.WorstClient.utils.StringUtils;
import xyz.WorstClient.utils.TimeHelper;
import xyz.WorstClient.utils.render.Colors;
import xyz.WorstClient.utils.render.RenderUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;

public class Notification
{
    private String message;
    private TimeHelper timer;
    private double lastY;
    private double posY;
    private double width;
    private double height;
    private double animationX;
    private int color;
    private int imageWidth;
    private ResourceLocation image;
    private long stayTime;
    private Type Type;
    Minecraft mc = Minecraft.getMinecraft();
    
    public Notification(final String message, final Type type) {
        if(Minecraft.theWorld!=null) {
     	   mc.thePlayer.playSound("random.click", 0.3f, 0.5f);
        }
    	Type = type;
        this.message = message;
        (this.timer = new TimeHelper()).reset();
        final UnicodeFontRenderer font = Client.fontManager.wqy16;
        this.width = font.getStringWidth(message)+5;
        this.height = 18.0;
        this.animationX = this.width;
        this.stayTime = 1800L;        
        this.posY = 0.0;
       
        this.color = ClientUtil.reAlpha(Colors.BLUE.c, 1.0F);
        }
        
    public void draw(final double getY, final double lastY) {
        this.lastY = lastY;
        this.animationX = RenderUtil.getAnimationState(this.animationX, this.isFinished() ? this.width : 0.0, Math.max(this.isFinished() ? 400 : 50, this.isFinished() ? 220 : 180.0));
        if (this.posY == 0.0) {
            this.posY = getY;
        }
        else {
            this.posY = RenderUtil.getAnimationState(this.posY, getY, 90.0);
        }
        final ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        final int x1 = (int)(res.getScaledWidth() - this.width + this.animationX);
        final int x2 = (int)(res.getScaledWidth() + this.animationX);
        final int y1 = (int)this.posY;
        final int y2 = (int)(y1 + this.height);
        Gui.drawRect(x1-25, y1-3, x2, y2-3, new Color(255,255,255,254).getRGB());
        final UnicodeFontRenderer font = Client.fontManager.wqy16;
        if(Type == Type.SUCCESS) {
        if(StringUtils.getSubString(this.message, "", " Enabled") != StringUtils.getSubString("123", "SD", "NMSL")) {
        Client.fontManager.wqy16.drawString(StringUtils.getSubString(this.message, "", " Enabled"), (float)(x1)+3, (float)(y1 + this.height / 2)-8, new Color(155,155,155).getRGB());
        Client.fontManager.wqy16.drawString("Enabled", (float)(x1)+7+Client.fontManager.wqy16.getStringWidth(StringUtils.getSubString(this.message, "", " Enabled")), (float)(y1 + this.height / 2)-8, new Color(100,100,100).getRGB());
        }
        }
        if(Type == Type.ERROR) {
        if(StringUtils.getSubString(this.message, "", " Disable") != StringUtils.getSubString("123", "SD", "NMSL")){
            Client.fontManager.wqy16.drawString(StringUtils.getSubString(this.message, "", " Disable"), (float)(x1)+3, (float)(y1 + this.height / 2)-8, new Color(155,155,155).getRGB());
            Client.fontManager.wqy16.drawString("Disable", (float)(x1)+7+Client.fontManager.wqy16.getStringWidth(StringUtils.getSubString(this.message, "", " Disable")), (float)(y1 + this.height / 2)-8, new Color(100,100,100).getRGB());
        }
        }
        
        if(Type != Type.ERROR & Type != Type.SUCCESS ) {
            Client.fontManager.wqy16.drawString(this.message, (float)(x1)+3, (float)(y1 + this.height / 2)-8, new Color(255,255,255).getRGB());
        }
        RenderUtil.drawIcon( (x1)-20,(int)(y1 + this.height / 2)-11, 16,16, new ResourceLocation("/Worst/Notification/"+Type.name()+".png"),new Color(200,200,200).getRGB());
    }
    
    public boolean shouldDelete() {
        return this.isFinished() && this.animationX >= this.width;
    }
    
    private boolean isFinished() {
        return this.timer.isDelayComplete(this.stayTime) && this.posY == this.lastY;
    }
    
    public double getHeight() {
        return this.height;
    }
    public enum Type
    {
        SUCCESS("SUCCESS", 0), 
        INFO("INFO", 1), 
        WARNING("WARNING", 2), 
        ERROR("ERROR", 3);
        
        private Type(final String s, final int n) {
        }
    }

    }




