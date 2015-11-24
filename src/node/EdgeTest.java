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
		Node node1 = new Node(1, 2, 0, "", true, true, "");
		Node node2 = new Node(2, 2, 0, "", true, true, "");
		Edge edge1 = new Edge(node1, node2, getDistance(node1, node2));
		
		assertEquals(node1, edge1.getFrom());
	}

	@Test
	public void testGetTo() {
		Node node1 = new Node(1, 2, 0, "", true, true, "");
		Node node2 = new Node(2, 2, 0, "", true, true, "");
		Edge edge1 = new Edge(node1, node2, getDistance(node1, node2));
		
		assertEquals(node2, edge1.getTo());
	}

	@Test
	public void testGetDistance() {
		Node node1 = new Node(1, 2, 0, "", true, true, "");
		Node node2 = new Node(2, 2, 0, "", true, true, "");
		Edge edge1 = new Edge(node1, node2, getDistance(node1, node2));
		
		assertEquals(1, edge1.getDistance(), 0.001);
	}
	
	public int getDistance(Node n1, Node n2){
    	return (int) Math.sqrt((Math.pow(((int)n1.getX() - (int)n2.getX()), 2)) + (Math.pow(((int)n1.getY() - (int)n2.getY()), 2)));
    }

}
