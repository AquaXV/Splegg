package com.raptorhosting.splegg.games;

public class Rollback {
	
	private String				world;
	private int					previd;
	private int					newid;
	private byte				prevdata;
	private byte				newdata;
	private int					x;
	private int					y;
	private int					z;
	
	public Rollback(String world, int previd, byte prevdata, int x, int y, int z) {
		this.world = world;
		this.previd = previd;
		this.prevdata = prevdata;
		this.newid = 0;
		this.newdata = 0;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public String getWorld() {
		return this.world;
	}
	
	public byte getPrevdata() {
		return this.prevdata;
	}
	
	public byte getNewdata() {
		return this.newdata;
	}
	
	public int getPrevid() {
		return this.previd;
	}
	
	public int getNewid() {
		return this.newid;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getZ() {
		return this.z;
	}
}
