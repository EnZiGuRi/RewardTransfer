package me.LegendsMC.RewardTransfer.Commands;

import me.LegendsMC.RewardTransfer.MySQLManager;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearDB implements CommandExecutor {
	static MySQLManager mysql = new MySQLManager();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (sender instanceof Player) {
			// String playerName = sender.getName();
			mysql.clearDB((Player) sender);
			if (mysql.mysql_success() == true) {
				sender.sendMessage(ChatColor.GREEN
						+ "[RewardTransfer] Cleared Database!");
			} else {
				sender.sendMessage(ChatColor.RED
						+ "[RewardTransfer] Error Clearing Database!");
			}
		} else {
			sender.sendMessage("[RewardTransfer] You must be a player!");
			return false;
		}
		// do something
		return false;
	}
}
