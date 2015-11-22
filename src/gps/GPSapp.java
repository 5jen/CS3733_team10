package gps;

import io.JsonParser;
import TurnByTurn.stepIndicator;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;
import node.*;

import java.io.File;
import java.util.LinkedList;


public class GPSapp extends Application{
	public static void main(String[] args) {
        launch(args);
    }
	
	//Load up the JSON data and create the nodes for the map
	JsonParser json = new JsonParser();
	LinkedList<AbsNode> nodeList = JsonParser.getJsonContent("Graphs/AK1.json");
	LinkedList<EdgeDataConversion> edgeListConversion = JsonParser.getJsonContentEdge("Graphs/AK1Edges.json");
	LinkedList<Edge> edgeList = convertEdgeData(edgeListConversion);	
	Canvas canvas = new Canvas(800, 650);
    GraphicsContext gc = canvas.getGraphicsContext2D();
	boolean start, end = false;
	String startNode, endNode;
	Graph graph = new Graph();
	
    @Override
    public void start(Stage primaryStage) {
    	
    	final Pane root = new Pane(); 
    	System.out.println("size of nodelist: " + nodeList.size());
    	//Create a map selection drop down menu
    	final VBox mapSelectionBoxV = new VBox(5);
    	final Label mapSelectorLabel = new Label("Choose map");
    	mapSelectorLabel.setTextFill(Color.WHITE);
    	final HBox mapSelectionBoxH = new HBox(5);
    	ObservableList<String> mapOptions = FXCollections.observableArrayList("AK1", "AK2", "AK3");
    	final ComboBox<String> mapSelector = new ComboBox<String>(mapOptions);
    	final Button LoadMapButton = new Button("Load Map");
    	mapSelectionBoxH.getChildren().addAll(mapSelector, LoadMapButton);
    	mapSelectionBoxV.setLayoutX(820);
    	mapSelectionBoxV.setLayoutY(20);
    	mapSelectionBoxV.getChildren().addAll(mapSelectorLabel, mapSelectionBoxH);
    	
    	
    	//Create a label and box for warnings, ie when the coordinates are outside the name
    	final HBox warningBox = new HBox(0); 
    	final Label warningLabel = new Label("");
    	warningLabel.setTextFill(Color.WHITE);
    	warningBox.setLayoutX(10);
    	warningBox.setLayoutY(680);
    	warningBox.getChildren().addAll(warningLabel); 
    	
        
      //Create the START selection drop down menu
        final Button findRouteButton = new Button("Find Route");
    	final VBox LocationSelectionBoxV = new VBox(5);
    	final Label LocationSelectorLabelSTART = new Label("Start");
    	LocationSelectorLabelSTART.setTextFill(Color.WHITE);
    	final Label LocationSelectorLabelDEST = new Label("Destination");
    	LocationSelectorLabelDEST.setTextFill(Color.WHITE);
    	final HBox LocationSelectionBoxHLABEL = new HBox(185);
    	final HBox LocationSelectionBoxH = new HBox(60); 
    	ObservableList<String> LocationOptions = FXCollections.observableArrayList();
    	//Initialize the Drop down menu for inital Map
    	for(int i = 0; i < nodeList.size() ; i ++){ 
    		if(nodeList.get(i).getIsPlace())
    			LocationOptions.add((nodeList.get(i)).getName());
        }
    	final ComboBox<String> LocationSelectorSTART = new ComboBox<String>(LocationOptions);
    	final ComboBox<String> LocationSelectorDEST = new ComboBox<String>(LocationOptions);
    	LocationSelectorSTART.setPrefWidth(150);
    	LocationSelectorDEST.setPrefWidth(150);
    	LocationSelectorSTART.setVisibleRowCount(8);
    	LocationSelectorDEST.setVisibleRowCount(8);
    	LocationSelectionBoxHLABEL.getChildren().addAll(LocationSelectorLabelSTART, LocationSelectorLabelDEST);
    	LocationSelectionBoxH.getChildren().addAll(LocationSelectorSTART, LocationSelectorDEST, findRouteButton);
    	LocationSelectionBoxV.setLayoutX(10);
    	LocationSelectionBoxV.setLayoutY(620);
    	LocationSelectionBoxV.getChildren().addAll(LocationSelectionBoxHLABEL, LocationSelectionBoxH);
  
        //Create the map image
        File mapFile = new File("CS3733_Graphics/AK1.png");
        mapSelector.setValue("AK1"); // Default Map when App is opened
        Image mapImage = new Image(mapFile.toURI().toString());
        ImageView imageView = new ImageView();
        imageView.setImage(mapImage);
        imageView.setLayoutX(0);  
        imageView.setLayoutY(0);
        
        //create background
        File backgroundFile = new File("CS3733_Graphics/BlueBackground.jpg");
        Image bgImage = new Image(backgroundFile.toURI().toString());
        ImageView bgView = new ImageView();
        bgView.setImage(bgImage);
        bgView.setLayoutX(0);  
        bgView.setLayoutY(0);
        
        //Create a keyimage to place the map key on screen
    	File keyFile = new File("CS3733_Graphics/Key.png");
        Image keyImage = new Image(keyFile.toURI().toString());
        
        ImageView imageViewKey = new ImageView();
        imageViewKey.setImage(keyImage);
        
        imageViewKey.setLayoutX(830);  
        imageViewKey.setLayoutY(530);
        
        //Add images to the screen
        root.getChildren().add(bgView); //Must add background image first!
        root.getChildren().add(mapSelectionBoxV);
        root.getChildren().add(LocationSelectionBoxV);
        root.getChildren().add(imageView);
        root.getChildren().add(imageViewKey);
        
        //Border the map app
       // drawMapBorder(gc, root); //TO USE< CREATE A NEW CANVAS FOR THIS..
        
        
        graph = createGraph(graph, nodeList, edgeList);
        drawPlaces(nodeList, root, LocationSelectorSTART, LocationSelectorDEST);
        
        
        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	//remove the canvas
            	root.getChildren().remove(canvas);
            }
        });
        
        //Add actions to the Load Map button
        LoadMapButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	
            	root.getChildren().remove(imageView); //remove current map, then load new one
            	nodeList.clear();
           		edgeList.clear();
            	nodeList = JsonParser.getJsonContent("Graphs/" + (String) mapSelector.getValue() + ".json");
            	//edgeList = json.getJsonContentEdge("Graphs/" + (String) mapSelector.getValue() + "Edges.json");
            	edgeListConversion = JsonParser.getJsonContentEdge("Graphs/" + (String) mapSelector.getValue() + "Edges.json");
            	edgeList = convertEdgeData(edgeListConversion);
            	
            	graph = createGraph(new Graph(), nodeList, edgeList);
            	
            	File newMapFile = new File("CS3733_Graphics/" + (String) mapSelector.getValue() + ".png"); //MUST ADD png extension!
            	Image mapImage = new Image(newMapFile.toURI().toString());
                ImageView imageView = new ImageView();
                imageView.setImage(mapImage);
                imageView.setLayoutX(0);  
                imageView.setLayoutY(0);
                imageView.resize(800, 600); //incase map is not already scaled perfectly
                root.getChildren().add(imageView); 
                //add node buttons to the screen and populates the drop down menus
                LocationOptions.clear();
                for(int i = 0; i < nodeList.size() - 1; i ++){ 
                	if(nodeList.get(i).getIsPlace())
                		LocationOptions.add(((Place)nodeList.get(i)).getName());
                }
                //drawMapBorder(gc, root);
                graph = createGraph(graph, nodeList, edgeList);
                drawPlaces(nodeList, root, LocationSelectorSTART, LocationSelectorDEST);
            }
        });
        
        //Add button actions
        findRouteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	root.getChildren().remove(canvas); //remove old path
            	gc.clearRect(0, 0, 800, 600);
            	
            	// Need to string compare from 
            	Place startPlace = new Place(0, 0, false, "");
            	Place endPlace = new Place(0, 0, false, "");
            	for(int i = 0; i < nodeList.size(); i ++){ 
                	if((nodeList.get(i)).getName().equals(LocationSelectorSTART.getValue())) {
                		startPlace = ((Place)nodeList.get(i));
                	}
                	if((nodeList.get(i)).getName().equals(LocationSelectorDEST.getValue())) {
                		endPlace = ((Place)nodeList.get(i));
                	}
                }
            	System.out.println("start: " + startPlace.getName());
            	System.out.println("end: " + endPlace.getName());
            	
            	// Call findRoute on 2 nodes, returns a LinkedList<AbsNode>
            	//create graph and add nodes
            	
                LinkedList<AbsNode> route = new LinkedList<AbsNode>();
                route = graph.findRoute(startPlace, endPlace); 
                //TODO add turn by turn insturctions right here

                System.out.println(" " +route);
                for(int i = 0; i < route.size(); i++){
                	System.out.println("Route node: " + i + " , " + route.get(i).getName());
                }

				stepIndicator s = new stepIndicator(route);
				LinkedList<String> steps = new LinkedList<String>(s.lInstructions());


                drawRoute(gc, route);
                root.getChildren().add(canvas);

                //for now, clear the route
                route = new LinkedList<AbsNode>();
            }
        });
        
  
        primaryStage.setScene(new Scene(root, 1050, 700));  
        primaryStage.show();  
    }  
    
   /* private void drawMapBorder(GraphicsContext gc, Pane root) {
    	root.getChildren().remove(canvas);
    	gc.setStroke(Color.BLACK);
        gc.setLineWidth(4);
        gc.strokeLine(2, 2, 2, 602);//left
  		gc.strokeLine(2, 602, 798, 602);//bottom
  		gc.strokeLine(798, 602, 798, 2);//right
  		gc.strokeLine(2, 2, 798, 2);//top
  		root.getChildren().add(canvas);
		
	}*/

	private Graph createGraph(Graph g, LinkedList<AbsNode> nodes, LinkedList<Edge> edges){
    	g.setNodes(nodes);
    	//g.setEdges(edges);
    	
    	for(int i = 0; i < g.getNodes().size(); i++){
    		//System.out.println("Node"+i+" = "+g.getNodes().get(i));
    	}
    	for(int i = 0; i < edges.size(); i++){
    		g.addEdge(edges.get(i).getFrom(), edges.get(i).getTo());
    		//System.out.println("Edge"+i+" = from: "+g.getEdges().get(i).getFrom().getName()+", to: "+g.getEdges().get(i).getTo().getName());
    	}
    	
    	return g;
    }
    
    
    private void drawPlaces(LinkedList<AbsNode> nodes, Pane root, ComboBox<String> LocationSelectorSTART, ComboBox<String> LocationSelectorDEST){
    	int i;
    	for(i = 0; i < nodes.size(); i ++){ 
    		if(nodes.get(i).getIsPlace()){
        		Button newNodeButton = new Button("");
            	newNodeButton.setStyle(
                        "-fx-background-radius: 5em; " +
                        "-fx-min-width: 15px; " +
                        "-fx-min-height: 15px; " +
                        "-fx-max-width: 15px; " +
                        "-fx-max-height: 15px;"
                );
            	newNodeButton.relocate(nodes.get(i).getX(), nodes.get(i).getY());
            	AbsNode newNode = nodes.get(i);
            	newNodeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                    	if (!start){
                    		if(newNode.getIsPlace()) startNode = ((Place) newNode).getName();
                    		LocationSelectorSTART.setValue(startNode);
                    		start = true;
                    	}
                    	else if(!end){
                    		if(newNode.getIsPlace()) endNode = ((Place) newNode).getName();
                    		LocationSelectorDEST.setValue(endNode);
                    		start = false;
                    		end = false;
                    	}
                    }
                });
            	root.getChildren().add(newNodeButton);
    		} else if(!nodes.get(i).getIsPlace()){
    			//Do nothing
    		}
	  		
    	}
    }
    
    private void drawRoute(GraphicsContext gc, LinkedList<AbsNode> route) {
    	 Color customBlue = Color.web("0x00b3fd"); 
    	 
    	 gc.setLineCap(StrokeLineCap.ROUND);
    	//iterate through the route drawing a connection between nodes
    	for(int i = 1; i < route.size(); i ++){
    		gc.setLineWidth(5);
    		
            gc.setStroke(Color.BLACK);
	  		gc.strokeLine(route.get(i-1).getX()+8, route.get(i-1).getY()+8, route.get(i).getX()+8,route.get(i).getY()+8);
            gc.setLineWidth(3);
            
            gc.setStroke(customBlue);
	  		gc.strokeLine(route.get(i-1).getX()+8, route.get(i-1).getY()+8, route.get(i).getX()+8,route.get(i).getY()+8);
	  		
    	}
    }
    
    private LinkedList<Edge> convertEdgeData(LinkedList<EdgeDataConversion> edgeData) {
    	LinkedList<Edge> edgeList = new LinkedList<Edge>();
    	AbsNode fromNode = new Place(0, 0, false, "");
    	AbsNode toNode = new Place(0, 0, false, "");
    	
    	//iterate through the edges 
    	for(int i = 0; i < edgeData.size(); i ++){
    		//System.out.println("Edge Iterator: " + i);
    		//iterate throught he nodelist to find the matching node
    		for(int j = 0; j < nodeList.size(); j ++){
        		//System.out.println("Node Iterator: " + j + ", x valFrom: " + nodeList.get(j).getX() + " =? " + nodeList.get(j).getName());

    			//check difference between place and node..
    			if(nodeList.get(j).getIsPlace()){
    				if(edgeListConversion.get(i).getFrom().equals((nodeList.get(j)).getName())){
    					fromNode = (Place)nodeList.get(j);
    				}
    				if(edgeListConversion.get(i).getTo().equals((nodeList.get(j)).getName())){
    					toNode = (Place)nodeList.get(j);
    				}
    			}else{
    				if(edgeListConversion.get(i).getFrom().equals((nodeList.get(j)).getName())){
    					fromNode = nodeList.get(j);
    				}
    				if(edgeListConversion.get(i).getTo().equals(( nodeList.get(j)).getName())){
    					toNode = nodeList.get(j);
    				}
    			}
    			
    		}
    		Edge newEdge = new Edge(fromNode, toNode, edgeListConversion.get(i).getDistance());
			edgeList.add(newEdge);
    	}
    	
    	return edgeList;
    }
       
}
