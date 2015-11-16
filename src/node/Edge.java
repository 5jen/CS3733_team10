package node;

public class Edge {

	private AbsNode from;
	private AbsNode to;
	private int distance;
	
	public Edge(AbsNode fromNode, AbsNode toNode, int distanceBetween){
		from = fromNode;
		to = toNode;
		distance = distanceBetween;
	}
	
	public AbsNode getFrom(){
		return from;
	}
	
	public AbsNode getTo(){
		return to;
	}
	
	public int getDistance(){
		return distance;
	}
}
