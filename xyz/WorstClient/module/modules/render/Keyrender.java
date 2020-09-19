package xyz.WorstClient.module.modules.render;

import java.awt.Color;

import xyz.WorstClient.Client;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.rendering.EventRender2D;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.ui.fontRenderer.UnicodeFontRenderer;
import xyz.WorstClient.ui.fontRenderer.CFont.CFontRenderer;
import xyz.WorstClient.utils.render.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class Keyrender extends Module{


	int anima;
	int anima2;
	int anima3;
	int anima4;
	int anima5;
	int anima6;
    private Numbers<Double> x = new Numbers<Double>("X", "X", 500.0, 1.0, 1920.0, 5.0);
    private Numbers<Double> y = new Numbers<Double>("Y", "Y", 2.0,1.0,1080.0, 5.0);
	private double rainbowTick;
    public Keyrender() {
    	super("KeyRender", new String[]{"Key"}, ModuleType.Render);
    	this.addValues(this.x,this.y);
        this.setRemoved(true);
    }
    @EventHandler
    public void onGui(EventRender2D e) {
        UnicodeFontRenderer font = Client.fontManager.comfortaa18;
		Color rainbow = ColorUtils.getRainbow();
        float xOffset = ((Double)this.x.getValue()).floatValue();
        float yOffset = ((Double)this.y.getValue()).floatValue();
        Gui.drawRect((double)xOffset+26, (double)yOffset, (double)(xOffset + 51), (double)(yOffset +25),new Color(0,0,0,150).getRGB());//w
        Gui.drawRect((double)xOffset+26, (double)yOffset+26, (double)(xOffset +51), (double)(yOffset+51),new Color(0,0,0,150).getRGB());//s
        Gui.drawRect((double)xOffset, (double)yOffset+26, (double)(xOffset +25), (double)(yOffset+51),new Color(0,0,0,150).getRGB());//a
        Gui.drawRect((double)xOffset+52, (double)yOffset+26, (double)(xOffset +77), (double)(yOffset+51),new Color(0,0,0,150).getRGB());//d
        Gui.drawRect((double)xOffset+1+77/2 ,(double)yOffset+52, (double)(xOffset +77), (double)(yOffset+77),new Color(0,0,0,150).getRGB());//LMB
        Gui.drawRect((double)xOffset, (double)yOffset+52, (double)(xOffset +77/2), (double)(yOffset+77),new Color(0,0,0,150).getRGB());//RMB
        font.drawStringWithShadow("W", xOffset+(float)34.5, yOffset+9, rainbow.getRGB());
        font.drawStringWithShadow("S", xOffset+(float)36, yOffset+35, rainbow.getRGB());
        font.drawStringWithShadow("A", xOffset+(float)10, yOffset+35, rainbow.getRGB());
        font.drawStringWithShadow("D", xOffset+(float)62, yOffset+35, rainbow.getRGB());
        font.drawStringWithShadow("LMB", xOffset+(float)10, yOffset+60, rainbow.getRGB());
        font.drawStringWithShadow("RMB", xOffset+(float)50, yOffset+60, rainbow.getRGB());
		if (++rainbowTick > 10000) {
			rainbowTick = 0;		
		}
        //w
        if(mc.gameSettings.keyBindForward.pressed) {
            if (this.anima < 25) {
               this.anima=this.anima+5;
            } 
        }else if (this.anima > 0) {
            this.anima=this.anima-5;
        }
        //s
        if(mc.gameSettings.keyBindBack.pressed) {
            if (this.anima2 < 25) {
                this.anima2=this.anima2+5;
            } 
        }else if (this.anima2 > 0) {
            this.anima2=this.anima2-5;
        }
        //a
        if(mc.gameSettings.keyBindLeft.pressed) {
            if (this.anima3 < 25) {
                this.anima3=this.anima3+5;
            } 
        }else if (this.anima3 > 0) {
            this.anima3=this.anima3-5;
        }
        //d
        if(mc.gameSettings.keyBindRight.pressed) {
            if (this.anima4 < 25) {
                this.anima4=this.anima4+5;
            } 
        }else if (this.anima4 > 0) {
            this.anima4=this.anima4-5;
        }
        //LMB
        if(mc.gameSettings.keyBindUseItem.pressed) {
            if (this.anima5 < 25) {
                this.anima5=this.anima5+5;
            } 
        }else if (this.anima5 > 0) {
            this.anima5=this.anima5-5;
        }
        //RMB
        if(mc.gameSettings.keyBindAttack.pressed) {
            if (this.anima6 < 25) {
                this.anima6=this.anima6+5;
            } 
        }else if (this.anima6 > 0) {
            this.anima6=this.anima6-5;
        }
            Gui.drawRect((double)xOffset+26, (double)yOffset+25, (double)(xOffset + 51), (double)(yOffset +25-anima),new Color(255,255,255,120).getRGB());//w
            Gui.drawRect((double)xOffset+26, (double)yOffset+51, (double)(xOffset +51), (double)(yOffset+51-anima2),new Color(255,255,255,120).getRGB());//s
            Gui.drawRect((double)xOffset, (double)yOffset+51, (double)(xOffset +25), (double)(yOffset+51-anima3),new Color(255,255,255,120).getRGB());//a
            Gui.drawRect((double)xOffset+52, (double)yOffset+51, (double)(xOffset +77), (double)(yOffset+51-anima4),new Color(255,255,255,120).getRGB());//d
            Gui.drawRect((double)xOffset+1+77/2 ,(double)yOffset+77, (double)(xOffset +77), (double)(yOffset+77-anima5),new Color(255,255,255,120).getRGB());//LMB
            Gui.drawRect((double)xOffset, (double)yOffset+77, (double)(xOffset +77/2), (double)(yOffset+77-anima6),new Color(255,255,255,120).getRGB());//RMB
    }
    public void onDisable() {
    	this.anima=0;
    	this.anima2=0;
    	this.anima3=0;
    	this.anima4=0;
    	this.anima5=0;
    	this.anima6=0;
        super.onDisable();
    }
    public void onEnable() {
        super.isEnabled();
    }

}
