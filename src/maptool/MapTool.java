package maptool;

import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import io.JsonParser;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import node.AbsNode;
import node.Edge;
import node.Graph;
import node.Node;
import node.Place;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import javafx.*;
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
 
    @Override
    public void start(Stage primaryStage) {
    	JsonParser json = new JsonParser();
    	LinkedList<AbsNode> nodeList = new LinkedList<AbsNode>();
    	LinkedList<Edge> edgeList = new LinkedList<Edge>();

        
    	/*Initialize the nodes
    	 * -Very expandable- can initialize classes for each building.
    	 */
    	final Pane root = new Pane();
    	final Scene scene = new Scene(root, 1050, 700);//set size of scene
    	
    	
    	//Create a map selection drop down menu
    	final VBox mapSelectionBoxV = new VBox(5);
    	final Label mapSelectorLabel = new Label("Choose map");
    	mapSelectorLabel.setTextFill(Color.WHITE);
    	final HBox mapSelectionBoxH = new HBox(5);
    	ObservableList<String> mapOptions = FXCollections.observableArrayList("AK0", "AK1", "AK2");
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
        final Label fromField = new Label("Start: ");
        final Label toField = new Label("End: ");
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
        root.getChildren().add(mapSelectionBoxV);
        root.getChildren().add(edgeControls);
        root.getChildren().add(controls); 
        root.getChildren().add(controlLabels);
        root.getChildren().add(imageView);  
        

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
                    	newNodeButton.relocate(x, y);
                    	Place newPlace = new Place(x, y, true, nameField.getText());
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
                            		startX = newNodeButton.getLayoutX()+ 8;
                            		startY = newNodeButton.getLayoutY() + 8;
                            		fromField.setText("Start: " + newPlace.getName());
                            		startCoord = true;
                            	}
                            	else if(!endCoord){
                            		endX = newNodeButton.getLayoutX() + 8;
                            		endY = newNodeButton.getLayoutY() + 8;
                            		toField.setText("End: " + newPlace.getName());
                            		startCoord = false;
                            		endCoord = false;
                            	}
                            }
                        });
                    	root.getChildren().add(newNodeButton); //add to the screen
                    	
                	}
                	//creating a way point
                	else{
                		Node newNode = new Node(x, y, true, nameField.getText());
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
                    	newNodeButton.relocate(x, y);
                       	newNodeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            public void handle(MouseEvent event) {
                            	//fromField.setText(newNodeButton.);
                            	if(delete){
                            		root.getChildren().remove(newNodeButton);
                            		nodeList.remove(newNode);
                            		delete = false;
                            	}
                            	else if(!startCoord){
                            		startX = newNodeButton.getLayoutX()+ 8;
                            		startY = newNodeButton.getLayoutY() + 8;
                            		fromField.setText("Start: " + newNode.getName());
                            		startCoord = true;
                            	}
                            	else if(!endCoord){
                            		endX = newNodeButton.getLayoutX() + 8;
                            		endY = newNodeButton.getLayoutY() + 8;
                            		toField.setText("End: " + newNode.getName());
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
            	String data = json.jsonToString(nodeList);
            	String path = "Graphs/" + (String) mapSelector.getValue() + ".json";
            	try {
					json.saveFile(data, path);
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
            	Edge newEdge = new Edge(nodeList.get(nodeList.size()-2), nodeList.get(nodeList.size()-1));
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
        	   nodeList.clear();
           		root.getChildren().remove(imageView); //remove current map, then load new one
           		
           		File newMapFile = new File("CS3733_Graphics/" + (String) mapSelector.getValue() + ".png"); //MUST ADD png extension!
           		Image mapImage = new Image(newMapFile.toURI().toString());
           		ImageView imageView = new ImageView();
           		imageView.setImage(mapImage);
           		imageView.setLayoutX(0);  
           		imageView.setLayoutY(0);
           		imageView.resize(800, 600); //incase map is not already scaled perfectly
           		root.getChildren().add(imageView); 

           }
           
       });
       
        createNodeButton.setOnAction(CreateHandler);  
  
        primaryStage.setScene(scene);  
        primaryStage.show();  
        
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
    
    
    public int getDistance(){
    	return (int) Math.sqrt((Math.pow(((int)startX - (int)endX), 2)) + (Math.pow(((int)startY - (int)endY), 2)));
    }
    
}
