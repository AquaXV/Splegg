package com.raptorhosting.splegg.lobby;

import org.bukkit.block.Sign;

public class SignDelay implements Runnable {
	
	String[] data;
	Sign sign;
	
	public SignDelay(String[] data, Sign sign) {
		this.data = data;
		this.sign = sign;
	}
	
	public void run() {
		
		for (int i = 0; i < data.length; i++) {
			sign.setLine(i, data[i]);
		}
		sign.update();
		
	}
	
}
