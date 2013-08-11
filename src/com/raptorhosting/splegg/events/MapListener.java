package com.raptorhosting.splegg.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;

import com.raptorhosting.splegg.Splegg;
import com.raptorhosting.splegg.players.UtilPlayer;

public class MapListener implements Listener {
	
	public Splegg splegg;
	
	public MapListener(Splegg s) {
		this.splegg = s;
	}
	
	@EventHandler
	public void blockBreak(BlockBreakEvent e) {
		
		Player player = e.getPlayer();
		UtilPlayer u = splegg.pm.getPlayer(player);
		
		if (u.getGame() != null) {
			if (u.isAlive()) {
				e.setCancelled(true);
			}
		}
		
	}
	
	@EventHandler
	public void blockPlace(BlockPlaceEvent e) {
		
		Player player = e.getPlayer();
		UtilPlayer u = splegg.pm.getPlayer(player);
		
		if (u.getGame() != null) {
			if (u.isAlive()) {
				e.setCancelled(true);
			}
		}
		
	}
	
	@EventHandler
	public void hangingEntityBreak(HangingBreakByEntityEvent e) {
		Player player = (Player)e.getRemover();
		UtilPlayer u = splegg.pm.getPlayer(player);
		
		if (u.getGame() != null) {
			if (u.isAlive()) {
				e.setCancelled(true);
			}
		}
		
	}
	
}
