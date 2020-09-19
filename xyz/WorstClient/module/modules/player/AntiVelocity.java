
package xyz.WorstClient.module.modules.player;

import java.awt.Color;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPacketRecieve;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;

public class AntiVelocity
extends Module {
    private Numbers<Double> percentage = new Numbers<Double>("Percentage", "percentage", 0.0, 0.0, 100.0, 5.0);

    public AntiVelocity() {
        super("Velocity", new String[]{"antivelocity", "antiknockback", "antikb"}, ModuleType.Player);
        this.addValues(this.percentage);
        this.setColor(new Color(191, 191, 191).getRGB());
    }

    @EventHandler
    private void onPacket(EventPacketRecieve e) {
        if (e.getPacket() instanceof S12PacketEntityVelocity || e.getPacket() instanceof S27PacketExplosion) {
            if (this.percentage.getValue().equals(0.0)) {
                e.setCancelled(true);
            } else {
                S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
                packet.motionX = (int)(this.percentage.getValue() / 100.0);
                packet.motionY = (int)(this.percentage.getValue() / 100.0);
                packet.motionZ = (int)(this.percentage.getValue() / 100.0);
            }
        }
    }
}

