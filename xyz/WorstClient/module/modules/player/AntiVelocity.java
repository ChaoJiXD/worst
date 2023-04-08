
package xyz.WorstClient.module.modules.player;

import java.awt.Color;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPacketRecieve;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.module.modules.movement.Speed;

public class AntiVelocity
extends Module {
    public static Mode<Enum> mode = new Mode("Mode", "Mode", (Enum[]) kbmode.values(), (Enum) kbmode.hypixel);
    public Numbers<Double> percent = new Numbers("percent","percent",0.0 , 0.0 ,42.0,42.0);

    public AntiVelocity() {
        super("Velocity", new String[]{"antivelocity", "antiknockback", "antikb"}, ModuleType.Player);
        this.setColor(new Color(191, 191, 191).getRGB());
        this.addValues(mode,percent);
    }

    @EventHandler
    private void onPacket(EventPacketRecieve e) {
        S12PacketEntityVelocity pkt = (S12PacketEntityVelocity)e.getPacket();
        if (e.getPacket() instanceof S12PacketEntityVelocity || e.getPacket() instanceof S27PacketExplosion) {
            if (mode.getValue() == kbmode.hypixel && mc.thePlayer.onGround) {
                pkt.motionX = 0;
                pkt.motionZ = 0;
            } else if (mode.getValue() == kbmode.zero) {
                e.setCancelled(true);
            } else if (mode.getValue() == kbmode.percent){
                pkt.motionX = (int)(this.percent.getValue() / 100.0);
                pkt.motionY = (int)(this.percent.getValue() / 100.0);
                pkt.motionZ = (int)(this.percent.getValue() / 100.0);
            }

            /*  if (this.percentage.getValue().equals(0.0)) {
                e.setCancelled(true);
            } else {
                S12PacketEntityVelocity packet = (S12PacketEntityVelocity)e.getPacket();
                packet.motionX = (int)(this.percentage.getValue() / 100.0);
                packet.motionY = (int)(this.percentage.getValue() / 100.0);
                packet.motionZ = (int)(this.percentage.getValue() / 100.0);
            }
        }*/
        }
    }
        static enum kbmode {
            zero,
            percent,
            hypixel;

        }

}

