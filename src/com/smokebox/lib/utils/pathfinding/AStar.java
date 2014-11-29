/**
 * 
 */
package com.smokebox.lib.utils.pathfinding;

import com.smokebox.lib.utils.Intersect;
import com.smokebox.lib.utils.MathUtils;
import com.smokebox.lib.utils.Vector2;
import com.smokebox.lib.utils.geom.Line;
import com.smokebox.lib.utils.geom.Rectangle;
import com.smokebox.lib.utils.geom.UnifiablePolyedge;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Harald Floor Wilhelmsen
 *
 */
public class AStar {
	
	private ArrayList<StarNode> nodes;
	
	public AStar() {
		nodes = new ArrayList<StarNode>();
	}
	
	public void addNode(float x, float y) {
		nodes.add(new StarNode(x, y));
	}
	
	/**
	 * Returns the nodes, to be used for debugging(drawing)
	 * @return	ArrayList<StarNode> of the node-network
	 */
	public ArrayList<StarNode> getNodes() {
		return nodes;
	}

	/**
	 *
	 * @param start	Coordinates of starting position
	 * @param goal	Coordinates of goal
	 * @param heuristic	The method of estimating the best next node.
	                    Manhattan for performance. Pythagorean for accuracy.
	 * @return
	 */
	public ArrayList<StarNode> findPath(StarNode start, StarNode goal, Heuristic heuristic) {
		PathfindingList open = new PathfindingList();
		PathfindingList closed = new PathfindingList();
		
		start.costSoFar = 0;
		start.estimatedTotalCost = heuristic.estimate(start);
		
		open.addNode(start);
		
		StarNode current = start;
		
		while(open.getSize() > 0) {
			
			current = open.getSmallestElement();
			if(current == goal) break;
			
			for(Connection c : current.getConnections()) {
				StarNode endNode = c.end;

				float endNodeCost = current.costSoFar + c.weight;
				float endNodeHeuristic;

				// Check if node is in closed list
				if(closed.contains(endNode)) {
					// If we have not found a quicker path
					if(endNode.costSoFar <= endNodeCost) continue;
					closed.purgeNode(endNode);
					endNodeHeuristic = endNode.estimatedTotalCost - endNode.costSoFar;
					
				} else if(open.contains(endNode)) { // If node is in open list already
					if(endNode.costSoFar <= endNodeCost) continue;
					endNodeHeuristic = endNode.estimatedTotalCost - endNode.costSoFar;
					
				} else { // We come this far if we need to update the node
					endNodeHeuristic = heuristic.estimate(endNode);
				}
				
				endNode.setParent(current);
				endNode.costSoFar = current.costSoFar + c.weight;
				endNode.estimatedTotalCost = endNodeCost + endNodeHeuristic;
				
				if(!open.contains(endNode))	
					open.addNode(endNode);
			}
			
			open.purgeNode(current);
			closed.addNode(current);
		}
		if(current != goal) return null;
		ArrayList<StarNode> path = new ArrayList<>();
		
		while(current != start) {
			path.add(current.getParent());
			current = current.getParent();
		}
		Collections.reverse(path);
		return path;
	}
	
	/**
	 * A class to store nodes
	 * @author Harald Floor Wilhelmsen
	 */
	private class PathfindingList {
		
		ArrayList<StarNode> nodes;
		
		public PathfindingList() {
			nodes = new ArrayList<>();
		}
		
		public StarNode getSmallestElement() {
			StarNode best = nodes.get(0);
			// Can be optimized to do one less if-comparison
			for(StarNode i : nodes) {
				if(i.estimatedTotalCost < best.estimatedTotalCost) best = i;
			}
			return best;
		}
		
		public void addNode(StarNode s) {
			nodes.add(s);
		}
		
		public void purgeNode(StarNode s) {
			nodes.remove(s);
		}
		
		public int getSize() {
			return nodes.size();
		}
		
		public boolean contains(StarNode n) {
			return nodes.contains(n);
		}
	}
	
	// TODO refine algorithm from World.java into these methods
	// 	to convert different types of world-representations to
	//	a pathfinding-friendly format
	public void defineWorldFromInt2(int[][] asInt) {
		ArrayList<Rectangle> rects = new ArrayList<Rectangle>();
		for(int i = 0; i < asInt.length; i++) {
			for(int j = 0; j < asInt[0].length; j++) {
				if(asInt[i][j] > 0) rects.add(new Rectangle(i, j, 1, 1));
			}
		}

		ArrayList<Line> lines = new ArrayList<Line>();
		for(Rectangle r : rects) {
			lines.add(new Line(
									  r.x, 			r.y,
									  r.x, 			r.y + r.height));
			lines.add(new Line(
									  r.x + r.width, 	r.y,
									  r.x + r.width, 	r.y + r.height));
			lines.add(new Line(
									  r.x, 			r.y,
									  r.x + r.width, 	r.y));
			lines.add(new Line(
									  r.x, 			r.y + r.height,
									  r.x + r.width,	r.y + r.height));
		}

		UnifiablePolyedge p = new UnifiablePolyedge(lines);
		defineWorldFromPolyedge(p);
	}
	
	/**
	 * Defines the world from the given polyedge and RoomsWithTree
	 * @param polyedge    The polyedge to define map from, required
	 *
	 */
	public void defineWorldFromPolyedge(UnifiablePolyedge polyedge) {
		polyedge.fixIntersectingWalls();
		System.out.println("Adding nodes...");
		for(Line l : polyedge.getEdges()) {
			if(getNodeAt(l.x, l.y, nodes) == null && !(nrLinesnPoint(l.x, l.y, polyedge.getEdges()) > 2)) nodes.add(new StarNode(l.x, l.y));
			if(getNodeAt(l.x2, l.y2, nodes) == null && !(nrLinesnPoint(l.x2, l.y2, polyedge.getEdges()) > 2)) nodes.add(new StarNode(l.x2, l.y2));
		}
		// Remove nodes in inner corners
		System.out.println(nodes.size() + " nodes added.");
		System.out.println("Detecting nodes in inner corners...");
		ArrayList<StarNode> toRemove = new ArrayList<StarNode>();
		for(StarNode n : nodes) {
			// TODO Refactor this method with a for(int i = 0; i < size;) if(nodeIsToBeRemoved){removeNode and i++}
			// Lines on this point
			ArrayList<Line> linesOnNode = getLinesFromThisPoint(n.x, n.y, polyedge.getEdges());
			if(linesOnNode.size() != 2) { // If node does not have exactly 2 lines, remove it
				toRemove.add(n);
				continue;
			}
			// we know linesOnNode only contains two lines
			Line l1 = linesOnNode.get(0);
			Line l2 = linesOnNode.get(1);
			Vector2 v1 = (n.x == l1.x && n.y == l1.y) ? new Vector2(l1.x2, l1.y2) : new Vector2(l1.x, l1.y);
			Vector2 v2 = (n.x == l2.x && n.y == l2.y) ? new Vector2(l2.x2, l2.y2)	: new Vector2(l2.x, l2.y);
			// It works. Don't question it
			Vector2 lIn = new Vector2(new Vector2(v2).sub(n.x, n.y).add(new Vector2(v1).sub(n.x, n.y)).scl(0.1f).add(n.x, n.y));
			if(Intersect.pointInsidePolyedge(lIn.x, lIn.y, polyedge.getEdges(), polyedge.getOrigin().x - 10)) toRemove.add(n);
		}
		System.out.println(toRemove.size() + " inner corner nodes detected. Purging...");
		while(toRemove.size() > 0) {
			StarNode s = toRemove.get(0);
			nodes.remove(s);
			toRemove.remove(s);
		}
		System.out.println("Done purging inner corner nodes. New size of nodeArray is: " + nodes.size());
		System.out.println("Final size of node-Array is: " + nodes.size());
		connectNodesBySight(polyedge.getEdges());
		connectNodesAlongWalls(polyedge.getEdges());
	}
	
	private ArrayList<Line> getLinesFromThisPoint(float x, float y, ArrayList<Line> lines) {
		ArrayList<Line> onThisPoint = new ArrayList<Line>();
		for(Line l : lines) {
			if((l.x == x && l.y == y) || (l.x2 == x && l.y2 == y)) onThisPoint.add(l);
		}
		return onThisPoint;
	}
	
	private int nrLinesnPoint(float x, float y, ArrayList<Line> lines) {
		int linesHere = 0;
		for(Line l : lines) {
			if((l.x == x && l.y == y) || (l.x2 == x && l.y2 == y)) linesHere++;
		}
		return linesHere;
	}
	
	public void connectNodesBySight(ArrayList<Line> intersectors) {
		System.out.println("Mapping lines of sight on " + nodes.size() + " nodes");
		float sightLines = 0;
		for(StarNode n : nodes) {
			for(StarNode s : nodes) {
				if(n == s) continue;
				Line l = new Line(n.x, n.y, s.x, s.y);
				if(!Intersect.pointInsidePolyedge(l.x + (l.x2 - l.x)/2, l.y + (l.y2 - l.y)/2, intersectors, -10)) continue;
				Boolean collision = false;
				for(Line w : intersectors) {
					if(Intersect.intersection(l, w)) {
						collision = true;
						break;
					}
				}
				if(collision) continue;
				n.addConnection(s);
				sightLines++;
			}
		}
		System.out.println("Done mapping lines of sight. Total sightlines: " + sightLines);
		System.out.println("Fixing sightline intersections...");
		for(StarNode s : nodes) { // for every node in nodeList
			ArrayList<Connection> toRemove_s = new ArrayList<>();
			for(Connection c : s.getConnections()) { // For every s-node connection
				Line cl = c.getAsLine(); // Connection represented as a Line
				for(StarNode s2 : nodes) { // For every node in nodeList. StarNode s2
					if(s == s2) continue;
					for(Connection c2 : s2.getConnections()) { // for every s2-node connection
						Line c2l = c2.getAsLine(); // Connection#2 represented as a Line
						if(cl.equalTo(c2l)) continue; // If parallel, no intersection; skip
						if(Intersect.intersection(cl, c2l)) { // If connections intersect
							if(cl.getMag2() >= c2l.getMag2()) { // If c is shorter than c2, remove c
								toRemove_s.add(c);
							}
						}
					}
				}
			}
			while(toRemove_s.size() > 0) {
				s.removeConnection(toRemove_s.get(0));
				toRemove_s.remove(0);
			}
		}
		sightLines = 0;
		for(StarNode s : nodes) {
			sightLines += s.getConnections().size();
		}
		System.out.println("Done simplifying sightLines. New total is: " + sightLines);
		System.out.println("Finalizing connections");
		for(StarNode n : nodes) n.finalizeConnections();
		System.out.println("Connections sorted by weight");
		System.out.println("Done finalizing connections");
	}
	
	private void connectNodesAlongWalls(ArrayList<Line> walls) {
		for(Line l : walls) {
			StarNode n = getNodeAt(l.x, l.y, nodes);
			StarNode n2 = getNodeAt(l.x2, l.y2, nodes);
			if(n != null && n2 != null) {
				n.addConnection(n2);
				n2.addConnection(n);
			}
		}
	}
	
	/**
	 * Returns node at given x and y coordinates if such a node exists. 
	 * Will return null if no node exists with given coordinates
	 * @param x		x-coordinate to check
	 * @param y		y-coordinate to check
	 * @param nodes	list of nodes
	 * @return		The node if there is one, null if not
	 */
	private StarNode getNodeAt(float x, float y, ArrayList<StarNode> nodes) {
		for(StarNode n : nodes) {
			if(n.x == x && n.y == y) return n;
		}
		return null;
	}
	
	/**
	 * Almost works FIXME
	 * @param path	The path as an ArrayList of Vector2s
	 * @param walls	ArrayList of intersectors blocking sightlines
	 * @return	The path with the unnecessary nodes cut
	 */
	public static ArrayList<Vector2> postSmooth(ArrayList<Vector2> path, ArrayList<Line> walls) {
		ArrayList<Vector2> toRemove = new ArrayList<>();
		
		for(int i = 0; i < path.size(); i++) {
			// If node is already scheduled for removal, skip
			Vector2 vi = path.get(i);
			if(toRemove.contains(vi)) continue;
			
			for(int j = i + 2; j < path.size(); j++) {
				Vector2 vj = path.get(j);
				if(toRemove.contains(vj)) continue;
				if(!lineLineList(new Line(vi, vj), walls)) {
					toRemove.add(path.get(j - 1));
				}
			}
		}
		
		while(toRemove.size() > 0) {
			path.remove(toRemove.get(0));
			toRemove.remove(0);
		}
		return path;
	}
	
	/**
	 * FIXME
	 * @param l		The line to check against the list
	 * @param lines	List of lines to check against Line l
	 * @return		Boolean representing intersection
	 */
	public static boolean lineLineList(Line l, ArrayList<Line> lines) {
		for(Line l2 : lines) {
			if(Intersect.intersection(l, l2)) return true;
		}
		return false;
	}
	
	
	public StarNode getNodeClosestTo(float x, float y) {
		StarNode closestNode = nodes.get(0);
		float closestDist = MathUtils.vectorLengthSquared(closestNode.x - x, closestNode.y - y);
		for(int j = 0; j < nodes.size(); j++) {
			StarNode i = nodes.get(j);
			float newDist = MathUtils.vectorLengthSquared(i.x - x, i.y - y);
			if(newDist < closestDist) {
				closestNode = i;
				closestDist = newDist;
			}
		}
		return closestNode;
	}
}
