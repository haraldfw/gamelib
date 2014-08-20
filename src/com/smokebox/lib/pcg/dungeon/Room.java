package com.smokebox.lib.pcg.dungeon;

import com.smokebox.lib.utils.IntVector2;

public class Room {
	
	public int x;
	public int y;
	public int w;
	public int h;

	public Room(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public Room(IntVector2 pos, int w, int h) {
		this.x = pos.x;
		this.y = pos.y;
		this.w = w;
		this.h = h;
	}
	
	public Room() {
		this.x = 0;
		this.y = 0;
		this.w = 0;
		this.h = 0;
	}
}
