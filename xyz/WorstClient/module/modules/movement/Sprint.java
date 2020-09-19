
package xyz.WorstClient.module.modules.movement;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.FoodStats;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;

public class Sprint
extends Module {
    private Option<Boolean> omni = new Option<Boolean>("Omni-Directional", "omni", true);

    public Sprint() {
        super("Sprint", new String[]{"run"}, ModuleType.Movement);
        this.setColor(new Color(158, 205, 125).getRGB());
        this.addValues(this.omni);
    }

    @EventHandler
    private void onUpdate(EventPreUpdate event) {
        if (this.mc.thePlayer.getFoodStats().getFoodLevel() > 6 && this.omni.getValue() != false ? this.mc.thePlayer.moving() : this.mc.thePlayer.moveForward > 0.0f) {
            this.mc.thePlayer.setSprinting(true);
        }
    }
}

