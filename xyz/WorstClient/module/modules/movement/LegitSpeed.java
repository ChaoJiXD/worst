package xyz.WorstClient.module.modules.movement;

import xyz.WorstClient.module.Module;
import xyz.WorstClient.api.events.world.EventJump;
import xyz.WorstClient.module.ModuleType;
import net.minecraft.potion.Potion;
public class LegitSpeed extends Module {
    public LegitSpeed() {
        super("LegitSpeed", new String[]{"legitspeed"}, ModuleType.Movement);
    }
    public void motion(EventJump e){

        if (mc.thePlayer == null || getSpeedEffect() <= 0 || mc.thePlayer.moveForward <= 0.0f || !mc.thePlayer.isSprinting() || getSpeedDuration() <= 20) {
            return;
        }


        mc.thePlayer.motionX *= (double)(1.0f + (float)getSpeedEffect() * 0.15f);
        mc.thePlayer.motionZ *= (double)(1.0f + (float)getSpeedEffect() * 0.15f);


    }

    public static int getSpeedEffect() {
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }

    public static int getSpeedDuration() {
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getDuration();
        }
        return 0;
    }

}


