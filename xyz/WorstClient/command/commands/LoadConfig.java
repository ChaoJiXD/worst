/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.command.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import xyz.WorstClient.Client;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.command.Command;
import xyz.WorstClient.management.FileManager;
import xyz.WorstClient.management.ModuleManager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.ui.Notification;
import xyz.WorstClient.ui.Notification.Type;
import xyz.WorstClient.utils.Helper;
import xyz.WorstClient.utils.WebUtils;

public class LoadConfig
extends Command {
    public LoadConfig() {
        super("LoadCouldConfig", new String[]{"LoadCouldConfig"}, "", "LoadCouldConfig");
    }

    @Override
    public String execute(String[] args) {
    	//Value
    	String Value="";
    	try {
    		Value=WebUtils.get("http://alxb.com.cn/Worst/"+args[0]+"/Values.txt");
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
    	
    	FileManager.save("Values.txt", Value, false);
    	
        List<String> vals = FileManager.read("Values.txt");
        for (String v : vals) {
            String name = v.split(":")[0];
            String values = v.split(":")[1];
            Module m = ModuleManager.getModuleByName(name);
            if (m == null) continue;
            for (Value value : m.getValues()) {
                if (!value.getName().equalsIgnoreCase(values)) continue;
                if (value instanceof Option) {
                    value.setValue(Boolean.parseBoolean(v.split(":")[2]));
                    continue;
                }
                if (value instanceof Numbers) {
                    value.setValue(Double.parseDouble(v.split(":")[2]));
                    continue;
                }
                ((Mode)value).setMode(v.split(":")[2]);
            }
        }
    	Helper.sendMessage("Values整好了");

    	
    	String Binds="";
    	try {
    		Binds=WebUtils.get("http://alxb.com.cn/Worst/"+args[0]+"/Binds.txt");
		} catch (IOException e) {
			// TODO 操你妈
			e.printStackTrace();
		}
    	
    	FileManager.save("Binds.txt", Binds, false);
    	
        List<String> binds = FileManager.read("Binds.txt");
        for (String v : binds) {
            String name = v.split(":")[0];
            String bind = v.split(":")[1];
            Module m = ModuleManager.getModuleByName(name);
            if (m == null) continue;
            m.setKey(Keyboard.getKeyIndex((String)bind.toUpperCase()));
        }

    	Helper.sendMessage("Binds自己整");
    	//Helper.sendMessage("Binds:"+Binds);
    	
    	Client.mc.thePlayer.sendChatMessage(".safemode");
    	String Enabled="";
    	try {
			Enabled = WebUtils.get("http://alxb.com.cn/Worst/"+args[0]+"/Enabled.txt");
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
    	
    	FileManager.save("Enabled.txt", Enabled, false);
    	Helper.sendMessage("开冲gogogo");
        List<String> enabled = FileManager.read("Enabled.txt");
        for (String v : enabled) {
            Module m = ModuleManager.getModuleByName(v);
            m.setEnabled(true);
        }
        
    	Helper.sendMessage("开冲吧"+args[0]+"哥哥");
        return null;
        
    }
}