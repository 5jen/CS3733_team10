package node;

import java.util.LinkedList;

public class AbsNode {

	private int X;
	private int Y;
	private boolean isWalkable;
	private AbsNode parent = null;
	private double cost = 0;
	private boolean isPlace;
	private LinkedList<Edge> edges = new LinkedList<Edge>();
	
	public AbsNode(int valX, int valY, boolean isWalk, boolean place){
		X = valX;
		Y = valY;
		isWalkable = isWalk;
		isPlace = place;
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
	
	public boolean getIsPlace(){
		return isPlace;
	}

	public void setEdges(Edge anEdge){
		edges.add(anEdge);
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
	
	public void deleteEdge(Edge anEdge){
		for(Edge e: edges){
			if(anEdge.equals(e)){
				edges.remove(e);
				break;
			}
		}
	}
	
	public void printConnectingNodes(){
		for (Edge e : edges){
			String sf = String.format("X:%d, Y:%d", e.getTo().getX(), e.getTo().getY());
			System.out.println(sf);
		}
	}

	public String getName() {
		// TODO Auto-generated method stub
		return "";
	}
}
