/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.command.commands;

import java.io.IOException;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import xyz.WorstClient.Client;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.command.Command;
import xyz.WorstClient.management.FileManager;
import xyz.WorstClient.management.ModuleManager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.ui.Notification;
import xyz.WorstClient.ui.Notification.Type;
import xyz.WorstClient.utils.Helper;
import xyz.WorstClient.utils.WebUtils;

public class UploadConfig
extends Command {
    public UploadConfig() {
        super("UploadConfig", new String[]{"ListConfig"}, "", "ListConfig");
    }

    @Override
    public String execute(String[] args) {

    	
        String values = "";
        Client.instance.getModuleManager();
        for (Module m : ModuleManager.getModules()) {
            for (Value v : m.getValues()) {
                values = String.valueOf(values) + String.format("%s:%s:%s%s", m.getName(), v.getName(), v.getValue(), System.lineSeparator());
            }
        }
        FileManager.save("Values.txt", values, false);
        
        String values2 = "";
        Client.instance.getModuleManager();
        for (Module m : ModuleManager.getModules()) {
            for (Value v : m.getValues()) {
                values2 = String.valueOf(values2) + String.format("%s:%s:%s%s", m.getName(), v.getName(), v.getValue(), "[p]");
            }
        }
        
        String enabled = "";
        Client.instance.getModuleManager();
        for (Module m : ModuleManager.getModules()) {
            if (!m.isEnabled()) continue;
            enabled = String.valueOf(enabled) + String.format("%s%s", m.getName(), System.lineSeparator());
        }
        FileManager.save("Enabled.txt", enabled, false);
        
        String enabled2 = "";
        Client.instance.getModuleManager();
        for (Module m : ModuleManager.getModules()) {
            if (!m.isEnabled()) continue;
            enabled2 = String.valueOf(enabled2) + String.format("%s%s", m.getName(),"[p]");
        }
        
        
        String binds = "";
        Client.instance.getModuleManager();
        for (Module m : ModuleManager.getModules()) {
            if (!m.isEnabled()) continue;
            binds = String.valueOf(binds) + String.format("%s%s", m.getName(), "[p]");
        }
        FileManager.save("Enabled.txt", enabled, false);
        
        
        String f="";
    	try {
			f=WebUtils.get("http://alxb.com.cn/Worst/upconfig.php?Name="+args[0]+"&content=[Binds]"+binds+"[Enabled]"+enabled2+"[Values]"+values2.replaceAll("\r|\n", "").replaceAll(" ", "")+"[Worst]");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
    	Helper.sendMessage("上传完成,配置名:"+args[0]);
        return null;
    }
}