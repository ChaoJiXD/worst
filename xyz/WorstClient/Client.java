
package xyz.WorstClient;

import java.awt.TrayIcon.MessageType;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.management.CommandManager;
import xyz.WorstClient.management.FileManager;
import xyz.WorstClient.management.FriendManager;
import xyz.WorstClient.management.ModuleManager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.modules.combat.Killaura;
import xyz.WorstClient.module.modules.render.UI.TabUI;
import xyz.WorstClient.ui.Notification;
import xyz.WorstClient.ui.Notification.Type;
import xyz.WorstClient.ui.fontRenderer.FontManager;
import xyz.WorstClient.utils.HWIDUtils;
import xyz.WorstClient.utils.RenderUtils;
import xyz.WorstClient.utils.WebUtils;

import static net.minecraft.client.main.Main.discordRP;


public class Client {
    public static String name = "worst reborn";
    public static String version = "firstver/2023";
    public DiscordRP getDiscordRP() {
        return discordRP;
    }
    public String PassWord;
    public String UserName;
    public static boolean publicMode = false;
    public static Client instance = new Client();
    public static ModuleManager modulemanager;
    private CommandManager commandmanager;
    private FriendManager friendmanager;
	public FontManager fontMgr;
	public static FontManager fontManager;
	private static ArrayList<Notification> notifications = new ArrayList<>();
    public static Minecraft mc = Minecraft.getMinecraft();
    private TabUI tabui;
	public static boolean paiduser;
    public void initiate() {
		fontManager = fontMgr = new FontManager();
        this.commandmanager = new CommandManager();
        this.commandmanager.init();
        this.friendmanager = new FriendManager();
        this.friendmanager.init();
        this.modulemanager = new ModuleManager();
        this.modulemanager.init();
        this.tabui = new TabUI();
        this.tabui.init();
        FileManager.init();
        
    }

    public static boolean isInteger(String str) {  
          Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
          return pattern.matcher(str).matches();  
    }
    

    
    public static ModuleManager getModuleManager() {
        return modulemanager;
    }

    public CommandManager getCommandManager() {
        return this.commandmanager;
    }

	public void drawNotifications() {
		ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
		double startY = res.getScaledHeight() - 25;
		final double lastY = startY;
		for (int i = 0; i < notifications.size(); i++) {
			Notification not = notifications.get(i);
			if (not.shouldDelete())
				notifications.remove(i);
			not.draw(startY, lastY);
			startY -= not.getHeight() + 1;
		}
	}
	public static void sendClientMessage(String message, Type type) {
		notifications.add(new Notification(message, type));
	}
    public void shutDown() {
        String values = "";
        instance.getModuleManager();
        for (Module m : ModuleManager.getModules()) {
            for (Value v : m.getValues()) {
                values = String.valueOf(values) + String.format("%s:%s:%s%s", m.getName(), v.getName(), v.getValue(), System.lineSeparator());
            }
        }
        FileManager.save("Values.txt", values, false);
        String enabled = "";
        instance.getModuleManager();
        for (Module m : ModuleManager.getModules()) {
            if (!m.isEnabled()) continue;
            enabled = String.valueOf(enabled) + String.format("%s%s", m.getName(), System.lineSeparator());
        }
        FileManager.save("Enabled.txt", enabled, false);
    }

    public static BlockPos getBlockCorner(BlockPos start, BlockPos end) {
        for(int x = 0; x <= 1; ++x) {
           for(int y = 0; y <= 1; ++y) {
              for(int z = 0; z <= 1; ++z) {
                 BlockPos pos = new BlockPos(end.getX() + x, end.getY() + y, end.getZ() + z);
                 if (!isBlockBetween(start, pos)) {
                    return pos;
                 }
              }
           }
        }

        return null;
     }


    public static boolean isBlockBetween(BlockPos start, BlockPos end) {
        int startX = start.getX();
        int startY = start.getY();
        int startZ = start.getZ();
        int endX = end.getX();
        int endY = end.getY();
        int endZ = end.getZ();
        double diffX = (double)(endX - startX);
        double diffY = (double)(endY - startY);
        double diffZ = (double)(endZ - startZ);
        double x = (double)startX;
        double y = (double)startY;
        double z = (double)startZ;
        double STEP = 0.1D;
        int STEPS = (int)Math.max(Math.abs(diffX), Math.max(Math.abs(diffY), Math.abs(diffZ))) * 4;

        for(int i = 0; i < STEPS - 1; ++i) {
           x += diffX / (double)STEPS;
           y += diffY / (double)STEPS;
           z += diffZ / (double)STEPS;
           if (x != (double)endX || y != (double)endY || z != (double)endZ) {
              BlockPos pos = new BlockPos(x, y, z);
              Block block = mc.theWorld.getBlockState(pos).getBlock();
              if (block.getMaterial() != Material.air && block.getMaterial() != Material.water && !(block instanceof BlockVine) && !(block instanceof BlockLadder)) {
                 return true;
              }
           }
        }

        return false;
     }

    public static void RenderRotate(float yaw) {
        final Minecraft mc = Killaura.mc;
        Minecraft.thePlayer.renderYawOffset = yaw;
        final Minecraft mc2 = Killaura.mc;
        Minecraft.thePlayer.rotationYawHead = yaw;

     }
}

