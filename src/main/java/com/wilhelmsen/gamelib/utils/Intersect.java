package com.wilhelmsen.gamelib.utils;

import com.wilhelmsen.gamelib.pcg.dungeon.Cell;
import com.wilhelmsen.gamelib.utils.geom.Circle;
import com.wilhelmsen.gamelib.utils.geom.HalfSpace;
import com.wilhelmsen.gamelib.utils.geom.Line;
import com.wilhelmsen.gamelib.utils.geom.Ray;
import com.wilhelmsen.gamelib.utils.geom.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class Intersect {
    /**
     * A class containing various static methods for checking collisions between objects,
     * namely rectangles, bounding-circles, circles, lines and rays
     *
     * @author Wilhelmsen, Harald Floor
     */

    /**
     * Returns true if two given rectangles are intersecting.
     *
     * @param a Rectangle #1
     * @param b Rectangle #2
     * @return Boolean for the intersection
     */
    public static boolean intersection(Rectangle a, Rectangle b) {
        return (a.x < b.x + b.width) && (a.x + a.width > b.x) && (a.y < b.y + b.height) && (
                a.y + a.height > b.y);
    }

    public static boolean intersectionRectRect(float x, float y, float width, float height, float x2,
                                               float y2, float width2, float height2) {
        return (x < x2 + width2) && (x + width > x2) && (y < y2 + height2) && (y + height > y2);
    }

    public static Vector2 linePointClosestDistance(float lx, float ly, float lx2, float ly2, float px,
                                                   float py) {
        float dx = lx2 - lx;
        float dy = ly2 - ly;

        float s = dx * dx + dy * dy;

        float u = ((px - lx) * dx + (py - ly) * dy) / s;

        if (u > 1) {
            u = 1;
        } else if (u < 0) {
            u = 0;
        }

        float nx = lx + u * dx;
        float ny = ly + u * dy;

        return new Vector2(nx - px, ny - py);
    }

    public static boolean rectCircle(Circle circle, Rectangle rect) {
        float distx = Math.abs(circle.x - rect.x);
        float disty = Math.abs(circle.y - rect.y);

        if (distx > (rect.width / 2 + circle.radius)) {
            return false;
        }
        if (disty > (rect.height / 2 + circle.radius)) {
            return false;
        }

        if (distx <= (rect.width / 2)) {
            return true;
        }
        if (disty <= (rect.height / 2)) {
            return true;
        }

        float fx = distx - rect.width / 2;
        fx *= fx;
        float fy = disty - rect.height / 2;
        fy *= fy;

        float cornerDistance_sq = fx + fy;

        return cornerDistance_sq <= (circle.radius * circle.radius);
    }

    /**
     * Return true if given line intersects with given rectangle.
     *
     * @param l Line
     * @param r Rectangle
     * @return True if intersection.
     */
    public static boolean intersection(Line l, Rectangle r) {
        return !((l.x < r.x && l.x2 < r.x) || (l.x > r.x + r.width && l.x2 > r.x + r.width) ||
                (l.y < r.y && l.y2 < r.y) || (l.y > r.y + r.height && l.y2 > r.y + r.height)) && intersection(l.asRay(), r);
    }

    /**
     * Return true if given ray intersects with given rectangle.
     *
     * @param ray Ray
     * @param r   Rectangle
     * @return Boolean for intersection.r
     */
    public static boolean intersection(Ray ray, Rectangle r) {
        boolean rTopLeftIsBelow = (r.y + r.height < ray.y + ray.slope * (r.x - ray.x));
        boolean rBotLeftBelow = (r.y < ray.y + ray.slope * (r.x - ray.x));
        boolean rTopRightBelow = (r.y + r.height < ray.y + ray.slope * (r.x + r.width - ray.x));
        boolean rBotRightBelow = (r.y < ray.y + ray.slope * (r.x + r.width - ray.x));

        return !(rTopLeftIsBelow == rTopRightBelow
                && rTopLeftIsBelow == rBotLeftBelow
                && rTopLeftIsBelow == rBotRightBelow);
    }

    /**
     * Return true if two given circles are intersecting.
     *
     * @param c1 Circle #1
     * @param c2 Circle #2
     * @return True if intersection
     */
    public static boolean intersection(Circle c1, Circle c2) {
        return (c1.radius + c2.radius) * (c1.radius + c2.radius)
                > (c1.x - c2.x) * (c1.x - c2.x) + (c1.y - c2.y) * (c1.y - c2.y);
    }

    public static boolean circleCircle(float x1, float y1, float r1, float x2, float y2, float r2) {
        return (r2 + r2) * (r1 + r2) > (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

    /**
     * Returns true if two given objects' bounding-circles are intersecting.
     *
     * @param x1 The centerX of the first object
     * @param y1 The centerY of the first object
     * @param r1 The radius of the first object
     * @param x2 The centerX of the second object
     * @param y2 The centerY of the second object
     * @param r2 The radius of the second object
     * @return Boolean for intersection
     */
    public static boolean boundingCollision(float x1, float y1, float r1, float x2, float y2,
                                            float r2) {
        return (r1 + r2) * (r1 + r2) > (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

    public static boolean rayLine(Ray r, Line l) {
        Line l2 = new Line(r.x, r.y, r.x + 1 + l.x2 - l.x, r.y + (l.x2 - l.x) * r.slope);
        return intersection(l, l2);
    }

    private static boolean ccw(Vector2 a, Vector2 b, Vector2 c) {
        return (c.y - a.y) * (b.x - a.x) >= (b.y - a.y) * (c.x - a.x);
    }

    public static boolean horLineVerLine(Line hor, Line ver) {
        if (ver.y2 < ver.y) {
            float t = ver.y;
            ver.y = ver.y2;
            ver.y2 = t;
        }
        return hor.y >= ver.y && hor.y <= ver.y2 && hor.x <= ver.x && hor.x2 >= ver.x;
    }

    public static boolean intersection(Line l, Line l2) {
        l.ensureCorrectDirection();
        l2.ensureCorrectDirection();
        // If lines have end-points together

        if (l.isPerfectHorizontal() && l2.isPerfectHorizontal() && l.y == l2.y) {
            return inRng(l.x, l2.x, l2.x2) || inRng(l2.x, l.x, l.x2);
        }
        if (l.isPerfectVertical() && l2.isPerfectVertical() && l.x == l2.x) {
            return inRng(l.y, l2.y, l2.y2) || inRng(l2.y, l.y, l.y2);
        }

        if (((l.x == l2.x && l.y == l2.y) ||
                (l.x2 == l2.x && l.y2 == l2.y) ||
                (l.x == l2.x2 && l.y == l2.y2) ||
                (l.x2 == l2.x2 && l.y2 == l2.y2))) {
            return false;
        }

        Vector2 a = l.startAsVector();
        Vector2 b = l.endAsVector();
        Vector2 c = l2.startAsVector();
        Vector2 d = l2.endAsVector();
        return (ccw(a, c, d) != ccw(b, c, d)) && (ccw(a, b, c) != ccw(a, b, d));
    }

    /**
     * Returns a boolean for the collision between a point and a line. Will return false if point is
     * on an end-coordinate TODO test and ensure potency
     *
     * @param p The point as a vector2
     * @param l The line
     * @return boolean for intersection
     */
    public static boolean pointLine(Vector2 p, Line l) {
        l.ensureCorrectDirection();
        return p.y == l.y + p.x * l.getSlope() && onRng(p.x, l.x, l.x2);

    }

    public static boolean isParallel(Line l, Line l2) {
        return Math.abs((l.y2 - l.y) / (l.x2 - l.x)) == Math.abs((l2.y2 - l2.y) / (l2.x2 - l2.x));
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

    /**
     * Only works for axis-aligned walls
     */
    public static boolean pointInsidePolyedge(float x, float y, ArrayList<Line> edges,
                                              float mostLeftX) {
        int inters = 0;

        Line line = new Line(x, y, mostLeftX - 10, y);

        for (Line l : edges) {
            if (Intersect.pointAxisAlignedLine(x, y, l)) {
                return false;
            }
            if (l.isPerfectVertical() && Intersect.intersection(line, l)) {
                inters++;
            }
        }

        return inters % 2 != 0;
    }

    /**
     * Only works for axis-aligned lines
     */
    public static boolean pointInsideWallList(float x, float y, ArrayList<HalfSpace> walls,
                                              float mostLeftX) {
        int inters = 0;

        Line line = new Line(x, y, mostLeftX - 10, y);

        for (HalfSpace w : walls) {
            if (Intersect.pointAxisAlignedLine(x, y, w.line)) {
                return false;
            }
            if (w.line.isPerfectVertical() && Intersect.intersection(line, w.line)) {
                inters++;
            }
        }

        return inters % 2 != 0;
    }

    public static boolean rayVerticalLine(Ray r, Line l) {
        float rY = r.y + r.slope * l.x;
        return rY < l.y2 && rY > l.y;
    }

    public static boolean onRng(float f, float min, float max) {
        return f >= min && f <= max;
    }

    private static boolean inRng(float f, float min, float max) {
        return f > min && f < max;
    }

    /**
     * Warning: Heavy, should not be run often!
     */
    public static void separateCells(ArrayList<Cell> cells, Random r, float f) {
        while (runOneSeparationIteration(cells, r, f)) ;
    }

    /**
     * Runs one separation-iteration on the given cells. Returns false if no intersections more
     * extensive than accuracy where found
     *
     * @param cells The cells to separate
     * @param r     Random-object to use to distribute rooms
     * @param acc   How accurate should the tester be?
     * @return False if no intersection found was larger than given accuracy
     */
    public static boolean runOneSeparationIteration(ArrayList<Cell> cells, Random r, float acc) {
        boolean collisionFound = false;

        for (int i = 0; i < cells.size() - 1; i++) {
            // Get cell to compare to rest of list
            Cell c1 = cells.get(i);

            // Iterate through every object beneath i-one, in the list
            for (int j = i + 1; j < cells.size(); j++) {
                // Get cell 2 to compare to cell 1
                Cell c2 = cells.get(j);

                // Find out if the two cells intersect
                float penX = horisontalPenetration(c1.rect, c2.rect);
                float penY = verticalPenetration(c1.rect, c2.rect);

                if (Math.abs(penX) > 0 && Math.abs(penY) > 0) {
                    // Find shortest penetration-axis. Set other one to 0
                    if (Math.abs(penX) <= Math.abs(penY)) {
                        penY = 0;
                    } else {
                        penX = 0;
                    }

                    if (penX + penY > acc) {
                        collisionFound = true;
                    }

                    float halfPenX = penX / 2;
                    float halfPenY = penY / 2;
                    c1.rect.addToPosition(halfPenX, halfPenY);
                    c2.rect.addToPosition(-halfPenX, -halfPenY);
                }
            }
        }

        return collisionFound;
    }

    public static Vector2 penetration(Rectangle r1, Rectangle r2) {
        Vector2 pen = new Vector2(horisontalPenetration(r1, r2), verticalPenetration(r1, r2));
        if (Math.abs(pen.x) <= Math.abs(pen.y)) {
            pen.y = 0;
        } else {
            pen.x = 0;
        }
        return pen;
    }


    public static float horisontalPenetration(Rectangle r1, Rectangle r2) {
        float halfW1 = r1.width / 2;
        float halfW2 = r2.width / 2;

        float center1 = r1.x + halfW1;
        float center2 = r2.x + halfW2;

        float distanceX = center1 - center2;
        float minDistanceX = halfW1 + halfW2;

        // No intersection
        if (Math.abs(distanceX) >= minDistanceX) {
            return 0f;
        }

        return distanceX > 0 ? minDistanceX - distanceX : -minDistanceX - distanceX;
    }

    public static float verticalPenetration(Rectangle r1, Rectangle r2) {
        float halfH1 = r1.height / 2;
        float halfH2 = r2.height / 2;

        float center1 = r1.y + halfH1;
        float center2 = r2.y + halfH2;

        float distanceY = center1 - center2;
        float minDistanceY = halfH1 + halfH2;

        // No intersection
        if (Math.abs(distanceY) >= minDistanceY) {
            return 0f;
        }

        return distanceY > 0 ? minDistanceY - distanceY : -minDistanceY - distanceY;
    }

    public static float horizontalPenetrationRectRect(float x, float x2, float width, float width2) {
        float halfW1 = width / 2;
        float halfW2 = width2 / 2;

        float center1 = x + halfW1;
        float center2 = x2 + halfW2;

        float distanceX = center1 - center2;
        float minDistanceX = halfW1 + halfW2;

        // No intersection
        if (Math.abs(distanceX) >= minDistanceX) {
            return 0f;
        }

        return distanceX > 0 ? minDistanceX - distanceX : -minDistanceX - distanceX;
    }

    public static float verticalPenetrationRectRect(float y, float y2, float height, float height2) {
        float halfH1 = height / 2;
        float halfH2 = height2 / 2;

        float center1 = y + halfH1;
        float center2 = y2 + halfH2;

        float distanceY = center1 - center2;
        float minDistanceY = halfH1 + halfH2;

        // No intersection
        if (Math.abs(distanceY) >= minDistanceY) {
            return 0f;
        }

        return distanceY > 0 ? minDistanceY - distanceY : -minDistanceY - distanceY;
    }

    public static Vector2 distance(Line l, float pntx, float pnty) {
        float px = l.x2 - l.x;
        float py = l.y2 - l.y;

        float s = px * px + py * py;

        float u = ((pntx - l.x) * px + (pnty - l.y) * py) / s;

        if (u > 1) {
            u = 1;
        } else if (u < 0) {
            u = 0;
        }

        float nx = l.x + u * px;
        float ny = l.y + u * py;

        float dx = nx - pntx;
        float dy = ny - pnty;

        return new Vector2(dx, dy);
    }

    public static Vector2 distance(Line l, Vector2 p) {
        return distance(l, p.x, p.y);
    }

    public static Vector2 penetration(Vector2 p1, float r1, Vector2 p2, float r2) {
        Vector2 dist = new Vector2(p2).sub(p1);
        return dist.nor().scl(dist.getMag() - r1 - r2);
    }
}