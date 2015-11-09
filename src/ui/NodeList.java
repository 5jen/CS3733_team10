package ui;

public class NodeList {

	Node daka;
	Node reccenter;
	Node harrington;
	Node higginscorner;
	Node campuscenter;
	Node fountain;
	Node library;
	
	public NodeList(){
		 //Offset for node image
        int offsetX = 9;
        int offsetY = 29;
		this.daka = new Node(150-offsetX, 395-offsetY, "daka");
		this.reccenter = new Node(165-offsetX, 340-offsetY, "reccenter");
		this.harrington = new Node(190-offsetX, 340-offsetY, "harrington");
		this.higginscorner = new Node(250-offsetX, 350-offsetY, "higginscorner");
		this.campuscenter = new Node(260-offsetX, 315-offsetY, "campuscenter");
		this.fountain = new Node(297-offsetX, 322-offsetY, "fountain");
		this.library = new Node(355-offsetX, 332-offsetY, "library");
	}
	
	
}
