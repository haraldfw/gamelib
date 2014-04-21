/**
 * 
 */
package com.smokebox.lib.pcg.dungeon;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Harald Floor Wilhelmsen
 *
 */
public class Node implements Comparable<Node>{

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
	
	public static class OrderById implements Comparator<Node> {

		@Override
		public int compare(Node a, Node b) {
			// TODO Auto-generated method stub
			return a.id > b.id ? 1 : (a.id < b.id ? -1 : 0);
		}
	}

	@Override
	public int compareTo(Node arg0) {
		return 0;
	}
}
