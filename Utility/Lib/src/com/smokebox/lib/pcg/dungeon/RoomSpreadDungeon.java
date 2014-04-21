/**
 * 
 */
package com.smokebox.lib.pcg.dungeon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.smokebox.lib.utils.Vector2;
import com.smokebox.lib.syne.shape.Line;
import com.smokebox.lib.syne.shape.Rectangle;
import com.smokebox.lib.utils.Intersect;

/**
 * @author Harald Floor Wilhelmsen
 *
 */
public class RoomSpreadDungeon {

	public static RoomsWithTree RoomSpreadFloor(int cells, Random rand) {
		
		// Time-keeping (debugging)
		float timeStarted = System.nanoTime();
		float timeOnRoomsAndSeperation;
		float timeOnTree;
		float timeOnCorridorsAndCollisions;
		float totalTimeUsed;
		
		Random random = (rand == null) ? new Random() : rand;

		// A scalar for room-dimensions
		int roomDimScalar = 5;
		// The distribution of the rooms on spawn.
		// A smaller distribution generally gives a poorer performance -
		// due to the separation-process taking more time
		int distribution = (int)(((float)cells*(float)roomDimScalar)/100) + 1;
		// Maximum attempts to separate rooms
		int maxIterations = cells;
		// The accepted ratio for the rooms' dimensions. 
		// A higher number gives longer rooms
		float maxRoomRatio = 2f;
		
		ArrayList<Cell> rooms = new ArrayList<>();
		
		Rectangle rect = new Rectangle();
		
		System.out.println("Generating " + cells +  " rooms in a cluster.");
		for(int i = 0; i < cells; i++) {
			
			boolean roomAccepted = false;
			
			while(!roomAccepted) {
				rect = new Rectangle(
						random.nextInt(distribution) - distribution/2, 
						random.nextInt(distribution) - distribution/2, 
						(float)Math.round(Math.abs(random.nextGaussian()*roomDimScalar)),
						(float)Math.round(Math.abs(random.nextGaussian()*roomDimScalar))
				);
				
				float ratio = rect.height/rect.width;
				if(ratio < maxRoomRatio && ratio > 1/maxRoomRatio
						&& Intersect.onRng(rect.width, 3, 50) && Intersect.onRng(rect.height, 3, 50))
					roomAccepted = true;
			}
			
			rooms.add(new Cell(rect));
		}
		
		
		System.out.println("Generating rooms finished. Time used so far: " 
				+ ((System.nanoTime() - timeStarted)/Math.pow(10, 9)) + " seconds");
		System.out.println("Seperating the rooms...");
		boolean collisionFound = true;
		int iterations = 0;
		int collisionChecks = 0;
		int collisionResolutions = 0;
		
		// Separate rooms
		while(collisionFound && iterations < maxIterations) {

			collisionFound = false;
			
			for(int i = 0; i < rooms.size(); i++) {
				Cell cell = rooms.get(i); // Define a cell to compare with the rest of the array

				for(int j = i + 1; j < rooms.size(); j++) {
					Cell cell2 = rooms.get(j); // Define a cell to compare to the previously chosen one

					collisionChecks++;
					
					if(Intersect.rectRect(cell.rect, cell2.rect)) { // If the two cells intersect
						collisionResolutions++;
						
						collisionFound = true; // Set collisionFound to true to ensure the loop restarts
						
						Vector2 newForce = new Vector2(); // New force-vector
						
						newForce.x = cell.rect.middlePos().x - cell2.rect.middlePos().x ;
						newForce.y = cell.rect.middlePos().y - cell2.rect.middlePos().y;
						
						cell.addForce(newForce); // Add force-vector to cell#1
						cell2.addForce(newForce.flip()); // Add flipped force-vector to cell#2
					}
				}
			}
			iterations++;
			for(Cell c : rooms) {
				c.forceAccumulation.nor();
				c.applyForces();
				c.clearForceAccumulation();
			}
			for(Cell c : rooms) c.rect.round();
			System.out.println("Seperating, iterations: " + iterations + "/" + maxIterations);
		}
		timeOnRoomsAndSeperation = System.nanoTime() - timeStarted;
		System.out.println("Total iterations: " + iterations);
		System.out.println("CollisionChecks:\t" + collisionChecks);
		System.out.println("CollisionResolutions:\t" + collisionResolutions);
		
		System.out.println("Finished seperating rooms. Time used so far: " 
				+ ((System.nanoTime() - timeStarted)/Math.pow(10, 9)) + " seconds");
		System.out.println("Creating the tree...");
		
		// The outer most coordinates of rooms
		int[] bounds = findBounds(rooms);
		
		// Shift rooms so lowest possible coordinates start at (0,0)
		// So if lower/left bounds are negative, rooms are shifted up/right
		for(int i = 0; i < rooms.size(); i++) {
			Cell c = rooms.get(i);
			c.rect.pos.x -= (float)bounds[0];
			c.rect.pos.y -= (float)bounds[1];
		}
		
		// Sort rooms by area, bigger first
		Collections.sort(rooms, new Comparator<Cell>(){
			@Override
			public int compare(Cell c, Cell c2) {
				return (int) (c2.rect.area() - c.rect.area());
			}
		});
		ArrayList<Cell> biggerRooms = new ArrayList<>();
		
		for(int i = 0; i < rooms.size()/10; i++) {
			biggerRooms.add(rooms.get(i));
		}
		
		// Tree
		Tree tree = new Tree(biggerRooms);
		timeOnTree = System.nanoTime() - timeStarted - timeOnRoomsAndSeperation;
		
		// Edges
		ArrayList<Line> edges = new ArrayList<>(tree.edges);
		
		// Corridors
		int corridorWidth = 3;
		ArrayList<Rectangle> corridors = new ArrayList<>();
		for(Line e : edges) {
			Rectangle horCorr = new Rectangle();
			
			horCorr.pos.x = (e.x < e.x2 ? e.x : e.x2) - 1;
			horCorr.width = Math.abs(e.x2 - e.x) + 2;
			
			horCorr.height = corridorWidth;
			horCorr.pos.y = (e.y2 - (corridorWidth - 1)/2);
			
			Rectangle verCorr = new Rectangle();
			
			verCorr.pos.y = (e.y < e.y2 ? e.y : e.y2) - 1;
			verCorr.height = Math.abs(e.y2 - e.y);
			
			verCorr.width = corridorWidth;
			verCorr.pos.x = (e.x - (corridorWidth - 1)/2);
			
			corridors.add(horCorr);
			corridors.add(verCorr);
		}
		
		// Generate int[][] from map to fill empty spaces
		int[][] map = new int[bounds[2] - bounds[0]][bounds[3] - bounds[1]];
		
		for(Cell c : rooms) burnRoom(map, c.rect);
		
		// Fill empty space with 1x1 rooms
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				if(map[i][j] == 0) {
					rooms.add(new Cell(new Rectangle(i, j, 1, 1)));
				}
			}
		}
		
		// Filtering through and only keeping rooms intersecting corridors
		ArrayList<Cell> finalRooms = new ArrayList<>();
		for(Rectangle corr : corridors) {
			for(Cell r : rooms) {
				if(Intersect.rectRect(corr, r.rect)) {
					finalRooms.add(r);
				}
			}
		}
		System.out.println("Done adding corridors.");
		timeOnCorridorsAndCollisions = System.nanoTime() - timeStarted - timeOnRoomsAndSeperation - timeOnTree;
		totalTimeUsed = System.nanoTime() - timeStarted;
		System.out.println("FinalRooms in list: " + finalRooms.size() + ".");
		
		System.out.println("Generating done. " + cells + " rooms placed and positioned. ");
		System.out.println("Total amount of cells is " + finalRooms.size() + " for the whole map.");
		System.out.println("Total time: " + totalTimeUsed/Math.pow(10, 9) + " seconds");
		
		System.out.println("Time on rooms and seperation: " + timeOnRoomsAndSeperation/Math.pow(10, 9) + " seconds. " +
				"\nTime on tree: " + timeOnTree/Math.pow(10, 9) +  " seconds. " +
						"\nTime on Corridors and collisions: " + timeOnCorridorsAndCollisions/Math.pow(10, 9) + " seconds.");
		//map = new int[bounds[2] - bounds[0]][bounds[3] - bounds[1]];
		return new RoomsWithTree(tree, finalRooms, rooms, corridors);
	}
	
	private static int[][] burnRoom(int[][] map, Rectangle r) {
		
		float xStart = r.pos.x;
		float xEnd = r.pos.x + r.width;
		float yStart = r.pos.y;
		float yEnd = r.pos.y + r.height;
		
		for(int i = (int) xStart; i < xEnd; i++) {
			for(int j = (int) yStart; j < yEnd; j++) {
				map[i][j] = 1;
			}
		}
		
		return map;
	}
	
	private static int[] findBounds(List<Cell> rooms) {
		int[] bounds = new int[4];
		
		for(int i = 0; i < rooms.size(); i++) {
			Rectangle rect = rooms.get(i).rect;
			
			float xLeft = rect.pos.x; // left bound 
			if(xLeft < bounds[0]) {
				bounds[0] = (int)Math.floor(xLeft);
			}
			
			float yBot = rect.pos.y; // bottom bound 
			if(yBot < bounds[1]) {
				bounds[1] = (int)Math.floor(yBot);
			}

			float xRight = rect.pos.x + rect.width; // right bound 
			if(xRight > bounds[2]) {
				bounds[2] = (int)Math.ceil(xRight);
			}
			
			float yTop = rect.pos.y + rect.height; // top bound 
			if(yTop > bounds[3]) {
				bounds[3] = (int)Math.ceil(yTop);
			}
		}
		
		return bounds;
	}
	
	public static int[][] asInt2(RoomsWithTree roomTree) {
		int[] bounds = findBounds(roomTree.rooms);
		
		int[][] map = new int[bounds[2] - bounds[0]][bounds[3] - bounds[1]];
		
		for(int i = 0; i < roomTree.rooms.size(); i++) {
			burnRoom(map, roomTree.rooms.get(i).rect);
		}
		
		return map;
	}
}
