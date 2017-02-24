/**
 *
 */
package com.wilhelmsen.gamelib.utils.geom;

import com.wilhelmsen.gamelib.utils.Vector2;

/**
 * @author Harald Floor Wilhelmsen
 */
public class Triangle {

  public Vector2[] corners = new Vector2[3];

  public float angle = 0;
  public float angleOffset = 0;

  public Vector2 origin;

  public Triangle(float x1, float y1, float xDiff1, float yDiff1, float xDiff2, float yDiff2,
                  float angleOffset) {
    corners[0] = new Vector2(x1, y1);
    corners[1] = new Vector2(x1 + xDiff1, y1 + yDiff1);
    corners[2] = new Vector2(x1 + xDiff2, y1 + yDiff2);

    origin = new Vector2();
    this.angleOffset = angleOffset;
    angle += angleOffset;
  }

  public Triangle(float originX, float originY,
                  float xDiff1, float yDiff1,
                  float xDiff2, float yDiff2,
                  float xDiff3, float yDiff3,
                  float angleOffset) {
    origin = new Vector2(originX, originY);
    corners[0] = new Vector2(originX + xDiff1, originY + yDiff1);
    corners[1] = new Vector2(originX + xDiff2, originY + yDiff2);
    corners[2] = new Vector2(originX + xDiff3, originY + yDiff3);
    this.angleOffset = angleOffset;
    angle += angleOffset;
  }

  public Triangle rotate(float angle) {
    corners[0].set(rotatePoint(corners[0], origin, angle));
    corners[1].set(rotatePoint(corners[1], origin, angle));
    corners[2].set(rotatePoint(corners[2], origin, angle));
    this.angle += angle;
    return this;
  }

  public Triangle setRotation(float angleInRadians) {
    corners[0].set(rotatePoint(corners[0], origin, angleInRadians - this.angle));
    corners[1].set(rotatePoint(corners[1], origin, angleInRadians - this.angle));
    corners[2].set(rotatePoint(corners[2], origin, angleInRadians - this.angle));
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
        (float) (x * cosine - y * sine) + ox,  // New X
        (float) (x * sine + y * cosine) + oy); // New Y
  }

  public void setOrigin(float x, float y) {
    origin.set(x, y);
  }

  public void print() {
    System.out.println("-- Rectangle --");
    System.out.println("Corners: ");
    System.out.println("#1: " + corners[0].x + "," + corners[0].y);
    System.out.println("#2: " + corners[1].x + "," + corners[1].y);
    System.out.println("#3: " + corners[2].x + "," + corners[2].y);
  }

  public void setAngleOffset(float radians) {
    angleOffset = radians;
  }

  public void setFirstCornerPos(float x, float y) {
    corners[0].x = x;
    corners[0].y = y;
  }

  public void setOriginKeepRelativeDistance(float x, float y) {
    corners[0].x += -origin.x + x;
    corners[0].y += -origin.y + y;
    corners[1].x += -origin.x + x;
    corners[1].y += -origin.y + y;
    corners[2].x += -origin.x + x;
    corners[2].y += -origin.y + y;
    origin.set(x, y);
  }

  public Triangle copyOf() {
    return new Triangle(origin.x, origin.y, corners[0].x - origin.x, corners[0].y - origin.y,
                        corners[1].x - origin.x, corners[1].y - origin.y, corners[2].x - origin.x,
                        corners[2].y - origin.y, angleOffset);
  }
}
