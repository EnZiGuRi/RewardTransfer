package me.LegendsMC.RewardTransfer.Events;

import java.sql.SQLException;
import me.LegendsMC.RewardTransfer.MySQLManager;
import me.LegendsMC.RewardTransfer.Utils.ItemStackSerializer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class WithdrawEvent implements Listener {
	static MySQLManager mysql = new MySQLManager();
	static ItemStackSerializer serializer = new ItemStackSerializer();

	public static void WithdrawAction(Player player, ItemStack withdrawitem)
			throws SQLException {
		boolean invFull = false;
		while ((!invFull) && (mysql.checkItemDB(player))) {
			if (player.getInventory().firstEmpty() != -1) {
				mysql.withdrawItemDB(player);
				invFull = false;
			} else {
				player.sendMessage(ChatColor.GREEN
						+ "[RewardTransfer] Inventory full, cannot get items");
				invFull = true;
			}
		}
	}

	public static void giveItem(Player player, String serializedItem) {
		ItemStack unserializedItem = serializer.StringToItemStack(
				serializedItem, player);

		player.getInventory().addItem(new ItemStack[] { unserializedItem });
		player.updateInventory();;
		mysql.removeRow(player);
	}
}