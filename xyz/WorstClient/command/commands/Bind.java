/*
 * Decompiled with CFR 0_132.
 * 
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 */
package xyz.WorstClient.command.commands;

import org.lwjgl.input.Keyboard;

import xyz.WorstClient.Client;
import xyz.WorstClient.command.Command;
import xyz.WorstClient.management.ModuleManager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.ui.Notification;
import xyz.WorstClient.utils.Helper;

public class Bind
extends Command {
    public Bind() {
        super("Bind", new String[]{"b"}, "", "sketit");
    }

    @Override
    public String execute(String[] args) {
        if (args.length >= 2) {
            Module m = Client.instance.getModuleManager().getAlias(args[0]);
            if (m != null) {
                int k = Keyboard.getKeyIndex((String)args[1].toUpperCase());
                m.setKey(k);
                Object[] arrobject = new Object[2];
                arrobject[0] = m.getName();
                arrobject[1] = k == 0 ? "none" : args[1].toUpperCase();
            	  Client.instance.sendClientMessage(String.format("Bound %s to %s", arrobject), Notification.Type.SUCCESS);
            } else {
            	  Client.instance.sendClientMessage("Invalid module name, double check spelling.", Notification.Type.ERROR);
            }
        } else {
      	  Client.instance.sendClientMessage(".bind <module> <key>", Notification.Type.ERROR);
        }
        return null;
    }
}

