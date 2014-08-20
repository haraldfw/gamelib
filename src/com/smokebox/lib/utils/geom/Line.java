package com.smokebox.lib.utils.geom;

import com.smokebox.lib.utils.Vector2;

/**
 * @author Harald Floor Wilhelmsen
 *
 */
public class Line {

	public float x;
	public float y;
	public float x2;
	public float y2;
	
	/**
	 * Constructs a line consisting of the start and end coordinates given
	 * @param x		The start x-coordinate
	 * @param y		The start y-coordinate
	 * @param x2	The end x-coordinate
	 * @param y2	The end y-coordinate
	 */
	public Line(float x, float y, float x2, float y2) {
		this.x = x;
		this.y = y;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	public Line(Vector2 start, Vector2 end) {
		this.x = start.x;
		this.y = start.y;
		this.x2 = end.x;
		this.y2 = end.y;
	}
	
	/**
	 * Constructs a line with all coordinates in 0,0
	 */
	public Line() {
		this.x = 0;
		this.y = 0;
		this.x2 = 0;
		this.y2 = 0;
	}
	
	/**
	 * Prints the lines variables in a read-friendly format
	 */
	public void Print() {
		System.out.print("Line from:\t" + x + "," + y + "\tto:\t" + x + "," + y + "\n");
	}
	
	/**
	 * Returns the slope of the line
	 * @return	The slope as a float
	 */
	public float getSlope() {
		if(y == y2 && x == x2) {
			return 0;
		} else {
			return (y2 - y)/(x2 - x);
		}
	}
	
	/**
	 * Set the coordinates of this line
	 * @param x		Start-x to set to
	 * @param y		Start-y to set to
	 * @param x2	End-x to set to
	 * @param y2	End-y to set to
	 */
	public void setPosition(float x, float y, float x2, float y2) {
		this.x = x;
		this.y = y;
		this.x2 = x2;
		this.y2 = y2;
	}
	
	/**
	 * Set only the start of this line
	 * @param x	The start-x to set to
	 * @param y	The start-y to set to
	 */
	public void setStart(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setStart(Vector2 v) {
		this.x = v.x;
		this.y = v.y;
	}
	
	public void setEnd(float x, float y) {
		this.x2 = x;
		this.y2 = y;
	}
	
	public float angle() {
		return (float)Math.atan2(y2 - y, x2 - x) + 3.1415f;
	}
	
	public float getMag2() {
		return new Vector2(x2 - x, y2 - y).getMag2();
	}
	
	public Line correctDirection() {
		if(x2 < x) { // If line climbs to the left, flip components
			float t = x;
			x = x2;
			x2 = t;
			t = y;
			y = y2;
			y2 = t;
		}
		
		if(isPerfectVertical() && y > y2) {
			float t = y;
			y = y2;
			y2 = t;
		}
		return this;
	}
	
	public Ray asRay() {
		return new Ray(x, y, (y2 - y)/(x2 - x), x2 > x);
	}
	
	public Vector2 startAsVector() {
		return new Vector2(x, y);
	}
	
	public Vector2 endAsVector() {
		return new Vector2(x2, y2);
	}
	
	public boolean isPerfectHorizontal() {
		return y == y2;
	}
	
	public boolean isPerfectVertical() {
		return x == x2;
	}
	
	public Line flipRight() {
		if(x2 < x) {
			float f = x2;
			x2 = x;
			x = f;
		}
		return this;
	}
	
	public void addToPosition(float x, float y) {
		this.x += x;
		this.y += y;
		x2 += x;
		y2 += y;
	}
	
	public boolean equalTo(Line l2) {
		return x == l2.x && x2 == l2.x2 && y == l2.y && y2 == l2.y2;
	}
	
	public Vector2 getMinimumDistance(float cx, float cy) {
		float px = x2 - x;
	    float py = y2 - y;

	    float s = px*px + py*py;

	    float u =  ((cx - x) * px + (cy - y) * py)/s;

	    if(u > 1) u = 1;
	    else if(u < 0) u = 0;

	    float nx = x + u * px;
	    float ny = y + u * py;

	    float dx = nx - cx;
	    float dy = ny - cy;
	    
	    return new Vector2(dx, dy);
	}
	
	public Vector2 getMinimumDistance(Vector2 c) {
		return getMinimumDistance(c.x, c.y);
	}
	
	/**
	 * Returns the line as if it was a vector from x1,y1 to x2,y2
	 * @return Vector2 representation
	 */
	public Vector2 getAsVector2() {
		return new Vector2(x2 - x, y2 - y);
	}
}
