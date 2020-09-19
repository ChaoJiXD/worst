
package xyz.WorstClient.module.modules.world;
import net.minecraft.client.Minecraft;
import xyz.WorstClient.Client;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.misc.EventChat;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.module.modules.combat.Killaura;
import xyz.WorstClient.module.modules.movement.Flight;
import xyz.WorstClient.module.modules.movement.Flight.FlightMode;
import xyz.WorstClient.module.modules.render.HUD;
import xyz.WorstClient.ui.Notification.Type;
import xyz.WorstClient.utils.TimerUtil;

public class AutoL
extends Module {
	public static Option<Boolean> autowdr = new Option<Boolean>("AutoWDR", "AutoWDR", false);
    public AutoL() {
        super("AutoL", new String[]{"AutoL"}, ModuleType.World);
        this.addValues(this.autowdr);
    }
	private TimerUtil timer = new TimerUtil();
    int num=0;
    public boolean Start =false;
    
    private void onUpdate(EventPreUpdate event) {
    	if(Start == true) {
    		Flight Flight = new Flight();
    				Flight.setEnabled(true);
    		Flight.mode.setValue(FlightMode.Vanilla);
    		if(timer.hasReached(1000L)) {
    			num++;
    			if(num>=4.9) {
    				new Flight().setEnabled(false);
    			}
    			timer.reset();
    		}
    	}
    	
    	
    }
    

    
@EventHandler
private void onChat(EventChat e)
{

      
  if (e.getMessage().contains(Killaura.curTarget.getName()+"��"))
  {
    Minecraft.thePlayer.sendChatMessage("[Worst]"+Killaura.curTarget.getName() + " Why not buy Worst Client at chaoji.maikama.cn" );
    if(this.autowdr.getValue().booleanValue()) {
        Minecraft.thePlayer.sendChatMessage("/wdr "+Killaura.curTarget.getName()+" ka fly speed nokb reach jesus");
    }
    e.setCancelled(true);
  }
  }
}


