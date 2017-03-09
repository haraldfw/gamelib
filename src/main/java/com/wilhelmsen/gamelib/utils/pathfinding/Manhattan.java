/**
 *
 */
package com.wilhelmsen.gamelib.utils.pathfinding;

import com.wilhelmsen.gamelib.utils.Vector2;

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
