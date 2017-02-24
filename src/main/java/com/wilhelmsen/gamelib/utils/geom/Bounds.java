package com.wilhelmsen.gamelib.utils.geom;

/**
 * Created by Harald on 25.5.15.
 */
public class Bounds {

  public int x;
  public int y;
  public int width;
  public int height;

  public Bounds(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public boolean overlaps(Bounds b) {
    return (x < b.x + b.width) && (x + width > b.x) && (y < b.y + b.height) && (
        y + height > b.y);
  }

  public boolean overlapsList(Iterable<Bounds> boundsList) {
    for (Bounds bounds : boundsList) {
      if (overlaps(bounds)) {
        return true;
      }
    }
    return false;
  }
}
