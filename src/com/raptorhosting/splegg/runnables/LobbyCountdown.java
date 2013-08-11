package com.raptorhosting.splegg.runnables;

import org.bukkit.Bukkit;

import com.raptorhosting.splegg.Splegg;
import com.raptorhosting.splegg.games.Game;
import com.raptorhosting.splegg.players.SpleggPlayer;

public class LobbyCountdown implements Runnable {
	
	int lobbycount;
	Game game;
	
	public LobbyCountdown(Game game, int lobbycount) {
		this.game = game;
		this.lobbycount = lobbycount;
	}
	
	public void run() {
		if (lobbycount >= 1) {
			
			lobbycount--;
			game.setLobbyCount(lobbycount);
			
			for (SpleggPlayer sp : game.getPlayers().values()) {
				sp.getPlayer().setLevel(lobbycount);
			}
			
			game.getSign().update(game.getMap(), false);
			
			if (lobbycount % 10 == 0) {
				Splegg.getSplegg().chat.bc("Splegg starting in &b" + lobbycount + "&6.", game);
			}
			if ((lobbycount <= 5) && (lobbycount >= 1) && (lobbycount != 0)) {
				Splegg.getSplegg().chat.bc("Splegg starting in &b" + lobbycount + "&6.", game);
			}
		} else {
			if (game.getPlayers().size() > 1) {
				Bukkit.getScheduler().cancelTask(game.getCounterID());
				Splegg.getSplegg().game.startGame(game);
			} else {
				Splegg.getSplegg().chat.bc("&cNot enough players", game);
				Bukkit.getScheduler().cancelTask(game.getCounterID());
				game.setLobbyCount(31);
				game.setStarting(false);
				game.getSign().update(game.getMap(), false);
			}
		}
	}
	
}
