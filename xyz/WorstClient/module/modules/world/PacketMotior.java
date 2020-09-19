
package xyz.WorstClient.module.modules.world;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPacketSend;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.management.ModuleManager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.module.modules.movement.BoostFly;
import xyz.WorstClient.utils.Helper;
import xyz.WorstClient.utils.TimerUtil;

public class PacketMotior
extends Module {
    private int packetcount;
    private TimerUtil time = new TimerUtil();

    public PacketMotior() {
        super("PacketMotior", new String[]{"PacketMotior"}, ModuleType.World);
    }

    @EventHandler
    public void OnUpdate(EventPreUpdate event) {
        if (this.time.delay(1000.0f)) {
            this.setSuffix((Object)("PPS:" + this.packetcount));
            if (this.packetcount > 22) {
                Helper.sendMessage((String)"Packet Warning!!");
            }
            this.packetcount = 0;
            this.time.reset();
        }
    }

    @EventHandler
    public void Packet(EventPacketSend event) {
        if (event.getPacket() instanceof C03PacketPlayer && !ModuleManager.getModuleByClass(BoostFly.class).isEnabled() && !ModuleManager.getModuleByClass(BoostFly.class).isEnabled()) {
            ++this.packetcount;
        }
    }
}

 