//package main;

package ui;

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

/*
 * /Users/andrewrottier/Desktop/CS3733 Graphcs
 */
 
public class BasicOpsTest extends Application {
 
    public static void main(String[] args) {
        launch(args);
    }
 
    @Override
    public void start(Stage primaryStage) {
        
    	/*Initialize the nodes
    	 * -Very expandable- can initialize classes for each building.
    	 */
    	NodeList places = new NodeList();
    	
        
        // Create Image and ImageView objects
    	File mapFile = new File("/Users/andrewrottier/Documents/CS_3733_team10/CS3733_Graphics/sample_map.jpg");
        Image mapImage = new Image(mapFile.toURI().toString());
        ImageView imageView = new ImageView();
        imageView.setImage(mapImage);
      
        // Display image on screen
        StackPane root = new StackPane();
        root.getChildren().add(imageView);
        
        //Set size of the screen
        Scene scene = new Scene(root, 742, 370);
        
        //create a canvas to paint onto over the scene
        Canvas canvas = new Canvas(800, 650);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        drawShapes(gc, places);
        root.getChildren().add(canvas);
        
        //add edges between the nodes
        addEdges( gc,  places);
        
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
        /*places.daka.button.setLayoutX(places.daka.X);
        places.daka.button.setLayoutY(places.daka.Y);
        places.daka.button.setOnAction(new EventHandler<ActionEvent>() {
        	 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });*/
        
        //Add scene to the stage
        root.getChildren().add(calculate);
        primaryStage.setTitle("Image Read Test");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    
    private void drawShapes(GraphicsContext gc, NodeList places) {
        
        //DRAW NODES
        ///Offset for pin image
        int offsetX = 9;
        int offsetY = 29;
        
        //Create nodes for locations around campus** 
        gc.drawImage(places.daka.nodeimg, places.daka.X, places.daka.Y);
        gc.drawImage(places.reccenter.nodeimg, places.reccenter.X, places.reccenter.Y);
        gc.drawImage(places.harrington.nodeimg, places.harrington.X, places.harrington.Y);
        gc.drawImage(places.higginscorner.nodeimg, places.higginscorner.X, places.higginscorner.Y);
        gc.drawImage(places.campuscenter.nodeimg, places.campuscenter.X, places.campuscenter.Y);
        gc.drawImage(places.fountain.nodeimg, places.fountain.X, places.fountain.Y);
        gc.drawImage(places.library.nodeimg, places.library.X, places.library.Y);
        
       //draw pins in map (start - dest)
        File pinFile = new File("/Users/andrewrottier/Documents/CS_3733_team10/CS3733_Graphics/pin.png");
        Image pin = new Image(pinFile.toURI().toString());
        gc.drawImage(pin, places.daka.X-3, places.daka.Y-15);
        gc.drawImage(pin, places.library.X-3, places.library.Y-15);
        
    }
    
    private void drawRoute(GraphicsContext gc, LinkedList<Node> route) {
    	gc.setStroke(Color.BLUE);
        gc.setLineWidth(5);
        //printRoute(route);
        
    	//iterate through the route drawing a connection between nodes
    	for(int i = 1; i < route.size(); i ++){ 
	  		gc.strokeLine(route.get(i-1).X+5, route.get(i-1).Y+5, route.get(i).X+5, route.get(i).Y+5); 

    	}
		
	}
    
    public void printRoute(LinkedList<Node> nodes){
		System.out.println(Arrays.toString(nodes.toArray()));
	}
    

    /*Method to be implemented...
     * 
     */
    private void addEdges(GraphicsContext gc, NodeList places){
    	//Add edges to each node, places that you can get to form each node
        
        //daka
        places.daka.edges.add(places.reccenter);
        places.daka.edges.add(places.harrington);
        //recenter
        places.reccenter.edges.add(places.daka);
        places.reccenter.edges.add(places.harrington);
        //harrington
        places.harrington.edges.add(places.reccenter);
        places.harrington.edges.add(places.daka);
        places.harrington.edges.add(places.higginscorner);
        //higginscorner
        places.higginscorner.edges.add(places.harrington);
        places.higginscorner.edges.add(places.campuscenter);
        //campuscenter
        places.campuscenter.edges.add(places.higginscorner);
        places.campuscenter.edges.add(places.fountain);
        //fountain
        places.fountain.edges.add(places.campuscenter);
        places.fountain.edges.add(places.library);
        //library
        places.library.edges.add(places.fountain);
    }
}