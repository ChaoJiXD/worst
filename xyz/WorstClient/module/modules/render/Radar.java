package xyz.WorstClient.module.modules.render;




import java.awt.Color;
import java.util.List;
import java.util.Objects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import xyz.WorstClient.Client;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.rendering.EventRender2D;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.management.FriendManager;
import xyz.WorstClient.management.waypoints.Waypoint;
import xyz.WorstClient.management.waypoints.WaypointManager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.ui.fontRenderer.UnicodeFontRenderer;
import xyz.WorstClient.utils.RenderUtils;
import xyz.WorstClient.utils.Timer;
import xyz.WorstClient.utils.render.ColorManager;
import xyz.WorstClient.utils.render.Colors;
import xyz.WorstClient.utils.render.Colors2;
import xyz.WorstClient.utils.render.RenderUtil;
import xyz.WorstClient.utils.render.StringConversions;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.UnicodeFont;

public class Radar
extends Module {
    private boolean dragging;
    float hue;
    private Numbers<Double> scale = new Numbers<Double>("Scale", "Scale", 2.0,1.0,5.0,0.1);
    private Numbers<Double> x = new Numbers<Double>("X", "X", 500.0, 1.0, 1920.0, 5.0);
    private Numbers<Double> y = new Numbers<Double>("Y", "Y", 2.0,1.0,1080.0, 5.0);
    private Numbers<Double> size = new Numbers<Double>("Size", "Size", 125.0,50.0, 500.0, 5.0);
    public Mode<Enum> mode = new Mode("Mode", "mode", (Enum[])RadarMode.values(), (Enum)RadarMode.Normal);

    public Radar() {
    	super("Radar", new String[]{"minimap"}, ModuleType.Render);
    	this.addValues(this.scale,this.x,this.y,this.size,this.mode);
    }

    @EventHandler
    public void onGui(EventRender2D e) {
    	if(this.mode.getValue()== RadarMode.Normal) {
        ScaledResolution sr = new ScaledResolution(this.mc);
        int size1 = ((Double)this.size.getValue()).intValue();
        float xOffset = ((Double)this.x.getValue()).floatValue();
        float yOffset = ((Double)this.y.getValue()).floatValue();
        float playerOffsetX = (float)Minecraft.thePlayer.posX;
        float playerOffSetZ = (float)Minecraft.thePlayer.posZ;
        int var141 = sr.getScaledWidth();
        int var151 = sr.getScaledHeight();
        int mouseX = Mouse.getX() * var141 / this.mc.displayWidth;
        int mouseY = var151 - Mouse.getY() * var151 / this.mc.displayHeight - 1;
        if ((float)mouseX >= xOffset && (float)mouseX <= xOffset + (float)size1 && (float)mouseY >= yOffset - 3.0f && (float)mouseY <= yOffset + 10.0f && Mouse.getEventButton() == 0) {
            this.dragging = !this.dragging;
            boolean bl = this.dragging;
        }
        if (this.dragging && this.mc.currentScreen instanceof GuiChat) {
            Object newValue = StringConversions.castNumber((String)Double.toString((double)(mouseX - size1 / 2)), (Object)5);
            this.x.setValue((Double)((Double)newValue));
            Object newValueY = StringConversions.castNumber((String)Double.toString((double)(mouseY - 2)), (Object)5);
            this.y.setValue((Double)((Double)newValueY));
        } else {
            this.dragging = false;
        }
        if (this.hue > 255.0f) {
            this.hue = 0.0f;
        }
        float h = this.hue;
        float h2 = this.hue + 85.0f;
        float h3 = this.hue + 170.0f;
        if (h > 255.0f) {
            h = 0.0f;
        }
        if (h2 > 255.0f) {
            h2 -= 255.0f;
        }
        if (h3 > 255.0f) {
            h3 -= 255.0f;
        }
        Color color33 = Color.getHSBColor((float)(h / 255.0f), (float)0.9f, (float)1.0f);
        Color color332 = Color.getHSBColor((float)(h2 / 255.0f), (float)0.9f, (float)1.0f);
        Color color333 = Color.getHSBColor((float)(h3 / 255.0f), (float)0.9f, (float)1.0f);
        int color1 = color33.getRGB();
        int color2 = color332.getRGB();
        int color3 = color333.getRGB();
        this.hue = (float)((double)this.hue + 0.1);
        //RenderUtil.rectangleBordered((double)xOffset, (double)yOffset, (double)(xOffset + (float)size1), (double)(yOffset + (float)size1), (double)0.5, (int)Colors2.getColor((int)90), (int)Colors2.getColor((int)0));
        //RenderUtil.rectangleBordered((double)(xOffset + 1.0f), (double)(yOffset + 1.0f), (double)(xOffset + (float)size1 - 1.0f), (double)(yOffset + (float)size1 - 1.0f), (double)1.0, (int)Colors2.getColor((int)90), (int)Colors2.getColor((int)61));
        //RenderUtil.rectangleBordered((double)((double)xOffset + 2.5), (double)((double)yOffset + 2.5), (double)((double)(xOffset + (float)size1) - 2.5), (double)((double)(yOffset + (float)size1) - 2.5), (double)0.5, (int)Colors2.getColor((int)61), (int)Colors2.getColor((int)0));
        RenderUtil.drawRect((xOffset + 3.0f), (yOffset + 3.0f), (xOffset + (float)size1 - 3.0f),(yOffset + (float)size1 - 3.0f),new Color(0,0,0,150).getRGB());
        RenderUtil.drawGradientSideways((double)(xOffset + 3.0f), (double)(yOffset + 3.0f), (double)(xOffset + (float)(size1 / 2)), (double)((double)yOffset + 3.6), (int)color1, (int)color2);
        RenderUtil.drawGradientSideways((double)(xOffset + (float)(size1 / 2)), (double)(yOffset + 3.0f), (double)(xOffset + (float)size1 - 3.0f), (double)((double)yOffset + 3.6), (int)color2, (int)color3);
        RenderUtil.rectangle((double)((double)xOffset + ((double)(size1 / 2) - 0.5)), (double)((double)yOffset + 3.5), (double)((double)xOffset + ((double)(size1 / 2) + 0.5)), (double)((double)(yOffset + (float)size1) - 3.5), (int)Colors2.getColor((int)255, (int)80));
        RenderUtil.rectangle((double)((double)xOffset + 3.5), (double)((double)yOffset + ((double)(size1 / 2) - 0.5)), (double)((double)(xOffset + (float)size1) - 3.5), (double)((double)yOffset + ((double)(size1 / 2) + 0.5)), (int)Colors2.getColor((int)255, (int)80));
        for (Object o : Minecraft.theWorld.getLoadedEntityList()) {
            EntityPlayer ent;
            if (!(o instanceof EntityPlayer) || !(ent = (EntityPlayer)o).isEntityAlive() || ent == Minecraft.thePlayer || ent.isInvisible() || ent.isInvisibleToPlayer((EntityPlayer)Minecraft.thePlayer)) continue;
            float pTicks = this.mc.timer.renderPartialTicks;
            float posX = (float)((ent.posX + (ent.posX - ent.lastTickPosX) * (double)pTicks - (double)playerOffsetX) * (Double)this.scale.getValue());
            float posZ = (float)((ent.posZ + (ent.posZ - ent.lastTickPosZ) * (double)pTicks - (double)playerOffSetZ) * (Double)this.scale.getValue());
            int color = Minecraft.thePlayer.canEntityBeSeen((Entity)ent) ? ColorManager.getEnemyVisible().getColorInt() : ColorManager.getEnemyInvisible().getColorInt();
            float cos = (float)Math.cos((double)((double)Minecraft.thePlayer.rotationYaw * 0.017453292519943295));
            float sin = (float)Math.sin((double)((double)Minecraft.thePlayer.rotationYaw * 0.017453292519943295));
            float rotY = (- posZ) * cos - posX * sin;
            float rotX = (- posX) * cos + posZ * sin;
            if (rotY > (float)(size1 / 2 - 5)) {
                rotY = (float)(size1 / 2) - 5.0f;
            } else if (rotY < (float)((- size1) / 2 - 5)) {
                rotY = (- size1) / 2 - 5;
            }
            if (rotX > (float)(size1 / 2) - 5.0f) {
                rotX = size1 / 2 - 5;
            } else if (rotX < (float)((- size1) / 2 - 5)) {
                rotX = - (float)(size1 / 2) - 5.0f;
            }
            RenderUtil.rectangleBordered((double)((double)(xOffset + (float)(size1 / 2) + rotX) - 1.5), (double)((double)(yOffset + (float)(size1 / 2) + rotY) - 1.5), (double)((double)(xOffset + (float)(size1 / 2) + rotX) + 1.5), (double)((double)(yOffset + (float)(size1 / 2) + rotY) + 1.5), (double)0.5, (int)color, (int)Colors2.getColor((int)46));
        }
    }if(this.mode.getValue()==RadarMode.Round) {
		Timer timer = new Timer();
        ScaledResolution sr = new ScaledResolution(this.mc);
        int size = ((Double)this.size.getValue()).intValue();
        float xOffset = ((Double)this.x.getValue()).floatValue();
        float yOffset = ((Double)this.y.getValue()).floatValue();
        float playerOffsetX = (float)Minecraft.thePlayer.posX;
        float playerOffSetZ = (float)Minecraft.thePlayer.posZ;
		RenderUtils.circle(xOffset + (size / 2), yOffset + size / 2, size / 2 - 4, Colors.getColor(50, 100));
        GlStateManager.pushMatrix();
        GlStateManager.translate(xOffset + size / 2, yOffset + size / 2, 0);
        GlStateManager.rotate(-mc.thePlayer.rotationYaw, 0, 0, 1);
        RenderUtil.rectangle((-0.5), -size / 2 + 4, (0.5), size / 2 - 4, Colors.getColor(255, 80));
        RenderUtil.rectangle(-size / 2 + 4, (-0.5), size / 2 - 4, (+0.5),
                Colors.getColor(255, 80));
        GlStateManager.popMatrix();

        RenderUtil.drawCircle(xOffset + (size / 2), yOffset + size / 2, size / 2 - 4, 72, Colors.getColor(0, 200));

        UnicodeFontRenderer normal = Client.fontManager.wqy14;
        float angle2 = -mc.thePlayer.rotationYaw + 90;
        float x2 = (float) ((size / 2 + 4) * Math.cos(Math.toRadians(angle2))) + xOffset + size / 2; // angle is in radians
        float y2 = (float) ((size / 2 + 4) * Math.sin(Math.toRadians(angle2))) + yOffset + size / 2;
        normal.drawStringWithShadow("N", x2 - normal.getStringWidth("N") / 2, y2 - 1, -1);
        x2 = (float) ((size / 2 + 4) * Math.cos(Math.toRadians(angle2 + 90))) + xOffset + size / 2; // angle is in radians
        y2 = (float) ((size / 2 + 4) * Math.sin(Math.toRadians(angle2 + 90))) + yOffset + size / 2;
        normal.drawStringWithShadow("E", x2 - normal.getStringWidth("E") / 2, y2 - 1, -1);
        x2 = (float) ((size / 2 + 4) * Math.cos(Math.toRadians(angle2 + 180))) + xOffset + size / 2; // angle is in radians
        y2 = (float) ((size / 2 + 4) * Math.sin(Math.toRadians(angle2 + 180))) + yOffset + size / 2;
        normal.drawStringWithShadow("S", x2 - normal.getStringWidth("S") / 2, y2 - 1, -1);
        x2 = (float) ((size / 2 + 4) * Math.cos(Math.toRadians(angle2 - 90))) + xOffset + size / 2; // angle is in radians
        y2 = (float) ((size / 2 + 4) * Math.sin(Math.toRadians(angle2 - 90))) + yOffset + size / 2;
        normal.drawStringWithShadow("W", x2 - normal.getStringWidth("W") / 2, y2 - 1, -1);

        int var141 = sr.getScaledWidth();
        int var151 = sr.getScaledHeight();
        final int mouseX = Mouse.getX() * var141 / mc.displayWidth;
        final int mouseY = var151 - Mouse.getY() * var151 / mc.displayHeight - 1;
        if (mouseX >= xOffset && mouseX <= xOffset + size && mouseY >= yOffset - 3 && mouseY <= yOffset + 10 && Mouse.getEventButton() == 0 ) {
        	timer.reset();
            dragging = !dragging;
        }
        if (dragging && mc.currentScreen instanceof GuiChat) {
            Object newValue = (StringConversions.castNumber(Double.toString(mouseX - size / 2), 5));
            x.setValue((Double)newValue);
            Object newValueY = (StringConversions.castNumber(Double.toString(mouseY - 2), 5));
           y.setValue((Double) newValueY);
        } else {
            dragging = false;
        }

        for (Object o : mc.theWorld.getLoadedEntityList()) {
            if (o instanceof EntityPlayer) {
                EntityPlayer ent = (EntityPlayer) o;
                if (ent.isEntityAlive() && ent != mc.thePlayer && !(ent.isInvisible() || ent.isInvisibleToPlayer(mc.thePlayer))) {

                    float pTicks = mc.timer.renderPartialTicks;
                    float posX = (float) (((ent.posX + (ent.posX - ent.lastTickPosX) * pTicks) -
                            playerOffsetX) * ((Number) scale.getValue()).doubleValue());

                    float posZ = (float) (((ent.posZ + (ent.posZ - ent.lastTickPosZ) * pTicks) -
                            playerOffSetZ) * ((Number) scale.getValue()).doubleValue());
                    int color;
                    if (FriendManager.isFriend(ent.getName())) {
                        color = mc.thePlayer.canEntityBeSeen(ent) ? Colors.getColor(0, 195, 255)
                                : Colors.getColor(0, 195, 255);
                    } else {
                        color = mc.thePlayer.canEntityBeSeen(ent) ? Colors.getColor(255, 0, 0)
                                : Colors.getColor(255, 255, 0);
                    }

                    float cos = (float) Math.cos(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
                    float sin = (float) Math.sin(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
                    float rotY = -(posZ * cos - posX * sin);
                    float rotX = -(posX * cos + posZ * sin);
                    float var7 = 0 - rotX;
                    float var9 = 0 - rotY;
                    if (MathHelper.sqrt_double(var7 * var7 + var9 * var9) > size / 2 - 4) {
                        float angle = findAngle(0, rotX, 0, rotY);
                        float x = (float) ((size / 2) * Math.cos(Math.toRadians(angle))) + xOffset + size / 2; // angle is in radians
                        float y = (float) ((size / 2) * Math.sin(Math.toRadians(angle))) + yOffset + size / 2;
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(x, y, 0);
                        GlStateManager.rotate(angle, 0, 0, 1);
                        GlStateManager.scale(1.5f, 0.5, 0.5);
                        RenderUtil.drawCircle(0, 0, 1.5f, 3, Colors.getColor(46));
                        RenderUtil.drawCircle(0, 0, 1, 3, color);
                        GlStateManager.popMatrix();
                    } else {
                    	RenderUtil.rectangleBordered(xOffset + (size / 2) + rotX - 1.5,
                                yOffset + (size / 2) + rotY - 1.5, xOffset + (size / 2) + rotX + 1.5,
                                yOffset + (size / 2) + rotY + 1.5, 0.5, color, Colors.getColor(46));
                    }

                    /*
                     * Clamps to the edge of the radar, have it less than
                     * the radar if you don't want squares to come out.
                     */


                }
            }
        }
        if (mc.getCurrentServerData() != null)
            for (Waypoint waypoint : WaypointManager.getManager().getWaypoints()) {
                if (Objects.equals(waypoint.getAddress(), mc.getCurrentServerData().serverIP)) {
                    /*
                     * (targetPlayer posX - localPlayer posX) * Distance
                     * Scale
                     */

                    float posX = (float) (waypoint.getVec3().xCoord - playerOffsetX * ((Number) scale.getValue()).doubleValue());
                    /*
                     * (targetPlayer posZ - localPlayer posZ) * Distance
                     * Scale
                     */
                    float posZ = (float) ((float) waypoint.getVec3().zCoord - playerOffSetZ * ((Number) scale.getValue()).doubleValue());

                    /*
                     * Fuck Ms. Goble's geometry class.
                     * Rotate the circle based off of the player yaw with some gay trig.
                     */

                    float cos = (float) Math.cos(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
                    float sin = (float) Math.sin(mc.thePlayer.rotationYaw * (Math.PI * 2 / 360));
                    float rotY = -(posZ * cos - posX * sin);
                    float rotX = -(posX * cos + posZ * sin);
                    float var7 = 0 - rotX;
                    float var9 = 0 - rotY;

                    if (MathHelper.sqrt_double(var7 * var7 + var9 * var9) > size / 2 - 4) {
                        float angle = findAngle(0, rotX, 0, rotY);
                        float x = (float) ((size / 2) * Math.cos(Math.toRadians(angle))) + xOffset + size / 2; // angle is in radians
                        float y = (float) ((size / 2) * Math.sin(Math.toRadians(angle))) + yOffset + size / 2;
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(x, y, 0);
                        GlStateManager.rotate(angle, 0, 0, 1);
                        GlStateManager.scale(1.5f, 0.5, 0.5);

                        RenderUtil.drawCircle(0, 0, 1.5f, 3, Colors.getColor(46));
                        RenderUtil.drawCircle(0, 0, 1, 3, waypoint.getColor());
                        GlStateManager.popMatrix();
                    } else {
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(xOffset + (size / 2) + rotX, yOffset + (size / 2) + rotY, 0);
                        GlStateManager.scale(0.5, 0.5, 1);
                        mc.fontRendererObj.drawStringWithShadow(waypoint.getName(), -mc.fontRendererObj.getStringWidth(waypoint.getName()) / 2 + 1, 5, -1);
                        GlStateManager.popMatrix();
                        RenderUtil.rectangleBordered(xOffset + (size / 2) + rotX - 1.5,
                                yOffset + (size / 2) + rotY - 1.5, xOffset + (size / 2) + rotX + 1.5,
                                yOffset + (size / 2) + rotY + 1.5, 0.5, waypoint.getColor(), Colors.getColor(46));
                    }
                }
            }

    }
    }

    public void onDisable() {
        super.onDisable();
    }

    public void onEnable() {
        super.isEnabled();
    }
    private float findAngle(float x, float x2, float y, float y2) {
        return (float) (Math.atan2(y2 - y, x2 - x) * 180 / Math.PI);
    }
    public static enum RadarMode {
        Normal,
        Round;
    }
}
