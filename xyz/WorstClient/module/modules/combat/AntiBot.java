package xyz.WorstClient.module.modules.combat;

import net.minecraft.entity.player.*;
import xyz.WorstClient.Client;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.ui.Notification.Type;
import xyz.WorstClient.utils.Helper;
import xyz.WorstClient.utils.TimerUtil;
import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.entity.item.*;
import java.util.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.network.*;

public class AntiBot extends Module
{
    public Mode<Enum> mode;
    public static ArrayList<EntityPlayer> Bots;
    private TimerUtil timer;
    public static List<EntityPlayer> invalid = new ArrayList();
    private static List<EntityPlayer> removed = new ArrayList();
    int bots;
    
    static {
        AntiBot.Bots = new ArrayList<EntityPlayer>();
        
    }
    
    public AntiBot() {
        super("AntiBot", new String[] { "AntiBot" }, ModuleType.Combat);
        this.mode = new Mode<Enum>("Mode", "Mode", AntiMode.values(), AntiMode.Hypixel);
        this.timer = new TimerUtil();
        this.bots = 0;
        this.addValues(this.mode);

    }
    
    @EventHandler
    public void onUpdate(final EventPreUpdate event) {
        this.setSuffix(this.mode.getValue());
        if (this.mode.getValue() == AntiMode.Hypixel) {
            final Minecraft mc2 = AntiBot.mc;
            for (final Object entities : Minecraft.theWorld.loadedEntityList) {
                if (entities instanceof EntityPlayer) {
                    final EntityPlayer entityPlayer2;
                    final EntityPlayer entity = entityPlayer2 = (EntityPlayer)entities;
                    final Minecraft mc3 = AntiBot.mc;
                    if (entityPlayer2 != Minecraft.thePlayer) {
                        final Minecraft mc4 = AntiBot.mc;
						if (mc.thePlayer.getDistanceToEntity(entity) < 10) {
							if (!entity.getDisplayName().getFormattedText().contains("ยง") || entity.isInvisible() //startwith
									|| entity.getDisplayName().getFormattedText().toLowerCase().contains("npc")
									|| entity.getDisplayName().getFormattedText().toLowerCase().contains("§r")) {
                            AntiBot.Bots.add(entity);
                        }
                    }
                    if (!AntiBot.Bots.contains(entity)) {
                        continue;
                    }
                    AntiBot.Bots.remove(entity);
                }
            }
            this.bots = 0;
            for (final Entity entity2 : Minecraft.theWorld.getLoadedEntityList()) {
                if (entity2 instanceof EntityPlayer) {
                    final EntityPlayer entityPlayer;
                    final EntityPlayer ent = entityPlayer = (EntityPlayer)entity2;
                    final Minecraft mc = AntiBot.mc;
                    if (entityPlayer == Minecraft.thePlayer) {
                        continue;
                    }
                    if (!ent.isInvisible()) {
                        continue;
                    }
                    if (ent.ticksExisted <= 105) {
                        continue;
                    }
                    if (getTabPlayerList().contains(ent)) {
                        if (!ent.isInvisible()) {
                            continue;
                        }
                        ent.setInvisible(false);
                    }
                    else {
                        ent.setInvisible(false);
                        ++this.bots;
                        Minecraft.theWorld.removeEntity(ent);
                    }
                }
            }
            if (this.bots != 0) {
            	Client.sendClientMessage("[BotKiller]Kill "+String.valueOf(this.bots) + " Bots",Type.INFO);
            }
        }
        }
    }
    
    public boolean isInGodMode(final Entity entity) {
        return this.isEnabled() && this.mode.getValue() == AntiMode.Hypixel && entity.ticksExisted <= 25;
    }
    
	public boolean isServerBot(Entity entity) {
		if (this.isEnabled()) {
			if (this.mode.getValue() == AntiMode.Hypixel) {
				if (entity.getDisplayName().getFormattedText().startsWith("\u00a7") && !entity.isInvisible()
						&& !entity.getDisplayName().getFormattedText().toLowerCase().contains("[NPC]")) {
					if(isInGodMode(entity))
					{
						return true;
					}
					return false;
				}
				return true;
			}

			if (this.mode.getValue() == AntiMode.Mineplex) {
				for (Object object : this.mc.theWorld.playerEntities) {
					EntityPlayer entityPlayer = (EntityPlayer) object;
					if (entityPlayer == null || entityPlayer == this.mc.thePlayer
							|| !entityPlayer.getName().startsWith("Body #") && entityPlayer.getMaxHealth() != 20.0f)
						continue;
					return true;
				}
			}
		}
		return false;
	}
    
    @Override
    public void onEnable() {
        AntiBot.Bots.clear();
    }
    
    @Override
    public void onDisable() {
        AntiBot.Bots.clear();
    }
    
    public static List<EntityPlayer> getTabPlayerList() {
        final Minecraft mc = AntiBot.mc;
        final NetHandlerPlayClient var4 = Minecraft.thePlayer.sendQueue;
        final ArrayList list = new ArrayList();
        final List players = GuiPlayerTabOverlay.field_175252_a.sortedCopy((Iterable)var4.getPlayerInfoMap());
        for (final Object o : players) {
            final NetworkPlayerInfo info = (NetworkPlayerInfo)o;
            if (info == null) {
                continue;
            }
            final ArrayList list2 = list;
            final Minecraft mc2 = AntiBot.mc;
            list2.add(Minecraft.theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
        }
        return (List<EntityPlayer>)list;
    }
    
    enum AntiMode
    {
        Hypixel("Hypixel", 0), 
        Mineplex("Mineplex", 1);
        
        private AntiMode(final String s, final int n) {
        }
    }
}
