package com.wilhelmsen.gamelib.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Array2 {

    /**
     * Burns the given int2 onto the given int2 map.
     *
     * @param toBeBurned The int2 that is to be burned
     * @param locX       The left edge of where it is to be burned
     * @param locY       The bottom edge of where it is to be burned
     * @param map        The int2 to burn onto
     * @return merged int[][]
     */
    public static int[][] burnInt2OnInt2(int[][] toBeBurned, int locX, int locY, int[][] map) {

        if (locX + toBeBurned.length > map.length || locY + toBeBurned[0].length > map[0].length) {
            System.out.println(
                    "Unable to burn array, due to 'toBeBurned' exiting bounds of 'map'. Returning 'map'");
            return map;
        }

        for (int x = 0; x < toBeBurned.length; x++) {
            System.arraycopy(toBeBurned[x], 0, map[locX + x], locY + 0, toBeBurned[0].length);
        }

        return map;
    }

    public static int sumInt2(int[][] array) {
        int sum = 0;
        for (int[] anArray : array) {
            for (int j = 0; j < array[0].length; j++) {
                sum += anArray[j];
            }
        }

        return sum;
    }

    public static int[][] strokeWalls(int[][] map, int emptySpace, int floor, int useAsWall) {

        bufferInt2(map, 2);

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y] == emptySpace && surroundingInt(map, 1, x, y, 1)) {
                    map[x][y] = useAsWall;
                }
            }
        }
        map = crop(new int[]{0}, map);

        return map;
    }

    private static boolean surroundingInt(int[][] mapToCheck, int intToCheckFor, int cellX, int cellY,
                                          int range) {

        for (int i = -range; i <= range; i++) {
            for (int j = -range; j <= range; j++) {

                int checkX = cellX + i;
                int checkY = cellY + j;

                if (checkX >= 0 && checkX < mapToCheck.length && checkY >= 0
                        && checkY < mapToCheck[0].length // Not outside an edge
                        && (i != 0 || j != 0) // Not on the cell itself
                        && mapToCheck[checkX][checkY] == intToCheckFor) { // Cell is the one we're looking for
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Adds a buffer(or border) around the int2.
     *
     * @param array  The array to buffer
     * @param buffer The thickness of the buffer
     * @return The buffered int2
     */
    public static int[][] bufferInt2(int[][] array, int buffer) {
        int[][] newArray = new int[array.length + buffer * 2][array[0].length + buffer * 2];

        for (int x = 0; x < array.length; x++) {
            System.arraycopy(array[x], 0, newArray[x + buffer], 0 + buffer, array[0].length);
        }

        return newArray;
    }

    /**
     * Returns a copy of the given array
     *
     * @param oldArray The array to be copied
     * @return The copy
     */
    public static int[][] copyInt2(int[][] oldArray) {
        int[][] newArray = new int[oldArray.length][oldArray[0].length];

        for (int x = 0; x < oldArray.length; x++) {
            System.arraycopy(oldArray[x], 0, newArray[x], 0, oldArray[0].length);
        }

        return newArray;
    }

    /**
     * Prints the given int2 into the console. The print-methods starts at the end of the array, so
     * that (0,0) will be in the bottom left corner All 0s in the array becomes a "#", everything else
     * becomes a "."
     *
     * @param map The array to print
     */
    public static void printInt2(int[][] map) {
        for (int y = map[0].length - 1; y >= 0; y--) {
            System.out.print("\n");
            for (int[] aMap : map) {
                int m = aMap[y];
                String s = " " + String.valueOf(m);
                //System.out.print(map[x][y]);
                if (m == 0) {
                    s = " #";
                } else if (m == 1) {
                    s = " .";
                }
                System.out.print(s);
            }
        }
        System.out.print("\n");
    }

    /**
     * Prints a 2DInt as is, with numbers. Y-up.
     *
     * @param map The 2DInt to print.
     */
    public static void printInt2Raw(int[][] map) {
        for (int y = map[0].length - 1; y >= 0; y--) {
            System.out.print("\n");
            for (int[] aMap : map) {
                int m = aMap[y];
                System.out.print(" \t" + String.valueOf(m));
            }
        }
        System.out.print("\n");
    }

    /**
     * Prints a float2. Primarily used in the creation of the minimum spanning tree-algorithm to se
     * node-relations.
     *
     * @param map The array to print.
     */
    public static void printFloat2(float[][] map) {
        System.out.print("\n");
        for (int i = -1; i < map.length; i++) {
            if (i >= 0) {
                System.out.print(i + "\t\t");
            } else {
                System.out.print("index\t");
            }
        }
        for (int i = 0; i < map.length; i++) {
            System.out.print("\n" + i + "\t");
            for (int j = 0; j < map[0].length; j++) {
                float m = map[i][j];
                String s = "none";
                if (m != 0) {
                    s = String.valueOf(m);
                }
                System.out.print(s + (s.length() <= 7 ? "\t\t" : "\t"));
            }
        }
        System.out.print("\n\n");
    }

    /**
     * Increases the resolution of an int2. As in:   "#" Becomes: "##" "##"
     *
     * @param factor The factor in which to increase the resolution with
     * @param map    The int2 to increase resolution on.
     * @return The new array
     */
    public static int[][] increaseResolutionInt2(int factor, int[][] map) {
        int[][] newMap = new int[map.length * factor][map[0].length * factor];

        for (int x = 0; x < newMap.length; x++) {
            for (int y = 0; y < newMap[0].length; y++) {
                newMap[x][y] = map[x / factor][y / factor];
            }
        }

        return newMap;
    }

    /**
     * Translates a 2d map in a 1d array to a 2d array
     *
     * @param int1  The old map
     * @param width The width of the map
     * @return The 2d array
     */
    public static int[][] translateInt1ToInt2(int[] int1, int width) {
        if (((float) int1.length / (float) width) % 1 != 0) {
            System.out.println("Invalid width-parameter in translateInt1To2-method. Returning null");
            return null;
        }

        int[][] int2 = new int[width][int1.length / width];

        int x = 0;
        int y = 0;

        for (int anInt1 : int1) {
            if (x == width) {
                x = 0;
                y++;
            }
            int2[x][y] = anInt1;
            x++;
        }

        return int2;
    }

    /**
     * Removes all empty rows and columns in an array. Effectively "cropping" the array.
     *
     * @param ignoredInts An array containing the ints that can be cropped
     * @param map         The array2 to crop
     * @return The cropped array
     */
    public static int[][] crop(int[] ignoredInts, int[][] map) {
        System.out.println("Starting crop...");

        int bottomBound = 0;
        int topBound = map[0].length - 1;
        int leftBound = 0;
        int rightBound = map.length - 1;

// Find leftBound, start -------------------------------------------------
        for (int x = 0; x < map.length; x++) {
            boolean boundFound = false;

            // The y-value of the tileInQuestion
            int yCheck = 0;

            while (!boundFound) {
                boundFound = true;
                // Define an Integer to compare with the ignoredInts-array
                int tileInQuestion = map[x][yCheck];
                // If ignoredInts does not contain the mapTile
                if (int1ContainsInt(tileInQuestion, ignoredInts)) {
                    boundFound = false;
                    //System.out.println(tileInQuestion + " exists in ignoredInts. boundFound = " +
                    // boundFound);
                }
                // Increment the y-value
                yCheck++;
                // If all y-values have been checked, break the while-loop
                if (yCheck == map[0].length) {
                    break;
                }
            }

            // If the bound is found break the loop
            if (boundFound) {
                leftBound = x;
                break;
            }
            //System.out.println("LeftBound-find checked for \n x = " + x);
        }
// Find leftBound, end -------------------------------------------------

// Find rightBound, start -------------------------------------------------
        for (int x = map.length - 1; x >= 0; x--) {
            boolean boundFound = false;

            // The y-value of the tileInQuestion
            int yCheck = 0;

            while (!boundFound) {
                boundFound = true;
                // Define an Integer to compare with the ignoredInts-array
                int tileInQuestion = map[x][yCheck];
                // If ignoredInts does not contain the mapTile
                if (int1ContainsInt(tileInQuestion, ignoredInts)) {
                    boundFound = false;
                    //System.out.println(tileInQuestion + " exists in ignoredInts. boundFound = " +
                    // boundFound);
                }
                // Increment the y-value
                yCheck++;
                // If all y-values have been checked, break the while-loop
                if (yCheck == map[0].length) {
                    break;
                }
            }

            // If the bound is found break the loop
            if (boundFound) {
                rightBound = x + 1;
                break;
            }
            //System.out.println("RightBound-find checked for \n x = " + x);
        }
// Find rightBound, end -------------------------------------------------

// Find bottomBound, start ------------------------------------------------
        for (int y = 0; y < map[0].length; y++) {
            boolean boundFound = false;

            // The y-value of the tileInQuestion
            int xCheck = 0;

            while (!boundFound) {
                boundFound = true;
                // Define an Integer to compare with the ignoredInts-array
                int tileInQuestion = map[xCheck][y];
                // If ignoredInts does not contain the mapTile
                if (int1ContainsInt(tileInQuestion, ignoredInts)) {
                    boundFound = false;
                    //System.out.println(tileInQuestion + " exists in ignoredInts. boundFound = " +
                    // boundFound);
                }
                // Increment the -value
                xCheck++;
                // If all x-values have been checked, break the while-loop
                if (xCheck == map.length) {
                    break;
                }
            }

            // If the bound is found break the loop
            if (boundFound) {
                bottomBound = y;
                break;
            }
            //System.out.println("BotBound-find checked for \n y = " + y);
        }
// Find bottomBound, end ------------------------------------------------

// Find topBound, start -------------------------------------------------
        for (int y = map[0].length - 1; y >= 0; y--) {
            boolean boundFound = false;

            // The y-value of the tileInQuestion
            int xCheck = 0;

            while (!boundFound) {
                boundFound = true;
                // Define an Integer to compare with the ignoredInts-array
                int tileInQuestion = map[xCheck][y];
                // If ignoredInts does not contain the mapTile
                if (int1ContainsInt(tileInQuestion, ignoredInts)) {
                    boundFound = false;
                    //System.out.println(tileInQuestion + " exists in ignoredInts. boundFound = " +
                    // boundFound);
                }
                // Increment the x-value
                xCheck++;
                // If all x-values have been checked, break the while-loop
                if (xCheck == map.length) {
                    break;
                }
            }

            // If the bound is found break the loop
            if (boundFound) {
                topBound = y + 1;
                break;
            }
            //System.out.println("TopBound-find checked for \n y = " + y);
        }
// Find topBound, end -------------------------------------------------

        System.out.print("\n");
        System.out.println("BottomBound =\t" + bottomBound);
        System.out.println("TopBound =\t" + topBound);
        System.out.println("LeftBound =\t" + leftBound);
        System.out.println("RightBound =\t" + rightBound);
        System.out.println("Bounds found, moving on to cropping...");

        int[][] croppedMap = new int[rightBound - leftBound][topBound - bottomBound];

        for (int x = 0; x < croppedMap.length; x++) {
            System.arraycopy(map[x + leftBound], 0 + bottomBound, croppedMap[x], 0, croppedMap[0].length);
        }

        return croppedMap;
    }

    /**
     * Runs 1(!) gameOfLife-like cellular automata process on the 2Darray
     *
     * @param map       The int2 to run the process on
     * @param threshold The threshold from where tiles become wall or not.
     * @return The processed array
     */
    public static int[][] cellularAutomataProcess(int[][] map, int threshold) {
        // FIXME
        int[][] oldMap = copyInt2(map);

        for (int x = 1; x < map.length - 1; x++) {
            for (int y = 1; y < map[0].length - 1; y++) {

                int surroundSum = 0;

                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        if (!(x + i < 0 || x + i > map.length - 1
                                || y + j < 0 || y + j > map[0].length - 1)) {
                            surroundSum += oldMap[x + i][y + j];
                        }
                    }
                }

                if (surroundSum > threshold) {
                    map[x][y] = 1;
                }
            }
        }

        return map;
    }

    /**
     * Returns a int2 with random numbers in their tiles
     *
     * @param width      The width of the array
     * @param height     The height of the array
     * @param rand       A random object, can be null
     * @param difference The difference between the random min and max
     * @param minimum    The lowest random
     * @return The random-array
     */
    public static int[][] randomInt2(int width, int height, Random rand, int difference,
                                     int minimum) {

        if (rand == null) {
            rand = new Random();
        }

        int[][] map = new int[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                map[x][y] = minimum + rand.nextInt(difference);
            }
        }

        return map;
    }

    /**
     * returns true if the given int-array contains the given int
     *
     * @param i    The int to search for
     * @param int1 The array to search in
     * @return Boolean telling if it contains
     */
    public static boolean int1ContainsInt(int i, int[] int1) {
        for (int anInt1 : int1) {
            if (i == anInt1) {
                return true;
            }
        }

        return false;
    }

    /**
     * Saves the given int2 as an image
     *
     * @param map The int2 to save
     */
    @Deprecated
    public static void saveMapAsImage(int[][] map) {
        BufferedImage
                theImage =
                new BufferedImage(map.length, map[0].length, BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < map[0].length; y++) {
            for (int x = 0; x < map.length; x++) {
                int m = map[x][y];
                int c = (m == 0) ? 0 : 255;
                theImage.setRGB(x, y, c);
            }
        }

        File outputfile = new File("saved.png");

        try {
            ImageIO.write(theImage, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to write image-file. Check permissions. ");
        }
    }

    /**
     * Returns a circle in an int2
     *
     * @param radius The radius of the circle
     * @return An array containing the circle
     */
    public static int[][] circle(int radius) {
        int[][] circle = new int[radius * 2 + 1][radius * 2 + 1];

        int size = circle.length;

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {

                float dx = x - size / 2;
                float dy = y - size / 2;

                float lengthSquared = dx * dx + dy * dy;
                float radiusSquared = radius * radius;

                if (lengthSquared <= radiusSquared) {
                    circle[x][y] = 1;
                }
            }
        }

        return circle;
    }
}

