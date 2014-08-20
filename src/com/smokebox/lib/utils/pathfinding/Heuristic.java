/**
 * 
 */
package com.smokebox.lib.utils.pathfinding;

import com.smokebox.lib.utils.Vector2;

/**
 * @author Harald Floor Wilhelmsen
 *
 */
public interface Heuristic {

	Vector2 goal = new Vector2();
	
	public float estimate(StarNode from);
}
