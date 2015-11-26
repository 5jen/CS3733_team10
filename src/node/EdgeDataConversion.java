package node;

public class EdgeDataConversion {

	private String from;
	private String to;
	private int distance;
	
	public EdgeDataConversion(String f, String t, int d){
		this.from = f;
		this.to = t;
		this.distance = d;
	}
	
	public String getFrom(){
		return this.from;
	}
	
	public String getTo(){
		return this.to;
	}
	
	public int getDistance(){
		return this.distance;
	}
}
