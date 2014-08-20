package com.smokebox.lib.pcg.dungeon;

import com.smokebox.lib.utils.Vector2;
import com.smokebox.lib.utils.geom.Rectangle;

/**
 * @author Harald Floor Wilhelmsen
 *
 */
public class Cell {

	public Rectangle rect;
	
	public Vector2 forceAccumulation;
	
	public Cell(Rectangle r) {
		rect = r;
		forceAccumulation = new Vector2();
	}
	
	public void applyForces() {
		rect.addToPosition(forceAccumulation);
	}
	
	public void addForce(Vector2 force) {
		forceAccumulation.add(force);
	}
	
	public void clearForceAccumulation() {
		forceAccumulation.clear();
	}
}
