package node;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

public class GraphTest {

	@Test
	public void testGraph() {
//		fail("Not yet implemented");
	}

	@Test
	public void testAddNode() {
		Graph testAdd = new Graph();
		AbsNode nodeAdd = new AbsNode(5,6, true, true);
		testAdd.addNode(nodeAdd);
		
		LinkedList<AbsNode> linkListNode = new LinkedList<AbsNode>();
		linkListNode.add(nodeAdd);
		
		assertEquals(linkListNode, testAdd.getNodes());
		
		Graph testMultiple = new Graph();
		
		AbsNode node1 = new AbsNode(0,0, true, true);
		AbsNode node2 = new AbsNode(1,0,true, true);
		AbsNode node3 = new AbsNode(0,1, true, true);
		AbsNode node4 = new AbsNode(1,1, true, true);
		
		testMultiple.addNode(node1);
		testMultiple.addNode(node2);
		testMultiple.addNode(node3);
		testMultiple.addNode(node4);
		
		LinkedList<AbsNode> linkList = new LinkedList<AbsNode>();
		linkList.add(node4);
		linkList.add(node3);
		linkList.add(node2);
		linkList.add(node1);
		
		assertEquals(linkList, testMultiple.getNodes());
		
	}

	@Test
	public void testAddEdge() {
		Graph addEdgeTest = new Graph();
		
		AbsNode node1 = new AbsNode(4,2, true, true);
		AbsNode node2 = new AbsNode(9,3, true, true);
		
		addEdgeTest.addNode(node2);
		addEdgeTest.addNode(node1);
		
		addEdgeTest.addEdge(node1, node2);
		
		assertEquals(node1.getEdges(), addEdgeTest.getNodes().getFirst().getEdges());
		assertEquals(node2.getEdges(), addEdgeTest.getNodes().getLast().getEdges());
	}
	
	@Test
	public void testDeleteNode() {
		Graph deleteTest = new Graph();
		
		AbsNode node1 = new AbsNode(0,0,true, true);
		AbsNode node2 = new AbsNode(0,5,true, true);
		AbsNode node3 = new AbsNode(5,0,true, true);
		AbsNode node4 = new AbsNode(5,5,true, true);
		
		deleteTest.addNode(node4);
		deleteTest.addNode(node3);
		deleteTest.addNode(node2);
		deleteTest.addNode(node1);
		
		deleteTest.addEdge(node1, node3);
		deleteTest.addEdge(node2, node4);
		deleteTest.addEdge(node1, node4);
		deleteTest.addEdge(node3, node4);
		
		deleteTest.getNodes().get(0).printConnectingNodes();
		
		deleteTest.deleteNode(node3);
		
		Graph expectedList = new Graph();
		
		expectedList.addNode(node4);
		expectedList.addNode(node2);
		expectedList.addNode(node1);
		
		assertEquals(expectedList.getNodes(), deleteTest.getNodes()); // See if the nodes in both lists are the same
		assertEquals(expectedList.getNodes().get(0).getEdges(), deleteTest.getNodes().get(0).getEdges()); //Check to see if the edges are the same
		expectedList.getNodes().get(0).printConnectingNodes();
		deleteTest.getNodes().get(0).printConnectingNodes();
		
	}

	@Test
	public void testFindRoute() {
		Graph testGraph = new Graph();
		
		AbsNode nodeA = new AbsNode(0,3,true, true);
		AbsNode nodeB = new AbsNode(1,3,true, true);
		AbsNode nodeC = new AbsNode(2,3,true, true);
		AbsNode nodeD = new AbsNode(0,2,true, true);
		AbsNode nodeE = new AbsNode(2,2,true, true);
		AbsNode nodeF = new AbsNode(0,1,true, true);
		AbsNode nodeG = new AbsNode(1,1,true, true);
		AbsNode nodeH = new AbsNode(2,1,true, true);
		AbsNode nodeI = new AbsNode(1,0,true, true);
		AbsNode nodeJ = new AbsNode(2,0,true, true);
		
		testGraph.addNode(nodeA);
		testGraph.addNode(nodeB);
		testGraph.addNode(nodeC);
		testGraph.addNode(nodeD);
		testGraph.addNode(nodeE);
		testGraph.addNode(nodeF);
		testGraph.addNode(nodeG);
		testGraph.addNode(nodeH);
		testGraph.addNode(nodeI);
		testGraph.addNode(nodeJ);
		
		testGraph.addEdge(nodeA, nodeB);
		testGraph.addEdge(nodeB, nodeC);
		testGraph.addEdge(nodeA, nodeD);
		testGraph.addEdge(nodeD, nodeF);
		testGraph.addEdge(nodeF, nodeG);
		testGraph.addEdge(nodeG, nodeH);
		testGraph.addEdge(nodeG, nodeI);
		testGraph.addEdge(nodeG, nodeJ);
		
		LinkedList<AbsNode> expectedResult = new LinkedList<AbsNode>();
		
		expectedResult.add(nodeA);
		expectedResult.add(nodeD);
		expectedResult.add(nodeF);
		expectedResult.add(nodeG);
		expectedResult.add(nodeJ);
		
		LinkedList<AbsNode> actualResult = testGraph.findRoute(nodeA, nodeJ);
		LinkedList<AbsNode> someList1 = new LinkedList<AbsNode>();
		someList1.add(nodeA);
		someList1.add(nodeB);
		
		assertEquals(expectedResult, actualResult);
		assertEquals(someList1, testGraph.findRoute(nodeA, nodeB));
		assertEquals(null, testGraph.findRoute(nodeE, nodeJ));
		
		//Tests the ability of the code to update the cost of travelling to a previously traveled to node
		LinkedList<AbsNode> anotherResult = new LinkedList<AbsNode>();
		anotherResult.add(nodeJ);
		anotherResult.add(nodeG);
		anotherResult.add(nodeF);
		anotherResult.add(nodeD);
		anotherResult.add(nodeA);
		anotherResult.add(nodeB);
		anotherResult.add(nodeC);
		testGraph.addEdge(nodeH, nodeE);
		testGraph.addEdge(nodeE, nodeD);
		LinkedList<AbsNode> parentChangeResult = testGraph.findRoute(nodeJ, nodeC);
		
		assertEquals(anotherResult, parentChangeResult);
	}

//	@Test
//	public void testDrawPath() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testD() {
		AbsNode node1 = new AbsNode(0,0,true, true);
		AbsNode node2 = new AbsNode(2,2,true, true);
		
		Graph dGraph = new Graph();
		
		double testD = dGraph.d(node1, node2);
		
		assertEquals(2.828, testD, 0.001);
	}

	@Test
	public void testBacktrack() {
		AbsNode node1 = new AbsNode(0,0,true, true);
		AbsNode node6 = new AbsNode(2,2,true, true);
		AbsNode node10 = new AbsNode(2,4,true, true);
		AbsNode node11 = new AbsNode(4,4,true, true);
		AbsNode node7 = new AbsNode(4,2,true, true);
		AbsNode node8 = new AbsNode(6,2,true, true);
		AbsNode node12 = new AbsNode(6,4,true, true);
		
		node12.setParent(node8);
		node8.setParent(node7);
		node7.setParent(node11);
		node11.setParent(node10);
		node10.setParent(node6);
		node6.setParent(node1);
		
		Graph dummyGraph = new Graph();
		
		LinkedList<AbsNode> actualPath = dummyGraph.backtrack(node12);
		
		LinkedList<AbsNode> expectedPath = new LinkedList<AbsNode>();
		
		expectedPath.add(node1);
		expectedPath.add(node6);
		expectedPath.add(node10);
		expectedPath.add(node11);
		expectedPath.add(node7);
		expectedPath.add(node8);
		expectedPath.add(node12);
		
		assertEquals(expectedPath, actualPath);
		
	}

}
