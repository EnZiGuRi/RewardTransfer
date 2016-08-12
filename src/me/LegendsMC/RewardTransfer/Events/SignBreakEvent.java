package me.LegendsMC.RewardTransfer.Events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class SignBreakEvent implements Listener {
	@EventHandler
	public void onSignBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Sign sign = (Sign) event.getBlock().getState();
		if (((event.getBlock().getType() == Material.SIGN)
				|| (event.getBlock().getType() == Material.SIGN_POST) || (event
				.getBlock().getType() == Material.WALL_SIGN))
				&& (((sign.getLine(0).contentEquals(ChatColor.DARK_RED
						+ "[Transfer]")) && (sign.getLine(1)
						.contentEquals(ChatColor.DARK_BLUE + "Deposit"))) || (sign
						.getLine(1).contentEquals(ChatColor.DARK_BLUE
						+ "Withdraw")))) {
			if (player.hasPermission("rewardtransfer.sign.remove")) {
				player.sendMessage(ChatColor.DARK_GREEN + "[RewardTransfer] "
						+ ChatColor.WHITE + "Sign successfully removed!");
			} else {
				event.setCancelled(true);
				player.sendMessage(ChatColor.DARK_GREEN + "[RewardTransfer] "
						+ ChatColor.WHITE
						+ "You dont have permission to remove that sign!");
			}
		}
	}
}