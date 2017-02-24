package com.wilhelmsen.gamelib.pcg.colosseum;

import com.wilhelmsen.gamelib.utils.Array2;

public class Colosseum {

  public static int[][] generateColosseum(int size) {
    int[][] map = new int[size][size];

    map = Array2.burnInt2OnInt2(Array2.circle(size / 2 - 1), 1, 1, map);

    // Place obstacles
                /*	Place random rectangles in the middle
                 * 	Push outwards
		 */

    return map;
  }
}