/**
 *
 */
package com.smokebox.lib.utils.pathfinding;

import com.smokebox.lib.utils.MathUtils;

/**
 * @author Harald Floor Wilhelmsen
 */
public class Euclidian implements Heuristic {

  private float goalX;
  private float goalY;

  public Euclidian(float goalX, float goalY) {
    this.goalX = goalX;
    this.goalY = goalY;
  }

  public float estimate(StarNode from) {
    return MathUtils.vectorLength(goalX - from.x, goalY - from.y);
  }
}
