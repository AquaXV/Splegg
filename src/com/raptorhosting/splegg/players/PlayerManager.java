package com.raptorhosting.splegg.players;

import java.util.HashMap;

import org.bukkit.entity.Player;

public class PlayerManager {
	
	public HashMap<String, UtilPlayer> PLAYERS = new HashMap<String, UtilPlayer>();
	
	public UtilPlayer getPlayer(String name) {
		return (UtilPlayer)PLAYERS.get(name);
	}
	
	public UtilPlayer getPlayer(Player player) {
		return (UtilPlayer)PLAYERS.get(player.getName());
	}
	
}
