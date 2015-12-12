package gps;

import java.io.File;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MyPreloader extends Preloader {

    private static final double WIDTH = 800;
    private static final double HEIGHT = 600;

    private Stage preloaderStage;
    private Scene scene;

    private Label progress;

    public MyPreloader() {
        // Constructor is called before everything.
        //System.out.println(GPSapp.STEP() + "MyPreloader constructor called, thread: " + Thread.currentThread().getName());
    }

    @Override
    public void init() throws Exception {
        //System.out.println(GPSapp.STEP() + "MyPreloader#init (could be used to initialize preloader view), thread: " + Thread.currentThread().getName());

        // If preloader has complex UI it's initialization can be done in MyPreloader#init
        Platform.runLater(() -> {
        	//Loading screen blurred
        	File BlurFile = new File("CS3733_Graphics/CampusMap.png");
            Image BlurImage = new Image(BlurFile.toURI().toString());
            ImageView imageViewBlur = new ImageView();
            imageViewBlur.setImage(BlurImage);
            imageViewBlur.setLayoutX(-800);
            imageViewBlur.setLayoutY(-500);
            
            BoxBlur blur = new BoxBlur();
            blur.setWidth(10);
            blur.setHeight(10);
            blur.setIterations(3);
            
            imageViewBlur.setEffect(blur);
            
            DropShadow ds = new DropShadow();
            ds.setOffsetY(3.0f);
            ds.setColor(Color.GRAY);
            
            
            Label title = new Label("Starting PiNavigator");
            title.relocate(280, 300);
            title.setFont(Font.font ("manteka", 20));
            title.setEffect(ds);
            title.setTextAlignment(TextAlignment.CENTER);
            progress = new Label("0%");
            progress.relocate(370, 320);
            progress.setFont(Font.font ("manteka", 20));
            progress.setEffect(ds);

            Pane root = new Pane();
            root.getChildren().add(imageViewBlur);
            root.getChildren().addAll(title, progress);
                      
            title.toFront();
            progress.toFront();

            scene = new Scene(root, WIDTH, HEIGHT);
        });
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //System.out.println(GPSapp.STEP() + "MyPreloader#start (showing preloader stage), thread: " + Thread.currentThread().getName());

        this.preloaderStage = primaryStage;
        
        File iconFile = new File("CS3733_Graphics/PI.png");
        Image iconImage = new Image(iconFile.toURI().toString());
        primaryStage.getIcons().add(iconImage);
        primaryStage.setTitle("PiNavigator");

        // Set preloader scene and show stage.
        preloaderStage.setScene(scene);
        preloaderStage.show();
    }

    @Override
    public void handleApplicationNotification(PreloaderNotification info) {
        // Handle application notification in this point (see MyApplication#init).
        if (info instanceof ProgressNotification) {
            progress.setText(((ProgressNotification) info).getProgress() + "%");
        }
    }

    @Override
    public void handleStateChangeNotification(StateChangeNotification info) {
        // Handle state change notifications.
        StateChangeNotification.Type type = info.getType();
        switch (type) {
            case BEFORE_LOAD:
                // Called after MyPreloader#start is called.
                //System.out.println(GPSapp.STEP() + "BEFORE_LOAD");
                break;
            case BEFORE_INIT:
                // Called before MyApplication#init is called.
                //System.out.println(GPSapp.STEP() + "BEFORE_INIT");
                break;
            case BEFORE_START:
                // Called after MyApplication#init and before MyApplication#start is called.
                //System.out.println(GPSapp.STEP() + "BEFORE_START");

                preloaderStage.hide();
                break;
        }
    }
}
