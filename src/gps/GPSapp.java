package gps;

import java.io.File;
import java.util.LinkedList;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import node.Building;
import node.Edge;
import node.Node;
import node.EdgeDataConversion;
import node.Graph;
import node.Map;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import io.JsonParser;


public class GPSapp extends Application{
	public static void main(String[] args) {
        launch(args);
    }
	
	//Load up the JSON data and create the nodes for the map
	JsonParser json = new JsonParser();
	LinkedList<Node> nodeList = JsonParser.getJsonContent("Graphs/CampusMap.json");
	LinkedList<EdgeDataConversion> edgeListConversion = JsonParser.getJsonContentEdge("Graphs/CampusMapEdges.json");
	LinkedList<Edge> edgeList = convertEdgeData(edgeListConversion);	
	Canvas canvas = new Canvas(800, 650);
    GraphicsContext gc = canvas.getGraphicsContext2D();
	boolean start, end = false;
	String startNode, endNode;
	Graph graph = new Graph();
	ObservableList<String> LocationOptions = FXCollections.observableArrayList();
	ListView<String> StartList = new ListView<String>();
    ListView<String> DestList = new ListView<String>();
    TextField StartText = new TextField();
	TextField DestText = new TextField();
	int k = 0; // Set Max zoom Variable
	
	
	//Groups to attach layered map 
	//Group g1 = new Group(), g2 = new Group(), g3 = new Group();
	
	ObservableList<String> mapOptions = FXCollections.observableArrayList("CampusMap", "AK1", "AK2", "AK3");
	final ComboBox<String> mapSelector = new ComboBox<String>(mapOptions);

	//Building Buildings with their content
  	Building AtwaterKent = new Building("Atwater Kent");
  	Building BoyntonHall = new Building("Boynton Hall");
  	Building CampusCenter = new Building("Campus Center");
  	Building GordonLibrary = new Building("Gordon Library");
  	Building HigginsHouse = new Building("Higgins House");
  	Building ProjectCenter = new Building("Project Center");
  	Building StrattonHall = new Building("Stratton Hall");
	
	//Map Buildings with their content
	Map AtwaterKent1 = new Map("Atwater Kent 1", "AK", "CS3733_Graphics/AK1.png", "Graphs/AK1.json", "Graphs/AK1Edges.json", -1.308, 1548, 594, 1, 1);
	Map AtwaterKent2 = new Map("Atwater Kent 2", "AK", "CS3733_Graphics/AK2.png", "Graphs/AK2.json", "Graphs/AK2Edges.json", -1.308, 1548, 594, 1, 2);
	Map AtwaterKent3 = new Map("Atwater Kent 3", "AK", "CS3733_Graphics/AK3.png", "Graphs/AK3.json", "Graphs/AK3Edges.json", -1.308, 1548, 594, 1, 3);
  
	Map GordonLibrary1 = new Map("Gordon Library SB", "GL", "CS3733_Graphics/GLSB.png", "Graphs/GLSB.json", "Graphs/GLSBEdges.json", -1.744, 1668, 726, 1, -1);
	Map GordonLibrary2 = new Map("Gordon Library B",  "GL", "CS3733_Graphics/GLB.png", "Graphs/GLB.json", "Graphs/GLBEdges.json", -1.744, 1668, 726, 1, 0);
	Map GordonLibrary3 = new Map("Gordon Library 1",  "GL", "CS3733_Graphics/GL1.png", "Graphs/GL1.json", "Graphs/GL1Edges.json", -1.744, 1668, 726, 1, 1);
	
	Map BoyntonHall1 = new Map("Boynton Hall 1", "BH","CS3733_Graphics/BH1.png","Graphs/BH1.json","Graphs/BH1Edges.json", -1.483, 1496, 991, 1, 1);
	
	Map CampusCenter1 = new Map("Campus Center 1", "CC", "CS3733_Graphics/CC1.png", "Graphs/CC1.json", "Graphs/CC1.json", 1.396, 1175, 670, 1, 1);
	Map CampusCenter2 = new Map("Campus Center 2", "CC", "CS3733_Graphics/CC2.png", "Graphs/CC2.json", "Graphs/CC2.json", 1.396, 1175, 670, 1, 2);

	Map HigginsHouse1 = new Map("Higgins House 1", "HH", "CS3733_Graphics/HH1.png", "Graphs/HH1.json", "Graphs/HH1.json", -2.355, 1200, 451, 1, 1);
	Map HigginsHouse2 = new Map("Higgins House 2", "HH", "CS3733_Graphics/HH2.png", "Graphs/HH2.json", "Graphs/HH2.json", -2.355, 1200, 451, 1, 2);
	
	Map ProjectCenter1 = new Map("Project Center 1", "PC", "CS3733_Graphics/PC1.png", "Graphs/PC1.json", "Graphs/PC1.json", 3.053, 1228, 772, 1, 1);
	Map ProjectCenter2 = new Map("Project Center 2", "PC", "CS3733_Graphics/PC2.png", "Graphs/PC2.json", "Graphs/PC2.json", 3.053, 1228, 772, 1, 2);

	Map StrattonHall1 = new Map("Stratton Hall 1", "SH", "CS3733_Graphics/SH1.png", "Graphs/SH1.json", "Graphs/SH1.json", 1.483, 1364, 898, 1, 1);
	Map StrattonHall2 = new Map("Stratton Hall 2", "SH", "CS3733_Graphics/SH2.png", "Graphs/SH2.json", "Graphs/SH2.json", 1.483, 1364, 898, 1, 2);
	Map StrattonHall3 = new Map("Stratton Hall 3", "SH", "CS3733_Graphics/SH3.png", "Graphs/SH3.json", "Graphs/SH3.json", 1.483, 1364, 898, 1, 3);
	Map StrattonHall4 = new Map("Stratton Hall 4", "SH", "CS3733_Graphics/SH4.png", "Graphs/SH4.json", "Graphs/SH4.json", 1.483, 1364, 898, 1, 4);

	
	final Label buildingSelected = new Label();
	
	//set perspective transformations to all 3 groups
	PerspectiveTransform pt = new PerspectiveTransform();
	final DropShadow shadow = new DropShadow();
	
	Path g1path = new Path();
	 MoveTo g1moveTo = new MoveTo();
	 LineTo g1lineTo = new LineTo();
	
    @Override
    public void start(Stage primaryStage) {
    	
    	final Pane root = new Pane();
    	
    	//Add Maps to buildings
    	AtwaterKent.addMap(AtwaterKent1);
    	AtwaterKent.addMap(AtwaterKent2);
    	AtwaterKent.addMap(AtwaterKent3);
    	
    	GordonLibrary.addMap(GordonLibrary1);
    	GordonLibrary.addMap(GordonLibrary2);
    	GordonLibrary.addMap(GordonLibrary3);
    	
    	//BoyntonHall.addMap(BoyntonHall1);
    	
    	CampusCenter.addMap(CampusCenter1);
    	CampusCenter.addMap(CampusCenter2);
    	
    	HigginsHouse.addMap(HigginsHouse1);
    	HigginsHouse.addMap(HigginsHouse2);
    	
    	StrattonHall.addMap(StrattonHall1);
    	StrattonHall.addMap(StrattonHall2);
    	StrattonHall.addMap(StrattonHall3);
    	StrattonHall.addMap(StrattonHall4);
    	
    	ProjectCenter.addMap(ProjectCenter1);
    	ProjectCenter.addMap(ProjectCenter2);

    	
    	//
    	double width = 80;
	    double height = 60;
    	pt = setCorners(pt, width, height);
    	shadow.setInput(pt);
    	
    	
    	//Create a map selection drop down menu
    	final VBox mapSelectionBoxV = new VBox(5);
    	final Label mapSelectorLabel = new Label("Choose map");
    	mapSelectorLabel.setTextFill(Color.WHITE);
    	final HBox mapSelectionBoxH = new HBox(5);
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
    	
    	
    	//Initialize the Drop down menu for initial Map
    	for(int i = 0; i < nodeList.size() ; i ++){ 
    		if(nodeList.get(i).getIsPlace())
    			LocationOptions.add((nodeList.get(i)).getName());
        }
    	
    	//Find Route Button
    	final Button findRouteButton = new Button("Find Route");
    	findRouteButton.relocate(650, 610);

    	//Searchable text boxes
    	VBox StartSearch = new VBox();
        VBox DestSearch = new VBox();
        StartText.setPromptText("Start");
        DestText.setPromptText("Destination");        
        StartList.setMaxHeight(75);
        DestList.setMaxHeight(75);
        StartList.setItems(LocationOptions);      
        DestList.setItems(LocationOptions);
        StartSearch.relocate(300, 610);
        StartSearch.getChildren().addAll(DestText, DestList);
        DestSearch.relocate(20, 610);
        DestSearch.getChildren().addAll(StartText, StartList);
        StartList.setOpacity(0);
        DestList.setOpacity(0);
        
        
      
  
        //Create the map image
        File mapFile = new File("CS3733_Graphics/CampusMap.png");
        mapSelector.setValue("CampusMap"); // Default Map when App is opened
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
        //root.getChildren().add(imageView);
        root.getChildren().add(imageViewKey);
        root.getChildren().add(StartSearch);
        root.getChildren().add(DestSearch);
        root.getChildren().add(findRouteButton);
        
        
        
        //Removes top bar!! Maybe implement a custom one to look better
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        
        
        graph = createGraph(graph, nodeList, edgeList);
        Pane NodePane = new Pane();

	    drawNodes(nodeList, NodePane, root, StartText, DestText,imageView);

        final Group group = new Group(imageView, canvas, NodePane);
	    Parent zoomPane = createZoomPane(group);
	    
	    //add to load map...
	    highLight(NodePane, imageView, root);
	    
	    root.getChildren().add(zoomPane);
	    
	    //Display building selected
    	buildingSelected.setText("...");
    	buildingSelected.setLayoutX(820);
    	buildingSelected.setLayoutY(300);
		root.getChildren().add(buildingSelected);
	    
	    
        //Add actions to the Load Map button
        LoadMapButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	loadMap( root,  zoomPane,  imageView);
            	
        	    
            }
        });
        
        //Add button actions
        findRouteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	gc.clearRect(0, 0, 800, 600); // Clears old path
            	if (StartText.getText().equals("")|| DestText.getText().equals("")) {
            		
            	} else {
            		root.getChildren().remove(zoomPane);
            	
                	// Need to string compare from 
                	Node startPlace = new Node(0, 0, 0, "","", false, false, "");
                	Node endPlace = new Node(0, 0, 0, "","", false, false, "");
                	for(int i = 0; i < nodeList.size(); i ++){ 
                    	if((nodeList.get(i)).getName().equals(StartText.getText())) {
                    		startPlace = (nodeList.get(i));
                    	}
                    	if((nodeList.get(i)).getName().equals(DestText.getText())) {
                    		endPlace = (nodeList.get(i));
                    	}
                    }
                	System.out.println("start: " + startPlace.getName());
                	System.out.println("end: " + endPlace.getName());
                	
                    LinkedList<Node> route = new LinkedList<Node>();
                    route = graph.findRoute(startPlace, endPlace); 
                    
                    System.out.println(" " +route);
                    for(int i = 0; i < route.size(); i++){
                    	System.out.println("Route node: " + i + " , " + route.get(i).getName());
                    }
                    
                    Pane NodePane = new Pane();
                    drawNodes(nodeList, NodePane, root, StartText, DestText, imageView);
                    drawRoute(gc, route);
                    
                    final Group group = new Group(imageView, canvas, NodePane);
            	    Parent zoomPane = createZoomPane(group);
            	    root.getChildren().add(zoomPane);

                    route = new LinkedList<Node>();
            	}
            }
        });
        
  
        primaryStage.setScene(new Scene(root, 1050, 700));  
        primaryStage.show();
        
        DestText.textProperty().addListener(
                new ChangeListener<Object>() {
                    public void changed(ObservableValue<?> observable2, 
                                        Object oldVal2, Object newVal2) {
                        handleSearchByKeyDest((String)oldVal2, (String)newVal2);
                    }
                });
            
            StartText.textProperty().addListener(
                    new ChangeListener<Object>() {
                        public void changed(ObservableValue<?> observable, 
                                            Object oldVal, Object newVal) {
                            handleSearchByKeyStart((String)oldVal, (String)newVal);
                        }
                    });
            
            DestText.focusedProperty().addListener(new ChangeListener<Boolean>() {
            	public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
            		if (newPropertyValue) {
            			DestList.setOpacity(100);
                    }
                    else {
                    	DestList.setOpacity(0);
                    }
            	}
            });
            
            StartText.focusedProperty().addListener(new ChangeListener<Boolean>() {
            	public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
            		if (newPropertyValue) {
            			StartList.setOpacity(100);
                    }
                    else {
                    	StartList.setOpacity(0);
                    }
            	}
            });
                    
            StartList.setOnMouseClicked(new EventHandler<MouseEvent>() {

    			public void handle(MouseEvent arg0) {
    				StartText.setText((String) StartList.getSelectionModel().getSelectedItem());
    				
    			}
            });
            DestList.setOnMouseClicked(new EventHandler<MouseEvent>() {

    			public void handle(MouseEvent arg0) {
    				DestText.setText((String) DestList.getSelectionModel().getSelectedItem());
    				
    			}
            });
    }  
    
    
    private void getMapSelector(Building building, Pane root, ImageView imageView) {
	    root.getChildren().remove(canvas);
	    
	    //attach background over map
	    File newBackground = new File("CS3733_Graphics/white.png");
     	final Image backgroundImage = new Image(newBackground.toURI().toString());
         imageView.setImage(backgroundImage);
   	     Pane NodePane = new Pane();
         gc.clearRect(0, 0, 800, 600);
         drawNodes(nodeList, NodePane, root, StartText, DestText, imageView);
         
                       
         final Group group = new Group(imageView, canvas, NodePane);
 	    Parent zoomPane = createZoomPane(group);
 	    root.getChildren().add(zoomPane);
 	    
 	   //Attach 3D image of building
 	    //BUILDING UST HAVE MAPS IN IT OTHERWISE ERRORS
 		File buildingFile = new File("CS3733_Graphics/BuildingImages/"+building.getMaps().get(0).getInitials()+".png");
 		final Image b = new Image(buildingFile.toURI().toString());
		final ImageView bImage = new ImageView();
		bImage.setImage(b);
		bImage.setLayoutX(400);
		bImage.setLayoutY(150);
		root.getChildren().add(bImage);
		
		//Attach Building label
		final Label BuildingNameLabel = new Label(building.getName());
		BuildingNameLabel.setTextFill(Color.BLACK);
		BuildingNameLabel.setFont(Font.font ("manteka", 30));
		BuildingNameLabel.setLayoutX(20);
		BuildingNameLabel.setLayoutY(560);
    	root.getChildren().addAll(BuildingNameLabel);
    	
//Attach Building label
    	
    	final Button BackButton = new Button("Back");
    	BackButton.setTextFill(Color.BLACK);
    	BackButton.setFont(Font.font ("manteka", 30));
    	BackButton.setLayoutX(650);
    	BackButton.setLayoutY(530);
    	root.getChildren().addAll(BackButton);
    	BackButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
    			public void handle(MouseEvent event) {
    				//b = null;
    	    		//bImage = null;
    	    		//backgroundImage = null;
    				root.getChildren().remove(BackButton);
    				loadMap(root, zoomPane, imageView);
    				
    			}
    	});
    	
 	    
    	//Load the layered Maps
	    //convert to for loop
	    int currentFloor = 0;
    	for(int i = 1; i <= building.getNumMaps(); i++){
    		currentFloor = i-1;
    		System.out.println("CS3733_Graphics/LayerMap/"+building.getName()+currentFloor+"L.png");
    		File mapFile = new File("CS3733_Graphics/LayerMap/"+building.getMaps().get(currentFloor).getInitials()+i+"L.png");//Change back to above
    		Image image = new Image(mapFile.toURI().toString());
    		ImageView mapImageView = new ImageView();
    		mapImageView.setImage(image);

    		
   	     	Group g1 = new Group();
   	     	g1.setEffect(pt);
   	     	g1.getChildren().add(mapImageView);
   	     	
   	     	//used inside action scope
   	     	int floor = currentFloor;
   	     	//Add actions to each of the layered map buttons
   	     	g1.setOnMouseClicked(new EventHandler<MouseEvent>() {
   	     			public void handle(MouseEvent event) {
   	     				BuildingNameLabel.setText(building.getName()+" " + floor);
   	     				//call load building here...
   	     				///
   	     			}
   	     	});
   	     	g1.setOnMouseExited(new EventHandler<MouseEvent>() {
     			public void handle(MouseEvent event) {
     				BuildingNameLabel.setText(building.getName());
     				
     			}
   	     	});
   	     	g1.setOnMouseMoved(new EventHandler<MouseEvent>() {
   	     		public void handle(MouseEvent event) {
   	     			BuildingNameLabel.setText(building.getName()+" " + floor);
   	     			g1.setEffect(shadow);
   	     		}
   	     	});
   	     	g1.setOnMouseExited(new EventHandler<MouseEvent>() {
   	     		public void handle(MouseEvent event) {
   	     			buildingSelected.setText("...");
   	     			//PerspectiveTransform pt = new PerspectiveTransform();
   	     			//pt = setCorners(pt, 80, 60);
   	     			g1.setEffect(pt);
   	     		}
   	     	});
   	     	g1.setLayoutX(120);
   	     	g1.setLayoutY(100-i*45);
   	     	applyAnimation(g1, i); 
   	     	root.getChildren().add(g1);
   	     	
    	}
	}
    
    private void applyAnimation(Group g1, int i){
   	 
		 //FLOOR 1
		 Path g1path = new Path();
		 MoveTo g1moveTo = new MoveTo();
		 g1moveTo.setX(500.0f);
		 g1moveTo.setY(600.0f);
		 LineTo g1lineTo = new LineTo();
		 g1lineTo.setX(500.0f);
		 g1lineTo.setY(560.0f - i*10);
		 g1path.getElements().add(g1moveTo);
		 g1path.getElements().add(g1lineTo);

		 PathTransition g1pt = new PathTransition();
		 g1pt.setDuration(Duration.millis(5000));
		 g1pt.setPath(g1path);
		 g1pt.setNode(g1);
		 g1pt.setOrientation(PathTransition.OrientationType.NONE);
		 g1pt.setCycleCount(Timeline.INDEFINITE);
		 g1pt.setAutoReverse(true);
		 g1pt.play();
		 
    }
    
    private PerspectiveTransform setCorners(PerspectiveTransform pt, double width, double height) {
		 pt.setUlx(width + 80);//upper left
	     pt.setUly(height + 0);
	     pt.setUrx(width + 280);//upper right
	     pt.setUry(height + 0);
	     pt.setLrx(width + 200);//Lower right
	     pt.setLry(height + 120);
	     pt.setLlx(width + 0);//lower left
	     pt.setLly(height + 120);
	     return pt;
	}


	private Graph createGraph(Graph g, LinkedList<Node> nodes, LinkedList<Edge> edges){
    	g.setNodes(nodes);
    	//Added this way so they can be bi directionally added
    	for(int i = 0; i < edges.size(); i++){
    		g.addEdge(edges.get(i).getFrom(), edges.get(i).getTo());
    	}
    	return g;
    }
    
    
    private void drawNodes(LinkedList<Node> nodes, Pane NodePane, Pane root, TextField startText, TextField destText, ImageView imageView){
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
            	newNodeButton.relocate(nodes.get(i).getX()-7, nodes.get(i).getY()-7);
            	Node newNode = nodes.get(i);
            	newNodeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                    	if (!start){
                    		if(newNode.getIsPlace()) startNode = newNode.getName();
                    		startText.setText(startNode);
                    		start = true;
                    	}
                    	else if(!end){
                    		if(newNode.getIsPlace()) endNode = newNode.getName();
                    		DestText.setText(endNode);
                    		start = false;
                    		end = false;
                    	}
                    }
                });
            	NodePane.getChildren().add(newNodeButton);
    		} else if(!nodes.get(i).getIsPlace()){
    			//Do nothing
    		}
	  		
    	}
    }
    
    private void drawRoute(GraphicsContext gc, LinkedList<Node> route) {
    	 Color customBlue = Color.web("0x00b3fd"); 
    	 
    	 gc.setLineCap(StrokeLineCap.ROUND);
    	//iterate through the route drawing a connection between nodes
    	for(int i = 1; i < route.size(); i ++){
    		gc.setLineWidth(5);
    		
            gc.setStroke(Color.BLACK);
	  		gc.strokeLine(route.get(i-1).getX(), route.get(i-1).getY(), route.get(i).getX(),route.get(i).getY());
            gc.setLineWidth(3);
            
            gc.setStroke(customBlue);
	  		gc.strokeLine(route.get(i-1).getX(), route.get(i-1).getY(), route.get(i).getX(),route.get(i).getY());
	  		
    	}
    }
    
    private LinkedList<Edge> convertEdgeData(LinkedList<EdgeDataConversion> edgeData) {
    	LinkedList<Edge> edgeList = new LinkedList<Edge>();
    	Node fromNode = new Node(0, 0, 0, "", "", false, false, "");
    	Node toNode = new Node(0, 0, 0, "","", false, false, "");
    	
    	//iterate through the edges 
    	for(int i = 0; i < edgeData.size(); i ++){
    		//System.out.println("Edge Iterator: " + i);
    		//iterate throught he nodelist to find the matching node
    		for(int j = 0; j < nodeList.size(); j ++){
        		//System.out.println("Node Iterator: " + j + ", x valFrom: " + nodeList.get(j).getX() + " =? " + nodeList.get(j).getName());

    			if(edgeListConversion.get(i).getFrom().equals((nodeList.get(j)).getName())){
					fromNode = nodeList.get(j);
				}
				if(edgeListConversion.get(i).getTo().equals((nodeList.get(j)).getName())){
					toNode = nodeList.get(j);
				}
    			
    		}
    		Edge newEdge = new Edge(fromNode, toNode, edgeListConversion.get(i).getDistance());
			edgeList.add(newEdge);
    	}
    	
    	return edgeList;
    }
    
    public void handleSearchByKeyStart(String oldVal, String newVal) {
    	// If the number of characters in the text box is less than last time
        // it must be because the user pressed delete
        if ( oldVal != null && (newVal.length() < oldVal.length()) ) {
            // Restore the lists original set of entries 
            // and start from the beginning
        	StartList.setItems(LocationOptions);
        }
        StartList.setOpacity(100);
        
 
        // Break out all of the parts of the search text 
        // by splitting on white space
        String[] parts = newVal.toUpperCase().split(" ");
 
        // Filter out the entries that don't contain the entered text
        ObservableList<String> subentries = FXCollections.observableArrayList();
        for ( Object entry: StartList.getItems() ) {
            boolean match = true;
            String entryText = (String)entry;
            for ( String part: parts ) {
                // The entry needs to contain all portions of the
                // search string *but* in any order
                if ( ! entryText.toUpperCase().contains(part) ) {
                    match = false;
                    break;
                }
            }
 
            if ( match ) {
                subentries.add(entryText);
            }
            if(subentries.size() *25 < 75)
            	StartList.setMaxHeight(subentries.size() *25);
            else
            	StartList.setMaxHeight(75);
        }
        StartList.setItems(subentries);
        
        	if(subentries.isEmpty() || subentries.get(0).equals(StartText.getText()))
        		StartList.setOpacity(0);
        
    }
 
    public void handleSearchByKeyDest(String oldVal, String newVal) {
        // If the number of characters in the text box is less than last time
        // it must be because the user pressed delete
        if ( oldVal != null && (newVal.length() < oldVal.length()) ) {
            // Restore the lists original set of entries 
            // and start from the beginning
        	DestList.setItems(LocationOptions);
        }
        DestList.setOpacity(100);
        
 
        // Break out all of the parts of the search text 
        // by splitting on white space
        String[] parts = newVal.toUpperCase().split(" ");
 
        // Filter out the entries that don't contain the entered text
        ObservableList<String> subentries = FXCollections.observableArrayList();
        String entryText = "";
        for ( Object entry: DestList.getItems() ) {
            boolean match = true;
            entryText = (String)entry;
            for ( String part: parts ) {
                // The entry needs to contain all portions of the
                // search string *but* in any order
                if ( ! entryText.toUpperCase().contains(part) ) {
                    match = false;
                    break;
                }
            }
 
            if ( match ) {
                subentries.add(entryText);
            }
            if(subentries.size() *25 < 75)
            	DestList.setMaxHeight(subentries.size() *25);
            else
            	DestList.setMaxHeight(75);
        }
        DestList.setItems(subentries);

        
        	if(subentries.isEmpty() || subentries.get(0).equals(DestText.getText()))
        		DestList.setOpacity(0);
        
    }
    
    private Parent createZoomPane(final Group group) {
	    final double SCALE_DELTA = 1.1;
	    final StackPane zoomPane = new StackPane();
	    final ScrollPane scrollPane = new ScrollPane();

	    zoomPane.getChildren().add(group);
	

	    final Group scrollContent = new Group(zoomPane);
	    scrollPane.setContent(scrollContent);
	    //Removes Scroll bars
	    scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
	    scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);

	    scrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
	      @Override
	      public void changed(ObservableValue<? extends Bounds> observable,
	          Bounds oldValue, Bounds newValue) {
	        zoomPane.setMinSize(newValue.getWidth(), newValue.getHeight());
	      }
	    });

	    scrollPane.setPrefViewportWidth(800);
	    scrollPane.setPrefViewportHeight(605);

	    zoomPane.setOnScroll(new EventHandler<ScrollEvent>() {
	      @Override
	      public void handle(ScrollEvent event) {
	        event.consume();

	        if (event.getDeltaY() == 0) {
	          return;
	        }

	        double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA
	            : 1 / SCALE_DELTA;

	        if(scaleFactor < 1 && k > -1) {
	        	k--;
	        	System.out.println(k);
		        // amount of scrolling in each direction in scrollContent coordinate
		        // units
		        Point2D scrollOffset = figureScrollOffset(scrollContent, scrollPane);
		        
		        group.setScaleX(group.getScaleX() * scaleFactor);
		        group.setScaleY(group.getScaleY() * scaleFactor);

		        // move viewport so that old center remains in the center after the
		        // scaling
		        repositionScroller(scrollContent, scrollPane, scaleFactor, scrollOffset);
	        }
	        if(scaleFactor > 1 && k < 8) {
	        	k++;
		        // amount of scrolling in each direction in scrollContent coordinate
		        // units
		        Point2D scrollOffset = figureScrollOffset(scrollContent, scrollPane);
		        
		        group.setScaleX(group.getScaleX() * scaleFactor);
		        group.setScaleY(group.getScaleY() * scaleFactor);

		        // move viewport so that old center remains in the center after the
		        // scaling
		        repositionScroller(scrollContent, scrollPane, scaleFactor, scrollOffset);
	        }

	      }
	    });

	    // Panning via drag....
	    final ObjectProperty<Point2D> lastMouseCoordinates = new SimpleObjectProperty<Point2D>();
	    scrollContent.setOnMousePressed(new EventHandler<MouseEvent>() {
	      @Override
	      public void handle(MouseEvent event) {
	        lastMouseCoordinates.set(new Point2D(event.getX(), event.getY()));
	      }
	    });

	    scrollContent.setOnMouseDragged(new EventHandler<MouseEvent>() {
	      @Override
	      public void handle(MouseEvent event) {
	        double deltaX = event.getX() - lastMouseCoordinates.get().getX();
	        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scrollPane.getViewportBounds().getWidth();
	        double deltaH = deltaX * (scrollPane.getHmax() - scrollPane.getHmin()) / extraWidth;
	        double desiredH = scrollPane.getHvalue() - deltaH;
	        scrollPane.setHvalue(Math.max(0, Math.min(scrollPane.getHmax(), desiredH)));

	        double deltaY = event.getY() - lastMouseCoordinates.get().getY();
	        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scrollPane.getViewportBounds().getHeight();
	        double deltaV = deltaY * (scrollPane.getHmax() - scrollPane.getHmin()) / extraHeight;
	        double desiredV = scrollPane.getVvalue() - deltaV;
	        scrollPane.setVvalue(Math.max(0, Math.min(scrollPane.getVmax(), desiredV)));
	      }
	    });

	    return scrollPane;
	}
    
    private void repositionScroller(Group scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
        double scrollXOffset = scrollOffset.getX();
        double scrollYOffset = scrollOffset.getY();
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        if (extraWidth > 0) {
          double halfWidth = scroller.getViewportBounds().getWidth() / 2 ;
          double newScrollXOffset = (scaleFactor - 1) *  halfWidth + scaleFactor * scrollXOffset;
          scroller.setHvalue(scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
        } else {
          scroller.setHvalue(scroller.getHmin());
        }
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        if (extraHeight > 0) {
          double halfHeight = scroller.getViewportBounds().getHeight() / 2 ;
          double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
          scroller.setVvalue(scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
        } else {
          scroller.setHvalue(scroller.getHmin());
        }
      }
    
    private Point2D figureScrollOffset(Group scrollContent, ScrollPane scroller) {
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        double hScrollProportion = (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
        double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        double vScrollProportion = (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
        double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
        return new Point2D(scrollXOffset, scrollYOffset);
      }
    
    private void loadMap(Pane root, Parent zoomPane, ImageView imageView){
    	k = 0; // Reset Zoom Variable
	    root.getChildren().remove(zoomPane);
	    root.getChildren().remove(canvas);

    	nodeList.clear();
   		edgeList.clear();
   		StartText.clear();
   		DestText.clear();
        StartList.setOpacity(0);
        DestList.setOpacity(0);
    	nodeList = JsonParser.getJsonContent("Graphs/" + (String) mapSelector.getValue() + ".json");
    	edgeListConversion = JsonParser.getJsonContentEdge("Graphs/" + (String) mapSelector.getValue() + "Edges.json");
    	edgeList = convertEdgeData(edgeListConversion);
    	
    	//graph = createGraph(new Graph(), nodeList, edgeList);
    	
    	File newMapFile = new File("CS3733_Graphics/" + (String) mapSelector.getValue() + ".png"); //MUST ADD png extension!
    	Image mapImage = new Image(newMapFile.toURI().toString());
        imageView.setImage(mapImage);
        //add node buttons to the screen and populates the drop down menus
        LocationOptions.clear();
        for(int i = 0; i < nodeList.size() - 1; i ++){ 
        	if(nodeList.get(i).getIsPlace())
        		LocationOptions.add(nodeList.get(i).getName());
        }
        StartList.setItems(LocationOptions);      
        DestList.setItems(LocationOptions);
        
        graph = createGraph(graph, nodeList, edgeList);
        Pane NodePane = new Pane();
        gc.clearRect(0, 0, 800, 600);
        drawNodes(nodeList, NodePane,root, StartText, DestText,imageView);
                      
        final Group group = new Group(imageView, canvas, NodePane);
	    zoomPane = createZoomPane(group);
	    root.getChildren().add(zoomPane);
	    highLight(NodePane, imageView, root);
	    
    }
    
    public void highLight(Pane NodePane, ImageView imageView, Pane root){        
        Polygon cc = new Polygon();
        cc.getPoints().addAll(new Double[]{
        	    1261.0, 649.0,
        	    1272.0, 656.0,
        	    1273.0, 668.0,
        	    1267.0, 677.0,
        	    1254.0, 680.0,
        	    1245.0, 673.0,
        	    1242.0, 662.0,
        	    1239.0, 677.0,
        	    1175.0, 667.0,
        	    1184.0, 617.0,
        	    1197.0, 620.0,
        	    1213.0, 630.0,
        	    1220.0, 623.0,
        	    1214.0, 617.0,
        	    1223.0, 604.0,
        	    1219.0, 601.0,
        	    1218.0, 591.0,
        	    1222.0, 582.0,
        	    1234.0, 580.0,
        	    1238.0, 584.0,
        	    1248.0, 573.0,
        	    1235.0, 565.0,
        	    1249.0, 543.0,
        	    1252.0, 537.0,
        	    1302.0, 546.0,
        	    1285.0, 652.0});
        
        cc.setFill(Color.TRANSPARENT);
        
        cc.setStroke(Color.TRANSPARENT);
        cc.setStrokeWidth(1.0);
        cc.setOnMouseEntered(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
                cc.setFill(new Color(1.0, 1.0, 0.0, 0.2));
        		System.out.println("I'm here");
        	}
        });
        cc.setOnMouseExited(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
                cc.setFill(Color.TRANSPARENT);
        	}
        });
        cc.setOnMouseClicked(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
        		getMapSelector(CampusCenter, root, imageView);
        	}
        });
        
        NodePane.getChildren().add(cc);
        
        
        Polygon olin = new Polygon();
        olin.getPoints().addAll(new Double[]{
        	    
        	    1334.0, 510.0,
        	    1373.0, 516.0,
        	    1350.0, 662.0,
        	    1311.0, 656.0});
        
        olin.setFill(Color.TRANSPARENT);
        
        olin.setStroke(Color.TRANSPARENT);
        olin.setStrokeWidth(1.0);
        olin.setOnMouseEntered(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
                olin.setFill(new Color(1.0, 1.0, 0.0, 0.2));
        		System.out.println("I'm here");
        	}
        });
        olin.setOnMouseExited(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
                olin.setFill(Color.TRANSPARENT);
        	}
        });
        olin.setOnMouseClicked(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
        		//getMapSelector(OlinHall, root, imageView);
        	}
        });
        
        NodePane.getChildren().add(olin);

        Polygon stratton = new Polygon();
        stratton.getPoints().addAll(new Double[]{
        	    
        	    1377.0, 813.0,
        	    1416.0, 820.0,
        	    1403.0, 903.0,
        	    1363.0, 896.0});
        
        stratton.setFill(Color.TRANSPARENT);
        
        stratton.setStroke(Color.TRANSPARENT);
        stratton.setStrokeWidth(1.0);
        stratton.setOnMouseEntered(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
        		stratton.setFill(new Color(1.0, 1.0, 0.0, 0.2));
        		System.out.println("I'm here");
        	}
        });
        stratton.setOnMouseExited(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
        		stratton.setFill(Color.TRANSPARENT);
        	}
        });
        stratton.setOnMouseClicked(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
        		getMapSelector(StrattonHall, root, imageView);
        	}
        });
        
        NodePane.getChildren().add(stratton);
        
        Polygon library = new Polygon();
        library.getPoints().addAll(new Double[]{
        	    
        	    1607.0, 712.0,
        	    1667.0, 725.0,
        	    1664.0, 742.0,
        	    1661.0, 742.0,
        	    1660.0, 769.0,
        	    1658.0, 782.0,
        	    1655.0, 799.0,
        	    1645.0, 824.0,
        	    1648.0, 825.0,
        	    1644.0, 841.0,
        	    1584.0, 829.0,
        	    1585.0, 794.0,
        	    1588.0, 773.0,
        	    1593.0, 750.0
        	    });
        
        library.setFill(Color.TRANSPARENT);
        
        library.setStroke(Color.TRANSPARENT);
        library.setStrokeWidth(1.0);
        library.setOnMouseEntered(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
        		library.setFill(new Color(1.0, 1.0, 0.0, 0.2));
        		System.out.println("I'm here");
        	}
        });
        library.setOnMouseExited(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
        		library.setFill(Color.TRANSPARENT);
        	}
        });
        library.setOnMouseClicked(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
        		getMapSelector(GordonLibrary, root, imageView);
        	}
        });
        
        
        NodePane.getChildren().add(library);
        
        
        Polygon ak = new Polygon();
        ak.getPoints().addAll(new Double[]{
        	    
        	    1471.0, 439.0,
        	    1508.0, 460.0,
        	    1491.0, 490.0,
        	    1540.0, 518.0,
        	    1557.0, 489.0,
        	    1594.0, 510.0,
        	    1553.0, 581.0,
        	    1530.0, 569.0,
        	    1522.0, 582.0,
        	    1445.0, 537.0,
        	    1452.0, 537.0,
        	    1452.0, 525.0,
        	    1429.0, 512.0
        	    });
        
        ak.setFill(Color.TRANSPARENT);
        
        ak.setStroke(Color.TRANSPARENT);
        ak.setStrokeWidth(1.0);
        ak.setOnMouseEntered(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
        		ak.setFill(new Color(1.0, 1.0, 0.0, 0.2));
        		System.out.println("I'm here");
        	}
        });
        ak.setOnMouseExited(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
        		ak.setFill(Color.TRANSPARENT);
        	}
        });
        ak.setOnMouseClicked(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
        		getMapSelector(AtwaterKent, root, imageView);
        	}
        });
        
        
        NodePane.getChildren().add(ak);
        
        Polygon cdc = new Polygon();
        cdc.getPoints().addAll(new Double[]{
        	   
        	    1391.0, 732.0,
        	    1430.0, 738.0,
        	    1420.0, 804.0,
        	    1380.0, 797.0
        	    });
        
        cdc.setFill(Color.TRANSPARENT);
        
        cdc.setStroke(Color.TRANSPARENT);
        cdc.setStrokeWidth(1.0);
        cdc.setOnMouseEntered(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
        		cdc.setFill(new Color(1.0, 1.0, 0.0, 0.2));
        		System.out.println("I'm here");
        	}
        });
        cdc.setOnMouseExited(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
        		cdc.setFill(Color.TRANSPARENT);
        	}
        });
        cdc.setOnMouseClicked(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
        		getMapSelector(ProjectCenter, root, imageView);
        	}
        });
        
        NodePane.getChildren().add(cdc);
        
        Polygon higginsHouse = new Polygon();
        higginsHouse.getPoints().addAll(new Double[]{
        	    
        	    1130.0, 435.0,
        	    1154.0, 451.0,
        	    1159.0, 443.0,
        	    1165.0, 446.0,
        	    1161.0, 441.0,
        	    1165.0, 435.0,
        	    1172.0, 435.0,
        	    1176.0, 433.0,
        	    1197.0, 448.0,
        	    1209.0, 431.0,
        	    1225.0, 441.0,
        	    1212.0, 459.0,
        	    1200.0, 452.0,
        	    1192.0, 464.0,
        	    1196.0, 466.0,
        	    1189.0, 476.0,
        	    1185.0, 473.0,
        	    1163.0, 505.0,
        	    1137.0, 487.0,
        	    1149.0, 471.0,
        	    1120.0, 450.0
        	    
        	    
        	    });
        
        higginsHouse.setFill(Color.TRANSPARENT);
        
        higginsHouse.setStroke(Color.TRANSPARENT);
        higginsHouse.setStrokeWidth(1.0);
        higginsHouse.setOnMouseEntered(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
        		higginsHouse.setFill(new Color(1.0, 1.0, 0.0, 0.2));
        		System.out.println("I'm here");
        	}
        });
        higginsHouse.setOnMouseExited(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
        		higginsHouse.setFill(Color.TRANSPARENT);
        	}
        });
        higginsHouse.setOnMouseClicked(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
        		getMapSelector(HigginsHouse, root, imageView);
        	}
        });
        
        
        NodePane.getChildren().add(higginsHouse);
        
        
        
        Polygon boyntonHall = new Polygon();
        boyntonHall.getPoints().addAll(new Double[]{
        	    
        	    1406.0, 932.0,
        	    1435.0, 937.0,
        	    1434.0, 943.0,
        	    1501.0, 954.0,
        	    1497.0, 984.0,
        	    1492.0, 984.0,
        	    1491.0, 988.0,
        	    1480.0, 987.0,
        	    1480.0, 981.0,
        	    1429.0, 973.0,
        	    1428.0, 980.0,
        	    1399.0, 975.0
        	    });
        
        boyntonHall.setFill(Color.TRANSPARENT);
        
        boyntonHall.setStroke(Color.TRANSPARENT);
        boyntonHall.setStrokeWidth(1.0);
        boyntonHall.setOnMouseEntered(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
        		boyntonHall.setFill(new Color(1.0, 1.0, 0.0, 0.2));
        		System.out.println("I'm here");
        	}
        });
        boyntonHall.setOnMouseExited(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
        		boyntonHall.setFill(Color.TRANSPARENT);
        	}
        });
        boyntonHall.setOnMouseClicked(new EventHandler <MouseEvent>(){
        	public void handle (MouseEvent event){
        		getMapSelector(BoyntonHall, root, imageView);
        	}
        });
        
        NodePane.getChildren().add(boyntonHall);
    }
    


}
