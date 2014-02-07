package net.cencraft.bungee.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Bungee implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		Player p = (Player) sender;
		if (commandLabel.equalsIgnoreCase("runner")) {
			if (args.length == 1) {
				if (args[0].equalsIgnoreCase("start")) {
					//TODO make start for arena.
				}
			}
		}
		return false;
	}
}
