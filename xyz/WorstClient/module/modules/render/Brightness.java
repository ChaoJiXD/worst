/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.module.modules.render;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.events.world.EventTick;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;

public class Brightness
extends Module {
    private float old;

    public Brightness() {
        super("Brightness", new String[]{"fbright", "brightness", "bright"}, ModuleType.Render);
        this.setColor(new Color(244, 255, 149).getRGB());
    }

    @Override
    public void onEnable() {
        this.old = this.mc.gameSettings.gammaSetting;
    }

    @EventHandler
    private void onTick(EventPreUpdate e) {
    	mc.thePlayer.addPotionEffect(new PotionEffect(Potion.nightVision.getId(), 4000, 1));
    }

    @Override
    public void onDisable() {
        this.mc.gameSettings.gammaSetting = this.old;
    }
}

