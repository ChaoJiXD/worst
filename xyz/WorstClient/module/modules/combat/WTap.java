package xyz.WorstClient.module.modules.combat;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPacketSend;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;

public class WTap
extends Module {

    public WTap() {
        super("WTap", new String[]{"WTaper"}, ModuleType.Combat);
    }

    @EventHandler
    private void onTick(EventPacketSend e) {
        C02PacketUseEntity packet;
        if (e.getType() == 2 && e.getPacket() instanceof C02PacketUseEntity && Minecraft.thePlayer != null && (packet = (C02PacketUseEntity)e.getPacket()).getAction() == C02PacketUseEntity.Action.ATTACK && packet.getEntityFromWorld(Minecraft.theWorld) != Minecraft.thePlayer && Minecraft.thePlayer.getFoodStats().getFoodLevel() > 6) {
            boolean sprint = Minecraft.thePlayer.isSprinting();
            Minecraft.thePlayer.setSprinting(false);
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Minecraft.thePlayer, C0BPacketEntityAction.Action.STOP_SPRINTING));
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C0BPacketEntityAction(Minecraft.thePlayer, C0BPacketEntityAction.Action.START_SPRINTING));
            Minecraft.thePlayer.setSprinting(sprint);
        }
    }
}

