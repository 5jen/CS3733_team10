package node;

public class Edge {

	private AbsNode from;
	private AbsNode to;
	private double distance;
	
	public Edge(AbsNode fromNode, AbsNode toNode, int dist){
		from = fromNode;
		to = toNode;
		distance = dist;
	}
	
	public AbsNode getFrom(){
		return from;
	}
	
	
	public AbsNode getTo(){
		return to;
	}
	
	public double getDistance(){
		return distance;
	}
	
	public void setDistance(double dToSet){
		distance = dToSet;
	}
}
