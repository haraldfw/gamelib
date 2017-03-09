package com.wilhelmsen.gamelib.pcg.dungeon;

import com.wilhelmsen.gamelib.utils.MathUtils;
import com.wilhelmsen.gamelib.utils.geom.Line;
import com.wilhelmsen.gamelib.utils.geom.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Harald Floor Wilhelmsen
 */
public class MinimumSpanningTree {

    public List<Node> nodesInTree;
    public List<Line> edges;
    float[][] relationChart;

    public MinimumSpanningTree(List<Node> openNodes) {
        relationChart = calculateRelations(openNodes);

        //Array2.printFloat2(relationChart);
                /*
                 * 1. Choose random node (0, probably)
		 * 2. Add to tree
		 * 3. Check all the nodes in the tree
		 * 		and compare their closest neighbors.
		 * 4. Add the closest node to the tree
		 * 5. Repeat step from step 3. until openNodes-list is empty
		 */

        nodesInTree = new ArrayList<>(); // Nodes finalized in the tree

        while (!openNodes.isEmpty()) { // While there are nodes that are not in the tree

            if (nodesInTree.isEmpty()) {
                nodesInTree.add(openNodes.get(0));
            }

            // Find node
            int bestNodeInTree = 0;
            int bestOpenNode = 0;
            float bestProximity = Float.MAX_VALUE;

            for (int i = 0; i < nodesInTree.size(); i++) {

                for (int j = 0; j < openNodes.size(); j++) {

                    int treeNodeCheck = nodesInTree.get(i).id;
                    int openNodeCheck = openNodes.get(j).id;
                    float distance = relationChart[treeNodeCheck][openNodeCheck];

                    if (distance < bestProximity) {
                        bestNodeInTree = treeNodeCheck;
                        bestOpenNode = openNodeCheck;
                        bestProximity = distance;
                    }
                }
            }

            for (int i = 0; i < openNodes.size(); i++) {
                if (openNodes.get(i).id == bestOpenNode) {
                    nodesInTree.add(openNodes.get(i));
                    for (int k = 0; k < nodesInTree.size(); k++) {
                        if (nodesInTree.get(k).id == bestNodeInTree) {
                            nodesInTree.get(k).connectTo(openNodes.get(i));
                        }
                    }
                    openNodes.remove(i);
                }
            }
        }

        Collections.sort(nodesInTree, (Node n, Node n2) -> n.compareTo(n2));

        edges = new ArrayList<>();

        for (int i = 0; i < nodesInTree.size(); i++) {
            Node ni = nodesInTree.get(i);
            for (int j = 0; j < nodesInTree.get(i).connectedNodes.size(); j++) {
                Node nj = nodesInTree.get(i).connectedNodes.get(j);
                edges.add(new Line(ni.x, ni.y, nj.x, nj.y));
            }
        }
        System.out.println(edges.size() + " edges added.");
    }

    public static MinimumSpanningTree createFromRects(List<Rectangle> rectangles) {
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < rectangles.size(); i++) {
            Rectangle r = rectangles.get(i);
            nodes.add(new Node(r.middlePos().x, r.middlePos().y, i));
        }
        return new MinimumSpanningTree(nodes);
    }

    public static MinimumSpanningTree createFromCells(List<Cell> cells) {
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < cells.size(); i++) {
            Cell c = cells.get(i);
            nodes.add(new Node(c.rect.middlePos().x, c.rect.middlePos().y, i));
        }
        return new MinimumSpanningTree(nodes);
    }

    private float[][] calculateRelations(List<Node> nodes) {

        float[][] relations = new float[nodes.size()][nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            for (int j = 0; j < nodes.size(); j++) {
                if (i != j) {
                    relations[i][j] = MathUtils.vectorLengthSquared(
                            nodes.get(i).x - nodes.get(j).x,
                            nodes.get(i).y - nodes.get(j).y
                    );
                }
            }
        }

        return relations;
    }
}
