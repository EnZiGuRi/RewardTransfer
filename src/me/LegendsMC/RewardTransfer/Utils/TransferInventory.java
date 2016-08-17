package me.LegendsMC.RewardTransfer.Utils;

import me.LegendsMC.RewardTransfer.MySQLManager;
import me.LegendsMC.RewardTransfer.RewardTransfer;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class TransferInventory{
	static InventorySerializer serializer = new InventorySerializer();
	static MySQLManager mysql = new MySQLManager();
	
	static boolean DebugMode = RewardTransfer.getInstance().getConfig().getBoolean("DebugMode");
	
	// Deposit

	public static void openDepositInventory(Player player) {
		Inventory CustomInv = Bukkit.createInventory(null, 54,
				ChatColor.DARK_GREEN + "RewardTransfer Deposit");
		String CodedInventory = mysql.getDBInventory(player);
		byte[] InventoryFromBase64 = Base64.decodeBase64(CodedInventory);
		String DecodedInventory = new String(InventoryFromBase64);
		Inventory unserializedItem = InventorySerializer.StringToInventory(
				DecodedInventory, player);
		CustomInv.setContents(unserializedItem.getContents());
		player.openInventory(CustomInv);
	}

	// Withdraw

	public static void openWithdrawInventory(Player player) {
		Inventory CustomInv = Bukkit.createInventory(null, 54,
				ChatColor.DARK_GREEN + "RewardTransfer Withdraw");
		String CodedInventory = mysql.getDBInventory(player);
		byte[] InventoryFromBase64 = Base64.decodeBase64(CodedInventory);
		String DecodedInventory = new String(InventoryFromBase64);
		Inventory unserializedItem = InventorySerializer.StringToInventory(
				DecodedInventory, player);
		CustomInv.setContents(unserializedItem.getContents());
		player.openInventory(CustomInv);
	}
	
	// Saving Inventory

	public static void saveInventory(Player player, Inventory inventory) {
		String SerializedInventory = InventorySerializer.InventoryToString(inventory, player);
		byte[] InventoryToBase64 = Base64.encodeBase64(SerializedInventory.getBytes());
		String CodedInventory = new String(InventoryToBase64);
		mysql.saveInventoryDB(player, CodedInventory);
	}
}