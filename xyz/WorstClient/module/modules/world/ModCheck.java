package xyz.WorstClient.module.modules.world;
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
public class ModCheck 
extends Module{
	private String[] modlist = new String[] {"mxu", "造化钟神秀", "startover_", "chen_xixi","tanker_01", "Owenkill", "bingmo", "小阿狸","绅士龙","SnowDay"};
	private String modname;
	private TimerUtil timer = new TimerUtil();
	private ArrayList<String> offlinemod = new ArrayList();
	private ArrayList<String> onlinemod = new ArrayList();
    private Option<Boolean> showOffline = new Option<Boolean>("ShowOffline", "ShowOffline", true);
    private Option<Boolean> showOnline = new Option<Boolean>("ShowOnline", "ShowOnline", true);
    private Numbers<Double> x = new Numbers<Double>("x", "X", 50.0, 0.0, 1000.0, 10.0);
    private Numbers<Double> y = new Numbers<Double>("y", "Y", 50.0, 0.0, 1000.0, 10.0);
	
	private int counter;
	private boolean isFinished;
	public  ModCheck () {
		super("ModCheck", new String[]{"LlllLL,LLLLL"}, ModuleType.World);
		this.addValues(this.showOffline,this.showOnline);
		this.addValues(this.x,this.y);
	}


	
	@EventHandler
	public void onRender(EventRender2D e) {
		FontRenderer font = mc.fontRendererObj;
		RenderUtils.drawRect(4, 123,60,130, new Color(0,155,255,230).getRGB());
		font.drawString("Mods:"+onlinemod.size()+"/"+modlist.length, 4, 122, new Color(255,255,255,230).getRGB());
		
		List<String> listArray = Arrays.asList(modlist);
		listArray.sort((o1, o2) -> {
			return font.getStringWidth(o2) - font.getStringWidth(o1);
		});
		int counter2 = 0;
		for(String mods : listArray) {
			if(offlinemod.contains(mods) && showOffline.getValue()) {
				RenderUtils.drawRect(4, 130 + counter2 * 10,60, 140 + counter2 * 10, new Color(255,255,255,150).getRGB());
				font.drawStringWithShadow(mods, 5, 130 + counter2 * 10, Color.RED.getRGB());
				
				counter2++;
			}
			if(onlinemod.contains(mods) && showOnline.getValue()) {
				RenderUtils.drawRect(4, 130 + counter2 * 10,60, 140 + counter2 * 10, new Color(255,255,255,150).getRGB());
				font.drawStringWithShadow(mods, 5, 130 + counter2 * 10, Color.GREEN.getRGB());
				counter2++;
			}
			
		}
	}
	
	@EventHandler
	public void onChat(EventChat e) {
		if(e.getMessage().contains("这名玩家不在线！")) {
			e.setCancelled(true);
			if(onlinemod.contains(modname)) {
				onlinemod.remove(modname);
				offlinemod.add(modname);
				return;
			}
			if(!offlinemod.contains(modname)) {
				offlinemod.add(modname);
			}
		}
		
		
		if(e.getMessage().contains("这名玩家不在线！") || e.getMessage().contains("That player is not online!")) {
			e.setCancelled(true);
			if(onlinemod.contains(modname)) {
				onlinemod.remove(modname);
				offlinemod.add(modname);
				return;
			}
			if(!offlinemod.contains(modname)) {
				offlinemod.add(modname);
			}
		}
		
		if(e.getMessage().contains("You cannot message this player.")) {
			e.setCancelled(true);
			if(offlinemod.contains(modname)) {
				offlinemod.remove(modname);
				onlinemod.add(modname);
				return;
			}
			if(!onlinemod.contains(modname)) {
				onlinemod.add(modname);
			}
		}

		if(e.getMessage().contains("找不到名为 \"" + modname + "\" 的玩家")) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onUpdate(EventPreUpdate e) {
		if(timer.hasReached(isFinished ? 10000L : 2000L)) {
			if(counter >= modlist.length) {
				counter = -1;
				if(!isFinished) {
					isFinished = true;
				}
				
			}
			counter++;
			modname = modlist[counter];
			mc.thePlayer.sendChatMessage("/message " + modname + " hi");
			timer.reset();
		}
	}
}
