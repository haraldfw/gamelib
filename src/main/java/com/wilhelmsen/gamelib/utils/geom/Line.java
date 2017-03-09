package com.wilhelmsen.gamelib.utils.geom;

import com.wilhelmsen.gamelib.utils.Vector2;

/**
 * @author Harald Floor Wilhelmsen
 */
public class Line {

    public float x;
    public float y;
    public float x2;
    public float y2;

    /**
     * Constructs a line consisting of the start and end coordinates given
     *
     * @param x  The start x-coordinate
     * @param y  The start y-coordinate
     * @param x2 The end x-coordinate
     * @param y2 The end y-coordinate
     */
    public Line(float x, float y, float x2, float y2) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Creates a line between the vectors given
     *
     * @param from Starting vector
     * @param to   Ending vector
     */
    public Line(Vector2 from, Vector2 to) {
        this.x = from.x;
        this.y = from.y;
        this.x2 = to.x;
        this.y2 = to.y;
    }

    /**
     * Constructs a line with all coordinates equal to zero
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
    public String toString() {
        return "Line from:\t" + x + "\t" + y + "\tto:\t" + x + "\t" + y;
    }

    /**
     * Returns the slope of the line
     *
     * @return The slope as a float
     */
    public float getSlope() {
        if (y == y2 && x == x2) {
            return 0;
        } else {
            return (y2 - y) / (x2 - x);
        }
    }

    /**
     * Set the coordinates of this line
     *
     * @param x  Start-x to set to
     * @param y  Start-y to set to
     * @param x2 End-x to set to
     * @param y2 End-y to set to
     */
    public Line set(float x, float y, float x2, float y2) {
        this.x = x;
        this.y = y;
        this.x2 = x2;
        this.y2 = y2;
        return this;
    }

    /**
     * Set only the start of this line
     *
     * @param x The start-x to set to
     * @param y The start-y to set to
     */
    public Line setStart(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Line setStart(Vector2 v) {
        this.x = v.x;
        this.y = v.y;
        return this;
    }

    public Line setEnd(float x, float y) {
        this.x2 = x;
        this.y2 = y;
        return this;
    }

    public float angle() {
        float f = (float) Math.atan2(y2 - y, x2 - x);
        if (f < 0) {
            f += 2f * Math.PI;
        }
        return f;
    }

    public float getMag2() {
        return new Vector2(x2 - x, y2 - y).getMag2();
    }

    /**
     * Ensures and corrects if needed the direction of the line. Ensures the line climbs to the
     * right(x < x2).
     */
    public Line ensureCorrectDirection() {
        if (x > x2) {
            float t = x;
            x = x2;
            x2 = t;
            t = y;
            y = y2;
            y2 = t;
        }
        return this;
    }

    public Ray asRay() {
        return new Ray(x, y, (y2 - y) / (x2 - x), x2 > x);
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
        if (x2 < x) {
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

    public boolean equalTo(Line l) {
        return x == l.x && x2 == l.x2 && y == l.y && y2 == l.y2;
    }

    public Vector2 getMinimumDistance(float cx, float cy) {
        float px = x2 - x;
        float py = y2 - y;

        float s = px * px + py * py;

        float u = ((cx - x) * px + (cy - y) * py) / s;

        if (u > 1) {
            u = 1;
        } else if (u < 0) {
            u = 0;
        }

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
     *
     * @return Vector2 representation
     */
    public Vector2 getAsVector2() {
        return new Vector2(x2 - x, y2 - y);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Line && equalTo((Line) obj);
    }
}
