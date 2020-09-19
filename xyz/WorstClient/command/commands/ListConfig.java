/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.command.commands;

import java.io.IOException;

import xyz.WorstClient.command.Command;
import xyz.WorstClient.utils.Helper;
import xyz.WorstClient.utils.WebUtils;

public class ListConfig
extends Command {
    public ListConfig() {
        super("ListConfig", new String[]{"Listconfig"}, "", "listConfig");
    }

    @Override
    public String execute(String[] args) {
    	String List="";
    	try {
			List=WebUtils.get("http://alxb.com.cn/Worst/read.php");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
    	Helper.sendMessageWithoutPrefix(List);
        return null;
    }
}

