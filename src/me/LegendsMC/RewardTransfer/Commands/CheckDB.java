package me.LegendsMC.RewardTransfer.Commands;

import me.LegendsMC.RewardTransfer.MySQLManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CheckDB implements CommandExecutor {
	MySQLManager mysql = new MySQLManager();

	public boolean onCommand(CommandSender s, Command c, String a, String[] args) {
		if (c.getName().equalsIgnoreCase("RewardTransfer")) {
			if (args.length == 0) {
				s.sendMessage(ChatColor.GREEN
						+ "[RewardTransfer] Avaliable commands:");
				s.sendMessage(ChatColor.GREEN + "[RewardTransfer] "
						+ ChatColor.WHITE + "/RewardTransfer CheckDB");
				return true;
			}
			if ((args[0].equalsIgnoreCase("CheckDB")) && (args.length == 1)) {
				boolean check = this.mysql.checkDBConnection();
				if (check) {
					s.sendMessage(ChatColor.GREEN
							+ "[RewardTransfer] Database Status: Connected.");
				}
				if (!check) {
					s.sendMessage(ChatColor.GREEN
							+ "[RewardTransfer] Database Status: Disconnected.");
				}
			}
			if ((args[0].equalsIgnoreCase("OpenDB")) && (args.length == 1)) {
				this.mysql.OpenDB();
			}
			if ((args[0].equalsIgnoreCase("CloseDB")) && (args.length == 1)) {
				this.mysql.closeDB();
			}
		}
		return true;
	}
}