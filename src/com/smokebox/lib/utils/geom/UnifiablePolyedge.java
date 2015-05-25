/**
 *
 */
package com.smokebox.lib.utils.geom;

import com.smokebox.lib.utils.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author Harald Floor Wilhelmsen
 */
public class UnifiablePolyedge {

  private ArrayList<Line> edges;

  private Vector2 origin = new Vector2();

  /**
   * Create an empty polyedge. To add edges call addEdge-method and give Lines
   */
  public UnifiablePolyedge() {
    edges = new ArrayList<>();
  }

  /**
   * Creates a new unifiable polyedge from the given map. This method only takes into account 2
   * tile-types; 0 and 1
   *
   * @param map The int[][] to create map from
   */
  public UnifiablePolyedge(int[][] map) {
    ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[0].length; j++) {
        if (map[i][j] > 0) {
          rects.add(new Rectangle(i, j, 1, 1));
        }
      }
    }

    ArrayList<Line> lines = new ArrayList<Line>();
    for (Rectangle r : rects) {
      lines.add(new Line(
          r.x, r.y,
          r.x, r.y + r.height));
      lines.add(new Line(
          r.x + r.width, r.y,
          r.x + r.width, r.y + r.height));
      lines.add(new Line(
          r.x, r.y,
          r.x + r.width, r.y));
      lines.add(new Line(
          r.x, r.y + r.height,
          r.x + r.width, r.y + r.height));
    }

    this.edges = lines;
  }

  /**
   * Creates a polyedge from the given Line-list
   */
  public UnifiablePolyedge(ArrayList<Line> edges) {
    this.edges = edges;
  }

  public void addEdge(Line l) {
    edges.add(l);

    if (l.x < origin.x) {
      origin.x = l.x;
    }
    if (l.x2 < origin.x) {
      origin.x = l.x2;
    }
    if (l.y < origin.y) {
      origin.y = l.y;
    }
    if (l.y2 < origin.y) {
      origin.y = l.y2;
    }
  }

  public void removeEdge(Line l) {
    if (edges.contains(l)) {
      edges.remove(l);
    }
  }

  /**
   * Iterates through the edges of this polygon and removes all unnecessary edges, merges successive
   * lines and fixes retains "loops"
   */
  public UnifiablePolyedge unify() {
// Remove duplicates -----------------------------------------------
    makeDirectionsRightUp(edges);

    System.out.println("Unifying polyedge");
    System.out.println("Size of edge-list: " + edges.size());
    System.out.println("Finding duplicate lines..");
    ArrayList<Line> toRemove = new ArrayList<>();

    for (int i = 0; i < edges.size(); i++) {
      Line l = edges.get(i);

      for (int j = i + 1; j < edges.size(); j++) {
        Line l2 = edges.get(j);

        if (isEqual(l, l2)) {
          toRemove.add(l);
          toRemove.add(l2);
        }
      }
    }

    System.out.println("Removing " + toRemove.size() + " duplicate lines");
    while (toRemove.size() > 0) {
      Line l = toRemove.get(0);
      edges.remove(l);
      toRemove.remove(l);
    }
    System.out.println("Done. New size of edge-list is: " + edges.size());

// Merge lines that are successive ---------------------------------------
    System.out.println("-------- Merging successive lines...");
    float highestX = 0;
    float highestY = 0;

    ArrayList<Line> horLines = new ArrayList<Line>();
    ArrayList<Line> verLines = new ArrayList<Line>();
    // Split edge-list into horizontal and vertical lines
    for (Line w : edges) {
      if (w.y > highestY) {
        highestY = w.y; // Find the highest y
      }
      if (w.x > highestX) {
        highestX = w.x; // Find the highest x
      }

      if (w.y == w.y2) { // if line is horizontal
        horLines.add(w); // Add to horizontal lines
      } else { // Line is vertical
        verLines.add(w); // Add to vertical lines
      }
    }

    edges = new ArrayList<>(); // Empty edges-list

    // Merge successive horizontal lines
    for (int y = 0; y <= highestY; y++) {

      ArrayList<Line> xLines = new ArrayList<Line>();

      for (Line w : horLines) {
        if (w.y == y) {
          xLines.add(w);
        }
      }
      Collections.sort(xLines, new Comparator<Line>() { // Sort xLines by xCoordinate
        @Override
        public int compare(Line l, Line l2) {
          return (int) (l2.x - l.x);
        }
      });

      for (int x = 0; x <= highestX; x++) {
        Line l = findLineStartingAt(xLines, x, y);
        if (l == null) {
          continue;
        }
        for (int x2 = x + 1; x2 <= highestX; x2++) {
          Line l2 = findLineStartingAt(xLines, x2, y);
          if (l2 == null) {
            edges.add(new Line(x, y, x2, y));
            x = x2;
            break;
          }
        }
      }
    }
    System.out.println("Horizontal lines merged.");
    // Merge successive vertical lines
    for (int x = 0; x <= highestX; x++) {

      ArrayList<Line> yLines = new ArrayList<Line>();

      for (Line w : verLines) {
        if (w.x == x) {
          yLines.add(w);
        }
      }
      Collections.sort(yLines, new Comparator<Line>() { // Sort yLines by y-coord
        @Override
        public int compare(Line l, Line l2) {
          return (int) (l2.y - l.y);
        }
      });

      for (int y = 0; y <= highestY; y++) {
        Line l = findLineStartingAt(yLines, x, y);
        if (l == null) {
          continue;
        }
        for (int y2 = y + 1; y2 <= highestY; y2++) {
          Line l2 = findLineStartingAt(yLines, x, y2);
          if (l2 == null) {
            edges.add(new Line(x, y, x, y2));
            y = y2;
            break;
          }
        }
      }
    }
    System.out.println("Vertical lines merged.");
    System.out.println("Done. New size of edge-list is: " + edges.size());
    return this;
  }

  public void fixIntersectingWalls() {
    // Check for "loops", as in intersections, in the lines
    System.out.println("-------- Starting clean-up");
    System.out.println("Fixing 'loops'");

    for (int i = 0; i < edges.size(); i++) {
      Line l = edges.get(i);
      for (int j = i + 1; j < edges.size(); j++) {
        Line l2 = edges.get(j);
        // If lines are parallel
        if ((l2.x2 == l2.x && l.x == l.x2) || (l2.y2 == l2.y && l.y == l.y2)) {
          continue;
        }
        Vector2 intersection = intersection(l, l2);
        if (intersection != null) { // If the line has intersector
          edges.add(new Line(l.x, l.y, intersection.x, intersection.y));
          edges.add(new Line(intersection.x, intersection.y, l.x2, l.y2));
          edges.add(new Line(l2.x, l2.y, intersection.x, intersection.y));
          edges.add(new Line(intersection.x, intersection.y, l2.x2, l2.y2));

          edges.remove(l);
          edges.remove(l2);
        }
      }
    }
    System.out.println("Done. Final size of edge-list is: " + edges.size() + "\n");
  }

  private Vector2 intersection(Line l, Line l2) {
    Line h, v;

    if (l.y == l.y2) {
      h = l;
      v = l2;
    } else {
      h = l2;
      v = l;
    }

    if (h.y <= v.y || h.y >= v.y2 || h.x >= v.x || h.x2 <= v.x) {
      return null;
    }
    return new Vector2(v.x, h.y);
  }

  private Line findLineStartingAt(ArrayList<Line> lines, float x, float y) {

    for (Line l : lines) {
      if (l.x == x && l.y == y) {
        return l;
      }
    }

    return null;
  }

  private void makeDirectionsRightUp(ArrayList<Line> lines) {
    for (Line l : lines) {
      if (l.x2 < l.x) {
        float t = l.x;
        l.x = l.x2;
        l.x2 = t;
      }
      if (l.y2 < l.y) {
        float t = l.y2;
        l.y = l.y2;
        l.y = t;
      }
    }
  }

  private boolean isEqual(Line l, Line l2) {
    return l.x == l2.x
           && l.x2 == l2.x2
           && l.y == l2.y
           && l.y2 == l2.y2;
  }

  public ArrayList<Line> getEdges() {
    return edges;
  }

  public Vector2 getOrigin() {
    return origin;
  }

  public void scale(float scale) {
    for (Line l : edges) {
      l.x *= scale;
      l.y *= scale;
      l.x2 *= scale;
      l.y2 *= scale;
    }
  }
}
