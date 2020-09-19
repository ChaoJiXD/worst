package xyz.WorstClient.module.modules.render;

import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;

public class SetScoreboard extends Module{

	public static Option<Boolean> hideboard = new Option<Boolean>("hideboard","hideboard",false);
	public static Option<Boolean> fastbord = new Option<Boolean>("fastbord","fastbord",false);
	public static Option<Boolean> Norednumber = new Option<Boolean>("Norednumber","Norednumber",false);
	public static Option<Boolean> noServername = new Option<Boolean>("noServername","noServername",false);
	public static Option<Boolean> noanyfont = new Option<Boolean>("noanyfont","noanyfont",false);
	public static Numbers<Double> X = new Numbers<Double>("X", "X", 4.5, 0.0, 1000.0, 1.0);
	public static Numbers<Double> Y = new Numbers<Double>("Y", "Y", 4.5, -300.0, 300.0, 1.0);
	public SetScoreboard() {
		super("Scoreboard", new String[] {"SetScoreboard"},ModuleType.Render);
		this.addValues(this.X, this .Y,this.hideboard ,this.fastbord,this.Norednumber,this.noServername,this.noanyfont);
	}
	
	

}
