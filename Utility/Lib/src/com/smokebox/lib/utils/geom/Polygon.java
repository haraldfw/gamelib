/**
 * 
 */
package com.smokebox.lib.utils.geom;

import com.smokebox.lib.utils.Vector2;

/**
 * @author Harald Floor Wilhelmsen
 *
 */
public class Polygon {

	public Vector2[] vertices = new Vector2[3];
	
	public float angle = 0;
	public float angleOffset = 0;
	
	public Vector2 origin;
	
	public Polygon(Vector2[] vertices, Vector2 origin, float angleOffset) {
		this.vertices = vertices;
		this.origin = new Vector2(origin);
		this.angleOffset = angleOffset;
		angle += angleOffset;
	}
	
	public Polygon(Vector2[] vertices) {
		this.vertices = vertices;
		
		float sx = vertices[0].x;
		float sy = vertices[0].y;
		
		for(int i = 0; i < vertices.length; i++) {
			if(vertices[i].x < sx) sx = vertices[i].x;
			if(vertices[i].y < sy) sy = vertices[i].y;
		}
		
		this.origin = new Vector2(sx, sy);
	}
	
	public Polygon rotate(float angle) {
		for(int i = 0; i < vertices.length; i++) {
			vertices[i].set(rotatePoint(vertices[i], origin, angle));
		}
		this.angle += angle;
		return this;
	}
	
	public float[] getVerticesAsFloatArray(float xStart, float yStart) {
		float[] ver = new float[vertices.length*2];
		int j = 0;
		for(int i = 0; i < vertices.length; i++) {
			ver[j] = vertices[i].x + xStart;
			j++;
			ver[j] = vertices[i].y + yStart;
			j++;
		}
		return ver;
	}
	
	public Polygon setRotation(float angleInRadians) {
		for(int i = 0; i < vertices.length; i++) {
			vertices[i].set(rotatePoint(vertices[i], origin, angleInRadians - this.angle));
		}
		this.angle = angleInRadians;
		angleInRadians += angleOffset;
		return this;
	}
	
	private Vector2 rotatePoint(Vector2 point, Vector2 origin, float radians) {
		double angle = radians;
		float ox = origin.x;
		float oy = origin.y;
		float x = point.x - ox;
		float y = point.y - oy;
		double sine = Math.sin(angle);
		double cosine = Math.cos(angle);
		return new Vector2(
				(float)(x*cosine - y*sine) + ox,  // New X
				(float)(x*sine + y*cosine) + oy); // New Y
	}
	
	public void setOrigin(float x, float y) {
		origin.set(x, y);
	}
	
	public void setAngleOffset(float radians) {
		angleOffset = radians;
	}
	
	public void setOriginKeepRelativeDistance(float x, float y) {
		for(int i = 0; i < vertices.length; i++) {
			vertices[i].x += -origin.x + x; 
			vertices[i].y += -origin.y + y;
		}
		origin.set(x, y);
	}
	
	public void posAdd(float x, float y) {
		for(int i = 0; i < vertices.length; i++) {
			vertices[i].add(x, y);
			origin.add(x, y);
		}
	}
}
