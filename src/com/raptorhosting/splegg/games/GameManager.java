package com.raptorhosting.splegg.games;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.raptorhosting.splegg.Splegg;
import com.raptorhosting.splegg.maps.Map;
import com.raptorhosting.splegg.players.SpleggPlayer;
import com.raptorhosting.splegg.scoreboards.ScoreboardUtils;

public class GameManager {
	
	Splegg splegg;
	
	public GameManager(Splegg splegg) {
		this.splegg = splegg;
	}
	
	public void startGame(Game game) {
		
		splegg.chat.log("New game commencing..");
		
		game.startGameTimer();
		
		Bukkit.getScheduler().cancelTask(game.counter);
		
		game.status = Status.INGAME;
		game.time = 901;
		game.setLobbyCount(31);
		
		int c = 1;
		
		game.loadFloors();
		
		splegg.chat.bc("You are playing on &2" + game.getMap().getName() + "&6.", game);
		
		Map map = game.getMap();
		
		ScoreboardUtils.get().hideScoreAll(game, "Starting in");
		
		for (SpleggPlayer sp : game.players.values()) {
			
			sp.getPlayer().setLevel(0);
			
			sp.getScoreboard().hideScore("Queue");
			
			sp.getUtilPlayer().setAlive(true);
			
			if (c > map.getSpawnCount()) {
				c = 1;
			}
			
			sp.getPlayer().teleport(map.getSpawn(c));
			c++;
			
			sp.getPlayer().setLevel(0);
			sp.getPlayer().setGameMode(GameMode.ADVENTURE);
			
			// give items
			for (int i = 0; i < 9; i++) {
				sp.getPlayer().getInventory().setItem(i, getGun());
			}
			
		}
		
		game.getSign().update(map,false);
		
		splegg.chat.bc("Use your Splegg-Gun to knock other players out.", game);
		
	}
	
	public void stopGame(Game game, int r) {
		
		splegg.chat.log("Commencing shutdown of " + game.getMap().getName() + ".");
		
		game.status = Status.ENDING;
		
		game.stopGameTimer();
		
		game.setLobbyCount(31);
		game.time = 601;
		game.setStatus(Status.LOBBY);
		
		game.resetArena();
		game.data.clear();
		game.floor.clear();
		
		game.setStarting(false);
		
		HashMap<String, SpleggPlayer> h = new HashMap<String, SpleggPlayer>(game.players);
		game.players.clear();
		
		for (SpleggPlayer sp : h.values()) {
			
			game.leaveGame(sp.getUtilPlayer());
			
		}
		
		
		if (r != 5) {
			splegg.chat.bc("Splegg has ended on the map " + game.getMap().getName() + ".");
		}
		
		if (!splegg.disabling) {
			game.getSign().update(game.map,true);
		}
		
		splegg.chat.log("Game has reset.");
		
	}
	
	public String getDigitTime(int count) {
		int minutes = count / 60;
		int seconds =count % 60; 
		String disMinu = (minutes < 10 ? "0" : "") + minutes;
		String disSec = (seconds < 10 ? "0" : "") + seconds;
		String formattedTime = disMinu + ":" + disSec;
		return formattedTime;
	}
	
	public void ingameTimer(int count, HashMap<String, SpleggPlayer> players) {
		for (SpleggPlayer sp : players.values()) {
			splegg.chat.sendMessage(sp.getPlayer(), "Splegg is ending in §5§l" + splegg.game.getDigitTime(count));
		}
	}
	
	public ItemStack getGun() {
		ItemStack is = new ItemStack(Material.IRON_SPADE);
		ItemMeta m = is.getItemMeta();
		m.setDisplayName("§eSplegg-Gun");
		m.setLore(Arrays.asList(new String[] { "§3Right-click with this to shoot a splegg." }));
		is.setItemMeta(m);
		return is;
	}
	
}
