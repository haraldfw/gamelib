/**
 *
 */
package com.smokebox.lib.pcg.dungeon;

import com.smokebox.lib.utils.geom.Rectangle;

import java.util.ArrayList;

/**
 * @author Harald Floor Wilhelmsen
 */
public class RoomsWithTree {

  public MinimumSpanningTree t;
  public ArrayList<Cell> rooms;
  public ArrayList<Cell> roomsOld;
  public ArrayList<Rectangle> corridors;

  // TODO find a better way to pass the map as an object, and delete this class

  public RoomsWithTree(MinimumSpanningTree t, ArrayList<Cell> rooms, ArrayList<Cell> roomsOld,
                       ArrayList<Rectangle> corridors) {
    this.t = t;
    this.rooms = rooms;
    this.roomsOld = roomsOld;
    this.corridors = corridors;
  }
}
