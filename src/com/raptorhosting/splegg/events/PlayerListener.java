package com.raptorhosting.splegg.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.raptorhosting.splegg.Splegg;
import com.raptorhosting.splegg.players.UtilPlayer;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onFood(FoodLevelChangeEvent e) {
		if (e.getEntity() instanceof Player) {
			Player player = (Player) e.getEntity();
			UtilPlayer u = Splegg.getSplegg().pm.getPlayer(player);
			if (u.getGame() != null && u.isAlive()) {
				e.setCancelled(true);
				e.setFoodLevel(20);
			}
		}
	}
	
	@EventHandler
	public void entityDamage(EntityDamageEvent e) {
		Entity ent = e.getEntity();
		
		if (ent instanceof Player) {
			
			Player player = (Player)ent;
			UtilPlayer u = Splegg.getSplegg().pm.getPlayer(player);
			
			if (u.getGame() != null && u.isAlive()) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		if (e.getWhoClicked() != null && e.getWhoClicked() instanceof Player) {
			Player player = (Player)e.getWhoClicked();
			UtilPlayer u = Splegg.getSplegg().pm.getPlayer(player);
			if (u.getGame() != null && u.isAlive()) {
				e.setCancelled(true);
				Splegg.getSplegg().chat.sendMessage(player, "&eYou cannot edit your inventory whilst ingame.");
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		
		UtilPlayer u = new UtilPlayer(player);
		
		Splegg.getSplegg().pm.PLAYERS.put(player.getName(), u);
		
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		UtilPlayer u = Splegg.getSplegg().pm.getPlayer(player);
		
		if (u.getGame() != null && u.isAlive()) {
			u.getGame().leaveGame(u);
		}
		
		Splegg.getSplegg().pm.PLAYERS.remove(player.getName());
		
	}
	
	@EventHandler
	public void dropItem(PlayerDropItemEvent e) {
		Player player = e.getPlayer();
		UtilPlayer u = Splegg.getSplegg().pm.getPlayer(player);
		
		if (u.getGame() != null && u.isAlive()) {
			e.setCancelled(true);
		}
	}
	
	String[] cmds = { "" };
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e) {
		Player player = e.getPlayer();
		UtilPlayer u = Splegg.getSplegg().pm.getPlayer(player);
		
		if (u.getGame() != null && u.isAlive()) {
			if (!e.getMessage().startsWith("/splegg") && !(player.isOp() || player.hasPermission("splegg.admin"))) {
				e.setCancelled(true);
				Splegg.getSplegg().chat.sendMessage(player, "&eYou cannot use commands in &bSplegg");
			}
		}
	}
	
}
