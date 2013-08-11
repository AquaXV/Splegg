package com.raptorhosting.splegg.misc;

import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;

public class Utilities {
	
	public void clearInventory(Player player) {
	
		PlayerInventory pInv = player.getInventory();
		
		pInv.setArmorContents(null);
		pInv.clear();
		
		player.setFireTicks(0);
		
		clearPotions(player);
		
	}
	
	public void clearPotions(Player player) {
	
		for (PotionEffect effect : player.getActivePotionEffects()) {
			
			player.removePotionEffect(effect.getType());
			
		}

	}
	
}
