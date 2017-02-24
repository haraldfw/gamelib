package com.wilhelmsen.gamelib.utils.pathfinding;

import com.wilhelmsen.gamelib.utils.Vector2;

/**
 * Created by Harald Wilhelmsen on 11/14/2014.
 */
public class Pythagorean implements Heuristic {

  Vector2 goal;

  public Pythagorean(Vector2 goal) {
    this.goal = goal;
  }

  @Override
  public float estimate(StarNode from) {
    float x = goal.x - from.x;
    float y = goal.y - from.y;
    return (float) Math.sqrt(x * x + y * y);
  }
}
