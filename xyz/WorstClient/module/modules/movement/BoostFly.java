
package xyz.WorstClient.module.modules.movement;

import java.awt.Color;

import xyz.WorstClient.Client;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventMove;
import xyz.WorstClient.api.events.world.EventPacketRecieve;
import xyz.WorstClient.api.events.world.EventPostUpdate;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.management.ModuleManager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.utils.PlayerUtil;
import xyz.WorstClient.utils.Timer;
import xyz.WorstClient.utils.TimerUtil;
import xyz.WorstClient.utils.math.MathUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovementInput;



public class BoostFly extends Module
{
    public static Mode mode = new Mode("Mode", "mode", (Enum[])BoostFly.FlightMode.values(), (Enum)BoostFly.FlightMode.HypixelZoom);;
    private TimerUtil timer = new TimerUtil();;
    private double movementSpeed;
    private int hypixelCounter;
    private int hypixelCounter2;
   public static Numbers<Number> VanillaSpeed = new Numbers<Number>("VanillaSpeed", "VanillaSpeed",  4.5, 1.0, 10.0, 1.0);
   private Option<Boolean> lagcheck = new Option<Boolean>("LagCheck", "LagCheck", true); 
   int counter;
    int level;
    double moveSpeed;
    double lastDist;
    boolean b2;
	private TimerUtil deactivationDelay = new TimerUtil();
	private int packetCounter;
	@EventHandler
	private void onPacket(EventPacketRecieve ep) {
		if (this.lagcheck.getValue().booleanValue()) {
			if (ep.getPacket() instanceof S08PacketPlayerPosLook && this.deactivationDelay.delay(2000F)) {
				++this.packetCounter;
				S08PacketPlayerPosLook pac = (S08PacketPlayerPosLook) ep.getPacket();
				pac.yaw = mc.thePlayer.rotationYaw;
				pac.pitch = mc.thePlayer.rotationPitch;
				this.level = -5;
			}
		}
	}
    public int V() {
        if (Minecraft.thePlayer.isPotionActive(Potion.jump)) {
            return Minecraft.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        }
        return 0;
    }
    
    public BoostFly() {
        super("Fly", new String[] { "ZoomFly", "BoostFly" ,"Fly"}, ModuleType.Movement); 
        this.setColor(new Color(158, 114, 243).getRGB());
        this.addValues(new Value[] { (Value)this.mode ,this.lagcheck,this.VanillaSpeed});
    }
    
    public void damagePlayer(int damage) {
		for (int index = 0; index <49; index++) {
  	    	mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.06249D, mc.thePlayer.posZ, false));
  	    	mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
  	    }
		mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
    }
    
    
    public void onEnable() {
        if (this.mode.getValue() == BoostFly.FlightMode.Hypixel || this.mode.getValue() == BoostFly.FlightMode.HypixelZoom) {
            if (this.mode.getValue() == BoostFly.FlightMode.HypixelZoom) {
                this.damagePlayer(1);
            }
            this.hypixelCounter = 0;
            this.hypixelCounter2 = 1000;
            Minecraft.thePlayer.motionY = 0.41999999999875 + this.V() * 0.1;
        }
        if(ModuleManager.getModuleByName("Scaffold").isEnabled()){
        	ModuleManager.getModuleByName("Scaffold").setEnabled(false);
        }
        this.level = 1;
        this.moveSpeed = 0.1;
        this.b2 = true;
        this.lastDist = 0.0;
    }
    
    public void onDisable() {
        this.hypixelCounter = 0;
        this.hypixelCounter2 = 100;
        final net.minecraft.util.Timer timer = this.mc.timer;
        timer.timerSpeed=1.0f;
        this.level = 1;
        this.moveSpeed = 0.1;
        this.b2 = false;
        this.lastDist = 0.0;
    }
    
    @EventHandler
    private void onUpdate(final EventPreUpdate e) {
        this.setSuffix(this.mode.getValue());
        if (this.mode.getValue() == FlightMode.Vanilla) {
            
            final EntityPlayerSP thePlayer8 = Minecraft.thePlayer;
            
            double motionY;
            if (Minecraft.thePlayer.movementInput.jump) {
                motionY = 1.0;
            }
            else {
                
                motionY = (Minecraft.thePlayer.movementInput.sneak ? -1.0 : 0.0);
            }
            thePlayer8.motionY = motionY;
           
            if (Minecraft.thePlayer.moving()) {
               
                Minecraft.thePlayer.setSpeed((double)this.VanillaSpeed.getValue());
            }
            else {
                final Minecraft mc12 = mc;
                Minecraft.thePlayer.setSpeed(0.0);
            }
        }
        else if (this.mode.getValue() == BoostFly.FlightMode.Hypixel || this.mode.getValue() == BoostFly.FlightMode.HypixelZoom) {
            ++this.counter;
            
            if (Minecraft.thePlayer.moveForward == 0.0f) {
                
                if (Minecraft.thePlayer.moveStrafing == 0.0f) {
                    
                    final EntityPlayerSP thePlayer4 = Minecraft.thePlayer;
                    
                    final double n = Minecraft.thePlayer.posX + 1.0;
                    
                    final double n2 = Minecraft.thePlayer.posY + 1.0;
                    
                    thePlayer4.setPosition(n, n2, Minecraft.thePlayer.posZ + 1.0);
                    
                    final EntityPlayerSP thePlayer5 = Minecraft.thePlayer;
                    
                    final double prevPosX = Minecraft.thePlayer.prevPosX;
                    
                    final double prevPosY = Minecraft.thePlayer.prevPosY;
                    
                    thePlayer5.setPosition(prevPosX, prevPosY, Minecraft.thePlayer.prevPosZ);
                    
                    Minecraft.thePlayer.motionX = 0.0;
                    
                    Minecraft.thePlayer.motionZ = 0.0;
                }
            }
            
            Minecraft.thePlayer.motionY = 0.0;
            if (Minecraft.getMinecraft().gameSettings.keyBindJump.pressed) {
                
                final EntityPlayerSP thePlayer6 = Minecraft.thePlayer;
                thePlayer6.motionY += 0.5;
            }
            if (Minecraft.getMinecraft().gameSettings.keyBindSneak.pressed) {
                
                final EntityPlayerSP thePlayer7 = Minecraft.thePlayer;
                thePlayer7.motionY -= 0.5;
            }
            if (this.counter != 1 && this.counter == 2) {
                
                final EntityPlayerSP thePlayer8 = Minecraft.thePlayer;
                
                final double posX = Minecraft.thePlayer.posX;
                
                final double n3 = Minecraft.thePlayer.posY + 1.0E-10;
                
                thePlayer8.setPosition(posX, n3, Minecraft.thePlayer.posZ);
                this.counter = 0;
            }
        }
    }
    
    @EventHandler
    public void onPost(final EventPostUpdate e) {
        if (this.mode.getValue() == BoostFly.FlightMode.Hypixel || this.mode.getValue() == BoostFly.FlightMode.HypixelZoom) {
            
            final double posX = Minecraft.thePlayer.posX;
            
            final double xDist = posX - Minecraft.thePlayer.prevPosX;
            
            final double posZ = Minecraft.thePlayer.posZ;
            
            final double zDist = posZ - Minecraft.thePlayer.prevPosZ;
            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
        }
    }
    
    @EventHandler
    private void onMove(final EventMove e) {
        if (this.mode.getValue() == BoostFly.FlightMode.Hypixel || this.mode.getValue() == BoostFly.FlightMode.HypixelZoom) {
            final float forward = MovementInput.moveForward;
            final float strafe = MovementInput.moveStrafe;
            final float yaw = Minecraft.thePlayer.rotationYaw;
            final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
            final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
            if (forward == 0.0f && strafe == 0.0f) {
                EventMove.x = 0.0;
                EventMove.z = 0.0;
            }
            if (this.b2) {
                Label_0393: {
                    Label_0137: {
                        if (this.level == 1) {
                            
                            if (Minecraft.thePlayer.moveForward == 0.0f) {
                                
                                if (Minecraft.thePlayer.moveStrafing == 0.0f) {
                                    break Label_0137;
                                }
                            }
                            this.level = 2;
                            
                            int n;
                            if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
                                
                                n = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
                            }
                            else {
                                n = 0;
                            }
                            final int amplifier = n;
                            
                            final double boost = Minecraft.thePlayer.isPotionActive(Potion.moveSpeed) ? 1.56 : 2.034;
                            this.moveSpeed = boost * MathUtil.getBaseMovementSpeed();
                            break Label_0393;
                        }
                    }
                    if (this.level == 2) {
                        this.level = 3;
                        this.moveSpeed *= 2.1399;
                    }
                    else if (this.level == 3) {
                        this.level = 4;
                        final double difference = ((Minecraft.thePlayer.ticksExisted % 2 == 0) ? 0.0103 : 0.0123) * (this.lastDist - MathUtil.getBaseMovementSpeed());
                        this.moveSpeed = this.lastDist - difference;
                    }
                    else {
                        
                        final WorldClient theWorld = Minecraft.theWorld;
                        
                        final EntityPlayerSP thePlayer = Minecraft.thePlayer;
                        
                        final AxisAlignedBB boundingBox = Minecraft.thePlayer.boundingBox;
                        final double n2 = 0.0;
                        
                        Label_0291: {
                            if (theWorld.getCollidingBoundingBoxes((Entity)thePlayer, boundingBox.offset(n2, Minecraft.thePlayer.motionY, 0.0)).size() <= 0) {
                                
                                if (!Minecraft.thePlayer.isCollidedVertically) {
                                    break Label_0291;
                                }
                            }
                            this.level = 1;
                        }
                        this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                    }
                }
                final double moveSpeed = (this.mode.getValue() == BoostFly.FlightMode.HypixelZoom) ? Math.max(this.moveSpeed, MathUtil.getBaseMovementSpeed()) : MathUtil.getBaseMovementSpeed();
                this.moveSpeed = moveSpeed;
                final double d = moveSpeed;
                if (strafe == 0.0f) {
                    EventMove.x = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
                    EventMove.z = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
                }
                else if (strafe != 0.0f) {
                    Minecraft.thePlayer.setMoveSpeed(e, this.moveSpeed);
                }
                if (forward == 0.0f && strafe == 0.0f) {
                    EventMove.x = 0.0;
                    EventMove.z = 0.0;
                }
            }
        }
    }
    
    double getBaseMoveSpeed() {
        double baseSpeed = 0.275;
        if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = Minecraft.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    public static enum FlightMode{
    	
		HypixelZoom, Hypixel,Vanilla
    	
    }
}