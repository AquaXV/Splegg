package com.raptorhosting.splegg.runnables;

import com.raptorhosting.splegg.Splegg;
import com.raptorhosting.splegg.games.Game;
import com.raptorhosting.splegg.scoreboards.ScoreboardUtils;

public class GameTime implements Runnable {
	
	Splegg splegg;
	Game game;
	
	public GameTime(Splegg splegg, Game game) {
		this.splegg = splegg;
		this.game = game;
	}

	@Override
	public void run() {
		
		if (game.getCount() > 0) {
			
			splegg.games.checkWinner(game);
			ScoreboardUtils.get().setDisplayAll(game, "Splegg | " + splegg.game.getDigitTime(game.getLobbyCount()));
			
			if (game.getCount() % 300 == 0) {
				splegg.game.ingameTimer(game.getCount(), game.getPlayers());
			}
			
			if (game.getCount() % 30 == 0 && game.getCount() < 60) {
				splegg.game.ingameTimer(game.getCount(), game.getPlayers());
			}
			
			if (game.getCount() <= 5 && game.getCount() >= 1) {
				splegg.chat.bc("Splegg is ending in &b&l" + game.getCount() + "&6.", game);
			}
			
		} else {
			
			game.stopGameTimer();
			splegg.chat.bc("&dTime limit reached.", game);
			splegg.game.stopGame(game, 1);
			
		}
		
	}
	
}
