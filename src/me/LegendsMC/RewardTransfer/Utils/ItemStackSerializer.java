package me.LegendsMC.RewardTransfer.Utils;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

public class ItemStackSerializer {
	public String ItemStackToString(ItemStack itemStack, Player player) {
		String serialization = null;
		if (itemStack != null) {
			String serializedItemStack = new String();

			String itemStackType = String.valueOf(itemStack.getType().getId());
			serializedItemStack = serializedItemStack + "Item@" + itemStackType;
			if (itemStack.getDurability() != 0){
				String itemStackDurability = String.valueOf(itemStack
						.getDurability());
				serializedItemStack = serializedItemStack + ":d@"
						+ itemStackDurability;
			}
			
			//NOT FUCKING WORKING MONSTER_EGG
			
			/*if (itemStack.getType() == Material.MONSTER_EGG) {
				String EggType = String.valueOf(itemStack.getItemMeta());
				serializedItemStack = serializedItemStack + ":d@"
						+ EggType;

				player.sendMessage(ChatColor.GREEN
						+ EggType.toString());
			}*/
			if (itemStack.getAmount() != 1) {
				String itemStackAmount = String.valueOf(itemStack.getAmount());
				serializedItemStack = serializedItemStack + ":a@"
						+ itemStackAmount;
			}
			Map<Enchantment, Integer> itemStackEnch = itemStack
					.getEnchantments();
			if (itemStackEnch.size() > 0) {
				for (Map.Entry<Enchantment, Integer> ench : itemStackEnch
						.entrySet()) {
					serializedItemStack = serializedItemStack + ":e@"
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
						serializedItemStack = serializedItemStack + ":m@"
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
			} else if ((itemAttribute[0].equals("d"))
					&& (createdItemStack.booleanValue())) {
				itemStack.setDurability(Short.valueOf(itemAttribute[1])
						.shortValue());
			} else if ((itemAttribute[0].equals("a"))
					&& (createdItemStack.booleanValue())) {
				itemStack.setAmount(Integer.valueOf(itemAttribute[1])
						.intValue());
			} else if ((itemAttribute[0].equals("e"))
					&& (createdItemStack.booleanValue())) {
				itemStack.addEnchantment(Enchantment.getById(Integer.valueOf(
						itemAttribute[1]).intValue()),
						Integer.valueOf(itemAttribute[2]).intValue());
			} else if ((itemAttribute[0].equals("m"))
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