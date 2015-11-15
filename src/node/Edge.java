package node;

import java.lang.Math;

public class Edge {

	private AbsNode from;
	private AbsNode to;
	private double distance;
	
	public Edge(AbsNode fromNode, AbsNode toNode){
		from = fromNode;
		to = toNode;
		distance = Math.sqrt( Math.pow((fromNode.getX() - toNode.getX()), 2.0) + Math.pow((fromNode.getY()-toNode.getY()), 2.0));
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
