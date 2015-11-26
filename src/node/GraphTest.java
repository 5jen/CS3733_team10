package node;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

public class GraphTest {

	@Test
	public void testAddNode() {
		Graph testAdd = new Graph();
		Node nodeAdd = new Node(5,6, 0, "", "", true, true, "");
		testAdd.addNode(nodeAdd);
		
		LinkedList<Node> linkListNode = new LinkedList<>();
		linkListNode.add(nodeAdd);
		
		assertEquals(linkListNode, testAdd.getNodes());
		
		Graph testMultiple = new Graph();
		
		Node node1 = new Node(0,0, 0, "", "", true, true, "");
		Node node2 = new Node(1,0, 0, "", "", true, true, "");
		Node node3 = new Node(0,1, 0, "", "", true, true, "");
		Node node4 = new Node(1,1, 0, "", "", true, true, "");
		
		testMultiple.addNode(node1);
		testMultiple.addNode(node2);
		testMultiple.addNode(node3);
		testMultiple.addNode(node4);
		
		LinkedList<Node> linkList = new LinkedList<>();
		linkList.add(node1);
		linkList.add(node2);
		linkList.add(node3);
		linkList.add(node4);
		
		assertEquals(linkList, testMultiple.getNodes());
		
	}

	@Test
	public void testAddEdge() {
		Graph addEdgeTest = new Graph();
		
		Node node1 = new Node(4,2, 0, "", "", true, true, "");
		Node node2 = new Node(9,3, 0, "", "", true, true, "");
		
		addEdgeTest.addNode(node1);
		addEdgeTest.addNode(node2);
		
		addEdgeTest.addEdge(node1, node2);
		
		assertEquals(node1.getEdges(), addEdgeTest.getNodes().getFirst().getEdges());
		assertEquals(node2.getEdges(), addEdgeTest.getNodes().getLast().getEdges());
	}
	
	@Test
	public void testDeleteNode() {
		Graph deleteTest = new Graph();
		
		Node node1 = new Node(0,0, 0, "", "", true, true, "");
		Node node2 = new Node(0,5, 0, "", "", true, true, "");
		Node node3 = new Node(5,0, 0, "", "", true, true, "");
		Node node4 = new Node(5,5, 0, "", "", true, true, "");
		
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
		
		Node nodeA = new Node(0,3, 0, "", "", true, true, "");
		Node nodeB = new Node(1,3, 0, "", "", true, true, "");
		Node nodeC = new Node(2,3, 0, "", "", true, true, "");
		Node nodeD = new Node(0,2, 0, "", "", true, true, "");
		Node nodeE = new Node(2,2, 0, "", "", true, true, "");
		Node nodeF = new Node(0,1, 0, "", "", true, true, "");
		Node nodeG = new Node(1,1, 0, "", "", true, true, "");
		Node nodeH = new Node(2,1, 0, "", "", true, true, "");
		Node nodeI = new Node(1,0, 0, "", "", true, true, "");
		Node nodeJ = new Node(2,0, 0, "", "", true, true, "");
		
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
		
		LinkedList<Node> expectedResult = new LinkedList<>();
		
		expectedResult.add(nodeA);
		expectedResult.add(nodeD);
		expectedResult.add(nodeF);
		expectedResult.add(nodeG);
		expectedResult.add(nodeJ);
		
		LinkedList<Node> actualResult = testGraph.findRoute(nodeA, nodeJ);
		
		assertEquals(expectedResult, actualResult);
		
		assertEquals(null, testGraph.findRoute(nodeE, nodeJ));

		//Tests the ability of the code to update the cost of travelling to a previously traveled to node
		LinkedList<Node> anotherResult = new LinkedList<>();
		anotherResult.add(nodeJ);
		anotherResult.add(nodeG);
		anotherResult.add(nodeF);
		anotherResult.add(nodeD);
		anotherResult.add(nodeA);
		anotherResult.add(nodeB);
		anotherResult.add(nodeC);
		testGraph.addEdge(nodeH, nodeE);
		testGraph.addEdge(nodeE, nodeD);
		LinkedList<Node> parentChangeResult = testGraph.findRoute(nodeJ, nodeC);

		assertEquals(anotherResult, parentChangeResult);
		
	}

//	@Test
//	public void testDrawPath() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testD() {
		Node node1 = new Node(0,0, 0, "", "", true, true, "");
		Node node2 = new Node(2,2, 0, "", "", true, true, "");
		
		Graph dGraph = new Graph();
		
		double testD = dGraph.d(node1, node2);
		
		assertEquals(2.828, testD, 0.001);
	}

	@Test
	public void testBacktrack() {
		Node node1 = new Node(0,0, 0, "", "", true, true, "");
		Node node6 = new Node(2,2, 0, "", "", true, true, "");
		Node node10 = new Node(2,4, 0, "", "", true, true, "");
		Node node11 = new Node(4,4, 0, "", "", true, true, "");
		Node node7 = new Node(4,2, 0, "", "", true, true, "");
		Node node8 = new Node(6,2, 0, "", "", true, true, "");
		Node node12 = new Node(6,4, 0, "", "", true, true, "");
		
		node12.setParent(node8);
		node8.setParent(node7);
		node7.setParent(node11);
		node11.setParent(node10);
		node10.setParent(node6);
		node6.setParent(node1);
		
		Graph dummyGraph = new Graph();
		
		LinkedList<Node> actualPath = dummyGraph.backtrack(node12);
		
		LinkedList<Node> expectedPath = new LinkedList<>();
		
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
