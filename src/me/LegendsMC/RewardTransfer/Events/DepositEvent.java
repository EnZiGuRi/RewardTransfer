package me.LegendsMC.RewardTransfer.Events;

import java.sql.SQLException;

import me.LegendsMC.RewardTransfer.MySQLManager;
import me.LegendsMC.RewardTransfer.RewardTransfer;
import me.LegendsMC.RewardTransfer.Utils.ItemStackSerializer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class DepositEvent implements Listener {
	static MySQLManager mysql = new MySQLManager();
	static ItemStackSerializer serializer = new ItemStackSerializer();
	
	static boolean DebugMode = RewardTransfer.getInstance().getConfig().getBoolean("DebugMode");

	public static void DepositAction(Player player, ItemStack deposititem)
			throws SQLException {
		int itemID = deposititem.getTypeId();
		String serializedItem = serializer.ItemStackToString(deposititem,
				player);
		if (itemID != 0) {
			mysql.depositItemDB(player, serializedItem);
			// Debug DB save
			if (DebugMode == true){
				player.sendMessage(ChatColor.GREEN + "Saving to DB: " + serializedItem);
			}
		}
		if (mysql.mysql_success()) {
			player.setItemInHand(null);
			player.updateInventory();
		}
	}
}