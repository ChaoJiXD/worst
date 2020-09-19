/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package xyz.WorstClient.module.modules.world;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.MovementInput;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPacketSend;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;

public class Disabler
extends Module {
    public Numbers<Double> speed = new Numbers<Double>("Disabler_Speed", "Disabler_Speed", 3.0, 1.0, 7.0, 0.1);
    private boolean laggedback;
    private boolean felldown;
    private float[] lastrot = new float[2];

    public Disabler() {
        super("Disabler",new String[] {""}, ModuleType.World);
        this.addValues(this.speed);
    }

    @EventHandler
    public void onPre(EventPreUpdate event) {
            this.setSuffix("Hypixel");
            if (this.felldown) {
                double x = Minecraft.thePlayer.posX;
                double y = Minecraft.thePlayer.posY;
                double z = Minecraft.thePlayer.posZ;
                Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.16, z, true));
                Minecraft.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.07, z, true));
                Minecraft.thePlayer.motionY = Minecraft.thePlayer.movementInput.jump ? this.speed.getValue() : (Minecraft.thePlayer.movementInput.sneak ? - this.speed.getValue().doubleValue() : 0.0);
                if (this.laggedback) {
                    this.setMoveSpeed(this.speed.getValue());
                } else {
                    Minecraft.thePlayer.movementInput.moveForward = 0.0f;
                    Minecraft.thePlayer.movementInput.moveStrafe = 0.0f;
                    event.setYaw(this.lastrot[0]);
                    event.setPitch(this.lastrot[1]);
                    Minecraft.thePlayer.motionX = 0.0;
                    Minecraft.thePlayer.motionZ = 0.0;
                    if (Minecraft.thePlayer.ticksExisted % 2 == 0) {
                        Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.01, Minecraft.thePlayer.posZ);
                    } else {
                        Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY - 0.01, Minecraft.thePlayer.posZ);
                    }
                }
            } else if ((double)Minecraft.thePlayer.fallDistance > 0.0) {
                this.felldown = true;
                Minecraft.thePlayer.motionX = 0.0;
                Minecraft.thePlayer.motionZ = 0.0;
            }
        
    }

    @EventHandler
    public void onPacket(EventPacketSend event) {
        S08PacketPlayerPosLook packet;
        if (this.felldown && !this.mc.isSingleplayer() && this.mc.theWorld != null  && event.packet instanceof S08PacketPlayerPosLook && Minecraft.thePlayer.posY > (packet = (S08PacketPlayerPosLook)event.getPacket()).getY()) {
            this.laggedback = true;
        }
    }

    @Override
    public void onEnable() {
        if (Minecraft.thePlayer != null) {
            this.laggedback = false;
            this.felldown = false;
            this.lastrot[0] = Minecraft.thePlayer.rotationYaw;
            this.lastrot[1] = Minecraft.thePlayer.rotationPitch;
            if (Minecraft.thePlayer.onGround) {
                Minecraft.thePlayer.jump();
            }
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if ( Minecraft.thePlayer != null) {
            Minecraft.thePlayer.motionX = 0.0;
            Minecraft.thePlayer.motionZ = 0.0;
        }
        super.onDisable();
    }

    private void setMoveSpeed(double speed) {
        double forward = Minecraft.thePlayer.movementInput.moveForward;
        double strafe = Minecraft.thePlayer.movementInput.moveStrafe;
        float yaw = Minecraft.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            Minecraft.thePlayer.motionX = 0.0;
            Minecraft.thePlayer.motionZ = 0.0;
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            Minecraft.thePlayer.motionX = forward * speed * (- Math.sin(Math.toRadians(yaw))) + strafe * speed * Math.cos(Math.toRadians(yaw));
            Minecraft.thePlayer.motionZ = forward * speed * Math.cos(Math.toRadians(yaw)) - strafe * speed * (- Math.sin(Math.toRadians(yaw)));
        }
    }
}

