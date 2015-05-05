/**
 *
 */
package com.smokebox.lib.utils.pathfinding;

import com.smokebox.lib.utils.Vector2;

/**
 * @author Harald Floor Wilhelmsen
 */
public class Manhattan implements Heuristic {

  Vector2 goal;

  public Manhattan(Vector2 goal) {
    this.goal = goal;
  }

  public float estimate(StarNode from) {
    return Math.abs(goal.x - from.x + goal.y - from.y);
  }
}
