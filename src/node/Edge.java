package node;

import java.lang.Math;

public class Edge {

	private AbsNode from;
	private AbsNode to;
	private double distance;
	
	public Edge(AbsNode fromNode, AbsNode toNode, double distanceBetween){
		from = fromNode;
		to = toNode;
		distance = Math.sqrt( (fromNode.getX() - toNode.getX())^2 + (fromNode.getY()-toNode.getY())^2);
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
}
