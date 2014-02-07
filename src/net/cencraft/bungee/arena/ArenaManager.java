package net.cencraft.bungee.arena;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

public class ArenaManager {
	private static ArenaManager am = new ArenaManager();

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

		if (getArena(arenaName) != null) {
			Arena arena = getArena(arenaName);
			if (arena.getRedLocation() != null) {
				if (arena.getJoinLocation() != null) {
					if (arena.getBlueLocation() != null) {
						if (!arena.isFull()) {
							if (!arena.isInGame()) {
								if (!arena.getPlayers().contains(
										player.getName())) {
									player.setHealth(20.0);
									player.setFireTicks(0);
									player.teleport(arena.getJoinLocation());
									arena.getPlayers().add(player.getName());
									int playersLeft = arena.getMaxPlayers()
											- arena.getPlayers().size();
									arena.sendMessage(ChatColor.BLUE
											+ player.getName()
											+ " has joined the arena! We only need "
											+ playersLeft
											+ " to start the game!");

									if (playersLeft == 0) {
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
					Util.sendMessage(player,
							"§4This arena was not setup right. Contact an admin.");
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
				player.setHealth(20.0);
				player.setFireTicks(0);
				player.setSaturation(10);
				player.getInventory().setHelmet(new ItemStack(Material.AIR));
				player.teleport(arena.getRedLocation());
				arena.getPlayers().remove(player.getName());
				World world = Bukkit.getWorld("world");
				String path = "Player." + player.getName() + ".";
				double commandX = ConfigurationAPI.getConfig(plugin,
						"players.yml").getInt(path + "CommandX");
				double commandY = ConfigurationAPI.getConfig(plugin,
						"players.yml").getInt(path + "CommandY");
				double commandZ = ConfigurationAPI.getConfig(plugin,
						"players.yml").getInt(path + "CommandZ");
				Location endloc = new Location(world, commandX, commandY,
						commandZ);
				player.teleport(endloc);
				player.getInventory().clear();
				Util.sendMessage(player, "Removed from arena!");
				arena.sendMessage(ChatColor.BLUE + player.getName()
						+ " has left the Arena! There are "
						+ arena.getPlayers().size()
						+ " players currently left!");
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
			Arena arena = getArena(arenaName);
			arena.sendMessage(ChatColor.GOLD + "Paintball has BEGUN!");
			arena.setInGame(true);
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			objective.setDisplayName("§ePaintBall");
			objective.getScore(Bukkit.getOfflinePlayer("§4Red")).setScore(0);
			objective.getScore(Bukkit.getOfflinePlayer("§bBlue")).setScore(0);
			ItemStack snowball = new ItemStack(Material.SNOW_BALL, 64);

			for (String s : arena.getPlayers()) {
				Player player = Bukkit.getPlayer(s);
				player.setSaturation(10000);
				// TODO add method for seletcing what team players go on.
				player.setGameMode(GameMode.SURVIVAL);
				player.getInventory().addItem(snowball);
				Bukkit.getPlayer(s).teleport(arena.getRedLocation());
				player.getInventory().setHelmet(new ItemStack(Material.GLASS));
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
				player.teleport(arena.getJoinLocation());
				player.setGameMode(GameMode.SURVIVAL);
				player.setSaturation(100000);
				player.setHealth(20.0);
				player.setFireTicks(0);
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
				double redX = fc.getDouble("arenas." + keys + ".redX");
				double redY = fc.getDouble("arenas." + keys + ".redY");
				double redZ = fc.getDouble("arenas." + keys + ".redZ");
				double blueX = fc.getDouble("arenas." + keys + ".blueX");
				double blueY = fc.getDouble("arenas." + keys + ".blueY");
				double blueZ = fc.getDouble("arenas." + keys + ".blueZ");
				int maxPlayers = fc.getInt("arenas." + keys + ".maxPlayer");

				Location joinLocation = new Location(world, joinX, joinY, joinZ);
				Location redLocation = new Location(world, redX, redY, redZ);
				Location blueLocation = new Location(world, blueX, blueY, blueZ);
				@SuppressWarnings("unused")
				Arena arena = new Arena(keys, joinLocation, redLocation,
						blueLocation, maxPlayers);
			}
		}
	}

	public void createArena(String arenaName, int maxPlayers) {
		@SuppressWarnings("unused")
		Arena arena = new Arena(arenaName, null, null, null, maxPlayers);
		ConfigurationAPI.getConfig(plugin, "arenas.yml").set(
				"arenas." + arenaName, null);
		String path = "arenas." + arenaName + ".";
		ConfigurationAPI.getConfig(plugin, "arenas.yml").set(
				path + "maxPlayers", maxPlayers);
		ConfigurationAPI.saveConfig(plugin, "arenas.yml");
	}

	public void SetRed(String arenaName, Location redLocation) {
		FileConfiguration fc = ConfigurationAPI.getConfig(plugin, "arenas.yml");
		String path = "arenas." + arenaName + ".";
		fc.set(path + "redX", redLocation.getX());
		fc.set(path + "redY", redLocation.getY());
		fc.set(path + "redZ", redLocation.getZ());
		Arena arena = getArena(arenaName);
		arena.setRedLocation(redLocation);
		ConfigurationAPI.saveConfig(plugin, "arenas.yml");
	}

	public void SetJoin(String arenaName, Location joinLocation) {
		FileConfiguration fc = ConfigurationAPI.getConfig(plugin, "arenas.yml");
		String path = "arenas." + arenaName + ".";
		fc.set(path + "joinX", joinLocation.getX());
		fc.set(path + "joinY", joinLocation.getY());
		fc.set(path + "joinZ", joinLocation.getZ());
		Arena arena = getArena(arenaName);
		arena.setJoinLocation(joinLocation);
		ConfigurationAPI.saveConfig(plugin, "arenas.yml");
	}

	public void SetBlue(String arenaName, Location blueLocation) {
		FileConfiguration fc = ConfigurationAPI.getConfig(plugin, "arenas.yml");
		String path = "arenas." + arenaName + ".";
		fc.set(path + "blueX", blueLocation.getX());
		fc.set(path + "blueY", blueLocation.getY());
		fc.set(path + "blueZ", blueLocation.getZ());
		Arena arena = getArena(arenaName);
		arena.setBlueLocation(blueLocation);
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
