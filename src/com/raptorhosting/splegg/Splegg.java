package com.raptorhosting.splegg;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import net.h31ix.updater.Updater;
import net.h31ix.updater.Updater.UpdateResult;
import net.h31ix.updater.Updater.UpdateType;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.raptorhosting.splegg.commands.SpleggCommand;
import com.raptorhosting.splegg.events.MapListener;
import com.raptorhosting.splegg.events.PlayerListener;
import com.raptorhosting.splegg.events.SignListener;
import com.raptorhosting.splegg.events.SpleggEvents;
import com.raptorhosting.splegg.games.Game;
import com.raptorhosting.splegg.games.GameManager;
import com.raptorhosting.splegg.games.GameUtilities;
import com.raptorhosting.splegg.games.Status;
import com.raptorhosting.splegg.maps.MapUtilities;
import com.raptorhosting.splegg.misc.Chat;
import com.raptorhosting.splegg.misc.Config;
import com.raptorhosting.splegg.misc.UpdateUtils;
import com.raptorhosting.splegg.misc.Utilities;
import com.raptorhosting.splegg.players.PlayerManager;
import com.raptorhosting.splegg.players.UtilPlayer;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class Splegg extends JavaPlugin {
	
	public static Splegg getSplegg() {
		return (Splegg)Bukkit.getPluginManager().getPlugin("Splegg");
	}
	
	public Chat chat;
	public MapUtilities maps;
	public GameUtilities games;
	public GameManager game;
	public PlayerManager pm;
	public Utilities utils;
	public Config config;
	
	public Updater u;
	public boolean updateOut = false;
	public String newVer = "";
	public File updateFile = this.getFile();
	
	public List<String> special = Arrays.asList(new String[] { "njb_said", "That1Guy2", "Shabuwa", "ChippNDipp", "TheSenorChang", "Dwizofoz" , "irRedemption"});
	public boolean eco = false;
	public boolean disabling = false;
	
	public void onEnable() {
		
		this.chat = new Chat();
		if (getServer().getPluginManager().getPlugin("WorldEdit") == null) {
			chat.log("WorldEdit not found! Please download it from http://dev.bukkit.org/bukkit-plugins/worldedit");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		this.maps = new MapUtilities();
		this.games = new GameUtilities();
		this.game = new GameManager(Splegg.getSplegg());
		this.pm = new PlayerManager();
		this.utils = new Utilities();
		this.config = new Config(this);
		
		maps.c.setup();
		config.setup();
		
		getConfig().options().copyDefaults(true);
		saveConfig();
		
		if (getConfig().getBoolean("auto-update")) {
			u = new Updater(this, "splegg-game", getFile(), UpdateType.NO_DOWNLOAD, false);
			updateOut = u.getResult() == UpdateResult.UPDATE_AVAILABLE;
			if (updateOut) {
				newVer = u.getLatestVersionString();
			}
			getServer().getPluginManager().registerEvents(new UpdateUtils(), this);
		}
		
//		economy.init(this);
		
		getServer().getPluginManager().registerEvents(new MapListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new SpleggEvents(), this);
		getServer().getPluginManager().registerEvents(new SignListener(), this);
		
		getCommand("splegg").setExecutor(new SpleggCommand());
		
		for (Player players : Bukkit.getOnlinePlayers()) {
			UtilPlayer u = new UtilPlayer(players);
			pm.PLAYERS.put(players.getName(), u);
		}
		
		super.onEnable();
	}
	
	public void onDisable() {
		disabling = true;
		for (Game game : this.games.GAMES.values()) {
			if (game.getStatus() == Status.INGAME) {
				this.game.stopGame(game, 1);
			}
		}
		super.onDisable();
	}
	
	public WorldEditPlugin getWorldEdit() {
		Plugin worldEdit = getServer().getPluginManager().getPlugin("WorldEdit");
		if ((worldEdit instanceof WorldEditPlugin)) {
			return (WorldEditPlugin) worldEdit;
		}
		return null;
	}
	
}
