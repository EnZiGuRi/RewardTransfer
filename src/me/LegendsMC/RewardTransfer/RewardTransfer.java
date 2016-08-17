package me.LegendsMC.RewardTransfer;

import java.io.File;

import me.LegendsMC.RewardTransfer.Commands.CheckDB;
import me.LegendsMC.RewardTransfer.Events.SignBreakEvent;
import me.LegendsMC.RewardTransfer.Events.SignCreateEvent;
import me.LegendsMC.RewardTransfer.Listeners.InventoryClick;
import me.LegendsMC.RewardTransfer.Listeners.InventoryClose;
import me.LegendsMC.RewardTransfer.Listeners.SignInteract;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class RewardTransfer extends JavaPlugin {
	private static Plugin instance;
	MySQLManager mysql = new MySQLManager();
	String dbHost = getConfig().getString("Hostname");
	String dbUser = getConfig().getString("Username");
	String dbPass = getConfig().getString("Password");
	String dbPort = getConfig().getString("Port");
	String dbDatabase = getConfig().getString("Database");

	boolean DebugMode = getConfig().getBoolean("DebugMode");

	public void onEnable() {
		instance = this;
		initializeConfig();
		this.mysql.setupDB(this.dbHost, this.dbPort, this.dbDatabase,
				this.dbUser, this.dbPass);
		registerEvents();
		registerCommands();
		if (DebugMode == true) {
			Bukkit.getConsoleSender().sendMessage(
					ChatColor.GREEN + "[RewardTransfer] " + ChatColor.RED
							+ "Debug Mode ON");
		}
		Bukkit.getConsoleSender().sendMessage(
				ChatColor.GREEN + "[RewardTransfer] Enabled!");
	}

	public void onDisable() {
		this.mysql.closeDB();
		Bukkit.getConsoleSender().sendMessage(
				ChatColor.GREEN + "[RewardTransfer] Disabled!");
	}

	public void initializeConfig() {
		File configfile = new File(getDataFolder() + File.separator
				+ "config.yml");
		if (!configfile.exists()) {
			Bukkit.getConsoleSender().sendMessage(
					ChatColor.GREEN
							+ "[RewardTransfer] Generating config files...");
			getConfig().options().copyDefaults(true);
			saveConfig();
		}
	}

	public void registerEvent(Listener listener) {
		getServer().getPluginManager().registerEvents(listener, this);
	}

	private void registerEvents() {
		registerEvent(new SignCreateEvent());
		registerEvent(new SignBreakEvent());
		registerEvent(new SignInteract());
		registerEvent(new InventoryClose());
		registerEvent(new InventoryClick());
	}

	private void registerCommands() {
		getCommand("RewardTransfer").setExecutor(new CheckDB());
	}

	public static Plugin getInstance() {
		return instance;
	}
}