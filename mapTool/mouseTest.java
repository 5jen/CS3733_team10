package mapTool;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.shape.Line;
public class mouseTest extends Application {
    @Override
    public void start(Stage primaryStage) {
    	 Pane root = new Pane();
         Scene scene = new Scene(root, 300, 250);

        Button bt1 = new Button ("1"); 
        root.getChildren().add(bt1);
        bt1.setLayoutX(20.0);
        bt1.setLayoutY(200.0);
        Button bt2 = new Button ("2"); 
        root.getChildren().add(bt2);
        bt2.setLayoutX(10.0);
        bt2.setLayoutY(100.0);


        Line line = new Line();
        line.setStartX(bt1.getLayoutX());
        line.setStartY(bt1.getLayoutY());
        line.setEndX(bt2.getLayoutX());
        line.setEndY(bt2.getLayoutY());
        root.getChildren().add(line);

        bt2.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	root.getChildren().remove(bt2);
            	
            	
            	
            	
            }
        });
        
        bt1.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	//root.getChildren().remove(bt1);
            	
//            	line.setEndtX(bt2.getLayoutX());
//            	line.setEndY(bt2.getLayoutY());
//            	
            	}
            
        });
       
        
        primaryStage.setTitle("mapTool");
        primaryStage.setScene(scene);
        primaryStage.show();
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	//Create Node
            	
            }
        });
        
        
    }
 public static void main(String[] args) {
        Application.launch(args);
    }
}
