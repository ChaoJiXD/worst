package xyz.WorstClient.module.modules.player;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xyz.WorstClient.Client;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.misc.EventChat;
import xyz.WorstClient.api.events.rendering.EventRender2D;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.management.FriendManager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.utils.RenderUtils;
import xyz.WorstClient.utils.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.EntityLiving;
public class Spammer 
extends Module{

	private TimerUtil timer = new TimerUtil();

	public  Spammer () {
		super("Spammer", new String[]{"LlllLL,LLLLL"}, ModuleType.World);

	}


	
	int delay =100;
	int l=0;
	@EventHandler
	public void onUpdate(EventPreUpdate e) {
		if(timer.hasReached(delay)) {
			l++;
			if(delay==100) {
				if(l>=8) {
					l=0;
					delay=5000;
				}
			}
			if(delay==5000) {
				if(l>=2) {
					l=0;
					delay=100;
				}
			}
				
			mc.thePlayer.sendChatMessage("欢迎购买Worst客户端，超级稳定，购买链接→[chaoji.maikama.cn]"+100+Math.random()*100);
			timer.reset();
		}
	}
}
