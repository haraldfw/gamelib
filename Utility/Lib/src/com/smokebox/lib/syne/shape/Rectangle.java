package com.smokebox.lib.syne.shape;

import com.smokebox.lib.utils.Vector2;


public class Rectangle {

	public Vector2 pos;
	public float width;
	public float height;
	public float boundingRadius;
	
	public Rectangle(float x, float y, float width, float height) {
		this.pos = new Vector2(x, y);
		this.width = width;
		this.height = height;
		
		boundingRadius = (float) Math.sqrt((width/2)*(width/2) + (height/2)*(height/2));
	}
	
	public Rectangle() {
		this.pos = new Vector2();
		this.width = 0;
		this.height = 0;
		
		boundingRadius = 0;
	}
	
	public float area() {
		return width*height;
	}

	public Vector2 botLeftPos() {
		return pos;
	}
	
	public Vector2 middlePos() {
		return new Vector2(pos.x + width/2, pos.y + height/2);
	}
	
	public void addToPosition(Vector2 v) {
		pos.add(v);
	}
	
	public void print() {
		System.out.println("-- Rectangle --");
		System.out.println("Position:   " + pos.x + ", " + pos.y);
		System.out.println("Dimensions: " + width + ", " + height);
	}
	
	public Rectangle getScaled(float f) {
		return new Rectangle(pos.x*f, pos.y*f, width*f, height*f);
	}
	
	public Rectangle round() {
		pos.x = Math.round(pos.x);
		pos.y = Math.round(pos.y);
		width = Math.round(width);
		height = Math.round(height);
		
		return this;
	}
	
	public Rectangle floor() {
		pos.x = (float) Math.floor(pos.x);
		pos.y = (float) Math.floor(pos.y);
		width = (float) Math.floor(width);
		width = (float) Math.floor(height);
		
		return this;
	}
	
	public Rectangle getTransformedCopy(float x, float y, float width, float height) {
		return new Rectangle(this.pos.x + x, this.pos.y + y, this.width + width, this.height + height);
	}
}
