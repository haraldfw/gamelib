/**
 * 
 */
package com.smokebox.lib.syne;

import java.util.ArrayList;

import com.smokebox.lib.syne.shape.Circle;
import com.smokebox.lib.syne.shape.Rectangle;

/**
 * @author Harald Floor Wilhelmsen
 *
 */
public class Engine {
	
	ArrayList<Circle> circles;
	ArrayList<Rectangle> rects;

	public Engine() {
		circles = new ArrayList<>();
		rects = new ArrayList<>();
	}
	
	public void update(float delta) {
		
	}
	
	public void addCircle(Circle c) {
		circles.add(c);
	}
}
