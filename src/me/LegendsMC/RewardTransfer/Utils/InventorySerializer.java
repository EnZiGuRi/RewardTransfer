package me.LegendsMC.RewardTransfer.Utils;

import java.util.ArrayList;
import java.util.Map;

import me.LegendsMC.RewardTransfer.RewardTransfer;
import net.minecraft.server.v1_10_R1.NBTTagCompound;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

public class InventorySerializer {

	// Global Variables

	static boolean DebugMode = RewardTransfer.getInstance().getConfig()
			.getBoolean("DebugMode");

	public static String InventoryToString(Inventory invInventory, Player player) {
		String serialization = invInventory.getSize() + ";";
		for (int i = 0; i < invInventory.getSize(); i++) {
			ItemStack itemStack = invInventory.getItem(i);
			if (itemStack != null) {
				String serializedItemStack = new String();
				String itemStackType = String.valueOf(itemStack.getType());
				serializedItemStack = serializedItemStack + "Item@"
						+ itemStackType;

				if (itemStack.getDurability() != 0) {
					String itemStackDurability = String.valueOf(itemStack
							.getDurability());
					serializedItemStack = serializedItemStack + ":Durability@"
							+ itemStackDurability;
				}

				if (itemStack.getAmount() != 1) {
					String itemStackAmount = String.valueOf(itemStack
							.getAmount());
					serializedItemStack = serializedItemStack + ":Amount@"
							+ itemStackAmount;
				}

				if (itemStack.getItemMeta().getDisplayName() != null) {
					String itemStackName = String.valueOf(itemStack
							.getItemMeta().getDisplayName());
					serializedItemStack = serializedItemStack + ":DisplayName@"
							+ itemStackName;
				}

				if (itemStack.getItemMeta().getLore() != null) {
					String itemStackLore = String.valueOf(itemStack
							.getItemMeta().getLore());
					if (!(itemStackLore == null)) {
						serializedItemStack = serializedItemStack
								+ ":DisplayLore@" + itemStackLore;
					}
				}
				Map<Enchantment, Integer> itemStackEnch = itemStack
						.getEnchantments();
				if (itemStackEnch.size() > 0) {
					for (Map.Entry<Enchantment, Integer> ench : itemStackEnch
							.entrySet()) {
						serializedItemStack = serializedItemStack
								+ ":Enchantment@" + ench.getKey().getId() + "@"
								+ ench.getValue();
					}
				}

				// Tipped Arrow

				if (itemStack.getType() == Material.TIPPED_ARROW) {
					PotionEffect TippedArrow = new PotionEffect(
							(Map<String, Object>) itemStack);
					String TippedArrowType = TippedArrow.getType().toString();
					int TippedArrowAmp = TippedArrow.getAmplifier();
					int TippedArrowDur = TippedArrow.getDuration();
					if (DebugMode == true) {
						player.sendMessage(ChatColor.GREEN + "Potion: "
								+ TippedArrowType.toString());
						player.sendMessage(ChatColor.GREEN + "Potion: "
								+ TippedArrowAmp);
						player.sendMessage(ChatColor.GREEN + "Potion: "
								+ TippedArrowDur);
					}
					/*
					 * if (!(itemStackLore == null)) { serializedItemStack =
					 * serializedItemStack + ":Potion@" + itemStackLore; }
					 */
				}

				// Potion

				/*
				 * if (itemStack.getType() == Material.POTION) {
				 * net.minecraft.server.v1_10_R1.ItemStack stack =
				 * CraftItemStack.asNMSCopy(item); NBTTagCompound tagCompound =
				 * stack.getTag(); if (DebugMode == true) {
				 * player.sendMessage(ChatColor.GREEN + "Potion: " +
				 * itemStackLore.toString()); player.sendMessage(ChatColor.GREEN
				 * + "Potion Type: " + PotionType.toString()); } if
				 * (!(itemStackLore == null)) { serializedItemStack =
				 * serializedItemStack + ":Potion@" + itemStackLore; } }
				 */

				// MONSTER_EGG

				if (itemStack.getType() == Material.MONSTER_EGG) {
					net.minecraft.server.v1_10_R1.ItemStack Monster_Egg = CraftItemStack
							.asNMSCopy(itemStack);
					if (Monster_Egg.hasTag()) {
						NBTTagCompound tag = Monster_Egg.getTag();
						NBTTagCompound entityTag = tag.getCompound("EntityTag");
						if (entityTag.hasKey("id")) {
							String EggEntity = entityTag.getString("id");
							serializedItemStack = serializedItemStack
									+ ":EntityTag@" + EggEntity;
						}
					}
				}

				// WRITTEN_BOOK

				if (itemStack.getType() == Material.WRITTEN_BOOK) {

					BookMeta book = (BookMeta) itemStack.getItemMeta();
					serializedItemStack = serializedItemStack + ":BookAuthor@"
							+ book.getAuthor();
					serializedItemStack = serializedItemStack + ":BookTitle@"
							+ book.getTitle();
					serializedItemStack = serializedItemStack
							+ ":BookGeneration@" + book.getGeneration();
					serializedItemStack = serializedItemStack
							+ ":BookPageAmount@" + book.getPageCount();
					Integer BookPageNumber = book.getPageCount();
					Integer i1 = 1;
					do {
						serializedItemStack = serializedItemStack + ":BookPage"
								+ i1 + "@" + book.getPage(i1);
						i1++;
					} while (i1 <= BookPageNumber);
				}

				// FIREWORK

				if (itemStack.getType() == Material.FIREWORK) {

					if (DebugMode == true) {
						ItemMeta FireworkMeta = itemStack.getItemMeta();
						player.sendMessage(ChatColor.GREEN + "FireworkMeta: "
								+ FireworkMeta.toString());
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
							serializedItemStack = serializedItemStack
									+ ":Meta@"
									+ ((Enchantment) bookenchants.getKey())
											.getId() + "@"
									+ bookenchants.getValue();
						}
					}
				}

				serialization += i + "#" + serializedItemStack + ";";
			}
		}
		return serialization;
	}

	public static Inventory StringToInventory(String invString, Player player) {
		String[] serializedBlocks = invString.split(";");
		String invInfo = serializedBlocks[0];
		Inventory deserializedInventory = Bukkit.getServer().createInventory(
				null, Integer.valueOf(invInfo).intValue());

		for (int i = 1; i < serializedBlocks.length; i++) {
			String[] serializedBlock = serializedBlocks[i].split("#");
			int stackPosition = Integer.valueOf(serializedBlock[0]);

			if (stackPosition >= deserializedInventory.getSize()) {
				continue;
			}
			ItemStack itemStack = null;
			Boolean createdItemStack = false;
			int Amount = 0;

			String[] serializedItemStack = serializedBlock[1].split(":");
			for (String itemInfo : serializedItemStack) {
				String[] itemAttribute = itemInfo.split("@");
				if (itemAttribute[0].equals("Item")) {
					itemStack = new ItemStack(Material.getMaterial(String
							.valueOf(itemAttribute[1])));
					createdItemStack = Boolean.valueOf(true);
				} else if (itemAttribute[0].equals("Durability")
						&& createdItemStack) {
					itemStack.setDurability(Short.valueOf(itemAttribute[1])
							.shortValue());
				} else if (itemAttribute[0].equals("Amount") && createdItemStack) {
					itemStack.setAmount(Integer.valueOf(itemAttribute[1])
							.intValue());
				} else if (itemAttribute[0].equals("Enchantment")
						&& createdItemStack) {
					itemStack.addEnchantment(Enchantment.getById(Integer.valueOf(
							itemAttribute[1]).intValue()),
							Integer.valueOf(itemAttribute[2]).intValue());
				} else if (itemAttribute[0].equals("Meta")) {
					EnchantmentStorageMeta itemStackMeta = (EnchantmentStorageMeta) itemStack
							.getItemMeta();
					itemStackMeta.addStoredEnchant(Enchantment.getById(Integer
							.valueOf(itemAttribute[1]).intValue()), Integer
							.valueOf(itemAttribute[2]).intValue(), true);
					itemStack.setItemMeta(itemStackMeta);
				} else if (itemAttribute[0].equals("DisplayName")
						&& createdItemStack) {
					ItemMeta itemStackMeta = itemStack.getItemMeta();
					itemStackMeta.setDisplayName(String.valueOf(itemAttribute[1]));
					itemStack.setItemMeta(itemStackMeta);
				} else if (itemAttribute[0].equals("DisplayLore")
						&& createdItemStack) {
					ItemMeta itemStackMeta = itemStack.getItemMeta();
					String removeBuckle = itemAttribute[1].substring(1,
							itemAttribute[1].length() - 1);
					ArrayList<String> Lore = new ArrayList<String>();
					for (String podpis : removeBuckle.split(", ")) {
						Lore.add(podpis);
					}
					itemStackMeta.setLore(Lore);
					itemStack.setItemMeta(itemStackMeta);
				} else if ((itemAttribute[0].equals("EntityTag"))
						&& (createdItemStack.booleanValue())) {

					net.minecraft.server.v1_10_R1.ItemStack nmsStack = CraftItemStack
							.asNMSCopy(itemStack);
					if (!(nmsStack.hasTag())) {
						NBTTagCompound tag = nmsStack.getTag();
						if (tag == null) {
							tag = new NBTTagCompound();
						}
						NBTTagCompound id = new NBTTagCompound();
						id.setString("id", String.valueOf(itemAttribute[1]));
						tag.set("EntityTag", id);
						nmsStack.setTag(tag);
						itemStack = CraftItemStack.asBukkitCopy(nmsStack);
					}
				}

				// Call Written Book

				if (itemAttribute[0].equals("BookPageAmount")) {
					Integer BookPageAmount = Integer.valueOf(itemAttribute[1]);
					Amount = BookPageAmount;
				}
				if ((itemAttribute[0].equals("BookTitle"))
						&& (createdItemStack.booleanValue())) {
					BookMeta bookTitle = (BookMeta) itemStack.getItemMeta();
					bookTitle.setTitle(String.valueOf(itemAttribute[1]).toString());
					itemStack.setItemMeta(bookTitle);
				}
				if ((itemAttribute[0].equals("BookAuthor"))
						&& (createdItemStack.booleanValue())) {
					BookMeta bookAuthor = (BookMeta) itemStack.getItemMeta();
					bookAuthor.setAuthor(String.valueOf(itemAttribute[1])
							.toString());
					itemStack.setItemMeta(bookAuthor);
				}
				if (itemAttribute[0].contains("BookPage")) {
					BookMeta bookPages = (BookMeta) itemStack.getItemMeta();
					String PageContent;
					for (int i1 = 1; i1 <= Amount; i1++) {
						if (itemAttribute[0].equals("BookPage" + i1)) {
							PageContent = String.valueOf(itemAttribute[1])
									.toString();
							bookPages.addPage(PageContent);
						}
					}
					itemStack.setItemMeta(bookPages);
				}
			}
			deserializedInventory.setItem(stackPosition, itemStack);
		}

		return deserializedInventory;
	}
}