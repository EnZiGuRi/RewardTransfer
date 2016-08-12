package me.LegendsMC.RewardTransfer.Events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignCreateEvent implements Listener {
	@EventHandler
	public void onSignChange(SignChangeEvent event) {
		Player player = event.getPlayer();
		if ((event.getLine(0).equalsIgnoreCase("[Transfer]"))
				&& (event.getLine(1).equalsIgnoreCase("Deposit"))) {
			if (player.hasPermission("rewardtransfer.sign.create")) {
				event.setLine(0, ChatColor.DARK_RED + "[Transfer]");
				event.setLine(1, ChatColor.DARK_BLUE + "Deposit");
				event.setLine(2, ChatColor.DARK_BLUE + "To:");
				event.setLine(3, ChatColor.DARK_BLUE + "Towny");
				player.sendMessage(ChatColor.DARK_GREEN + "[RewardTransfer]: "
						+ ChatColor.WHITE
						+ "Deposit sign successfully created!");
			} else {
				event.setLine(0, "");
				event.setLine(1, "");
				event.setLine(2, "");
				event.setLine(3, "");
				player.sendMessage(ChatColor.DARK_GREEN
						+ "[RewardTransfer]: "
						+ ChatColor.WHITE
						+ "You dont have permission to create [RewardTransfer] signs!");
			}
		}
		if ((event.getLine(0).equalsIgnoreCase("[Transfer]"))
				&& (event.getLine(1).equalsIgnoreCase("Withdraw"))) {
			if (player.hasPermission("rewardtransfer.sign.create")) {
				event.setLine(0, ChatColor.DARK_RED + "[Transfer]");
				event.setLine(1, ChatColor.DARK_BLUE + "Withdraw");
				event.setLine(2, ChatColor.DARK_BLUE + "From:");
				event.setLine(3, ChatColor.DARK_BLUE + "Games");
				player.sendMessage(ChatColor.DARK_GREEN + "[RewardTransfer]: "
						+ ChatColor.WHITE
						+ "Withdraw sign successfully created!");
			} else {
				event.setLine(0, "");
				event.setLine(1, "");
				event.setLine(2, "");
				event.setLine(3, "");
				player.sendMessage(ChatColor.DARK_GREEN
						+ "[RewardTransfer]: "
						+ ChatColor.WHITE
						+ "You dont have permission to create [RewardTransfer] signs!");
			}
		}
	}
}