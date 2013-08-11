package com.raptorhosting.splegg.players;

import org.bukkit.entity.Player;

import com.raptorhosting.splegg.scoreboards.SpleggScoreboard;

public class SpleggPlayer {
	
	Player player;
	UtilPlayer u;
	int kills;
	int broken;
	private SpleggScoreboard sb;
	
	public SpleggPlayer(Player player, UtilPlayer u) {
		this.player = player;
		this.u = u;
		this.kills = 0;
		this.broken = 0;
		this.setScoreboard(new SpleggScoreboard());
		player.setScoreboard(this.getScoreboard().getScoreboard());
	}
	
	public UtilPlayer getUtilPlayer() {
		return this.u;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public int getKills() {
		return this.kills;
	}
	
	public void setKills(int i) {
		this.kills = i;
	}
	
	public int getBroken() {
		return this.broken;
	}
	
	public void setBroken(int i) {
		this.broken = i;
		getScoreboard().setScore("Broken Blocks", i);
	}
	
	public SpleggScoreboard getScoreboard() {
		return sb;
	}
	
	public void setScoreboard(SpleggScoreboard sb) {
		this.sb = sb;
	}
	
}
