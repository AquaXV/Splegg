package com.raptorhosting.splegg.misc;

import org.bukkit.entity.Player;

public class Permissions {
	
	Player player;
	
	public Permissions(Player player) {
		this.player = player;
	}
	
	public boolean isAdmin() {
		return player.isOp() || player.hasPermission("splegg.admin");
	}
	
	public boolean canjoin() {
		return player.hasPermission("splegg.join") || isAdmin();
	}
	
	public boolean canModifyMaps() {
		return player.hasPermission("splegg.maps") || isAdmin();
	}
	
	public boolean canStartEnd() {
		return player.hasPermission("splegg.startend") || isAdmin();
	}
	
}
