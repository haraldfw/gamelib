package com.wilhelmsen.gamelib.pcg.dungeon;

import com.wilhelmsen.gamelib.utils.Intersect;
import com.wilhelmsen.gamelib.utils.geom.Line;
import com.wilhelmsen.gamelib.utils.geom.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
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
                                 float ySpreadScale, float roomDimScalar,
                                 int corridorWidth, float maxRoomRatio,
                                 Random rand) {
        if (rand == null) {
            rand = new Random();
        }
        // generate list of rectangles
        List<Rectangle> rectangles =
                generateRectangles(amountOfCells, xSpreadScale, ySpreadScale, roomDimScalar, maxRoomRatio,
                        rand);

        // separate rectangles
        separate(rectangles, rand);

        // round the positions of the rectangles
        for (Rectangle r : rectangles) {
            r.floor();
        }

        // shift map so that the left and bottom bounds are at 0
        int[] bounds = findBounds(rectangles);
        shiftRectangles(rectangles, bounds);

        // select the biggest rooms
        List<Rectangle> mainRooms = getBiggestRooms(amountOfCells, rectangles);

        // Fill empty spaces
        fillEmptySpace(rectangles);

        // Tree for corridor-spanning
        List<Line> corridorLines = MinimumSpanningTree.createFromRects(mainRooms).edges;

        rectangles = addCorridors(rectangles, corridorLines, corridorWidth, rand);

        System.out.println("final: " + Arrays.toString(findBounds(rectangles)));

        return asArr(rectangles);
    }

    private static List<Rectangle> addCorridors(List<Rectangle> rectangles, List<Line> edges,
                                                int corridorWidth, Random random) {
        List<Rectangle> corridorRects = new ArrayList<>();
        for (Line l : edges) {
            l.ensureCorrectDirection();
            // all corridors turn one way as of now, TODO fix this
            float x = l.x - 1;
            float y = l.y - corridorWidth / 2.0f;
            float w = l.x2 - l.x + 1;
            float h = corridorWidth;
            corridorRects.add(new Rectangle(x, y, w, h));
            x = l.x2 - corridorWidth / 2.0f;
            y = (l.y <= l.y2 ? l.y : l.y2) - 1;
            w = corridorWidth;
            h = Math.abs(l.y + l.y2) + 1;
            corridorRects.add(new Rectangle(x, y, w, h));
        }

        List<Rectangle> newRooms = new ArrayList<>();
        for (Rectangle corr : corridorRects) {
            for (Rectangle r : rectangles) {
                if (Intersect.intersection(corr, r)) {
                    newRooms.add(r);
                }
            }
        }

        System.out.println(Arrays.toString(corridorRects.toArray()));
        return newRooms;
    }

    private static int[][] asArr(List<Rectangle> rectangles) {
        int[] bounds = findBounds(rectangles);
        System.out.println(rectangles.size());
        int[][] map = new int[bounds[2]][bounds[3]];
        for (Rectangle r : rectangles) {
            for (int x = (int) r.x; x < r.x2(); x++) {
                for (int y = (int) r.y; y < r.y2(); y++) {
                    map[x][y] = 1;
                }
            }
        }
        return map;
    }

    private static List<Rectangle> getBiggestRooms(int amount, List<Rectangle> rectangles) {
        if (amount >= rectangles.size()) {
            throw new IllegalArgumentException(
                    "amount is larger than rectangles-list; amount: " + amount
                            + ", rectangles-size: " + rectangles.size());
        }
        rectangles.sort((Rectangle o1, Rectangle o2) -> (int) Math.floor(o2.area() - o1.area()));

        List<Rectangle> biggest = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            biggest.add(rectangles.get(i));
        }
        return biggest;
    }

    private static void fillEmptySpace(List<Rectangle> rectangles) {
        int[] bounds = findBounds(rectangles);

        List<Rectangle> toAdd = new ArrayList<>();
        for (int x = 0; x < bounds[2]; x++) {
            for (int y = 0; y < bounds[3]; y++) {
                if (!intersects(x, y, rectangles)) {
                    toAdd.add(new Rectangle(x, y, 1, 1));
                }
            }
        }
        for (Rectangle r : toAdd) {
            rectangles.add(r);
        }
    }

    private static boolean intersects(int x, int y, List<Rectangle> rectangles) {
        for (Rectangle r : rectangles) {
            if (!notIntersects(x, y, r)) {
                return true;
            }
        }
        return false;
    }

    private static boolean notIntersects(int x, int y, Rectangle r) {
        return x < r.x || r.x >= r.x2() || y < r.y || r.y >= r.y2();
    }

    private static List<Rectangle> generateRectangles(int amountOfCells, float xSpreadScale,
                                                      float ySpreadScale, float roomDimScalar,
                                                      float maxRoomRatio, Random rand) {
        List<Rectangle> rects = new ArrayList<>();
        while (rects.size() < amountOfCells * 10) {
            // generate a new rect with random specs.
            float x = rand.nextFloat() * xSpreadScale;
            float y = rand.nextFloat() * ySpreadScale;
            float w = (rand.nextFloat() + 1) * roomDimScalar;
            float h = (rand.nextFloat() + 1) * roomDimScalar;
            Rectangle rect = new Rectangle(x, y, w, h);
            // if specs pass ratio-check, add to rectangle-list
            float ratio = rect.height / rect.width;
            if (ratio < maxRoomRatio && ratio > 1 / maxRoomRatio) {
                rects.add(rect);
            }
        }
        return rects;
    }

    private static void separate(List<Rectangle> rectangles, Random random) {
        ArrayList<Cell> asCells = new ArrayList<>();
        for (Rectangle r : rectangles) {
            asCells.add(new Cell(r));
        }
        while (Intersect.runOneSeparationIteration(asCells, random, 0.1f));
    }

    private static int[] findBounds(List<Rectangle> rectangles) {
        // bounds{left, bottom, right, top}
        int[] bounds =
                new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};
        // find the outer most rectangles
        for (Rectangle rect : rectangles) {

            float xLeft = rect.x; // left bound
            if (xLeft < bounds[0]) {
                bounds[0] = (int) Math.floor(xLeft);
            }

            float yBot = rect.y; // bottom bound
            if (yBot < bounds[1]) {
                bounds[1] = (int) Math.floor(yBot);
            }

            float xRight = rect.x + rect.width; // right bound
            if (xRight > bounds[2]) {
                bounds[2] = (int) Math.ceil(xRight);
            }

            float yTop = rect.y + rect.height; // top bound
            if (yTop > bounds[3]) {
                bounds[3] = (int) Math.ceil(yTop);
            }
        }

        return bounds;
    }

    private static void shiftRectangles(List<Rectangle> rectangles, int[] bounds) {
        for (Rectangle rect : rectangles) {
            rect.x -= bounds[0];
            rect.y -= bounds[1];
        }
    }
}
