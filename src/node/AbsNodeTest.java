package node;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

public class AbsNodeTest {

	@Test
	public void testAbsNode() {
		AbsNode node1 = new AbsNode(5, 6, true, true);
		assertEquals(5, node1.getX());
		assertEquals(6, node1.getY());
		assertEquals(true, node1.getIsWalkable());
		
		//Add a case for when it will fail
	}

	@Test
	public void testGetX() {
		
		AbsNode node1 = new AbsNode(4, 5, true, true);
		assertEquals(4, node1.getX());
	}

	@Test
	public void testGetY() {
		AbsNode node1 = new AbsNode(4, 5, true, true);
		assertEquals(5, node1.getY());
	}

	@Test
	public void testGetIsWalkable() {
		AbsNode node1 = new AbsNode(4, 5, true, true);
		assertEquals(true, node1.getIsWalkable());
	}

	@Test
	public void testSetGetEdges() {
		AbsNode aNode = new AbsNode(4, 5, true, true);
		AbsNode bNode = new AbsNode(6, 7, true, true);
		Edge edge1 = new Edge(aNode, bNode, getDistance(aNode, bNode));
		aNode.setEdges(edge1);
		
		assertEquals(edge1, aNode.getEdges().pop());
	}

	@Test
	public void testSetGetParent() {
		AbsNode node19 = new AbsNode(1, 2, true, true);
		AbsNode node18 = new AbsNode(3, 4, true, true);
		node19.setParent(node18);
		assertEquals(node18, node19.getParent());
	}


	@Test
	public void testSetGetCost() {
		AbsNode node20 = new AbsNode(1, 2, true, true);
		node20.setCost(20);
		double testCost = node20.getCost();
		
		assertEquals(20, testCost, 1);
		
	}
	
	@Test
	public void testDeleteEdge() {
		AbsNode node25 = new AbsNode(1, 2, true, true);
		AbsNode node26 = new AbsNode(2, 2, true, true);
		AbsNode node27 = new AbsNode(2, 1, true, true);

		
		Edge someEdge = new Edge(node25, node26, getDistance(node25, node26));
		Edge anotherEdge = new Edge(node25, node27, getDistance(node25, node27));
		
		node25.setEdges(someEdge);
		node25.setEdges(anotherEdge);
		
		node25.deleteEdge(someEdge);
		
		LinkedList<Edge> aResult = new LinkedList<Edge>();
		aResult.add(anotherEdge);

		assertEquals(aResult, node25.getEdges());
		
		node25.deleteEdge(anotherEdge);
		
		LinkedList<Edge> eResult = new LinkedList<Edge>();
		
		assertEquals(eResult, node25.getEdges());
		
	}

	
	public int getDistance(AbsNode n1, AbsNode n2){
		return (int) Math.sqrt((Math.pow(((int)n1.getX() - (int)n2.getX()), 2)) + (Math.pow(((int)n1.getY() - (int)n2.getY()), 2)));
	}

	@Test
	public void testUpdateNode() throws Exception {
		AbsNode node1 = new AbsNode(0, 0, true, true);
        AbsNode node2 = new AbsNode(0, 1, true, true);
        AbsNode node3 = new AbsNode(1, 1, true, true);

        LinkedList<AbsNode> nodeList = new LinkedList<AbsNode>();
        nodeList.add(node1);
        nodeList.add(node2);
        nodeList.add(node3);

        Graph graph = new Graph();
        graph.setNodes(nodeList);

        graph.addEdge(node1, node2);
        graph.addEdge(node1, node3);
        graph.addEdge(node2, node3);

        node1.updateNode(node1.getX(), -1, node1.getIsWalkable(), node1.getIsPlace());

        Edge edgeNode12 = node1.getEdges().getFirst();
        Edge edgeNode21 = node2.getEdges().getFirst();

        assertEquals(2, edgeNode12.getDistance(), .001);
        assertEquals(2, edgeNode21.getDistance(), .001);
	}
}