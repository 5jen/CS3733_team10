package node;

import java.util.LinkedList;
import java.util.TreeMap;

public class Graph {
	
	private LinkedList<AbsNode> nodes = new LinkedList<AbsNode>();
	
	public Graph(){
		
	}
	
	public void addNode(AbsNode nodeToAdd){
		nodes.push(nodeToAdd);
	}
	
	public void deleteNode(AbsNode nodeToDelete){
		LinkedList<AbsNode> connectingNodeList = new LinkedList<AbsNode>();
		LinkedList<Edge> edgeList = nodeToDelete.getEdges();
		for(Edge e : edgeList){
			connectingNodeList.add(e.getTo());
		}
		
		nodes.remove(nodeToDelete); // Deletes the node from the graph
		
		for (AbsNode node : nodes){
			for (AbsNode cNode : connectingNodeList){
				if (cNode.equals(node)){
					for(Edge e : node.getEdges()){
						node.deleteEdge(e);
					}
				}
			}
		}
	}
	
	public void addEdge(AbsNode node1, AbsNode node2){
		Edge newEdge1 = new Edge(node1, node2);
		Edge newEdge2 = new Edge(node2, node1);
		
		node1.setEdges(newEdge1);
		node2.setEdges(newEdge2);
		
	}
	
	public LinkedList<AbsNode> findRoute(AbsNode from, AbsNode to){
		if (!to.getIsWalkable()){
			return null;
		}
		
		if (!from.getIsWalkable()){
			return null;
		}
		
		LinkedList<AbsNode> path = new LinkedList<AbsNode>();
		TreeMap<Double, AbsNode> unknownFrontier = new TreeMap<Double, AbsNode>();
		from.setCost(0);
		unknownFrontier.put((from.getCost() + d(from, to)), from);
		LinkedList<AbsNode> explored = new LinkedList<AbsNode>();
		
		for (AbsNode n : nodes){
			n.setParent(null);
		}
		
		while (unknownFrontier.size() != 0){
			AbsNode current = unknownFrontier.pollFirstEntry().getValue();
			
			if (current == to){
				path = backtrack(current);
				return path;
			}
			
			explored.add(current);
			
			for (Edge neighbor : current.getEdges()){
				AbsNode neighborNode = neighbor.getTo();
				if (neighborNode.getParent() == null){
					neighborNode.setCost(d(current, neighborNode) + current.getCost());
					neighborNode.setParent(current);
					unknownFrontier.put((neighborNode.getCost()+ d(neighborNode, to)), neighborNode);
				}
				else {
					if ((d(current, neighborNode) + current.getCost()) < neighborNode.getCost()){
						neighborNode.setCost(d(current, neighborNode) + current.getCost());
						neighborNode.setParent(current);
					}
				}
			}
		}
			
		return null; // No Path Found
	}
	
	public void drawPath(LinkedList<Node> nodeList, LinkedList<AbsNode> absList){
		
	}
	
	public LinkedList<AbsNode> getNodes(){
		return nodes;
	}
	
	public double d(AbsNode from, AbsNode to){
		return Math.sqrt( (from.getX() - to.getX())^2 + (from.getY()-to.getY())^2);
	}
	
	public LinkedList<AbsNode> backtrack(AbsNode current){
		LinkedList <AbsNode> path = new LinkedList<AbsNode>();
		path.push(current);
		while (current.getParent() != null){
			path.push(current.getParent());
			current = current.getParent();
		}
		return path;
	}
}
		
