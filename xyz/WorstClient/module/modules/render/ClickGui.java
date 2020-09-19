/*
 * Decompiled with CFR 0.136.
 */
package xyz.WorstClient.module.modules.render;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.ui.Waitz.ClickyUI;
import xyz.WorstClient.ui.Worst.Clickui;
import xyz.WorstClient.utils.Helper;
import xyz.WorstClient.utils.Wrapper;


public class ClickGui
extends Module {
	public static Mode<Enum> mode = new Mode("Mode", "mode", (Enum[])mod.values(), (Enum)mod.List);

	
    public ClickGui() {
        super("Clickgui", new String[]{"clickui"}, ModuleType.Render);
        this.addValues(this.mode);
    }

    @Override
    public void onEnable() {
    	if(this.mode.getValue()==mod.List) {
        Wrapper.mc.displayGuiScreen(new ClickyUI());
        this.setEnabled(false);
    }
    	if(this.mode.getValue()==mod.CsGo) {
    		Helper.sendMessage("本模式正在开发中");
    		this.mode.setValue(mod.List);
            //Wrapper.mc.displayGuiScreen(new Clickui());
            this.setEnabled(false);
        }
    }

   public enum mod{
	   List,
	   CsGo
	
   }
}

