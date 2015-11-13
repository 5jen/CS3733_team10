package node;

public class Place extends AbsNode {
	
	private String name;
	
	public Place(int valX, int valY, boolean isWalk, String nameOfPlace){
		super(valX, valY, isWalk);
		name = nameOfPlace;
	}
	
	public String getName(){
		return name;
	}
}
