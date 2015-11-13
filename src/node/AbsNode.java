package node;

import java.util.LinkedList;

public class AbsNode {

	private int X;
	private int Y;
	private boolean isWalkable;
	private AbsNode parent;
	private double cost;
	private LinkedList<Edge> edges;
	
	public AbsNode(int valX, int valY, boolean isWalk){
		X = valX;
		Y = valY;
		isWalkable = isWalk;
	}
	
	public int getX(){
		return X;
	}
	
	public int getY(){
		return Y;
	}
	
	public boolean getIsWalkable(){
		return isWalkable;
	}
	
	public LinkedList<Edge> getEdges(){
		return edges;
	}
	
	public AbsNode getParent(){
		return parent;
	}
	
	public void setParent(AbsNode parentToAdd){
		parent = parentToAdd;
	}
	
	public double getCost(){
		return cost;
	}
	
	public void setCost(double costToSet){
		cost = costToSet;
	}
}
