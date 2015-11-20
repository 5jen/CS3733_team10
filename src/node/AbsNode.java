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
	
	public void updateNode(int xPos, int yPos, boolean isWalk, boolean isPlce){
		X = xPos;
		Y = yPos;
		isWalkable = isWalk;
		isPlace = isPlce;
		
		// Update edges where this node is the from node
		for (Edge e : edges){
			e.setDistance(getDistance(this, e.getTo()));
		}
		
		// Update all edges where this node is the to node
		LinkedList<AbsNode> connectingNodeList = new LinkedList<AbsNode>();
		for (Edge e : edges){
			connectingNodeList.add(e.getTo());
		}
		
		for (AbsNode n : connectingNodeList){
			for (Edge e : n.getEdges()){
				if (e.getTo() == this){
					e.setDistance(getDistance(this, n));
				}
			}
		}
	}

	public String getName() {
		// TODO Auto-generated method stub
		return "";
	}
	
	public int getDistance(AbsNode n1, AbsNode n2){
		return (int) Math.sqrt((Math.pow(((int)n1.getX() - (int)n2.getX()), 2)) + (Math.pow(((int)n1.getY() - (int)n2.getY()), 2)));
	}
}
