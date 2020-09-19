/*     */ package xyz.WorstClient.module.modules.movement;

/*     */ 

/*     */ import java.awt.Color;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSnow;
/*     */ import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
import xyz.WorstClient.Client;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.rendering.EventRender2D;
import xyz.WorstClient.api.events.rendering.EventRender3D;
import xyz.WorstClient.api.events.world.EventPostUpdate;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.utils.MoveUtils;
import xyz.WorstClient.utils.PlayerUtil;
import xyz.WorstClient.utils.TimerUtil;
import xyz.WorstClient.utils.math.RotationUtil;
import xyz.WorstClient.utils.render.Colors;
import xyz.WorstClient.utils.render.RenderUtil;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.play.client.C09PacketHeldItemChange;
/*     */ import net.minecraft.network.play.client.C0APacketAnimation;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Scaffold
/*     */   extends Module
/*     */ {
	public static boolean firstdown = true;
/*     */   public static boolean safewalk;
/*  61 */   public static Option<Boolean> tower = new Option("Tower", "tower", Boolean.valueOf(true));
/*  62 */   public static Option<Boolean> silent = new Option("Silent", "Silent", Boolean.valueOf(true));
/*  63 */   public static Option<Boolean> aac = new Option("AAC", "AAC", Boolean.valueOf(false));
/*  64 */   public static Option<Boolean> Esp = new Option("ESP", "ESP", Boolean.valueOf(true));
/*  65 */   public static Option<Boolean> swingItem = new Option("swingItem", "swingItem", Boolean.valueOf(true));
/*  66 */   public static Option<Boolean> towermove = new Option("TowerMove", "BetterTower", Boolean.valueOf(false));
/*  67 */   public static Option<Boolean> pick = new Option("pick", "pick", Boolean.valueOf(true));
/*  68 */   public static Option<Boolean> safe = new Option("SafeWalk", "SafeWalk", Boolean.valueOf(false));
public static Option<Boolean> down = new Option("DownScaffold", "DownScaffold", Boolean.valueOf(true));

public static Option<Boolean> Lag = new Option("Lag", "Lag", Boolean.valueOf(true));
public static Option<Boolean> auracheck = new Option("Auracheck", "Auracheck", Boolean.valueOf(true));
/*     */   	private Boolean SprintKeyDown;
public BlockData blockdata;
private float Disfall;
private Boolean autodis;
/*     */   private static List invalid;
/*     */   private List<Block> blacklist;
/*  72 */   private int width = 0;
/*     */   private BlockCache blockCache;
/*     */   private int currentItem;
/*     */   
/*     */   public Scaffold() {
/*  77 */     super("Scaffold", new String[] { "magiccarpet", "blockplacer", "airwalk" }, ModuleType.Movement);
/*  78 */     invalid = Arrays.asList(new Block[] { Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.chest, Blocks.enchanting_table, Blocks.tnt });
/*  79 */     addValues(new Value[] { tower, silent, aac, Esp, swingItem, towermove, safe,Lag,down,pick,auracheck});
/*  80 */     this.currentItem = 0;
/*  81 */     setColor((new Color(244, 119, 194)).getRGB());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  86 */     //this.currentItem = Minecraft.thePlayer.inventory.currentItem;
/*  87 */     super.onEnable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  92 */     //Minecraft.thePlayer.inventory.currentItem = this.currentItem;
/*  93 */     super.onDisable();
/*     */   }
/*     */   @EventHandler
/*     */     public void onRender2D(EventRender2D event) {
    ScaledResolution sr = new ScaledResolution(mc);
    mc.fontRendererObj.drawStringWithShadow(Integer.toString(getBlockCount())+" Blocks", sr.getScaledWidth() / 2 + 1 - mc.fontRendererObj.getStringWidth(Integer.toString(getBlockCount())) / 2, sr.getScaledHeight() / 2 + 12, getBlockColor(getBlockCount()));
}
private int getBlockColor(int count) {
    float f = count;
    float f1 = 64;
    float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
    return Color.HSBtoRGB(f2 / 3.0F, 1.0F, 1.0F) | 0xFF000000;
}
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   private void onrender(EventRender3D event) {
/* 100 */     Color color = new Color(Colors.BLACK.c);
/* 101 */     Color color2 = new Color(Colors.ORANGE.c);
/* 102 */     double x = Minecraft.thePlayer.lastTickPosX + (Minecraft.thePlayer.posX - Minecraft.thePlayer.lastTickPosX) * this.mc.timer.renderPartialTicks - RenderManager.renderPosX;
/* 103 */     double y = Minecraft.thePlayer.lastTickPosY + (Minecraft.thePlayer.posY - Minecraft.thePlayer.lastTickPosY) * this.mc.timer.renderPartialTicks - RenderManager.renderPosY;
/* 104 */     double z = Minecraft.thePlayer.lastTickPosZ + (Minecraft.thePlayer.posZ - Minecraft.thePlayer.lastTickPosZ) * this.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
/* 105 */     double x2 = Minecraft.thePlayer.lastTickPosX + (Minecraft.thePlayer.posX - Minecraft.thePlayer.lastTickPosX) * this.mc.timer.renderPartialTicks - RenderManager.renderPosX;
/* 106 */     double y2 = Minecraft.thePlayer.lastTickPosY + (Minecraft.thePlayer.posY - Minecraft.thePlayer.lastTickPosY) * this.mc.timer.renderPartialTicks - RenderManager.renderPosY;
/* 107 */     double z2 = Minecraft.thePlayer.lastTickPosZ + (Minecraft.thePlayer.posZ - Minecraft.thePlayer.lastTickPosZ) * this.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
/* 108 */     x -= 0.65D;
/* 109 */     z -= 0.65D;
/* 110 */     x2 -= 0.5D;
/* 111 */     z2 -= 0.5D;
/* 112 */     y += Minecraft.thePlayer.getEyeHeight() + 0.35D - (Minecraft.thePlayer.isSneaking() ? 0.25D : 0.0D);
/* 113 */     if (((Boolean)Esp.getValue()).booleanValue()) {
/* 114 */       GL11.glPushMatrix();
/* 115 */       GL11.glEnable(3042);
/* 116 */       GL11.glBlendFunc(770, 771);
/* 117 */       double rotAdd = -0.25D * (Math.abs(Minecraft.thePlayer.rotationPitch) / 90.0F);
/* 118 */       GL11.glDisable(3553);
/* 119 */       GL11.glEnable(2848);
/* 120 */       GL11.glDisable(2929);
/* 121 */       GL11.glDepthMask(false);
/* 122 */       GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, 1.0F);
/* 123 */       GL11.glLineWidth(2.0F);
/* 124 */       RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x, y - 2.0D, z, x + 1.3D, y - 2.0D, z + 1.3D));
/* 125 */       RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x2, y - 2.0D, z2, x2 + 1.0D, y - 2.0D, z2 + 1.0D));
/* 126 */       GL11.glColor4f(color2.getRed() / 255.0F, color2.getGreen() / 255.0F, color2.getBlue() / 255.0F, 1.0F);
/* 127 */       RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x, y - 2.0D, z, x + 1.3D, y - 2.0D, z + 1.3D));
/*     */     } 
/* 129 */     GL11.glDisable(2848);
/* 130 */     GL11.glEnable(3553);
/* 131 */     GL11.glEnable(2929);
/* 132 */     GL11.glDepthMask(true);
/* 133 */     GL11.glDisable(3042);
/* 134 */     GL11.glPopMatrix();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */  
/*     */ 
/*     */   
/*     */   @EventHandler
/*     */   private void onUpdate(EventPreUpdate event) {
	if(this.aac.getValue()) {
		this.setSuffix((Object)"AAC");
	}else {
		this.setSuffix((Object)"Smooth");
	}

/* 165 */     if (((Boolean)aac.getValue()).booleanValue() ) {
/* 166 */       Minecraft.thePlayer.setSprinting(false);
/*     */     } //else {
/*     */       
/* 169 */      // Minecraft.thePlayer.setSprinting(true);
/*     */    // } 
/*     */     if(this.down.getValue() && Minecraft.getMinecraft().gameSettings.keyBindSprint.isKeyDown()) {
	mc.thePlayer.motionX *= 0.7;
	mc.thePlayer.motionZ *= 0.7;
}
/* 172 */     if (Minecraft.thePlayer.isPotionActive(Potion.moveSpeed)) {
/* 173 */       this.mc.timer.timerSpeed = 0.999F;
/*     */     }
/*     */     
/* 176 */     if (grabBlockSlot() != -1) {
/*     */       
this.blockCache = grab();
if (this.tower.getValue() && (this.towermove.getValue() || !PlayerUtil.isMoving2())) {
		tower();
	
	
} 
BlockPos underPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY-1, mc.thePlayer.posZ);
Block underBlock = mc.theWorld.getBlockState(underPos).getBlock();

BlockData data = getBlockData(underPos);

float[] rot = getRotations(data.position, data.face);
Client.RenderRotate(rot[0]);
event.setYaw(rot[0]);
event.setPitch(rot[1]);



//if (!mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.onGround && MoveUtils.isOnGround(0.001) && mc.thePlayer.isCollidedVertically) {
//    event.setOnground(false);                     
//}

/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
public static float[] getRotations(BlockPos block, EnumFacing face) {
    double x = block.getX() + 0.5 - mc.thePlayer.posX +  (double) face.getFrontOffsetX()/2;
    double z = block.getZ() + 0.5 - mc.thePlayer.posZ +  (double) face.getFrontOffsetZ()/2;
    double y = (block.getY() + 0.5);

    double d1 = mc.thePlayer.posY + mc.thePlayer.getEyeHeight() - y;
    double d3 = MathHelper.sqrt_double(x * x + z * z);
    float yaw = (float) (Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
    float pitch = (float) (Math.atan2(d1, d3) * 180.0D / Math.PI);
    if (yaw < 0.0F) {
        yaw += 360f;
    }
    return new float[]{yaw, pitch};
}
/*     */   
/*     */   private boolean invCheck() {
/* 193 */     for (int i = 36; i < 45; i++) {
/* 194 */       if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
/* 195 */         Item item = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
/* 196 */         if (item instanceof ItemBlock && isValid(item)) {
/* 197 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/* 201 */     return true;
/*     */   }
/*     */   
/*     */   public static int getBlockCount() {
/* 205 */     int blockCount = 0;
/* 206 */     for (int i = 0; i < 45; i++) {
/* 207 */       if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
/* 208 */         ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
/* 209 */         Item item = is.getItem();
/* 210 */         if (is.getItem() instanceof ItemBlock && isValid(item)) {
/* 211 */           blockCount += is.stackSize;
/*     */         }
/*     */       } 
/*     */     } 
/* 215 */     return blockCount;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isValid(Item item) {
/* 221 */     if (!(item instanceof ItemBlock)) {
/* 222 */       return false;
/*     */     }
/* 224 */     ItemBlock iBlock = (ItemBlock)item;
/* 225 */     Block block = iBlock.getBlock();
/* 226 */     if (invalid.contains(block)) {
/* 227 */       return false;
/*     */     }
/*     */     
/* 230 */     return true;
/*     */   }
/*     */     public static Vec3 getVec3(BlockPos pos, EnumFacing face) {
    double x = pos.getX() + 0.5;
    double y = pos.getY() + 0.5;
    double z = pos.getZ() + 0.5;
    x += (double) face.getFrontOffsetX() / 2;
    z += (double) face.getFrontOffsetZ() / 2;
    y += (double) face.getFrontOffsetY() / 2;
    if (face == EnumFacing.UP || face == EnumFacing.DOWN) {
        x += randomNumber(0.3, -0.3);
        z += randomNumber(0.3, -0.3);
    } else {
        y += randomNumber(0.3, -0.3);
    }
    if (face == EnumFacing.WEST || face == EnumFacing.EAST) {
        z += randomNumber(0.3, -0.3);
    }
    if (face == EnumFacing.SOUTH || face == EnumFacing.NORTH) {
        x += randomNumber(0.3, -0.3);
    }
    return new Vec3(x, y, z);
}
/*     */   
public static double randomNumber(double max, double min) {
    return (Math.random() * (max - min)) + min;
}
    	



/*     */   @EventHandler
/*     */   private void onPostUpdate(EventPostUpdate event) {
/* 236 */     if (this.blockCache != null) {
/*     */       if (this.swingItem.getValue()) {
	Minecraft.thePlayer.swingItem();
}else {
    mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
}
/* 238 */       
/*     */                
/* 278 */       int currentSlot = Minecraft.thePlayer.inventory.currentItem;
/* 279 */       int slot = grabBlockSlot(); 
if(slot == -1){
	/* 287 */         this.blockCache = null;
	return;
}



Minecraft.thePlayer.inventory.currentItem = slot;
BlockPos underPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY-1, mc.thePlayer.posZ);
Block underBlock = mc.theWorld.getBlockState(underPos).getBlock();

BlockData data = getBlockData(underPos);



/* 280 */       if (placeBlock(data)) {       
	if (((Boolean)silent.getValue()).booleanValue()) {

/* 282 */           //getBestBlocks();
/* 283 */           Minecraft.thePlayer.inventory.currentItem = currentSlot;
/* 284 */           Minecraft.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(currentSlot));
/*     */         } 
/*     */         
/* 287 */         this.blockCache = null;
/*     */       } 
/*     */     } 
/*     */   }
private boolean isPosSolid(BlockPos pos) {
    Block block = mc.theWorld.getBlockState(pos).getBlock();
    if ((block.getMaterial().isSolid() || !block.isTranslucent() || block.isSolidFullCube() || block instanceof BlockLadder || block instanceof BlockCarpet
            || block instanceof BlockSnow || block instanceof BlockSkull)
            && !block.getMaterial().isLiquid() && !(block instanceof BlockContainer)) {
        return true;
    }
    return false;
}
private boolean CanDownPut() {
	// TODO �Զ����ɵķ������
	return this.down.getValue() && mc.gameSettings.keyBindSprint.isKeyDown() && mc.thePlayer.onGround;
}
private BlockData getBlockData(BlockPos pos) {

    if (isPosSolid(pos.add(0, -1, 0))) {
    	return new BlockData(pos.add(0, -1, 0), CanDownPut() ?EnumFacing.DOWN :EnumFacing.UP);
    }
    if (isPosSolid(pos.add(-1, 0, 0))) {
        return new BlockData(pos.add(-1, 0, 0), CanDownPut() ?EnumFacing.DOWN :EnumFacing.EAST);
    }
    if (isPosSolid(pos.add(1, 0, 0))) {
        return new BlockData(pos.add(1, 0, 0),CanDownPut() ?EnumFacing.DOWN : EnumFacing.WEST);
    }
    if (isPosSolid(pos.add(0, 0, 1))) {
        return new BlockData(pos.add(0, 0, 1), CanDownPut() ?EnumFacing.DOWN :EnumFacing.NORTH);
    }
    if (isPosSolid(pos.add(0, 0, -1))) {
        return new BlockData(pos.add(0, 0, -1), CanDownPut() ?EnumFacing.DOWN :EnumFacing.SOUTH);
    }
    BlockPos pos1 = pos.add(-1, 0, 0);
    if (isPosSolid(pos1.add(0, -1, 0))) {
        return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
    }
    if (isPosSolid(pos1.add(-1, 0, 0))) {
        return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
    }
    if (isPosSolid(pos1.add(1, 0, 0))) {
        return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
    }
    if (isPosSolid(pos1.add(0, 0, 1))) {
        return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
    }
    if (isPosSolid(pos1.add(0, 0, -1))) {
        return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
    }
    BlockPos pos2 = pos.add(1, 0, 0);
    if (isPosSolid(pos2.add(0, -1, 0))) {
        return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
    }
    if (isPosSolid(pos2.add(-1, 0, 0))) {
        return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
    }
    if (isPosSolid(pos2.add(1, 0, 0))) {
        return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
    }
    if (isPosSolid(pos2.add(0, 0, 1))) {
        return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
    }
    if (isPosSolid(pos2.add(0, 0, -1))) {
        return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
    }
    BlockPos pos3 = pos.add(0, 0, 1);
    if (isPosSolid(pos3.add(0, -1, 0))) {
        return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
    }
    if (isPosSolid(pos3.add(-1, 0, 0))) {
        return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
    }
    if (isPosSolid(pos3.add(1, 0, 0))) {
        return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
    }
    if (isPosSolid(pos3.add(0, 0, 1))) {
        return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
    }
    if (isPosSolid(pos3.add(0, 0, -1))) {
        return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
    }
    BlockPos pos4 = pos.add(0, 0, -1);
    if (isPosSolid(pos4.add(0, -1, 0))) {
        return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
    }
    if (isPosSolid(pos4.add(-1, 0, 0))) {
        return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
    }
    if (isPosSolid(pos4.add(1, 0, 0))) {
        return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
    }
    if (isPosSolid(pos4.add(0, 0, 1))) {
        return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
    }
    if (isPosSolid(pos4.add(0, 0, -1))) {
        return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
    }
    BlockPos pos19 = pos.add(-2, 0, 0);
    if (isPosSolid(pos1.add(0, -1, 0))) {
        return new BlockData(pos1.add(0, -1, 0), EnumFacing.UP);
    }
    if (isPosSolid(pos1.add(-1, 0, 0))) {
        return new BlockData(pos1.add(-1, 0, 0), EnumFacing.EAST);
    }
    if (isPosSolid(pos1.add(1, 0, 0))) {
        return new BlockData(pos1.add(1, 0, 0), EnumFacing.WEST);
    }
    if (isPosSolid(pos1.add(0, 0, 1))) {
        return new BlockData(pos1.add(0, 0, 1), EnumFacing.NORTH);
    }
    if (isPosSolid(pos1.add(0, 0, -1))) {
        return new BlockData(pos1.add(0, 0, -1), EnumFacing.SOUTH);
    }
    BlockPos pos29 = pos.add(2, 0, 0);
    if (isPosSolid(pos2.add(0, -1, 0))) {
        return new BlockData(pos2.add(0, -1, 0), EnumFacing.UP);
    }
    if (isPosSolid(pos2.add(-1, 0, 0))) {
        return new BlockData(pos2.add(-1, 0, 0), EnumFacing.EAST);
    }
    if (isPosSolid(pos2.add(1, 0, 0))) {
        return new BlockData(pos2.add(1, 0, 0), EnumFacing.WEST);
    }
    if (isPosSolid(pos2.add(0, 0, 1))) {
        return new BlockData(pos2.add(0, 0, 1), EnumFacing.NORTH);
    }
    if (isPosSolid(pos2.add(0, 0, -1))) {
        return new BlockData(pos2.add(0, 0, -1), EnumFacing.SOUTH);
    }
    BlockPos pos39 = pos.add(0, 0, 2);
    if (isPosSolid(pos3.add(0, -1, 0))) {
        return new BlockData(pos3.add(0, -1, 0), EnumFacing.UP);
    }
    if (isPosSolid(pos3.add(-1, 0, 0))) {
        return new BlockData(pos3.add(-1, 0, 0), EnumFacing.EAST);
    }
    if (isPosSolid(pos3.add(1, 0, 0))) {
        return new BlockData(pos3.add(1, 0, 0), EnumFacing.WEST);
    }
    if (isPosSolid(pos3.add(0, 0, 1))) {
        return new BlockData(pos3.add(0, 0, 1), EnumFacing.NORTH);
    }
    if (isPosSolid(pos3.add(0, 0, -1))) {
        return new BlockData(pos3.add(0, 0, -1), EnumFacing.SOUTH);
    }
    BlockPos pos49 = pos.add(0, 0, -2);
    if (isPosSolid(pos4.add(0, -1, 0))) {
        return new BlockData(pos4.add(0, -1, 0), EnumFacing.UP);
    }
    if (isPosSolid(pos4.add(-1, 0, 0))) {
        return new BlockData(pos4.add(-1, 0, 0), EnumFacing.EAST);
    }
    if (isPosSolid(pos4.add(1, 0, 0))) {
        return new BlockData(pos4.add(1, 0, 0), EnumFacing.WEST);
    }
    if (isPosSolid(pos4.add(0, 0, 1))) {
        return new BlockData(pos4.add(0, 0, 1), EnumFacing.NORTH);
    }
    if (isPosSolid(pos4.add(0, 0, -1))) {
        return new BlockData(pos4.add(0, 0, -1), EnumFacing.SOUTH);
    }
    BlockPos pos5 = pos.add(0, -1, 0);
    if (isPosSolid(pos5.add(0, -1, 0))) {
        return new BlockData(pos5.add(0, -1, 0), EnumFacing.UP);
    }
    if (isPosSolid(pos5.add(-1, 0, 0))) {
        return new BlockData(pos5.add(-1, 0, 0), EnumFacing.EAST);
    }
    if (isPosSolid(pos5.add(1, 0, 0))) {
        return new BlockData(pos5.add(1, 0, 0), EnumFacing.WEST);
    }
    if (isPosSolid(pos5.add(0, 0, 1))) {
        return new BlockData(pos5.add(0, 0, 1), EnumFacing.NORTH);
    }
    if (isPosSolid(pos5.add(0, 0, -1))) {
        return new BlockData(pos5.add(0, 0, -1), EnumFacing.SOUTH);
    }
    BlockPos pos6 = pos5.add(1, 0, 0);
    if (isPosSolid(pos6.add(0, -1, 0))) {
        return new BlockData(pos6.add(0, -1, 0), EnumFacing.UP);
    }
    if (isPosSolid(pos6.add(-1, 0, 0))) {
        return new BlockData(pos6.add(-1, 0, 0), EnumFacing.EAST);
    }
    if (isPosSolid(pos6.add(1, 0, 0))) {
        return new BlockData(pos6.add(1, 0, 0), EnumFacing.WEST);
    }
    if (isPosSolid(pos6.add(0, 0, 1))) {
        return new BlockData(pos6.add(0, 0, 1), EnumFacing.NORTH);
    }
    if (isPosSolid(pos6.add(0, 0, -1))) {
        return new BlockData(pos6.add(0, 0, -1), EnumFacing.SOUTH);
    }
    BlockPos pos7 = pos5.add(-1, 0, 0);
    if (isPosSolid(pos7.add(0, -1, 0))) {
        return new BlockData(pos7.add(0, -1, 0), EnumFacing.UP);
    }
    if (isPosSolid(pos7.add(-1, 0, 0))) {
        return new BlockData(pos7.add(-1, 0, 0), EnumFacing.EAST);
    }
    if (isPosSolid(pos7.add(1, 0, 0))) {
        return new BlockData(pos7.add(1, 0, 0), EnumFacing.WEST);
    }
    if (isPosSolid(pos7.add(0, 0, 1))) {
        return new BlockData(pos7.add(0, 0, 1), EnumFacing.NORTH);
    }
    if (isPosSolid(pos7.add(0, 0, -1))) {
        return new BlockData(pos7.add(0, 0, -1), EnumFacing.SOUTH);
    }
    BlockPos pos8 = pos5.add(0, 0, 1);
    if (isPosSolid(pos8.add(0, -1, 0))) {
        return new BlockData(pos8.add(0, -1, 0), EnumFacing.UP);
    }
    if (isPosSolid(pos8.add(-1, 0, 0))) {
        return new BlockData(pos8.add(-1, 0, 0), EnumFacing.EAST);
    }
    if (isPosSolid(pos8.add(1, 0, 0))) {
        return new BlockData(pos8.add(1, 0, 0), EnumFacing.WEST);
    }
    if (isPosSolid(pos8.add(0, 0, 1))) {
        return new BlockData(pos8.add(0, 0, 1), EnumFacing.NORTH);
    }
    if (isPosSolid(pos8.add(0, 0, -1))) {
        return new BlockData(pos8.add(0, 0, -1), EnumFacing.SOUTH);
    }
    BlockPos pos9 = pos5.add(0, 0, -1);
    if (isPosSolid(pos9.add(0, -1, 0))) {
        return new BlockData(pos9.add(0, -1, 0), EnumFacing.UP);
    }
    if (isPosSolid(pos9.add(-1, 0, 0))) {
        return new BlockData(pos9.add(-1, 0, 0), EnumFacing.EAST);
    }
    if (isPosSolid(pos9.add(1, 0, 0))) {
        return new BlockData(pos9.add(1, 0, 0), EnumFacing.WEST);
    }
    if (isPosSolid(pos9.add(0, 0, 1))) {
        return new BlockData(pos9.add(0, 0, 1), EnumFacing.NORTH);
    }
    if (isPosSolid(pos9.add(0, 0, -1))) {
        return new BlockData(pos9.add(0, 0, -1), EnumFacing.SOUTH);
    }
    return null;
}

public void tower() {
	// TODO �Զ����ɵķ������
    BlockPos underPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1, mc.thePlayer.posZ);
    Block underBlock = mc.theWorld.getBlockState(underPos).getBlock();
    BlockData data = getBlockData(underPos);
    if(!mc.gameSettings.keyBindJump.isKeyDown()){
    	if((this.towermove.getValue() && PlayerUtil.isMoving2())){
    		if (MoveUtils.isOnGround(0.76) && !MoveUtils.isOnGround(0.75) && mc.thePlayer.motionY > 0.23 && mc.thePlayer.motionY < 0.25) {
                mc.thePlayer.motionY = (Math.round(mc.thePlayer.posY) - mc.thePlayer.posY);
            }
            if (MoveUtils.isOnGround(0.0001)) {   

            }else if(mc.thePlayer.motionY > 0.1 && mc.thePlayer.posY >= Math.round(mc.thePlayer.posY) - 0.0001 && mc.thePlayer.posY <= Math.round(mc.thePlayer.posY) + 0.0001){
               
            	mc.thePlayer.motionY = 0;
            }
    	}
    	return;
    }
	if(PlayerUtil.isMoving2()){
        if (MoveUtils.isOnGround(0.76) && !MoveUtils.isOnGround(0.75) && mc.thePlayer.motionY > 0.23 && mc.thePlayer.motionY < 0.25) {
            mc.thePlayer.motionY = (Math.round(mc.thePlayer.posY) - mc.thePlayer.posY);
        }
        if (MoveUtils.isOnGround(0.0001)) {            	
            mc.thePlayer.motionY = 0.42;
            mc.thePlayer.motionX *= 0.9;
            mc.thePlayer.motionZ *= 0.9;          	
        }else if(mc.thePlayer.posY >= Math.round(mc.thePlayer.posY) - 0.0001 && mc.thePlayer.posY <= Math.round(mc.thePlayer.posY) + 0.0001){
            mc.thePlayer.motionY = 0;
        }
	}else{
		mc.thePlayer.motionX = 0;
		mc.thePlayer.motionZ = 0;
		mc.thePlayer.jumpMovementFactor = 0;
		if (isAirBlock(underBlock) && data != null) {
            mc.thePlayer.motionY = 0.4196;
            mc.thePlayer.motionX *= 0.75;
            mc.thePlayer.motionZ *= 0.75;
        }
	}
}
/*     */   
/*     */   public static boolean isOnGround(double height) {
/* 293 */     Minecraft.getMinecraft().getMinecraft().getMinecraft(); if (!Minecraft.theWorld.getCollidingBoundingBoxes(Minecraft.thePlayer, Minecraft.thePlayer.getEntityBoundingBox().offset(0.0D, -height, 0.0D)).isEmpty()) {
/* 294 */       return true;
/*     */     }
/* 296 */     return false;
/*     */   }
/*     */   
public boolean isAirBlock(Block block) {
    if (block.getMaterial().isReplaceable()) {
        if (block instanceof BlockSnow && block.getBlockBoundsMaxY() > 0.125) {
            return false;
        }
        return true;
    }

    return false;
}
/*     */   
/*     */   public static boolean isMoving2() {
/* 310 */     Minecraft.getMinecraft(); if (Minecraft.thePlayer.moveForward == 0.0F) { Minecraft.getMinecraft(); if (Minecraft.thePlayer.moveStrafing == 0.0F)
/* 311 */         return false;  }
/*     */     
/* 313 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean placeBlock(BlockData data) {
/* 319 */     if (Minecraft.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), data.position, data.face, getVec3(data.position, data.face))) {
/* 320 */       Minecraft.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
/* 321 */       return true;
/*     */     } 
/* 323 */     return false;
/*     */   }

/*     */   private Vec3 grabPosition(BlockPos position, EnumFacing facing) {
/* 327 */     Vec3 offset = new Vec3(facing.getDirectionVec().getX() / 2.0D, facing.getDirectionVec().getY() / 2.0D, facing.getDirectionVec().getZ() / 2.0D);
/* 328 */     Vec3 point = new Vec3(position.getX() + 0.5D, position.getY() + 0.5D, position.getZ() + 0.5D);
/* 329 */     return point.add(offset);
/*     */   }
/*     */   
/*     */   private BlockCache grab() {
/* 333 */     EnumFacing[] invert = { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST };
/* 334 */     BlockPos position = (new BlockPos(Minecraft.thePlayer.getPositionVector())).offset(EnumFacing.DOWN);
/* 335 */     if (!(Minecraft.theWorld.getBlockState(position).getBlock() instanceof net.minecraft.block.BlockAir)) {
/* 336 */       return null;
/*     */     }
/* 338 */     EnumFacing[] var6 = EnumFacing.values();
/* 339 */     int var5 = var6.length;
/* 340 */     int offset = 0;
/* 341 */     while (offset < var5) {
/* 342 */       EnumFacing offsets = var6[offset];
/* 343 */       BlockPos offset1 = position.offset(offsets);
/* 344 */       Minecraft.theWorld.getBlockState(offset1);
/* 345 */       if (!(Minecraft.theWorld.getBlockState(offset1).getBlock() instanceof net.minecraft.block.BlockAir)) {
/* 346 */         return new BlockCache(this, offset1, invert[offsets.ordinal()], null);
/*     */       }
/* 348 */       offset++;
/*     */     } 
/*     */     
/* 351 */     BlockPos[] var16 = { new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1), new BlockPos(1, 0, 0) }, var19 = var16;
/* 352 */     int var18 = var16.length;
/* 353 */     var5 = 0;
/* 354 */     while (var5 < var18) {
/* 355 */       BlockPos var17 = var19[var5];
/* 356 */       BlockPos offsetPos = position.add(var17.getX(), 0, var17.getZ());
/* 357 */       Minecraft.theWorld.getBlockState(offsetPos);
/* 358 */       if (Minecraft.theWorld.getBlockState(offsetPos).getBlock() instanceof net.minecraft.block.BlockAir) {
/* 359 */         EnumFacing[] var13 = EnumFacing.values();
/* 360 */         int var12 = var13.length;
/* 361 */         int var11 = 0;
/* 362 */         while (var11 < var12) {
/* 363 */           EnumFacing facing2 = var13[var11];
/* 364 */           BlockPos offset2 = offsetPos.offset(facing2);
/* 365 */           Minecraft.theWorld.getBlockState(offset2);
/* 366 */           if (!(Minecraft.theWorld.getBlockState(offset2).getBlock() instanceof net.minecraft.block.BlockAir)) {
/* 367 */             return new BlockCache(this, offset2, invert[facing2.ordinal()], null);
/*     */           }
/* 369 */           var11++;
/*     */         } 
/*     */       } 
/* 372 */       var5++;
/*     */     } 
/*     */     
/* 375 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getBestBlocks() {
/* 381 */     if (getBlockCount() == 0)
/*     */       return; 
/* 383 */     if (((Boolean)this.pick.getValue()).booleanValue()) {
/* 384 */       ItemStack is = new ItemStack(Item.getItemById(261));
/* 385 */       int bestInvSlot = getBiggestBlockSlotInv();
/* 386 */       int bestHotbarSlot = getBiggestBlockSlotHotbar();
/* 387 */       int bestSlot = (getBiggestBlockSlotHotbar() > 0) ? getBiggestBlockSlotHotbar() : getBiggestBlockSlotInv();
/* 388 */       int spoofSlot = 42;
/* 389 */       if (bestHotbarSlot > 0 && bestInvSlot > 0 && 
/* 390 */         Minecraft.thePlayer.inventoryContainer.getSlot(bestInvSlot).getHasStack() && Minecraft.thePlayer.inventoryContainer.getSlot(bestHotbarSlot).getHasStack() && 
/* 391 */         (Minecraft.thePlayer.inventoryContainer.getSlot(bestHotbarSlot).getStack()).stackSize < (Minecraft.thePlayer.inventoryContainer.getSlot(bestInvSlot).getStack()).stackSize) {
/* 392 */         bestSlot = bestInvSlot;
/*     */       }
/*     */ 
/*     */       
/* 396 */       if (hotbarContainBlock()) {
/* 397 */         for (int a = 36; a < 45; a++) {
/* 398 */           if (Minecraft.thePlayer.inventoryContainer.getSlot(a).getHasStack()) {
/* 399 */             Item item = Minecraft.thePlayer.inventoryContainer.getSlot(a).getStack().getItem();
/* 400 */             if (item instanceof ItemBlock && isValid(item)) {
/* 401 */               spoofSlot = a;
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } else {
/* 407 */         for (int a = 36; a < 45; a++) {
/* 408 */           if (!Minecraft.thePlayer.inventoryContainer.getSlot(a).getHasStack()) {
/* 409 */             spoofSlot = a;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 415 */       if ((Minecraft.thePlayer.inventoryContainer.getSlot(spoofSlot)).slotNumber != bestSlot)
/*     */       {
/* 417 */         swap(bestSlot, spoofSlot - 36);
/* 418 */         Minecraft.playerController.updateController();
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 423 */     else if (invCheck()) {
/*     */       
/* 425 */       ItemStack is = new ItemStack(Item.getItemById(261));
/* 426 */       for (int i = 9; i < 36; i++) {
/*     */         
/* 428 */         if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
/* 429 */           Item item = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
/* 430 */           int count = 0;
/* 431 */           if (item instanceof ItemBlock && isValid(item)) {
/* 432 */             for (int a = 36; a < 45; a++) {
/* 433 */               if (Minecraft.thePlayer.inventoryContainer.canAddItemToSlot(Minecraft.thePlayer.inventoryContainer.getSlot(a), is, true)) {
/*     */                 
/* 435 */                 swap(i, a - 36);
/* 436 */                 count++;
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } 
/* 441 */             if (count == 0)
/*     */             {
/* 443 */               swap(i, 7);
/*     */             }
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 462 */   protected void swap(int slot, int hotbarNum) { Minecraft.playerController.windowClick(Minecraft.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, Minecraft.thePlayer); }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getBiggestBlockSlotInv() {
/* 467 */     int slot = -1;
/* 468 */     int size = 0;
/* 469 */     if (getBlockCount() == 0)
/* 470 */       return -1; 
/* 471 */     for (int i = 9; i < 36; i++) {
/* 472 */       if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
/* 473 */         Item item = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
/* 474 */         ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
/* 475 */         if (item instanceof ItemBlock && isValid(item) && 
/* 476 */           is.stackSize > size) {
/* 477 */           size = is.stackSize;
/* 478 */           slot = i;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 483 */     return slot;
/*     */   }
/*     */   
/*     */   public int getBiggestBlockSlotHotbar() {
/* 487 */     int slot = -1;
/* 488 */     int size = 0;
/* 489 */     if (getBlockCount() == 0)
/* 490 */       return -1; 
/* 491 */     for (int i = 36; i < 45; i++) {
/* 492 */       if (Minecraft.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
/* 493 */         Item item = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
/* 494 */         ItemStack is = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
/* 495 */         if (item instanceof ItemBlock && isValid(item) && 
/* 496 */           is.stackSize > size) {
/* 497 */           size = is.stackSize;
/* 498 */           slot = i;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 503 */     return slot;
/*     */   }
/*     */   
/*     */   private boolean hotbarContainBlock() {
/* 507 */     int i = 36;
/*     */     
/* 509 */     while (i < 45) {
/*     */       try {
/* 511 */         ItemStack stack = Minecraft.thePlayer.inventoryContainer.getSlot(i).getStack();
/* 512 */         if (stack == null || stack.getItem() == null || !(stack.getItem() instanceof ItemBlock) || !isValid(stack.getItem())) {
/* 513 */           i++;
/*     */           continue;
/*     */         } 
/* 516 */         return true;
/* 517 */       } catch (Exception exception) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 522 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private int grabBlockSlot() {
/* 527 */     int i2 = 0;
/* 528 */     while (i2 < 9) {
/* 529 */       ItemStack itemStack = Minecraft.thePlayer.inventory.mainInventory[i2];
/* 530 */       if (itemStack != null && itemStack.getItem() instanceof ItemBlock) {
/* 531 */         return i2;
/*     */       }
/* 533 */       i2++;
/*     */     } 
/* 535 */     return -1;
/*     */   }
static class BlockCache {
	private BlockPos position;
	private EnumFacing facing;
	final Scaffold this$0;

	private BlockCache(Scaffold var1, BlockPos position, EnumFacing facing) {
		this.this$0 = var1;
		this.position = position;
		this.facing = facing;
	}

	private BlockPos getPosition() {
		return this.position;
	}

	private EnumFacing getFacing() {
		return this.facing;
	}

	static BlockPos access$0(BlockCache var0) {
		return var0.getPosition();
	}

	static EnumFacing access$1(BlockCache var0) {
		return var0.getFacing();
	}

	static BlockPos access$2(BlockCache var0) {
		return var0.position;
	}

	static EnumFacing access$3(BlockCache var0) {
		return var0.facing;
	}

	BlockCache(Scaffold var1, BlockPos var2, EnumFacing var3, BlockCache var4) {
		this(var1, var2, var3);
	}
}
/*     */ }
class BlockData {
    public BlockPos position;
    public EnumFacing face;

    public BlockData(BlockPos position, EnumFacing face) {
        this.position = position;
        this.face = face;
    }
}

/* Location:              X:\MobileFile\sakill\Sakill.jar!\lac.LEFgangaDEV.LacClient\Module\Modules\Move\Scaffold.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */




/* Location:              C:\Users\Administrator\Desktop\Ice.jar!\com\enjoytheban\module\modules\movement\Scaffold.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */

