package com.raptorhosting.splegg.games;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

import com.raptorhosting.splegg.players.SpleggPlayer;

public class GameUtilities {
	
	public HashMap<String, Game> GAMES = new HashMap<String, Game>();
	
	public Game getGame(String map) {
		return (Game)GAMES.get(map);
	}
	
	public void addGame(String map, Game game) {
		GAMES.put(map, game);
	}
	
	public SpleggPlayer getPlayer(Player player) {
		
		if (player == null) {
			return null;
		}
		
		SpleggPlayer sp = null;
		for (Game games : this.GAMES.values()) {
			for (SpleggPlayer sps : games.players.values()) {
				if (sps.getPlayer().getName().equalsIgnoreCase(player.getName())) {
					sp = sps;
				}
			}
		}
		return sp;
	}
	
	public Game getMatchedGame(Player player) {
		Game game = null;
		for (Game g : GAMES.values()) {
			if (g.players.containsKey(player.getName())) {
				game = g;
			}
		}
		return game;
	}
	
	public int howManyOpenGames() {
	    ArrayList<Game> all = new ArrayList<Game>();
	    for (Game games : this.GAMES.values()) {
	      if (games.getStatus() == Status.LOBBY) {
	        all.add(games);
	      }
	    }

	    return all.size();
	  }
	
	public void checkWinner(Game game) {
		if (game.players.size() <= 1) {
			if (game.players.size() == 0) {
				game.splegg.game.stopGame(game, 0);
			} else {
				String w = "";
				for (SpleggPlayer sp : game.players.values()) {
					w = sp.getPlayer().getName();
					game.leaveGame(sp.getUtilPlayer());
				}
				
				game.splegg.chat.bc("&b" + (game.splegg.special.contains(w) ? "§4" : "§a") + w + "&6 has won Splegg on &c" + game.map.getName() + "&6.");
				game.splegg.game.stopGame(game, 5);
			}
		}
	}
	
}
