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
import javafx.stage.Stage;
import node.AbsNode;
import node.Edge;
import node.EdgeDataConversion;
import node.Node;
import node.Place;

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
	LinkedList<AbsNode> nodeList = JsonParser.getJsonContent("Graphs/AK1.json");
	LinkedList<EdgeDataConversion> edgeListConversion = JsonParser.getJsonContentEdge("Graphs/AK1Edges.json");
	LinkedList<Edge> edgeList = convertEdgeData(edgeListConversion);
	Canvas canvas = new Canvas(800, 600);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	Boolean drawEdgeBool = true;
	boolean start, end = false;
	String startNode, endNode;
 
    @Override
    public void start(Stage primaryStage) {
    	
    	System.out.println("edgelist length" + edgeList.size());
    	final Pane root = new Pane();
    	final Scene scene = new Scene(root, 1050, 700);//set size of scene
    	
    	
    	//Create a map selection drop down menu
    	final VBox mapSelectionBoxV = new VBox(5);
    	final Label mapSelectorLabel = new Label("Choose map");
    	mapSelectorLabel.setTextFill(Color.WHITE);
    	final HBox mapSelectionBoxH = new HBox(5);
    	ObservableList<String> mapOptions = FXCollections.observableArrayList("AK1", "AK2", "AK3");
    	final ComboBox<String> mapSelector = new ComboBox<String>(mapOptions);
    	final Button LoadMapButton = new Button("Load Map");
    	mapSelector.setValue("AK1");
    	mapSelectionBoxH.getChildren().addAll(mapSelector, LoadMapButton);
    	mapSelectionBoxV.setLayoutX(820);
    	mapSelectionBoxV.setLayoutY(400);
    	mapSelectionBoxV.getChildren().addAll(mapSelectorLabel, mapSelectionBoxH);
          
    	//Create a label and box for warnings, ie when the coordinates are outside the name
    	final HBox warningBox = new HBox(0); 
    	final Label warningLabel = new Label("");
    	warningLabel.setTextFill(Color.WHITE);
    	warningBox.setLayoutX(10);
    	warningBox.setLayoutY(680);
    	warningBox.getChildren().addAll(warningLabel);  

    	
    	//Create input box field labels (on top of the text boxes)
        final HBox controlLabels = new HBox(115); 
        final Label xFieldName = new Label("X Coordinate");
        final Label yFieldName = new Label("Y Coordinate");
        final Label isPlaceName = new Label("Place?");
        final Label nameFieldName = new Label("Name");
        controlLabels.setLayoutX(10);
        controlLabels.setLayoutY(620);
        controlLabels.getChildren().addAll(xFieldName, yFieldName, nameFieldName, isPlaceName);  

        //Create the actual input boxes and button 
        final HBox controls = new HBox(25);
        final TextField xField = new TextField("");  
        final TextField yField = new TextField("");  
        final TextField nameField = new TextField(""); 
        //final TextField typeField = new TextField("Type"); 
        final RadioButton isPlace = new RadioButton();
        final Button createNodeButton = new Button("Create Node");
        final Button deleteNodeButton = new Button("Delete Node");
        controls.setLayoutX(10);
        controls.setLayoutY(640);
        controls.getChildren().addAll(xField, yField, nameField, isPlace, createNodeButton,deleteNodeButton);  
  
        //create vertical interface
        final VBox edgeControls = new VBox(20);
        final Label fromField = new Label("");
        final Label toField = new Label("");
        final Button createEdgeButton = new Button("Create Edge");
        final Button deleteEdgeButton = new Button("Delete Edge");
        final Button saveGraph = new Button("Save");
        edgeControls.setLayoutX(830);
        edgeControls.setLayoutY(20);
        edgeControls.getChildren().addAll(fromField, toField, createEdgeButton, deleteEdgeButton, saveGraph);  
  
        
        //create actual map
        File mapFile = new File("CS3733_Graphics/AK1.png");
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
        
        //Attach everything to the screen
        root.getChildren().add(bgView);
        root.getChildren().add(imageView);
        
        root.getChildren().add(mapSelectionBoxV);
        root.getChildren().add(edgeControls);
        root.getChildren().add(controls); 
        root.getChildren().add(controlLabels);
        
        drawEdges(edgeList, gc, root);
        root.getChildren().add(canvas);
        drawPlaces(nodeList, root, fromField, toField);
        
        

        final EventHandler<ActionEvent> CreateHandler = new EventHandler<ActionEvent>() {  
            @Override  
            public void handle(ActionEvent event) {  
                root.getChildren().remove(warningBox);
            	int x = -1, y = -1;
            	
            	try{
            		x = Integer.parseInt(xField.getText());  
            		y = Integer.parseInt(yField.getText());
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
                
            	//passes all validity checks, create waypoint and add button
                else{
                	warningLabel.setText("");//Remove warning, bc successful
                	//If we are creating an actual place
                	if(isPlace.isSelected()){
                    	Button newNodeButton = new Button("");
                    	newNodeButton.setStyle(
                                "-fx-background-radius: 5em; " +
                                "-fx-min-width: 15px; " +
                                "-fx-min-height: 15px; " +
                                "-fx-max-width: 15px; " +
                                "-fx-max-height: 15px;"
                        );
                    	newNodeButton.relocate(x-7, y-7);
                    	Place newPlace = new Place(x-7, y-7, true, nameField.getText());
                		nodeList.add(newPlace);
                    	//Add actions for when you click this unique button
                    	newNodeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            public void handle(MouseEvent event) {
                            	if(delete){
                            		root.getChildren().remove(newNodeButton);
                            		nodeList.remove(newPlace);
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
                            }
                        });
                    	root.getChildren().add(newNodeButton); //add to the screen
                    	
                	}
                	//creating a way point
                	else{
                		Node newNode = new Node(x-7, y-7, true, nameField.getText());
                		nodeList.add(newNode);
                    	Button newNodeButton = new Button("");
                    	newNodeButton.setStyle(
                    			"-fx-background-color: #000000; " +
                                "-fx-background-radius: 5em; " +
                                "-fx-min-width: 10px; " +
                                "-fx-min-height: 10px; " +
                                "-fx-max-width: 10px; " +
                                "-fx-max-height: 10px;"
                        );
                    	newNodeButton.relocate(x-7, y-7);
                       	newNodeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            public void handle(MouseEvent event) {
                            	if(delete){
                            		root.getChildren().remove(newNodeButton);
                            		nodeList.remove(newNode);
                            		delete = false;
                            	}
                            	else if(!startCoord){
                            		startX = newNodeButton.getLayoutX()+8;
                            		startY = newNodeButton.getLayoutY()+8;
                            		fromField.setText(newNode.getName());
                            		startCoord = true;
                            	}
                            	else if(!endCoord){
                            		endX = newNodeButton.getLayoutX()+8;
                            		endY = newNodeButton.getLayoutY()+8;
                            		toField.setText(newNode.getName());
                            		startCoord = false;
                            		endCoord = false;
                            	}
                            }
                        });
                    	root.getChildren().add(newNodeButton);
                	}
                          
                }
            	
            }  
        }; 
        
        //Save the Graph
        saveGraph.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	//save(nodeList);
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
            	AbsNode fromNode = new AbsNode(0, 0, false, false);
            	AbsNode toNode = new AbsNode(0, 0, false, false);
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
               	System.out.println("Drew edges");
               	drawEdgeBool = false;
               	drawPlaces(nodeList, root, fromField, toField);


           }
           
       });
       
       
       if(drawEdgeBool){
    	   int i;
       	for( i = 0; i < edgeList.size(); i++){
       		int j = i; //used for getting edge index inside method scope
       		//System.out.println( "from x: " + edges.get(i).getFrom().getX() + "from y: " + edges.get(i).getFrom().getY());
       		//System.out.println( "to x: " + edges.get(i).getTo().getX() + "to y: " + edges.get(i).getTo().getY());
       		//gc.setLineWidth(5);
       		//gc.strokeLine(edges.get(i).getFrom().getX(), edges.get(i).getFrom().getY(), edges.get(i).getTo().getX(),edges.get(i).getTo().getY());
       		
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
       	System.out.println("Drew edges");
       	drawEdgeBool = false;
       }
       
       
        createNodeButton.setOnAction(CreateHandler);  
        
        primaryStage.setScene(scene);  
        primaryStage.show();  
        
    }  
    
    //Change where we call drawEdges to just change the drawEdgeBool to true;
    private void drawEdges(LinkedList<Edge> edges, GraphicsContext gc, Pane root){
    	drawEdgeBool = true;
    	
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
    private void drawPlaces(LinkedList<AbsNode> nodes, Pane root, Label fromField, Label toField){
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
                    	if(delete){
                    		root.getChildren().remove(newNodeButton);
                    		nodeList.remove(newNode);
                    		delete = false;
                    	}
                    	else if(!startCoord){
                    		startX = newNodeButton.getLayoutX()+ 8;
                    		startY = newNodeButton.getLayoutY() + 8;
                    		fromField.setText(((Place) newNode).getName());
                    		startCoord = true;
                    	}
                    	else if(!endCoord){
                    		endX = newNodeButton.getLayoutX() + 8;
                    		endY = newNodeButton.getLayoutY() + 8;
                    		toField.setText(((Place) newNode).getName());
                    		startCoord = false;
                    		endCoord = false;
                    	}
                    }
                });
            	root.getChildren().add(newNodeButton);
    		} else if(!nodes.get(i).getIsPlace()){
        		Button newNodeButton = new Button("");
        		newNodeButton.setStyle(
            			"-fx-background-color: #000000; " +
                        "-fx-background-radius: 5em; " +
                        "-fx-min-width: 10px; " +
                        "-fx-min-height: 10px; " +
                        "-fx-max-width: 10px; " +
                        "-fx-max-height: 10px;"
                );
            	newNodeButton.relocate(nodes.get(i).getX(), nodes.get(i).getY());
            	AbsNode newNode = nodes.get(i);
            	newNodeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                    	if(delete){
                    		root.getChildren().remove(newNodeButton);
                    		nodeList.remove(newNode);
                    		delete = false;
                    	}
                    	else if(!startCoord){
                    		startX = newNodeButton.getLayoutX()+ 8;
                    		startY = newNodeButton.getLayoutY() + 8;
                    		fromField.setText( ((Node) newNode).getName());
                    		startCoord = true;
                    	}
                    	else if(!endCoord){
                    		endX = newNodeButton.getLayoutX() + 8;
                    		endY = newNodeButton.getLayoutY() + 8;
                    		toField.setText( ((Node) newNode).getName());
                    		startCoord = false;
                    		endCoord = false;
                    	}
                    }
                });
            	root.getChildren().add(newNodeButton);
    		}
	  		

    	}
    }
    
    private LinkedList<Edge> convertEdgeData(LinkedList<EdgeDataConversion> edgeData) {
    	LinkedList<Edge> edgeList = new LinkedList<Edge>();
    	AbsNode fromNode = new AbsNode(0, 0, delete, delete);
    	AbsNode toNode = new AbsNode(0, 0, delete, delete);
    	
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
    					fromNode = (Node)nodeList.get(j);
    				}
    				if(edgeListConversion.get(i).getTo().equals(( nodeList.get(j)).getName())){
    					toNode = (Node)nodeList.get(j);
    				}
    			}
    			
    		}
    		Edge newEdge = new Edge(fromNode, toNode, edgeListConversion.get(i).getDistance());
			edgeList.add(newEdge);
    	}
    	
    	return edgeList;
    }
    
}