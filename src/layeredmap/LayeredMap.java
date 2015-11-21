package layeredmap;

import java.io.File;
import java.util.LinkedList;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import node.Edge;
import node.Node;
import node.EdgeDataConversion;
import node.Graph;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;

import io.JsonParser;


import javafx.stage.Stage;

public class LayeredMap extends Application {
	public static void main(String[] args) {
        launch(args);
    }
	
	Canvas canvas = new Canvas(800, 650);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    boolean expanding = false;
    Group g1 = new Group();
    Group g2 = new Group();
    Group g3 = new Group();
	
	 public void start(Stage primaryStage) {
		 
		 final Pane root = new Pane(); 
		 
		 File ak1file = new File("CS3733_Graphics/AK1.png");
		 File ak2file = new File("CS3733_Graphics/AK2.png");
		 File ak3file = new File("CS3733_Graphics/AK3.png");
	     Image ak1 = new Image(ak1file.toURI().toString());
	     Image ak2 = new Image(ak2file.toURI().toString());
	     Image ak3 = new Image(ak3file.toURI().toString());
	     ImageView ak1Image = new ImageView();
	     ImageView ak2Image = new ImageView();
	     ImageView ak3Image = new ImageView();
	     ak1Image.setImage(ak1);
	     ak2Image.setImage(ak2);
	     ak3Image.setImage(ak3);
	     
	     
	     ak2Image.setLayoutX(10);  
	     ak2Image.setLayoutY(20);
	     
	     ak1Image.setLayoutX(20);  
	     ak1Image.setLayoutY(40);
	     
	     //double width = ak1Image.getFitWidth();
	     //double height = ak1Image.getFitHeight();
	     double width = 800;
	     double height = 600;
	        
	     //set perspective transformations to all 3 groups
	     PerspectiveTransform pt = new PerspectiveTransform();
	     pt = setCorners(pt, width, height);
	     
	     
	        
	     g1.setEffect(pt);
	     //g.setCache(true);
	     //sets group g x and y
	     //g.setLayoutY(200);
	      
	     //g.getChildren().add(ak1Image);
	     //g.getChildren().add(ak2Image);
	     //g.getChildren().add(ak3Image);

	     //** ATTACH EACH IMAGE TO ITS OWN GROUP AND THEN MOVE EACH GROUP UP INDIVIDUALLY
	     root.getChildren().add(g1);
	     
	     RadioButton expand = new RadioButton("Expand");
	     expand.setLayoutX(400);
	     root.getChildren().add(expand);
	     //Add button actions
	     expand.setOnMouseMoved(new EventHandler<MouseEvent>() {
	    	 public void handle(MouseEvent event) {
	    		 //g.setLayoutY(g.getLayoutY()-2);
	    		 root.setPrefWidth(root.getWidth() + 100) ; 
	    		 root.setPrefHeight(root.getHeight() + 100) ;
	    		g1.getChildren().get(1).setLayoutY(g1.getChildren().get(1).getLayoutY()-2);;
	    		//root.getChildren().add(g);
	    		 //moveMaps();
	    		 
	    	 }

			
	     });
	     
	     primaryStage.setScene(new Scene(root, 600, 400));  
	     primaryStage.show(); 
	        
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

	public void moveMaps(){
		 if(expanding){
			 //g.setLayoutY(g.getLayoutY()-20);
		 }
	 }
	 
}
