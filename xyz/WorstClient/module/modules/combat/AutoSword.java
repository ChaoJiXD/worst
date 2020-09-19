package xyz.WorstClient.module.modules.combat;

import com.google.common.collect.Multimap;
import java.awt.Color;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.misc.Event;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.utils.InventoryUtils;
import xyz.WorstClient.utils.TimerUtil;

public class AutoSword
extends Module {
    private ItemStack bestSword;
    private ItemStack prevBestSword;
    private boolean shouldSwitch = false;
    public TimerUtil timer = new TimerUtil();
    private Option<Boolean> swap = new Option<Boolean>("swap", "swap", true);
    public AutoSword() {
        super("AutoSword", new String[]{"autosword"}, ModuleType.Combat);
        this.setColor(new Color(208, 30, 142).getRGB());
        addValues(this.swap);
    }

    @EventHandler
    public void onEvent(Event event) {
        EventPreUpdate em = (EventPreUpdate)event;
        if (em.isPre() && (mc.currentScreen instanceof GuiInventory || mc.currentScreen == null)) {
           int i;
           Item item;
           if (mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
              if (this.timer.delay(100.0F)) {
                 for(i = 0; i < 45; ++i) {
                    if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                       item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                       if (item instanceof ItemSword) {
                          float itemDamage = this.getAttackDamage(mc.thePlayer.inventoryContainer.getSlot(i).getStack());
                          float currentDamage = this.getAttackDamage(mc.thePlayer.getCurrentEquippedItem());
                          if (itemDamage > currentDamage) {
                             this.swap(i, mc.thePlayer.inventory.currentItem);
                             this.timer.reset();
                             break;
                          }
                       }
                    }
                 }
              }
           } if (this.timer.delay(0.0F)) {
          	if (((Boolean)this.swap.getValue()).booleanValue()) {
                   for(i = 36; i < 45; ++i) {
                      if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                         item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                         if (item instanceof ItemSword && (Killaura.curTarget != null || !Killaura.targets.isEmpty())) {
                            mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem = i - 36));
                            mc.playerController.updateController();
                            break;
                         }
                      }
                   }
                } 
           }
          	 
           
           
        }
        
     }

     protected void swap(int slot, int hotbarNum) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
     }

     private float getAttackDamage(ItemStack stack) {
        return !(stack.getItem() instanceof ItemSword) ? 0.0F : (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F + ((ItemSword)stack.getItem()).getDamageVsEntity() + (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01F;
     }
  }


