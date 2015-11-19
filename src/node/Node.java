package node;

public class Node extends AbsNode {

	private String name;
	
	public Node(int valX, int valY, boolean isWalk, String refPoint){
		super(valX, valY, isWalk, false);
		name = refPoint;
	}
	
	public String getName(){
		return name;
	}
}
