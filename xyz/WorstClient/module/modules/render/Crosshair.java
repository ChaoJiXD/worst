package xyz.WorstClient.module.modules.render;

import java.awt.Color;

import net.minecraft.client.gui.ScaledResolution;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.rendering.EventRender2D;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.utils.render.Colors;
import xyz.WorstClient.utils.render.RenderUtil;


public class Crosshair
extends Module {
    private boolean dragging;
    float hue;
    private Option<Boolean> DYNAMIC = new Option<Boolean>("DYNAMIC", "DYNAMIC", true);
    public static Numbers<Double> GAP = new Numbers<Double>("gap", "gap", 5.0, 0.25, 15.0, 0.25);
    private Numbers<Double> WIDTH = new Numbers<Double>("width", "width", 2.0,0.25,10.0, 0.25);
    public static Numbers<Double> SIZE = new Numbers<Double>("size", "size", 7.0,0.25,15.0,0.25);

    public Crosshair() {
        super("Crosshair", new String[] {"Crosshair"},ModuleType.Render);
        this.addValues(this.DYNAMIC,this.GAP,this.WIDTH,this.SIZE);
    }

    @EventHandler
    public void onGui(EventRender2D e) {
    	int red = setColor.r.getValue().intValue();
        int green = setColor.g.getValue().intValue();
        int blue = setColor.b.getValue().intValue();
        int alph = 255;
        double gap = ((Double)this.GAP.getValue()).doubleValue();
        double width = ((Double) this.WIDTH.getValue()).doubleValue();
        double size = ((Double) this.SIZE.getValue()).doubleValue();
        ScaledResolution scaledRes = new ScaledResolution(mc);
        RenderUtil.rectangleBordered(
                scaledRes.getScaledWidth() / 2 - width,
                scaledRes.getScaledHeight() / 2 - gap - size - (isMoving() ? 2 : 0),
                scaledRes.getScaledWidth() / 2 + 1.0f + width,
                scaledRes.getScaledHeight() / 2 - gap - (isMoving() ? 2 : 0),  0.5f, Colors.getColor(red, green, blue, alph),
                new Color(0, 0, 0, alph).getRGB());
        RenderUtil.rectangleBordered(
                scaledRes.getScaledWidth() / 2 - width,
                scaledRes.getScaledHeight() / 2 + gap + 1 + (isMoving() ? 2 : 0) - 0.15,
                scaledRes.getScaledWidth() / 2 + 1.0f + width,
                scaledRes.getScaledHeight() / 2 + 1 + gap + size + (isMoving() ? 2 : 0) - 0.15, 0.5f, Colors.getColor(red, green, blue, alph),
                new Color(0, 0, 0, alph).getRGB());
        RenderUtil.rectangleBordered(
                scaledRes.getScaledWidth() / 2 - gap - size - (isMoving() ? 2 : 0) + 0.15,
                scaledRes.getScaledHeight() / 2 - width,
                scaledRes.getScaledWidth() / 2 - gap - (isMoving() ? 2 : 0) + 0.15,
                scaledRes.getScaledHeight() / 2 + 1.0f + width, 0.5f, Colors.getColor(red, green, blue, alph),
                new Color(0, 0, 0, alph).getRGB());
        RenderUtil.rectangleBordered(
                scaledRes.getScaledWidth() / 2 + 1 + gap + (isMoving() ? 2 : 0),
                scaledRes.getScaledHeight() / 2 - width,
                scaledRes.getScaledWidth() / 2 + size + gap + 1 + (isMoving() ? 2 : 0),
                scaledRes.getScaledHeight() / 2 + 1.0f + width, 0.5f, Colors.getColor(red, green, blue, alph),
                new Color(0, 0, 0, alph).getRGB());
    }
    public boolean isMoving() {
        return DYNAMIC.getValue() && (!mc.thePlayer.isCollidedHorizontally) && (!mc.thePlayer.isSneaking()) && ((mc.thePlayer.movementInput.moveForward != 0.0F) || (mc.thePlayer.movementInput.moveStrafe != 0.0F));
    }
    
}
