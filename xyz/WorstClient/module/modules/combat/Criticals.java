/*
 * Decompiled with CFR 0_132.
 */
package xyz.WorstClient.module.modules.combat;


import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import xyz.WorstClient.Client;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventMove;
import xyz.WorstClient.api.events.world.EventPacketRecieve;
import xyz.WorstClient.api.events.world.EventPacketSend;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.management.ModuleManager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.module.modules.movement.BoostFly;
import xyz.WorstClient.module.modules.movement.Scaffold;
import xyz.WorstClient.module.modules.movement.Speed;
import xyz.WorstClient.utils.Helper;
import xyz.WorstClient.utils.TimerUtil;
import xyz.WorstClient.utils.math.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings.Options;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.ChatComponentText;

public class Criticals
extends Module {
    public Mode mode = new Mode("Mode", "mode", (Enum[])CritMode.values(), (Enum)CritMode.HypixelPacket);
    private boolean N;
    private boolean M;
    private TimerUtil timer = new TimerUtil();
    private static List<EntityPlayer> invalid = new ArrayList();
    private static List<EntityPlayer> removed = new ArrayList();
	private static int[] var1;
    private EventPacketRecieve event;
	private int packetCounter;
    public static int  aacCount;
    public static Option<Boolean> NoSpeed = new Option("NoSpeed", "NoSpeed", Boolean.valueOf(false));
	private static int stage;
    public Criticals() {
        super("Criticals", new String[]{"crits", "crit"}, ModuleType.Combat);
        this.setColor(new Color(235, 194, 138).getRGB());
        this.addValues(this.mode,this.NoSpeed);
    }
    public void onEnable() {
        invalid.clear();

        
    }

    public void onDisable() {
        invalid.clear();

    }
    //@EventHandler
   // private void onMove(EventMove e10) {
    //	Scaffold i3 = (Scaffold)Client.instance.getModuleManager().getModuleByName("Scaffold");
	//	if(i3.isEnabled() && i3.Lag.getValue()) {
		//	return;
    @EventHandler
    private void onMove(EventMove e50) {
    	if (this.NoSpeed.getValue().booleanValue()) {
    		Speed i3 = (Speed)Client.instance.getModuleManager().getModuleByName("Speed");
    					Client.instance.getModuleManager().getModuleByName("Speed").setEnabled(false);
    			

    			}
				}
    	
		

    

	
    @EventHandler
    private void onUpdate(EventPreUpdate e10) {
        this.setSuffix(this.mode.getValue());
        
        if(mode.getValue() == CritMode.NoGround) {
            if(mc.thePlayer.onGround && !Minecraft.getMinecraft().gameSettings.keyBindAttack.isKeyDown()) {
            	e10.setOnground(false);
            }else {
            	e10.setOnground(true);
            }
        }
        if (mode.getValue() == CritMode.Always) {
        	e10.setOnground(false);
        }

    }

    private boolean canCrit() {
        if (this.mc.thePlayer.onGround && !this.mc.thePlayer.isInWater() && !Client.instance.getModuleManager().getModuleByClass(Speed.class).isEnabled() 
        		&& !Client.instance.getModuleManager().getModuleByClass(BoostFly.class).isEnabled() && !Client.instance.getModuleManager().getModuleByClass(BoostFly.class).isEnabled()) {
            return true;
        }
        return false;
    }
    

    
    @EventHandler
    private void onPacket(EventPacketSend e10) {
    	
    	
    	
    	
		if(this.mode.getValue() == CritMode.Hypixel&&canCrit()) {

			double[] offsets = new double[]{0.0625, 0.0, 1.0E-4, 0.0};
            for (int i2 = 0; i2 < offsets.length; ++i2) {
            }
            this.timer.reset();
        }
    
		
	      

        if (e10.getPacket() instanceof C02PacketUseEntity && this.canCrit() && this.mode.getValue() == CritMode.Minijumps) {
            Minecraft.thePlayer.motionY = 0.2;
        }
    }

    void packetCrit() {
        if (this.timer.hasReached(Helper.onServer("hypixel") ? 500 : 10) && this.mode.getValue() == CritMode.HypixelPacket && this.canCrit()) {
            double[] offsets = new double[]{0.0625, 0.0, 1.0E-4, 0.0};
            for (int i2 = 0; i2 < offsets.length; ++i2) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + offsets[i2], Minecraft.thePlayer.posZ, false));
            }
            this.timer.reset();
        }
    }
    void HypixelCrit() {
    	if (this.timer.hasReached(Helper.onServer("hypixel") ? 500 : 10) && this.mode.getValue() == CritMode.HypixelNew && this.canCrit()) {
    	double[] offsets = { 0.05250000001304D, 0.00150000001304D, 0.01400000001304D, 0.00150000001304D };
    	/* 168 */     for (int i = 0; i < offsets.length; i++) {
    	/* 169 */       Minecraft.getMinecraft(); EntityPlayerSP p = Minecraft.thePlayer;
    	/* 170 */       p.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(p.posX, p.posY + offsets[i], p.posZ, false));
    	}
    }
    }

    void offsetCrit() {
        if (this.canCrit() && !Criticals.mc.getCurrentServerData().serverIP.contains("hypixel")) {
            double[] offsets = new double[]{0.0624, 0.0, 1.0E-4, 0.0};
            for (int i2 = 0; i2 < offsets.length; ++i2) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + offsets[i2], Minecraft.thePlayer.posZ, false));
            }
        }
    }

 
    void RemixCrits() {
    	if (this.timer.hasReached(500L) && isOnGround(0.072) && this.canCrit() && this.mode.getValue() == CritMode.Edit) {
            final double[] var5;
            final double[] offsets = var5 = new double[] { 0.051 * MathUtil.randomDouble(1.08d, 1.1d), 0.0, 0.0125 * MathUtil.randomDouble(1.01d, 1.07d), 0.0 };
            for (int var6 = offsets.length, var7 = 0; var7 < var6; ++var7) {
                final double v = var5[var7];
                System.out.print("C08");
                final Minecraft mc = Criticals.mc;
                final NetHandlerPlayClient sendQueue = Minecraft.thePlayer.sendQueue;
                final Minecraft mc2 = Criticals.mc;
                final double posX = Minecraft.thePlayer.posX;
                final Minecraft mc3 = Criticals.mc;
                final double posY = Minecraft.thePlayer.posY + v;
                final Minecraft mc4 = Criticals.mc;
                sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, Minecraft.thePlayer.posZ, false));
            }
            this.timer.reset();
        }
    }
    public static boolean isOnGround(final double height) {
        Minecraft.getMinecraft();
        final WorldClient theWorld = Minecraft.theWorld;
        Minecraft.getMinecraft();
        final EntityPlayerSP thePlayer = Minecraft.thePlayer;
        Minecraft.getMinecraft();
        return !theWorld.getCollidingBoundingBoxes(thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0, -height, 0.0)).isEmpty();
    }

    void AACCrits() {
        double x = Minecraft.thePlayer.posX;
        double y = Minecraft.thePlayer.posY;
        double z = Minecraft.thePlayer.posZ;
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.06253, z, false));
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.06254, z, false));
        Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.05, z, false));
        this.timer.reset();
    
    }
	   
    

    

	   private static int GetRandomNumber(int n, int n2) {
		      return (int)(Math.random() * (double)(n - n2)) + n2;
		   }
	   private static int randomNumber(int max, int min) {
		      return (int)(Math.random() * (double)(max - min)) + min;
		   }

		 void newCrit() {
			   if(mode.getValue() == CritMode.NewPacket && canCrit()) {
				double offset1 = (double) (randomNumber(-9999, 9999) / 10000000);
				double offset2 = (double) (randomNumber(-9999, 9999) / 1000000000);
				double[] var5 = new double[] { 0.0624218713251234D + offset1, 0.0D, 1.0834773E-5D + offset2, 0.0D };
				int var6 = var5.length;

				for (int var7 = 0; var7 < var6; ++var7) {
					double offset = var5[var7];
					mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
							mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
				}
		}

		 }
		 
		 void autoCrit() {
				this.hypixelCrit();
			 this.RemixCrits();
         this.packetCrit();
         this.newCrit();
         this.AACCrits();
         this.ExCrit();
         this.PacketOldCrit();

         

		 }
		    public void hypixelCrit() {
		        if (this.mode.getValue() == CritMode.HypixelTest && this.canCrit()) {
		            C02PacketUseEntity packet;
		            Speed speed = (Speed)ModuleManager.getModuleByName("Speed");
		      
		            if ((speed.isEnabled() && (packet = (C02PacketUseEntity)this.event.getPacket()).getAction() == C02PacketUseEntity.Action.ATTACK)) {
		                Minecraft.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.1625, Minecraft.thePlayer.posZ, false));
		            }
		            for (double offset : new double[]{0.06142999976873398, 0.0, 0.012511000037193298, 0.0}) {
		                Minecraft.thePlayer.sendQueue.addToSendQueue((Packet)new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + offset, Minecraft.thePlayer.posZ, false));
		            }
		        }
		    }
		static  {
			    lIlIlllIIIlIllII();
			  }
			  private static void lIlIlllIIIlIllII() {
			    var1 = new int[21];
			    var1[0] = 0;
			    var1[1] = " ".length();
			    var1[2] = "  ".length();
			    var1[3] = "   ".length();
			    var1[4] = 4;
			    var1[5] = 5;
			    var1[6] = 6;
			    var1[7] = 7;
			    var1[8] = 8;
			    var1[9] = -" ".length();
			    var1[10] = 9;
			    var1[11] = 10;
			    var1[12] = 11;
			    var1[13] = 12;
			    var1[14] = 350;
			    var1[15] = 300;
			    var1[16] = 13;
			    var1[17] = 14;
			    var1[18] = 15;
			    var1[19] = 16;
			    var1[20] = 17;
			  }
			  
			  private static boolean lIlIlllIIIllIIIl(int llllllllllllIlIIIIlIIllIllllllll, int llllllllllllIlIIIIlIIllIlllllllI) { return (llllllllllllIlIIIIlIIllIllllllll >= llllllllllllIlIIIIlIIllIlllllllI); }
			  
			  public void PacketOldCrit() {
			    if (this.mode.getValue() == CritMode.PacketOld && canCrit()) {
			      double var11 = this.mc.thePlayer.posX;
			      double var22 = this.mc.thePlayer.posY;
			      double var33 = this.mc.thePlayer.posZ;
			      double[] var13 = new double[var1[3]];
			      var13[var1[0]] = 0.05954835722479834D;
			      var13[var1[1]] = 0.05943483573247983D;
			      var13[var1[2]] = 0.01354835722479834D;
			      double[] llllllllllllIlIIIIlIIllllIIlIIII = var13;
			      double[] llllllllllllIlIIIIlIIllllIIIlllI = llllllllllllIlIIIIlIIllllIIlIIII;
			      int llllllllllllIlIIIIlIIllllIIIllII = llllllllllllIlIIIIlIIllllIIlIIII.length;
			      int llllllllllllIlIIIIlIIllllIIIlIlI = var1[0];
			      "".length();



			      
			      while (!lIlIlllIIIllIIIl(llllllllllllIlIIIIlIIllllIIIlIlI, llllllllllllIlIIIIlIIllllIIIllII)) {
			        double array = llllllllllllIlIIIIlIIllllIIIlllI[llllllllllllIlIIIIlIIllllIIIlIlI];
			        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(var11, var22 + array, var33, false));
			        llllllllllllIlIIIIlIIllllIIIlIlI++;
			      } 
			    } 
			  }
			  /*     */   void ExCrit() {
				  /* 138 */     if (this.mode.getValue() == CritMode.Packet && canCrit()) {
				  /* 139 */       double offset1 = (randomNumber(-9999, 9999) / 10000000);
				  /* 140 */       double offset2 = (randomNumber(-9999, 9999) / 1000000000);
				  /*     */       double[] var9 = new double[] { 0.0624218713251234D + offset1, 0.0D, 1.0834773E-5D + offset2, 0.0D };;
				  /* 142 */       int var8 = var9.length;

				  /*     */       
				  /* 144 */       for (int var7 = 0; var7 < var8; var7++) {
				  /* 145 */         double offset = var9[var7];
				  /* 146 */         Minecraft var10000 = mc;
				  /* 147 */         Minecraft var10003 = mc;
				  /* 148 */         Minecraft var10004 = mc;
				  /* 149 */         double var10 = Minecraft.thePlayer.posY + offset;
				  /* 150 */         Minecraft var10005 = mc;
				  /* 151 */         if ((Killaura.curTarget.hurtResistantTime<= ((Double)14d).intValue())) {
				  /* 152 */           Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, var10, Minecraft.thePlayer.posZ, false));
				  /*     */         } 
				  /*     */       } 
				  /*     */     } 
				  /*     */   }
    static enum CritMode {
        HypixelPacket,
        PacketOld,
        Packet,
        NewPacket,
        Minijumps,
        Edit,
        SenPs,
        NoGround,
        Always,
        HypixelTest,
        Hypixel,
        HypixelNew;
    }
}
