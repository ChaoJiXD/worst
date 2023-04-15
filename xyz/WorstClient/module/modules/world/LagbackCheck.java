package xyz.WorstClient.module.modules.world;

import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import xyz.WorstClient.Client;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPacketRecieve;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.management.ModuleManager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.module.modules.movement.Flight;
import xyz.WorstClient.module.modules.movement.Longjump;
import xyz.WorstClient.module.modules.movement.Scaffold;
import xyz.WorstClient.module.modules.movement.Speed;
import xyz.WorstClient.ui.Notification;

public class LagbackCheck extends Module{
    private Option<Boolean> boostflight = new Option<Boolean>("BoostFly lagback", "BoostFly lagback", true);
    private Option<Boolean> flight = new Option<Boolean>("Flight lagback", "Flight lagback", true);
    private Option<Boolean> speed = new Option<Boolean>("Speed lagback", "Speed lagback", true);
    private Option<Boolean> scaffold = new Option<Boolean>("Scaffold lagback", "Scaffold lagback", true);
    private Option<Boolean> longjump = new Option<Boolean>("Longjump lagback", "Longjump lagback", true);
    int sb;
	public LagbackCheck(){
	      super("LagBackCheck", new String[]{"Lagback","LagbackCheck"}, ModuleType.World);
	      this.addValues(this.boostflight,this.flight,this.speed,this.scaffold,this.longjump);
	   }
	@EventHandler
	   private void Onputee(EventPacketRecieve event) {
	         if(!(event.getPacket() instanceof S08PacketPlayerPosLook)) {
	            return;
	         }
	         Client var10000 = Client.instance;
	         Client.getModuleManager();
	         Flight flight;
	         if(this.flight.getValue().booleanValue()) {
	         if((flight = (Flight)ModuleManager.getModuleByClass(Flight.class)).isEnabled()) {
	            flight.setEnabled(false);
				Client.instance.sendClientMessage("(lagbackcheck)Flight Disable", Notification.Type.ERROR);
	         }
	         }
	         var10000 = Client.instance;
	         Client.getModuleManager();
	         var10000 = Client.instance;
	         Client.getModuleManager();
	         Speed speed;
	         if(this.speed.getValue().booleanValue()) {
	         if((speed = (Speed)ModuleManager.getModuleByClass(Speed.class)).isEnabled()) {
	            speed.setEnabled(false);
				Client.instance.sendClientMessage("(lagbackcheck)Speed Disable", Notification.Type.ERROR);
	         }
	         }
	         var10000 = Client.instance;
	         Client.getModuleManager();
	         Scaffold scaffold = (Scaffold)ModuleManager.getModuleByClass(Scaffold.class);
	         if(this.scaffold.getValue().booleanValue()) {
	         if(scaffold.isEnabled()) {
	            scaffold.setEnabled(false);
				Client.instance.sendClientMessage("(lagbackcheck)Scaffold Disable", Notification.Type.ERROR);
	         }}
	         var10000 = Client.instance;
	         Client.getModuleManager();
	         Longjump longjump;
	         if(this.longjump.getValue().booleanValue()) {
	         if((longjump = (Longjump)ModuleManager.getModuleByClass(Longjump.class)).isEnabled()) {
	            longjump.setEnabled(false);
				Client.instance.sendClientMessage("(lagbackcheck)LongJump Disable", Notification.Type.ERROR);
	         }}
	      }
	}
