package net.cencraft.bungee.main;

import net.cencraft.bungee.util.PLogger;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	private static Main plugin;
	public final PLogger plo = new PLogger(this);

	@Override
	public void onEnable() {
		plugin = this;
		plo.enabled(true);
	}

	@Override
	public void onDisable() {
	plo.enabled(false);
	}

	public static JavaPlugin getJavaPlugin() {
		JavaPlugin plugin = Main.plugin;
		return plugin;
	}

	public static void registerEvents(org.bukkit.plugin.Plugin plugin,
			Listener... listeners) {
		for (Listener listener : listeners) {
			Bukkit.getServer().getPluginManager()
					.registerEvents(listener, plugin);
		}
	}
}
