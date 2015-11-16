package gps;

import java.io.File;
import java.util.LinkedList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import node.AbsNode;
import node.Place;
import ui.Node;
import ui.NodeList;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import javafx.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Light.Point;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;


public class GPSapp extends Application{
	public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage primaryStage) {
        
    	/*Initialize the nodes
    	 * -Very expandable- can initialize classes for each building.
    	 */
    	
    	//Create a map selection drop down menu
    	final VBox mapSelectionBoxV = new VBox(5);
    	final Label mapSelectorLabel = new Label("Choose map");
    	final HBox mapSelectionBoxH = new HBox(5);
    	ObservableList<String> mapOptions = 
    		    FXCollections.observableArrayList(
    		        "AK1",
    		        "AK2",
    		        "AK3"
    		    );
    	
    	final ComboBox mapSelector = new ComboBox(mapOptions);
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
    	
    	//Create input box field labels (on top of the text boxes)
        final HBox controlLabels = new HBox(160); 
        final Label xFieldName = new Label("Start");
        xFieldName.setTextFill(Color.WHITE);
        final Label yFieldName = new Label("Destination");
        yFieldName.setTextFill(Color.WHITE);
        controlLabels.setLayoutX(10);
        controlLabels.setLayoutY(620);
        controlLabels.getChildren().addAll(xFieldName, yFieldName);  

    	
    	//Create the menu interface for entering info
    	final Pane root = new Pane();  
        final HBox controls = new HBox(25);  
        final TextField startField = new TextField("");  
        final TextField endField = new TextField("");  
        final Button findRouteButton = new Button("Find Route");
        controls.setLayoutX(10);
        controls.setLayoutY(640);
        controls.getChildren().addAll(startField, endField, findRouteButton);  
  
        //Create the map image
        File mapFile = new File("CS3733_Graphics/AK2.png");
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
        
        //Add images to the screen
        root.getChildren().add(bgView); //Must add background image first!
        root.getChildren().add(mapSelectionBoxV);
        root.getChildren().add(controls);
        root.getChildren().add(controlLabels); 
        root.getChildren().add(imageView);  
  
        /*final EventHandler<ActionEvent> moveHandler = new EventHandler<ActionEvent>() {  
            @Override  
            public void handle(ActionEvent event) {  
                double x = Double.parseDouble(startField.getText());  
                double y = Double.parseDouble(endField.getText());  
                imageView.setLayoutX(x);  
                imageView.setLayoutY(y);  
            }  
        }; */ 
        
        final EventHandler<ActionEvent> moveHandler = new EventHandler<ActionEvent>() {  
            @Override  
            public void handle(ActionEvent event) {  
                root.getChildren().remove(warningBox); //clear any existing warning
            	
                //CHANGE TO MAKE SURE THEY ARE STRINGS (FOR IF SOMEONE MAY MANUALLY ENTER INPUT)
                //check to see if proper fields types given
                /*if(!isValidCoords(startField.getText())){
            		warningLabel.setText("Error, coordinates not valid");
            		root.getChildren().add(warningBox); 
            	}*/
                
            	//passes all validity checks, create waypoint and add button
                /*else{
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
                    	//Place newPlace = new Place(x, y, true, nameField.getText());
                		//nodeList.add(newPlace);
                    	
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
                    	
                    	//root.getChildren().add(newNodeButton); //add to the screen
                    	
                	}
                	
                	 
                	
                	//After placing node on screen, save it to a external file (wait for yang)
                }*/
            	
            }  
        }; 
        
        
        //Add actions to the Load Map button
        LoadMapButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	root.getChildren().remove(imageView); //remove current map, then load new one
            	
            	File newMapFile = new File("CS3733_Graphics/" + (String) mapSelector.getValue() + ".png"); //MUST ADD png extension!
                //System.out.println("CS3733_Graphics/" + (String) mapSelector.getValue()+"/.png");
            	Image mapImage = new Image(newMapFile.toURI().toString());
                ImageView imageView = new ImageView();
                imageView.setImage(mapImage);
                imageView.setLayoutX(0);  
                imageView.setLayoutY(0);
                imageView.resize(800, 600); //incase map is not already scaled perfectly
                root.getChildren().add(imageView); 
                //add nodes/node buttons to the screen
                //graph.drawEdges?
            }
        });
        
        //Add button actions
        findRouteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	 // Call findRoute on 2 nodes
                // LinkedList<AbsNode> route = findRoute();
                // Call Draw Route
                // drawRoute(root, route);
            }
        });
        
        findRouteButton.setOnAction(moveHandler);  
        startField.setOnAction(moveHandler);  
        endField.setOnAction(moveHandler);  
  
        primaryStage.setScene(new Scene(root, 1050, 700));  
        primaryStage.show();  
    }  
    
    
    private void drawRoute(Pane root, LinkedList<AbsNode> route) {
        
    	//iterate through the route drawing a connection between nodes
    	for(int i = 0; i < route.size(); i ++){  
	  		Line line = new Line();
	  		line.setStartX(route.get(i).getX() );
            line.setStartY(route.get(i).getY());
            line.setEndX(route.get(i+1).getX());
            line.setEndY(route.get(i+1).getY());
            line.setStrokeWidth(3);
            line.setStyle("-fx-background-color:  #F0F8FF; ");
            root.getChildren().add(line);

    	}
		
	}

    
}
