/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.api.events.world;

import java.util.Random;

import net.minecraft.client.Minecraft;
import xyz.WorstClient.api.Event;

public class EventPreUpdate
extends Event {
    private static final Random random = new Random();
    private boolean isPre;
	public float yaw;
    public float pitch;
    public double y;
    private boolean ground;

    public EventPreUpdate(float yaw, float pitch, double y, boolean ground) {
    	this.isPre = true;
        this.yaw = yaw;
        this.pitch = pitch;
        this.y = y;
        this.ground = ground;
    }

    public float getYaw() {
        return this.yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public boolean isOnground() {
        return this.ground;
    }

    public void setOnground(boolean ground) {
        this.ground = ground;
    }
	
	    public void setRotations(float[] rotations, boolean random) {
        if (random) {
            yaw = rotations[0] + (float) (EventPreUpdate.random.nextBoolean() ? Math.random() : -Math.random());
            pitch = rotations[1] + (float) (EventPreUpdate.random.nextBoolean() ? Math.random() : -Math.random());
        } else {
            yaw = rotations[0];
            pitch = rotations[1];
        }
        Minecraft.getMinecraft().thePlayer.rotationYawHead = yaw;
        Minecraft.getMinecraft().thePlayer.renderYawOffset = yaw;
        Minecraft.getMinecraft().thePlayer.rotationPitchHead = pitch;
    }

	    public boolean isPre() {
	        return this.isPre;
	     }
}

