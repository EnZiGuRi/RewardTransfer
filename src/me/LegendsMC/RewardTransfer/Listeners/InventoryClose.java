package me.LegendsMC.RewardTransfer.Listeners;

import me.LegendsMC.RewardTransfer.Utils.TransferInventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class InventoryClose implements Listener {
	
	@EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();
        Inventory inventory = event.getInventory();
        if(inventory.getName().contains("RewardTransfer Deposit") || (inventory.getName().contains("RewardTransfer Withdraw"))){
        	TransferInventory.saveInventory(player, inventory);
        }
    }
}
