/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.module.modules.movement;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Timer;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;

public class Step
extends Module {
    private Numbers<Double> height = new Numbers<Double>("Height", "height", 1.0, 1.0, 10.0, 0.5);
    private Mode<Enum> mode = new Mode("Mode", "mode", (Enum[])StepMode.values(), (Enum)StepMode.NCP);

    public Step() {
        super("Step", new String[]{"autojump"}, ModuleType.Movement);
        this.setColor(new Color(165, 238, 65).getRGB());
        this.addValues(this.height,this.mode);
    }

    @Override
    public void onDisable() {
        this.mc.thePlayer.stepHeight = 0.6f;
    }

    @EventHandler
    private void onUpdate(EventPreUpdate e) {
        if (this.mode.getValue()==StepMode.NCP) {
        	ncpStep(mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY);
        }if(this.mode.getValue()==StepMode.Vanlia){
        	ncpStep(mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY);
            //this.mc.thePlayer.stepHeight = this.height.getValue().intValue();
        }
    }
    void ncpStep(double height){
    	List<Double>offset = Arrays.asList(0.42,0.333,0.248,0.083,-0.078);
    	double posX = mc.thePlayer.posX; double posZ = mc.thePlayer.posZ;
    	double y = mc.thePlayer.posY;
    	if(height < 1.1){
    		double first = 0.42;
    		double second = 0.75;
    		if(height != 1){
    			first *= height;
    			second *= height;
        		if(first > 0.425){
        			first = 0.425;
        		}
        		if(second > 0.78){
        			second = 0.78;
        		}
        		if(second < 0.49){
        			second = 0.49;
        		}
    		}
    		if(first == 0.42)
    			first = 0.41999998688698;
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + first, posZ, false));
    		if(y+second < y + height)
    		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + second, posZ, false));
    		return;
    	}else if(height <1.6){
    		for(int i = 0; i < offset.size(); i++){
        		double off = offset.get(i);
        		y += off;
        		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y, posZ, false));
        	}
    	}else if(height < 2.1){
    		double[] heights = {0.425,0.821,0.699,0.599,1.022,1.372,1.652,1.869};
			for(double off : heights){
        		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
        	}
    	}else{
        	double[] heights = {0.425,0.821,0.699,0.599,1.022,1.372,1.652,1.869,2.019,1.907};
        	for(double off : heights){
        		mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
        	}
    	}
    	
    }
    enum StepMode{
    	NCP,
    	Vanlia;
    }
}

