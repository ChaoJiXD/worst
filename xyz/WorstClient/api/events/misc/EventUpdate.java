/*     */ package xyz.WorstClient.api.events.misc;
/*     */ import net.minecraft.entity.Entity;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EventUpdate
/*     */   implements Event
/*     */ {
/*     */   private boolean isPre;
/*     */   private float yaw;
/*     */   private float pitch;
/*     */   private double x;
/*     */   private double y;
/*     */   private double z;
/*     */   private boolean onground;
/*     */   private boolean alwaysSend;
/*     */   private boolean sneaking;
/*     */   public static float YAW;
/*     */   public static float PITCH;
/*     */   public static float PREVYAW;
/*     */   public static float PREVPITCH;
/*     */   public static boolean SNEAKING;
/*     */   private Entity ent;
/*     */   
/*  33 */   public Entity getEntity() { return this.ent; }
/*     */ 
/*     */ 
/*     */   
/*  37 */   public boolean isPre() { return this.isPre; }
/*     */ 
/*     */ 
/*     */   
/*  41 */   public boolean isPost() { return !this.isPre; }
/*     */ 
/*     */ 
/*     */   
/*  45 */   public float getYaw() { return this.yaw; }
/*     */ 
/*     */ 
/*     */   
/*  49 */   public float getPitch() { return this.pitch; }
/*     */ 
/*     */ 
/*     */   
/*  53 */   public double getX() { return this.x; }
/*     */ 
/*     */   
/*  56 */   public double getY() { return this.y; }
/*     */ 
/*     */   
/*  59 */   public double getZ() { return this.z; }
/*     */ 
/*     */   
/*  62 */   public boolean isSneaking() { return this.sneaking; }
/*     */ 
/*     */   
/*  65 */   public boolean isOnground() { return this.onground; }
/*     */ 
/*     */   
/*  68 */   public void setSneaking(boolean sneaking) { this.sneaking = sneaking; }
/*     */ 
/*     */   
/*  71 */   public boolean shouldAlwaysSend() { return this.alwaysSend; }
/*     */ 
/*     */ 
/*     */   
/*  75 */   public void setYaw(float yaw) { this.yaw = yaw; }
/*     */ 
/*     */ 
/*     */   
/*  79 */   public void setPitch(float pitch) { this.pitch = pitch; }
/*     */ 
/*     */ 
/*     */   
/*  83 */   public void setX(double x) { this.x = x; }
/*     */ 
/*     */   
/*  86 */   public void setY(double y) { this.y = y; }
/*     */ 
/*     */   
/*  89 */   public void setZ(double z) { this.z = z; }
/*     */ 
/*     */ 
/*     */   
/*  93 */   public void setGround(boolean ground) { this.onground = ground; }
/*     */ 
/*     */ 
/*     */   
/*  97 */   public void setAlwaysSend(boolean alwaysSend) { this.alwaysSend = alwaysSend; }
/*     */ 
/*     */ 
/*     */   
/* 101 */   public void setOnGround(boolean onground) { this.onground = onground; }
/*     */ }


/* Location:              G:\历史遗留\AzureWare.jar!\net\AzureWare\events\EventUpdate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.2
 */