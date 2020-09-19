/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.module.modules.player;

import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.utils.MoveUtils;

public class NoFall
extends Module {
    public static Mode<Enum> mode = new Mode("Mode", "mode", (Enum[])fallmode.values(), (Enum)fallmode.Hypixel);
    double fall;
    public NoFall() {
        super("NoFall", new String[]{"Nofalldamage"}, ModuleType.Player);
        this.addValues(this.mode);
    }
    @Override
    public void onEnable(){
    	fall = 0;
    }
    @EventHandler
    private void onUpdate(EventPreUpdate e) {
    	this.setSuffix(this.mode.getValue());
    	if(this.mode.getValue()==fallmode.Hypixel) {
    		if(!MoveUtils.isOnGround(0.001)){
    			if(mc.thePlayer.motionY < -0.08)
    				fall -= mc.thePlayer.motionY;
    			if(fall > 2){
    				fall = 0;   			
        			e.setOnground(true);
        		}
    		}else
    			fall = 0;
        }
}
    enum fallmode{
    	Hypixel;
    }
}

