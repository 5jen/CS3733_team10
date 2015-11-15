package maptool;

import java.io.File;
import java.util.LinkedList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
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
import javafx.stage.Stage;
import node.Place;
import node.Node;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import javafx.*;
import javafx.application.Application;


public class MapTool extends Application{
	public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage primaryStage) {
        
    	/*Initialize the nodes
    	 * -Very expandable- can initialize classes for each building.
    	 */
    	final Pane root = new Pane();
    	final Scene scene = new Scene(root, 1050, 700);//set size of scene
          
    	//Create a label and box for warnings, ie when the coordinates are outside the name
    	final HBox warningBox = new HBox(0); 
    	final Label warningLabel = new Label("");
    	warningLabel.setTextFill(Color.RED);
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
        controls.setLayoutX(10);
        controls.setLayoutY(640);
        controls.getChildren().addAll(xField, yField, nameField, isPlace, createNodeButton);  
  
        File mapFile = new File("CS3733_Graphics/AK2.png");
        Image mapImage = new Image(mapFile.toURI().toString());
        ImageView imageView = new ImageView();
        imageView.setImage(mapImage);
        imageView.setLayoutX(0);  
        imageView.setLayoutY(0);  
        
        //Attach everything to the screen
        root.getChildren().add(controls); 
        root.getChildren().add(controlLabels);
        root.getChildren().add(imageView);  
  
        final EventHandler<ActionEvent> moveHandler = new EventHandler<ActionEvent>() {  
            @Override  
            public void handle(ActionEvent event) {  
                
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
                
            	//passes all validity checks, create waypoint and add button
                else{
                	warningLabel.setText("");//Remove warning, bc successful
                	//If we are creating an actual place
                	if(isPlace.isSelected()){
                		Place newPlace = new Place(x, y, true, nameField.getText());
                    	Button newNodeButton = new Button("");
                    	newNodeButton.setStyle(
                                "-fx-background-radius: 5em; " +
                                "-fx-min-width: 15px; " +
                                "-fx-min-height: 15px; " +
                                "-fx-max-width: 15px; " +
                                "-fx-max-height: 15px;"
                        );
                    	newNodeButton.relocate(x, y);
                    	root.getChildren().add(newNodeButton);
                	}
                	//creating a way point
                	else{
                		Node newNode = new Node(x, y, true, nameField.getText());
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
                    	root.getChildren().add(newNodeButton);
                	}
                	 
                	
                	//After placing node on screen, save it to a external file (wait for yang)
                }
            	
            }  
        }; 
        
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	//Set the location coordinates in the input boxes
            	xField.setText(Integer.toString((int)event.getX()));
            	yField.setText(Integer.toString((int)event.getY()));
            }
        });
        

        
        createNodeButton.setOnAction(moveHandler);  
        xField.setOnAction(moveHandler);  
        yField.setOnAction(moveHandler);  
  
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
    
    
}
