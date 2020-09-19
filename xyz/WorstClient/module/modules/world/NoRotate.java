/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.module.modules.world;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPacketSend;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;

public class NoRotate
extends Module {
    public NoRotate() {
        super("NoRotate", new String[]{"rotate"}, ModuleType.World);
        this.setColor(new Color(17, 250, 154).getRGB());
    }
    @EventHandler
    private void onPacket(EventPacketSend e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook look = (S08PacketPlayerPosLook)e.getPacket();
            look.yaw = this.mc.thePlayer.rotationYaw;
            look.pitch = this.mc.thePlayer.rotationPitch;
        }
    }
}

