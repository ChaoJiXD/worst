package xyz.WorstClient.module.modules.render;

import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;

public class Animations extends Module{
    public static Mode<Enum> mode = new Mode("Mode", "mode", (Enum[])renderMode.values(), (Enum)renderMode.ETB);
    public static Option<Boolean> smooth = new Option<Boolean>("Smooth", "Smooth", false);
	public Animations() {
		super("Animations", new String[] {"BlockHitanimations"}, ModuleType.Render);
		this.addValues(this.mode,this.smooth);
	}
	
	@EventHandler
	private void onUpdate(EventPreUpdate event) {
		this.setSuffix(mode.getValue().name());
	}
	
	public static enum renderMode {
		ETB,
		Slide,
		Avatar,
		Vanlia,
		Jigsaw,
		Jello,
		Sigma,
		Swang,
		Swong,
		Swank;
	}
}
