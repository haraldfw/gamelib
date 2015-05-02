package com.smokebox.lib.pcg.caves;

import com.smokebox.lib.utils.Array2;

import java.util.Random;

public class CellAutomataCaves {

  public static int[][] generateCaves(int width, int height, Random rand) {
    int[][] map = Array2.randomInt2(width, height, rand, 2, 0); // Get a random map to work from
    map = Array2.copyInt2(map);

    for (int i = 0; i < 1; i++) {
      Array2.cellularAutomataProcess(map, 4);
    }

    return map; // Map
  }
}
