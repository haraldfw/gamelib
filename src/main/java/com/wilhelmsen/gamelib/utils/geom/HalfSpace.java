/**
 *
 */
package com.wilhelmsen.gamelib.utils.geom;

import com.wilhelmsen.gamelib.utils.Vector2;

/**
 * @author Harald Floor Wilhelmsen
 */
public class HalfSpace {

    public Line line;
    // Perpendicular vector pointing "into" the open-side
    public Vector2 inside;

    public HalfSpace(Line l, Vector2 inside) {
        line = l;
        this.inside = inside;
    }

    public Vector2 getAsVector2() {
        return new Vector2();
    }
}
