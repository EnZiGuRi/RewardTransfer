package me.LegendsMC.RewardTransfer.Events;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;


public class SignBreakEvent implements Listener {
	@EventHandler
	public void onSignBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if ((event.getBlock().getState() instanceof Sign)){
			final Sign sign = (Sign) event.getBlock().getState();
			if ((sign.getLine(0).contentEquals(ChatColor.DARK_RED
					+ "[Transfer]")) && ((sign.getLine(1)
					.contentEquals(ChatColor.DARK_BLUE + "Deposit"))
					|| (sign.getLine(1).contentEquals(ChatColor.DARK_BLUE
							+ "Withdraw")))) {
				if (player.hasPermission("rewardtransfer.sign.remove")) {
					player.sendMessage(ChatColor.DARK_GREEN
							+ "[RewardTransfer]: " + ChatColor.WHITE
							+ "Sign successfully removed!");
				} else {
					event.setCancelled(true);
					player.sendMessage(ChatColor.DARK_GREEN
							+ "[RewardTransfer] " + ChatColor.WHITE
							+ "You dont have permission to remove that sign!");
				}
			}
		}
	}
}