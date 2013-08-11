package com.raptorhosting.splegg.events;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.BlockIterator;

import com.raptorhosting.splegg.Splegg;
import com.raptorhosting.splegg.games.Game;
import com.raptorhosting.splegg.games.Status;
import com.raptorhosting.splegg.players.SpleggPlayer;
import com.raptorhosting.splegg.players.UtilPlayer;

public class SpleggEvents implements Listener {
	
	@EventHandler
	public void onShoot(PlayerInteractEvent e) {
		
		Player player = e.getPlayer();
		
		if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			if (player.getItemInHand().getType() == Material.IRON_SPADE) {
				
				UtilPlayer u = Splegg.getSplegg().pm.getPlayer(player);
				
				if (u.getGame() != null && u.isAlive()) {
					
					player.launchProjectile(Egg.class);
					player.playSound(player.getLocation(), Sound.WITHER_SHOOT, 0.1F, 1F);
					
				}
				
			}
			
		}
		
	}
	
	@EventHandler
	public void eggLand(ProjectileHitEvent e) {
		
		if (e.getEntity().getShooter() instanceof Player) {
			if (e.getEntity() instanceof Egg) {
				
				Player player = (Player) e.getEntity().getShooter();
				UtilPlayer u = Splegg.getSplegg().pm.getPlayer(player);
				
				if (u.getGame() != null && u.isAlive()) {
					
					BlockIterator bi = new BlockIterator(e.getEntity().getWorld(), e.getEntity().getLocation().toVector(), e.getEntity().getVelocity().normalize(), 0.0D, 4);
					Block hit = null;
					while (bi.hasNext()) {
						hit = bi.next();
						if (hit.getTypeId() != 0) {
							break;
						}
					}
					
					if (u.getGame().getFloor().contains(hit.getLocation())) {
						
						Game game = u.getGame();
						
						if (game.getStatus() == Status.INGAME) {
							
							game.getPlayer(player).setBroken(game.getPlayer(player).getBroken() + 1);
							
							hit.setTypeId(0);
							
							player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1F, 1F);
							for (SpleggPlayer sp : u.getGame().getPlayers().values()) {
								sp.getPlayer().playEffect(new Location(hit.getWorld(), hit.getLocation().getBlockX(), hit.getLocation().getBlockY() + 1.0D, hit.getLocation().getBlockZ()), Effect.MOBSPAWNER_FLAMES, 25);
							}
						}
							
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onKnockout(PlayerMoveEvent e) {
		
		Player player = e.getPlayer();
		UtilPlayer u = Splegg.getSplegg().pm.getPlayer(player);
		
		if (u.getGame() != null && u.isAlive()) {
			
			if (player.getLocation().getBlockY() < -5.0D || player.getLocation().getBlockY() < u.getGame().getLowestPossible()) {
				
				if (u.getGame().getStatus() == Status.INGAME) {
					
					Splegg.getSplegg().chat.bc("Player &e" + (Splegg.getSplegg().special.contains(player.getName()) ? "§4" : "§e") + player.getName() + " &6has been knocked out!", u.getGame());
					Splegg.getSplegg().chat.bc("&b&l" + (u.getGame().getPlayers().size() - 1) + " PLAYERS REMAIN", u.getGame());
					player.setFallDistance(0.0F);
					u.getGame().leaveGame(u);
					player.setFallDistance(0.0F);
					Splegg.getSplegg().chat.sendMessage(player, "You have been knocked out.");
					
				}
				
			}
			
		}
		
	}
	
	@EventHandler
	public void eggHatch(PlayerEggThrowEvent e) {
		Player player = e.getPlayer();
		UtilPlayer u = Splegg.getSplegg().pm.getPlayer(player);
		if (u.getGame() != null && u.isAlive()) {
			e.setHatching(false);
		}
	}
	
}
