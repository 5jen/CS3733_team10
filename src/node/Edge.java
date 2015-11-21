package node;

public class Edge {

	private Node from;
	private Node to;
	private double distance;
	
	public Edge(Node fromNode, Node toNode, int dist){
		from = fromNode;
		to = toNode;
		distance = dist;
	}
	
	public Node getFrom(){
		return from;
	}
	
	
	public Node getTo(){
		return to;
	}
	
	public double getDistance(){
		return distance;
	}
	
	public void setDistance(double dToSet){
		distance = dToSet;
	}
}
