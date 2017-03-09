/**
 *
 */
package com.wilhelmsen.gamelib.pcg.dungeon;

import com.wilhelmsen.gamelib.utils.Intersect;
import com.wilhelmsen.gamelib.utils.geom.Line;
import com.wilhelmsen.gamelib.utils.geom.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Harald Floor Wilhelmsen
 */
public class RoomSpreadDungeon {

    public static RoomsWithTree roomSpreadFloor(int cells, int roomDimScalar, float maxRoomRatio,
                                                Random rand, boolean debugPrints) {

        // Time-keeping (debugging)
        float timeStarted = System.nanoTime();
        float timeOnRoomsAndSeperation;
        float timeOnTree = 0;
        float timeOnCorridorsAndCollisions;
        float totalTimeUsed;

        Random random = (rand == null) ? new Random() : rand;

        // A scalar for room-dimensions
        //int roomDimScalar = 5;
        // The distribution of the rooms on spawn.
        // A smaller distribution generally gives a poorer performance -
        // due to the separation-process taking more time
        float distributionScale = cells * roomDimScalar / 2;
        // The accepted ratio for the rooms' dimensions.
        // A higher number gives longer rooms
        //float maxRoomRatio = 2f;

        // Get random rooms
        if (debugPrints) {
            System.out.println("Generating rooms");
            System.out.println("\tCells: " + cells);
            System.out.println("\tDistribution: " + distributionScale);
            System.out.println("\tRatio: " + 1 / maxRoomRatio + "-" + maxRoomRatio);
        }
        ArrayList<Cell>
                rooms =
                getListRandomCells(cells, random, roomDimScalar, distributionScale, distributionScale,
                        maxRoomRatio);
        if (debugPrints) {
            System.out.println("Generating rooms finished. Time used so far: "
                    + ((System.nanoTime() - timeStarted) / Math.pow(10, 9)) + " seconds");

            System.out.println("Seperating the rooms...");
        }

        // Separate rooms
        Intersect.separateCells(rooms, random, 0.01f);

        for (Cell c : rooms) {
            c.rect.round();
        }

        timeOnRoomsAndSeperation = System.nanoTime() - timeStarted;

        if (debugPrints) {
            System.out.println("Finished seperating rooms. Time used so far: "
                    + ((System.nanoTime() - timeStarted) / Math.pow(10, 9)) + " seconds");
            System.out.println("Creating the tree...");
        }

        // The outer most coordinates of rooms
        int[] bounds = findBounds(rooms);

        // Shift rooms so lowest possible coordinates start at (0,0)
        // Rooms are shifted up/right
        for (int i = 0; i < rooms.size(); i++) {
            Cell c = rooms.get(i);
            c.rect.x -= (float) bounds[0];
            c.rect.y -= (float) bounds[1];
        }

        // Sort rooms by area, bigger first
        Collections.sort(rooms, (c1, c2) -> {
            return (int) (c2.rect.area() - c1.rect.area());
        });

        ArrayList<Cell> biggerRooms = new ArrayList<>();

        for (int i = 0; i < rooms.size() / 10; i++) {
            biggerRooms.add(rooms.get(i));
        }
        // Tree
        MinimumSpanningTree tree = MinimumSpanningTree.createFromCells(biggerRooms);
        if (debugPrints) {
            timeOnTree = System.nanoTime() - timeStarted - timeOnRoomsAndSeperation;
        }

        // Edges
        ArrayList<Line> edges = new ArrayList<>(tree.edges);

        // Corridors
        int corridorWidth = 3;
        ArrayList<Rectangle> corridors = new ArrayList<>();
        for (Line e : edges) {
            Rectangle horCorr = new Rectangle();

            horCorr.x = (e.x <= e.x2 ? e.x : e.x2) - 1;
            horCorr.width = Math.abs(e.x2 - e.x) + 2;

            horCorr.height = corridorWidth;
            horCorr.y = (e.y2 - (corridorWidth - 1) / 2);

            corridors.add(horCorr);

            Rectangle verCorr = new Rectangle();

            verCorr.y = (e.y <= e.y2 ? e.y : e.y2) - 1;
            verCorr.height = Math.abs(e.y2 - e.y) + 2;

            verCorr.width = corridorWidth;
            verCorr.x = (e.x - (corridorWidth - 1) / 2);

            corridors.add(verCorr);
        }

        // Generate int[][] from map to fill empty spaces
        int[][] map = new int[bounds[2] - bounds[0]][bounds[3] - bounds[1]];

        for (Cell c : rooms) {
            burnRoom(map, c.rect);
        }

        // Fill empty space with 1x1 rooms
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] == 0) {
                    rooms.add(new Cell(new Rectangle(i, j, 1, 1)));
                }
            }
        }

        // Filtering through and only keeping rooms intersecting corridors
        ArrayList<Cell> finalRooms = new ArrayList<>();
        for (Rectangle corr : corridors) {
            for (Cell r : rooms) {
                if (Intersect.intersection(corr, r.rect)) {
                    finalRooms.add(r);
                }
            }
        }

        if (debugPrints) {
            System.out.println("Done adding corridors.");
            timeOnCorridorsAndCollisions =
                    System.nanoTime() - timeStarted - timeOnRoomsAndSeperation - timeOnTree;
            totalTimeUsed = System.nanoTime() - timeStarted;
            System.out.println("FinalRooms in list: " + finalRooms.size() + ".");

            System.out.println("Generating done. " + cells + " rooms placed and positioned. ");
            System.out.println("Total amount of cells is " + finalRooms.size() + " for the whole map.");
            System.out.println("Total time: " + totalTimeUsed / Math.pow(10, 9) + " seconds");

            System.out.println(
                    "Time on rooms and seperation: " + timeOnRoomsAndSeperation / Math.pow(10, 9) + " seconds. "
                            +
                            "\nTime on tree: " + timeOnTree / Math.pow(10, 9) + " seconds. " +
                            "\nTime on Corridors and collisions: " + timeOnCorridorsAndCollisions / Math.pow(10, 9)
                            + " seconds.");
        }
        //map = new int[bounds[2] - bounds[0]][bounds[3] - bounds[1]];
        return new RoomsWithTree(tree, finalRooms, rooms, corridors);
    }

    public static ArrayList<Cell> getListRandomCells(
            int cells,
            Random random,
            float roomDimScalar,
            float xDistributionScale,
            float yDistrubutionScale,
            float maxSideRatio) {
        Rectangle rect = new Rectangle();
        ArrayList<Cell> rooms = new ArrayList<>();
        for (int i = 0; i < cells; i++) {
            boolean roomAccepted = false;

            while (!roomAccepted) {
                rect = new Rectangle(
                        random.nextFloat() * xDistributionScale,
                        random.nextFloat() * yDistrubutionScale,
                        (float) Math.round(Math.abs(random.nextGaussian() * roomDimScalar)),
                        (float) Math.round(Math.abs(random.nextGaussian() * roomDimScalar))
                );

                float ratio = rect.height / rect.width;
                if (ratio < maxSideRatio && ratio > 1 / maxSideRatio
                        && Intersect.onRng(rect.width, 3, 50) && Intersect.onRng(rect.height, 3, 50)) {
                    roomAccepted = true;
                }
            }

            rooms.add(new Cell(rect));
        }
        return rooms;
    }

    private static int[][] burnRoom(int[][] map, Rectangle r) {
        float xStart = r.x;
        float xEnd = r.x + r.width;
        float yStart = r.y;
        float yEnd = r.y + r.height;

        for (int i = (int) xStart; i < xEnd; i++) {
            for (int j = (int) yStart; j < yEnd; j++) {
                map[i][j] = 1;
            }
        }

        return map;
    }

    public static int[] findBounds(List<Cell> rooms) {
        int[] bounds = new int[4];

        for (int i = 0; i < rooms.size(); i++) {
            Rectangle rect = rooms.get(i).rect;

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

    public static int[][] asInt2(RoomsWithTree roomTree) {
        int[] bounds = findBounds(roomTree.rooms);

        int[][] map = new int[bounds[2] - bounds[0]][bounds[3] - bounds[1]];

        for (int i = 0; i < roomTree.rooms.size(); i++) {
            burnRoom(map, roomTree.rooms.get(i).rect);
        }

        return map;
    }
}
