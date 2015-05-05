package com.smokebox.lib.pcg.roomBased;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Harald Wilhelmsen on 11/24/2014.
 */
public final class Isaac {

		/* GENERATE MAP [outline]
			1. add new room at 0,0 with size 1,1. This is the spawn-room
			2. for(int i = 1; i < amountOfRooms; i++)
				1.2 choose random room from the list
				1.3 if (room's connections < longestWall + 1 or (room is fork) and (room has at least 1 free side) continue, else jump back to step 1.2
						TODO find solution for looping map where all rooms have max connections
						Declare a random room as a fork, and then loop again(?)
				1.4 place a room randomly on one of the free sides of the room
			3. iterate over room[] and make an ArrayList with all end-rooms
			4. if endList's size is smaller than the amount of special-rooms, clear room[] and return to step 1
			5. Place special-rooms
				5.1 Place bossRoom on the endRoom with the longest path to the spawn-room and remove this room from the endList
				5.2 Place the shop and treasure-room and remove in similar style on random endList entries
				/place secret-room/
		*/

	/**
	 * Generates a 2D array of Rooms
	 * @param amountOfRooms	How many rooms to place, *not* including all special rooms.
	 * @param chanceForLargeRooms	Between 0 and 1. 0 will ensure all rooms stay 1by1. 1 will make all rooms bigger
	 * @param amountOfEnds	How many ends the map will have *Not yet implemented
	 * @param maxWidth	The max width of rooms
	 * @param maxHeight	The max height of rooms
	 * @param pathsAreSingular	If true, the loop will not place rooms that connect to more than one other at a time.
	 * @return	The generated map in a 2D Room-array.
	 */
	public static RoomDef[] generate(int amountOfRooms, float chanceForLargeRooms, int amountOfEnds,
										int maxWidth, int maxHeight, Random rng, boolean pathsAreSingular) {
		// list for storing the rooms, this list is returned as an array at end of algo
		ArrayList<RoomDef> rooms = new ArrayList<RoomDef>();

		// place spawnRoom
		rooms.add(new RoomDef(0, 0, 1, 1));

		HashMap<String, Boolean> occupiedCoordinates = new HashMap<>();

		while(rooms.size() <= amountOfRooms) {

			// get random room from list that has open and available connections
			// shuffle list of rooms
			Collections.shuffle(rooms, rng);

			// get first room in list which has open wall

			RoomDef toBuildFrom = null;
			for(RoomDef def : rooms) {
				// check if this def has an unclaimed door
				if(def.canHaveMoreConnections()) {
					toBuildFrom = def;
					break;
				}
			}

			// if no room has free connections
			if(toBuildFrom == null) {
				Collections.shuffle(rooms, rng);
				// find first room which has an open wall
				for(RoomDef def : rooms) {
					if(def.hasOpenWalls() && def.increaseMaxConnections(1)) {
						break;
					}
				}
			}

			// get random door from selected room
			DoorDef expanding = toBuildFrom.getRandomUnclaimedDoor(rng);

			int x = expanding.leadsToX;
			int y = expanding.leadsToY;
			int width = 1;
			int height = 1;

			// randomly increase width and height
			if(rng.nextFloat() < chanceForLargeRooms) {
				width += ((int)Math.round(rng.nextFloat()*(float)maxWidth));
				if(width > maxWidth) width = maxWidth;
				height += ((int)Math.round(rng.nextFloat()*(float)maxHeight));
				if(height > maxHeight) height = maxHeight;
			}

			// move room to cope with larger dimensions
			if(x < toBuildFrom.x) x -= width + 1;
			if(y < toBuildFrom.y) y -= height + 1;

			// place a room with found variables
			RoomDef placed = new RoomDef(x, y, width, height);

			// check if there actually can be a room at the given location
			if(pathsAreSingular) {
				// check leadsTo-coordinates for every door in placed room and cancel if any of them are occupied
				for(DoorDef door : placed.unclaimedDoors) {
					if(occupiedCoordinates.get(door.leadsToX + "," + door.leadsToY)) continue;
				}
			} else {
				for(int plx = placed.x; plx < placed.x + placed.width; plx++) {
					for(int ply = placed.y; ply < placed.y + placed.height; ply++) {
						// check if placed overlaps any other rooms
						if(occupiedCoordinates.get(plx + "," + ply)) continue;
					}
				}
			}

			// If iteration reaches this point, the room is valid
			// add placed room to rooms-list
			rooms.add(placed);

			// add the occupied coordinates to coordinates-map
			for(int plx = placed.x; plx < placed.x + placed.width; plx++) {
				for(int ply = placed.y; ply < placed.y + placed.height; ply++) {
					// check if placed overlaps any other rooms
					occupiedCoordinates.put(plx + "," + ply, true);
				}
			}

			// connect doors
			toBuildFrom.connectDoors(expanding, placed.getDoorTo(expanding.leadsToX, expanding.leadsToY));
		}



		return (RoomDef[]) rooms.toArray();
	}
}
