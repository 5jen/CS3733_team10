package node;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

public class AbsNodeTest {

	@Test
	public void testAbsNode() {
		AbsNode node1 = new AbsNode(5, 6, true);
		assertEquals(5, node1.getX());
		assertEquals(6, node1.getY());
		assertEquals(true, node1.getIsWalkable());
		
		//Add a case for when it will fail
	}

	@Test
	public void testGetX() {
		
		AbsNode node1 = new AbsNode(4, 5, true);
		assertEquals(4, node1.getX());
	}

	@Test
	public void testGetY() {
		AbsNode node1 = new AbsNode(4, 5, true);
		assertEquals(5, node1.getY());
	}

	@Test
	public void testGetIsWalkable() {
		AbsNode node1 = new AbsNode(4, 5, true);
		assertEquals(true, node1.getIsWalkable());
	}

	@Test
	public void testSetGetEdges() {
		AbsNode aNode = new AbsNode(4, 5, true);
		AbsNode bNode = new AbsNode(6, 7, true);
		Edge edge1 = new Edge(aNode, bNode);
		aNode.setEdges(edge1);
		
		assertEquals(edge1, aNode.getEdges().pop());
	}

	@Test
	public void testSetGetParent() {
		AbsNode node19 = new AbsNode(1, 2, true);
		AbsNode node18 = new AbsNode(3, 4, true);
		node19.setParent(node18);
		assertEquals(node18, node19.getParent());
	}


	@Test
	public void testSetGetCost() {
		AbsNode node20 = new AbsNode(1, 2, true);
		node20.setCost(20);
		double testCost = node20.getCost();
		
		assertEquals(20, testCost, 1);
		
	}
	
	@Test
	public void testDeleteEdge() {
		AbsNode node25 = new AbsNode(1, 2, true);
		AbsNode node26 = new AbsNode(2, 2, true);
		Edge anEdge = new Edge(node25, node26);
		
		node25.deleteEdge(anEdge);
		
		LinkedList<Edge> aResult = new LinkedList<Edge>();
		
		
		assertEquals(aResult, node25.getEdges());
		
		node25.deleteEdge(anEdge);
		
	}

	
	
	
}