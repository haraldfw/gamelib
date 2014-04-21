package com.smokebox.lib.utils;

import java.util.ArrayList;

import com.smokebox.lib.syne.shape.Circle;
import com.smokebox.lib.syne.shape.Line;
import com.smokebox.lib.syne.shape.Ray;
import com.smokebox.lib.syne.shape.Rectangle;

public class Intersect {
	/**
	 * A class containing various static methods for checking collisions between objects,
	 * namely rectangles, bounding-circles, circles, lines and rays 
	 * 
	 * @author 	Wilhelmsen, Harald Floor
	 */

	/**
	 * Returns true if two given rectangles are intersecting.
	 * @param a	Rectangle #1
	 * @param b	Rectangle #2
	 * @return	Boolean for the intersection
	 */
	public static boolean rectRect(Rectangle a, Rectangle b) {
		return (a.pos.x < b.pos.x + b.width) && (a.pos.x + a.width > b.pos.x) && (a.pos.y < b.pos.y + b.height) && (a.pos.y + a.height > b.pos.y);
	}
	
	/**
	 * Return true if given line intersects with given rectangle.
	 * @param l	Line
	 * @param r	Rectangle
	 * @return	Boolean for the intersection.
	 */
	public static boolean lineRect(Line l, Rectangle r) {
		
		if(	(l.x < r.pos.x && l.x2 < r.pos.x) || (l.x > r.pos.x + r.width && l.x2 > r.pos.x + r.width) ||
			(l.y < r.pos.y && l.y2 < r.pos.y) || (l.y > r.pos.y + r.height && l.y2 > r.pos.y + r.height)) {
			return false;
		}
		
		return rayRect(l.asRay(), r);
	}
	
	/**
	 * Return true if given ray intersects with given rectangle.
	 * @param ray	Ray
	 * @param r		Rectangle
	 * @return		Boolean for intersection.r
	 */
	public static boolean rayRect(Ray ray, Rectangle r) {
		
		boolean rTopLeftIsBelow = (r.pos.y + r.height < ray.y + ray.slope*(r.pos.x - ray.x));
		boolean rBotLeftBelow = (r.pos.y < ray.y + ray.slope*(r.pos.x - ray.x));
		boolean rTopRightBelow = (r.pos.y + r.height < ray.y + ray.slope*(r.pos.x + r.width - ray.x));
		boolean rBotRightBelow = (r.pos.y < ray.y + ray.slope*(r.pos.x + r.width - ray.x));
		
		return !(	rTopLeftIsBelow == rTopRightBelow
				&& 	rTopLeftIsBelow == rBotLeftBelow
				&& 	rTopLeftIsBelow == rBotRightBelow);
	}
	
	/**
	 * Return true if two given circles are intersecting.
	 * @param c1	Circle #1
	 * @param c2	Circle #2
	 * @return		Boolean for intersection
	 */
	public static boolean circleCircle(Circle c1, Circle c2) {
		return (c1.radius + c2.radius)*(c1.radius + c2.radius) > (c1.x - c2.x)*(c1.x - c2.x) + (c1.y - c2.y)*(c1.y - c2.y);
	}
	
	/**
	 * Returns true if two given objects' bounding-circles are intersecting.
	 * @param x1	The centerX of the first object
	 * @param y1	The centerY of the first object
	 * @param r1	The radius of the first object
	 * @param x2	The centerX of the second object
	 * @param y2	The centerY of the second object
	 * @param r2	The radius of the second object
	 * @return		Boolean for intersection
	 */
	public static boolean boundingCollision(float x1, float y1, float r1, float x2, float y2, float r2) {
		return (r1 + r2)*(r1 + r2) > (x1 - x2)*(x1 - x2) + (y1 - y2)*(y1 - y2);
	}
	
	public static boolean rayLine(Ray r, Line l) {
		Line l2 = new Line(r.x, r.y, r.x + 1 + l.x2 - l.x, r.y + (l.x2 - l.x)*r.slope);
		return lineLine(l, l2);
	}
	
	private static boolean ccw(Vector2 a, Vector2 b, Vector2 c) {
		return (c.y-a.y)*(b.x-a.x) >= (b.y-a.y)*(c.x-a.x);
	}
	
	public static boolean horLineVerLine(Line hor, Line ver) {
		if(ver.y2 < ver.y) {
			float t = ver.y;
			ver.y = ver.y2;
			ver.y2 = t;
		}
		return hor.y >= ver.y && hor.y <= ver.y2 && hor.x <= ver.x && hor.x2 >= ver.x;
	}
	
	public static boolean lineLine(Line l, Line l2) {
		l.correctDirection();
		l2.correctDirection();
		// If lines have end-points together
		
		if(l.isPerfectHorizontal() && l2.isPerfectHorizontal() && l.y == l2.y) 
			return inRng(l.x, l2.x, l2.x2) || inRng(l2.x, l.x, l.x2);
		if(l.isPerfectVertical() && l2.isPerfectVertical() && l.x == l2.x) {
			return inRng(l.y, l2.y, l2.y2) || inRng(l2.y, l.y, l.y2);
		}
		
		if(		((l.x 	== l2.x 	&& l.y 	== l2.y) ||
				(l.x2 	== l2.x 	&& l.y2 == l2.y) ||
				(l.x 	== l2.x2 	&& l.y == l2.y2) ||
				(l.x2 	== l2.x2 	&& l.y2 == l2.y2))) return false;
		
		Vector2 a = l.startAsVector();
		Vector2 b = l.endAsVector();
		Vector2 c = l2.startAsVector();
		Vector2 d = l2.endAsVector();
		return (ccw(a,c,d) != ccw(b,c,d)) && (ccw(a,b,c) != ccw(a,b,d));
	}
	
	/**
	 * Returns a boolean for the collision between a point and a line.
	 * Will return false if point is on an end-coordinate
	 * TODO test and ensure potency
	 * @param p	The point as a vector2
	 * @param l	The line
	 * @return	boolean for intersection
	 */
	public static boolean pointLine(Vector2 p, Line l) {
		l.correctDirection();
		return p.y == l.y + p.x*l.getSlope() && onRng(p.x, l.x, l.x2);
		
	}
	
	public static boolean isParallel(Line l, Line l2) {
		return Math.abs((l.y2 - l.y)/(l.x2 - l.x)) == Math.abs((l2.y2 - l2.y)/(l2.x2 - l2.x)); 
	}
	
	public static boolean pointAxisAlignedLine(float x, float y, Line l) {
		/* readable
		if(l.y == l.y2) return p.y == l.y && inRng(p.x, l.x, l.x2);
		if(l.x == l.x2) return p.x == l.x && inRng(p.y, l.y, l.y2);
		return false;
		Non-readable*/
		return (l.y == l.y2 && y == l.y && inRng(x, l.x, l.x2)) || 
				(l.x == l.x2 && x == l.x && inRng(y, l.y, l.y2));
	}
	
	public static boolean pointInsidePolyedge(float x, float y, ArrayList<Line> edges, float mostLeftX) {
		int inters = 0;
		
		Line line = new Line(x, y, mostLeftX - 10, y);
		
		for(Line l : edges) {
			if(Intersect.pointAxisAlignedLine(x, y, l)) return false;
			if(l.isPerfectVertical() && Intersect.lineLine(line, l)) inters++;
		}
		
		return inters%2 != 0;
	}
	
	public static boolean rayVerticalLine(Ray r, Line l) {
		float rY = r.y + r.slope*l.x; 
		return rY < l.y2 && rY > l.y;
	}
	
	public static boolean onRng(float f, float min, float max) {
		return f >= min && f <= max;
	}
	
	private static boolean inRng(float f, float min, float max) {
		return f > min && f < max;
	}
}