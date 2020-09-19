package xyz.WorstClient.module.modules.world;

import java.awt.Color;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.MathHelper;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.rendering.EventRender2D;
import xyz.WorstClient.management.ModuleManager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;

public class MemoryFix extends Module{
	private int width;
	public MemoryFix() {
		super("MemoryFix", new String[] {"MemoryFix"}, ModuleType.World);
	}
    @Override
    public void onEnable() {
    	Runtime.getRuntime().gc();
    	super.onEnable();
    }
}
