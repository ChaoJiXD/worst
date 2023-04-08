/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.module.modules.movement;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPacketRecieve;
import xyz.WorstClient.api.events.world.EventPostUpdate;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.events.world.EventTarget;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.utils.Helper;
import xyz.WorstClient.utils.MoveUtils;
import xyz.WorstClient.utils.PlayerUtil;

public class NoSlowDown
        extends Module {
    public NoSlowDown() {
        super("NoSlowDown", new String[]{"noslowdown"}, ModuleType.Movement);
    }
    @EventTarget
    public void onPreEvent(EventPreUpdate e){
        if(doNoSlow()){
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange((Minecraft.thePlayer.inventory.currentItem + 1) % 9));
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(Minecraft.thePlayer.inventory.currentItem));
        }

    }
    @EventTarget
    public void packet(EventPacketRecieve event) {
        if (event.getPacket() instanceof S30PacketWindowItems && (mc.thePlayer.isUsingItem() || mc.thePlayer.isBlocking()) && mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            event.setCancelled(true);
        }

    }

    public boolean doNoSlow(){
        return Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword && Minecraft.thePlayer.moving();
    }
}

