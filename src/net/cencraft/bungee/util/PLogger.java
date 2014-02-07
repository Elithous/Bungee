package net.cencraft.bungee.util;

import java.util.logging.Logger;

import net.cencraft.bungee.main.Main;

import org.bukkit.plugin.PluginDescriptionFile;

public class PLogger {
	public static Main plugin;

	public PLogger(Main instance) {
		plugin = instance;
	}

	public final Logger logger = Logger.getLogger("Minecraft");

	public void enabled(boolean enabled) {
		if (enabled) {
			PluginDescriptionFile pdfFile = plugin.getDescription();
			this.logger.info(pdfFile.getName() + " Version "
					+ pdfFile.getVersion() + " Has Been Enabled!");
		} else {
			PluginDescriptionFile pdfFile = plugin.getDescription();
			this.logger.info(pdfFile.getName() + " Version "
					+ pdfFile.getVersion() + " Has Been Disabled!");
		}
	}
}