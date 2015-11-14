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
		AbsNode node1 = new AbsNode(1, 2, true);
		AbsNode node2 = new AbsNode(2, 2, true);
		Edge edge1 = new Edge(node1, node2);
		
		assertEquals(node1, edge1.getFrom());
	}

	@Test
	public void testGetTo() {
		AbsNode node1 = new AbsNode(1, 2, true);
		AbsNode node2 = new AbsNode(2, 2, true);
		Edge edge1 = new Edge(node1, node2);
		
		assertEquals(node2, edge1.getTo());
	}

	@Test
	public void testGetDistance() {
		AbsNode node1 = new AbsNode(1, 2, true);
		AbsNode node2 = new AbsNode(2, 2, true);
		Edge edge1 = new Edge(node1, node2);
		
		assertEquals(1, edge1.getDistance(), 0.001);
	}

}
