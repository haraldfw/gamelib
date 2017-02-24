package com.wilhelmsen.gamelib.pcg.dungeon;

import com.wilhelmsen.gamelib.utils.IntVector2;

import java.util.Random;

public class Dungeon {

	/*
         * 3: Door
	 * 2: Perimeter
	 * 1: Floor
	 * 0: Nothingness
	 */

  public static int[][] newDungeon(int size, int roomAmount, int roomMinDim, int roomMaxDim,
                                   boolean corridors, int corridorWidth, int corridorLength,
                                   Random rand) {

    if (rand == null) { // If random passed as null, create a new random
      rand = new Random();
    }

    int[][] map = new int[size][size]; // The map array to be returned

    Room spawnRoom = new Room(size / 2 - 5, size / 2 - 5, 10, 10); // Starting room
    if (enoughSpace(spawnRoom, map)) {
      burnRoom(spawnRoom, map); // Burn starting-room into map
    }

    return map;
  }

  public static Room findNextRoom(int roomMinDim, int roomMaxDim, int size, Random rand,
                                  int[][] map) {
    Room room = new Room();
    room.w = roomMinDim + rand.nextInt(roomMaxDim - roomMinDim);
    room.h = roomMinDim + rand.nextInt(roomMaxDim - roomMinDim);

    boolean roomPlaced = false;
    while (!roomPlaced) {
      IntVector2 wallTile = randomWallTile(rand, map);
      IntVector2 openDirection = findRandomDir(wallTile, rand, map);

      boolean wallTileAccesible = (openDirection.x != 0 || openDirection.y != 0);

      if (wallTileAccesible && !enoughSpace(room, map)) {
        fitRoom(room, map);
      }

      if (wallTileAccesible && enoughSpace(room, map)) {
        burnRoom(room, map);
        roomPlaced = true;
      }
    }

    return room;
  }

  private static IntVector2 randomWallTile(Random rand, int[][] map) {
    IntVector2 v = new IntVector2();

    while (map[v.x][v.y] != 2) {
      v.x = rand.nextInt(map.length);
      v.y = rand.nextInt(map[0].length);
    }

    return v;
  }

  private static IntVector2 findRandomDir(IntVector2 wallTile, Random rand, int[][] map) {
    IntVector2 dir = new IntVector2();

    int x = wallTile.x;
    int y = wallTile.y;

    if (map[x + 1][y] == 0) {
      dir.set(1, 0);
    } else if (map[x - 1][y] == 0) {
      dir.set(-1, 0);
    } else if (map[x][y + 1] == 0) {
      dir.set(0, 1);
    } else if (map[x][y - 1] == 0) {
      dir.set(0, -1);
    } else {
      dir.set(0, 0);
    }

    return dir;
  }

  private static int[][] burnRoom(Room room, int[][] map) {

    int x = room.x;
    int y = room.y;
    int w = room.w;
    int h = room.h;

    for (int i = 0; i < w; i++) {
      for (int j = 0; j < h; j++) {
        map[x + i][y + j] = ((i == 0 || i == w - 1) || (j == 0 || j == h - 1)) ? 2 : 1;
      }
    }

    return map;
  }

  private static boolean enoughSpace(Room room, int[][] map) {
    int x = room.x;
    int y = room.y;
    int w = room.w;
    int h = room.h;

    if (x < 0 || x + w > map.length || y < 0 || y + h > map[0].length) {
      return false;
    }

    for (int i = x; i < x + w; i++) {
      for (int j = y; j < y + h; j++) {
        if (map[i][j] > 0) {
          return false;
        }
      }
    }

    return true;
  }

  private static Room fitRoom(Room room, int[][] map) {
    return room;
  }
}
