package com.smokebox.lib.pcg.roomBased;

import com.smokebox.lib.utils.IntVector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Harald Wilhelmsen on 11/14/2014.
 */
public final class RoomDef {

	private HashMap<String, DoorDef> doorDefs;

	public final int x;
	public final int y;
	public final int width;
	public final int height;
	private int maxConnections = 2;

	public ArrayList<DoorDef> unclaimedDoors;

	public RoomDef(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		// place doors
		int doorX;
		int doorY;

		for(int dx = 0; dx < width; dx++) {
			// calculate x-coordinate
			doorX = x + dx;

			// top wall
			doorY = y + height;
			putDoor(doorX, doorY);

			// bottom wall
			doorY = y - 1;
			putDoor(doorX, doorY);
		}
		for(int dy = 0; dy < height; dy++) {
			// calculate y-coordinate
			doorY = y + dy;

			// left wall
			doorX = x - 1;
			putDoor(doorX, doorY);

			// right wall
			doorX = x + width;
			putDoor(doorX, doorY);
		}

		// add all doors to unclaimedDoors list
		unclaimedDoors.addAll(doorDefs.values());
	}

	/**
	 * Finalizes the room by removing all doors that do not lead to anything. This method
	 * will re-create the Doors-array into an array with only the doors that lead to something.
	 */
	public void finalizeDoor() {
		// List of all doors that will be final
		ArrayList<DoorDef> doorDefList = new ArrayList<DoorDef>();

		// loop through all doors. If a door does not have an end, remove it
		for(Map.Entry<String, DoorDef> entry : doorDefs.entrySet()) {
			if(entry.getValue().end == null)
				doorDefs.remove(entry.getKey());
		}

		// after finalization, ensures algorithm will fail if unclaimedDoors is accessed
		unclaimedDoors = null;
	}

	public void connectDoors(DoorDef door, DoorDef toConnect) {
		door.end = toConnect;
		unclaimedDoors.remove(door);
	}

	public boolean increaseMaxConnections(int amount) {
		maxConnections += amount;
		// if room cannot physically have more connections, all walls are taken return false
		// else return true
		int w = width*2 + height*2;
		if(maxConnections > w) {
			maxConnections = w;
			return false;
		}
		return true;
	}

	public boolean canHaveMoreConnections() {
		return claimedDoors() < maxConnections;
	}

	private int claimedDoors() {
		return width*2 + height*2 - unclaimedDoors.size();
	}

	public int openWalls() {
		return unclaimedDoors.size();
	}

	public boolean hasOpenWalls() {
		return !unclaimedDoors.isEmpty();
	}

	public DoorDef getRandomUnclaimedDoor(Random rng) {
		return unclaimedDoors.get(rng.nextInt(unclaimedDoors.size()));
	}

	public DoorDef getDoorTo(int x, int y) {
		return doorDefs.get(x + "," + y);
	}

	private void putDoor(int x, int y) {
		doorDefs.put(x + "," + x, new DoorDef(this, x, x));
	}
}