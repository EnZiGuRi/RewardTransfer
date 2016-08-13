package me.LegendsMC.RewardTransfer.Utils;

import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.server.v1_10_R1.NBTTagCompound;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackSerializer {
	public String ItemStackToString(ItemStack itemStack, Player player) {
		String serialization = null;
		if (itemStack != null) {
			String serializedItemStack = new String();

			String itemStackType = String.valueOf(itemStack.getType().getId());
			serializedItemStack = serializedItemStack + "Item@" + itemStackType;

			if (itemStack.getItemMeta().getDisplayName() != null) {
				String itemStackmeta = String.valueOf(itemStack
						.getItemMeta().getDisplayName());
				serializedItemStack = serializedItemStack + ":DisplayName@"
						+ itemStackmeta;
			}
			
			if (itemStack.getDurability() != 0) {
				String itemStackDurability = String.valueOf(itemStack
						.getDurability());
				serializedItemStack = serializedItemStack + ":Durability@"
						+ itemStackDurability;
			}

			// MONSTER_EGG

			if (itemStack.getType() == Material.MONSTER_EGG) {
				net.minecraft.server.v1_10_R1.ItemStack nmsStack = CraftItemStack
						.asNMSCopy(itemStack);
				if (nmsStack.hasTag()) {
					NBTTagCompound tag = nmsStack.getTag();
					NBTTagCompound entityTag = tag.getCompound("EntityTag");
					if (entityTag.hasKey("id")) {
						String EggEntity = entityTag.getString("id");
						serializedItemStack = serializedItemStack + ":EntityTag@"
								+ EggEntity;
					}
				}
			}

			// FIREWORK

			/*
			 * if (itemStack.getType() == Material.FIREWORK) { String
			 * FirworkData = String.valueOf(itemStack.getItemMeta());
			 * serializedItemStack = serializedItemStack + ":d@" + FirworkData;
			 * 
			 * player.sendMessage(ChatColor.GREEN + FirworkData.toString()); }
			 */

			if (itemStack.getAmount() != 1) {
				String itemStackAmount = String.valueOf(itemStack.getAmount());
				serializedItemStack = serializedItemStack + ":Amount@"
						+ itemStackAmount;
			}
			Map<Enchantment, Integer> itemStackEnch = itemStack
					.getEnchantments();
			if (itemStackEnch.size() > 0) {
				for (Map.Entry<Enchantment, Integer> ench : itemStackEnch
						.entrySet()) {
					serializedItemStack = serializedItemStack + ":Enchantment@"
							+ ((Enchantment) ench.getKey()).getId() + "@"
							+ ench.getValue();
				}
			}
			if (itemStack.getType() == Material.ENCHANTED_BOOK) {
				EnchantmentStorageMeta bookmeta = (EnchantmentStorageMeta) itemStack
						.getItemMeta();
				Map<Enchantment, Integer> enchantments = bookmeta
						.getStoredEnchants();
				if (((Map) enchantments).size() > 0) {
					for (Map.Entry<Enchantment, Integer> bookenchants : (enchantments)
							.entrySet()) {
						serializedItemStack = serializedItemStack + ":Meta@"
								+ ((Enchantment) bookenchants.getKey()).getId()
								+ "@" + bookenchants.getValue();
					}
				}
			}
			serialization = serializedItemStack;
		}
		return serialization;
	}

	public ItemStack StringToItemStack(String serializedItem, Player player) {
		ItemStack itemStack = null;
		Boolean createdItemStack = Boolean.valueOf(false);
		String[] serializedItemStack = serializedItem.split(":");
		for (String itemInfo : serializedItemStack) {
			String[] itemAttribute = itemInfo.split("@");
			if (itemAttribute[0].equals("Item")) {
				itemStack = new ItemStack(Material.getMaterial(Integer.valueOf(
						itemAttribute[1]).intValue()));
				createdItemStack = Boolean.valueOf(true);
			} else if ((itemAttribute[0].equals("EntityTag"))
					&& (createdItemStack.booleanValue())) {
				itemStack.setTypeId(Integer.valueOf(itemAttribute[1])
						.intValue());
			} else if ((itemAttribute[0].equals("DisplayName"))
					&& (createdItemStack.booleanValue())) {
				ItemMeta itemStackMeta = itemStack.getItemMeta();
				itemStackMeta.setDisplayName(String.valueOf(itemAttribute[1]));
				itemStack.setItemMeta(itemStackMeta);
			} else if ((itemAttribute[0].equals("Durability"))
					&& (createdItemStack.booleanValue())) {
				itemStack.setDurability(Short.valueOf(itemAttribute[1])
						.shortValue());
			} else if ((itemAttribute[0].equals("Amount"))
					&& (createdItemStack.booleanValue())) {
				itemStack.setAmount(Integer.valueOf(itemAttribute[1])
						.intValue());
			} else if ((itemAttribute[0].equals("Enchantment"))
					&& (createdItemStack.booleanValue())) {
				itemStack.addEnchantment(Enchantment.getById(Integer.valueOf(
						itemAttribute[1]).intValue()),
						Integer.valueOf(itemAttribute[2]).intValue());
			} else if ((itemAttribute[0].equals("Meta"))
					&& (createdItemStack.booleanValue())) {
				EnchantmentStorageMeta itemStackMeta = (EnchantmentStorageMeta) itemStack
						.getItemMeta();
				itemStackMeta.addStoredEnchant(Enchantment.getById(Integer
						.valueOf(itemAttribute[1]).intValue()), Integer
						.valueOf(itemAttribute[2]).intValue(), true);
				itemStack.setItemMeta(itemStackMeta);
			}
		}
		ItemStack unserializedItem = itemStack;
		return unserializedItem;
	}
}