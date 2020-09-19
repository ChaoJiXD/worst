package xyz.WorstClient.module.modules.render;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.utils.render.ColorUtils;

public class setColor extends Module{
    public static Numbers<Double> r = new Numbers<Double>("Red", "Red", 255.0, 0.0, 255.0, 1.0);
    public static Numbers<Double> g = new Numbers<Double>("Green", "Green", 255.0, 0.0, 255.0, 1.0);
    public static Numbers<Double> b = new Numbers<Double>("Blue", "Blue", 255.0, 0.0, 255.0, 1.0);
    public static Numbers<Double> a = new Numbers<Double>("Alpha", "Alpha", 255.0, 0.0, 255.0, 1.0);
	public setColor() {
		super("RainbowColor", new String[] {"SetColor"}, ModuleType.Render);
		this.addValues(this.r,this.g,this.b,this.a);
	}
    @EventHandler
    private void onUpdate(EventPreUpdate e)  {
    	r.setValue((double) ColorUtils.getRainbow().getRed());
    	g.setValue((double) ColorUtils.getRainbow().getGreen());
    	b.setValue((double) ColorUtils.getRainbow().getBlue());
        //this.setEnabled(false);
	}
}
