/**
 * 
 */
package com.smokebox.lib.utils;

/**
 * @author Harald Floor Wilhelmsen
 *
 */
public class Vector2 {

	public float x;
	public float y;
	
	/**
	 * Creates and empty 2D-vector.
	 */
	public Vector2() {
		x = 0;
		y = 0;
	}
	
	/**
	 * Creates a copy of the given vector.
	 * @param v	The vector to copy
	 */
	public Vector2(Vector2 v) {
		x = v.x;
		y = v.y;
	}
	
	/**
	 * Creates a vector from the two given components 
	 * @param x	The x-component
	 * @param y The y-component
	 */
	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;	
	}
	
	/**
	 * Sets this vector equal to the one given
	 * @param v	The vector to mimic
	 */
	public Vector2 set(Vector2 v) {
		this.x = v.x;
		this.y = v.y;
		return this;
	}
	
	/**
	 * Sets this vector to the two given component
	 * @param x	The x-component
	 * @param y	The y-component
	 */
	public Vector2 set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	/**
	 * Returns the magnitude of this vector
	 * @return	float for the magnitude
	 */
	public float getMag() {
		return (float) Math.sqrt(x*x + y*y);
	}
	
	/**
	 * Returns the squared magnitude of the vector.
	 * Use this to spare resources if comparing the length of two vectors.
	 * @return	float for magnitude^2
	 */
	public float getMag2() {
		return (float) (x*x + y*y);
	}
	
	/**
	 * Returns the scalar product of this vector and the given
	 * @param v
	 * @return
	 */
	public float getScalarProduct(Vector2 v) {
		return x*v.x + y*v.y;
	}
	
	/**
	 * Normalizes this vector
	 * @return	This vector for chaining
	 */
	public Vector2 nor() {
		float mag = getMag();
		if(mag != 0) {
			x /= mag;
			y /= mag;
		}
		return this;
	}
	
	/**
	 * Returns a normalized copy of this vector
	 * @return A normalized copy of this vector for chaining
	 */
	public Vector2 getNor() {
		return new Vector2(this).nor();
	}
	
	/**
	 * Adds the given vector to this vector
	 * @param v	The vector to add by
	 * @return	This vector for chaining
	 */
	public Vector2 add(Vector2 v) {
		x += v.x;
		y += v.y;
		return this;
	}
	
	/**
	 * Adds the given x and y to this vector
	 * @param x
	 * @param y
	 * @return This vector for chaining
	 */
	public Vector2 add(float x, float y) {
		this.x += x;
		this.y += y;
		return this;
	}
	
	/**
	 * Subtracts the given vector from this vector
	 * @param v	The vector to subtract by
	 * @return	This vector for chaining
	 */
	public Vector2 sub(Vector2 v) {
		x -= v.x;
		y -= v.y;
		return this;
	}
	
	/**
	 * Subtracts the given x and y from this vector
	 * @param x	The x to subtract by
	 * @param y	The y to subtract by
	 * @return	This vector for chaining
	 */
	public Vector2 sub(float x, float y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	/**
	 * Scales this vector by given scalar
	 * @param scalar	Float to scale by
	 * @return	This vector for chaining
	 */
	public Vector2 scl(float scalar) {
		x *= scalar;
		y *= scalar;
		return this;
	}
	
	/**
	 * Flips both the x- and y-components of this vector
	 * @return	This vector for chaining
	 */
	public Vector2 flip() {
		x = -x;
		y = -y;
		return this;
	}

	/**
	 * Flips the x-component of this vector
	 * @return 	This vector for chaining
	 */
	public Vector2 flipX() {
		x = -x;
		return this;
	}
	
	/**
	 * Flips the y-component of this vector
	 * @return 	This vector for chaining
	 */
	public Vector2 flipY() {
		y = -y;
		return this;
	}
	
	/**
	 * Adds a scaled vector to this vector
	 * @param v		The vector to add
	 * @param scl	The scale to scale the add-vector with
	 * @return		This vector for chaining
	 */
	public Vector2 addScaledVector(Vector2 v, float scl) {
		x += v.x*scl;
		y += v.y*scl;
		return this;
	}
	
	/**
	 * Rounds the components with java's Math.round()
	 * @return This vector for chaining
	 */
	public Vector2 round() {
		x = Math.round(x);
		y = Math.round(y);
		return this;
	}
	
	/**
	 * Sets vector to random values.
	 * @param xNeg	Can x be negative
	 * @param xPos	Can x be positive
	 * @param yNeg	Can y be negative
	 * @param yPos	Can y be positive
	 * @return	This vector for chaining
	 */
	public Vector2 setRandom(boolean xNeg, boolean xPos, boolean yNeg, boolean yPos) {
		if(!xNeg && !xPos && !yNeg && !yPos) return this;
		
		x = (float) (Math.random()*(1 + MathUtils.BoolToInt(xPos)) - 1*MathUtils.BoolToInt(xNeg));
		y = (float) (Math.random()*(1 + MathUtils.BoolToInt(yPos)) - 1*MathUtils.BoolToInt(yNeg));
		
		return this;
	}
	
	/**
	 * Sets the components to random values. X and y can be both positive and negative
	 * @return This vector for chaining
	 */
	public Vector2 setRandom() {
		this.x = (float)Math.random()*2 - 1;
		this.y = (float)Math.random()*2 - 1;
		
		return this;
	}

	/**
	 * Returns the angle of this vector in radians
	 * @return The angle of this vector in radians
	 */
	public float getAngleAsRadians() {
		if(y == 0) return (float) (x >= 0 ? 0 : Math.PI);
		if(x == 0) return (float) (y >= 0 ? Math.PI/2: (0.75f)*Math.PI);
		return (float)Math.atan2(y, x);
	}
	
	/**
	 * Returns the angle between this vector and the one given
	 * @param v	The vector to compare angle with
	 * @return	The angle between this vector and the one given
	 */
	public float getAngleTo(Vector2 v) {
		return (float) (Math.atan2(x*v.y - y*v.x, x*v.x + y*v.y));
	}
	
	/**
	 * Returns the dot-product of this vector and the given vector
	 * @param v	Vector to get dot-product with
	 * @return	The dot-product of this vector and the given vector
	 */
	public float getDotProduct(Vector2 v) {
		return x*v.x + y*v.y;
	}
	
	/**
	 * Sets this vectors components to zero
	 * @return	This vector for chaining
	 */
	public Vector2 clear() {
		x = 0;
		y = 0;
		return this;
	}
	
	/**
	 * Returns the magnitude of the difference between the vectors
	 * @param v	Vector to get difference to
	 * @return	A float for magnitude of the difference
	 */
	public float getDifferenceTo(Vector2 v) {
		float xD = x - v.x;
		float yD = y - v.y;
		return (float)Math.sqrt(xD*xD + yD*yD);
	}
	
	/**
	 * Return the true distance to the given coordinates
	 * @param x	The destination-x
	 * @param y	The destination-y
	 * @return	The true difference between this vector and two coordinates given
	 */
	
	public float getDifferenceTo(float x, float y) {
		float xD = this.x - x;
		float yD = this.y - y;
		return (float)Math.sqrt(xD*xD + yD*yD);
	}
	
	/**
	 * Return the squared distance to the given coordinates
	 * @param x	The destination-x
	 * @param y	The destination-y
	 * @return	The squared difference between this vector and two coordinates given
	 */
	public float getDifferenceTo2(float x, float y) {
		float xD = this.x - x;
		float yD = this.y - y;
		return xD*xD + yD*yD;
	}
	
	/**
	 * Returns true of this vector is zero
	 * @return	True of the this vector is zero
	 */
	public boolean isZero() {
		return x == 0 && y == 0;
	}
	
	/**
	 * Raises x- and y-component to the given power
	 * @param power	The power to raise by
	 * @return	This vector for chaining
	 */
	public Vector2 raiseToPower(float power) {
		x = (float)Math.pow(x, power);
		y = (float)Math.pow(y, power);
		return this;
	}
	
	/**
	 * Sets the x-component of this vector to the given value
	 * @param x	The value to set x to
	 * @return This vector for chaining
	 */
	public Vector2 setX(float x) {
		this.x = x;
		return this;
	}
	
	/**
	 * Sets the y-component of this vector to the given value
	 * @param y The value to set y to
	 * @return This vector for chaining
	 */
	public Vector2 setY(float y) {
		this.y = y;
		return this;
	}
	
	/**
	 * Truncates(limits) this vector to the given length
	 * @param max	The maximum value
	 * @return	This vector for chaining
	 */
	public Vector2 truncate(float max) {
		if(getMag() > max) nor().scl(max);
		return this;
	}
}
