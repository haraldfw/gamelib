package com.smokebox.lib.pcg.dungeon;

import com.smokebox.lib.syne.shape.Rectangle;
import com.smokebox.lib.utils.Vector2;

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
		rect.addToPosition(forceAccumulation.round());
	}
	
	public void addForce(Vector2 force) {
		forceAccumulation.add(force);
	}
	
	public void clearForceAccumulation() {
		forceAccumulation.clear();
	}
}
