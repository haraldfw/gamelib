/**
 *
 */
package com.wilhelmsen.gamelib.utils.pathfinding;

import com.wilhelmsen.gamelib.utils.Vector2;

/**
 * @author Harald Floor Wilhelmsen
 */
public interface Heuristic {

  Vector2 goal = new Vector2();

  float estimate(StarNode from);
}
