/**
 * 
 */
package com.smokebox.lib.utils.pathfinding;

import com.smokebox.lib.utils.geom.Line;

/**
 * @author Harald Floor Wilhelmsen
 *
 */
public class Connection {

	public StarNode start;
	public StarNode end;
	public float weight;
	
	public Connection(StarNode start, StarNode end, float weight) {
		this.start = start;
		this.end = end;
		this.weight = weight;
	}
	
	public Line getAsLine() {
		return new Line(start.x, start.y, end.x, end.y);
	}
}
