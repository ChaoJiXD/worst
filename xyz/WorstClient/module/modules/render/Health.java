package xyz.WorstClient.module.modules.render;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import xyz.WorstClient.Client;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.rendering.EventRender2D;
import xyz.WorstClient.management.ModuleManager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;

public class Health extends Module{
	private int width;
	public Health() {
		super("Health", new String[] {"Healthy"}, ModuleType.Render);
	}
	@EventHandler
    private void renderHud(EventRender2D event) {
		if(mc.thePlayer.getHealth()>=0&&mc.thePlayer.getHealth()<10) {
			this.width=3;
		}if(mc.thePlayer.getHealth()>=10&&mc.thePlayer.getHealth()<100) {
			this.width=6;
		}
		Client.fontManager.comfortaa24.drawStringWithShadow(""+MathHelper.ceiling_float_int(Minecraft.thePlayer.getHealth()), new ScaledResolution(this.mc).getScaledWidth()/2-this.width, new ScaledResolution(this.mc).getScaledHeight()/2-13-(float)(double)Crosshair.SIZE.getValue()-(float)(double)Crosshair.GAP.getValue(), mc.thePlayer.getHealth()<=10?new Color(255,0,0).getRGB():new Color(0,255,0).getRGB());
	}
}
