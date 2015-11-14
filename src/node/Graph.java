package node;

import java.util.LinkedList;
import java.util.TreeMap;

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
	
	public LinkedList<AbsNode> findRoute(AbsNode from, AbsNode to){
		if (to.getIsWalkable()){
			return null;
		}
		
		if (from.getIsWalkable()){
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
				return path;
			}
			
			explored.add(current);
			
			for (Edge neighbor : current.getEdges()){
				if (neighbor.getParent() == null){
					neighbor.cost = d(current, neighbor) + current.cost;
					neighbor.parent = current;
					unknownFrontier.put((neighbor.cost+ d(neighbor, goal)), neighbor);
				}
				else {
					if ((d(current, neighbor) + current.cost) < neighbor.cost){
						neighbor.cost = d(current, neighbor) + current.cost;
						neighbor.parent = current;
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

}
