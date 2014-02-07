package net.cencraft.bungee.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * You are free to use and modify this library as long as you give credit to me.
 * 
 * @author BungeeTheCookie
 * 
 *         Thank you for including this into your plugin. Happy coding!
 */
public class ConfigurationAPI {

	static File file = null;
	static FileConfiguration FileConfiguration = null;

	public static void reloadConfig(JavaPlugin plugin, String string) {
		if (file == null) {
			file = new File(plugin.getDataFolder(), string);
		}
		FileConfiguration = YamlConfiguration.loadConfiguration(file);

		InputStream defConfigStream = plugin.getResource(string);
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration
					.loadConfiguration(defConfigStream);
			FileConfiguration.setDefaults(defConfig);
		}
	}

	public static FileConfiguration getConfig(JavaPlugin plugin, String string) {
		if (FileConfiguration == null) {
			reloadConfig(plugin, string);
		}
		return FileConfiguration;
	}

	public static void saveConfig(JavaPlugin plugin, String string) {
		if (FileConfiguration == null || file == null) {
			Bukkit.getServer()
					.getLogger()
					.severe("Could not save the file " + string
							+ " because it doesn't exist!");
			return;
		}
		try {
			getConfig(plugin, string).save(file);
		} catch (IOException ex) {
			plugin.getLogger().log(Level.SEVERE,
					"ERROR: Could not save config to " + file, ex);
		}
	}

	public static void saveDefaultConfig(JavaPlugin plugin, String string) {
		if (file == null) {
			file = new File(plugin.getDataFolder(), string);
		}
		if (!file.exists()) {
			plugin.saveResource(string, false);
		}
	}
}