package node;

public class Node extends AbsNode {

	private String referencePoint;
	
	public Node(int valX, int valY, boolean isWalk, String refPoint){
		super(valX, valY, isWalk);
		referencePoint = refPoint;
	}
	
	public String getReferencePoint(){
		return referencePoint;
	}
}
