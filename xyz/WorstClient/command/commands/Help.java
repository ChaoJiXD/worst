/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.command.commands;

import java.io.IOException;

import xyz.WorstClient.command.Command;
import xyz.WorstClient.utils.Helper;
import xyz.WorstClient.utils.WebUtils;

public class Help
extends Command {
    public Help() {
        super("Help", new String[]{"list"}, "", "sketit");
    }

    @Override
    public String execute(String[] args) {
    	String List="";
    	try {
			List=WebUtils.get("");
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
    	
        if (args.length == 0) {
            Helper.sendMessageWithoutPrefix("\u00a77\u00a7m\u00a7l----------------------------------");
            Helper.sendMessageWithoutPrefix("                    \u00a7b\u00a7lChaoJi Client");
            Helper.sendMessageWithoutPrefix("\u00a7b.help >\u00a77 list commands");
            Helper.sendMessageWithoutPrefix("\u00a7b.bind >\u00a77 bind a module to a key");
            Helper.sendMessageWithoutPrefix("\u00a7b.t >\u00a77 toggle a module on/off");
            Helper.sendMessageWithoutPrefix("\u00a7b.friend >\u00a77 friend a player");
            Helper.sendMessageWithoutPrefix("\u00a7b.cheats >\u00a77 list all modules");
            Helper.sendMessageWithoutPrefix("\u00a7b.LoadCouldConfig >\u00a77 load a Could config");
            Helper.sendMessageWithoutPrefix("\u00a77\u00a7m\u00a7l----------------------------------");
            Helper.sendMessageWithoutPrefix("ConfigList:"+List);
        } else {
            Helper.sendMessage("invalid syntax Valid .help");
        }
        return null;
    }
}

