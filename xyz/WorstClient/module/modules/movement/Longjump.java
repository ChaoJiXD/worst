/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.module.modules.movement;

import java.awt.Color;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventMove;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.utils.math.MathUtil;

public class Longjump
extends Module {
    private Mode<JumpMode> mode = new Mode("Mode", "mode", (Enum[])JumpMode.values(), (Enum)JumpMode.NCP);
    private int stage;
    private double moveSpeed;
    private double lastDist;

    public Longjump() {
        super("LongJump", new String[]{"lj", "jumpman", "jump"}, ModuleType.Movement);
        this.addValues(this.mode);
        this.setColor(new Color(76, 67, 216).getRGB());
    }

    @Override
    public void onDisable() {
        this.mc.timer.timerSpeed = 1.0f;
        if (this.mode.getValue() == JumpMode.Area51) {
            this.mc.thePlayer.motionX = 0.0;
            this.mc.thePlayer.motionZ = 0.0;
        }
        if (this.mc.thePlayer != null) {
            this.moveSpeed = this.getBaseMoveSpeed();
        }
        this.lastDist = 0.0;
        this.stage = 0;
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        this.setSuffix((Object)this.mode.getValue());
        if (this.mode.getValue() == JumpMode.OldGuardian) {
            if (this.mc.thePlayer.moving() && this.mc.thePlayer.onGround) {
                this.mc.thePlayer.motionY = 0.44;
                this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-9, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
                this.mc.thePlayer.setSpeed(7.0);
            } else {
                this.mc.thePlayer.setSpeed(Math.sqrt(this.mc.thePlayer.motionX * this.mc.thePlayer.motionX + this.mc.thePlayer.motionZ * this.mc.thePlayer.motionZ));
            }
        } else if (this.mode.getValue() == JumpMode.Area51) {
            if (this.mc.thePlayer.moving()) {
                this.mc.timer.timerSpeed = 0.33f;
                if (this.mc.thePlayer.onGround) {
                    this.mc.thePlayer.setSpeed(5.0);
                    this.mc.thePlayer.motionY = 0.45500001311302185;
                } else {
                    this.mc.thePlayer.setSpeed(7.0);
                }
            } else {
                this.mc.timer.timerSpeed = 0.33f;
                this.mc.thePlayer.motionX = 0.0;
                this.mc.thePlayer.motionZ = 0.0;
            }
        } else if (this.mode.getValue() == JumpMode.Janitor && e.getType() == 0 && this.mc.thePlayer.moving() && this.mc.thePlayer.onGround) {
            e.setY(e.getY() + (this.mc.thePlayer.ticksExisted % 2 == 0 ? MathUtil.getHighestOffset(0.1) : 0.0));
        } else if (e.getType() == 0) {
            double xDist = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
            double zDist = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
    }

    @EventHandler
    private void onMove(EventMove e) {
        if (this.mode.getValue() == JumpMode.NCP) {
            if (this.mc.thePlayer.moveStrafing <= 0.0f && this.mc.thePlayer.moveForward <= 0.0f) {
                this.stage = 1;
            }
            if (this.stage == 1 && (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f)) {
                this.stage = 2;
                this.moveSpeed = 3.0 * this.getBaseMoveSpeed() - 0.01;
            } else if (this.stage == 2) {
                this.stage = 3;
                this.mc.thePlayer.motionY = 0.424;
                EventMove.y = 0.424;
                this.moveSpeed *= 2.149802;
            } else if (this.stage == 3) {
                this.stage = 4;
                double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
                this.moveSpeed = this.lastDist - difference;
            } else {
                if (this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically) {
                    this.stage = 1;
                }
                this.moveSpeed = this.lastDist - this.lastDist / 159.0;
            }
            this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
            MovementInput movementInput = this.mc.thePlayer.movementInput;
            float forward = movementInput.moveForward;
            float strafe = movementInput.moveStrafe;
            float yaw = this.mc.thePlayer.rotationYaw;
            if (forward == 0.0f && strafe == 0.0f) {
                EventMove.x = 0.0;
                EventMove.z = 0.0;
            } else if (forward != 0.0f) {
                if (strafe >= 1.0f) {
                    yaw += (float)(forward > 0.0f ? -45 : 45);
                    strafe = 0.0f;
                } else if (strafe <= -1.0f) {
                    yaw += (float)(forward > 0.0f ? 45 : -45);
                    strafe = 0.0f;
                }
                if (forward > 0.0f) {
                    forward = 1.0f;
                } else if (forward < 0.0f) {
                    forward = -1.0f;
                }
            }
            double mx = Math.cos(Math.toRadians(yaw + 90.0f));
            double mz = Math.sin(Math.toRadians(yaw + 90.0f));
            EventMove.x = (double)forward * this.moveSpeed * mx + (double)strafe * this.moveSpeed * mz;
            EventMove.z = (double)forward * this.moveSpeed * mz - (double)strafe * this.moveSpeed * mx;
            if (forward == 0.0f && strafe == 0.0f) {
                EventMove.x = 0.0;
                EventMove.z = 0.0;
            } else if (forward != 0.0f) {
                if (strafe >= 1.0f) {
                    yaw += (float)(forward > 0.0f ? -45 : 45);
                    strafe = 0.0f;
                } else if (strafe <= -1.0f) {
                    yaw += (float)(forward > 0.0f ? 45 : -45);
                    strafe = 0.0f;
                }
                if (forward > 0.0f) {
                    forward = 1.0f;
                } else if (forward < 0.0f) {
                    forward = -1.0f;
                }
            }
        } else if (this.mode.getValue() == JumpMode.Janitor && this.mc.thePlayer.moving()) {
            this.moveSpeed = MathUtil.getBaseMovementSpeed() * (double)(this.mc.thePlayer.ticksExisted % 2 != 0 ? 5 : 6);
            double x = - Math.sin(this.mc.thePlayer.getDirection()) * this.moveSpeed;
            double z = Math.cos(this.mc.thePlayer.getDirection()) * this.moveSpeed;
            e.setX(x);
            e.setZ(z);
            if (this.mc.thePlayer.onGround) {
                this.mc.thePlayer.motionY = 0.3;
                e.setY(0.3);
            }
        } else if (this.mode.getValue() == JumpMode.Guardian && this.mc.thePlayer.moving()) {
            if (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f) {
                if (this.mc.thePlayer.onGround) {
                    int i = 0;
                    while (i < 20) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-9, this.mc.thePlayer.posZ, this.mc.thePlayer.onGround));
                        ++i;
                    }
                    this.mc.thePlayer.motionY = 0.4;
                    EventMove.y = 0.4;
                    this.mc.thePlayer.setSpeed(8.0);
                }
            } else {
                this.mc.thePlayer.motionX = 0.0;
                this.mc.thePlayer.motionZ = 0.0;
            }
        }
    }

    double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (this.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (double)(amplifier + 1);
        }
        return baseSpeed;
    }

    static enum JumpMode {
        NCP,
        OldGuardian,
        Guardian,
        Janitor,
        Area51;
    }

}

