package me.LegendsMC.RewardTransfer.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
 
public class CustomInventory{
	public static void openCustomInventory(Player player){
        Inventory commandsInv = Bukkit.createInventory(null, 54, ChatColor.GREEN + "RewardTransfer Deposit");
       
       
        player.openInventory(commandsInv);
    }
}