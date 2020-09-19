/*
 * Decompiled with CFR 0.136.
 */
package xyz.WorstClient.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class RotationUtils {
    public static float[] getBlockRotations(int x2, int y2, int z2, EnumFacing facing) {
        Minecraft mc2 = Minecraft.getMinecraft();
        EntitySnowball temp = new EntitySnowball(Minecraft.theWorld);
        temp.posX = (double)x2 + 0.5;
        temp.posY = (double)y2 + 0.5;
        temp.posZ = (double)z2 + 0.5;
        return RotationUtils.getAngles(temp);
    }

    public static float[] getAngles(Entity e2) {
        Minecraft mc2 = Minecraft.getMinecraft();
        return new float[]{RotationUtils.getYawChangeToEntity(e2) + Minecraft.thePlayer.rotationYaw, RotationUtils.getPitchChangeToEntity(e2) + Minecraft.thePlayer.rotationPitch};
    }

    public static float[] getRotations(Entity entity) {
        double diffY;
        if (entity == null) {
            return null;
        }
        Minecraft.getMinecraft();
        double diffX = entity.posX - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double diffZ = entity.posZ - Minecraft.thePlayer.posZ;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase elb = (EntityLivingBase)entity;
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            diffY = elb.posY + ((double)elb.getEyeHeight() - 0.2) - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        } else {
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            diffY = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2.0 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        }
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)((- Math.atan2(diffY, dist)) * 180.0 / 3.141592653589793) - 60.0f;
        return new float[]{yaw, pitch};
    }

    public static float getYawChangeToEntity(Entity entity) {
        Minecraft mc2 = Minecraft.getMinecraft();
        double deltaX = entity.posX - Minecraft.thePlayer.posX;
        double deltaZ = entity.posZ - Minecraft.thePlayer.posZ;
        double yawToEntity = deltaZ < 0.0 && deltaX < 0.0 ? 90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)) : (deltaZ < 0.0 && deltaX > 0.0 ? -90.0 + Math.toDegrees(Math.atan(deltaZ / deltaX)) : Math.toDegrees(- Math.atan(deltaX / deltaZ)));
        return MathHelper.wrapAngleTo180_float(- Minecraft.thePlayer.rotationYaw - (float)yawToEntity);
    }

    public static float getPitchChangeToEntity(Entity entity) {
        Minecraft mc2 = Minecraft.getMinecraft();
        double deltaX = entity.posX - Minecraft.thePlayer.posX;
        double deltaZ = entity.posZ - Minecraft.thePlayer.posZ;
        double deltaY = entity.posY - 1.6 + (double)entity.getEyeHeight() - 0.4 - Minecraft.thePlayer.posY;
        double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        double pitchToEntity = - Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return - MathHelper.wrapAngleTo180_float(Minecraft.thePlayer.rotationPitch - (float)pitchToEntity);
    }

    public static float[] getRotationFromPosition(double x2, double z2, double y2) {
        Minecraft.getMinecraft();
        double xDiff = x2 - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double zDiff = z2 - Minecraft.thePlayer.posZ;
        Minecraft.getMinecraft();
        double yDiff = y2 - Minecraft.thePlayer.posY - 0.8;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) + 90.0f;
        float pitch = (float)((- Math.atan2(yDiff, dist)) * 180.0 / 3.141592653589793) + 90.0f;
        return new float[]{yaw, pitch};
    }

    public static float getDistanceBetweenAngles(float angle1, float angle2) {
        float angle3 = Math.abs(angle1 - angle2) % 360.0f;
        if (angle3 > 180.0f) {
            angle3 = 0.0f;
        }
        return angle3;
    }

    public static float[] getRotations(Vec3 position) {
        return RotationUtils.getRotations(Minecraft.thePlayer.getPositionVector().addVector(0.0, Minecraft.thePlayer.getEyeHeight(), 0.0), position);
    }

    public static float[] getRotations(Vec3 origin, Vec3 position) {
        Vec3 difference = position.subtract(origin);
        double distance = difference.flat().lengthVector();
        float yaw = (float)Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0f;
        float pitch = (float)(- Math.toDegrees(Math.atan2(difference.yCoord, distance)));
        return new float[]{yaw, pitch};
    }

    public static float[] getRotations(BlockPos pos) {
        return RotationUtils.getRotations(Minecraft.thePlayer.getPositionVector().addVector(0.0, Minecraft.thePlayer.getEyeHeight(), 0.0), new Vec3((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5));
    }

    public static float[] getBowAngles(Entity entity) {
        double xDelta = entity.posX - entity.lastTickPosX;
        double zDelta = entity.posZ - entity.lastTickPosZ;
        Minecraft.getMinecraft();
        double d2 = Minecraft.thePlayer.getDistanceToEntity(entity);
        d2 -= d2 % 0.8;
        double xMulti = 1.0;
        double zMulti = 1.0;
        boolean sprint = entity.isSprinting();
        xMulti = d2 / 0.8 * xDelta * (sprint ? 1.25 : 1.0);
        zMulti = d2 / 0.8 * zDelta * (sprint ? 1.25 : 1.0);
        Minecraft.getMinecraft();
        double x2 = entity.posX + xMulti - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double z2 = entity.posZ + zMulti - Minecraft.thePlayer.posZ;
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        double y2 = Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight() - (entity.posY + (double)entity.getEyeHeight());
        Minecraft.getMinecraft();
        double dist = Minecraft.thePlayer.getDistanceToEntity(entity);
        float yaw = (float)Math.toDegrees(Math.atan2(z2, x2)) - 90.0f;
        float pitch = (float)Math.toDegrees(Math.atan2(y2, dist));
        return new float[]{yaw, pitch};
    }

    public static float normalizeAngle(float angle) {
        return (angle + 360.0f) % 360.0f;
    }

    public static float getTrajAngleSolutionLow(float d3, float d1, float velocity) {
        float g2 = 0.006f;
        float sqrt = velocity * velocity * velocity * velocity - g2 * (g2 * (d3 * d3) + 2.0f * d1 * (velocity * velocity));
        return (float)Math.toDegrees(Math.atan(((double)(velocity * velocity) - Math.sqrt(sqrt)) / (double)(g2 * d3)));
    }

    public static Vec3 getEyesPos() {
        return new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight(), Minecraft.thePlayer.posZ);
    }

    public static float[] getRotationsBlock(BlockPos pos) {
        Minecraft mc2 = Minecraft.getMinecraft();
        double d0 = (double)pos.getX() - Minecraft.thePlayer.posX;
        double d1 = (double)pos.getY() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        double d2 = (double)pos.getZ() - Minecraft.thePlayer.posZ;
        double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        float f2 = (float)(MathHelper.atan2(d2, d0) * 180.0 / 3.141592653589793) - 90.0f;
        float f1 = (float)(- Math.toDegrees(Math.atan2(d3, d1)));
        return new float[]{f1};
    }

    public static float[] getNeededRotations(Vec3 vec) {
        Vec3 eyesPos = RotationUtils.getEyesPos();
        double diffX = vec.xCoord - eyesPos.xCoord + 0.5;
        double diffY = vec.yCoord - eyesPos.yCoord + 0.5;
        double diffZ = vec.zCoord - eyesPos.zCoord + 0.5;
        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0f;
        float pitch = (float)((- Math.atan2(diffY, diffXZ)) * 180.0 / 3.141592653589793);
        float[] arrf = new float[]{MathHelper.wrapAngleTo180_float(yaw), Minecraft.getMinecraft().gameSettings.keyBindJump.pressed ? 90.0f : MathHelper.wrapAngleTo180_float(pitch)};
        return arrf;
    }

    public static void faceVectorPacketInstant(Vec3 vec) {
        float[] rotations = RotationUtils.getNeededRotations(vec);
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(rotations[0], rotations[1], Minecraft.thePlayer.onGround));
    }
    public static float getYawChange(float yaw, double posX, double posZ) {
        double deltaX = posX - Minecraft.getMinecraft().thePlayer.posX;
        double deltaZ = posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double yawToEntity = 0;
        if ((deltaZ < 0.0D) && (deltaX < 0.0D)) {
        	if(deltaX != 0)
            yawToEntity = 90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else if ((deltaZ < 0.0D) && (deltaX > 0.0D)) {
        	if(deltaX != 0)
            yawToEntity = -90.0D + Math.toDegrees(Math.atan(deltaZ / deltaX));
        } else {
        	if(deltaZ != 0)
            yawToEntity = Math.toDegrees(-Math.atan(deltaX / deltaZ));
        }

        return MathHelper.wrapAngleTo180_float(-(yaw- (float) yawToEntity));
    }

    public static float getPitchChange(float pitch, Entity entity, double posY) {
        double deltaX = entity.posX - Minecraft.getMinecraft().thePlayer.posX;
        double deltaZ = entity.posZ - Minecraft.getMinecraft().thePlayer.posZ;
        double deltaY = posY - 2.2D + entity.getEyeHeight() - Minecraft.getMinecraft().thePlayer.posY;
        double distanceXZ = MathHelper.sqrt_double(deltaX * deltaX + deltaZ * deltaZ);
        double pitchToEntity = -Math.toDegrees(Math.atan(deltaY / distanceXZ));
        return -MathHelper.wrapAngleTo180_float(pitch - (float) pitchToEntity) - 2.5F;
    }

}

