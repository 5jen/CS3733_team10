package node;

import java.util.LinkedList;

public class Node {

	private int X;
	private int Y;
	private int Z;
	private String name;
	private boolean isWalkable;
	private Node parent = null;
	private double cost = 0;
	private boolean isPlace;
	private LinkedList<Edge> edges = new LinkedList<Edge>();
	
	public Node(int valX, int valY, int valZ, String n, boolean isWalk, boolean place){
		X = valX;
		Y = valY;
		Z = valZ;
		name = n;
		isWalkable = isWalk;
		isPlace = place;
	}
	
	public String getName(){
		return this.name;
	}
	
	public int getX(){
		return X;
	}
	
	public int getY(){
		return Y;
	}
	
	public int getZ(){
		return Z;
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
	
	public Node getParent(){
		return parent;
	}
	
	public void setParent(Node parentToAdd){
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

}
