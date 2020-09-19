package xyz.WorstClient.command.commands;

import java.util.List;

import xyz.WorstClient.Client;
import xyz.WorstClient.command.Command;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.modules.combat.Hitbox;
import xyz.WorstClient.module.modules.combat.KeepSprint;
import xyz.WorstClient.module.modules.combat.Reach;
import xyz.WorstClient.module.modules.combat.WTap;

public class safemode extends Command{
    public safemode() {
        super("safemode", new String[]{"safemode"}, "", "safemode");
    }

    @Override
    public String execute(String[] args) {
    List<Module> enabled2 = Client.getModuleManager().modules;
    for (Module v : enabled2) {
    	v.setEnabled(false);
    }
    Client.modulemanager.getModuleByClass(Reach.class).setEnabled(true);
    Client.modulemanager.getModuleByClass(WTap.class).setEnabled(true);
    Client.modulemanager.getModuleByClass(Hitbox.class).setEnabled(true);
    Client.modulemanager.getModuleByClass(KeepSprint.class).setEnabled(true);
    
    
    return null;
    }
	
}
