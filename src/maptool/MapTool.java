package maptool;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import io.JsonParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import node.*;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class MapTool extends Application{
	boolean delete = false;
	boolean startCoord, endCoord  = false;
	double startX, startY, startZ, endX, endY, endZ = 0.0;
	int k = 0; // Set Max zoom Variable

    // Buildings
	// TODO Add more buildings
    Building Campus = new Building("Campus");
    Building AtwaterKent = new Building("Atwater Kent");
    Building BoyntonHall = new Building("Boynton Hall");
    Building CampusCenter = new Building("Campus Center");
    Building GordonLibrary = new Building("Gordon Library");
    Building HigginsHouse = new Building("Higgins House");
    Building ProjectCenter = new Building("Project Center");
    Building StrattonHall = new Building("Stratton Hall");

    // Maps
    // TODO Add more maps
    Map CampusMap = new Map("Campus Map", "CM", "CS3733_Graphics/CampusMap.png", "Graphs/CampusMap.json", "Graphs/CampusMapEdges.json", 0, 0, 0, 1, 1);

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

	public static void main(String[] args) {launch(args);}
	
	JsonParser json = new JsonParser();
	LinkedList<Node> nodeList = JsonParser.getJsonContent("Graphs/Nodes/CampusMap.json");
	LinkedList<EdgeDataConversion> edgeListConversion = JsonParser.getJsonContentEdge("Graphs/Edges/CampusMapEdges.json");
	LinkedList<Edge> edgeList = convertEdgeData(edgeListConversion);
	Canvas canvas = new Canvas(800, 600);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	boolean start, end = false;
	String startNode, endNode;
	String nodeReference = "";
	boolean updateNode = false;
	Button nodeButtonReference = new Button("");
	
    final TextField xField = new TextField("");  
    final TextField yField = new TextField("");
    final TextField zField = new TextField("");
    final TextField nameField = new TextField(""); 
    ObservableList<String> typeOptions = FXCollections.observableArrayList("Place", "Transition Point", "Staircase", "Vending Machine", "Water Fountain");
	final ComboBox<String> typeSelector = new ComboBox<String>(typeOptions);
    final RadioButton isPlace = new RadioButton();


    ObservableList<Map> mapOptions = FXCollections.observableArrayList();
	final ComboBox<Map> mapSelector = new ComboBox<>(mapOptions);

    // Variable to store map that is currently displayed
    Map currentlySelectedMap;
    
    final Label fromField = new Label("");
    final Label toField = new Label("");
    final Label updateNodeLabel = new Label("");
    
    
    final Pane root = new Pane();
	
    //create actual map
    File mapFile = new File("CS3733_Graphics/CampusMap.png");
    Image mapImage = new Image(mapFile.toURI().toString());
    ImageView imageView = new ImageView();
    
 
    @Override
    public void start(Stage primaryStage) {


        // Create maps and add them to their respective buildings
		// TODO Add more buildings and maps
        //Add Maps to buildings
        Campus.addMap(CampusMap);

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

        // Store the Buildings in a list
        // TODO Add more buildings to this list
        LinkedList<Building> buildings = new LinkedList<>();
        buildings.add(Campus);
        buildings.add(AtwaterKent);
        buildings.add(GordonLibrary);
        buildings.add(CampusCenter);
        buildings.add(HigginsHouse);
        buildings.add(StrattonHall);
        buildings.add(ProjectCenter);

        // Iterate over the list of buildings and add their maps to another list
        LinkedList<Map> maps = new LinkedList<>();
        for (Building b : buildings){
             maps.addAll(b.getMaps());
        }

        mapOptions.addAll(maps);


    	final Pane root = new Pane();
    	final Scene scene = new Scene(root, 1050, 700);//set size of scene
    	
    	//Set default Type
    	typeSelector.setValue("Place");
    	
    	//Create a map selection drop down menu
    	final VBox mapSelectionBoxV = new VBox(5);
    	final Label mapSelectorLabel = new Label("Choose map");
    	mapSelectorLabel.setTextFill(Color.WHITE);
    	mapSelectorLabel.setFont(Font.font ("manteka", 14));
    	final HBox mapSelectionBoxH = new HBox(5);
    	final Button LoadMapButton = new Button("Load Map");


    	mapSelector.setValue(mapSelector.getItems().get(0));
        // Assign Campus map as currently selected map
        currentlySelectedMap = mapSelector.getValue();

        // Shows Name of Map Object in ComboBox dropdown
        mapSelector.setCellFactory(new Callback<ListView<Map>, ListCell<Map>>() {
            @Override
            public ListCell<Map> call(ListView<Map> param) {
                ListCell cell= new ListCell<Map>(){
                    @Override
                    protected void updateItem(Map map, boolean empty){
                        super.updateItem(map, empty);
                        if (empty){
                            setText("");
                        } else {
                            setText(map.getName());
                        }
                    }
                };
                return cell;
            }
        });
        // Shows name in ComboBox
        mapSelector.setButtonCell(new ListCell<Map>(){
            @Override
            protected void updateItem(Map map, boolean bln){
                super.updateItem(map, bln);
                if (bln){
                    setText("");
                } else {
                    setText(map.getName());
                }
            }
        });

    	mapSelectionBoxH.getChildren().addAll(mapSelector, LoadMapButton);
    	mapSelectionBoxV.setLayoutX(830);
    	mapSelectionBoxV.setLayoutY(620);
    	mapSelectionBoxV.getChildren().addAll(mapSelectorLabel, mapSelectionBoxH);
          
    	//Create a label and box for warnings, ie when the coordinates are outside the name
    	final HBox warningBox = new HBox(0); 
    	final Label warningLabel = new Label("");
    	warningLabel.setTextFill(Color.WHITE);
    	warningBox.setLayoutX(830);
    	warningBox.setLayoutY(20);
    	warningBox.getChildren().addAll(warningLabel);  

    	
    	//Create input box field labels (on top of the text boxes)
        final VBox controlLabels = new VBox(10); 
        
        final Label xFieldName = new Label("X Coordinate");
        xFieldName.setFont(Font.font ("manteka", 12));
        xFieldName.setTextFill(Color.WHITE);
        
        final Label yFieldName = new Label("Y Coordinate");
        yFieldName.setFont(Font.font ("manteka", 12));
        yFieldName.setTextFill(Color.WHITE);
        
        final Label zFieldName = new Label("Z Coordinate");
        zFieldName.setTextFill(Color.WHITE);
        zFieldName.setFont(Font.font ("manteka", 12));
        
        final Label nameFieldName = new Label("Name");
        nameFieldName.setTextFill(Color.WHITE);
        nameFieldName.setFont(Font.font ("manteka", 12));
        
        final Label nodeTypeName = new Label("Node Type");
        nodeTypeName.setTextFill(Color.WHITE);
        nodeTypeName.setFont(Font.font ("manteka", 12));
        
    	final Label isPlaceName = new Label("Place?");
        isPlaceName.setTextFill(Color.WHITE);
        isPlaceName.setFont(Font.font ("manteka", 12));
        
        //final Label updateNodeLabel = new Label("Node");
        updateNodeLabel.setTextFill(Color.WHITE);
        updateNodeLabel.setFont(Font.font ("manteka", 12));
        
        final HBox isPlaceUpdateLabelBox = new HBox(60);
        isPlaceUpdateLabelBox.getChildren().addAll(isPlace, updateNodeLabel);
         
        HBox NodeCreationBox = new HBox(5);
        final Button updateNodeButton = new Button("Update Node");
        final Button createNodeButton = new Button("Create Node");
        NodeCreationBox.getChildren().addAll(createNodeButton, updateNodeButton);
        final Button deleteNodeButton = new Button("Delete Node");
        
        controlLabels.setLayoutX(830);
        controlLabels.setLayoutY(20);
        controlLabels.getChildren().addAll(xFieldName, xField, yFieldName, yField, zFieldName, zField, nameFieldName, nameField, nodeTypeName, typeSelector, isPlaceName, isPlace, NodeCreationBox,deleteNodeButton);  

        
        //create edge interface
        final VBox edgeControls = new VBox(10);
        
        final HBox fromBox = new HBox(10);
        final Label fromLabel = new Label("From: ");
        fromLabel.setFont(Font.font ("manteka", 12));
        fromLabel.setTextFill(Color.WHITE);
        fromField.setFont(Font.font ("manteka", 12));
        fromField.setTextFill(Color.WHITE);
        fromBox.getChildren().addAll(fromLabel, fromField);
        
        final HBox toBox = new HBox(10);
        final Label toName = new Label("To:   ");
        toName.setFont(Font.font ("manteka", 12));
        toName.setTextFill(Color.WHITE);
        toField.setFont(Font.font ("manteka", 12));
        toField.setTextFill(Color.WHITE);
        toBox.getChildren().addAll(toName, toField);
        
        HBox EdgeCreationBox = new HBox(5);
        final Button createEdgeButton = new Button("Create Edge");
        final Button deleteEdgeButton = new Button("Delete Edge");
        EdgeCreationBox.getChildren().addAll(createEdgeButton, deleteEdgeButton);
        final Button saveGraph = new Button("Save");
        edgeControls.setLayoutX(830);
        edgeControls.setLayoutY(460);
        edgeControls.getChildren().addAll(fromBox, toBox, EdgeCreationBox, saveGraph);  
  
        imageView.setImage(mapImage);
        imageView.setLayoutX(0);  
        imageView.setLayoutY(0);  
        
        //create background
        File backgroundFile = new File("CS3733_Graphics/Background.jpg");
        Image bgImage = new Image(backgroundFile.toURI().toString());
        ImageView bgView = new ImageView();
        bgView.setImage(bgImage);
        bgView.setLayoutX(0);  
        bgView.setLayoutY(0);  
        
        //Attach everything to the screen
        root.getChildren().add(bgView);        

        root.getChildren().add(mapSelectionBoxV);
        root.getChildren().add(edgeControls);
        root.getChildren().add(controlLabels);
        

        Pane NodePane = new Pane();
        NodePane.setPrefSize(8000, 6000);
        drawEdges(edgeList, gc, NodePane); //from here we draw the nodes so that nodes are on top of the edges
        
        final Group group = new Group(imageView, NodePane);
	    Parent zoomPane = createZoomPane(group);
	    
	    root.getChildren().add(zoomPane);
        
        final EventHandler<ActionEvent> CreateHandler = new EventHandler<ActionEvent>() {  
            @Override  
            public void handle(ActionEvent event) {  
                root.getChildren().remove(warningBox);
            	int x = -1, y = -1, z = -1;
            	
            	/************************************************/
            	try{
            		x = Integer.parseInt(xField.getText());  
            		y = Integer.parseInt(yField.getText());
            		z = Integer.parseInt(zField.getText());
            	} catch (NumberFormatException e) {
            	    System.err.println("NumberFormatException: " + e.getMessage());
            	} 
            	
                //check to see if coordinates are within map bounds, We dont care if it's campus map
                if(!isInBounds(x, y) && !currentlySelectedMap.getName().equals("Campus Map")){
                	warningLabel.setText("Error, coordinates out of bounds");
                	root.getChildren().add(warningBox); 
                }
                
                //check to see if proper fields types given
                else if(!isValidCoords(xField.getText())){
            		warningLabel.setText("Error, coordinates not valid");
            		root.getChildren().add(warningBox); 
            	}
                
                // Make sure a name is entered before creating node
                else if (nameField.getText().equals("")){
                	warningLabel.setText("Error, must enter a name");
            		root.getChildren().add(warningBox); 
                }
            	/************************************************/
            	//passes all validity checks, create waypoint and add button
                else{
                	int newX = x, newY = y, newZ = z;
                	warningLabel.setText("");//Remove warning, bc successful
                	
                	Button newNodeButton = new Button("");
                	
                	if(isPlace.isSelected()){
                    	newNodeButton.setStyle(
                                "-fx-background-radius: 5em; " +  "-fx-min-width: 15px; " + "-fx-min-height: 15px; " + "-fx-max-width: 15px; " + "-fx-max-height: 15px;"
                        );
                	}
                	else{
                		newNodeButton.setStyle(
                    			"-fx-background-color: #000000; " + "-fx-background-radius: 5em; " +  "-fx-min-width: 10px; " + "-fx-min-height: 10px; " + "-fx-max-width: 10px; " + "-fx-max-height: 10px;"
                        );
                	}
                	
                	Node newPlace = new Node(x, y, z, (String) nameField.getText(), (String) currentlySelectedMap.getBuildingName(), currentlySelectedMap.getName(), true, isPlace.isSelected(), typeSelector.getValue());

                    // Set the Global X and Global Y.
                    newPlace.setGlobalX((int)((x*Math.cos(currentlySelectedMap.getRotationalConstant())
                            + y*Math.sin(currentlySelectedMap.getRotationalConstant()) +
                            currentlySelectedMap.getGlobalToLocalOffsetX()) *
                            (currentlySelectedMap.getConversionRatio())));
                	newPlace.setGlobalY((int)((-x*Math.sin(currentlySelectedMap.getRotationalConstant())
                            + y*Math.cos(currentlySelectedMap.getRotationalConstant())
                            + currentlySelectedMap.getGlobalToLocalOffsetY()) *
                            (currentlySelectedMap.getConversionRatio())));
                    // TODO This should also add to the global map nodes
                	nodeList.add(newPlace);
                    //Add actions for when you click this unique button
                    newNodeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    	public void handle(MouseEvent event) {
                        	//check to see if we are just editting an exiting node
                    		
                    		if(delete){
                    			root.getChildren().remove(newNodeButton);
                            	nodeList.remove(newPlace);
                            	//iterate through the edge list and delete all edges attached to this node
                            	for(int i = 0; i < edgeList.size(); i++){
                            		if(edgeList.get(i).getFrom().getName() == newPlace.getName() || edgeList.get(i).getTo().getName() == newPlace.getName()){
                            			edgeList.remove(i);
                            		}
                            	}
                            	delete = false;
                            }
                            else if(!startCoord){
                            	startX = newNodeButton.getLayoutX()+7;
                            	startY = newNodeButton.getLayoutY()+7;
                            	fromField.setText(newPlace.getName());
                            	startCoord = true;
                            }
                            else if(!endCoord){
                            	endX = newNodeButton.getLayoutX()+7;
                            	endY = newNodeButton.getLayoutY()+7;
                            	toField.setText(newPlace.getName());
                            	startCoord = false;
                            	endCoord = false;
                           	}
                    		//no matter what fill in this nodes data into the input box fields
                    		xField.setText(""+newPlace.getX());
                    		yField.setText(""+newPlace.getY());
                    		zField.setText(""+newPlace.getZ());
                    		nameField.setText(newPlace.getName());
                    		typeSelector.setValue(newPlace.getType());
                    		if(newPlace.getIsPlace())
                    			isPlace.setSelected(true);
                    		else { isPlace.setSelected(false); }
                    		nodeReference = newPlace.getName(); //so we can reference this node in other places
                    		updateNode = true;
                    		nodeButtonReference = newNodeButton;
                    		updateNodeLabel.setText(""+newPlace.getName());
                        }
                    		
                    });
                    NodePane.getChildren().add(newNodeButton);
                    if(isPlace.isSelected())
                    	newNodeButton.relocate(newX-7, newY-7);
                    else
                    	newNodeButton.relocate(newX-5, newY-5);
                }
            }  
        }; 
        
        updateNodeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	int x = -1, y = -1, z = -1;
            	
            	/************************************************/
            	try{
            		x = Integer.parseInt(xField.getText());  
            		y = Integer.parseInt(yField.getText());
            		z = Integer.parseInt(zField.getText());
            	} catch (NumberFormatException e) {
            	    System.err.println("NumberFormatException: " + e.getMessage());
            	} 
            	if(updateNode){
            		for(int i = 0; i < nodeList.size(); i++){
                		if(nodeReference == nodeList.get(i).getName()){
                			//root.getChildren().remove(nodeButtonReference);
                            // TODO If a node is updated the edges also have to be updated
                			nodeList.get(i).setX(x);
                			nodeList.get(i).setY(y);
                			nodeList.get(i).setZ(z);
                			nodeList.get(i).setName(nameField.getText());
                			nodeList.get(i).setIsPlace(isPlace.isSelected());
                			nodeList.get(i).setType(typeSelector.getValue());
                			
                			nodeButtonReference.relocate(x, y);
                		}
                			//set all fields and then break out of this
                	}
            		updateNode = false;
            	}
            	
            	saveGraphMethod();

        	   	NodePane.getChildren().clear();

            	root.getChildren().remove(zoomPane);
            	root.getChildren().remove(canvas);
           		root.getChildren().remove(imageView); //remove current map, then load new one
           		nodeList.clear(); 
           		edgeListConversion.clear();
           		edgeList.clear();
                // TODO load the nodes and edges only on the currently selected map
            	nodeList = JsonParser.getJsonContent(currentlySelectedMap.getNodesPath());
            	edgeListConversion = JsonParser.getJsonContentEdge(currentlySelectedMap.getEdgesPath());
            	edgeList = convertEdgeData(edgeListConversion);
            	
            	/* ^^^^^^^^^
            	 * IMPORTANT, THE PROGRAM WILL NOT RUN IF WE DONT HAVE ACTUAL FILES
            	 * WHERE THESE PATHS ARE POINTING TO, FOR NOW, CREATE TEMP ONES AND THEN
            	 * OVERRIDE THEM.
            	 */
            	
           		File newMapFile = new File(currentlySelectedMap.getMapPath()); //MUST ADD png extension!
           		Image mapImage = new Image(newMapFile.toURI().toString());
           		ImageView imageView = new ImageView();
           		imageView.setImage(mapImage);
           		imageView.setLayoutX(0);  
           		imageView.setLayoutY(0);
                
            	drawEdges(edgeList, gc, NodePane);
            	
                final Group group = new Group(imageView, NodePane);
        	    Parent zoomPane = createZoomPane(group);
        	    root.getChildren().add(zoomPane);
            }
            
        });
        
        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	//remove the canvas
            	nodeReference = "";
            	updateNode = false;
            }
        });
        
        //Save the Graph
        saveGraph.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	saveGraphMethod();
            }
        });
        
        NodePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	//Set the location coordinates in the input boxes
            	xField.setText(Integer.toString((int)event.getX()));
            	yField.setText(Integer.toString((int)event.getY()));
            }
        });
        
        deleteNodeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	delete = true;
            }
        });
        deleteEdgeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	delete = true;
            }
        });
       createEdgeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	Node fromNode = new Node(0, 0, 0, "", "", "", false, false, "");
            	Node toNode = new Node(0, 0, 0, "", "", "", false, false, "");
                // FIXME Change this so that it can properly save the edge across different maps
            	for(int i = 0; i < nodeList.size(); i ++){

        			//check difference between place and node..
        			if(nodeList.get(i).getName().equals(fromField.getText())){
        				fromNode = nodeList.get(i);
        			}
        			if(nodeList.get(i).getName().equals(toField.getText())){
        				toNode = nodeList.get(i);
        			}
        			
            	}
            	
            	Edge newEdge = new Edge(fromNode, toNode, getDistance());
                // TODO also add edge on global map
            	edgeList.add(newEdge);
            	Line line = new Line();
            	 line.setStartX(startX);
                 line.setStartY(startY);
                 line.setEndX(endX);
                 line.setEndY(endY);
                 line.setStrokeWidth(3);
                 line.setStyle("-fx-background-color:  #F0F8FF; ");
                 
                 line.setOnMouseClicked(new EventHandler<MouseEvent>(){
                	 public void handle(MouseEvent event){
                		if(delete) {
                			NodePane.getChildren().remove(line);
                            // TODO also remove from the global edge list
                			edgeList.remove(newEdge);
                			delete = false;
                		}
                	 }
                 });
                 NodePane.getChildren().add(line);
            }
        });
       
       
       //Add actions to the Load Map button
       LoadMapButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
           public void handle(MouseEvent event) {
        	   	k = 0; // Reset Zoom Variable
        	   	NodePane.getChildren().clear();
        	   	//clear existing node list
        	   	root.getChildren().remove(zoomPane);
        	   	root.getChildren().remove(canvas);        	   	
           		root.getChildren().remove(imageView); //remove current map, then load new one
           		nodeList.clear(); 
           		edgeListConversion.clear();
           		edgeList.clear();
               // TODO only load the selected map content
               currentlySelectedMap = mapSelector.getValue();
            	nodeList = JsonParser.getJsonContent(currentlySelectedMap.getNodesPath());
            	edgeListConversion = JsonParser.getJsonContentEdge(currentlySelectedMap.getEdgesPath());
            	edgeList = convertEdgeData(edgeListConversion);
            	
            	/* ^^^^^^^^^
            	 * IMPORTANT, THE PROGRAM WILL NOT RUN IF WE DONT HAVE ACTUAL FILES
            	 * WHERE THESE PATHS ARE POINTING TO, FOR NOW, CREATE TEMP ONES AND THEN
            	 * OVERRIDE THEM.
            	 */
            	
           		File newMapFile = new File(currentlySelectedMap.getMapPath()); //MUST ADD png extension!
           		Image mapImage = new Image(newMapFile.toURI().toString());
           		ImageView imageView = new ImageView();
           		imageView.setImage(mapImage);
           		imageView.setLayoutX(0);  
           		imageView.setLayoutY(0);
           		
                drawEdges(edgeList, gc, NodePane);
                             
                final Group group = new Group(imageView, NodePane);
        	    Parent zoomPane = createZoomPane(group);
        	    root.getChildren().add(zoomPane);
           }
           
       });
             
        createNodeButton.setOnAction(CreateHandler);  
        
        primaryStage.setScene(scene);  
        primaryStage.show();  
        
    }  
    
    //Change where we call drawEdges to just change the drawEdgeBool to true;
    private void drawEdges(LinkedList<Edge> edges, GraphicsContext gc, Pane nodePane){
    	root.getChildren().remove(canvas);
    	gc.clearRect(0, 0, 800, 600);
    	int i;
    	
        for( i = 0; i < edgeList.size(); i++){ // TODO Iterate over local edge list
       		int j = i;
       		Line line = new Line();
       	//Determine the offset we need to use for the tool graph FROM NODE
       		if(edgeList.get(i).getFrom().getIsPlace()){
       			line.setStartX(edgeList.get(i).getFrom().getX());
                line.setStartY(edgeList.get(i).getFrom().getY());
       		} else{
       			line.setStartX(edgeList.get(i).getFrom().getX());
                line.setStartY(edgeList.get(i).getFrom().getY());
                
       		}
       		//Determine the offset we need to use for the tool graph TO NODE
       		if(edgeList.get(i).getTo().getIsPlace()){
       			line.setEndX(edgeList.get(i).getTo().getX());
                line.setEndY(edgeList.get(i).getTo().getY());
       		} else {
       			line.setEndX(edgeList.get(i).getTo().getX());
                line.setEndY(edgeList.get(i).getTo().getY());
       		}
       		line.setStrokeWidth(3);
            
               
       		line.setOnMouseClicked(new EventHandler<MouseEvent>(){
              	public void handle(MouseEvent event){
              		if(delete) {
              			nodePane.getChildren().remove(line);
              			edgeList.remove(edgeList.get(j));
              			delete = false;
              		}
              	 }
               });
       		nodePane.getChildren().add(line);
       		}

       	drawNodes(nodeList, nodePane, fromField, toField);

	}
  
    	
    //check to see if the coordinates are integers
    public boolean isValidCoords(String s){
    	String validCoords = "0123456789";
        for(int i = 1; i <= s.length(); i++){
        	if(!validCoords.contains(s.substring(i-1, i))){
            	return false;
        	}
        }
        return true;
    }
    
    //check to see if node coordinates are within map bounds
    public boolean isInBounds(int x, int y){
    	if(x > 800 || y > 600 || x < 0 || y < 0){
    		return false;
        }
    	return true;
    }
    
    // Returns the distance between the two nodes, in pixels
    // FIXME needs to be updated to calculate distance based on global coordinates and not local
    public int getDistance(){
    	return (int) Math.sqrt((Math.pow(((int)startX - (int)endX), 2)) + (Math.pow(((int)startY - (int)endY), 2)) + (Math.pow(startZ - endZ, 2)));
    }
    
    // Draws the Places and Nodes on to the map
    private void drawNodes(LinkedList<Node> nodes, Pane root, Label fromField, Label toField){
    	int i;
    	for(i = 0; i < nodes.size(); i ++){ 
    		Button newNodeButton = new Button("");
    		//Determine what type of node image we choose
    		if(nodes.get(i).getIsPlace()){
            	newNodeButton.setStyle(
                        "-fx-background-radius: 5em; " +
                        "-fx-min-width: 15px; " +
                        "-fx-min-height: 15px; " +
                        "-fx-max-width: 15px; " +
                        "-fx-max-height: 15px;"
                );
            	newNodeButton.relocate(nodes.get(i).getX()-7, nodes.get(i).getY()-7);
    		}
            else{
            	newNodeButton.setStyle(
            			"-fx-background-color: #000000; " +
                        "-fx-background-radius: 5em; " +
                        "-fx-min-width: 10px; " +
                        "-fx-min-height: 10px; " +
                        "-fx-max-width: 10px; " +
                        "-fx-max-height: 10px;"
                );
            	newNodeButton.relocate(nodes.get(i).getX()-5, nodes.get(i).getY()-5);

            }
            Node newPlace = nodes.get(i);
            newNodeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            	public void handle(MouseEvent event) {
            		if(delete){
            			root.getChildren().remove(newNodeButton);
                    	nodeList.remove(newPlace);
                    	//iterate through the edge list and delete all edges attached to this node
                    	for(int i = 0; i < edgeList.size(); i++){
                    		if(edgeList.get(i).getFrom().getName() == newPlace.getName() || edgeList.get(i).getTo().getName() == newPlace.getName()){
                    			edgeList.remove(i);
                    		}
                    	}
                    	delete = false;
                    }
                    else if(!startCoord){
                    	startX = newNodeButton.getLayoutX()+7;
                    	startY = newNodeButton.getLayoutY()+7;
                    	fromField.setText(newPlace.getName());
                    	startCoord = true;
                    }
                    else if(!endCoord){
                    	endX = newNodeButton.getLayoutX()+7;
                    	endY = newNodeButton.getLayoutY()+7;
                    	toField.setText(newPlace.getName());
                    	startCoord = false;
                    	endCoord = false;
                   	}
            		//no matter what fill in this nodes data into the input box fields
            		xField.setText(""+newPlace.getX());
            		yField.setText(""+newPlace.getY());
            		zField.setText(""+newPlace.getZ());
            		nameField.setText(newPlace.getName());
            		typeSelector.setValue(newPlace.getType());
            		if(newPlace.getIsPlace())
            			isPlace.setSelected(true);
            		else { isPlace.setSelected(false); }
            		nodeReference = newPlace.getName(); //so we can referecne this node in other places
            		updateNode = true;
            		nodeButtonReference = newNodeButton;
            	}
            });
            root.getChildren().add(newNodeButton);
    		
    	}
    }
    
    private LinkedList<Edge> convertEdgeData(LinkedList<EdgeDataConversion> edgeData) {
    	LinkedList<Edge> edgeList = new LinkedList<Edge>();
    	Node fromNode = new Node(0, 0, 0, "", "", "", false, false, "");
    	Node toNode = new Node(0, 0, 0, "", "", "", false, false, "");
    	
    	//iterate through the edges 
    	for(int i = 0; i < edgeData.size(); i ++){
    		//iterate throught he nodelist to find the matching node
    		for(int j = 0; j < nodeList.size(); j ++){
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
    
    private void saveGraphMethod(){
    	String nodeData = JsonParser.jsonToString(nodeList);
    	String path = currentlySelectedMap.getNodesPath();
    	try {
			JsonParser.saveFile(nodeData, path);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	//save edges
    	String edgeData = JsonParser.jsonToStringEdge(edgeList);
    	String edgePath = currentlySelectedMap.getEdgesPath();
    	try {
			JsonParser.saveFile(edgeData, edgePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
    
    /*//NOT WORKING RIGHT NOW
    private void loadMapMethod(){
    	//clear existing node list
    	root.getChildren().remove(canvas);
    	root.getChildren().remove(imageView); //remove current map, then load new one
    	nodeList.clear(); 
    	edgeListConversion.clear();
    	edgeList.clear();
     	nodeList = JsonParser.getJsonContent("Graphs/" + (String) mapSelector.getValue() + ".json");
     	edgeListConversion = JsonParser.getJsonContentEdge("Graphs/" + (String) mapSelector.getValue() + "Edges.json");
     	edgeList = convertEdgeData(edgeListConversion);
     	System.out.println(mapSelector.getValue());*/
     	
     	/* ^^^^^^^^^
     	 * IMPORTANT, THE PROGRAM WILL NOT RUN IF WE DONT HAVE ACTUAL FILES
     	 * WHERE THESE PATHS ARE POINTING TO, FOR NOW, CREATE TEMP ONES AND THEN
     	 * OVERRIDE THEM.
     	 */
     	/*
    	File newMapFile = new File("CS3733_Graphics/" + (String) mapSelector.getValue() + ".png"); //MUST ADD png extension!
    	Image mapImage = new Image(newMapFile.toURI().toString());
    	ImageView imageView = new ImageView();
    	imageView.setImage(mapImage);
    	imageView.setLayoutX(0);  
    	imageView.setLayoutY(0);
    	imageView.resize(800, 600); //incase map is not already scaled perfectly
    	root.getChildren().add(imageView); 
          
    	root.getChildren().add(canvas);
        //drawEdges(edgeList, gc, root);
        
    }*/
    
}