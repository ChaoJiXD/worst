/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.module.modules.movement;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.Timer;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.utils.PlayerUtil;
import xyz.WorstClient.utils.TimerUtil;

public class Boost
extends Module {
    private TimerUtil timer = new TimerUtil();

    public Boost() {
        super("Boost", new String[]{"boost"}, ModuleType.Movement);
        this.setColor(new Color(216, 253, 100).getRGB());
    }

    @EventHandler
    public void onUpdate(EventPreUpdate event) {
        this.mc.timer.timerSpeed =  2.5f;
        if (this.mc.thePlayer.ticksExisted % 30 == 0) {
            this.setEnabled(false);
        }
    }

    @Override
    public void onDisable() {
        this.timer.reset();
        this.mc.timer.timerSpeed = 1.0f;
    }
}

