/**
 *
 */
package com.wilhelmsen.gamelib.utils.geom;

import com.wilhelmsen.gamelib.utils.Vector2;

/**
 * @author Harald Floor Wilhelmsen
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

        for (Vector2 vertice : vertices) {
            if (vertice.x < sx) {
                sx = vertice.x;
            }
            if (vertice.y < sy) {
                sy = vertice.y;
            }
        }

        this.origin = new Vector2(sx, sy);
    }

    public Polygon rotate(float angle) {
        for (Vector2 vertice : vertices) {
            vertice.rotate(origin, angle);
        }
        this.angle += angle;
        return this;
    }

    public float[] getVerticesAsFloatArray(float xStart, float yStart) {
        float[] ver = new float[vertices.length * 2];
        int j = 0;
        for (Vector2 vertice : vertices) {
            ver[j] = vertice.x + xStart;
            j++;
            ver[j] = vertice.y + yStart;
            j++;
        }
        return ver;
    }

    public Polygon setRotation(float angleInRadians) {
        for (Vector2 vertice : vertices) {
            vertice.rotate(origin, angleInRadians - this.angle);
        }
        this.angle = angleInRadians;
        angleInRadians += angleOffset; // wtf
        return this;
    }

    public void setOrigin(float x, float y) {
        origin.set(x, y);
    }

    public void setAngleOffset(float radians) {
        angleOffset = radians;
    }

    public void setOriginKeepRelativeDistance(float x, float y) {
        for (Vector2 vertice : vertices) {
            vertice.x += -origin.x + x;
            vertice.y += -origin.y + y;
        }
        origin.set(x, y);
    }

    public void posAdd(float x, float y) {
        for (Vector2 vertice : vertices) {
            vertice.add(x, y);
            origin.add(x, y);
        }
    }
}
