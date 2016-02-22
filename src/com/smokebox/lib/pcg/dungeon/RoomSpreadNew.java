package com.smokebox.lib.pcg.dungeon;

import com.smokebox.lib.utils.geom.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Harald on 22.2.16.
 */
public class RoomSpreadNew {

  private RoomSpreadNew() {
  }

  public static int[][] getNew(int amountOfCells, float xSpreadScale,
                               float ySpreadScale, int roomDimScalar,
                               float maxRoomRatio, Random rand) {
    if (rand == null) {
      rand = new Random();
    }
    // generate list of rectangles
    List<Rectangle> rectangles =
        generateRects(amountOfCells, xSpreadScale, ySpreadScale, roomDimScalar, maxRoomRatio, rand);

    // seperate rectangles
    seperate(rectangles);


    return new int[0][];
  }

  private static List<Rectangle> generateRects(int amountOfCells, float xSpreadScale,
                                               float ySpreadScale, int roomDimScalar,
                                               float maxRoomRatio, Random rand) {
    List<Rectangle> rects = new ArrayList<>();
    while (rects.size() < amountOfCells * 10) {
      // generate a new rect with random specs.
      Rectangle rect =
          new Rectangle(rand.nextFloat() * xSpreadScale, rand.nextFloat() * ySpreadScale,
                        rand.nextFloat() * roomDimScalar, rand.nextFloat() * roomDimScalar);
      // if specs pass ratio-check, add to rectangle-list
      if (rect.width / rect.height <= maxRoomRatio && rect.height / rect.width <= maxRoomRatio) {
        rects.add(rect);
      }
    }
    return rects;
  }

  private static void seperate(List<Rectangle> rectangles) {
    List<Rectangle> notHandled = new ArrayList<>();
    Collections.copy(notHandled, rectangles);
    while (notHandled.size() > 0) {
      // get the first rect in the notHandled-list
      Rectangle toHandle = notHandled.get(0);
      rectangles.remove(0);
      // if this was the last rectangle in the list, stop the loop
      if (notHandled.size() == 0) {
        break;
      }
      for (Rectangle r : notHandled) {

      }
    }
  }

  public static RoomsWithTree roomSpreadFloor2(int amountOfCells, float xSpreadScale,
                                               float ySpreadScale, int roomDimScalar,
                                               float maxRoomRatio, Random rand) {
    if (rand == null) {
      rand = new Random();
    }
    ArrayList<Cell> cells = new ArrayList<>();
    ArrayList<Cell> finalCells = new ArrayList<>();
    ArrayList<Rectangle> corridors = new ArrayList<>();
    MinimumSpanningTree tree = new MinimumSpanningTree(cells);

    for (int i = 0; i < amountOfCells * 10; i++) {
      cells.add(new Cell(
          new Rectangle(rand.nextFloat() * xSpreadScale, rand.nextFloat() * ySpreadScale,
                        rand.nextFloat() * roomDimScalar, rand.nextFloat() * roomDimScalar)));
    }

    return new RoomsWithTree(tree, finalCells, cells, corridors);
  }
}
