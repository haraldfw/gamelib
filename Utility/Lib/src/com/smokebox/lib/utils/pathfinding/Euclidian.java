/**
 * 
 */
package com.smokebox.lib.utils.pathfinding;

import com.smokebox.lib.utils.MathUtils;
import com.smokebox.lib.utils.Vector2;

/**
 * @author Harald Floor Wilhelmsen
 *
 */
public class Euclidian implements Heuristic {
	
	private Vector2 goal;

	public Euclidian(Vector2 goal) {
		this.goal = goal;
	}
	
	public float estimate(StarNode from) {
		return MathUtils.vectorLength(goal.x - from.x, goal.y - from.y);
	}
}
