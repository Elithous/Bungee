package net.cencraft.bungee.util;

import org.bukkit.entity.Player;

public class Util {

	public static void sendMessage(Player player, String string) {
		player.sendMessage("[Bungee]" + string);
		
	}

}
