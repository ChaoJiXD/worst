/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.api.events.world;

import net.minecraft.network.Packet;
import xyz.WorstClient.api.Event;

public class EventPacketSend
extends Event {
    public Packet packet;

    public EventPacketSend(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}

