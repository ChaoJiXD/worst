package xyz.WorstClient.utils.liquidslowly;

import java.util.Random;


import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import xyz.WorstClient.api.events.misc.Event;
import xyz.WorstClient.module.modules.combat.Killaura;

public class AngleUtility  implements Event{

	private static float minYawSmoothing;
	private static float maxYawSmoothing;
	private static float minPitchSmoothing;
	private static float maxPitchSmoothing;
	private static Vector3<Float> delta;
	private static Angle smoothedAngle;
	private static Random random;
	private static float height = 1.5f;

	 public AngleUtility(float minYawSmoothing, float maxYawSmoothing, float minPitchSmoothing, float maxPitchSmoothing) {
	        this.minYawSmoothing = minYawSmoothing;
	        this.maxYawSmoothing = maxYawSmoothing;
	        this.minPitchSmoothing = minPitchSmoothing;
	        this.maxPitchSmoothing = maxPitchSmoothing;
	        this.random = new Random();
	        this.delta = new Vector3<>(0F, 0F, 0F);
	        this.smoothedAngle = new Angle(0F, 0F);
	    }

	 public static float[] getAngleBlockpos(BlockPos target) {
	        double xDiff = target.getX() - Minecraft.getMinecraft().thePlayer.posX;
	        double yDiff = target.getY() - Minecraft.getMinecraft().thePlayer.posY;
	        double zDiff = target.getZ() - Minecraft.getMinecraft().thePlayer.posZ;
	        float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / Math.PI) - 90.0f;
	        float pitch = (float) ((-Math.atan2(
	                target.getY() + (double) -1 - (Minecraft.getMinecraft().thePlayer.posY + (double) Minecraft.getMinecraft().thePlayer.getEyeHeight()),
	                Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);

	        if (yDiff > -0.2 && yDiff < 0.2) {
	            pitch = (float) ((-Math.atan2(
	                    target.getY() + (double) -1 - (Minecraft.getMinecraft().thePlayer.posY + (double) Minecraft.getMinecraft().thePlayer.getEyeHeight()),
	                    Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
	        } else if (yDiff > -0.2) {
	            pitch = (float) ((-Math.atan2(
	                    target.getY() + (double) -1 - (Minecraft.getMinecraft().thePlayer.posY + (double) Minecraft.getMinecraft().thePlayer.getEyeHeight()),
	                    Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
	        } else if (yDiff < 0.3) {
	            pitch = (float) ((-Math.atan2(
	                    target.getY() + (double) -1 - (Minecraft.getMinecraft().thePlayer.posY + (double) Minecraft.getMinecraft().thePlayer.getEyeHeight()),
	                    Math.hypot(xDiff, zDiff))) * 180.0 / 3.141592653589793);
	        }

	        return new float[]{yaw, pitch};
	    }

	    public float randomFloat(float min, float max) {
	        return min + (this.random.nextFloat() * (max - min));
	    }

	    public Angle calculateAngle(Vector3<Double> destination, Vector3<Double> source) {
	        Angle angles = new Angle(0.0F, 0.0F);
	        //Height of where you want to aim at on the entity.
	        float height = 1.2F;
	        this.delta
	        .setX(destination.getX().floatValue() - source.getX().floatValue())
	        .setY((destination.getY().floatValue() + height) - (source.getY().floatValue() + height))
	        .setZ(destination.getZ().floatValue() - source.getZ().floatValue());
	        double hypotenuse = Math.hypot(this.delta.getX().doubleValue(), this.delta.getZ().doubleValue());
	        float yawAtan = ((float) Math.atan2(this.delta.getZ().floatValue(), this.delta.getX().floatValue()));
	        float pitchAtan = ((float) Math.atan2(this.delta.getY().floatValue(), hypotenuse));
	        float deg = ((float) (180 / Math.PI));
	        float yaw = ((yawAtan * deg) - 90F);
	        float pitch = -(pitchAtan * deg);
	        return angles.setYaw(yaw).setPitch(pitch).constrantAngle();
	    }
	    
	    
	    public Angle smoothAngle(Angle destination, Angle source) {
	        return this.smoothedAngle
	                .setYaw(source.getYaw() - destination.getYaw())
	                .setPitch(source.getPitch() - destination.getPitch())
	                .constrantAngle()
	                .setYaw(source.getYaw() - this.smoothedAngle.getYaw() / 110* randomFloat(minYawSmoothing, maxYawSmoothing))
	                .setPitch(source.getPitch() - this.smoothedAngle.getPitch() / 90 * randomFloat(minPitchSmoothing, maxPitchSmoothing))
	                .constrantAngle();
	    }
}