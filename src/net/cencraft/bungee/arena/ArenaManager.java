package net.cencraft.bungee.arena;

import java.util.ArrayList;

import net.cencraft.bungee.main.Main;
import net.cencraft.bungee.util.ConfigurationAPI;
import net.cencraft.bungee.util.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ArenaManager {
	private static ArenaManager am = new ArenaManager();
	JavaPlugin plugin = Main.getJavaPlugin();
	public static ArenaManager getManager() {
		return am;
	}

	public Arena getArena(String name) {
		for (Arena a : Arena.arenaObjects) {
			if (a.getName().equals(name)) {
				return a;
			}
		}
		return null;
	}

	public void addPlayers(Player player, String arenaName) {
// Test push
		if (getArena(arenaName) != null) {
			Arena arena = getArena(arenaName);
				if (arena.getStartLocation() != null && arena.getEndLocation() != null) {
						if (!arena.isFull()) {
							if (!arena.isInGame()) {
								if (!arena.getPlayers().contains(
										player.getName())) {
									player.setHealth(20.0);
									player.setFireTicks(0);
									player.teleport(arena.getStartLocation());
									arena.getPlayers().add(player.getName());
									int playersLeft = arena.getMaxPlayers()
											- arena.getPlayers().size();
									arena.sendMessage(ChatColor.BLUE
											+ player.getName()
											+ " has joined the arena! We only need "
											+ playersLeft
											+ " to start the game!");

									if (playersLeft == 0) {
										//TODO add runnable for arena countdown starts.
										startArena(arenaName);
									}
								} else {
									Util.sendMessage(player,
											"§4You are already in an arena!");
								}

							} else {
								player.sendMessage(ChatColor.RED
										+ "§4The arena you are looking for is currently running!");

							}
						} else {
							player.sendMessage(ChatColor.RED
									+ "§4The arena you are looking for is currently full!");
						}
					} else {
					Util.sendMessage(player,
							"§4This arena was not setup right. Contact an admin.");
				}
			} else {
			player.sendMessage(ChatColor.RED
					+ "§4The arena you are looking for could not be found!");
		}

	}

	public void removePlayer(Player player, String arenaName) {

		if (getArena(arenaName) != null) {
			Arena arena = getArena(arenaName);
			if (arena.getPlayers().contains(player.getName())) {
				
			} else {
				player.sendMessage(ChatColor.RED
						+ "Your not in the arena you're looking for!");

			}
		} else {
			player.sendMessage(ChatColor.RED
					+ "The arena you are looking for could not be found!");
		}
	}

	public void startArena(String arenaName) {

		if (getArena(arenaName) != null) {
			//declare arena, send message, set in game
			Arena arena = getArena(arenaName);
			arena.sendMessage(ChatColor.GOLD + "Paintball has BEGUN!");
			arena.setInGame(true);
			for (@SuppressWarnings("unused") String s : arena.getPlayers()) {
				//stuff for each player
			}
		}
	}

	public void endArena(String arenaName) {

		if (getArena(arenaName) != null) {
			Arena arena = getArena(arenaName);
			arena.sendMessage("§cThe arena has ended");
			ArrayList<String> players = new ArrayList<String>(
					arena.getPlayers());
			for (String s : players) {
				Player player = Bukkit.getPlayer(s);
				player.getInventory().clear();
				player.teleport(arena.getEndLocation());
				arena.getPlayers().remove(s);
			}
			arena.setInGame(false);
		}
	}

	public void loadArenas() {
		FileConfiguration fc = ConfigurationAPI.getConfig(plugin, "aenas.yml");

		if (fc.getConfigurationSection("arenas") != null) {
			for (String keys : fc.getConfigurationSection("arenas").getKeys(
					false)) {
				World world = Bukkit.getWorld("world");
				double joinX = fc.getDouble("arenas." + keys + ".joinX");
				double joinY = fc.getDouble("arenas." + keys + ".joinY");
				double joinZ = fc.getDouble("arenas." + keys + ".joinZ");
				double endX = fc.getDouble("arenas." + keys + ".endX");
				double endY = fc.getDouble("arenas." + keys + ".endY");
				double endZ = fc.getDouble("arenas." + keys + ".endZ");
				int maxPlayers = fc.getInt("arenas." + keys + ".maxPlayer");

				Location startLocation = new Location(world, joinX, joinY, joinZ);
				Location endLocation = new Location(world, endX, endY, endZ);
				
				@SuppressWarnings("unused")
				Arena arena = new Arena(keys, startLocation, endLocation, maxPlayers);
			}
		}
	}

	public void createArena(String arenaName, int maxPlayers) {
		@SuppressWarnings("unused")
		Arena arena = new Arena(arenaName, null, null, maxPlayers);
		ConfigurationAPI.getConfig(plugin, "arenas.yml").set(
				"arenas." + arenaName, null);
		String path = "arenas." + arenaName + ".";
		ConfigurationAPI.getConfig(plugin, "arenas.yml").set(
				path + "maxPlayers", maxPlayers);
		ConfigurationAPI.saveConfig(plugin, "arenas.yml");
	}

	public void SetEnd(String arenaName, Location joinLocation) {
		FileConfiguration fc = ConfigurationAPI.getConfig(plugin, "arenas.yml");
		String path = "arenas." + arenaName + ".";
		fc.set(path + "endX", joinLocation.getX());
		fc.set(path + "endY", joinLocation.getY());
		fc.set(path + "endZ", joinLocation.getZ());
		Arena arena = getArena(arenaName);
		arena.setEndLocation(joinLocation);
		ConfigurationAPI.saveConfig(plugin, "arenas.yml");
	}

	public void SetStart(String arenaName, Location blueLocation) {
		FileConfiguration fc = ConfigurationAPI.getConfig(plugin, "arenas.yml");
		String path = "arenas." + arenaName + ".";
		fc.set(path + "startX", blueLocation.getX());
		fc.set(path + "startY", blueLocation.getY());
		fc.set(path + "startZ", blueLocation.getZ());
		Arena arena = getArena(arenaName);
		arena.setStartLocation(blueLocation);
		ConfigurationAPI.saveConfig(plugin, "arenas.yml");
	}

	public void RemoveArena(Player player, String arenaName) {
		FileConfiguration fc = ConfigurationAPI.getConfig(plugin, "arenas.yml");
		String path = "arenas." + arenaName;
		fc.set(path, null);
		ConfigurationAPI.saveConfig(plugin, "arenas.yml");
		Util.sendMessage(player, "Arena removed.");
		Arena.arenaObjects.remove(getArena(arenaName));
	}
}
