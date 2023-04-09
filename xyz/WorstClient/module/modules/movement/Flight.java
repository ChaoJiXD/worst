/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.module.modules.movement;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventMove;
import xyz.WorstClient.api.events.world.EventPostUpdate;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.utils.PlayerUtil;
import xyz.WorstClient.utils.TimerUtil;
import xyz.WorstClient.utils.math.MathUtil;

public class Flight
extends Module {
    private Numbers<Double> vanliaspeed = new Numbers<Double>("VanliaSpeed", "VanliaSpeed", 2.0, 1.0, 5.0, 0.1);
    public static Mode mode = new Mode("Mode", "mode", (Enum[])FlightMode.values(), (Enum)FlightMode.Hypixel);
    private Option bob = new Option("Bobbing", "Bobbing", Boolean.valueOf(true));
    private TimerUtil timer = new TimerUtil();
    private double movementSpeed;
    private int hypixelCounter;
    private int hypixelCounter2;
    int counter, level;
    double moveSpeed, lastDist;   
    boolean b2;
    public Flight() {
        super("AirWalk", new String[]{"AirWalk"}, ModuleType.Movement);
        this.addValues(this.mode,this.bob,this.vanliaspeed);
    }


    
    @Override
    public void onEnable() {	
    	new Boost().setEnabled(true);
        if (this.mode.getValue() == FlightMode.Hypixel ) {
            this.hypixelCounter = 0;
            this.hypixelCounter2 = 1000;
            this.mc.thePlayer.motionY = 0.42f;
           	PlayerUtil.setSpeed(1.3);
        }
		level = 1;
		moveSpeed = 0.1D;
		b2 = true;
		lastDist = 0.0D;
    }

    @Override
    public void onDisable() {
    	PlayerUtil.setSpeed(1.0);
        this.hypixelCounter = 0;
        this.hypixelCounter2 = 100;
        this.mc.timer.timerSpeed = 1.0f;
		level = 1;
		moveSpeed = 0.1D;
		b2 = false;
		lastDist = 0.0D;
		this.mc.thePlayer.setSpeed(0.1);
    }
    @EventHandler
    public void onPost(EventPostUpdate e) {
    	if (this.mode.getValue() == FlightMode.Hypixel ) {
 			double xDist = Minecraft.getMinecraft().thePlayer.posX
 					- Minecraft.getMinecraft().thePlayer.prevPosX;
 			double zDist = Minecraft.getMinecraft().thePlayer.posZ
 					- Minecraft.getMinecraft().thePlayer.prevPosZ;
 			lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
    	}
    }
    int i=0;
    boolean Boost =false;
    @EventHandler
    private void onUpdate(EventPreUpdate e) {
    	

    	if(((Boolean) this.bob.getValue()).booleanValue()) {
    	this.mc.thePlayer.cameraYaw =(float) 0.1;
    	}
        this.setSuffix(this.mode.getValue());
        if (this.mode.getValue() == FlightMode.Hypixel ) {
   			++counter;
   			if (Minecraft.getMinecraft().thePlayer.moveForward == 0
   					&& Minecraft.getMinecraft().thePlayer.moveStrafing == 0) {
   				Minecraft.getMinecraft().thePlayer.setPosition(
   						Minecraft.getMinecraft().thePlayer.posX + 1.0D,
   						Minecraft.getMinecraft().thePlayer.posY + 1.0D,
   						Minecraft.getMinecraft().thePlayer.posZ + 1.0D);
   				Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.prevPosX,
   						Minecraft.getMinecraft().thePlayer.prevPosY,
   						Minecraft.getMinecraft().thePlayer.prevPosZ);
   				Minecraft.getMinecraft().thePlayer.motionX = 0.0D;
   				Minecraft.getMinecraft().thePlayer.motionZ = 0.0D;
   			}
   			Minecraft.getMinecraft().thePlayer.motionY = 0.0D;
   			if (Minecraft.getMinecraft().gameSettings.keyBindJump.pressed)
   				Minecraft.getMinecraft().thePlayer.motionY += 0.5f;
   			if (Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed)
   				Minecraft.getMinecraft().thePlayer.motionY -= 0.5f;
   			if (counter != 1 && counter == 2) {
   				Minecraft.getMinecraft().thePlayer.setPosition(Minecraft.getMinecraft().thePlayer.posX,
   						Minecraft.getMinecraft().thePlayer.posY + 1.0E-10D,
   						Minecraft.getMinecraft().thePlayer.posZ);
   				counter = 0;
   			}
        } else if (this.mode.getValue() == FlightMode.Vanilla) {
            this.mc.thePlayer.motionY = this.mc.thePlayer.movementInput.jump ? 1.0 : (this.mc.thePlayer.movementInput.sneak ? -1.0 : 0.0);
            if (this.mc.thePlayer.moving()) {
                this.mc.thePlayer.setSpeed(this.vanliaspeed.getValue().doubleValue());
            } else {
                this.mc.thePlayer.setSpeed(0.0);
            }
    }
        }
    @EventHandler
    private void onMove(EventMove e) {
    	if(this.mode.getValue()==FlightMode.Hypixel) {
			float forward = MovementInput.moveForward;
			float strafe = MovementInput.moveStrafe;
			float yaw = mc.thePlayer.rotationYaw;
			double mx = Math.cos(Math.toRadians((double) (yaw + 90.0F)));
			double mz = Math.sin(Math.toRadians((double) (yaw + 90.0F)));
			
			if (forward == 0.0F && strafe == 0.0F) {
				e.x = 0.0D;
				e.z = 0.0D;
			} else if (forward != 0.0F) {
				if (strafe >= 1.0F) {
					yaw += (float) (forward > 0.0F ? -45 : 45);
					strafe = 0.0F;
				} else if (strafe <= -1.0F) {
					yaw += (float) (forward > 0.0F ? 45 : -45);
					strafe = 0.0F;
				}

				if (forward > 0.0F) {
					forward = 1.0F;
				} else if (forward < 0.0F) {
					forward = -1.0F;
				}
			}
			if (b2) {
				if (level != 1 || Minecraft.getMinecraft().thePlayer.moveForward == 0.0F
						&& Minecraft.getMinecraft().thePlayer.moveStrafing == 0.0F) {
					if (level == 2) {
						level = 3;
						moveSpeed *= 2.1499999D;
					} else if (level == 3) {
						level = 4;
						double difference = (mc.thePlayer.ticksExisted % 2 == 0 ? 0.0103D : 0.0123D)
								* (lastDist - MathUtil.getBaseMovementSpeed());
						moveSpeed = lastDist - difference;
					} else {
						if (Minecraft.getMinecraft().theWorld
								.getCollidingBoundingBoxes(Minecraft.getMinecraft().thePlayer,
										Minecraft.getMinecraft().thePlayer.boundingBox.offset(0.0D,
												Minecraft.getMinecraft().thePlayer.motionY, 0.0D))
								.size() > 0 || Minecraft.getMinecraft().thePlayer.isCollidedVertically) {
							level = 1;
						}
						moveSpeed = lastDist - lastDist / 159.0D;
					}
				} else {
					level = 2;
					int amplifier = Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)
							? Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed)
									.getAmplifier() + 1
							: 0;
					double boost = Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed) ? 1.56
							: 2.034;
					moveSpeed = boost * MathUtil.getBaseMovementSpeed();
				}
				moveSpeed =  MathUtil.getBaseMovementSpeed();
				
				e.x = (double) forward * moveSpeed * mx + (double) strafe * moveSpeed * mz;
				e.z = (double) forward * moveSpeed * mz - (double) strafe * moveSpeed * mx;
				if (forward == 0.0F && strafe == 0.0F) {
					e.x = 0.0D;
					e.z = 0.0D;
				}
			}
		}
    }
    double getBaseMoveSpeed() {
        double baseSpeed = 0.275;
        if (this.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    public static enum FlightMode {
        Vanilla,
        Hypixel,
    }

}

