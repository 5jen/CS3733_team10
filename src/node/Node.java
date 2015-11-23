package node;

import java.util.LinkedList;

public class Node {

	/*
	 * To add:
	 * 	global x, y
	 *  building name
	 */
	private int X;
	private int Y;
	private int Z;
	private double globalX;
	private double globalY;
	private String building;
	private String name;
	private boolean isWalkable;
	private boolean isPlace;
	private String type;
	private Node parent = null;
	private double cost = 0;
	private LinkedList<Edge> edges = new LinkedList<Edge>();
	
	public Node(int valX, int valY, int valZ, String n, String b, boolean isWalk, boolean place, String t){
		X = valX;
		Y = valY;
		Z = valZ;
		building = b;
		name = n;
		isWalkable = isWalk;
		isPlace = place;
		type = t;
	}
	
	public String getName(){ return this.name; }
	public void setName(String n){ this.name = n; }
	
	public int getX(){ return X; }
	public void setX(int x){ this.X = x; }
	
	public int getY(){ return Y; }
	public void setY(int y){ this.Y = y; }
	
	public int getZ(){ return Z; }
	public void setZ(int z){ this.Z = z; }
	
	public double getGlobalX(){ return globalX; }
	public void setGlobalX(double d){ this.globalX = d; }
	
	public double getGlobalY(){ return globalY; }
	public void setGlobalY(double y){ this.globalY = y; }
	
	public String getBuilding(){ return this.building; }
	public void setBuilding(String b){ this.building = b; }
	
	public boolean getIsWalkable(){	return isWalkable; }
	public void setIsWalkable(boolean n){ this.isWalkable = n; }
	
	public boolean getIsPlace(){ return isPlace; }
	public void setIsPlace(boolean n){ this.isPlace = n; }
	
	public String getType(){ return type; }
	public void setType(String n){ this.type = n; }
	

	public void setEdges(Edge anEdge){ edges.add(anEdge); }
	
	public LinkedList<Edge> getEdges(){ return edges; }
	
	public Node getParent(){ return parent; }
	
	public void setParent(Node parentToAdd){ parent = parentToAdd; }
	
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
		LinkedList<Node> connectingNodeList = new LinkedList<Node>();
		for (Edge e : edges){
			connectingNodeList.add(e.getTo());
		}

		for (Node n : connectingNodeList){
			for (Edge e : n.getEdges()){
				if (e.getTo() == this){
					e.setDistance(getDistance(this, n));
				}
			}
		}
	}
	
	public void printConnectingNodes(){
		for (Edge e : edges){
			String sf = String.format("X:%d, Y:%d", e.getTo().getX(), e.getTo().getY());
			System.out.println(sf);
		}
	}

	public int getDistance(Node n1, Node n2){
		return (int) Math.sqrt((Math.pow(((int)n1.getX() - (int)n2.getX()), 2)) + (Math.pow(((int)n1.getY() - (int)n2.getY()), 2)));
	}

}
