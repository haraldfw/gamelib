package com.smokebox.lib.pcg.dungeon;

import java.util.Random;

import com.smokebox.lib.utils.Array2;

//http://roguebasin.roguelikedevelopment.org/index.php?title=Java_Example_of_Dungeon-Building_Algorithm
//Designed by Mike Andersen
//Java version by "Solarnus"

public class RogueBasinDungeon {
	
	Random random = new Random(1337);

	//size of the map
	private int xsize = 0;
	private int ysize = 0;

	//number of "objects" to generate on the map
	private int objects = 0;

	//define the %chance to generate either a room or a corridor on the map
	//BTW, rooms are 1st priority so actually it's enough to just define the chance of generating a room
	private int chanceRoom = 75;

	//our map
	private int[] dungeon_map = {};

	//a list over tile types we're using
	final private int tileUnused = 0;
	final private int tileDirtWall = 1;
	final private int tileDirtFloor = 2;
	final private int tileStoneWall = 3;
	final private int tileCorridor = 4;
	final private int tileDoor = 5;
	final private int tileChest = 6;

	//misc. messages to print
	private String msgXSize = "X size of dungeon: \t";
	private String msgYSize = "Y size of dungeon: \t";
	private String msgMaxObjects = "max # of objects: \t";
	private String msgNumObjects = "# of objects made: \t";

	public void createDungeon(int inx, int iny, int inobj, long seed) {
		random.setSeed(seed);
		/*******************************************************************************/
		// Here's the one generating the whole map
		if (inobj < 1) objects = 10;
		else objects = inobj;

		// Adjust the size of the map if it's too small
		if (inx < 3) xsize = 3;
		else xsize = inx;

		if (iny < 3) ysize = 3;
		else ysize = iny;

		System.out.println(msgXSize + xsize);
		System.out.println(msgYSize + ysize);
		System.out.println(msgMaxObjects + objects);

		//redefine the map var, so it's adjusted to our new map size
		dungeon_map = new int[xsize * ysize];

		//start with making the "standard stuff" on the map
		for (int y = 0; y < ysize; y++) {
			for (int x = 0; x < xsize; x++) {
				//ie, making the borders of unwalkable walls
				if (y == 0) setCell(x, y, tileStoneWall);
				else if (y == ysize-1) setCell(x, y, tileStoneWall);
				else if (x == 0) setCell(x, y, tileStoneWall);
				else if (x == xsize-1) setCell(x, y, tileStoneWall);

				//and fill the rest with dirt
				else setCell(x, y, tileUnused);
			}
		}

		/*******************************************************************************

	 And now the code of the random-map-generation-algorithm begins!

		 *******************************************************************************/

		//start with making a room in the middle, which we can start building upon
		makeRoom(xsize/2, ysize/2, 8, 6, getRand(0,3));

		//keep count of the number of "objects" we've made
		int currentFeatures = 1; //+1 for the first room we just made

		//then we start the main loop
		for (int countingTries = 0; countingTries < 1000; countingTries++) {

			//check if we've reached our quota
			if (currentFeatures == objects) {
				break;
			}

			//start with a random wall
			int newx = 0;
			int xmod = 0;
			int newy = 0;
			int ymod = 0;
			int validTile = -1;

			//1000 chances to find a suitable object (room or corridor)..
			//(yea, i know it's kinda ugly with a for-loop... -_-')

			for (int testing = 0; testing < 1000; testing++) {
				newx = getRand(1, xsize-1);
				newy = getRand(1, ysize-1);
				validTile = -1;

				//System.out.println("tempx: " + newx + "\ttempy: " + newy);

				if (getCell(newx, newy) == tileDirtWall || getCell(newx, newy) == tileCorridor) {
					//check if we can reach the place
					if (getCell(newx, newy+1) == tileDirtFloor || getCell(newx, newy+1) == tileCorridor) {
						validTile = 0; //
						xmod = 0;
						ymod = -1;
					}
					else if (getCell(newx-1, newy) == tileDirtFloor || getCell(newx-1, newy) == tileCorridor) {
						validTile = 1; //
						xmod = +1;
						ymod = 0;
					}

					else if (getCell(newx, newy-1) == tileDirtFloor || getCell(newx, newy-1) == tileCorridor) {
						validTile = 2; //
						xmod = 0;
						ymod = +1;
					}

					else if (getCell(newx+1, newy) == tileDirtFloor || getCell(newx+1, newy) == tileCorridor) {
						validTile = 3; //
						xmod = -1;
						ymod = 0;
					}

					//check that we haven't got another door nearby, so we won't get alot of openings besides each other

					if (validTile > -1) {
						if (getCell(newx, newy+1) == tileDoor) //north
							validTile = -1;
						else if (getCell(newx-1, newy) == tileDoor)//east
							validTile = -1;
						else if (getCell(newx, newy-1) == tileDoor)//south
							validTile = -1;
						else if (getCell(newx+1, newy) == tileDoor)//west
							validTile = -1;
					}

					//if we can, jump out of the loop and continue with the rest
					if (validTile > -1) break;
				}
			}

			if (validTile > -1) {

				//choose what to build now at our newly found place, and at what direction
				int feature = getRand(0, 100);
				if (feature <= chanceRoom) { //a new room
					if (makeRoom((newx+xmod), (newy+ymod), 8, 6, validTile)) {
						currentFeatures++; //add to our quota

						//then we mark the wall opening with a door
						setCell(newx, newy, tileDoor);

						//clean up infront of the door so we can reach it
						setCell((newx+xmod), (newy+ymod), tileDirtFloor);
					}
				}
			}
		}
		System.out.println("Building done.");

		/*******************************************************************************

	 All done with the building, let's finish this one off

		 *******************************************************************************/

		//all done with the map generation, tell the user about it and finish
		System.out.println(msgNumObjects + currentFeatures);

		// Print map
		
		for(int i = 0; i < dungeon_map.length; i++) {
			if(i%inx == 0) {
				System.out.println();
			}
			int j = dungeon_map[i];
			
			if(j == 0)  {
				System.out.print(" .");
			} else if(j == 1)  {
				System.out.print(" ¤");
			} else if(j == 2)  {
				System.out.print("  ");
			} else if(j == 3)  {
				System.out.print(" #");
			} else if(j == 4)  {
				System.out.print(" ,");
			} else if(j == 5)  {
				System.out.print(" =");
			} else if(j == 6)  {
				System.out.print(" c");
			} else {
				System.out.print(" " + j);
			}
		}
		System.out.println();
		
		int[] ignoredInts = new int[2];
		ignoredInts[0] = 3;
		ignoredInts[1] = 0;
		
		Array2.printInt2(Array2.crop(ignoredInts, Array2.translateInt1ToInt2(dungeon_map, inx)));
	}

	//setting a tile's type
	private void setCell(int x, int y, int celltype) {
		dungeon_map[x + xsize * y] = celltype;
	}

	//returns the type of a tile
	private int getCell(int x, int y) {
		return dungeon_map[x + xsize * y];
	}

	//The RNG. the seed is based on seconds from the "java epoch" ( I think..)
	//perhaps it's the same date as the unix epoch
	private int getRand(int min, int max) {
		return min + random.nextInt(max - min);
	}


	
	private boolean makeRoom(int x, int y, int xlength, int ylength, int direction) {
		/*******************************************************************************/

		//define the dimensions of the room, it should be at least 4x4 tiles (2x2 for walking on, the rest is walls)
		int xlen = getRand(4, xlength);
		int ylen = getRand(4, ylength);

		//the tile type it's going to be filled with
		int floor = tileDirtFloor; //jordgolv..
		int wall = tileDirtWall; //jordv????gg

		//choose the way it's pointing at
		int dir = 0;
		if (direction > 0 && direction < 4) dir = direction;

		switch(dir) {

		case 0: // north

			//Check if there's enough space left for it
			for (int ytemp = y; ytemp > (y-ylen); ytemp--) {
				if (ytemp < 0 || ytemp > ysize) return false;
				for (int xtemp = (x-xlen/2); xtemp < (x+(xlen+1)/2); xtemp++) {
					if (xtemp < 0 || xtemp > xsize) return false;
					if (getCell(xtemp, ytemp) != tileUnused) return false; //no space left...
				}
			}

			//we're still here, build
			for (int ytemp = y; ytemp > (y-ylen); ytemp--) {
				for (int xtemp = (x-xlen/2); xtemp < (x+(xlen+1)/2); xtemp++) {
					//start with the walls
					if (xtemp == (x-xlen/2)) setCell(xtemp, ytemp, wall);
					else if (xtemp == (x+(xlen-1)/2)) setCell(xtemp, ytemp, wall);
					else if (ytemp == y) setCell(xtemp, ytemp, wall);
					else if (ytemp == (y-ylen+1)) setCell(xtemp, ytemp, wall);
					//and then fill with the floor
					else setCell(xtemp, ytemp, floor);
				}
			}

			break;

		case 1: // east

			for (int ytemp = (y-ylen/2); ytemp < (y+(ylen+1)/2); ytemp++) {
				if (ytemp < 0 || ytemp > ysize) return false;
				for (int xtemp = x; xtemp < (x+xlen); xtemp++) {
					if (xtemp < 0 || xtemp > xsize) return false;
					if (getCell(xtemp, ytemp) != tileUnused) return false;
				}
			}

			for (int ytemp = (y-ylen/2); ytemp < (y+(ylen+1)/2); ytemp++) {
				for (int xtemp = x; xtemp < (x+xlen); xtemp++) {
					if (xtemp == x) setCell(xtemp, ytemp, wall);
					else if (xtemp == (x+xlen-1)) setCell(xtemp, ytemp, wall);
					else if (ytemp == (y-ylen/2)) setCell(xtemp, ytemp, wall);
					else if (ytemp == (y+(ylen-1)/2)) setCell(xtemp, ytemp, wall);
					else setCell(xtemp, ytemp, floor);
				}
			}

			break;

		case 2: // south

			for (int ytemp = y; ytemp < (y+ylen); ytemp++) {
				if (ytemp < 0 || ytemp > ysize) return false;
				for (int xtemp = (x-xlen/2); xtemp < (x+(xlen+1)/2); xtemp++) {
					if (xtemp < 0 || xtemp > xsize) return false;
					if (getCell(xtemp, ytemp) != tileUnused) return false;
				}
			}

			for (int ytemp = y; ytemp < (y+ylen); ytemp++) {
				for (int xtemp = (x-xlen/2); xtemp < (x+(xlen+1)/2); xtemp++) {
					if (xtemp == (x-xlen/2)) setCell(xtemp, ytemp, wall);
					else if (xtemp == (x+(xlen-1)/2)) setCell(xtemp, ytemp, wall);
					else if (ytemp == y) setCell(xtemp, ytemp, wall);
					else if (ytemp == (y+ylen-1)) setCell(xtemp, ytemp, wall);
					else setCell(xtemp, ytemp, floor);
				}
			}

			break;

		case 3: // west

			for (int ytemp = (y-ylen/2); ytemp < (y+(ylen+1)/2); ytemp++) {
				if (ytemp < 0 || ytemp > ysize) return false;
				for (int xtemp = x; xtemp > (x-xlen); xtemp--) {
					if (xtemp < 0 || xtemp > xsize) return false;
					if (getCell(xtemp, ytemp) != tileUnused) return false;
				}
			}

			for (int ytemp = (y-ylen/2); ytemp < (y+(ylen+1)/2); ytemp++) {
				for (int xtemp = x; xtemp > (x-xlen); xtemp--) {
					if (xtemp == x) setCell(xtemp, ytemp, wall);
					else if (xtemp == (x-xlen+1)) setCell(xtemp, ytemp, wall);
					else if (ytemp == (y-ylen/2)) setCell(xtemp, ytemp, wall);
					else if (ytemp == (y+(ylen-1)/2)) setCell(xtemp, ytemp, wall);
					else setCell(xtemp, ytemp, floor);
				}
			}

			break;
		}

		//yay, all done
		return true;
	}

	String showDungeon() {
		/*******************************************************************************/
		//used to print the map on the screen
		String dungeonMap = "";
		for (int y = 0; y < ysize; y++) {
			for (int x = 0; x < xsize; x++) {
				switch(getCell(x, y)) {
				case tileUnused: dungeonMap += " "; break;
				case tileDirtWall: dungeonMap += "+"; break;
				case tileDirtFloor: dungeonMap += "."; break;
				case tileStoneWall: dungeonMap += "O"; break;
				case tileCorridor: dungeonMap += "#"; break;
				case tileDoor: dungeonMap += "D"; break;
				case tileChest: dungeonMap += "*"; break;
				}
			}
			dungeonMap += "\n";
		}
		return dungeonMap;
	}

	public int[][] get2DMap() {
		return Array2.translateInt1ToInt2(dungeon_map, xsize);
	}
}

