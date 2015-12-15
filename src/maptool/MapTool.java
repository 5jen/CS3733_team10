package maptool;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.*;
import io.JsonParser;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import javafx.util.Callback;
import node.*;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class MapTool extends Application {
    boolean delete = false;
    boolean startCoord, endCoord = false;
    double startX, startY, startZ, endX, endY, endZ = 0.0, buttonRescale = 1 / 0.75;
    int k = 0; // Set Max zoom Variable
    boolean disableKey = false;
    double mouseYposition, mouseXposition;
    double stageInitialWidthDifference = 100;
    double stageInitialHeightDifference = 100;
    ScrollPane scrollPane = new ScrollPane();

    // Buildings
    // TODO Add more buildings
    Building Campus = new Building("Campus");
    Building AtwaterKent = new Building("Atwater Kent");
    Building BoyntonHall = new Building("Boynton Hall");
    Building CampusCenter = new Building("Campus Center");
    Building GordonLibrary = new Building("Gordon Library");
    Building HigginsHouse = new Building("Higgins House");
    Building ProjectCenter = new Building("Project Center");
    Building StrattonHall = new Building("Stratton Hall");
    Building FullerLabs = new Building("Fuller Labs");
    Building SalisburyLabs = new Building("Salisbury Labs");
    Building WestStreet = new Building("157 West Street");
    Building WashburnShops = new Building("Washburn Shops");

    //Map Buildings with their content
    Map CampusMap = new Map("Campus Map", "CampusMap", "CS3733_Graphics/CampusMap.png", "Graphs/Nodes/CampusMap.json", "Graphs/Edges/CampusMapEdges.json", 0, 0, 0, 2.6053, "");

    Map AtwaterKentB = new Map("Atwater Kent B", "AK", "CS3733_Graphics/AKB.png", "Graphs/Nodes/AKB.json", "Graphs/Edges/AKBEdges.json", -2.617, 1548, 594, 0.1627, "B");
    Map AtwaterKent1 = new Map("Atwater Kent 1", "AK", "CS3733_Graphics/AK1.png", "Graphs/Nodes/AK1.json", "Graphs/Edges/AK1Edges.json", -2.617, 1548, 594, 0.1312, "1");
    Map AtwaterKent2 = new Map("Atwater Kent 2", "AK", "CS3733_Graphics/AK2.png", "Graphs/Nodes/AK2.json", "Graphs/Edges/AK2Edges.json", -2.617, 1548, 594, 0.1692, "2");
    Map AtwaterKent3 = new Map("Atwater Kent 3", "AK", "CS3733_Graphics/AK3.png", "Graphs/Nodes/AK3.json", "Graphs/Edges/AK3Edges.json", -2.617, 1548, 594, 0.1690, "3");

    Map GordonLibrarySB = new Map("Gordon Library SB", "GL", "CS3733_Graphics/GLSB.png", "Graphs/Nodes/GLSB.json", "Graphs/Edges/GLSBEdges.json", 1.762, 1668, 726, 0.1187, "SB");
    Map GordonLibraryB = new Map("Gordon Library B", "GL", "CS3733_Graphics/GLB.png", "Graphs/Nodes/GLB.json", "Graphs/Edges/GLBEdges.json", 1.762, 1668, 726, 0.1251, "B");
    Map GordonLibrary1 = new Map("Gordon Library 1", "GL", "CS3733_Graphics/GL1.png", "Graphs/Nodes/GL1.json", "Graphs/Edges/GL1Edges.json", 1.762, 1668, 726, 0.1194, "1");
    Map GordonLibrary2 = new Map("Gordon Library 2", "GL", "CS3733_Graphics/GL2.png", "Graphs/Nodes/GL2.json", "Graphs/Edges/GL2Edges.json", 1.762, 1668, 726, 0.1223, "2");
    Map GordonLibrary3 = new Map("Gordon Library 3", "GL", "CS3733_Graphics/GL3.png", "Graphs/Nodes/GL3.json", "Graphs/Edges/GL3Edges.json", 1.762, 1668, 726, 0.1387, "3");

    Map BoyntonHallB = new Map("Boynton Hall B", "BH", "CS3733_Graphics/BHB.png", "Graphs/Nodes/BHB.json", "Graphs/Edges/BHBEdges.json", 0.157, 1496, 991, 0.0956, "B");
    Map BoyntonHall1 = new Map("Boynton Hall 1", "BH", "CS3733_Graphics/BH1.png", "Graphs/Nodes/BH1.json", "Graphs/Edges/BH1Edges.json", 0.157, 1496, 991, 0.0973, "1");
    Map BoyntonHall2 = new Map("Boynton Hall 2", "BH", "CS3733_Graphics/BH2.png", "Graphs/Nodes/BH2.json", "Graphs/Edges/BH2Edges.json", 0.157, 1496, 991, 0.0981, "2");
    Map BoyntonHall3 = new Map("Boynton Hall 3", "BH", "CS3733_Graphics/BH3.png", "Graphs/Nodes/BH3.json", "Graphs/Edges/BH3Edges.json", 0.157, 1496, 991, 0.1003, "3");

    Map CampusCenter1 = new Map("Campus Center 1", "CC", "CS3733_Graphics/CC1.png", "Graphs/Nodes/CC1.json", "Graphs/Edges/CC1Edges.json", -1.413, 1175, 670, 0.1695, "1");
    Map CampusCenter2 = new Map("Campus Center 2", "CC", "CS3733_Graphics/CC2.png", "Graphs/Nodes/CC2.json", "Graphs/Edges/CC2Edges.json", -1.413, 1175, 670, 0.166, "2");
    Map CampusCenter3 = new Map("Campus Center 3", "CC", "CS3733_Graphics/CC3.png", "Graphs/Nodes/CC3.json", "Graphs/Edges/CC3Edges.json", -1.413, 1175, 670, 0.1689, "3");

    Map HigginsHouseB = new Map("Higgins House B", "HH", "CS3733_Graphics/HHB.png", "Graphs/Nodes/HHB.json", "Graphs/Edges/HHBEdges.json", -2.529, 1161, 504, 0.1314, "B");
    Map HigginsHouse1 = new Map("Higgins House 1", "HH", "CS3733_Graphics/HH1.png", "Graphs/Nodes/HH1.json", "Graphs/Edges/HH1Edges.json", -2.529, 1161, 504, 0.1364, "1");
    Map HigginsHouse2 = new Map("Higgins House 2", "HH", "CS3733_Graphics/HH2.png", "Graphs/Nodes/HH2.json", "Graphs/Edges/HH2Edges.json", -2.529, 1161, 504, 0.1343, "2");
    Map HigginsHouse3 = new Map("Higgins House 3", "HH", "CS3733_Graphics/HH3.png", "Graphs/Nodes/HH3.json", "Graphs/Edges/HH3Edges.json", -2.529, 1161, 504, 0.1317, "3");
    Map HigginsHouseAPT = new Map("Higgins House Apartment", "HH", "CS3733_Graphics/HHAPT.png", "Graphs/Nodes/HHAPT.json", "Graphs/Edges/HHAPTEdges.json", -0.942, 1215, 394, 0.0521, "APT");
    Map HigginsHouseGAR = new Map("Higgins House Garage", "HH", "CS3733_Graphics/HHGAR.png", "Graphs/Nodes/HHGAR.json", "Graphs/Edges/HHGAREdges.json", -0.942, 1215, 394, 0.053, "GAR");

    Map ProjectCenter1 = new Map("Project Center 1", "PC", "CS3733_Graphics/PC1.png", "Graphs/Nodes/PC1.json", "Graphs/Edges/PC1Edges.json", 1.71, 1228, 772, 0.0701, "1");
    Map ProjectCenter2 = new Map("Project Center 2", "PC", "CS3733_Graphics/PC2.png", "Graphs/Nodes/PC2.json", "Graphs/Edges/PC2Edges.json", 1.71, 1228, 772, 0.1016, "2");

    Map StrattonHallB = new Map("Stratton Hall B", "SH", "CS3733_Graphics/SHB.png", "Graphs/Nodes/SHB.json", "Graphs/Edges/SHBEdges.json", 1.71, 1364, 898, 0.0804, "B");
    Map StrattonHall1 = new Map("Stratton Hall 1", "SH", "CS3733_Graphics/SH1.png", "Graphs/Nodes/SH1.json", "Graphs/Edges/SH1Edges.json", 1.71, 1364, 898, 0.0813, "1");
    Map StrattonHall2 = new Map("Stratton Hall 2", "SH", "CS3733_Graphics/SH2.png", "Graphs/Nodes/SH2.json", "Graphs/Edges/SH2Edges.json", 1.71, 1364, 898, 0.0766, "2");
    Map StrattonHall3 = new Map("Stratton Hall 3", "SH", "CS3733_Graphics/SH3.png", "Graphs/Nodes/SH3.json", "Graphs/Edges/SH3Edges.json", 1.71, 1364, 898, 0.0749, "3");

    Map FullerLabsSB = new Map("Fuller Labs SB", "FL", "CS3733_Graphics/FLSB.png", "Graphs/Nodes/FLSB.json", "Graphs/Edges/FLSBEdges.json", 1.099, 1636, 497, 0.1735, "SB");
    Map FullerLabsB = new Map("Fuller Labs B", "FL", "CS3733_Graphics/FLB.png", "Graphs/Nodes/FLB.json", "Graphs/Edges/FLBEdges.json", 1.099, 1636, 497, 0.1641, "B");
    Map FullerLabs1 = new Map("Fuller Labs 1", "FL", "CS3733_Graphics/FL1.png", "Graphs/Nodes/FL1.json", "Graphs/Edges/FL1Edges.json", 1.099, 1636, 497, 0.169, "1");
    Map FullerLabs2 = new Map("Fuller Labs 2", "FL", "CS3733_Graphics/FL2.png", "Graphs/Nodes/FL2.json", "Graphs/Edges/FL2Edges.json", 1.099, 1636, 497, 0.168, "2");
    Map FullerLabs3 = new Map("Fuller Labs 3", "FL", "CS3733_Graphics/FL3.png", "Graphs/Nodes/FL3.json", "Graphs/Edges/FL3Edges.json", 1.099, 1636, 497, 0.1661, "3");
   
    //TODO Add rest of maps
    //TODO
    //TODO
    Map SalisburyLabs3 = new Map("Salisbury Labs 3", "SL", "CS3733_Graphics/SL3.png", "Graphs/Nodes/SL3.json", "Graphs/Edges/SL3Edges.json", -1.396, 1438, 717, 0.1636, "3");
    Map SalisburyLabs4 = new Map("Salisbury Labs 4", "SL", "CS3733_Graphics/SL4.png", "Graphs/Nodes/SL4.json", "Graphs/Edges/SL4Edges.json", -1.396, 1438, 717, 0.1629, "4");
    
    Map WestStreetB = new Map("157 West Street B", "West", "CS3733_Graphics/WestB.png", "Graphs/Nodes/WestB.json", "Graphs/Edges/WestBEdges.json", -1.413, 1306, 1290, 0.0547, "B");
    Map WestStreet1 = new Map("157 West Street 1", "West", "CS3733_Graphics/West1.png", "Graphs/Nodes/West1.json", "Graphs/Edges/West1Edges.json", -1.413, 1306, 1290, 0.0483, "1");
    Map WestStreet2 = new Map("157 West Street 2", "West", "CS3733_Graphics/West2.png", "Graphs/Nodes/West2.json", "Graphs/Edges/West2Edges.json", -1.413, 1306, 1290, 0.0532, "2");

    Map WashburnShops1 = new Map("Washburn Shops 1", "WS", "CS3733_Graphics/WS1.png", "Graphs/Nodes/WS1.json", "Graphs/Edges/WS1Edges.json", 0.157, 1422, 903, 0.1661, "1");
    
    public static void main(String[] args) {
        launch(args);
    }

    
    
    JsonParser json = new JsonParser();
    LinkedList<Node> nodeList = JsonParser.getJsonContent("Graphs/Nodes/CampusMap.json");
    LinkedList<EdgeDataConversion> edgeListConversion = JsonParser.getJsonContentEdge("Graphs/Edges/CampusMapEdges.json");
    LinkedList<Edge> edgeList = new LinkedList<Edge>();
    Canvas canvas = new Canvas(800, 600);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    boolean start, end = false;
    String startNode, endNode;
    String nodeReference = "";
    boolean updateNode = false;
    Button nodeButtonReference = new Button("");
    Button startButton = null, endButton = null, currentButton = null;
    final TextField xField = new TextField("");
    final TextField yField = new TextField("");
    final TextField zField = new TextField("");
    final TextField nameField = new TextField("");
    ObservableList<String> typeOptions = FXCollections.observableArrayList("Point", "Transition Point", "Staircase", "Vending Machine", "Water Fountain", "Men's Bathroom", "Women's Bathroom", "Emergency Pole", "Dining", "Elevator", "Computer Lab");
    final ComboBox<String> typeSelector = new ComboBox<String>(typeOptions);
    final RadioButton isPlace = new RadioButton();

    // Variables to store to and from nodes
    Node fromNode;
    Node toNode;

    ObservableList<Map> mapOptions = FXCollections.observableArrayList();
    final ComboBox<Map> mapSelector = new ComboBox<>(mapOptions);

    // Variable to store map that is currently displayed
    Map currentlySelectedMap;

    //add the cross image
    File crossFile = new File("CS3733_Graphics/cross.png");
    Image crossImage = new Image(crossFile.toURI().toString());
    ImageView cross = new ImageView();


    final Label fromField = new Label("");
    final Label toField = new Label("");
    final Label updateNodeLabel = new Label("");

    final HBox warningBox = new HBox(0);
    final Label warningLabel = new Label("");

    final RadioButton autoNodeCreate = new RadioButton();
    final RadioButton autoEdgeCreate = new RadioButton();

    //LOCK BUTTONS
    final RadioButton lockX = new RadioButton();
    final RadioButton lockY = new RadioButton();


    final Pane root = new Pane();

    //create actual map
    File mapFile = new File("CS3733_Graphics/CampusMap.png");
    Image mapImage = new Image(mapFile.toURI().toString());
    ImageView imageView = new ImageView();

    Graph globalGraph = new Graph();
    HashMap<String, Node> globalNodeHashMap = new HashMap<>();
    LinkedList<Node> globalNodeList = new LinkedList<Node>();

    @Override
    public void start(Stage primaryStage) {
    	
    	
    	File iconFile = new File("CS3733_Graphics/PI.png");
        Image iconImage = new Image(iconFile.toURI().toString());
		primaryStage.getIcons().add(iconImage);
		primaryStage.setTitle("PiEditor");
    	
        //Add Maps to buildings
        Campus.addMap(CampusMap);

        AtwaterKent.addMap(AtwaterKentB);
        AtwaterKent.addMap(AtwaterKent1);
        AtwaterKent.addMap(AtwaterKent2);
        AtwaterKent.addMap(AtwaterKent3);

        GordonLibrary.addMap(GordonLibrarySB);
        GordonLibrary.addMap(GordonLibraryB);
        GordonLibrary.addMap(GordonLibrary1);
        GordonLibrary.addMap(GordonLibrary2);
        GordonLibrary.addMap(GordonLibrary3);

        BoyntonHall.addMap(BoyntonHallB);
        BoyntonHall.addMap(BoyntonHall1);
        BoyntonHall.addMap(BoyntonHall2);
        BoyntonHall.addMap(BoyntonHall3);

        CampusCenter.addMap(CampusCenter1);
        CampusCenter.addMap(CampusCenter2);
        CampusCenter.addMap(CampusCenter3);

        HigginsHouse.addMap(HigginsHouseB);
        HigginsHouse.addMap(HigginsHouse1);
        HigginsHouse.addMap(HigginsHouse2);
        HigginsHouse.addMap(HigginsHouse3);
        HigginsHouse.addMap(HigginsHouseAPT);
        HigginsHouse.addMap(HigginsHouseGAR);

        StrattonHall.addMap(StrattonHallB);
        StrattonHall.addMap(StrattonHall1);
        StrattonHall.addMap(StrattonHall2);
        StrattonHall.addMap(StrattonHall3);

        ProjectCenter.addMap(ProjectCenter1);
        ProjectCenter.addMap(ProjectCenter2);
        
        FullerLabs.addMap(FullerLabsSB);
        FullerLabs.addMap(FullerLabsB);
        FullerLabs.addMap(FullerLabs1);
        FullerLabs.addMap(FullerLabs2);
        FullerLabs.addMap(FullerLabs3);
        
        
        //TODO Add rest of maps
        SalisburyLabs.addMap(SalisburyLabs3);
        SalisburyLabs.addMap(SalisburyLabs4);
        
        WashburnShops.addMap(WashburnShops1);
        
        WestStreet.addMap(WestStreetB);
        WestStreet.addMap(WestStreet1);
        WestStreet.addMap(WestStreet2);
        
        // Store the Buildings in a list
        // TODO Add more buildings to this list
        LinkedList<Building> buildings = new LinkedList<>();
        buildings.add(Campus);
        buildings.add(AtwaterKent);
        buildings.add(GordonLibrary);
        buildings.add(CampusCenter);
        buildings.add(HigginsHouse);
        buildings.add(StrattonHall);
        buildings.add(ProjectCenter);
        buildings.add(BoyntonHall);
        buildings.add(FullerLabs);
        buildings.add(SalisburyLabs);
        buildings.add(WashburnShops);
        buildings.add(WestStreet);

        // Iterate over the list of buildings and add their maps to another list
        LinkedList<Map> maps = new LinkedList<>();
        for (Building b : buildings) {
            maps.addAll(b.getMaps());
        }

        mapOptions.addAll(maps);
        
      //Generate the Global map graph
        createGlobalGraph();
        
      //now we can create the local edge connections
        
        LinkedList<Edge> edgeList = convertEdgeData(edgeListConversion);
        


        final Pane root = new Pane();
        Scene scene = new Scene(root, 1125, 700);//set size of scene
        scene.getStylesheets().add(getClass().getResource("Buttons.css").toExternalForm());
        stageInitialWidthDifference = scene.getWidth()-1100;
        stageInitialHeightDifference = scene.getHeight()-700;

        //Set default Type
        typeSelector.setValue("Point");

        //Create a map selection drop down menu
        final VBox mapSelectionBoxV = new VBox(5);
        final Label mapSelectorLabel = new Label("Choose map");
        mapSelectorLabel.setTextFill(Color.WHITE);
        mapSelectorLabel.setFont(Font.font("manteka", 14));
        final HBox mapSelectionBoxH = new HBox(5);
        final Button LoadMapButton = new Button("Load Map");


        mapSelector.setValue(mapSelector.getItems().get(0));
        // Assign Campus map as currently selected map
        currentlySelectedMap = mapSelector.getValue();

        // Shows Name of Map Object in ComboBox dropdown
        mapSelector.setCellFactory(new Callback<ListView<Map>, ListCell<Map>>() {
            @Override
            public ListCell<Map> call(ListView<Map> param) {
                ListCell cell = new ListCell<Map>() {
                    @Override
                    protected void updateItem(Map map, boolean empty) {
                        super.updateItem(map, empty);
                        if (empty) {
                            setText("");
                        } else {
                            setText(map.getName());
                        }
                    }
                };
                return cell;
            }
        });
        // Shows name in ComboBox
        mapSelector.setButtonCell(new ListCell<Map>() {
            @Override
            protected void updateItem(Map map, boolean bln) {
                super.updateItem(map, bln);
                if (bln) {
                    setText("");
                } else {
                    setText(map.getName());
                }
            }
        });

        mapSelectionBoxH.getChildren().addAll(mapSelector, LoadMapButton);
        mapSelectionBoxV.setLayoutX(830);
        mapSelectionBoxV.setLayoutY(620);
        mapSelectionBoxV.getChildren().addAll(mapSelectorLabel, mapSelectionBoxH);

        //Create a label and box for warnings, ie when the coordinates are outside the name
        warningLabel.setFont(Font.font("manteka", 10));
        warningLabel.setTextFill(Color.RED);
        warningBox.setLayoutX(830);
        warningBox.setLayoutY(440);
        warningBox.getChildren().add(warningLabel);


        //Create input box field labels (on top of the text boxes)
        final VBox controlLabels = new VBox(10);

        final Label xFieldName = new Label("X Coordinate");
        xFieldName.setFont(Font.font("manteka", 12));
        xFieldName.setTextFill(Color.WHITE);
        HBox xFieldBox = new HBox(60);
        xFieldBox.getChildren().addAll(xField, lockX);
        //************

        final Label yFieldName = new Label("Y Coordinate");
        yFieldName.setFont(Font.font("manteka", 12));
        yFieldName.setTextFill(Color.WHITE);
        HBox yFieldBox = new HBox(60);
        yFieldBox.getChildren().addAll(yField, lockY);

        final Label zFieldName = new Label("Z Coordinate");
        zFieldName.setTextFill(Color.WHITE);
        zFieldName.setFont(Font.font("manteka", 12));
        HBox zFieldBox = new HBox(60);
        zFieldBox.getChildren().addAll(zField);

        final Label nameFieldName = new Label("Name");
        nameFieldName.setTextFill(Color.WHITE);
        nameFieldName.setFont(Font.font("manteka", 12));

        final Label nodeTypeName = new Label("Node Type");
        nodeTypeName.setTextFill(Color.WHITE);
        nodeTypeName.setFont(Font.font("manteka", 12));

        final Label isPlaceName = new Label("Place?");
        isPlaceName.setTextFill(Color.WHITE);
        isPlaceName.setFont(Font.font("manteka", 12));


      //AutoCreation node button
        final Label autoCreationNodeButton = new Label("Auto Node Mode");
        autoCreationNodeButton.setTextFill(Color.WHITE);
        autoCreationNodeButton.setFont(Font.font("manteka", 12));
        HBox autoNodeModeHBox = new HBox(5);
        autoNodeModeHBox.getChildren().addAll(autoNodeCreate, autoCreationNodeButton);

        final Label autoCreationEdgeButton = new Label("Auto Edge Mode");
        autoCreationEdgeButton.setTextFill(Color.WHITE);
        autoCreationEdgeButton.setFont(Font.font("manteka", 12));
        HBox autoEdgeModeHBox = new HBox(5);
        autoEdgeModeHBox.getChildren().addAll(autoEdgeCreate, autoCreationEdgeButton);

        
        
        
        //final Label updateNodeLabel = new Label("Node");
        updateNodeLabel.setTextFill(Color.WHITE);
        updateNodeLabel.setFont(Font.font("manteka", 12));

        final HBox isPlaceUpdateLabelBox = new HBox(60);
        isPlaceUpdateLabelBox.getChildren().addAll(isPlace, updateNodeLabel);

        HBox NodeCreationBox = new HBox(5);
        final Button updateNodeButton = new Button("Update Node");
        final Button createNodeButton = new Button("Create Node");
        final Button deleteNodeButton = new Button("Delete Node");
        NodeCreationBox.getChildren().addAll(createNodeButton, deleteNodeButton);
        
        HBox NodeUpdateBox = new HBox(5);
        NodeUpdateBox.getChildren().addAll(updateNodeButton, autoNodeModeHBox);
        

        controlLabels.setLayoutX(830);
        controlLabels.setLayoutY(20);
        controlLabels.getChildren().addAll(xFieldName, xFieldBox, yFieldName, yFieldBox, zFieldName, zFieldBox, nameFieldName, nameField, nodeTypeName, typeSelector, isPlaceName, isPlace, NodeCreationBox, NodeUpdateBox);

        //attach the cross image
        cross.setImage(crossImage);


        //create edge interface
        final VBox edgeControls = new VBox(10);

        final HBox fromBox = new HBox(10);
        final Label fromLabel = new Label("From: ");
        fromLabel.setFont(Font.font("manteka", 12));
        fromLabel.setTextFill(Color.WHITE);
        fromField.setFont(Font.font("manteka", 12));
        fromField.setTextFill(Color.WHITE);
        fromBox.getChildren().addAll(fromLabel, fromField);


        //Create Lock label
        final Label lockLabel = new Label("Lock");
        lockLabel.setFont(Font.font("manteka", 12));
        lockLabel.setTextFill(Color.WHITE);
        lockLabel.setLayoutX(1040);
        lockLabel.setLayoutY(20);


        final HBox toBox = new HBox(10);
        final Label toName = new Label("To:   ");
        toName.setFont(Font.font("manteka", 12));
        toName.setTextFill(Color.WHITE);
        toField.setFont(Font.font("manteka", 12));
        toField.setTextFill(Color.WHITE);
        toBox.getChildren().addAll(toName, toField);
        
        

        HBox EdgeCreationBox = new HBox(5);
        final Button createEdgeButton = new Button("Create Edge");
        final Button deleteEdgeButton = new Button("Delete Edge");
        EdgeCreationBox.getChildren().addAll(createEdgeButton, deleteEdgeButton);
        final Button saveGraph = new Button("Save");
        final HBox SaveBox = new HBox(5);
        SaveBox.getChildren().addAll(saveGraph, autoEdgeModeHBox);
        edgeControls.setLayoutX(830);
        edgeControls.setLayoutY(460);
        edgeControls.getChildren().addAll(fromBox, toBox, EdgeCreationBox, SaveBox);

        imageView.setImage(mapImage);
        imageView.setLayoutX(0);
        imageView.setLayoutY(0);

        //create background
        File backgroundFile = new File("CS3733_Graphics/Background.jpg");
        Image bgImage = new Image(backgroundFile.toURI().toString());
        ImageView bgView = new ImageView();
        bgView.setImage(bgImage);
        bgView.setLayoutX(0);
        bgView.setLayoutY(0);

        

        //Attach everything to the screen
        root.getChildren().add(bgView);

        root.getChildren().add(mapSelectionBoxV);
        root.getChildren().add(edgeControls);
        root.getChildren().add(controlLabels);
        root.getChildren().addAll(lockLabel);

        root.getChildren().add(warningBox);

        Pane NodePane = new Pane();
        imageView.setScaleX(0.75);
        imageView.setScaleY(0.75);
        imageView.relocate(-1000, -600);
        NodePane.setPrefSize(3000, 2000);
        NodePane.setScaleX(0.75);
        NodePane.setScaleY(0.75);
        NodePane.relocate(-965, -643);
        zField.setText("0");
        drawEdges(edgeList, gc, NodePane); //from here we draw the nodes so that nodes are on top of the edges

        final Group group = new Group(imageView, NodePane);
        Parent zoomPane = createZoomPane(group);
        root.getChildren().add(zoomPane);

        // Keyboard shortcuts

        root.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (disableKey || event.getCode() == KeyCode.SHIFT || event.getCode() == KeyCode.ALT || event.getCode() == KeyCode.CONTROL) {

                } else {
                    if (event.getCode() == KeyCode.W && event.isControlDown()) {
                        autoEdgeCreate.setSelected(!autoEdgeCreate.isSelected());
                    } else if (event.getCode() == KeyCode.W && !event.isShiftDown()) {
                        createEdge(NodePane);
                    }
                    if (event.getCode() == KeyCode.Q && event.isControlDown()) {
                        autoNodeCreate.setSelected(!autoNodeCreate.isSelected());
                    } else if (event.getCode() == KeyCode.Q && !event.isShiftDown()) {
                        createNode(NodePane);
                    }
                    if (event.getCode() == KeyCode.P && !event.isShiftDown()) {
                        isPlace.setSelected(!isPlace.isSelected());
                    }
                    if (event.getCode() == KeyCode.L && !event.isShiftDown()) {
                        loadMap(root, canvas, zoomPane, NodePane, imageView);
                    }
                    if (event.getCode() == KeyCode.S && event.isControlDown()) {
                        saveGraphMethod();
                        warningLabel.setText("Map Saved");
                    }
                    if (event.getCode() == KeyCode.X && !event.isShiftDown()) {
                        lockX.setSelected(!lockX.isSelected());
                    }
                    if (event.getCode() == KeyCode.Y && !event.isShiftDown()) {
                        lockY.setSelected(!lockY.isSelected());
                    }
                }
            }
        });


        //For update name field when we update the type
        typeSelector.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                if (!isPlace.isSelected())
                    nameField.setText(currentlySelectedMap.getInitials() + currentlySelectedMap.getFloor() + ":" + typeSelector.getValue() + ":" + xField.getText() + ":" + yField.getText());
                warningLabel.setText(" ");
            }
        });

        autoCreationEdgeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                autoEdgeCreate.setSelected(!autoEdgeCreate.isSelected());
            }
        });

        autoCreationNodeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                autoNodeCreate.setSelected(!autoNodeCreate.isSelected());
            }
        });


        updateNodeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	updateNode = true;
            	int x = -1, y = -1, z = -1;
            	
            	/************************************************/
            	try{
            		x = Integer.parseInt(xField.getText());  
            		y = Integer.parseInt(yField.getText());
            		z = Integer.parseInt(zField.getText());
            	} catch (NumberFormatException e) {
            	    System.err.println("NumberFormatException: " + e.getMessage());
            	} 
            	if(updateNode){

            		for(int i = 0; i < nodeList.size(); i++){
                		if(Objects.equals(nodeReference, nodeList.get(i).getName())){
                			//root.getChildren().remove(nodeButtonReference);
                            // TODO If a node is updated the edges also have to be updated
                			nodeList.get(i).setX(x);
                			nodeList.get(i).setY(y);
                			nodeList.get(i).setZ(z);
                			nodeList.get(i).setName(nameField.getText());
                			nodeList.get(i).setIsPlace(isPlace.isSelected());
                			nodeList.get(i).setType(typeSelector.getValue());
                            nodeList.get(i).setGlobalX((int) (((x * Math.cos(currentlySelectedMap.getRotationalConstant())
                                    - y * Math.sin(currentlySelectedMap.getRotationalConstant())) * (currentlySelectedMap.getConversionRatio()) +
                                    currentlySelectedMap.getGlobalToLocalOffsetX())
                            ));
                            nodeList.get(i).setGlobalY((int) (((x * Math.sin(currentlySelectedMap.getRotationalConstant())
                                    + y * Math.cos(currentlySelectedMap.getRotationalConstant())) * (currentlySelectedMap.getConversionRatio())
                                    + currentlySelectedMap.getGlobalToLocalOffsetY())));
                			
                			nodeButtonReference.relocate(x, y);
                		}
                			//set all fields and then break out of this
                	}
                	nameField.setText(currentlySelectedMap.getInitials() + currentlySelectedMap.getFloor() + ":" + typeSelector.getValue() + ":" + xField.getText() + ":" + yField.getText());

            		updateNode = false;
            	}
            	
            	saveGraphMethod();

            	//loadMap(root, canvas, zoomPane, NodePane, imageView);

            }

        });


        //Save the Graph
        saveGraph.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                saveGraphMethod();
                warningLabel.setText(" ");
            }
        });

        nameField.setOnInputMethodTextChanged(new EventHandler<InputMethodEvent>() {
            @Override
            public void handle(InputMethodEvent event) {
                disableKey = true;
            }
        });
        
scene.widthProperty().addListener(new ChangeListener<Number>() {
            
    		@Override
    		public void changed(ObservableValue<? extends Number> observable,
    				Number oldValue, Number newValue) {
    			stageInitialWidthDifference = scene.getWidth()- 1100;    			
    			scrollPane.setPrefViewportWidth(800 + stageInitialWidthDifference);
    			
    			mapSelectionBoxV.setTranslateX(stageInitialWidthDifference);
    			controlLabels.setTranslateX(stageInitialWidthDifference);
    			edgeControls.setTranslateX(stageInitialWidthDifference);
    			lockLabel.setTranslateX(stageInitialWidthDifference);
    			
    			//TODO asdasd//TODO asdasd//TODO asdasd//TODO asdasd//TODO asdasd
    			//TODO asdasd
    			//TODO asdasd
    			//TODO asdasd
    			//TODO asdasd
    			//TODO asdasd
    			//TODO asdasd
    			//TODO asdasd
    			

    		}
    	});

	    scene.heightProperty().addListener(new ChangeListener<Number>() {
    		@Override
    		public void changed(ObservableValue<? extends Number> observable,
                   Number oldValue, Number newValue) {
    	        stageInitialHeightDifference = scene.getHeight()-700;
    	        scrollPane.setPrefViewportHeight(700 + stageInitialHeightDifference);
    			//s1.setPrefSize(270, 400 + stageInitialHeightDifference);
    			
    			

    		}
    	});


        NodePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                disableKey = false;
                //Set the location coordinates in the input boxes

                //still need to find where to add Z..****
                //TODO add tool to write the locations we click to the screen so we can make outlines on rooms for clicking anypoint in rooms

                warningLabel.setText(" ");
                if (event.isStillSincePress()) {
                    //Set the location coordinates in the input boxes
                    if (!lockX.isSelected())
                        xField.setText(Integer.toString((int) event.getX()));
                    if (!lockY.isSelected())
                        yField.setText(Integer.toString((int) event.getY()));


                    nameField.setText(currentlySelectedMap.getInitials() + currentlySelectedMap.getFloor() + ":" + typeSelector.getValue() + ":" + xField.getText() + ":" + yField.getText());
                    int x = Integer.parseInt(xField.getText());
                    int y = Integer.parseInt(yField.getText());

                    if (event.getX() > 40 && event.getY() > 40) {
                        //add a cross when click on the canvas
                        if (NodePane.getChildren().contains(cross)) {
                            NodePane.getChildren().remove(cross);
                        }
                        NodePane.getChildren().add(cross);
                        cross.relocate(x - 38, y - 38);
                    } else {
                        NodePane.getChildren().remove(cross);
                    }
                    if (autoNodeCreate.isSelected()) {

                        createNode(NodePane);

                    }

                }
            }
        });

        deleteNodeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                delete = true;
                warningLabel.setText(" ");
            }
        });
        deleteEdgeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                delete = true;
                warningLabel.setText(" ");
            }
        });


        createEdgeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                createEdge(NodePane);
            }
        });


        //Add actions to the Load Map button
        LoadMapButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                loadMap(root, canvas, zoomPane, NodePane, imageView);
                warningLabel.setText(" ");
            }

        });

        root.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                //remove the canvas
                nodeReference = "";
                updateNode = false;
                warningLabel.setText(" ");

            }


        });


        createNodeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
                createNode(NodePane);
                warningLabel.setText(" ");
            }
        });


        primaryStage.setScene(scene);
        primaryStage.show();
        
        loadMap( root,  canvas,  zoomPane,  NodePane,  imageView);

    }

    //create an edge between 2 nodes 
    public void createEdge(Pane NodePane) {
        if (!(fromNode == null) || !(toNode == null)) {
            if (Objects.equals(fromNode.getName(), toNode.getName())) {
                //System.out.println("Cannot create Edge: Tried to make edge connecting node to self");
                warningLabel.setText("Cannot Create Edge");
            } else {
                Edge newEdge = new Edge(fromNode, toNode, getDistanceNodeFlat(fromNode, toNode));
                //System.out.println(fromNode.getName());
                //System.out.println(toNode.getName());
                edgeList.add(newEdge);
                if (Objects.equals(fromNode.getFloorMap(), toNode.getFloorMap())) {
                    Line line = new Line();
                    line.setStartX(startX);
                    line.setStartY(startY);
                    line.setEndX(endX);
                    line.setEndY(endY);
                    line.setStrokeWidth(3 * buttonRescale);
                    line.setStyle("-fx-background-color:  #F0F8FF; ");
                    line.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        public void handle(MouseEvent event) {
                            fromField.setText(fromNode.getName());
                            toField.setText(toNode.getName());
                            if (delete) {
                                NodePane.getChildren().remove(line);
                                edgeList.remove(newEdge);
                                delete = false;
                            }
                        }
                    });
                    NodePane.getChildren().add(line);
                }
            }

        }
    }

    //Create Node Method to be called when we wanna create a node
    public void createNode(Pane NodePane) {
        warningLabel.setText(" ");
        NodePane.getChildren().remove(cross);
        int x = -1, y = -1, z = -1;

        /************************************************/
        try {
            x = Integer.parseInt(xField.getText());
            y = Integer.parseInt(yField.getText());
            z = Integer.parseInt(zField.getText());
        } catch (NumberFormatException e) {
            System.err.println("NumberFormatException: " + e.getMessage());
        }

        // Check for dupilcate name
        Boolean duplicateName = false;
        for (int i = 0; i < nodeList.size(); i++) {
            if (nameField.getText().equals(nodeList.get(i).getName())) {
                duplicateName = true;
            }
        }

        //check to see if coordinates are within map bounds, We dont care if it's campus map
        if (!isInBounds(x, y)) {
            warningLabel.setText("Error, coordinates out of bounds");
        }

        //check to see if proper fields types given
        else if (!isValidCoords(xField.getText())) {
            warningLabel.setText("Error, coordinates not valid");
        }

        // Make sure a name is entered before creating node
        else if (nameField.getText().equals("")) {
            warningLabel.setText("Error, must enter a name");
        } else if (duplicateName) {
            warningLabel.setText("Error, cannot have two nodes with the same name");
        }

        /************************************************/
        //passes all validity checks, create waypoint and add button
        else {

            int newX = x, newY = y, newZ = z;
            warningLabel.setText("");//Remove warning, bc successful

            Button newNodeButton = new Button("");

            if (isPlace.isSelected()) {
                newNodeButton.setStyle(
                        "-fx-background-radius: 5em; " +
                                "-fx-min-width: " + 15 * buttonRescale + "px; " +
                                "-fx-min-height: " + 15 * buttonRescale + "px; " +
                                "-fx-max-width: " + 15 * buttonRescale + "px; " +
                                "-fx-max-height: " + 15 * buttonRescale + "px;"
                );
                newNodeButton.setId("shiny-orange");

            } else {
                newNodeButton.setStyle(
                        "-fx-background-radius: 5em; " +
                                "-fx-min-width: " + 10 * buttonRescale + "px; " +
                                "-fx-min-height: " + 10 * buttonRescale + "px; " +
                                "-fx-max-width: " + 10 * buttonRescale + "px; " +
                                "-fx-max-height: " + 10 * buttonRescale + "px;"
                );
                newNodeButton.setId("dark-blue");
            }

            Node newPlace = new Node(x, y, z, nameField.getText(), currentlySelectedMap.getBuildingName(), currentlySelectedMap.getName(), true, isPlace.isSelected(), typeSelector.getValue());

            // Set the Global X and Global Y.
            newPlace.setGlobalX((int) (((x * Math.cos(currentlySelectedMap.getRotationalConstant())
                    - y * Math.sin(currentlySelectedMap.getRotationalConstant())) * (currentlySelectedMap.getConversionRatio()) +
                    currentlySelectedMap.getGlobalToLocalOffsetX())
            ));
            newPlace.setGlobalY((int) (((x * Math.sin(currentlySelectedMap.getRotationalConstant())
                    + y * Math.cos(currentlySelectedMap.getRotationalConstant())) * (currentlySelectedMap.getConversionRatio())
                    + currentlySelectedMap.getGlobalToLocalOffsetY())));
            // TODO This should also add to the global map nodes
            nodeList.add(newPlace);
            //Add actions for when you click this unique button
            newNodeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

                public void handle(MouseEvent event) {
                    //check to see if we are just editting an exiting node

                    if (delete) {
                        root.getChildren().remove(newNodeButton);
                        nodeList.remove(newPlace);
                        LinkedList<Edge> edgesToDelete = new LinkedList<>();
                        //iterate through the edge list and delete all edges attached to this node
                        edgeList.stream().filter(e -> Objects.equals(e.getFrom().getName(), newPlace.getName()) || Objects.equals(e.getTo().getName(), newPlace.getName())).forEach(e -> {
                            edgesToDelete.add(e);
                        });
                        edgeList.removeAll(edgesToDelete);
                        delete = false;
                    } else if (!startCoord) {
                        if (startButton != null && endButton != startButton) startButton.setId(null);
                        newNodeButton.setId("green");
                        startButton = newNodeButton;
                        startX = newNodeButton.getLayoutX() + 7 * buttonRescale;
                        startY = newNodeButton.getLayoutY() + 7 * buttonRescale;

                        fromField.setText(newPlace.getName());
                        fromNode = newPlace;
                        startCoord = true;

                        if (autoEdgeCreate.isSelected() && toNode != null) {

                            createEdge(NodePane);

                        }

                    } else if (!endCoord) {
                        if (endButton != null && endButton != startButton) endButton.setId(null);
                        newNodeButton.setId("green");
                        endButton = newNodeButton;
                        endX = newNodeButton.getLayoutX() + 7 * buttonRescale;
                        endY = newNodeButton.getLayoutY() + 7 * buttonRescale;

                        toField.setText(newPlace.getName());
                        toNode = newPlace;
                        startCoord = false;
                        endCoord = false;


                        if (autoEdgeCreate.isSelected()) {

                            createEdge(NodePane);

                        }
                    }
                    //no matter what fill in this nodes data into the input box fields
                    xField.setText("" + newPlace.getX());
                    yField.setText("" + newPlace.getY());
                    zField.setText("" + newPlace.getZ());
                    nameField.setText(newPlace.getName());
                    typeSelector.setValue(newPlace.getType());
                    if (newPlace.getIsPlace())
                        isPlace.setSelected(true);
                    else {
                        isPlace.setSelected(false);
                    }
                    nodeReference = newPlace.getName(); //so we can reference this node in other places
                    updateNode = true;
                    nodeButtonReference = newNodeButton;
                    updateNodeLabel.setText("" + newPlace.getName());
                }

            });
            nameField.setText("");
            NodePane.getChildren().add(newNodeButton);
            if (isPlace.isSelected())
                newNodeButton.relocate(newX - 7 * buttonRescale, newY - 7 * buttonRescale);
            else
                newNodeButton.relocate(newX - 5 * buttonRescale, newY - 5 * buttonRescale);
        }
    }


    //Create Hot Key events
    public void buttonPressed(KeyEvent e) {
        if (e.getCode().toString().equals("ENTER")) {
            //System.out.println("asdasdasdasdasdasdasdasda)");
            //createNodeButton.setOnAction(CreateHandler);
        }
    }
    
    private void createGlobalGraph() {

    	//create Global nodes and edges list to pass to other createGraph method
    	LinkedList<EdgeDataConversion> globalEdgeListConversion = new LinkedList<EdgeDataConversion>();

    	//Manually add all of the Nodes...
        File nodeFolder = new File("Graphs/Nodes");
        for (File file : nodeFolder.listFiles()){
            if (file.getName().endsWith(".json")){
                globalNodeList.addAll(JsonParser.getJsonContent("Graphs/Nodes/" + file.getName()));
                //System.out.println(file.getName());
            }
        }

        globalGraph.setNodes(globalNodeList);

        File edgeFolder = new File("Graphs/Edges");
        for (File file : edgeFolder.listFiles()){
            if (file.getName().endsWith(".json")){
                globalEdgeListConversion.addAll(JsonParser.getJsonContentEdge("Graphs/Edges/" + file.getName()));
                //System.out.println(file.getName());
            }
        }

        globalGraph.setNodes(globalNodeList);

        //Create HashMap of every node in the globalNodeList
        globalNodeHashMap = new HashMap<>();
        for (Node n : globalNodeList){
            globalNodeHashMap.put(n.getName(), n);
        }

        // Create edges and add them to the graph
        for (EdgeDataConversion edc : globalEdgeListConversion){
            Node fromNode = globalNodeHashMap.get(edc.getFrom());
            Node toNode = globalNodeHashMap.get(edc.getTo());
            if (fromNode == null || toNode == null){
                continue;
            }
            globalGraph.addEdge(fromNode, toNode);
        }
	}

    //Change where we call drawEdges to just change the drawEdgeBool to true;
    private void drawEdges(LinkedList<Edge> edges, GraphicsContext gc, Pane nodePane) {
        root.getChildren().remove(canvas);
        gc.clearRect(0, 0, 8000, 6000);
        int i;

        for (i = 0; i < edgeList.size(); i++) {
            int j = i;
            Line line = new Line();

            if (Objects.equals(edgeList.get(i).getFrom().getFloorMap(), edgeList.get(i).getTo().getFloorMap())) {
                //Determine the offset we need to use for the tool graph FROM NODE
                if (edgeList.get(i).getFrom().getIsPlace()) {
                    line.setStartX(edgeList.get(i).getFrom().getX());
                    line.setStartY(edgeList.get(i).getFrom().getY());
                } else {
                    line.setStartX(edgeList.get(i).getFrom().getX());
                    line.setStartY(edgeList.get(i).getFrom().getY());

                }
                //Determine the offset we need to use for the tool graph TO NODE
                if (edgeList.get(i).getTo().getIsPlace()) {
                    line.setEndX(edgeList.get(i).getTo().getX());
                    line.setEndY(edgeList.get(i).getTo().getY());
                } else {
                    line.setEndX(edgeList.get(i).getTo().getX());
                    line.setEndY(edgeList.get(i).getTo().getY());
                }
                line.setStrokeWidth(3 * buttonRescale);


                line.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        fromField.setText(edgeList.get(j).getFrom().getName());
                        toField.setText(edgeList.get(j).getTo().getName());
                        if (delete) {
                            nodePane.getChildren().remove(line);
                            edgeList.remove(edgeList.get(j));
                            delete = false;
                        }
                    }
                });
                nodePane.getChildren().add(line);
            }
        }
        drawNodes(nodeList, nodePane, fromField, toField, nodePane);

    }


    //check to see if the coordinates are integers
    public boolean isValidCoords(String s) {
        String validCoords = "0123456789";
        for (int i = 1; i <= s.length(); i++) {
            if (!validCoords.contains(s.substring(i - 1, i))) {
                return false;
            }
        }
        return true;
    }

    //check to see if node coordinates are within map bounds
    public boolean isInBounds(int x, int y) {
        if (x < 0 || y < 0) {
            return false;
        }
        return true;
    }

    // Returns the distance between the two nodes, in pixels
    // FIXME needs to be updated to calculate distance based on global coordinates and not local
    public int getDistance() {
        return (int) Math.sqrt((Math.pow(((int) startX - (int) endX), 2)) + (Math.pow(((int) startY - (int) endY), 2)));
    }

    public int getDistanceNodeFlat(Node n1, Node n2) {
        return (int) Math.sqrt((Math.pow((n1.getGlobalX() - n2.getGlobalX()), 2)) + (Math.pow((n1.getGlobalY() - n2.getGlobalY()), 2)));
    }

    // Draws the Places and Nodes on to the map
    private void drawNodes(LinkedList<Node> nodes, Pane root, Label fromField, Label toField, Pane NodePane) {
        int i;
        for (i = 0; i < nodes.size(); i++) {
            Button newNodeButton = new Button("");
            //Determine what type of node image we choose
            if (nodes.get(i).getIsPlace()) {
                newNodeButton.setStyle(
                        "-fx-background-radius: 5em; " +
                                "-fx-min-width: " + 15 * buttonRescale + "px; " +
                                "-fx-min-height: " + 15 * buttonRescale + "px; " +
                                "-fx-max-width: " + 15 * buttonRescale + "px; " +
                                "-fx-max-height: " + 15 * buttonRescale + "px;"
                );
                newNodeButton.relocate(nodes.get(i).getX() - 7, nodes.get(i).getY() - 7);
                newNodeButton.setId("shiny-orange");

            } else {
                newNodeButton.setStyle(
                        "-fx-background-radius: 5em; " +
                                "-fx-min-width: " + 10 * buttonRescale + "px; " +
                                "-fx-min-height: " + 10 * buttonRescale + "px; " +
                                "-fx-max-width: " + 10 * buttonRescale + "px; " +
                                "-fx-max-height: " + 10 * buttonRescale + "px;"
                );
                newNodeButton.setId("dark-blue");
                newNodeButton.relocate(nodes.get(i).getX() - 5 * buttonRescale, nodes.get(i).getY() - 5 * buttonRescale);

            }
            Node newPlace = nodes.get(i);
            newNodeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {

                    if (delete) {
                        root.getChildren().remove(newNodeButton);
                        nodeList.remove(newPlace);
                        LinkedList<Edge> edgesToDelete = new LinkedList<>();
                        //iterate through the edge list and delete all edges attached to this node
                        edgeList.stream().filter(e -> Objects.equals(e.getFrom().getName(), newPlace.getName()) || Objects.equals(e.getTo().getName(), newPlace.getName())).forEach(e -> {
                            edgesToDelete.add(e);
                        });
                        edgeList.removeAll(edgesToDelete);
                        delete = false;
                    } else if (!startCoord) {
                        if (startButton != null && endButton != startButton) startButton.setId(null);
                        newNodeButton.setId("green");
                        startButton = newNodeButton;
                        startX = newNodeButton.getLayoutX() + 7 * buttonRescale;
                        startY = newNodeButton.getLayoutY() + 7 * buttonRescale;
                        fromField.setText(newPlace.getName());
                        fromNode = newPlace;
                        startCoord = true;
                        if (autoEdgeCreate.isSelected() && toNode != null) {

                            createEdge(NodePane);

                        }
                    } else if (!endCoord) {
                        if (endButton != null && endButton != startButton) endButton.setId(null);
                        newNodeButton.setId("green");
                        endButton = newNodeButton;
                        endX = newNodeButton.getLayoutX() + 7 * buttonRescale;
                        endY = newNodeButton.getLayoutY() + 7 * buttonRescale;
                        toField.setText(newPlace.getName());
                        toNode = newPlace;
                        startCoord = false;
                        endCoord = false;
                        if (autoEdgeCreate.isSelected()) {

                            createEdge(NodePane);

                        }
                    }

                    //no matter what fill in this nodes data into the input box fields
                    xField.setText("" + newPlace.getX());
                    yField.setText("" + newPlace.getY());
                    zField.setText("" + newPlace.getZ());
                    nameField.setText(newPlace.getName());
                    typeSelector.setValue(newPlace.getType());
                    if (newPlace.getIsPlace())
                        isPlace.setSelected(true);
                    else {
                        isPlace.setSelected(false);
                    }
                    nodeReference = newPlace.getName(); //so we can referecne this node in other places
                    updateNode = true;
                    nodeButtonReference = newNodeButton;
                }
            });
            root.getChildren().add(newNodeButton);

        }
    }

    private LinkedList<Edge> convertEdgeData(LinkedList<EdgeDataConversion> edgeData) {
        LinkedList<Edge> edgeList = new LinkedList<Edge>();
        //iterate through the edges
        for (EdgeDataConversion edc : edgeData){
            Node fromNode = globalNodeHashMap.get(edc.getFrom());
            Node toNode = globalNodeHashMap.get(edc.getTo());
            if (fromNode == null || toNode == null){
                continue;
            }
            Edge newEdge = new Edge(fromNode, toNode, (int) Graph.d(fromNode, toNode));
            edgeList.add(newEdge);
        }

        return edgeList;
    }

    private void saveGraphMethod() {
        String nodeData = JsonParser.jsonToString(nodeList);
        String path = currentlySelectedMap.getNodesPath();
        try {
            JsonParser.saveFile(nodeData, path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //save edges
        String edgeData = JsonParser.jsonToStringEdge(edgeList);
        String edgePath = currentlySelectedMap.getEdgesPath();
        try {
            JsonParser.saveFile(edgeData, edgePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Parent createZoomPane(final Group group) {
        final double SCALE_DELTA = 1.1;
        final StackPane zoomPane = new StackPane();

        zoomPane.getChildren().add(group);


        final Group scrollContent = new Group(zoomPane);
        scrollPane.setContent(scrollContent);
        
        
        if(mapSelector.getValue().getInitials().equals("CampusMap")) {
		    scrollContent.setTranslateX(-517);
	    	scrollContent.setTranslateY(-196);
	    }
        
        //Removes Scroll bars
        scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);

        scrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observable,
                                Bounds oldValue, Bounds newValue) {
                zoomPane.setMinSize(newValue.getWidth(), newValue.getHeight());
            }
        });

        scrollPane.setPrefViewportWidth(800);
        scrollPane.setPrefViewportHeight(700);

        
        class DragContext {

            double mouseAnchorX;
            double mouseAnchorY;

            double translateAnchorX;
            double translateAnchorY;

        }

        
        zoomPane.setOnMouseMoved(new EventHandler<MouseEvent>() {
        	public void handle(MouseEvent event) {
        		mouseYposition = event.getY();
        		mouseXposition = event.getX();
        		
        	}
        });
        
        
        zoomPane.setOnScroll(new EventHandler<ScrollEvent>() {
  	      @Override
  	      public void handle(ScrollEvent event) {
  	    	  double zoomFactor = 1.1;
  	    	  
  	    	  
  	          if (event.getDeltaY() <= 0) {
  	              // zoom out
  	              zoomFactor = 1 / zoomFactor;
  	              
  	          }
  	          Scale scale = new Scale(zoomFactor, zoomFactor, mouseXposition, mouseYposition);
  	          
  	          if ( zoomFactor > 1 && k < 10) {
  	        	  
  		          scrollContent.getTransforms().add(scale);
  	        	  k++;
  	          }
  	          if (zoomFactor < 1 && k > -5 ){
  		          scrollContent.getTransforms().add(scale);
  	        	  k--;
  	          }
  	                    
  	          event.consume();
  	      }
  	    });

        // Panning via drag....
        DragContext sceneDragContext = new DragContext();
        scrollContent.setOnMousePressed(new EventHandler<MouseEvent>() {
  	      @Override
  	      public void handle(MouseEvent event) {
  	    	  sceneDragContext.mouseAnchorX = event.getSceneX();
  	            sceneDragContext.mouseAnchorY = event.getSceneY();

  	            sceneDragContext.translateAnchorX = scrollContent.getTranslateX();
  	            sceneDragContext.translateAnchorY = scrollContent.getTranslateY();
  	      }
  	    });

        scrollContent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	scrollContent.setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
            	scrollContent.setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);
            	event.consume();
            	}
        });

        return scrollPane;
    }

    private void loadMap(Pane root, Canvas canvas, Parent zoomPane, Pane NodePane, ImageView imageView) {
        
    	//Generate the Global map graph
        createGlobalGraph();
    	
    	k = 0; // Reset Zoom Variable
        disableKey = false;
        warningLabel.setText(" ");
        NodePane.getChildren().clear();
        //clear existing node list
        root.getChildren().remove(zoomPane);
        root.getChildren().remove(canvas);
        root.getChildren().remove(imageView); //remove current map, then load new one
        nodeList.clear();
        edgeListConversion.clear();
        edgeList.clear();
        // TODO only load the selected map content
        currentlySelectedMap = mapSelector.getValue();
        nodeList = JsonParser.getJsonContent(currentlySelectedMap.getNodesPath());
        edgeListConversion = JsonParser.getJsonContentEdge(currentlySelectedMap.getEdgesPath());
        edgeList = convertEdgeData(edgeListConversion);
    	
        
    	/* ^^^^^^^^^
    	 * IMPORTANT, THE PROGRAM WILL NOT RUN IF WE DONT HAVE ACTUAL FILES
    	 * WHERE THESE PATHS ARE POINTING TO, FOR NOW, CREATE TEMP ONES AND THEN
    	 * OVERRIDE THEM.
    	 */

        File newMapFile = new File(currentlySelectedMap.getMapPath()); //MUST ADD png extension!
        Image mapImage = new Image(newMapFile.toURI().toString());
        imageView.setImage(mapImage);
        NodePane.setPrefSize(2450, 1250);

		switch (mapSelector.getValue().getInitials() + mapSelector.getValue().getFloor()) {
		case "CampusMap":
			NodePane.setPrefSize(3000, 2000);
			imageView.setScaleX(0.75);
			imageView.setScaleY(0.75);
			imageView.relocate(-1000, -600);
			NodePane.setScaleX(0.75);
			NodePane.setScaleY(0.75);
			NodePane.relocate(-965, -643);
			zField.setText("0");
			buttonRescale = 1 / 0.75;
			break;
		case "AKB":
			imageView.setScaleX(0.6536);
			imageView.setScaleY(0.6536);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6536);
			NodePane.setScaleY(0.6536);
			NodePane.relocate(-212, -88);
			buttonRescale = 1 / 0.6536;
			zField.setText("-2");
			break;
		case "AK1":
			imageView.setScaleX(0.5161);
			imageView.setScaleY(0.5161);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5161);
			NodePane.setScaleY(0.5161);
			NodePane.relocate(-218, -22);
			buttonRescale = 1 / 0.5161;
			zField.setText("-1");
			break;
		case "AK2":
			imageView.setScaleX(0.6706);
			imageView.setScaleY(0.6706);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6706);
			NodePane.setScaleY(0.6706);
			NodePane.relocate(-206, -57);
			buttonRescale = 1 / 0.6706;
			zField.setText("0");
			break;
		case "AK3":
			imageView.setScaleX(0.6536);
			imageView.setScaleY(0.6536);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6536);
			NodePane.setScaleY(0.6536);
			NodePane.relocate(-212, -0);
			buttonRescale = 1 / 0.6536;
			zField.setText("1");
			break;
		case "BHB":
			imageView.setScaleX(0.5427);
			imageView.setScaleY(0.5427);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5427);
			NodePane.setScaleY(0.5427);
			NodePane.relocate(-200, -90);
			buttonRescale = 1 / 0.5427;
			zField.setText("-1");
			break;
		case "BH1":
			imageView.setScaleX(0.5476);
			imageView.setScaleY(0.5476);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5476);
			NodePane.setScaleY(0.5476);
			NodePane.relocate(-220, -86);
			buttonRescale = 1 / 0.5476;
			zField.setText("0");
			break;
		case "BH2":
			imageView.setScaleX(0.5438);
			imageView.setScaleY(0.5438);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5438);
			NodePane.setScaleY(0.5438);
			NodePane.relocate(-220, -99);
			buttonRescale = 1 / 0.5438;
			zField.setText("1");
			break;
		case "BH3":
			imageView.setScaleX(0.5358);
			imageView.setScaleY(0.5358);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5358);
			NodePane.setScaleY(0.5358);
			NodePane.relocate(-220, -110);
			buttonRescale = 1 / 0.5358;
			zField.setText("2");
			break;
		case "CC1":
			imageView.setScaleX(0.6107);
			imageView.setScaleY(0.6107);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6107);
			NodePane.setScaleY(0.6107);
			NodePane.relocate(-222, -59);
			buttonRescale = 1 / 0.6107;
			zField.setText("-1");
			break;
		case "CC2":
			imageView.setScaleX(0.6127);
			imageView.setScaleY(0.6127);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6127);
			NodePane.setScaleY(0.6127);
			NodePane.relocate(-222, -59);
			buttonRescale = 1 / 0.6127;
			zField.setText("0");
			break;
		case "CC3":
			imageView.setScaleX(0.6061);
			imageView.setScaleY(0.6061);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6061);
			NodePane.setScaleY(0.6061);
			NodePane.relocate(-222, -59);
			buttonRescale = 1 / 0.6061;
			zField.setText("1");
			break;
		case "GLSB":
			imageView.setScaleX(0.5686);
			imageView.setScaleY(0.5686);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5686);
			NodePane.setScaleY(0.5686);
			NodePane.relocate(-225, -42);
			buttonRescale = 1 / 0.5686;
			zField.setText("-3");
			break;
		case "GLB":
			imageView.setScaleX(0.5409);
			imageView.setScaleY(0.5409);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5409);
			NodePane.setScaleY(0.5409);
			NodePane.relocate(-225, -42);
			buttonRescale = 1 / 0.5409;
			zField.setText("-2");
			break;
		case "GL1":
			imageView.setScaleX(0.5678);
			imageView.setScaleY(0.5678);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5678);
			NodePane.setScaleY(0.5678);
			NodePane.relocate(-225, -42);
			buttonRescale = 1 / 0.5678;
			zField.setText("-1");
			break;
		case "GL2":
			imageView.setScaleX(0.5638);
			imageView.setScaleY(0.5638);
			imageView.relocate(-0, 0);
			NodePane.setScaleX(0.5638);
			NodePane.setScaleY(0.5638);
			NodePane.relocate(-225, -42);
			buttonRescale = 1 / 0.5638;
			zField.setText("0");
			break;
		case "GL3":
			imageView.setScaleX(0.6119);
			imageView.setScaleY(0.6119);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6119);
			NodePane.setScaleY(0.6119);
			NodePane.relocate(-225, -42);
			buttonRescale = 1 / 0.6119;
			zField.setText("1");
			break;
		case "HHB":
			imageView.setScaleX(0.5181);
			imageView.setScaleY(0.5181);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5181);
			NodePane.setScaleY(0.5181);
			NodePane.relocate(-360, -22);
			buttonRescale = 1 / 0.5181;
			zField.setText("-3");
			break;
		case "HH1":
			imageView.setScaleX(0.5535);
			imageView.setScaleY(0.5535);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5535);
			NodePane.setScaleY(0.5535);
			NodePane.relocate(-338, -37);
			buttonRescale = 1 / 0.5535;
			zField.setText("-2");
			break;
		case "HH2":
			imageView.setScaleX(0.6067);
			imageView.setScaleY(0.6067);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6067);
			NodePane.setScaleY(0.6067);
			NodePane.relocate(-298, -50);
			buttonRescale = 1 / 0.6067;
			zField.setText("-1");
			break;
		case "HH3":
			imageView.setScaleX(0.5917);
			imageView.setScaleY(0.5917);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5917);
			NodePane.setScaleY(0.5917);
			NodePane.relocate(-310, -48);
			buttonRescale = 1 / 0.5917;
			zField.setText("0");
			break;
		case "HHAPT":
			imageView.setScaleX(0.8197);
			imageView.setScaleY(0.8197);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.8197);
			NodePane.setScaleY(0.8197);
			NodePane.relocate(-130, -50);
			buttonRescale = 1 / 0.8197;
			zField.setText("-1");
			break;
		case "HHGAR":
			imageView.setScaleX(0.8172);
			imageView.setScaleY(0.8172);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.8172);
			NodePane.setScaleY(0.8172);
			NodePane.relocate(-133, -53);
			buttonRescale = 1 / 0.8172;
			zField.setText("-2");
			break;
		case "PC1":
			imageView.setScaleX(0.6764);
			imageView.setScaleY(0.6764);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6764);
			NodePane.setScaleY(0.6764);
			NodePane.relocate(-208, -58);
			buttonRescale = 1 / 0.6764;
			zField.setText("0");
			break;
		case "PC2":
			imageView.setScaleX(0.6006);
			imageView.setScaleY(0.6006);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6006);
			NodePane.setScaleY(0.6006);
			NodePane.relocate(-222, -48);
			buttonRescale = 1 / 0.6006;
			zField.setText("1");
			break;
		case "SHB":
			imageView.setScaleX(0.5464);
			imageView.setScaleY(0.5464);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5464);
			NodePane.setScaleY(0.5464);
			NodePane.relocate(-224, -88);
			buttonRescale = 1 / 0.5464;
			zField.setText("-1");
			break;
		case "SH1":
			imageView.setScaleX(0.5583);
			imageView.setScaleY(0.5583);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5583);
			NodePane.setScaleY(0.5583);
			NodePane.relocate(-224, -82);
			buttonRescale = 1 / 0.5583;
			zField.setText("0");
			break;
		case "SH2":
			imageView.setScaleX(0.5556);
			imageView.setScaleY(0.5556);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5556);
			NodePane.setScaleY(0.5556);
			NodePane.relocate(-224, -86);
			buttonRescale = 1 / 0.5556;
			zField.setText("1");
			break;
		case "SH3":
			imageView.setScaleX(0.5544);
			imageView.setScaleY(0.5544);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5544);
			NodePane.setScaleY(0.5544);
			NodePane.relocate(-224, -83);
			buttonRescale = 1 / 0.5544;
			zField.setText("2");
			break;
		case "FLSB":
			imageView.setScaleX(0.7882);
			imageView.setScaleY(0.7882);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.7882);
			NodePane.setScaleY(0.7882);
			NodePane.relocate(-150, -80);
			buttonRescale = 1 / 0.7882;
			zField.setText("-4");
			break;
		case "FLB":
			imageView.setScaleX(0.7601);
			imageView.setScaleY(0.7601);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.7601);
			NodePane.setScaleY(0.7601);
			NodePane.relocate(-170, -55);
			buttonRescale = 1 / 0.7601;
			zField.setText("-3");
			break;
		case "FL1":
			imageView.setScaleX(0.6098);
			imageView.setScaleY(0.6098);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6098);
			NodePane.setScaleY(0.6098);
			NodePane.relocate(-250, -52);
			buttonRescale = 1 / 0.6098;
			zField.setText("-2");
			break;
		case "FL2":
			imageView.setScaleX(0.5585);
			imageView.setScaleY(0.5585);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5585);
			NodePane.setScaleY(0.5585);
			NodePane.relocate(-250, -40);
			buttonRescale = 1 / 0.5585;
			zField.setText("-1");
			break;
		case "FL3":
			imageView.setScaleX(0.5515);
			imageView.setScaleY(0.5515);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5515);
			NodePane.setScaleY(0.5515);
			NodePane.relocate(-270, -40);
			buttonRescale = 1 / 0.5515;
			zField.setText("0");
			break;
		case "WestB":
			imageView.setScaleX(0.6305);
			imageView.setScaleY(0.6305);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6305);
			NodePane.setScaleY(0.6305);
			NodePane.relocate(-310, -55);
			canvas.setScaleX(0.6305);
			canvas.setScaleY(0.6305);
			canvas.relocate(-310, -55);
			buttonRescale = 1 / 0.6305;
			break;
		case "West1":
			imageView.setScaleX(0.5949);
			imageView.setScaleY(0.5949);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5949);
			NodePane.setScaleY(0.5949);
			NodePane.relocate(-270, -50);
			canvas.setScaleX(0.5949);
			canvas.setScaleY(0.5949);
			canvas.relocate(-270, -50);
			buttonRescale = 1 / 0.5949;
			break;
		case "West2":
			imageView.setScaleX(0.5929);
			imageView.setScaleY(0.5929);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5929);
			NodePane.setScaleY(0.5929);
			NodePane.relocate(-270, -50);
			canvas.setScaleX(0.5929);
			canvas.setScaleY(0.5929);
			canvas.relocate(-270, -50);
			buttonRescale = 1 / 0.5929;
			break;
		case "SL3":
			imageView.setScaleX(0.6840);
			imageView.setScaleY(0.6840);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6840);
			NodePane.setScaleY(0.6840);
			NodePane.relocate(-270, -60);
			canvas.setScaleX(0.6840);
			canvas.setScaleY(0.6840);
			canvas.relocate(-270, -60);
			buttonRescale = 1 / 0.6840;
			break;
		case "SL4":
			imageView.setScaleX(0.6988);
			imageView.setScaleY(0.6988);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6988);
			NodePane.setScaleY(0.6988);
			NodePane.relocate(-270, -60);
			canvas.setScaleX(0.6988);
			canvas.setScaleY(0.6988);
			canvas.relocate(-270, -60);
			buttonRescale = 1 / 0.6988;
			break;
		case "WS1":
			imageView.setScaleX(0.6116);
			imageView.setScaleY(0.6116);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6116);
			NodePane.setScaleY(0.6116);
			NodePane.relocate(-225, -45);
			canvas.setScaleX(0.6116);
			canvas.setScaleY(0.6116);
			canvas.relocate(-225, -45);
			buttonRescale = 1 / 0.6116;
			break;
              //TODO ADD BUILDINGS

                    
        }

        drawEdges(edgeList, gc, NodePane);

        final Group group = new Group(imageView, NodePane);
        zoomPane = createZoomPane(group);
        root.getChildren().add(zoomPane);
    }

//    public void updateNodeMethod(Parent zoomPane, Pane NodePane) {
//        int x = -1, y = -1, z = -1;
//
//        /************************************************/
//        try {
//            x = Integer.parseInt(xField.getText());
//            y = Integer.parseInt(yField.getText());
//            z = Integer.parseInt(zField.getText());
//        } catch (NumberFormatException e) {
//            System.err.println("NumberFormatException: " + e.getMessage());
//        }
//        if (updateNode) {
//            nameField.setText(currentlySelectedMap.getInitials() + currentlySelectedMap.getFloor() + ":" + typeSelector.getValue() + ":" + xField.getText() + ":" + yField.getText());
//
//            for (int i = 0; i < nodeList.size(); i++) {
//                if (Objects.equals(nodeReference, nodeList.get(i).getName())) {
//                    //root.getChildren().remove(nodeButtonReference);
//                    // TODO If a node is updated the edges also have to be updated
//                    nodeList.get(i).setX(x);
//                    nodeList.get(i).setY(y);
//                    nodeList.get(i).setZ(z);
//                    nodeList.get(i).setName(nameField.getText());
//                    nodeList.get(i).setIsPlace(isPlace.isSelected());
//                    nodeList.get(i).setType(typeSelector.getValue());
//
//                    nodeButtonReference.relocate(x, y);
//                }
//                //set all fields and then break out of this
//            }
//            updateNode = false;
//        }
//
//        saveGraphMethod();
//
//        loadMap(root, canvas, zoomPane, NodePane, imageView);
//    }
}