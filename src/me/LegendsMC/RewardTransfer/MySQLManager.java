package me.LegendsMC.RewardTransfer;

import code.husky.mysql.MySQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import me.LegendsMC.RewardTransfer.Events.WithdrawEvent;
import me.LegendsMC.RewardTransfer.Utils.TransferInventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MySQLManager {
	private static MySQL db;
	boolean mysql_success = false;
	int rowID;
	public boolean noItems;

	public void setupDB(String dbHost, String dbPort, String dbDatabase,
			String dbUser, String dbPass) {
		try {
			db = new MySQL(null, dbHost, dbPort, dbDatabase, dbUser, dbPass);
			db.openConnection();
			Statement statement = db.getConnection().createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS RewardTransfer (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, player_uuid varchar(512), item varchar(512))";
			statement.executeUpdate(sql);
			Bukkit.getConsoleSender()
					.sendMessage(
							ChatColor.GREEN
									+ "[RewardTransfer] Successfully connected to database!");
		} catch (SQLException e) {
			Bukkit.getConsoleSender().sendMessage(
					ChatColor.RED
							+ "[RewardTransfer] Can't connect to database!");
		}
	}

	public void saveInventoryDB(Player player, String serializedItem) {
		try {
			db.openConnection();
			Statement statement = db.getConnection().createStatement();
			String playerUUID = player.getUniqueId().toString();
			if (checkItemDB(player)) {
				String sql = "UPDATE RewardTransfer " + "SET item = '"
						+ serializedItem + "' " + "WHERE player_uuid = '"
						+ playerUUID + "'";
				statement.executeUpdate(sql);
				this.mysql_success = true;
			} else {
				String sql = "INSERT INTO RewardTransfer (player_uuid, item) VALUES ('"
						+ playerUUID + "', 'NTQ7')";
				statement.executeUpdate(sql);
				this.mysql_success = true;
			}
		} catch (SQLException e) {
			closeDB();
			Bukkit.getConsoleSender().sendMessage(
					ChatColor.RED
							+ "[RewardTransfer] Can't Deposit to database! "
							+ e.getMessage());
			this.mysql_success = false;
		}
	}

	public void depositItemDB(Player player, String serializedItem) {
		try {
			db.openConnection();
			Statement statement = db.getConnection().createStatement();
			String playerUUID = player.getUniqueId().toString();
			String sql = "INSERT INTO RewardTransfer (player_uuid, item) VALUES ('"
					+ playerUUID + "', '" + serializedItem + "')";
			statement.executeUpdate(sql);
			this.mysql_success = true;
		} catch (SQLException e) {
			closeDB();
			Bukkit.getConsoleSender().sendMessage(
					ChatColor.RED
							+ "[RewardTransfer] Can't Deposit to database! "
							+ e.getMessage());
			this.mysql_success = false;
		}
	}

	public String getDBInventory(Player player) {
		String serializedItem = null;
		try {
			db.openConnection();
			Statement statement = db.getConnection().createStatement();
			String playerUUID = player.getUniqueId().toString();
			if (checkItemDB(player)) {
				String sql = "SELECT * FROM RewardTransfer WHERE player_uuid = '"
						+ playerUUID + "'";
				ResultSet result = statement.executeQuery(sql);
				if (result.next()) {
					this.rowID = result.getInt(1);
					serializedItem = result.getString(3);
				}
				this.mysql_success = true;
			} else {
				String sql = "INSERT INTO RewardTransfer (player_uuid, item) VALUES ('"
						+ playerUUID + "', 'NTQ7')";
				statement.executeUpdate(sql);
				this.mysql_success = true;
			}
		} catch (SQLException e) {
			closeDB();
			Bukkit.getConsoleSender().sendMessage(
					ChatColor.RED
							+ "[RewardTransfer] Can't Withdraw from database! "
							+ e.getMessage());
			this.mysql_success = false;
		}
		return serializedItem;
	}

	public void withdrawItemDB(Player player) {
		try {
			db.openConnection();
			Statement statement = db.getConnection().createStatement();
			String playerUUID = player.getUniqueId().toString();
			String sql = "SELECT * FROM RewardTransfer WHERE player_uuid = '"
					+ playerUUID + "'";
			ResultSet result = statement.executeQuery(sql);
			if (result.next()) {
				this.rowID = result.getInt(1);
				String serializedItem = result.getString(3);
				WithdrawEvent.giveItem(player, serializedItem);
			}
			this.mysql_success = true;
		} catch (SQLException e) {
			closeDB();
			Bukkit.getConsoleSender().sendMessage(
					ChatColor.RED
							+ "[RewardTransfer] Can't Withdraw from database! "
							+ e.getMessage());
			this.mysql_success = false;
		}
	}

	public boolean checkItemDB(Player player) {
		try {
			db.openConnection();
			Statement statement = db.getConnection().createStatement();
			String playerUUID = player.getUniqueId().toString();
			String sql = "SELECT * FROM RewardTransfer WHERE player_uuid = '"
					+ playerUUID + "'";
			ResultSet result = statement.executeQuery(sql);
			if (result.next()) {
				return true;
			}
			closeDB();
			return false;
		} catch (SQLException e) {
			this.mysql_success = false;
		}
		return false;
	}

	public void removeRow(Player player) {
		try {
			db.openConnection();
			Statement statement = db.getConnection().createStatement();
			String playerUUID = player.getUniqueId().toString();
			String sql = "DELETE FROM RewardTransfer WHERE player_uuid = '"
					+ playerUUID + "' AND id = '" + this.rowID + "'";
			statement.executeUpdate(sql);
			this.mysql_success = true;
		} catch (SQLException e) {
			closeDB();
			Bukkit.getConsoleSender()
					.sendMessage(
							ChatColor.RED
									+ "[RewardTransfer] Can't remove item from database! "
									+ e.getMessage());
			this.mysql_success = false;
		}
	}

	public void clearDB(Player player) {
		try {
			db.openConnection();
			Statement statement = db.getConnection().createStatement();
			String playerUUID = player.getUniqueId().toString();
			String sql = "DELETE FROM RewardTransfer WHERE player_uuid = '"
					+ playerUUID + "'";
			statement.executeUpdate(sql);
			this.mysql_success = true;
		} catch (SQLException e) {
			closeDB();
			Bukkit.getConsoleSender().sendMessage(
					ChatColor.RED + "[RewardTransfer] Can't clear database! "
							+ e.getMessage());
			this.mysql_success = false;
		}
	}

	public void closeDB() {
		db.closeConnection();
	}

	public void OpenDB() {
		db.openConnection();
	}

	public boolean mysql_success() {
		return this.mysql_success;
	}

	public boolean checkDBConnection() {
		return db.checkConnection();
	}
}