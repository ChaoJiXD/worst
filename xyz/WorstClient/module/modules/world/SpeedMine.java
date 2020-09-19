package xyz.WorstClient.module.modules.world;

import xyz.WorstClient.module.*;
import java.awt.*;
import xyz.WorstClient.api.events.world.*;
import net.minecraft.client.*;
import net.minecraft.potion.*;
import net.minecraft.util.ChatComponentText;
import xyz.WorstClient.api.*;

public class SpeedMine extends Module
{
    public SpeedMine() {
        super("SpeedMine", new String[] { "fb", "antifall" }, ModuleType.World);
        this.setColor(new Color(223, 233, 233).getRGB());
    }
    
    @EventHandler
    private void onUpdate(final EventPreUpdate e) {
        final boolean item = false;
        final Minecraft mc = SpeedMine.mc;
        Minecraft.playerController.blockHitDelay = 0;
        final Minecraft mc2 = SpeedMine.mc;
        if (Minecraft.playerController.curBlockDamageMP < 0.7f) {
        	return;
        }
        final Minecraft mc3 = SpeedMine.mc;
        Minecraft.playerController.curBlockDamageMP = 1.0f;
    }
    
    public void onDisable() {
        super.onDisable();
        final Minecraft mc = SpeedMine.mc;
    }
}
