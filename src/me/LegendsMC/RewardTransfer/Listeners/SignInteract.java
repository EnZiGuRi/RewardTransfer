package me.LegendsMC.RewardTransfer.Listeners;

import java.sql.SQLException;
import java.util.Set;

import me.LegendsMC.RewardTransfer.Utils.TransferInventory;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignInteract implements Listener {
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerInteract(PlayerInteractEvent event) throws SQLException {
		Action action = event.getAction();
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if ((action.equals(Action.RIGHT_CLICK_AIR))
				&& (player.getInventory().getItemInMainHand().getTypeId() < 256)) {
			Set<Material> set = null;
			block = player.getTargetBlock(set, 5);
			if ((block != null)
					&& ((block.getType().equals(Material.WALL_SIGN)) || (block
							.getType().equals(Material.SIGN_POST)))) {
				action = Action.RIGHT_CLICK_BLOCK;
			}
		} else if (event.isCancelled()) {
			return;
		}
		if ((action.equals(Action.RIGHT_CLICK_BLOCK))
				&& ((block.getType().name().equals("SIGN_POST")) || (block
						.getType().name().equals("WALL_SIGN")))) {
			Sign sign = (Sign) block.getState();
			if (sign.getLine(0)
					.contentEquals(ChatColor.DARK_RED + "[Transfer]")) {
				event.setCancelled(true);
				if (sign.getLine(1).contentEquals(
						ChatColor.DARK_BLUE + "Deposit")) {
					if (player.hasPermission("rewardtransfer.sign.use.deposit")) {
						TransferInventory.openDepositInventory(player);
					} else {
						player.sendMessage(ChatColor.DARK_GREEN
								+ "[RewardTransfer] " + ChatColor.WHITE
								+ "You dont have permission to deposit items!");
					}
				}
				if (sign.getLine(1).contentEquals(
						ChatColor.DARK_BLUE + "Withdraw")) {
					if (player
							.hasPermission("rewardtransfer.sign.use.withdraw")) {
						TransferInventory.openWithdrawInventory(player);
					} else {
						player.sendMessage(ChatColor.DARK_GREEN
								+ "[RewardTransfer] " + ChatColor.WHITE
								+ "You dont have permission to withdraw items!");
					}
				}
			}
		}
	}
}