package com.raptorhosting.splegg.misc;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.raptorhosting.splegg.Splegg;

public class Config {
	
	Splegg splegg;
	
	public Config(Splegg s) {
		this.splegg = s;
	}
	
	public FileConfiguration	spawns;
	private File				f;
	
	public void setup() {
	
		f = new File(splegg.getDataFolder(), "spawns.yml");
		
		try {
			if (!f.exists()) {
				f.createNewFile();
			}
		} catch (Exception ex) {
		}
		
		reloadSpawns();
		saveSpawns();
		reloadSpawns();
		
	}
	
	private void reloadSpawns() {
	
		spawns = YamlConfiguration.loadConfiguration(f);
		
	}
	
	private void saveSpawns() {
	
		try {
			
			spawns.save(f);
			
		} catch (IOException ex) {
		}
		
	}
	
	public void setLobby(Location l) {
		
		int x = l.getBlockX();
		int y = l.getBlockY();
		int z = l.getBlockZ();
		
		float yaw = l.getYaw();
		float pitch = l.getPitch();
		
		String worldName = l.getWorld().getName();
		
		spawns.set("Spawns.lobby.world", worldName);
		spawns.set("Spawns.lobby.x", x);
		spawns.set("Spawns.lobby.y", y);
		spawns.set("Spawns.lobby.z", z);
		spawns.set("Spawns.lobby.pitch", pitch);
		spawns.set("Spawns.lobby.yaw", yaw);
		
		saveSpawns();
		
	}
	
	public Location getLobby() {
	
		int x, y, z;
		float yaw, pitch;
		World worldName;
		
		x = spawns.getInt("Spawns.lobby.x");
		y = spawns.getInt("Spawns.lobby.y");
		z = spawns.getInt("Spawns.lobby.z");
		
		yaw = spawns.getInt("Spawns.lobby.yaw");
		pitch = spawns.getInt("Spawns.lobby.pitch");
		
		worldName = Bukkit.getWorld(spawns.getString("Spawns.lobby.world"));
		
		return new Location(worldName, x + 0.5D, y + 0.5D, z + 0.5D, yaw, pitch);
		
	}
	
}
