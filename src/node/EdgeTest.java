package node;

import static org.junit.Assert.*;

import org.junit.Test;

public class EdgeTest {

	@Test
	public void testEdge() {

		//Add a test for a case were it would fail
	}

	@Test
	public void testGetFrom() {
		AbsNode node1 = new AbsNode(1, 2, true, true);
		AbsNode node2 = new AbsNode(2, 2, true, true);
		Edge edge1 = new Edge(node1, node2, getDistance(node1, node2));
		
		assertEquals(node1, edge1.getFrom());
	}

	@Test
	public void testGetTo() {
		AbsNode node1 = new AbsNode(1, 2, true, true);
		AbsNode node2 = new AbsNode(2, 2, true, true);
		Edge edge1 = new Edge(node1, node2, getDistance(node1, node2));
		
		assertEquals(node2, edge1.getTo());
	}

	@Test
	public void testGetDistance() {
		AbsNode node1 = new AbsNode(1, 2, true, true);
		AbsNode node2 = new AbsNode(2, 2, true, true);
		Edge edge1 = new Edge(node1, node2, getDistance(node1, node2));
		
		assertEquals(1, edge1.getDistance(), 0.001);
	}
	
	public int getDistance(AbsNode n1, AbsNode n2){
    	return (int) Math.sqrt((Math.pow(((int)n1.getX() - (int)n2.getX()), 2)) + (Math.pow(((int)n1.getY() - (int)n2.getY()), 2)));
    }

}
