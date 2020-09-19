/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.command.commands;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import xyz.WorstClient.Client;
import xyz.WorstClient.command.Command;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.ui.Notification;
import xyz.WorstClient.ui.Notification.Type;
import xyz.WorstClient.utils.Helper;

public class Title
extends Command {
    public Title() {
        super("title", new String[]{"list"}, "", "sketit");
    }

    @Override
    public String execute(String[] args) {
    	Client.instance.sendClientMessage("Title set "+ args[0]+" "+args[1], Type.SUCCESS);
    	Client.name=args[0];
    	Client.version=args[1];
    	Display.setTitle(Client.name+" "+Client.version);
        return null;
    }
}