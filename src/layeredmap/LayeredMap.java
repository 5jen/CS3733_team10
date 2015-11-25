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
import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import node.Edge;
import node.Node;
import node.EdgeDataConversion;
import node.Graph;
import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.VLineTo;
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
    Group bigGroup = new Group();
    final Label floorSelected = new Label();
	
	 public void start(Stage primaryStage) {
		 
		 final Pane root = new Pane(); 
		 
		 //add all of the image groups to the big group so we can move them all at once
		 bigGroup.getChildren().addAll(g1, g2, g3);
		 
		 floorSelected.setText("...");
		 floorSelected.setLayoutX(500);
		 floorSelected.setLayoutY(500);
		 root.getChildren().add(floorSelected);
		 
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
	     
	     double width = 80;
	     double height = 60;
	        
	     //set perspective transformations to all 3 groups
	     PerspectiveTransform pt = new PerspectiveTransform();
	     pt = setCorners(pt, width, height);
	     
	     final DropShadow shadow = new DropShadow();
	     shadow.setInput(pt);
	     pt = setCorners(pt, width, height);
	     
	     g1.setEffect(pt);
	     g2.setEffect(pt);
	     g3.setEffect(pt);
	     //g.setCache(true);
	     //sets group g x and y
	     g1.setLayoutY(120);
	     g2.setLayoutY(110);
	     g3.setLayoutY(100);
	      
	     g1.getChildren().add(ak1Image);
	     g2.getChildren().add(ak2Image);
	     g3.getChildren().add(ak3Image);

	     //** ATTACH EACH IMAGE TO ITS OWN GROUP AND THEN MOVE EACH GROUP UP INDIVIDUALLY
	     root.getChildren().addAll(g1, g2, g3);
	     root.getChildren().addAll(bigGroup);
	     
	     Button expand = new Button("Expand");
	     expand.setLayoutX(400);
	     root.getChildren().add(expand);
	     //Add button actions
	     expand.setOnMouseClicked(new EventHandler<MouseEvent>() {
	    	 public void handle(MouseEvent event) {
	    		 bigGroup.setLayoutX(bigGroup.getLayoutX()+20);
	    	 }

	     });
	     
	     //floorSelected
	     g3.setOnMouseClicked(new EventHandler<MouseEvent>() {
	    	 public void handle(MouseEvent event) {
	    		 floorSelected.setText("AK3");
	    	 }
	     });
	     g3.setOnMouseMoved(new EventHandler<MouseEvent>() {
	    	 public void handle(MouseEvent event) {
	    		 floorSelected.setText("AK3");
	    		 g3.setEffect(shadow);
	    	 }
	     });
	     g3.setOnMouseExited(new EventHandler<MouseEvent>() {
	    	 public void handle(MouseEvent event) {
	    		 floorSelected.setText("...");
	    		 PerspectiveTransform pt = new PerspectiveTransform();
	    		 pt = setCorners(pt, width, height);
	    		 g3.setEffect(pt);
	    	 }
	     });
	     
	     g2.setOnMouseClicked(new EventHandler<MouseEvent>() {
	    	 public void handle(MouseEvent event) {
	    		 floorSelected.setText("AK2");
	    	 }
	     });
	     g2.setOnMouseMoved(new EventHandler<MouseEvent>() {
	    	 public void handle(MouseEvent event) {
	    		 floorSelected.setText("AK2");
	    		 g2.setEffect(shadow);
	    	 }
	     });
	     g2.setOnMouseExited(new EventHandler<MouseEvent>() {
	    	 public void handle(MouseEvent event) {
	    		 floorSelected.setText("...");
	    		 PerspectiveTransform pt = new PerspectiveTransform();
	    		 pt = setCorners(pt, width, height);
	    		 g2.setEffect(pt);
	    	 }
	     });

	     g1.setOnMouseClicked(new EventHandler<MouseEvent>() {
	    	 public void handle(MouseEvent event) {
	    		 floorSelected.setText("AK1");
	    	 }
	     });
	     g1.setOnMouseMoved(new EventHandler<MouseEvent>() {
	    	 public void handle(MouseEvent event) {
	    		 floorSelected.setText("AK1");
	    		 g1.setEffect(shadow);
	    	 }
	     });
	     g1.setOnMouseExited(new EventHandler<MouseEvent>() {
	    	 public void handle(MouseEvent event) {
	    		 floorSelected.setText("...");
	    		 PerspectiveTransform pt = new PerspectiveTransform();
	    		 pt = setCorners(pt, width, height);
	    		 g1.setEffect(pt);
	    	 }
	     });
	     
	     
	     primaryStage.setScene(new Scene(root, 800, 600));  
	     primaryStage.show(); 
	     applyAnimation();  
	     
	     
	        
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
	 
	 
     void applyAnimation(){
    	 
    	 //Floor 3
    	 Path g3path = new Path();
    	 MoveTo g3moveTo = new MoveTo();
    	 g3moveTo.setX(400.0f);
    	 g3moveTo.setY(400.0f);
    	 LineTo g3lineTo = new LineTo();
    	 g3lineTo.setX(400.0f);
    	 g3lineTo.setY(310.0f);
    	 g3path.getElements().add(g3moveTo);
    	 g3path.getElements().add(g3lineTo);

		 PathTransition g3pt = new PathTransition();
		 g3pt.setDuration(Duration.millis(3000));
		 g3pt.setPath(g3path);
		 g3pt.setNode(g3);
		 g3pt.setOrientation(PathTransition.OrientationType.NONE);
		 g3pt.setCycleCount(Timeline.INDEFINITE);
		 g3pt.setAutoReverse(true);
		 g3pt.play();
		
		 //FLOOR 2
		 Path g2path = new Path();
    	 MoveTo g2moveTo = new MoveTo();
    	 g2moveTo.setX(400.0f);
    	 g2moveTo.setY(400.0f);
    	 LineTo g2lineTo = new LineTo();
    	 g2lineTo.setX(400.0f);
    	 g2lineTo.setY(340.0f);
    	 g2path.getElements().add(g2moveTo);
    	 g2path.getElements().add(g2lineTo);

		 PathTransition g2pt = new PathTransition();
		 g2pt.setDuration(Duration.millis(3000));
		 g2pt.setPath(g2path);
		 g2pt.setNode(g2);
		 g2pt.setOrientation(PathTransition.OrientationType.NONE);
		 g2pt.setCycleCount(Timeline.INDEFINITE);
		 g2pt.setAutoReverse(true);
		 g2pt.play();
		 
		 //FLOOR 1
		 Path g1path = new Path();
    	 MoveTo g1moveTo = new MoveTo();
    	 g1moveTo.setX(400.0f);
    	 g1moveTo.setY(400.0f);
    	 LineTo g1lineTo = new LineTo();
    	 g1lineTo.setX(400.0f);
    	 g1lineTo.setY(370.0f);
    	 g1path.getElements().add(g1moveTo);
    	 g1path.getElements().add(g1lineTo);

		 PathTransition g1pt = new PathTransition();
		 g1pt.setDuration(Duration.millis(3000));
		 g1pt.setPath(g1path);
		 g1pt.setNode(g1);
		 g1pt.setOrientation(PathTransition.OrientationType.NONE);
		 g1pt.setCycleCount(Timeline.INDEFINITE);
		 g1pt.setAutoReverse(true);
		 g1pt.play();
		 
     }

	public void moveMaps(){
		 if(expanding){
			 for(int i = 0; i < 70; i++){
				 g3.setLayoutY(g3.getLayoutY()-2);
				 g2.setLayoutY(g2.getLayoutY()-1);
			 }
			 expanding = false;
		 }
		 else{
			 for(int i = 0; i < 70; i++){
				 g3.setLayoutY(g3.getLayoutY()+2);
				 g2.setLayoutY(g2.getLayoutY()+1);
			 }
			 expanding = true;
		 }
	 }
	 
}
