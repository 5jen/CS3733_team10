package node;

import java.util.LinkedList;
import java.util.TreeMap;

public class Graph {
	
	private LinkedList<Node> nodes = new LinkedList<Node>();
	private LinkedList<Edge> edges = new LinkedList<Edge>();
	
	public Graph(){
		
	}
	
	public void addNode(Node nodeToAdd){
		nodes.add(nodeToAdd);
	}
	
	public void setNodes(LinkedList<Node> nodeList){
		nodes = nodeList;
	}
	
	public void setEdges(LinkedList<Edge> edgeList){
		edges = edgeList;
	}
	
	
	public void deleteNode(Node nodeToDelete){
		LinkedList<Node> connectingNodeList = new LinkedList<Node>();
		LinkedList<Edge> edgeList = nodeToDelete.getEdges();
		for(Edge e : edgeList){
			connectingNodeList.add(e.getTo());
		}
		
		nodes.remove(nodeToDelete); // Deletes the node from the graph
		
		for (Node node : nodes){
			for (Node cNode : connectingNodeList){
				if (cNode.equals(node)){
					for (Edge e : node.getEdges()){
						if(e.getTo().equals(nodeToDelete)){
							node.deleteEdge(e);
							break;
						}
					}
				}
			}
		}
	}
	
	public void addEdge(Node node1, Node node2){
		int index1 = nodes.indexOf(node1);
		int index2 = nodes.indexOf(node2);
		int dist = getDistance(node1, node2);
		
		System.out.println("from: "+nodes.get(index1).getName()+" , to: "+nodes.get(index2).getName());
		System.out.println("from: "+nodes.get(index2).getName()+" , to: "+nodes.get(index1).getName());

		Edge newEdge1 = new Edge(nodes.get(index1), nodes.get(index2), dist);
		Edge newEdge2 = new Edge(nodes.get(index2), nodes.get(index1), dist);
		
		nodes.get(index1).setEdges(newEdge1);
		nodes.get(index2).setEdges(newEdge2);
		
	}
	
	public LinkedList<Node> findRoute(Node from, Node to){
//		if (!to.getIsWalkable()){
//			return null;
//		}
//		
//		if (!from.getIsWalkable()){
//			return null;
//		}
		
		LinkedList<Node> path = new LinkedList<Node>();
		TreeMap<Double, Node> unknownFrontier = new TreeMap<Double, Node>();
		from.setCost(0);
		unknownFrontier.put((from.getCost() + d(from, to)), from);
		LinkedList<Node> explored = new LinkedList<Node>();
		
		for (Node n : nodes){
			n.setParent(null);
		}
		System.out.println("About to find the Route..");

		while (unknownFrontier.size() > 0){
			Node current = unknownFrontier.pollFirstEntry().getValue();
			System.out.println("Start looking at current node");
			
			if (current.equals(to)){
				System.out.println("Path Found!");
				from.setParent(null);
				path = backtrack(current);
				return path;
			}
			
			explored.add(current);
			
			for (Edge neighbor : current.getEdges()){
				System.out.println("Looking at neighbors...");
				Node neighborNode = neighbor.getTo();
				if (neighborNode.getParent() == null){
					neighborNode.setCost(d(current, neighborNode) + current.getCost());
					neighborNode.setParent(current);
					unknownFrontier.put((neighborNode.getCost()+ d(neighborNode, to)), neighborNode);
					System.out.println("Adding new node to frontier...");
				}
				else {
					if ((d(current, neighborNode) + current.getCost()) < neighborNode.getCost()){
						System.out.println("Resetting cost and parent of already visited node");
						neighborNode.setCost(d(current, neighborNode) + current.getCost());
						neighborNode.setParent(current);
					}
				}
			}
		}
			
		return null; // No Path Found
	}
	
	public void drawPath(LinkedList<Node> nodeList, LinkedList<Node> absList){
		
	}
	
	public LinkedList<Node> getNodes(){
		return nodes;
	}
	
	public LinkedList<Edge> getEdges(){
		return edges;
	}
	
	public double d(Node from, Node to){
		return Math.sqrt( Math.pow(from.getX() - to.getX(), 2.0) + Math.pow((from.getY()-to.getY()), 2.0));
	}
	
	public LinkedList<Node> backtrack(Node current){
		LinkedList <Node> path = new LinkedList<Node>();
		path.push(current);
		while (current.getParent() != null){
			String fs = String.format("Current Node Position, X:%d Y:%d", current.getParent().getX(), current.getParent().getY());
			System.out.println(fs);
			path.push(current.getParent());
			current = current.getParent();
			System.out.println("Added node to result");
		}
		return path;
	}
	
	public int getDistance(Node n1, Node n2){
    	return (int) Math.sqrt((Math.pow(((int)n1.getX() - (int)n2.getX()), 2)) + (Math.pow(((int)n1.getY() - (int)n2.getY()), 2)));
    }
}
		
