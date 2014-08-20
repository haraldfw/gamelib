/**
 * 
 */
package com.smokebox.lib.utils.geom;

import com.smokebox.lib.utils.Vector2;
import com.smokebox.lib.utils.geom.Line;

/**
 * @author Harald Floor Wilhelmsen
 *
 */
public class HalfSpace {

	public Line line;
	// Perpendicular vector pointing "into" the open-side
	public Vector2 inside;
	
	public HalfSpace(Line l, Vector2 inside) {
		line = l;
		this.inside = inside;
	}
	
	public Vector2 getAsVector2() {
		return new Vector2();
	}
}
