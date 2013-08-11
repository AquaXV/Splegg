package com.raptorhosting.splegg.misc;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.raptorhosting.splegg.Splegg;

public class UpdateUtils implements Listener {
	
	@EventHandler
	public void updateJoin(PlayerJoinEvent e) {
		
		Player player = e.getPlayer();
		Permissions perms = new Permissions(player);
		
		if (perms.isAdmin()) {
			if (Splegg.getSplegg().updateOut) {
				Splegg.getSplegg().chat.sendMessage(player, "&bSplegg update is available: &d" + Splegg.getSplegg().newVer + "");
				Splegg.getSplegg().chat.sendMessage(player, "&bTo update type &4/splegg update");
			} else if (Splegg.getSplegg().getConfig().getBoolean("auto-update")) {
				Splegg.getSplegg().chat.sendMessage(player, "&bNo update found.");
			}
		}
		
	}

}
