package node;

public class Building {

	private String name;
	private int rotationalConstant;
	//private int localNodePos;
	private int globalToLocalOffsetX;
	private int globalToLocalOffsetY;
	private int conversionRatio;
	private int numFloors;
	
	public Building(String n, int rc, int gtlcX,  int gtlcY, int cr, int nf){
		name = n;
		rotationalConstant = rc;
		globalToLocalOffsetX = gtlcX;
		globalToLocalOffsetY = gtlcY;
		conversionRatio = cr;
		numFloors = nf;
	}
	
	public String getName(){
		return name;
	}
	
	public int getRotationalConstant(){
		return rotationalConstant;
	}
	public void setRotationalConstant(int x){
		this.rotationalConstant = x;
	}
	
	public int getGlobalToLocalOffsetX(){
		return globalToLocalOffsetX;
	}
	public void setGlobalToLocalOffset(int x){
		this.globalToLocalOffsetX = x;
	}
	
	public int getGlobalToLocalOffsetY(){
		return globalToLocalOffsetY;
	}
	public void setGlobalToLocalOffsetY(int x){
		this.globalToLocalOffsetY = x;
	}
	
	public int getConversionRatio(){
		return conversionRatio;
	}
	public void setConversionRatio(int x){
		this.conversionRatio = x;
	}
	
	public int getNumFloors(){
		return numFloors;
	}
	

}
