package com.raptorhosting.splegg.scoreboards;

import com.raptorhosting.splegg.games.Game;
import com.raptorhosting.splegg.players.SpleggPlayer;

public class ScoreboardUtils {
	
	private static ScoreboardUtils ins = new ScoreboardUtils();
	
	public static ScoreboardUtils get() {
		return ins;
	}
	
	public void setDisplayAll(Game game, String name) {
		for (SpleggPlayer sp : game.getPlayers().values()) {
			sp.getScoreboard().setDisplayName(name);
		}
	}
	
	public void setScoreAll(Game game, String iden, int score) {
		for (SpleggPlayer sp : game.getPlayers().values()) {
			sp.getScoreboard().setScore(iden, score);
		}
	}
	
	public void hideScoreAll(Game game, String iden) {
		for (SpleggPlayer sp : game.getPlayers().values()) {
			sp.getScoreboard().hideScore(iden);
		}
	}
	
}
