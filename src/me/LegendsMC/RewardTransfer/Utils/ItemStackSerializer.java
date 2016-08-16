package me.LegendsMC.RewardTransfer.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.LegendsMC.RewardTransfer.RewardTransfer;
import net.minecraft.server.v1_10_R1.NBTTagCompound;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackSerializer {

	// Global Variables

	boolean DebugMode = RewardTransfer.getInstance().getConfig()
			.getBoolean("DebugMode");

	// Serialize

	public String ItemStackToString(ItemStack itemStack, Player player) {
		String serialization = null;
		if (itemStack != null) {
			String serializedItemStack = new String();
			String itemStackType = String.valueOf(itemStack.getType());
			serializedItemStack = serializedItemStack + "Item@" + itemStackType;

			if (itemStack.getItemMeta().getDisplayName() != null) {
				String itemStackmeta = String.valueOf(itemStack.getItemMeta()
						.getDisplayName());
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
				serializedItemStack = serializedItemStack + ":BookGeneration@"
						+ book.getGeneration();
				serializedItemStack = serializedItemStack + ":BookPageAmount@"
						+ book.getPageCount();
				Integer BookPageNumber = book.getPageCount();
				Integer i = 1;
				do {
					serializedItemStack = serializedItemStack + ":BookPage" + i
							+ "@" + book.getPage(i);
					i++;
				} while (i <= BookPageNumber);
			}

			// FIREWORK

			if (itemStack.getType() == Material.FIREWORK) {

				if (DebugMode == true) {
					ItemMeta FireworkMeta = itemStack.getItemMeta();
					player.sendMessage(ChatColor.GREEN
							+ "FireworkMeta: " + FireworkMeta.toString());
				}
			}

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

	/***************************************/
	/************* Deserialize *************/
	/***************************************/

	String[] auxAttribute;
	Player nplayer;
	ItemStack book;
	int Amount;

	// Auxiliary Methods

	/*
	 * // Get Item Name
	 * 
	 * public Material GetitemName() { return itemName; }
	 * 
	 * public void SetitemName() { if (itemAttribute[0].equals("Item")) {
	 * itemName = Material.getMaterial(String.valueOf( itemAttribute[1])); } }
	 * 
	 * //Item Amount
	 * 
	 * public int GetitemAmount() { return itemAmount; }
	 * 
	 * public void SetitemAmount() {
	 * 
	 * if (itemAttribute[0].equals("Amount")) { itemAmount =
	 * Integer.valueOf(itemAttribute[1]) .intValue(); }
	 * 
	 * }
	 */

	// Written Book

	public ItemStack GetWrittenBook() {
		return book;
	}

	public void SetWrittenBook(ItemStack itemStack) {
		int Amount = 0;
		BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();
		if (auxAttribute[0].equals("BookPageAmount")) {
			Integer BookPageAmount = Integer.valueOf(auxAttribute[1]);
			Amount = BookPageAmount;
		}
		if (auxAttribute[0].equals("BookTitle")) {
			bookMeta.setTitle(String.valueOf(auxAttribute[1]).toString());
		}
		if (auxAttribute[0].equals("BookAuthor")) {
			bookMeta.setAuthor(String.valueOf(auxAttribute[1]).toString());
		}

		if (auxAttribute[0].contains("BookPage")) {
			BookMeta bookPages = (BookMeta) itemStack.getItemMeta();
			List<String> pages = new ArrayList<String>();
			String PageContent;
			Integer i = 1;
			do {
				if (auxAttribute[0].equals("BookPage" + i)) {
					PageContent = String.valueOf(auxAttribute[1]).toString();
					nplayer.sendMessage(ChatColor.GREEN + "PageAmount: "
							+ Amount);
					nplayer.sendMessage(ChatColor.GREEN + "PageContent: "
							+ PageContent + "Loop:" + i);
					pages.add("This will be page one.");
					pages.add("This will be page two.");
				}
				i++;
			} while (i >= 1 && i <= Amount);

			bookPages.setPages(pages);
			itemStack.setItemMeta(bookPages);
		}

		/*
		 * String PageContent; int i = 1; BookMeta meta = (BookMeta)
		 * itemStack.getItemMeta();
		 * 
		 * if (auxAttribute[0].equals("BookTitle")) {
		 * meta.setTitle(String.valueOf(auxAttribute[1])); } if
		 * (auxAttribute[0].equals("BookAuthor")) {
		 * meta.setAuthor(String.valueOf(auxAttribute[1])); } if
		 * (auxAttribute[0].equals("BookPageAmount")) { bookPageAmount =
		 * Integer.valueOf(auxAttribute[1]); }
		 * 
		 * List<String> pages = Lists.newArrayList();
		 * 
		 * do { if (auxAttribute[0].equals("BookPage" + i)) { PageContent =
		 * String.valueOf(auxAttribute[1]).toString();
		 * nplayer.sendMessage(ChatColor.GREEN + "PageAmount: " +
		 * bookPageAmount); nplayer.sendMessage(ChatColor.GREEN +
		 * "PageContent: " + PageContent + "Loop:" + i); pages.add(PageContent);
		 * } i++; } while (i >= 1 && i <= bookPageAmount); meta.setPages(pages);
		 * book.setItemMeta(meta);
		 */
	}

	// /////////////////////////////

	public ItemStack StringToItemStack(String serializedItem, Player player) {
		nplayer = player;
		ItemStack itemStack = null;
		Boolean createdItemStack = Boolean.valueOf(false);
		String[] serializedItemStack = serializedItem.split(":");
		for (String itemInfo : serializedItemStack) {
			String[] itemAttribute = itemInfo.split("@");
			auxAttribute = itemAttribute;
			if (itemAttribute[0].equals("Item")) {
				itemStack = new ItemStack(Material.getMaterial(String
						.valueOf(itemAttribute[1])));
				createdItemStack = Boolean.valueOf(true);
			} else if (itemAttribute[0].equals("DisplayName")) {
				ItemMeta itemStackMeta = itemStack.getItemMeta();
				itemStackMeta.setDisplayName(String.valueOf(itemAttribute[1]));
				itemStack.setItemMeta(itemStackMeta);
			} else if (itemAttribute[0].equals("Durability")) {
				itemStack.setDurability(Short.valueOf(itemAttribute[1])
						.shortValue());
			} else if (itemAttribute[0].equals("Amount")) {
				itemStack.setAmount(Integer.valueOf(itemAttribute[1])
						.intValue());
			} else if (itemAttribute[0].equals("Enchantment")) {
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
			}
			// Call Written Book

			if (auxAttribute[0].equals("BookPageAmount")) {
				Integer BookPageAmount = Integer.valueOf(auxAttribute[1]);
				Amount = BookPageAmount;
			}
			if ((itemAttribute[0].equals("BookTitle"))
					&& (createdItemStack.booleanValue())) {
				BookMeta bookTitle = (BookMeta) itemStack.getItemMeta();
				bookTitle.setTitle(String.valueOf(itemAttribute[1])
						.toString());
				itemStack.setItemMeta(bookTitle);
			}
			if ((itemAttribute[0].equals("BookAuthor"))
					&& (createdItemStack.booleanValue())) {
				BookMeta bookAuthor = (BookMeta) itemStack.getItemMeta();
				bookAuthor.setAuthor(String.valueOf(itemAttribute[1])
						.toString());
				itemStack.setItemMeta(bookAuthor);
			}
			if (auxAttribute[0].contains("BookPage")) {
				BookMeta bookPages = (BookMeta) itemStack.getItemMeta();
				String PageContent;
				for (int i = 1; i <= Amount; i++) {
					if (auxAttribute[0].equals("BookPage" + i)) {
						PageContent = String.valueOf(auxAttribute[1])
								.toString();
						if (DebugMode == true) {
							nplayer.sendMessage(ChatColor.GREEN
									+ "PageAmount: " + bookPages.toString());
							nplayer.sendMessage(ChatColor.GREEN
									+ "PageContent: " + PageContent + "Loop:"
									+ i);
						}
						bookPages.addPage(PageContent);
					}
				}
				itemStack.setItemMeta(bookPages);
			}
		}
		ItemStack unserializedItem = itemStack;
		return unserializedItem;
	}
}