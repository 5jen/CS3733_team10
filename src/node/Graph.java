package node;

import java.util.LinkedList;

public class Graph {
	
	private LinkedList<AbsNode> nodes;
	
	public Graph(){
		
	}
	
	public void addNode(AbsNode nodeToAdd){
		
	}
	
	public void deleteNode(AbsNode nodeToDelete){
		
	}
	
	public void addEdge(AbsNode node1, AbsNode node2){
		Edge newEdge1 = new Edge()
	}
	
	public LinkedList<AbsNode> findRoute(AbsNode to, AbsNode from){
		return new LinkedList<AbsNode>();
	}
	
	public void drawPath(LinkedList<Node> nodeList, LinkedList<AbsNode> absList){
		
	}
	
	public LinkedList<AbsNode> getNodes(){
		return nodes;
	}

}
