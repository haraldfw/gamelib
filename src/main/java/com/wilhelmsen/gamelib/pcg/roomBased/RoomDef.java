package com.wilhelmsen.gamelib.pcg.roomBased;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Harald Wilhelmsen on 11/14/2014.
 */
public final class RoomDef {

    public final int x;
    public final int y;
    public final int width;
    public final int height;
    private HashMap<String, DoorDef> doorDefs;
    private int maxConnections = 2;

    private ArrayList<DoorDef> unclaimedDoors;

    public RoomDef(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        // place doors
        int doorX;
        int doorY;

        for (int dx = 0; dx < width; dx++) {
            // calculate x-coordinate
            doorX = x + dx;

            // top wall
            doorY = y + height;
            doorDefs.put(String.valueOf(doorX) + String.valueOf(doorY), new DoorDef(this, doorX, doorY));

            // bottom wall
            doorY = y - 1;
            doorDefs.put(String.valueOf(doorX) + String.valueOf(doorY), new DoorDef(this, doorX, doorY));
        }
        for (int dy = 0; dy < height; dy++) {
            // calculate y-coordinate
            doorY = y + dy;

            // left wall
            doorX = x - 1;
            doorDefs.put(String.valueOf(doorX) + String.valueOf(doorY), new DoorDef(this, doorX, doorY));

            // right wall
            doorX = x + width;
            doorDefs.put(String.valueOf(doorX) + String.valueOf(doorY), new DoorDef(this, doorX, doorY));
        }

        // add all doors to unclaimedDoors list
        unclaimedDoors.addAll(doorDefs.values());
    }

    /**
     * Finalizes the room by removing all doors that do not lead to anything. This method will
     * re-create the Doors-array into an array with only the doors that lead to something.
     */
    public void finalizeDoor() {
        // List of all doors that will be final
        ArrayList<DoorDef> doorDefList = new ArrayList<>();

        // loop through all doors. If a door does not have an end, remove it
        for (Map.Entry<String, DoorDef> entry : doorDefs.entrySet()) {
            if (entry.getValue().end == null) {
                doorDefs.remove(entry.getKey());
            }
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
        if (maxConnections > width * 2 + height * 2) {
            maxConnections = width * 2 + height * 2;
            return false;
        } else {
            return true;
        }
    }

    public int amountOfUnclaimedDoors() {
        return unclaimedDoors.size();
    }

    public boolean canHaveMoreConnections() {
        return !unclaimedDoors.isEmpty();
    }

    public DoorDef getRandomUnclaimedDoor(Random rng) {
        return unclaimedDoors.get(rng.nextInt(unclaimedDoors.size()));
    }

    public DoorDef getDoorTo(int x, int y) {
        return doorDefs.get(String.valueOf(x) + String.valueOf(y));
    }
}
