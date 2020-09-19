package xyz.WorstClient.module.modules.player;

import xyz.WorstClient.api.EventHandler;
import xyz.WorstClient.api.events.world.EventPreUpdate;
import xyz.WorstClient.api.value.Option;
import xyz.WorstClient.module.Module;
import xyz.WorstClient.module.ModuleType;
import xyz.WorstClient.utils.TimerUtil;

import com.google.common.collect.Multimap;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemFlintAndSteel;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class InvCleaner extends Module {
	private static final Random RANDOM = new Random();
	public static List<Integer> blacklistedItems = new ArrayList<Integer>();
	private boolean allowSwitch = true;
	private boolean hasNoItems;
	private Random random = new Random();
	private static boolean isCleaning;
	public final TimerUtil timer = new TimerUtil();
	private Option<Boolean> TOGGLE = new Option<Boolean>("Toggle", "Toggle", false);
	public static int weaponSlot = 36, pickaxeSlot = 37, axeSlot = 38, shovelSlot = 39;

	public InvCleaner() {
		super("InvCleaner", new String[] { "inventorycleaner", "invclean" }, ModuleType.Player);
		this.setColor(Color.BLUE.getRGB());
		this.addValues(this.TOGGLE);
	}

	public static boolean isCleaning() {
		return isCleaning;
	}

	@Override
	public void onEnable() {
		isCleaning = false;
	}

	@Override
	public void onDisable() {
		isCleaning = false;
	}

	@EventHandler
	private void onUpdate(EventPreUpdate event) {
		if (mc.thePlayer != null && (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory)
				&& this.random.nextInt(2) == 0) {
			for (int i = 9; i < 45; ++i) {
				if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
					ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
					if (this.isBad(is) && is != mc.thePlayer.getCurrentEquippedItem()) {
						if (!isCleaning) {
							isCleaning = true;
							mc.thePlayer.sendQueue.addToSendQueue(new C16PacketClientStatus(
									C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
						}

						mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 0, 0,
								mc.thePlayer);
						mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, -999, 0, 0,
								mc.thePlayer);
						break;
					}
				}

				if (i == 44 && isCleaning) {
					isCleaning = false;
					mc.thePlayer.sendQueue.addToSendQueue(new C0DPacketCloseWindow(0));

					if (this.TOGGLE.getValue()) {
						this.setEnabled(false);
					}
				}
			}
		}
		if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory
				|| mc.currentScreen instanceof GuiChat) {
			if (weaponSlot >= 36) {

				if (!mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getHasStack()) {
					getBestWeapon(weaponSlot);

				} else {
					if (!isBestWeapon(mc.thePlayer.inventoryContainer.getSlot(weaponSlot).getStack())) {
						getBestWeapon(weaponSlot);
					}
				}

			}
		}
	}

	private boolean isBad(ItemStack item) {
		int swordCount = 0;

		for (int i = 0; i < 45; ++i) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (is.getItem() instanceof ItemSword) {
					++swordCount;
				}
			}
		}

		if (swordCount > 1 && item != null && item.getItem() instanceof ItemSword) {
			return this.getDamage(item) <= this.bestDamage();
		} else if (item == null || !(item.getItem() instanceof ItemArmor) || this.canEquip(item)
				|| this.betterCheck(item) && !this.canEquip(item)) {
			return item != null && !(item.getItem() instanceof ItemArmor)
					&& (item.getItem().getUnlocalizedName().contains("tnt")
							|| item.getItem().getUnlocalizedName().contains("stick")
							|| item.getItem().getUnlocalizedName().contains("egg")
							|| item.getItem().getUnlocalizedName().contains("string")
							|| item.getItem().getUnlocalizedName().contains("flint")
							|| item.getItem().getUnlocalizedName().contains("compass")
							|| item.getItem().getUnlocalizedName().contains("feather")
							|| item.getItem().getUnlocalizedName().contains("bucket")
							|| item.getItem().getUnlocalizedName().contains("gunpowder")
							|| item.getItem().getUnlocalizedName().contains("chest")
									&& !item.getDisplayName().toLowerCase().contains("collect")
							|| item.getItem().getUnlocalizedName().contains("snow")
							|| item.getItem().getUnlocalizedName().contains("fish")
							|| item.getItem().getUnlocalizedName().contains("enchant")
							|| item.getItem().getUnlocalizedName().contains("exp")
							|| item.getItem().getUnlocalizedName().contains("shears")
							|| item.getItem().getUnlocalizedName().contains("anvil")
							|| item.getItem().getUnlocalizedName().contains("torch")
							|| item.getItem().getUnlocalizedName().contains("seeds")
							|| item.getItem().getUnlocalizedName().contains("leather")
							|| item.getItem() instanceof ItemGlassBottle
							|| item.getItem().getUnlocalizedName().contains("piston")
							|| item.getItem().getUnlocalizedName().contains("potion") && this.isBadPotion(item));
		} else {
			return true;
		}
	}

	private double getProtectionValue(ItemStack stack) {
		return stack.getItem() instanceof ItemArmor ? (double) ((ItemArmor) stack.getItem()).damageReduceAmount
				+ (double) ((100 - ((ItemArmor) stack.getItem()).damageReduceAmount)
						* EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack)) * 0.0075D
				: 0.0D;
	}

	private boolean betterCheck(ItemStack stack) {
		try {
			if (stack.getItem() instanceof ItemArmor) {
				if (mc.thePlayer.getEquipmentInSlot(1) != null && stack.getUnlocalizedName().contains("boots")) {
					assert mc.thePlayer.getEquipmentInSlot(1).getItem() instanceof ItemArmor;

					if (this.getProtectionValue(stack)
							+ (double) ((ItemArmor) stack.getItem()).damageReduceAmount > this
									.getProtectionValue(mc.thePlayer.getEquipmentInSlot(1))
									+ (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(1)
											.getItem()).damageReduceAmount) {
						return true;
					}
				}

				if (mc.thePlayer.getEquipmentInSlot(2) != null && stack.getUnlocalizedName().contains("leggings")) {
					assert mc.thePlayer.getEquipmentInSlot(2).getItem() instanceof ItemArmor;

					if (this.getProtectionValue(stack)
							+ (double) ((ItemArmor) stack.getItem()).damageReduceAmount > this
									.getProtectionValue(mc.thePlayer.getEquipmentInSlot(2))
									+ (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(2)
											.getItem()).damageReduceAmount) {
						return true;
					}
				}

				if (mc.thePlayer.getEquipmentInSlot(3) != null && stack.getUnlocalizedName().contains("chestplate")) {
					assert mc.thePlayer.getEquipmentInSlot(3).getItem() instanceof ItemArmor;

					if (this.getProtectionValue(stack)
							+ (double) ((ItemArmor) stack.getItem()).damageReduceAmount > this
									.getProtectionValue(mc.thePlayer.getEquipmentInSlot(3))
									+ (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(3)
											.getItem()).damageReduceAmount) {
						return true;
					}
				}

				if (mc.thePlayer.getEquipmentInSlot(4) != null && stack.getUnlocalizedName().contains("helmet")) {
					assert mc.thePlayer.getEquipmentInSlot(4).getItem() instanceof ItemArmor;

					if (this.getProtectionValue(stack)
							+ (double) ((ItemArmor) stack.getItem()).damageReduceAmount > this
									.getProtectionValue(mc.thePlayer.getEquipmentInSlot(4))
									+ (double) ((ItemArmor) mc.thePlayer.getEquipmentInSlot(4)
											.getItem()).damageReduceAmount) {
						return true;
					}
				}
			}

			return false;
		} catch (Exception var3) {
			return false;
		}
	}

	private float bestDamage() {
		float bestDamage = 0.0F;

		for (int i = 0; i < 45; ++i) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (is.getItem() instanceof ItemSword && this.getDamage(is) > bestDamage) {
					bestDamage = this.getDamage(is);
				}
			}
		}

		return bestDamage;
	}

	private boolean canEquip(ItemStack stack) {
		assert stack.getItem() instanceof ItemArmor;

		return mc.thePlayer.getEquipmentInSlot(1) == null && stack.getUnlocalizedName().contains("boots")
				|| mc.thePlayer.getEquipmentInSlot(2) == null && stack.getUnlocalizedName().contains("leggings")
				|| mc.thePlayer.getEquipmentInSlot(3) == null && stack.getUnlocalizedName().contains("chestplate")
				|| mc.thePlayer.getEquipmentInSlot(4) == null && stack.getUnlocalizedName().contains("helmet");
	}

	private boolean isBadPotion(ItemStack stack) {
		if (stack != null && stack.getItem() instanceof ItemPotion) {
			ItemPotion potion = (ItemPotion) stack.getItem();
			if (ItemPotion.isSplash(stack.getItemDamage())) {
				Iterator var3 = potion.getEffects(stack).iterator();

				while (var3.hasNext()) {
					Object o = var3.next();
					PotionEffect effect = (PotionEffect) o;
					if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId()
							|| effect.getPotionID() == Potion.moveSlowdown.getId()
							|| effect.getPotionID() == Potion.weakness.getId()) {
						return true;
					}
				}
			}
		}

		return false;
	}

	public boolean isBestWeapon(ItemStack stack) {
		float damage = getDamage2(stack);
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (getDamage2(is) > damage && (is.getItem() instanceof ItemSword))
					return false;
			}
		}
		if ((stack.getItem() instanceof ItemSword)) {
			return true;
		} else {
			return false;
		}

	}

	public void getBestWeapon(int slot) {
		for (int i = 9; i < 45; i++) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				if (isBestWeapon(is) && (is.getItem() instanceof ItemSword)) {
					swap(i, slot - 36);
					timer.reset();
					break;
				}
			}
		}
	}

	private float getDamage2(ItemStack stack) {
		float damage = 0;
		Item item = stack.getItem();
		if (item instanceof ItemTool) {
			ItemTool tool = (ItemTool) item;
			damage += getAttackDamage(stack);
		}
		if (item instanceof ItemSword) {
			ItemSword sword = (ItemSword) item;
			damage += getAttackDamage(stack);
		}
		damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f
				+ EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01f;
		return damage;
	}

	private float getAttackDamage(ItemStack stack) {
		return !(stack.getItem() instanceof ItemSword) ? 0.0F
				: (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F
						+ ((ItemSword) stack.getItem()).getDamageVsEntity()
						+ (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01F;
	}

	public void swap(int slot1, int hotbarSlot) {
		mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot1, hotbarSlot, 2, mc.thePlayer);
	}

	private float getDamage(ItemStack stack) {
		return !(stack.getItem() instanceof ItemSword) ? -1.0F
				: (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25F
						+ ((ItemSword) stack.getItem()).getDamageVsEntity()
						+ (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack) * 0.01F;
	}
}
