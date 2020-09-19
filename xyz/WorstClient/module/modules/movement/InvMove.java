package xyz.WorstClient.module.modules.movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;

public class InvMove extends Module{

	public InvMove() {
		super("InvMove", new String[] {}, ModuleType.Movement);
	}
	
	@EventHandler
	public void onUpdate(EventPreUpdate event) {
		if (this.mc.currentScreen != null && !(this.mc.currentScreen instanceof GuiChat)) {
			KeyBinding[] key = { this.mc.gameSettings.keyBindForward, this.mc.gameSettings.keyBindBack, this.mc.gameSettings.keyBindLeft, this.mc.gameSettings.keyBindRight, this.mc.gameSettings.keyBindSprint, this.mc.gameSettings.keyBindJump };
			KeyBinding[] array;
			for (int length = (array = key).length, i = 0; i < length; ++i) {
				KeyBinding b = array[i];
				KeyBinding.setKeyBindState(b.getKeyCode(), Keyboard.isKeyDown(b.getKeyCode()));
			}
		}
	}
}
