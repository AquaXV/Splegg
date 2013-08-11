package com.raptorhosting.splegg.events;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.raptorhosting.splegg.Splegg;
import com.raptorhosting.splegg.lobby.LobbySign;
import com.raptorhosting.splegg.lobby.LobbySignUtils;
import com.raptorhosting.splegg.misc.Permissions;

public class SignListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void signPlace(SignChangeEvent e) {
	
		Player player = e.getPlayer();
		Permissions perms = new Permissions(player);
		
		if (e.getLine(0).equalsIgnoreCase("[sp]") && perms.canModifyMaps()) {
			String map = e.getLine(1);
			if (Splegg.getSplegg().maps.mapExists(map)) {
				LobbySign ls = new LobbySign(Splegg.getSplegg().maps.getMap(map), Splegg.getSplegg());
				ls.create(e.getBlock().getLocation(), Splegg.getSplegg().maps.getMap(map));
				Splegg.getSplegg().chat.sendMessage(player, "You have created a lobby sign for §b" + map + "&6.");
			}
		}
		
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
	
		if ((e.hasBlock()) && ((e.getClickedBlock().getType() == Material.WALL_SIGN) || (e.getClickedBlock().getType() == Material.SIGN) || (e.getClickedBlock().getType() == Material.SIGN_POST)) && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			Sign s = (Sign) e.getClickedBlock().getState();
			Player player = e.getPlayer();
			if (s.getLine(0).equalsIgnoreCase(ChatColor.DARK_AQUA + "[Splegg]")) {
				String map = ChatColor.stripColor(s.getLine(1));
				if (Splegg.getSplegg().maps.mapExists(map)) {
					player.chat("/splegg join " + map);
					e.setCancelled(true);
				} else {
					Splegg.getSplegg().chat.sendMessage(player, "&cMap does not exist!");
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void signBreak(BlockBreakEvent e) {
		
		Player player = e.getPlayer();
		
		if (e.getBlock().getType() == Material.WALL_SIGN || e.getBlock().getType() == Material.SIGN_POST) {
			
			Sign s = (Sign)e.getBlock().getState();
			String[] lines = s.getLines();
			
			String map = ChatColor.stripColor(lines[1]);
			
			if (LobbySignUtils.get().isLobbySign(e.getBlock().getLocation(), map)) {
				
				Permissions perms = new Permissions(player);
				if (perms.canModifyMaps()) {
				
				LobbySign sign = new LobbySign(Splegg.getSplegg().maps.getMap(map), Splegg.getSplegg());
				sign.delete(e.getBlock().getLocation());
				
				Splegg.getSplegg().chat.sendMessage(player, "Removed a lobby sign for map &c" + map + "&6.");
				
				} else {
					
					e.setCancelled(true);
					Splegg.getSplegg().chat.sendMessage(player, "You cannot break a lobby sign!");
					
				}
			}
		}
		
	}
}
