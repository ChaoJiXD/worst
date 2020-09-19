package xyz.WorstClient.module.modules.combat;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import javax.swing.text.DefaultEditorKit.CutAction;
import javax.vecmath.Vector2f;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiWinGame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.main.Main;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import xyz.WorstClient.Client;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.rendering.EventRender2D;
import xyz.WorstClient.api.events.rendering.EventRender3D;
import xyz.WorstClient.api.events.world.EventPostUpdate;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Mode;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.management.FriendManager;
import xyz.WorstClient.management.Manager;
import xyz.WorstClient.management.ModuleManager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.module.modules.movement.Scaffold;
import xyz.WorstClient.module.modules.player.Teams;
import xyz.WorstClient.ui.fontRenderer.UnicodeFontRenderer;
import xyz.WorstClient.utils.CombatUtil;
import xyz.WorstClient.utils.RenderUtils;
import xyz.WorstClient.utils.RotationUtils;
import xyz.WorstClient.utils.TimerUtil;
import xyz.WorstClient.utils.math.MathUtil;
import xyz.WorstClient.utils.math.RotationUtil;
import xyz.WorstClient.utils.render.ColorUtils;
import xyz.WorstClient.utils.render.Colors;
import xyz.WorstClient.utils.render.RenderUtil;

public class Killaura extends Module {
	protected ModelBase mainModel;
	TimerUtil kms = new TimerUtil();
	public static float rotationPitch;
	public static ArrayList<EntityLivingBase> targets = new ArrayList();
	public ArrayList<EntityLivingBase> attackedTargets = new ArrayList();
	public static EntityLivingBase curTarget = null;
	public Mode<Enum> espmode = new Mode("ESP", "ESP", (Enum[]) EMode.values(), (Enum) EMode.Box);
	private Mode<Enum> Priority = new Mode("Priority", "Priority", (Enum[]) priority.values(), (Enum) priority.Health);
	private Mode<Enum> mode = new Mode("Mode", "Mode", (Enum[]) AuraMode.values(), (Enum) AuraMode.Switch);
	private Mode<Enum> hand = new Mode("Mode", "Mode", (Enum[]) handMode.values(), (Enum) handMode.Nromal);
	public static Numbers<Double>Turnspeed= new Numbers<Double>("TurnSpeed", "TurnSpeed", 90.0, 1.0, 180.0, 1.0);
	private static Numbers<Double>Switchdelay= new Numbers<Double>("Switchdelay", "switchdelay", 11.0, 0.0, 50.0, 1.0);
	private Numbers<Double> crack = new Numbers("CrackSize", "CrackSize", Double.valueOf(1.0D), Double.valueOf(0.0D), Double.valueOf(5.0D), Double.valueOf(1.0D));
	private static Numbers<Double> aps = new Numbers<Double>("APS", "APS", 10.0,1.0,20.0,0.5);
	private Numbers<Double> reach = new Numbers<Double>("Reach", "Reach", 4.5, 1.0, 6.0, 0.1);
	private Option<Boolean> blocking = new Option<Boolean>("Autoblock", "Autoblock", true);
	private Option<Boolean> players = new Option<Boolean>("Players", "Players", true);
	private Option<Boolean> animals = new Option<Boolean>("Animals", "Animals", true);
	private Option<Boolean> mobs = new Option<Boolean>("Mobs", "Mobs", false);
	private Option<Boolean> invis = new Option<Boolean>("Invisibles", "Invisibles", false);
    public  Option<Boolean> autoaim = new Option<Boolean>("AutoAim", "AutoAim", false);
    private Option<Boolean> raycast = new Option("Raycast", "Raycast", Boolean.valueOf(true));
    private Option<Boolean> targethp = new Option<Boolean>("TargetHP", "TargetHP", false);
    private Option<Boolean> heart = new Option<Boolean>("Heart", "heart", false);
    
	private static long lastMS, lastMS2;
	private TimerUtil test = new TimerUtil();
	private boolean doBlock = false;
	private boolean unBlock = false;
	private long lastMs;
	private float curYaw = 0.0f;
	private float curPitch = 0.0f;
	private int tick = 0;
	private int index;
	private TimerUtil timer = new TimerUtil();
	public static float[] facing;
	private float[] facing0;
	private float[] facing1;
	private float[] facing2;
	private float[] facing3;
    public Vector2f lastAngle = new Vector2f(0.0F, 0.0F);
	static boolean allowCrits;
	private static float sYaw;
	private Numbers<Double> particleSize= new Numbers<Double>("ParticleSize", "ParticleSize", 1.0, 0.0, 5.0, 1.0);
	
	public Killaura() {
		super("KillAura", new String[] { "aura"}, ModuleType.Combat);
		this.addValues(this.espmode,this.hand, this.Priority, this.mode, this.aps,this.Switchdelay, this.reach,this.crack,this.Turnspeed,this.particleSize,this.heart, this.blocking, this.players,
				this.animals, this.mobs ,this.raycast, this.invis,this.targethp);
	}

	public static double random(double min, double max) {
		Random random = new Random();
		return min + (int) (random.nextDouble() * (max - min));
	}

	private boolean shouldAttack() {
		return this.timer.hasReached((int) (1000 / this.aps.getValue().intValue()));
	}

	@EventHandler
	private void render(EventRender3D e) {

		
        if (curTarget == null || this.espmode.getValue() == EMode.None) {
            return;
        }
        
        Color color = new Color(255, 255, 255, 120);
        if (Killaura.curTarget.hurtResistantTime > 0) {
            color = new Color(Colors.RED.c);
        }
        if (curTarget != null) {
        	if(heart.getValue()) {
        	boolean p = true;
    		int i=0;
    		int a=0;
    		while(i < particleSize.getValue()) {
    			if(p == true) {
    			mc.theWorld.spawnParticle(EnumParticleTypes.HEART,Killaura.curTarget.posX+Math.random()/2, Killaura.curTarget.posY+2, Killaura.curTarget.posZ+Math.random()/2, 0.0D, 0.0D, 0.0D);

    			}else {
    			mc.theWorld.spawnParticle(EnumParticleTypes.HEART,Killaura.curTarget.posX-Math.random()/2, Killaura.curTarget.posY+2, Killaura.curTarget.posZ-Math.random()/2, 0.0D, 0.0D, 0.0D);
    			}
    			i++;
    			a++;
    			p = !p;
    		}
        	}
            if (this.espmode.getValue() == EMode.Box) {
                mc.getRenderManager();
                double x = Killaura.curTarget.lastTickPosX + (Killaura.curTarget.posX - Killaura.curTarget.lastTickPosX) * (double)Killaura.mc.timer.renderPartialTicks - RenderManager.renderPosX;
                mc.getRenderManager();
                double y = Killaura.curTarget.lastTickPosY + (Killaura.curTarget.posY - Killaura.curTarget.lastTickPosY) * (double)Killaura.mc.timer.renderPartialTicks - RenderManager.renderPosY;
                mc.getRenderManager();
                double z = Killaura.curTarget.lastTickPosZ + (Killaura.curTarget.posZ - Killaura.curTarget.lastTickPosZ) * (double)Killaura.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
          
                    double width = Killaura.curTarget.getEntityBoundingBox().maxX - Killaura.curTarget.getEntityBoundingBox().minX;
                    double height = Killaura.curTarget.getEntityBoundingBox().maxY - Killaura.curTarget.getEntityBoundingBox().minY + 0.25;
                    float red = 0.0f;
                    float green = 1.0f;
                    float blue = 0.0f;
                    float alpha = 0.5f;
                    float lineRed = 0.0f;
                    float lineGreen = 0.5f;
                    float lineBlue = 1.0f;
                    float lineAlpha = 1.0f;
                    float lineWdith = 2.0f;
                    if(curTarget.hurtTime == 0) {
                    	RenderUtil.drawEntityESP((double)x, (double)y, (double)z, (double)width, (double)height, (float)0.0f, (float)1.0f, (float)0.0f, (float)0.3f, (float)0.0f, (float)1.0f, (float)0.1f, (float)1.0f, (float)2.0f);
                    }else {
                        RenderUtil.drawEntityESP((double)x, (double)y, (double)z, (double)width, (double)height, (float)1.0f, (float)0.0f, (float)0.0f, (float)0.3f, (float)1.0f, (float)0f, (float)0f, (float)1.0f, (float)2.0f);
                    }
                
            } else if(this.espmode.getValue() == EMode.Liquidbounce){
                mc.getRenderManager();
                double x = Killaura.curTarget.lastTickPosX + (Killaura.curTarget.posX - Killaura.curTarget.lastTickPosX) * (double)Killaura.mc.timer.renderPartialTicks - RenderManager.renderPosX;
                mc.getRenderManager();
                double y = Killaura.curTarget.lastTickPosY + (Killaura.curTarget.posY - Killaura.curTarget.lastTickPosY) * (double)Killaura.mc.timer.renderPartialTicks - RenderManager.renderPosY;
                mc.getRenderManager();
                double z = Killaura.curTarget.lastTickPosZ + (Killaura.curTarget.posZ - Killaura.curTarget.lastTickPosZ) * (double)Killaura.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
                if (curTarget instanceof EntityPlayer) {
                    double d = curTarget.isSneaking() ? 0.25 : 0.0;
                    double mid = 0.5;
                    GL11.glPushMatrix();
                    GL11.glEnable((int)3042);
                    GL11.glBlendFunc((int)770, (int)771);
                    double rotAdd = -0.25 * (double)(Math.abs((float)Killaura.curTarget.rotationPitch) / 90.0f);
                    GL11.glTranslated((double)((x -= 0.5) + 0.5), (double)((y += (double)curTarget.getEyeHeight() + 0.35 - d) + 0.5), (double)((z -= 0.5) + 0.5));
                    GL11.glRotated((double)(-Killaura.curTarget.rotationYaw % 360.0f), (double)0.0, (double)1.0, (double)0.0);
                    GL11.glTranslated((double)(-(x + 0.5)), (double)(-(y + 0.5)), (double)(-(z + 0.5)));
                    GL11.glDisable((int)3553);
                    GL11.glEnable((int)2848);
                    GL11.glDisable((int)2929);
                    GL11.glDepthMask((boolean)false);
                    GL11.glColor4f(0, (float)(100 / 255.0f), (float)((float)color.getBlue() / 255.0f), (float)0.5f);
                    RenderUtil.drawBoundingBox((AxisAlignedBB)new AxisAlignedBB(x+0.1, y-0.1, z+0.1, x + 0.9, y-0.15, z + 0.9));
                    GL11.glDisable((int)2848);
                    GL11.glEnable((int)3553);
                    GL11.glEnable((int)2929);
                    GL11.glDepthMask((boolean)true);
                    GL11.glDisable((int)3042);
                    GL11.glPopMatrix();
                } else {
                    double width = Killaura.curTarget.getEntityBoundingBox().maxZ - Killaura.curTarget.getEntityBoundingBox().minZ;
                    double height = 0.1;
                    float red = 0.0f;
                    float green = 0.5f;
                    float blue = 1.0f;
                    float alpha = 0.5f;
                    float lineRed = 0.0f;
                    float lineGreen = 0.5f;
                    float lineBlue = 1.0f;
                    float lineAlpha = 1.0f;
                    float lineWdith = 2.0f;
                    RenderUtil.drawEntityESP((double)x, (double)(y + (double)curTarget.getEyeHeight() + 0.25), (double)z, (double)width, (double)0.1, (float)0.0f, (float)0.5f, (float)1.0f, (float)0.5f, (float)0.0f, (float)0.5f, (float)1.0f, (float)1.0f, (float)2.0f);
                }
                
                
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                GL11.glNormal3f((float)1.0f, (float)1.0f, (float)1.0f);
                GL11.glPopMatrix();
            }
            
            if (curTarget == null || this.espmode.getValue() == EMode.Vape) {
                mc.getRenderManager();
                double x1 = Killaura.curTarget.lastTickPosX + (Killaura.curTarget.posX - Killaura.curTarget.lastTickPosX) * (double)Killaura.mc.timer.renderPartialTicks - RenderManager.renderPosX;
                mc.getRenderManager();
                double y1 = Killaura.curTarget.lastTickPosY + (Killaura.curTarget.posY - Killaura.curTarget.lastTickPosY) * (double)Killaura.mc.timer.renderPartialTicks - RenderManager.renderPosY;
                mc.getRenderManager();
                double z1 = Killaura.curTarget.lastTickPosZ + (Killaura.curTarget.posZ - Killaura.curTarget.lastTickPosZ) * (double)Killaura.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
          
                    double width = Killaura.curTarget.getEntityBoundingBox().maxX - Killaura.curTarget.getEntityBoundingBox().minX-0.2;
                    double height = Killaura.curTarget.getEntityBoundingBox().maxY - Killaura.curTarget.getEntityBoundingBox().minY +0.05;
                    float red = 220-Killaura.curTarget.hurtTime * 5/ 255.0f;
                    float green = Killaura.curTarget.hurtTime * 10/255.0f;
                    float blue = Killaura.curTarget.hurtTime *2/255.0f;
                    float alpha = (float)(80 + Killaura.curTarget.hurtTime*10)/255.0f;

                    RenderUtil.drawEntityESP(x1, y1, z1, width, height, red, green, blue, alpha, 0, 0, 0, 0, 0);
                
            	
                
            }
            if (curTarget == null || this.espmode.getValue() == EMode.Rainbow) {
                mc.getRenderManager();
                double x1 = Killaura.curTarget.lastTickPosX + (Killaura.curTarget.posX - Killaura.curTarget.lastTickPosX) * (double)Killaura.mc.timer.renderPartialTicks - RenderManager.renderPosX;
                mc.getRenderManager();
                double y1 = Killaura.curTarget.lastTickPosY + (Killaura.curTarget.posY - Killaura.curTarget.lastTickPosY) * (double)Killaura.mc.timer.renderPartialTicks - RenderManager.renderPosY;
                mc.getRenderManager();
                double z1 = Killaura.curTarget.lastTickPosZ + (Killaura.curTarget.posZ - Killaura.curTarget.lastTickPosZ) * (double)Killaura.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
          
                
                float blue;
                float green;
                float red;
                float alpha;
                
                    double width = Killaura.curTarget.getEntityBoundingBox().maxX - Killaura.curTarget.getEntityBoundingBox().minX-0.2;
                    double height = Killaura.curTarget.getEntityBoundingBox().maxY - Killaura.curTarget.getEntityBoundingBox().minY +0.05;
                    if(Killaura.curTarget.hurtTime == 10 ) {
                        red = ColorUtils.getRainbow().getRed()/ 255.0f;
                        green = ColorUtils.getRainbow().getGreen()/255.0f;
                        blue = ColorUtils.getRainbow().getBlue()/255.0f;
                        //float alpha = 160.0f;
                        alpha = (float)(80 + Killaura.curTarget.hurtTime*10)/255.0f;	
                    }else {
                        red = ColorUtils.getRainbow().getRed()/ 255.0f;
                        green = ColorUtils.getRainbow().getGreen()/255.0f;
                        blue = ColorUtils.getRainbow().getBlue()/255.0f;
                    	
                    	alpha = (float)(80 + Killaura.curTarget.hurtTime*10)/255.0f;	
                    }


                    RenderUtil.drawEntityESP(x1, y1, z1, width, height, red, green, blue, alpha, 0, 0, 0, 0, 0);
                
            	
                
            }
            
        }
        
        
    }
	

	private boolean canBlock() {
		if (this.mc.thePlayer.getCurrentEquippedItem() != null
				&& this.mc.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
			return true;
		}
		return false;
	}
	public Entity raycast5(Entity fromEntity) {
		/* 178 */     if (((Boolean)this.raycast.getValue()).booleanValue())
		/* 179 */       for (Entity en2 : Minecraft.theWorld.loadedEntityList) {
		/* 180 */         if (en2 == Minecraft.thePlayer || en2.equals(Minecraft.thePlayer) || en2 == fromEntity || en2.equals(fromEntity) || (!en2.isInvisible() && !(en2 instanceof net.minecraft.entity.item.EntityArmorStand)) || !en2.boundingBox.intersectsWith(fromEntity.boundingBox))
		/*     */           continue; 
		/* 181 */         return Minecraft.thePlayer.canEntityBeSeen(en2) ? en2 : en2;
		/*     */       }  
		/* 184 */     return fromEntity;
		/*     */   }


	public static long getCurrentMS() {
		return System.nanoTime() / 1000000L;
	}

	public static boolean hit(long milliseconds) {
		return (getCurrentMS() - lastMS) >= milliseconds;
	}

	public static void revert() {
		lastMS = getCurrentMS();
	}

	@EventHandler
	private void onUpdate(final EventPreUpdate event) {
		this.setSuffix(this.mode.getValue());
        if (Minecraft.thePlayer.ticksExisted % this.Switchdelay.getValue().intValue() == 0 && this.targets.size() > 1) {
            ++this.index;
        }
        if (!this.targets.isEmpty() && this.index >= this.targets.size()) {
            this.index = 0;
        }
    	if(this.autoaim.getValue().booleanValue()) {
    		if(this.curTarget!=null) {
            float[] rotations = CombatUtil.getRotations(curTarget);
            mc.thePlayer.rotationYawHead = rotations[0];
            mc.thePlayer.rotationYaw = rotations[0] ;
    	}
    	}
    	this.doBlock = false;
        this.clear();
        this.findTargets(event);
        this.setCurTarget();
        if (this.hand.getValue() == handMode.Nromal) {
        	  if (curTarget != null) {
                  Random rand = new Random();
                  this.facing0 = Killaura.getHypixelRotationsNeededBlock(Killaura.curTarget.posX, Killaura.curTarget.posY, Killaura.curTarget.posZ);
                  this.facing1 = Killaura.getRotationFromPosition(Killaura.curTarget.posX, Killaura.curTarget.posY, Killaura.curTarget.posZ);
                  this.facing2 = Killaura.getRotationsNeededBlock(Killaura.curTarget.posX, Killaura.curTarget.posY, Killaura.curTarget.posZ);
                  this.facing3 = Killaura.getRotations(curTarget);
                  for (int i2 = 0; i2 <= 3; ++i2) {
                      switch (Killaura.randomNumber(0.0, i2)) {
                          case 0: {
                              facing = this.facing0;
                          }
                          case 1: {
                              facing = this.facing1;
                          }
                          case 2: {
                              facing = this.facing2;
                          }
                          case 3: {
                              facing = this.facing3;
                          }
                      }
                  }
                  if (facing.length >= 0) {
                      event.setYaw(facing[0]);
                      event.setPitch(facing[1]);
                  }
                  if (curTarget != null) {
                  	Minecraft.thePlayer.renderYawOffset = facing[0];
                      Minecraft.thePlayer.rotationYawHead = facing[0];
                  }
              } else {
                  this.targets.clear();
                  this.attackedTargets.clear();
                  this.lastMs = System.currentTimeMillis();
                  if (this.unBlock) {
                      mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                      Minecraft.thePlayer.itemInUseCount = 0;
                      this.unBlock = false;
                  }
              }
              }

        
	if (this.hand.getValue() == handMode.Smooth) {
		 if (Killaura.curTarget != null) {
	            final Random rand = new Random();
	            this.facing0 = getHypixelRotationsNeededBlock(Killaura.curTarget.posX, Killaura.curTarget.posY, Killaura.curTarget.posZ);
	            this.facing1 = getRotationFromPosition(Killaura.curTarget.posX, Killaura.curTarget.posY, Killaura.curTarget.posZ);
	            this.facing2 = getRotationsNeededBlock(Killaura.curTarget.posX, Killaura.curTarget.posY, Killaura.curTarget.posZ);
	            this.facing3 = getRotations(Killaura.curTarget);
	            for (int i = 0; i <= 3; ++i) {
	                switch (randomNumber(0.0, i)) {
	                    case 0: {
	                        Killaura.facing = this.facing0;
	                    }
	                    case 1: {
	                        Killaura.facing = this.facing1;
	                    }
	                    case 2: {
	                        Killaura.facing = this.facing2;
	                    }
	                    case 3: {
	                        Killaura.facing = this.facing3;
	                        break;
	                    }
	                }
	            }
	            if (Killaura.facing.length >= 0) {
	            	this.Turnspeed.getValue().intValue();
	                event.setYaw(Killaura.facing[0]);
	                this.Turnspeed.getValue().intValue();
	                event.setPitch(Killaura.facing[1]);
	            }
	            
	            if (Killaura.curTarget != null) {
	                final Minecraft mc = Killaura.mc;
	                Minecraft.thePlayer.renderYawOffset = Killaura.facing[0];
	                final Minecraft mc2 = Killaura.mc;
	                Minecraft.thePlayer.rotationYawHead = Killaura.facing[0];
	            }
	            final int maxAngleStep = this.Turnspeed.getValue().intValue();
	            final float[] rotations = RotationUtil.faceTarget(Killaura.curTarget, 1000.0f, 1000.0f, false);
	            final int xz = (int)(randomNumber1(maxAngleStep, maxAngleStep) / 100.0);
	            float targetYaw = RotationUtils.getYawChange(Killaura.sYaw, Killaura.curTarget.posX, Killaura.curTarget.posZ);
	            Random rand1 = new Random();
				facing0 = getHypixelRotationsNeededBlock1(curTarget.posX, curTarget.posY, curTarget.posZ);
				facing1 = getRotationFromPosition1(curTarget.posX, curTarget.posY, curTarget.posZ);
				facing2 = getRotationsNeededBlock1(curTarget.posX, curTarget.posY, curTarget.posZ);
				facing3 = getRotations1(curTarget);
				int i;
				for (i = 0; i <= 3; i++) {
					switch (this.randomNumber1(0, i)) {
					case 0:
						facing = facing0;
					case 1:
						facing = facing1;
					case 2:
						facing = facing2;
					case 3:
						facing = facing3;
					}
				}

				if (facing.length >= 0) {
					event.setYaw((facing[0]));
					event.setPitch(facing[1]);
				}
				if (Killaura.curTarget != null) {
					mc.thePlayer.renderYawOffset = facing[0];
					mc.thePlayer.rotationYawHead = facing[0];
				}
			} else {
				this.targets.clear();
				this.attackedTargets.clear();
				this.lastMs = System.currentTimeMillis();
				if (this.unBlock) {
					this.mc.getNetHandler().addToSendQueue((Packet) new C07PacketPlayerDigging(
							C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
					this.mc.thePlayer.itemInUseCount = 0;
					this.unBlock = false;
				}
			}
		}
	

	
	}
	
	private static int randomNumber(double min, double max) {
		Random random = new Random();
		return (int) (min + (random.nextDouble() * (max - min)));
	}

	private static int randomNumber1(double min, double max) {
		Random random = new Random();
		return (int) (min + (random.nextDouble() * (max - min)));
	}
	


	
	


	public static float[] getRotationsNeededBlock(double x2, double y2, double z2) {
        Minecraft.getMinecraft();
        double diffX = x2 - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double diffZ = z2 - Minecraft.thePlayer.posZ;
        Minecraft.getMinecraft();
        double diffY = y2 - Minecraft.thePlayer.posY - 0.2;
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        float[] arrf = new float[2];
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        arrf[0] = Minecraft.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.thePlayer.rotationYaw);
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        arrf[1] = Minecraft.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.thePlayer.rotationPitch);
        return arrf;
    }
	public static float[] getRotationsNeededBlock1(double x, double y, double z) {
		double diffX = x - Minecraft.getMinecraft().thePlayer.posX;
		double diffZ = z - Minecraft.getMinecraft().thePlayer.posZ;
		double diffY = y - Minecraft.getMinecraft().thePlayer.posY - 0.2;
		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
		float pitch = (float) (-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
		return new float[] {
				Minecraft.getMinecraft().thePlayer.rotationYaw
						+ MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw),
				Minecraft.getMinecraft().thePlayer.rotationPitch
						+ MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) };
	}
	


	 public static float[] getHypixelRotationsNeededBlock(double x2, double y2, double z2) {
	        Minecraft.getMinecraft();
	        double diffX = x2 + 0.5 - Minecraft.thePlayer.posX;
	        Minecraft.getMinecraft();
	        double diffZ = z2 + 0.5 - Minecraft.thePlayer.posZ;
	        Minecraft.getMinecraft();
	        double diffY = y2 - Minecraft.thePlayer.posY - 0.2;
	        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
	        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
	        float pitch = (float)(-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
	        float[] arrf = new float[2];
	        Minecraft.getMinecraft();
	        arrf[0] = Minecraft.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - (float)(120 + new Random().nextInt(2)));
	        Minecraft.getMinecraft();
	        Minecraft.getMinecraft();
	        arrf[1] = Minecraft.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.thePlayer.rotationPitch);
	        return arrf;
	    }
	 public static float[] getHypixelRotationsNeededBlock1(double x, double y, double z) {
			double diffX = x + 0.5 - Minecraft.getMinecraft().thePlayer.posX;
			double diffZ = z + 0.5 - Minecraft.getMinecraft().thePlayer.posZ;
			double diffY = y - Minecraft.getMinecraft().thePlayer.posY - 0.2;

			double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
			float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
			float pitch = (float) (-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
			return new float[] {
					Minecraft.getMinecraft().thePlayer.rotationYaw
							+ MathHelper.wrapAngleTo180_float(yaw - (float) (120 + new Random().nextInt(2))),
					Minecraft.getMinecraft().thePlayer.rotationPitch
							+ MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) };
		}




	public static float[] getRotationFromPosition(double x2, double z2, double y2) {
        Minecraft.getMinecraft();
        double xDiff = x2 - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double zDiff = z2 - Minecraft.thePlayer.posZ;
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        double yDiff = y2 - Minecraft.thePlayer.posY - 0.4;
        double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(-Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793);
        float[] arrf = new float[2];
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        arrf[0] = Minecraft.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - Minecraft.thePlayer.rotationYaw);
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        arrf[1] = Minecraft.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - Minecraft.thePlayer.rotationPitch);
        return arrf;
    }
	public static float[] getRotationFromPosition1(double x, double z, double y) {
		Minecraft.getMinecraft();
		double xDiff = x - Minecraft.thePlayer.posX;
		Minecraft.getMinecraft();
		double zDiff = z - Minecraft.thePlayer.posZ;
		Minecraft.getMinecraft();
		double yDiff = y - Minecraft.getMinecraft().thePlayer.posY - 0.4;
		double dist = MathHelper.sqrt_double(xDiff * xDiff + zDiff * zDiff);
		float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
		float pitch = (float) (-Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793);
		return new float[] {
				Minecraft.getMinecraft().thePlayer.rotationYaw
						+ MathHelper.wrapAngleTo180_float(yaw - Minecraft.getMinecraft().thePlayer.rotationYaw),
				Minecraft.getMinecraft().thePlayer.rotationPitch
						+ MathHelper.wrapAngleTo180_float(pitch - Minecraft.getMinecraft().thePlayer.rotationPitch) };
	}



	public static float[] getRotations(Entity entity) {
        if (entity == null) {
            return null;
        }
        Minecraft.getMinecraft();
        double diffX = entity.posX - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double diffZ = entity.posZ - Minecraft.thePlayer.posZ;
        double diffY = entity.posY - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight());
        double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float)(-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }
	public static float[] getRotations1(Entity entity) {

		if (entity == null) {
			return null;
		}
		Minecraft.getMinecraft();
		double diffX = entity.posX - Minecraft.thePlayer.posX;
		Minecraft.getMinecraft();
		double diffZ = entity.posZ - Minecraft.thePlayer.posZ;
		double diffY = entity.posY - (Minecraft.thePlayer.posY + (double) Minecraft.thePlayer.getEyeHeight());

		double dist = MathHelper.sqrt_double(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
		float pitch = (float) (-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
		return new float[] { yaw, pitch };
	}



    
	private void doAttack() {
		int aps = this.aps.getValue().intValue();
		int delayValue = (int) (1000 / this.aps.getValue().intValue() + MathUtil.randomDouble(-2.0, 2.0));

		if ((double) Minecraft.thePlayer.getDistanceToEntity(curTarget) <= this.reach.getValue() + 0.4 && this.tick == 0
				&& this.test.delay(delayValue - 1)) {
			boolean miss = false;
			this.test.reset();

			if (Minecraft.thePlayer.isBlocking() || Minecraft.thePlayer.getHeldItem() != null
					&& Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword
					&& this.blocking.getValue().booleanValue()) {
				this.mc.getNetHandler().addToSendQueue((Packet) new C07PacketPlayerDigging(
						C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				this.unBlock = false;
			}
			if (!Minecraft.thePlayer.isBlocking() && !this.blocking.getValue().booleanValue()
					&& Minecraft.thePlayer.itemInUseCount > 0) {
				Minecraft.thePlayer.itemInUseCount = 0;
			}

			this.attack(miss);
			this.doBlock = true;
			if (!miss) {
				for (Object o : Minecraft.theWorld.loadedEntityList) {
					EntityLivingBase entity;
					if (!(o instanceof EntityLivingBase) || !this.isValidEntity(entity = (EntityLivingBase) o))
						continue;
					this.attackedTargets.add(curTarget);
				}
			}
		}

	}

	private void swap(int slot, int hotbarNum) {
		this.mc.playerController.windowClick(this.mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2,
				this.mc.thePlayer);
	}

	@EventHandler
	public void onPost(EventPostUpdate event) {
		this.sortList(targets);
		if (this.curTarget != null && this.shouldAttack()) {
			this.doAttack();
			this.newAttack();
		}
		int crackSize = ((Double)this.crack.getValue()).intValue();
		
		if (curTarget != null
				&& (Minecraft.thePlayer.getHeldItem() != null
						&& Minecraft.thePlayer.getHeldItem().getItem() instanceof ItemSword
						&& this.blocking.getValue().booleanValue() || Minecraft.thePlayer.isBlocking())
				&& this.doBlock) {
			Minecraft.thePlayer.itemInUseCount = Minecraft.thePlayer.getHeldItem().getMaxItemUseDuration();
			this.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255,
					Minecraft.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
			this.unBlock = true;
		}
	
	int i2 = 0;
	/* 251 */       while (i2 < crackSize) {
	/* 252 */         this.mc.effectRenderer.emitParticleAtEntity(curTarget, EnumParticleTypes.CRIT);
	/* 253 */         this.mc.effectRenderer.emitParticleAtEntity(curTarget, EnumParticleTypes.CRIT_MAGIC);
	/* 254 */         i2++;
	/*     */       }
	}


	private void attack(boolean fake) {
		Minecraft.thePlayer.swingItem();
		if (!fake) {
			this.doBlock = true;
			this.mc.thePlayer.sendQueue.addToSendQueue((Packet) new C02PacketUseEntity(Killaura.curTarget, C02PacketUseEntity.Action.ATTACK));
			if (Minecraft.thePlayer.isBlocking() && this.blocking.getValue().booleanValue()
					&& Minecraft.thePlayer.inventory.getCurrentItem() != null
					&& Minecraft.thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
				this.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255,
						Minecraft.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
				this.unBlock = true;
			}
			if (!Minecraft.thePlayer.isBlocking() && !this.blocking.getValue().booleanValue()
					&& Minecraft.thePlayer.itemInUseCount > 0) {
				Minecraft.thePlayer.itemInUseCount = 0;
			}
		}
	}

	private void newAttack() {
		if (Minecraft.thePlayer.isBlocking()) {
			for (int i = 0; i <= 2; i++) {
				this.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(0, 0, 0), 255, Minecraft.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
			}
		}
		if (Minecraft.thePlayer.isBlocking()) {
			for (int i = 0; i <= 2; i++) {
				this.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255,
						Minecraft.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
			}
		}
		if (Minecraft.thePlayer.isBlocking() && this.timer.delay(100)) {
			for (int i = 0; i <= 2; i++) {
				this.mc.getNetHandler().addToSendQueue((Packet) new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			}
		}
		if (!Minecraft.thePlayer.isBlocking() && !this.blocking.getValue().booleanValue() && Minecraft.thePlayer.itemInUseCount > 0) {
			Minecraft.thePlayer.itemInUseCount = 0;
		}
	}

	private void setCurTarget() {
		if (targets.size() == 0) {
			curTarget = null;
			return;
		}
		curTarget = this.targets.get(index);
	}

	private void clear() {
		curTarget = null;
		this.targets.clear();
		for (EntityLivingBase ent : this.targets) {
			if (this.isValidEntity(ent))
				continue;
			this.targets.remove(ent);
			if (!this.attackedTargets.contains(ent))
				continue;
			this.attackedTargets.remove(ent);
		}
	}

	private void findTargets(EventPreUpdate event) {
		int maxSize = this.mode.getValue() == AuraMode.Switch ? 4 : 1;
		for (Entity o3 : Minecraft.theWorld.loadedEntityList) {
			EntityLivingBase curEnt;
			if (o3 instanceof EntityLivingBase && this.isValidEntity(curEnt = (EntityLivingBase) o3)
					&& !this.targets.contains(curEnt)) {
				this.targets.add(curEnt);
			}
			if (this.targets.size() >= maxSize)
				break;
		}
		this.targets.sort((o1, o2) -> (int) (o1.getDistanceToEntity(o2) - o2.getDistanceToEntity(o1)));
	}

	private boolean isValidEntity(EntityLivingBase ent) {
		AntiBot ab = (AntiBot) Client.instance.getModuleManager().getModuleByClass(AntiBot.class);
		return ent == null ? false
				: (ent == this.mc.thePlayer ? false
						: (ent instanceof EntityPlayer && !this.players.getValue() ? false
								: ((ent instanceof EntityAnimal || ent instanceof EntitySquid)
										&& !this.animals.getValue()
												? false
												: ((ent instanceof EntityMob || ent instanceof EntityVillager
														|| ent instanceof EntityBat) && !this.mobs.getValue()
																? false
																: ((double) this.mc.thePlayer.getDistanceToEntity(
																		ent) > this.reach.getValue() + 0.4
																				? false
																				: (ent instanceof EntityPlayer
																						&& FriendManager
																								.isFriend(ent.getName())
																										? false
																										: (!ent.isDead
																												&& ent.getHealth() > 0.0F
																														? (ent.isInvisible()
																																&& !this.invis
																																		.getValue()
																																				? false
																																				: ab.isServerBot(
																																						ent) ? false
																																								: (this.mc.thePlayer.isDead
																																										? false
																																										: !(ent instanceof EntityPlayer)
																																												|| !Teams
																																														.isOnSameTeam(
																																																(EntityPlayer) ent)))
																														: false)))))));
	}

	@Override
	public void onEnable() {
		index = 0;
		this.curYaw = Minecraft.thePlayer.rotationYaw;
		this.curPitch = Minecraft.thePlayer.rotationPitch;
		super.onEnable();
	}

	public static float[] getRotationToEntity(Entity target) {
        Minecraft.getMinecraft();
        double xDiff = target.posX - Minecraft.thePlayer.posX;
        Minecraft.getMinecraft();
        double yDiff = target.posY - Minecraft.thePlayer.posY;
        Minecraft.getMinecraft();
        double zDiff = target.posZ - Minecraft.thePlayer.posZ;
        float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        Minecraft.getMinecraft();
        Minecraft.getMinecraft();
        float pitch = (float)(-Math.atan2(target.posY + (double)target.getEyeHeight() / 0.0 - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff)) * 180.0 / 3.141592653589793);
        if (yDiff > -0.2 && yDiff < 0.2) {
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            pitch = (float)(-Math.atan2(target.posY + (double)target.getEyeHeight() / HitLocation.CHEST.getOffset() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff)) * 180.0 / 3.141592653589793);
        } else if (yDiff > -0.2) {
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            pitch = (float)(-Math.atan2(target.posY + (double)target.getEyeHeight() / HitLocation.FEET.getOffset() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff)) * 180.0 / 3.141592653589793);
        } else if (yDiff < 0.3) {
            Minecraft.getMinecraft();
            Minecraft.getMinecraft();
            pitch = (float)(-Math.atan2(target.posY + (double)target.getEyeHeight() / HitLocation.HEAD.getOffset() - (Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight()), Math.hypot(xDiff, zDiff)) * 180.0 / 3.141592653589793);
        }
        return new float[]{yaw, pitch};
    }

	static enum HitLocation {
		AUTO(0.0), HEAD(1.0), CHEST(1.5), FEET(3.5);

		private double offset;

		HitLocation(double offset) {
			this.offset = offset;
		}

		public double getOffset() {
			return this.offset;
		}
	}

	@Override
	public void onDisable() {
		this.targets.clear();
		this.attackedTargets.clear();
		curTarget = null;
		Minecraft.thePlayer.itemInUseCount = 0;
		allowCrits = true;
		mc.thePlayer.renderYawOffset = mc.thePlayer.rotationYaw;
		rotationPitch = 0.0f;
		this.curYaw = Minecraft.thePlayer.rotationYaw;
		this.curPitch = Minecraft.thePlayer.rotationPitch;

		super.onDisable();
	}

	private void sortList(List<EntityLivingBase> weed) {
		if (this.Priority.getValue() == priority.slowly) {
			weed.sort((o1, o2) -> (int) (o1.getHealth() - o2.getHealth()));
			 ((List)Killaura.curTarget).sort((ent1, ent2) -> {
		            float f2 = 0.0F;
	            float e1 = RotationUtil.getRotations((Entity)ent1)[0];
		             float e2 = RotationUtil.getRotations((Entity)ent2)[0];
				         return (e1 < f2) ? 1 : ((e1 == e2) ? 0 : -2);
			          }); 
				   }
			  
	

		
		if (this.Priority.getValue() == priority.Range) {
			weed.sort((o1, o2) -> (int) (o1.getDistanceToEntity(mc.thePlayer) - o2.getDistanceToEntity(mc.thePlayer)));
		}
		if (this.Priority.getValue() == priority.Fov) {
			weed.sort(Comparator.comparingDouble(o -> RotationUtil.getDistanceBetweenAngles(mc.thePlayer.rotationPitch,
					Killaura.getRotationToEntity(o)[0])));
		}
		if (this.Priority.getValue() == priority.Angle) {
			weed.sort((o1, o2) -> {
				float[] rot1 = getRotationToEntity(o1);
				float[] rot2 = getRotationToEntity(o2);
				return (int) (mc.thePlayer.rotationYaw - rot1[0] - (mc.thePlayer.rotationYaw - rot2[0]));
			});
		}
		if (this.Priority.getValue() == priority.Health) {
			weed.sort((o1, o2) -> (int) (o1.getHealth() - o2.getHealth()));
		}
	}
	
	

	public static float getYawDifference(float current, float target) {
        float rot = 0;
        return rot + ((rot = (target + 180.0f - current) % 360.0f) > 0.0f ? -180.0f : 180.0f);
    }

	private float getYawDifference(float yaw, EntityLivingBase target) {
        return Killaura.getYawDifference(yaw, Killaura.getRotationToEntity(target)[0]);
    }

	public static enum EMode {
		Box, None,Liquidbounce,Vape,Rainbow;
	}

	static enum priority {
		Range, Fov, Angle, Health,slowly;	
	}

	static enum AuraMode {
		Switch, Single;
	}
	static enum handMode{
		Nromal,
		Smooth;
	}
}
