package xyz.WorstClient.module.modules.player;

import xyz.WorstClient.Client;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.misc.EventChat;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.ui.Notification.Type;

public class AntiSpam extends Module {
	int i = 0;

	public AntiSpam() {
		super("AntiSpam", new String[] { "fuckNMSL" }, ModuleType.World);
	}

	@EventHandler
	public void onChat(EventChat e) {
		if (e.getMessage().contains("NMSL") || e.getMessage().contains("cnm") || e.getMessage().contains("nmsl")
				|| e.getMessage().contains("NM$L") || e.getMessage().contains("nm$l") || e.getMessage().contains("fw")
				|| e.getMessage().contains("FW") || e.getMessage().contains("Fw") || e.getMessage().contains("Nmsl")
				|| e.getMessage().contains("Nm$l") || e.getMessage().contains("lj") || e.getMessage().contains("LJ")
				|| e.getMessage().contains("hax") || e.getMessage().contains("配置᳡") || e.getMessage().contains("FW")
				|| e.getMessage().contains("你妈死了") || e.getMessage().contains("婊子") || e.getMessage().contains("你妈")
				|| e.getMessage().contains("[Hanabi]") || e.getMessage().contains("杀妈") || e.getMessage().contains("biss")
				|| e.getMessage().contains("[Azureware]") || e.getMessage().contains("购买") || e.getMessage().contains("全家")
				|| e.getMessage().contains("随便") || e.getMessage().contains("脑瘫") || e.getMessage().contains("gck")
				|| e.getMessage().contains("客户端") || e.getMessage().contains("RMB") || e.getMessage().contains("wdnmd")
				|| e.getMessage().contains("配置") || e.getMessage().contains("CNY") || e.getMessage().contains("废物")
				|| e.getMessage().contains("免费获取") || e.getMessage().contains("内部")
				|| e.getMessage().contains("加Q群") || e.getMessage().contains("恼羞成怒")
				|| e.getMessage().contains("maikama.cn") || e.getMessage().contains("绕16")
				|| e.getMessage().contains("黑客") || e.getMessage().contains("绕狗") || e.getMessage().contains("绕过")
				|| e.getMessage().contains("脑残") || e.getMessage().contains("稳定") || e.getMessage().contains("操")
				|| e.getMessage().contains("你妈死了") || e.getMessage().contains("url.cn")
				|| e.getMessage().contains("anpaiba.top") || e.getMessage().contains("杀妈")
				|| e.getMessage().contains("nm sl") || e.getMessage().contains("https://") || e.getMessage().contains("sb")
				|| e.getMessage().contains("外挂") || e.getMessage().contains("官网") || e.getMessage().contains("QQ群")
				|| e.getMessage().contains("LLL") || e.getMessage().contains("fW") || e.getMessage().contains("Client")) {
			i++;
			Client.sendClientMessage("一个玩家正在滥用聊天功能。", Type.WARNING);
			if (i < 2) {
				this.setSuffix("Time:" + i);
			} else {
				this.setSuffix("Times:" + i);
			}
		}
	}

	@Override
	public void onDisable() {
		this.setSuffix("");
		i = 0;
	}
}
