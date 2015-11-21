package node;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

public class NodeTest {

	@Test
	public void testNode() {
		Node node1 = new Node(5, 6, 0, "", true, true, "");
		assertEquals(5, node1.getX());
		assertEquals(6, node1.getY());
		assertEquals(true, node1.getIsWalkable());
		
		//Add a case for when it will fail
	}

	@Test
	public void testGetX() {
		
		Node node1 = new Node(4, 5, 0, "", true, true, "");
		assertEquals(4, node1.getX());
	}

	@Test
	public void testGetY() {
		Node node1 = new Node(4, 5, 0, "", true, true, "");
		assertEquals(5, node1.getY());
	}

	@Test
	public void testGetIsWalkable() {
		Node node1 = new Node(4, 5, 0, "", true, true, "");
		assertEquals(true, node1.getIsWalkable());
	}

	@Test
	public void testSetGetEdges() {
		Node aNode = new Node(4, 5, 0, "", true, true, "");
		Node bNode = new Node(6, 7, 0, "", true, true, "");
		Edge edge1 = new Edge(aNode, bNode, getDistance(aNode, bNode));
		aNode.setEdges(edge1);
		
		assertEquals(edge1, aNode.getEdges().pop());
	}

	@Test
	public void testSetGetParent() {
		Node node19 = new Node(1, 2, 0, "", true, true, "");
		Node node18 = new Node(3, 4, 0, "", true, true, "");
		node19.setParent(node18);
		assertEquals(node18, node19.getParent());
	}


	@Test
	public void testSetGetCost() {
		Node node20 = new Node(1, 2, 0, "", true, true, "");
		node20.setCost(20);
		double testCost = node20.getCost();
		
		assertEquals(20, testCost, 1);
		
	}
	
	@Test
	public void testDeleteEdge() {
		Node node25 = new Node(1, 2, 0, "", true, true, "");
		Node node26 = new Node(2, 2, 0, "", true, true, "");
		Edge anEdge = new Edge(node25, node26, getDistance(node25, node26));
		
		node25.deleteEdge(anEdge);
		
		LinkedList<Edge> aResult = new LinkedList<Edge>();
		
		assertEquals(aResult, node25.getEdges());
		
		node25.deleteEdge(anEdge);
		
	}

	
	public int getDistance(Node n1, Node n2){
		return (int) Math.sqrt((Math.pow(((int)n1.getX() - (int)n2.getX()), 2)) + (Math.pow(((int)n1.getY() - (int)n2.getY()), 2)));
	}
	
}