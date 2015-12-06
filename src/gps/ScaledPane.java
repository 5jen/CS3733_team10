package gps;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

public class ScaledPane extends Application {
     private double scaleXratio = 1;
     private double scaleYRatio = 1;
     private double stageInitialWidth = 0;
     private double stageInitialHeight = 0;
     
public static void main(String[] args) throws Exception { launch(args); }
@Override public void start(Stage stage) throws Exception {
// create a pane with various objects on it.
final Pane pane = new Pane();
pane.setStyle("-fx-background-color: linear-gradient(to bottom right, derive(red, 20%), derive(red, -40%));");
final Label resizeMe = new Label("Resize me");
resizeMe.setAlignment(Pos.CENTER);
resizeMe.prefWidthProperty().bind(pane.widthProperty());
final ImageView iv1 = new ImageView( 
new Image("http://icons.iconarchive.com/icons/kidaubis-design/cool-heroes/128/Ironman-icon.png")
); 
final ImageView iv2 = new ImageView(
new Image("http://icons.iconarchive.com/icons/kidaubis-design/cool-heroes/128/Starwars-Stormtrooper-icon.png")
);
iv1.relocate(10, 10);
iv2.relocate(80, 60);
Button button = new Button("Zap!");
button.relocate(25, 140);
pane.getChildren().addAll(resizeMe, iv1, iv2, button);

// layout the scene.
final Group group = new Group(pane);
final Scene scene = new Scene(group);
stage.setScene(scene);
stage.show();

// scale the entire scene as the stage is resized. 
          this.stageInitialWidth = scene.getWidth();
          this.stageInitialHeight = scene.getHeight();

pane.getScene().widthProperty()
          .addListener(new ChangeListener<Number>() {
               @Override
               public void changed(ObservableValue<? extends Number> observable,
                         Number oldValue, Number newValue) {

                    scaleXratio = newValue.doubleValue() / stageInitialWidth;

                    pane.getTransforms().clear();
                    Scale scale = new Scale(scaleXratio, scaleYRatio, 0, 0);
                    pane.getTransforms().add(scale);

               }
          });

pane.getScene().heightProperty()
          .addListener(new ChangeListener<Number>() {
               @Override
               public void changed(ObservableValue<? extends Number> observable,
                         Number oldValue, Number newValue) {

                    scaleYRatio = (newValue.doubleValue())
                              / (stageInitialHeight);

                    pane.getTransforms().clear();
                    Scale scale = new Scale(scaleXratio, scaleYRatio, 0, 0);
                    pane.getTransforms().add(scale);

               }
          });

}
}