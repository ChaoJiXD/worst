/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.module.modules.movement;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPreUpdate;
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
}

