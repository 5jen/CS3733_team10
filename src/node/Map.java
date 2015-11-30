package node;

public class Map {

	private String name;
	private String buildingName;
	private String mapPath;
	private String nodesPath;
    private String edgesPath;
	private double rotationalConstant;
	private int globalToLocalOffsetX;
	private int globalToLocalOffsetY;
	private double conversionRatio;
	private int floor;
	
	public Map(String n, String mP, String nP, String eP, double rc, int gtlcX, int gtlcY, double cr, int nf){
		name = n;
        mapPath = mP;
        nodesPath = nP;
        edgesPath = eP;
		rotationalConstant = rc;
		globalToLocalOffsetX = gtlcX;
		globalToLocalOffsetY = gtlcY;
		conversionRatio = cr;
		floor = nf;
	}

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getMapPath() {
        return mapPath;
    }

    public void setMapPath(String mapPath) {
        this.mapPath = mapPath;
    }

    public String getNodesPath() {
        return nodesPath;
    }

    public void setNodesPath(String nodesPath) {
        this.nodesPath = nodesPath;
    }

    public String getEdgesPath() {
        return edgesPath;
    }

    public void setEdgesPath(String edgesPath) {
        this.edgesPath = edgesPath;
    }

    public String getName(){
		return name;
	}
	
	public double getRotationalConstant(){
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
	
	public double getConversionRatio(){
		return conversionRatio;
	}
	public void setConversionRatio(int x){
		this.conversionRatio = x;
	}
	
	public int getFloor(){
		return floor;
	}
	

}