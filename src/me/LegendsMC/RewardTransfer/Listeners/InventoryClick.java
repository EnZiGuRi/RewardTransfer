package me.LegendsMC.RewardTransfer.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;

public class InventoryClick implements Listener {

	@EventHandler
	public void onInvClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		Inventory inventory = event.getInventory();
		if (inventory.getName().contains("RewardTransfer Deposit")) {
			if ((event.getRawSlot() >= 0) && (event.getRawSlot() <= 53)) {
				if (player.hasPermission("rewardtransfer.deposit.bypass")) {
				} else {
					if (event.getCurrentItem().getType() == Material.AIR) {
						event.setCancelled(false);
					} else {
						player.sendMessage(ChatColor.DARK_GREEN
								+ "[RewardTransfer]: "
								+ ChatColor.WHITE
								+ "You are not allowed to withdraw items from there.");
						event.setCancelled(true);

					}
				}
			}
		}
		if (inventory.getName().contains("RewardTransfer Withdraw")) {
			if ((event.getRawSlot() >= 0) && (event.getRawSlot() <= 53)) {
				if (player.hasPermission("rewardtransfer.withdraw.bypass")) {
				} else {
					if (event.getCurrentItem().getType() != Material.AIR) {
						event.setCancelled(false);
					} else {
					player.sendMessage(ChatColor.DARK_GREEN
							+ "[RewardTransfer]: " + ChatColor.WHITE
							+ "You are not allowed to deposit items there.");
					event.setCancelled(true);
					}
				}
			}
		}
	}
}
