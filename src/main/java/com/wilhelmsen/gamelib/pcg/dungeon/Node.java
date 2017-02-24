/**
 *
 */
package com.wilhelmsen.gamelib.pcg.dungeon;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Harald Floor Wilhelmsen
 */
public class Node implements Comparable<Node> {

  public float x;
  public float y;

  public int id;

  public List<Node> connectedNodes;

  public Node(float x, float y, int id) {
    this.x = x;
    this.y = y;

    this.id = id;

    connectedNodes = new ArrayList<>();
  }

  public void connectTo(Node n) {
    connectedNodes.add(n);
  }

  @Override
  public int compareTo(Node n2) {
    return n2.id - this.id;
  }
}
