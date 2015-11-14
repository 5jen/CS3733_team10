package node;

import static org.junit.Assert.*;

import org.junit.Test;

public class AbsNodeTest {

	@Test
	public void testAbsNode() {
		fail("Not yet implemented");
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
	public void testGetEdges() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetParent() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetParent() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCost() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetCost() {
		fail("Not yet implemented");
	}

}
