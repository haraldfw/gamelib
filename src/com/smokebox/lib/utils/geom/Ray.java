/**
 *
 */
package com.smokebox.lib.utils.geom;

/**
 * @author Harald Floor Wilhelmsen
 */
public class Ray {

  public float x;
  public float y;
  public float slope;
  public boolean xPositive;

  public Ray(float x, float y, float slope, boolean xPositive) {
    this.x = x;
    this.y = y;
    this.slope = slope;
    this.xPositive = xPositive;
  }

  public float getYAtX(float x) {
    return this.y + x * slope; // May be wrong. Pls verify
  }

  public float getXAtY(float y) {
    return this.x + y / slope; // May be wrong. Pls verify
  }
}
