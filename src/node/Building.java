package node;

public class Building {
	
	private String name;
	private int rotationalConstant;
	//private int localNodePos;
	private int globalToLocalOffset;
	private int conversionRatio;
	
	public Building(String n, int rc, int gtlc, int cr){
		name = n;
		rotationalConstant = rc;
		globalToLocalOffset = gtlc;
		conversionRatio = cr;
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
	
	public int getGlobalToLocalOffset(){
		return globalToLocalOffset;
	}
	public void setGlobalToLocalOffset(int x){
		this.globalToLocalOffset = x;
	}
	
	public int getConversionRatio(){
		return conversionRatio;
	}
	public void setConversionRatio(int x){
		this.conversionRatio = x;
	}
	

}
