package maptool;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import io.JsonParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import node.Edge;
import node.EdgeDataConversion;
import node.Node;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class MapTool extends Application{
	boolean delete = false;
	boolean startCoord, endCoord  = false;
	double startX, startY, endX, endY = 0.0;

	public static void main(String[] args) {
        launch(args);
    }
	
	JsonParser json = new JsonParser();
	LinkedList<Node> nodeList = JsonParser.getJsonContent("Graphs/AK1.json");
	LinkedList<EdgeDataConversion> edgeListConversion = JsonParser.getJsonContentEdge("Graphs/AK1Edges.json");
	LinkedList<Edge> edgeList = convertEdgeData(edgeListConversion);
	Canvas canvas = new Canvas(800, 600);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	Boolean drawEdgeBool = true;
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
    
    ObservableList<String> mapOptions = FXCollections.observableArrayList("AK1", "AK2", "AK3");
	final ComboBox<String> mapSelector = new ComboBox<String>(mapOptions);
    
    final Label fromField = new Label("");
    final Label toField = new Label("");
    final Label updateNodeLabel = new Label("");
    
    
    final Pane root = new Pane();
	
    //create actual map
    File mapFile = new File("CS3733_Graphics/AK1.png");
    Image mapImage = new Image(mapFile.toURI().toString());
    ImageView imageView = new ImageView();
    
 
    @Override
    public void start(Stage primaryStage) {
    	
    	//final Pane root = new Pane();
    	final Scene scene = new Scene(root, 1050, 700);//set size of scene
    	
    	
    	//Create a map selection drop down menu
    	final VBox mapSelectionBoxV = new VBox(5);
    	final Label mapSelectorLabel = new Label("Choose map");
    	mapSelectorLabel.setTextFill(Color.WHITE);
    	mapSelectorLabel.setFont(Font.font ("manteka", 14));
    	final HBox mapSelectionBoxH = new HBox(5);
    	final Button LoadMapButton = new Button("Load Map");
    	mapSelector.setValue("AK1");
    	mapSelectionBoxH.getChildren().addAll(mapSelector, LoadMapButton);
    	mapSelectionBoxV.setLayoutX(830);
    	mapSelectionBoxV.setLayoutY(620);
    	mapSelectionBoxV.getChildren().addAll(mapSelectorLabel, mapSelectionBoxH);
          
    	//Create a label and box for warnings, ie when the coordinates are outside the name
    	final HBox warningBox = new HBox(0); 
    	final Label warningLabel = new Label("");
    	warningLabel.setTextFill(Color.WHITE);
    	warningBox.setLayoutX(20);
    	warningBox.setLayoutY(620);
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
        controlLabels.getChildren().addAll(xFieldName, xField, yFieldName, yField, zFieldName, zField, nameFieldName, nameField, nodeTypeName, typeSelector, isPlaceName, isPlaceUpdateLabelBox, NodeCreationBox,deleteNodeButton);  

        
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
        edgeControls.setLayoutY(480);
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
        root.getChildren().add(imageView);
        
        root.getChildren().add(mapSelectionBoxV);
        root.getChildren().add(edgeControls);
        //root.getChildren().add(controls); 
        root.getChildren().add(controlLabels);
        
        drawEdges(edgeList, gc, root); //from here we draw the nodes so that nodes are on top of the edges
        //root.getChildren().add(canvas);
        //drawNodes(nodeList, root, fromField, toField);
        
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
            	
                //check to see if coordinates are within map bounds
                if(!isInBounds(x, y)){
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
                	
                	Node newPlace = new Node(x-7, y-7, z, (String) mapSelector.getValue()+nameField.getText(), (String) mapSelector.getValue(), true, isPlace.isSelected(), typeSelector.getValue());
                	// TODO Update these to use coordinate transformations dependent on the current map that's selected
					newPlace.setGlobalX(x*Math.cos(0)+y*Math.sin(0) + 200);
                	newPlace.setGlobalY(x*Math.cos(0)+y*Math.sin(0) + 200);
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
                            	startX = newNodeButton.getLayoutX()+8;
                            	startY = newNodeButton.getLayoutY()+8;
                            	fromField.setText(newPlace.getName());
                            	startCoord = true;
                            }
                            else if(!endCoord){
                            	endX = newNodeButton.getLayoutX()+8;
                            	endY = newNodeButton.getLayoutY()+8;
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
                    		updateNodeLabel.setText(""+newPlace.getName());
                        }
                    		
                    });
                    root.getChildren().add(newNodeButton); //add to the screen
                    newNodeButton.relocate(newX, newY);
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
                			nodeList.get(i).setGlobalX(x*Math.cos(0)+y*Math.sin(0) + 200);
                			nodeList.get(i).setGlobalY(x*Math.cos(0)+y*Math.sin(0) + 200);
                			//root.getChildren().remove(nodeButtonReference);
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
            	
            	root.getChildren().remove(canvas);
           		root.getChildren().remove(imageView); //remove current map, then load new one
           		nodeList.clear(); 
           		edgeListConversion.clear();
           		edgeList.clear();
            	nodeList = JsonParser.getJsonContent("Graphs/" + (String) mapSelector.getValue() + ".json");
            	edgeListConversion = JsonParser.getJsonContentEdge("Graphs/" + (String) mapSelector.getValue() + "Edges.json");
            	edgeList = convertEdgeData(edgeListConversion);
            	System.out.println(mapSelector.getValue());
            	
            	/* ^^^^^^^^^
            	 * IMPORTANT, THE PROGRAM WILL NOT RUN IF WE DONT HAVE ACTUAL FILES
            	 * WHERE THESE PATHS ARE POINTING TO, FOR NOW, CREATE TEMP ONES AND THEN
            	 * OVERRIDE THEM.
            	 */
            	
           		File newMapFile = new File("CS3733_Graphics/" + (String) mapSelector.getValue() + ".png"); //MUST ADD png extension!
           		Image mapImage = new Image(newMapFile.toURI().toString());
           		ImageView imageView = new ImageView();
           		imageView.setImage(mapImage);
           		imageView.setLayoutX(0);  
           		imageView.setLayoutY(0);
           		imageView.resize(800, 600); //incase map is not already scaled perfectly
           		root.getChildren().add(imageView); 
                
            	drawEdges(edgeList, gc, root);
            	
            	//updateNodeLabel.setText("");
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
        
        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
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
            	Node fromNode = new Node(0, 0, 0, "", "", false, false, "");
            	Node toNode = new Node(0, 0, 0, "", "", false, false, "");
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
            	edgeList.add(newEdge);
            	Line line = new Line();
            	 line.setStartX(startX);
                 line.setStartY(startY);
                 line.setEndX(endX);
                 line.setEndY(endY);
                 line.setStrokeWidth(3);
                 line.setStyle("-fx-background-color:  #F0F8FF; ");
                 root.getChildren().add(line);
                 
                 line.setOnMouseClicked(new EventHandler<MouseEvent>(){
                	 public void handle(MouseEvent event){
                		if(delete) {
                			root.getChildren().remove(line);
                			edgeList.remove(newEdge);
                			System.out.println(edgeList);
                			delete = false;
                		}
                	 }
                 });
            }
        });
       
       
       //Add actions to the Load Map button
       LoadMapButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
           public void handle(MouseEvent event) {
               //clear existing node list
        	   root.getChildren().remove(canvas);
           		root.getChildren().remove(imageView); //remove current map, then load new one
           		nodeList.clear(); 
           		edgeListConversion.clear();
           		edgeList.clear();
            	nodeList = JsonParser.getJsonContent("Graphs/" + (String) mapSelector.getValue() + ".json");
            	edgeListConversion = JsonParser.getJsonContentEdge("Graphs/" + (String) mapSelector.getValue() + "Edges.json");
            	edgeList = convertEdgeData(edgeListConversion);
            	System.out.println(mapSelector.getValue());
            	
            	/* ^^^^^^^^^
            	 * IMPORTANT, THE PROGRAM WILL NOT RUN IF WE DONT HAVE ACTUAL FILES
            	 * WHERE THESE PATHS ARE POINTING TO, FOR NOW, CREATE TEMP ONES AND THEN
            	 * OVERRIDE THEM.
            	 */
            	
           		File newMapFile = new File("CS3733_Graphics/" + (String) mapSelector.getValue() + ".png"); //MUST ADD png extension!
           		Image mapImage = new Image(newMapFile.toURI().toString());
           		ImageView imageView = new ImageView();
           		imageView.setImage(mapImage);
           		imageView.setLayoutX(0);  
           		imageView.setLayoutY(0);
           		imageView.resize(800, 600); //incase map is not already scaled perfectly
           		root.getChildren().add(imageView); 
                
           		root.getChildren().add(canvas);
                drawEdges(edgeList, gc, root);
           }
           
       });
       
       
       if(drawEdgeBool){
    	   int i;
       	for( i = 0; i < edgeList.size(); i++){
       		int j = i; //used for getting edge index inside method scope
       		
       		Line line = new Line();
       		//Determine the offset we need to use for the tool graph FROM NODE
       		if(edgeList.get(i).getFrom().getIsPlace()){
       			line.setStartX(edgeList.get(i).getFrom().getX()+8);
                line.setStartY(edgeList.get(i).getFrom().getY()+8);
       		} else{
       			line.setStartX(edgeList.get(i).getFrom().getX()+5);
                line.setStartY(edgeList.get(i).getFrom().getY()+5);
                
       		}
       		//Determine the offset we need to use for the tool graph TO NODE
       		if(edgeList.get(i).getTo().getIsPlace()){
       			line.setEndX(edgeList.get(i).getTo().getX()+8);
                line.setEndY(edgeList.get(i).getTo().getY()+8);
       		} else {
       			line.setEndX(edgeList.get(i).getTo().getX()+5);
                line.setEndY(edgeList.get(i).getTo().getY()+5);
       		}
       		
          	
            line.setStrokeWidth(3);
            //line.setStyle("-fx-background-color:  #F0F8FF; ");
            root.getChildren().add(line);
            
       		line.setOnMouseClicked(new EventHandler<MouseEvent>(){
              	public void handle(MouseEvent event){
              		if(delete) {
              			root.getChildren().remove(line);
              			edgeList.remove(edgeList.get(j));
              			System.out.println("Deleted edge");
              			delete = false;
              		}
              	 }
               });
       		}
       	//System.out.println("Drew edges");
       	drawEdgeBool = false;
       }
       
        createNodeButton.setOnAction(CreateHandler);  
        
        primaryStage.setScene(scene);  
        primaryStage.show();  
        
    }  
    
    //Change where we call drawEdges to just change the drawEdgeBool to true;
    private void drawEdges(LinkedList<Edge> edges, GraphicsContext gc, Pane root){
    	root.getChildren().remove(canvas);
    	gc.clearRect(0, 0, 800, 600);
    	int i;
        for( i = 0; i < edgeList.size(); i++){
       		int j = i;
       		Line line = new Line();
       	//Determine the offset we need to use for the tool graph FROM NODE
       		if(edgeList.get(i).getFrom().getIsPlace()){
       			line.setStartX(edgeList.get(i).getFrom().getX()+8);
                line.setStartY(edgeList.get(i).getFrom().getY()+8);
       		} else{
       			line.setStartX(edgeList.get(i).getFrom().getX()+5);
                line.setStartY(edgeList.get(i).getFrom().getY()+5);
                
       		}
       		//Determine the offset we need to use for the tool graph TO NODE
       		if(edgeList.get(i).getTo().getIsPlace()){
       			line.setEndX(edgeList.get(i).getTo().getX()+8);
                line.setEndY(edgeList.get(i).getTo().getY()+8);
       		} else {
       			line.setEndX(edgeList.get(i).getTo().getX()+5);
                line.setEndY(edgeList.get(i).getTo().getY()+5);
       		}
       		line.setStrokeWidth(3);
             root.getChildren().add(line);
               
       		line.setOnMouseClicked(new EventHandler<MouseEvent>(){
              	public void handle(MouseEvent event){
              		if(delete) {
              			root.getChildren().remove(line);
              			edgeList.remove(edgeList.get(j));
              			System.out.println("Deleted edge");
              			delete = false;
              		}
              	 }
               });
       		}
       	System.out.println("Drew edges");
       	drawEdgeBool = false;
    	root.getChildren().add(canvas);

       	drawNodes(nodeList, root, fromField, toField);

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
    public int getDistance(){
    	return (int) Math.sqrt((Math.pow(((int)startX - (int)endX), 2)) + (Math.pow(((int)startY - (int)endY), 2)));
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
            }
            newNodeButton.relocate(nodes.get(i).getX(), nodes.get(i).getY());
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
                    	startX = newNodeButton.getLayoutX()+8;
                    	startY = newNodeButton.getLayoutY()+8;
                    	fromField.setText(newPlace.getName());
                    	startCoord = true;
                    }
                    else if(!endCoord){
                    	endX = newNodeButton.getLayoutX()+8;
                    	endY = newNodeButton.getLayoutY()+8;
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
    	Node fromNode = new Node(0, 0, 0, "", "", false, false, "");
    	Node toNode = new Node(0, 0, 0, "", "", false, false, "");
    	
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
    	String path = "Graphs/" + (String) mapSelector.getValue() + ".json";
    	try {
			JsonParser.saveFile(nodeData, path);
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
    	//save edges
    	String edgeData = JsonParser.jsonToStringEdge(edgeList);
    	System.out.println(edgeData);
    	String edgePath = "Graphs/" + (String) mapSelector.getValue() + "Edges.json";
    	try {
			JsonParser.saveFile(edgeData, edgePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    //NOT WORKING RIGHT NOW
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
     	System.out.println(mapSelector.getValue());
     	
     	/* ^^^^^^^^^
     	 * IMPORTANT, THE PROGRAM WILL NOT RUN IF WE DONT HAVE ACTUAL FILES
     	 * WHERE THESE PATHS ARE POINTING TO, FOR NOW, CREATE TEMP ONES AND THEN
     	 * OVERRIDE THEM.
     	 */
     	
    	File newMapFile = new File("CS3733_Graphics/" + (String) mapSelector.getValue() + ".png"); //MUST ADD png extension!
    	Image mapImage = new Image(newMapFile.toURI().toString());
    	ImageView imageView = new ImageView();
    	imageView.setImage(mapImage);
    	imageView.setLayoutX(0);  
    	imageView.setLayoutY(0);
    	imageView.resize(800, 600); //incase map is not already scaled perfectly
    	root.getChildren().add(imageView); 
          
    	root.getChildren().add(canvas);
        drawEdges(edgeList, gc, root);
        
    }
    
}