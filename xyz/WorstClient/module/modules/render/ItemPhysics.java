package xyz.WorstClient.module.modules.render;

import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;

public class ItemPhysics extends Module{

	public static boolean active;
	
	public ItemPhysics() {
        super("ItemPhysics",new String[]{"ItemPhysics"},ModuleType.Render);
	}

@Override
public void onDisable() {
	active=false;
	super.onDisable();
}
	
	 @Override
	public void onEnable() {
		active=true;
		super.onEnable();
	}
	 
	public String getValue() {
		return null;
	}



}
