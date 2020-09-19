package xyz.WorstClient.module.modules.world;

import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import xyz.WorstClient.Client;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Numbers;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.api.value.Value;
import xyz.WorstClient.management.ModuleManager;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.module.modules.combat.Killaura;
import xyz.WorstClient.utils.CombatUtil;
import xyz.WorstClient.utils.TimerUtil;

public class ChestAura extends Module {
   private ArrayList pos = new ArrayList();
   private TimerUtil timer = new TimerUtil();
   public static BlockPos lastBlock;
   private BlockPos blockCorner;
   private Numbers delay = new Numbers("Delay", "delay", Double.valueOf(100.0D), Double.valueOf(0.0D), Double.valueOf(1000.0D), Double.valueOf(10.0D));
   private Numbers reach = new Numbers("Reach", "Reach", Double.valueOf(4.0D), Double.valueOf(1.0D), Double.valueOf(7.0D), Double.valueOf(0.1D));
   private Option raytrace = new Option("rayTrace", "rayTrace", Boolean.valueOf(true));

   public ChestAura() {
      super("Chestaura", new String[]{"ChestAura"}, ModuleType.World);
      this.addValues(new Value[]{this.delay, this.reach, this.raytrace});
   }

   @EventHandler
   private void onUpdate(EventPreUpdate event) {
      this.getNextChest();
      if(lastBlock != null) {
         Minecraft var10000 = mc;
         if(Minecraft.thePlayer.getDistance((double)lastBlock.getX(), (double)lastBlock.getY(), (double)lastBlock.getZ()) > ((Double)this.reach.getValue()).doubleValue()) {
            lastBlock = null;
         }

         if(mc.currentScreen instanceof GuiChest) {
            this.pos.add(lastBlock);
            lastBlock = null;
         }
      }

      if(lastBlock == null) {
         this.blockCorner = null;
      }

      if(mc.currentScreen == null && this.isAllowed()) {
         BlockPos chest = lastBlock != null?lastBlock:this.getNextChest();
         if(chest != null && this.blockCorner != null) {
            float[] rot = CombatUtil.getRotationsNeededBlock((double)chest.getX(), (double)chest.getY(), (double)chest.getZ());
            event.yaw = rot[0];
            event.pitch = rot[1];
            //Client.RenderRotate(rot[0]);
            if(this.timer.hasReached((double)((Double)this.delay.getValue()).intValue())) {
               Minecraft var5 = mc;
               Minecraft.thePlayer.swingItem();
               var5 = mc;
               PlayerControllerMP var7 = Minecraft.playerController;
               Minecraft var10001 = mc;
               EntityPlayerSP var8 = Minecraft.thePlayer;
               Minecraft var10002 = mc;
               WorldClient var9 = Minecraft.theWorld;
               Minecraft var10003 = mc;
               ItemStack var10 = Minecraft.thePlayer.getHeldItem();
               Minecraft var10008 = mc;
               Minecraft var10009 = mc;
               Minecraft var10010 = mc;
               var7.onPlayerRightClick(var8, var9, var10, chest, EnumFacing.DOWN, new Vec3(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ));
               this.timer.reset();
               lastBlock = chest;
            }
         }
      }

   }

   private boolean isAllowed() {
      return !ModuleManager.getModuleByClass(Killaura.class).isEnabled() || Killaura.curTarget == null;
   }

   private BlockPos getNextChest() {
      Minecraft var10000 = mc;
      BlockPos var6 = Minecraft.thePlayer.getPosition().subtract(new Vec3i(((Double)this.reach.getValue()).doubleValue(), ((Double)this.reach.getValue()).doubleValue(), ((Double)this.reach.getValue()).doubleValue()));
      Minecraft var10001 = mc;
      Iterator<BlockPos> positions = BlockPos.getAllInBox(var6, Minecraft.thePlayer.getPosition().add(new Vec3i(((Double)this.reach.getValue()).doubleValue(), ((Double)this.reach.getValue()).doubleValue(), ((Double)this.reach.getValue()).doubleValue()))).iterator();
      BlockPos chestPos = null;

      while((chestPos = (BlockPos)positions.next()) != null) {
         Minecraft var7 = mc;
         if(Minecraft.theWorld.getBlockState(chestPos.add(0, 1, 0)).getBlock() instanceof BlockAir) {
            var7 = mc;
            if(Minecraft.theWorld.getBlockState(chestPos).getBlock() instanceof BlockChest && !this.pos.contains(chestPos)) {
               BlockPos var9;
               if(!((Boolean)this.raytrace.getValue()).booleanValue()) {
                  var9 = chestPos;
               } else {
                  Minecraft var10002 = mc;
                  double var10 = Minecraft.thePlayer.posX;
                  Minecraft var10003 = mc;
                  Minecraft var10004 = mc;
                  double var11 = Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight();
                  var10004 = mc;
                  var9 = Client.getBlockCorner(new BlockPos(var10, var11, Minecraft.thePlayer.posZ), chestPos);
               }

               BlockPos corner = var9;
               if(corner != null) {
                  this.blockCorner = corner;
                  return chestPos;
               }
            }
         }
      }

      return null;
   }
}
