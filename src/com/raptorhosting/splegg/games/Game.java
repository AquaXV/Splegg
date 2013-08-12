package com.raptorhosting.splegg.games;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.raptorhosting.splegg.Splegg;
import com.raptorhosting.splegg.lobby.LobbySign;
import com.raptorhosting.splegg.maps.Map;
import com.raptorhosting.splegg.players.InvStore;
import com.raptorhosting.splegg.players.SpleggPlayer;
import com.raptorhosting.splegg.players.UtilPlayer;
import com.raptorhosting.splegg.runnables.GameTime;
import com.raptorhosting.splegg.runnables.LobbyCountdown;
import com.raptorhosting.splegg.scoreboards.ScoreboardUtils;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;

public class Game {
	
	Splegg splegg;
	String name;
	Map map;
	Status status;
	HashMap<String,SpleggPlayer> players;
	HashSet<Location> floor;
	ArrayList<Rollback> data;
	private int lobbycount;
	int time;
	int y1;
	int y2;
	int small;
	int counter = 0;
	int timer = 0;
	private boolean	starting;
	private LobbySign sign;
	
	public Game(Splegg splegg, Map map) {
		this.splegg = splegg;
		this.map = map;
		this.name = map.getName();
		this.status = Status.LOBBY;
		this.players = new HashMap<String, SpleggPlayer>();
		this.floor = new HashSet<Location>();
		this.data = new ArrayList<Rollback>();
		time = 601;
		setLobbyCount(31);
		y1 = 0;
		y2 = 0;
		small = -1;
		this.setSign(new LobbySign(map, splegg));
		final Map m = map;
		new BukkitRunnable() {
			
			public void run() { getSign().update(m, true); }
			
		}.runTaskLater(splegg, 10L);
		this.setStarting(false);
	}
	
	public void startGameTimer() {
		this.timer = Bukkit.getScheduler().scheduleSyncRepeatingTask(splegg, new GameTime(splegg, this), 0L, 20L);
	}
	
	public void stopGameTimer() {
		Bukkit.getScheduler().cancelTask(timer);
	}
	
	public int getCounterID() {
		return this.counter;
	}
	
	public HashSet<Location> getFloor() {
		return this.floor;
	}
	
	public ArrayList<Rollback> getDatas() {
		return this.data;
	}
	
	public HashMap<String, SpleggPlayer> getPlayers() {
		return this.players;
	}
	
	public ArrayList<SpleggPlayer> getSp() {
		ArrayList<SpleggPlayer> sp = new ArrayList<SpleggPlayer>();
		for (SpleggPlayer sps : players.values()) {
			sp.add(sps);
		}
		return sp;
	}
	
	public SpleggPlayer getPlayer(Player player) {
		return (SpleggPlayer)players.get(player.getName());
	}
	
	public Status getStatus() {
		return this.status;
	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public void setMap(Map map) {
		this.map = map;
	}
	
	public Map getMap() {
		return this.map;
	}
	
	public void joinGame(UtilPlayer u) {
		Player player = u.getPlayer();
		if (u.getGame() != null) {
			splegg.chat.sendMessage(player, "You are already in a game.");
		} else {
			if (players.containsKey(player.getName())) {
				splegg.chat.sendMessage(player, "You are already in this lobby.");
			} else {
				if (status == Status.LOBBY) {
					int size = this.players.size();
					int max = this.map.getSpawnCount();
					if (max == 1) {
						SpleggPlayer sp = new SpleggPlayer(player, u);
						
						u.setAlive(true);
						u.getStore().save();
						splegg.utils.clearInventory(player);
						
						player.setHealth(20);
						player.setFoodLevel(20);
						player.setLevel(0);
						player.setExp(0.0F);
						player.setGameMode(GameMode.ADVENTURE);
						
						players.put(player.getName(), sp);
						u.setGame(splegg.games.getGame(name));
						
						player.teleport(splegg.config.getLobby());
						
						splegg.chat.sendMessage(player, "You have been teleported to the splegg lobby. You will be teleported to the map on the game start.");
						
						splegg.chat.sendMessage(player, "Players in your game: " + getPlayersIn());
						
						splegg.chat.sendMessage(player, "You have joined the lobby for map §c" + map.getName() + "§6.");

						ScoreboardUtils.get().setScoreAll(this, "Queue", players.size());
						
						if (this.players.size() >= splegg.getConfig().getInt("auto-start.players") && (!this.isStarting())) {
							startCountdown();
							this.setStarting(true);
						}
					} else {
						if (size >= max && !(player.hasPermission("splegg.joinfull"))) {
							splegg.chat.sendMessage(player, "§cSorry, this game is §efull§c. Max players: " + max + ".");
						} else {
							if (size >= max) {
								splegg.chat.sendMessage(player, "§6You have joined a full game!");
							}
							SpleggPlayer sp = new SpleggPlayer(player, u);
							
							u.setAlive(true);
							u.getStore().save();
							splegg.utils.clearInventory(player);
							
							player.setHealth(20);
							player.setFoodLevel(20);
							player.setLevel(0);
							player.setExp(0.0F);
							player.setGameMode(GameMode.ADVENTURE);
							
							players.put(player.getName(), sp);
							u.setGame(splegg.games.getGame(name));
							
							player.teleport(splegg.config.getLobby());
							
							splegg.chat.sendMessage(player, "You have been teleported to the splegg lobby. You will be teleported to the map on the game start.");
							
							splegg.chat.sendMessage(player, "Players in your game: " + getPlayersIn());
							
							ScoreboardUtils.get().setScoreAll(this, "Queue", players.size());
							
							splegg.chat.sendMessage(player, "You have joined the lobby for map §c" + map.getName() + "§6.");
							splegg.chat.bcNotForPlayer(player, (splegg.special.contains(player.getName()) ? "§4" : "§a") + player.getName() + "&6 has joined the game. &e" + players.size() + "/" + max, this);
							
							if (this.players.size() >= splegg.getConfig().getInt("auto-start.players") && (!this.isStarting())) {
								startCountdown();
								this.setStarting(true);
							}
							
						}
					}
					getSign().update(map, false);
				} else if (status == Status.DISABLED) {
					splegg.chat.sendMessage(player, "§4Map is disabled.");
				} else {
					splegg.chat.sendMessage(player, "§5Game in progress.");
				}
			}
		}
	}
	
	private String getPlayersIn() {
		String p = "";
		
		for (SpleggPlayer sp : players.values()) {
			p = (splegg.special.contains(sp.getPlayer().getName()) ? "§4" : "§a") + sp.getPlayer().getName() + "§6, " + p;
		}
		return p;
	}

	private void startCountdown() {
		Bukkit.getScheduler().cancelTask(counter);
		
		if (this.status == Status.LOBBY) {
			this.setLobbyCount(31);
			for (SpleggPlayer sp : players.values()) {
				sp.getPlayer().setLevel(getLobbyCount());
				sp.getScoreboard().setScore("Starting in", getLobbyCount());
			}
			this.counter = Bukkit.getScheduler().scheduleSyncRepeatingTask(splegg, new LobbyCountdown(this, getLobbyCount()), 0L, 20L);
		}
		
	}
	
	public void leaveGame(UtilPlayer u) {
		SpleggPlayer sp = getPlayer(u.getPlayer());
		if (status == Status.ENDING || status == Status.INGAME) {
			splegg.chat.sendMessage(u.getPlayer(), "You broke " + sp.getBroken() + " blocks.");
			ScoreboardUtils.get().setScoreAll(this, "Players Left", players.size());
		}
		players.remove(u.getName());
		u.getPlayer().teleport(splegg.config.getLobby());
		u.setGame(null);
		u.setAlive(false);
		u.getPlayer().setHealth(20);
		InvStore store = u.getStore();
		store.load();
		store.reset();
		u.getPlayer().setFallDistance(0.0F);
		if (!splegg.disabling) {
			getSign().update(map, false);
		}
	}
	
	public int getLobbyCount() {
		return lobbycount;
	}

	public int getCount() {
		return time;
	}
	
	public int getLowestPossible() {
		return this.small;
	}
	
	public boolean loadFloors() {
		
		this.floor.clear();
		
		if (map.getFloors() > 0) {
			for (int i = 1; i <= map.getFloors(); i++) {
				Selection sel = new CuboidSelection(map.getSpawn(1).getWorld(), map.getFloor(i, "1"), map.getFloor(i, "2"));
				Location min = sel.getMinimumPoint();
				Location max = sel.getMaximumPoint();
				
				int minX = min.getBlockX();
				int minY = min.getBlockY();
				this.y1 = minY;
				int minZ = min.getBlockZ();
				int maxX = max.getBlockX();
				int maxY = max.getBlockY();
				this.y2 = maxY;
				int maxZ = max.getBlockZ();
				
				this.small = Math.min(y1, y2);
				
				for (int x = minX; x <= maxX; x++) {
					for (int y = minY; y <= maxY; y++) {
						for (int z = minZ; z <= maxZ; z++) {
							Location l = new Location(min.getWorld(), x, y, z);
							floor.add(l);
							Block block = l.getWorld().getBlockAt(x, y, z);
							data.add(new Rollback(l.getWorld().getName(), block.getTypeId(), block.getData(), x, y, z));
						}
					}
				}
			}
		} else {
			return false;
		}
		return true;
	}
	
	public void resetArena() {
		for (Rollback d : data) {
			Location l = new Location(Bukkit.getWorld(d.getWorld()), d.getX(), d.getY(), d.getZ());
			l.getBlock().setTypeId(d.getPrevid());
			l.getBlock().setData(d.getPrevdata());
			l.getBlock().getState().update();
		}
	}
	
	public boolean isStarting() {
		return starting;
	}

	public void setStarting(boolean starting) {
		this.starting = starting;
	}

	public LobbySign getSign() {
		return sign;
	}

	public void setSign(LobbySign sign) {
		this.sign = sign;
	}

	public void setLobbyCount(int lobbycount) {
		this.lobbycount = lobbycount;
	}
	
}