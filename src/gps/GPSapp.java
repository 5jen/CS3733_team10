package gps;

import java.io.File;
import java.util.LinkedList;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ui.Node;
import ui.NodeList;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import javafx.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.effect.Light.Point;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
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
    	final Pane root = new Pane();  
        final HBox controls = new HBox(25);  
        final TextField startField = new TextField("Start");  
        final TextField endField = new TextField("Destination");  
        final Button findRouteButton = new Button("Find Route");
        controls.setLayoutX(10);
        controls.setLayoutY(625);
        
        controls.getChildren().addAll(startField, endField, findRouteButton);  
  
        File mapFile = new File("CS3733_Graphics/AK2.png");
        Image mapImage = new Image(mapFile.toURI().toString());
        ImageView imageView = new ImageView();
        imageView.setImage(mapImage);
        
        imageView.setLayoutX(0);  
        imageView.setLayoutY(0);  
        root.getChildren().add(controls);  
        root.getChildren().add(imageView);  
  
        final EventHandler<ActionEvent> moveHandler = new EventHandler<ActionEvent>() {  
            @Override  
            public void handle(ActionEvent event) {  
                double x = Double.parseDouble(startField.getText());  
                double y = Double.parseDouble(endField.getText());  
                imageView.setLayoutX(x);  
                imageView.setLayoutY(y);  
            }  
        };  
        findRouteButton.setOnAction(moveHandler);  
        startField.setOnAction(moveHandler);  
        endField.setOnAction(moveHandler);  
  
        primaryStage.setScene(new Scene(root, 1050, 700));  
        primaryStage.show();  
    }  
  
    	/*
    	
        
        // Create Image and ImageView objects
    	File mapFile = new File("CS3733_Graphics/AK2.png");
        Image mapImage = new Image(mapFile.toURI().toString());
        ImageView imageView = new ImageView();
        //imageView.setImage(mapImage);
        imageView.setX(0);
        imageView.setY(0);
      
        // Display image on screen
        StackPane root = new StackPane();
        root.getChildren().add(imageView);
        
        //Set size of the screen
        Scene scene = new Scene(root, 1100, 900);
        
        
        //Add buttons to screen and give them actions
        Button calculate = new Button("Calculate");
        calculate.setLayoutX(700);
        calculate.setLayoutY(500);
        calculate.setOnAction(new EventHandler<ActionEvent>() {
        	 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Calculate route from Daka to Library");
                LinkedList<Node> route = places.daka.findRoute(places.library, new LinkedList<Node>());
                drawRoute(gc, route);
            }

        });
        
        //TESTING making node images buttons...........
        places.daka.button.setLayoutX(places.daka.X);
        places.daka.button.setLayoutY(places.daka.Y);
        places.daka.button.setOnAction(new EventHandler<ActionEvent>() {
        	 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
        //Add scene to the stage
        //root.getChildren().add(calculate);
        primaryStage.setTitle("Image Read Test");
        primaryStage.setScene(scene);
        primaryStage.show();*/
    
}
