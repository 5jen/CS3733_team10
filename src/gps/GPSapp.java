package gps;

import TurnByTurn.Step;
import TurnByTurn.stepIndicator;
import io.JsonParser;
import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Preloader;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.PerspectiveTransform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.scene.web.*;
import javafx.util.Duration;
import node.*;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout.Alignment;

import com.sun.javafx.application.LauncherImpl;

import Calendar.CalendarEvents;
import Calendar.MyEvent;
import planner.v1.EmailSender;
import gps.MyPreloader;


public class GPSapp extends Application {
	public static void main(String[] args) {
		LauncherImpl.launchApplication(GPSapp.class, MyPreloader.class, args);
	}

	public GPSapp() {
		// Constructor is called after BEFORE_LOAD.
		// System.out.println(GPSapp.STEP() + "MyApplication constructor called,
		// thread: " + Thread.currentThread().getName());
	}

	// Load up the JSON data and create the nodes for the map
	JsonParser json = new JsonParser();
	LinkedList<Node> nodeList = JsonParser.getJsonContent("Graphs/Nodes/CampusMap.json");
	LinkedList<EdgeDataConversion> edgeListConversion = JsonParser
			.getJsonContentEdge("Graphs/Edges/CampusMapEdges.json");

	// LinkedList<Edge> edgeList = convertEdgeData(edgeListConversion);
	Canvas canvas = new Canvas(3000, 2000);
	GraphicsContext gc = canvas.getGraphicsContext2D();
	boolean start, end = false, toggle = true, startBool = false, destBool = false, startButtonBool = false,
			destButtonBool = false;
	String startNode, endNode;
	Graph graph = new Graph();
	ObservableList<Node> LocationOptions = FXCollections.observableArrayList();
	ListView<Node> StartList = new ListView<>();
	ListView<Node> DestList = new ListView<>();
	TextField StartText = new TextField();
	TextField DestText = new TextField();
	int k = 0; // Set Max zoom Variable
	static Parent zoomPane;
	Group scrollContent;
	Pane NodePane = new Pane();
	Label BuildingNameLabel = new Label();
	Double buttonRescale = 1 / 0.75;
	Button startButton = null, endButton = null, currentButton = null;
	VBox directionBox = new VBox(2);
	ScrollPane s1 = new ScrollPane();
	double mouseYposition, mouseXposition;
	double OriginalScale = 1.0;
	Label directionsTitle = new Label("Directions");

	// Groups to attach layered map
	Group LayerGroup = new Group();
	
	Group ChangeFloorButtonGroup = new Group();

	// TODO PROBABLY CHANGE THIS TO SELECT BUILDING AND THEN SUB DROP DOWN TO
	// SELECT FLOOR
	ObservableList<Map> mapOptions = FXCollections.observableArrayList();
	final ComboBox<Map> mapSelector = new ComboBox<Map>(mapOptions);

	ObservableList<ComboBox<Map>> buildingOptions = FXCollections.observableArrayList();
	final ComboBox<ComboBox<Map>> buildingSelector = new ComboBox<>(buildingOptions);

	// For Rescaling the application
	final Pane root = new Pane();
	final BorderPane Borders = new BorderPane();
	ScrollPane scrollPane = new ScrollPane();
	Scene scene = new Scene(root, 1100, 750);
	double scaleXratio = 1;
	double scaleYRatio = 1;
	double stageInitialWidthDifference = 100;
	double stageInitialHeightDifference = 100;
	int initialPanAmountX = 0, initialPanAmountY = 0;

	// Building Buildings with their content
	Building Campus = new Building("Campus");
	Building AtwaterKent = new Building("Atwater Kent"); // need layered maps
	Building BoyntonHall = new Building("Boynton Hall");
	Building CampusCenter = new Building("Campus Center");
	Building GordonLibrary = new Building("Gordon Library");
	Building HigginsHouse = new Building("Higgins House"); // need layered maps
	Building HigginsHouseGarage = new Building("Higgins House Garage"); // need
																		// layered
																		// maps
	Building ProjectCenter = new Building("Project Center");
	Building StrattonHall = new Building("Stratton Hall");
	Building FullerLabs = new Building("Fuller Labs");
	Building SalisburyLabs = new Building("Salisbury Labs");
	Building WestStreet = new Building("157 West Street");
	Building WashburnShops = new Building("Washburn Shops");
	Building RecCenter = new Building("Rec Center");
	Building Harrington = new Building("Harrington Auditorium");
	Building HigginsLabs = new Building("Higgins Labs");
	Building AldenHall = new Building("Alden Hall");
	static Building NullBuilding = new Building("Null Building");

	// Map Buildings with their content
	Map CampusMap = new Map("Campus Map", "CampusMap", "CS3733_Graphics/CampusMap.png", "Graphs/Nodes/CampusMap.json",
			"Graphs/Edges/CampusMapEdges.json", 0, 0, 0, 2.6053, "");

	Map AtwaterKentB = new Map("Atwater Kent B", "AK", "CS3733_Graphics/AKB.png", "Graphs/Nodes/AKB.json",
			"Graphs/Edges/AKBEdges.json", -2.617, 1548, 594, 0.1627, "B");
	Map AtwaterKent1 = new Map("Atwater Kent 1", "AK", "CS3733_Graphics/AK1.png", "Graphs/Nodes/AK1.json",
			"Graphs/Edges/AK1Edges.json", -2.617, 1548, 594, 0.1312, "1");
	Map AtwaterKent2 = new Map("Atwater Kent 2", "AK", "CS3733_Graphics/AK2.png", "Graphs/Nodes/AK2.json",
			"Graphs/Edges/AK2Edges.json", -2.617, 1548, 594, 0.1692, "2");
	Map AtwaterKent3 = new Map("Atwater Kent 3", "AK", "CS3733_Graphics/AK3.png", "Graphs/Nodes/AK3.json",
			"Graphs/Edges/AK3Edges.json", -2.617, 1548, 594, 0.1690, "3");

	Map GordonLibrarySB = new Map("Gordon Library SB", "GL", "CS3733_Graphics/GLSB.png", "Graphs/Nodes/GLSB.json",
			"Graphs/Edges/GLSBEdges.json", 1.762, 1668, 726, 0.1187, "SB");
	Map GordonLibraryB = new Map("Gordon Library B", "GL", "CS3733_Graphics/GLB.png", "Graphs/Nodes/GLB.json",
			"Graphs/Edges/GLBEdges.json", 1.762, 1668, 726, 0.1251, "B");
	Map GordonLibrary1 = new Map("Gordon Library 1", "GL", "CS3733_Graphics/GL1.png", "Graphs/Nodes/GL1.json",
			"Graphs/Edges/GL1Edges.json", 1.762, 1668, 726, 0.1194, "1");
	Map GordonLibrary2 = new Map("Gordon Library 2", "GL", "CS3733_Graphics/GL2.png", "Graphs/Nodes/GL2.json",
			"Graphs/Edges/GL2Edges.json", 1.762, 1668, 726, 0.1223, "2");
	Map GordonLibrary3 = new Map("Gordon Library 3", "GL", "CS3733_Graphics/GL3.png", "Graphs/Nodes/GL3.json",
			"Graphs/Edges/GL3Edges.json", 1.762, 1668, 726, 0.1387, "3");

	Map BoyntonHallB = new Map("Boynton Hall B", "BH", "CS3733_Graphics/BHB.png", "Graphs/Nodes/BHB.json",
			"Graphs/Edges/BHBEdges.json", 0.157, 1496, 991, 0.0956, "B");
	Map BoyntonHall1 = new Map("Boynton Hall 1", "BH", "CS3733_Graphics/BH1.png", "Graphs/Nodes/BH1.json",
			"Graphs/Edges/BH1Edges.json", 0.157, 1496, 991, 0.0973, "1");
	Map BoyntonHall2 = new Map("Boynton Hall 2", "BH", "CS3733_Graphics/BH2.png", "Graphs/Nodes/BH2.json",
			"Graphs/Edges/BH2Edges.json", 0.157, 1496, 991, 0.0981, "2");
	Map BoyntonHall3 = new Map("Boynton Hall 3", "BH", "CS3733_Graphics/BH3.png", "Graphs/Nodes/BH3.json",
			"Graphs/Edges/BH3Edges.json", 0.157, 1496, 991, 0.1003, "3");

	Map CampusCenter1 = new Map("Campus Center 1", "CC", "CS3733_Graphics/CC1.png", "Graphs/Nodes/CC1.json",
			"Graphs/Edges/CC1Edges.json", -1.413, 1175, 670, 0.1695, "1");
	Map CampusCenter2 = new Map("Campus Center 2", "CC", "CS3733_Graphics/CC2.png", "Graphs/Nodes/CC2.json",
			"Graphs/Edges/CC2Edges.json", -1.413, 1175, 670, 0.166, "2");
	Map CampusCenter3 = new Map("Campus Center 3", "CC", "CS3733_Graphics/CC3.png", "Graphs/Nodes/CC3.json",
			"Graphs/Edges/CC3Edges.json", -1.413, 1175, 670, 0.1689, "3");

	Map HigginsHouseB = new Map("Higgins House B", "HH", "CS3733_Graphics/HHB.png", "Graphs/Nodes/HHB.json",
			"Graphs/Edges/HHBEdges.json", -2.529, 1161, 504, 0.1314, "B");
	Map HigginsHouse1 = new Map("Higgins House 1", "HH", "CS3733_Graphics/HH1.png", "Graphs/Nodes/HH1.json",
			"Graphs/Edges/HH1Edges.json", -2.529, 1161, 504, 0.1364, "1");
	Map HigginsHouse2 = new Map("Higgins House 2", "HH", "CS3733_Graphics/HH2.png", "Graphs/Nodes/HH2.json",
			"Graphs/Edges/HH2Edges.json", -2.529, 1161, 504, 0.1343, "2");
	Map HigginsHouse3 = new Map("Higgins House 3", "HH", "CS3733_Graphics/HH3.png", "Graphs/Nodes/HH3.json",
			"Graphs/Edges/HH3Edges.json", -2.529, 1161, 504, 0.1317, "3");
	Map HigginsHouseAPT = new Map("Higgins House Apartment", "HH", "CS3733_Graphics/HHAPT.png",
			"Graphs/Nodes/HHAPT.json", "Graphs/Edges/HHAPTEdges.json", -0.942, 1215, 394, 0.0521, "APT");
	Map HigginsHouseGAR = new Map("Higgins House Garage", "HH", "CS3733_Graphics/HHGAR.png", "Graphs/Nodes/HHGAR.json",
			"Graphs/Edges/HHGAREdges.json", -0.942, 1215, 394, 0.053, "GAR");

	Map ProjectCenter1 = new Map("Project Center 1", "PC", "CS3733_Graphics/PC1.png", "Graphs/Nodes/PC1.json",
			"Graphs/Edges/PC1Edges.json", 1.71, 1228, 772, 0.0701, "1");
	Map ProjectCenter2 = new Map("Project Center 2", "PC", "CS3733_Graphics/PC2.png", "Graphs/Nodes/PC2.json",
			"Graphs/Edges/PC2Edges.json", 1.71, 1228, 772, 0.1016, "2");

	Map StrattonHallB = new Map("Stratton Hall B", "SH", "CS3733_Graphics/SHB.png", "Graphs/Nodes/SHB.json",
			"Graphs/Edges/SHBEdges.json", 1.71, 1364, 898, 0.0804, "B");
	Map StrattonHall1 = new Map("Stratton Hall 1", "SH", "CS3733_Graphics/SH1.png", "Graphs/Nodes/SH1.json",
			"Graphs/Edges/SH1Edges.json", 1.71, 1364, 898, 0.0813, "1");
	Map StrattonHall2 = new Map("Stratton Hall 2", "SH", "CS3733_Graphics/SH2.png", "Graphs/Nodes/SH2.json",
			"Graphs/Edges/SH2Edges.json", 1.71, 1364, 898, 0.0766, "2");
	Map StrattonHall3 = new Map("Stratton Hall 3", "SH", "CS3733_Graphics/SH3.png", "Graphs/Nodes/SH3.json",
			"Graphs/Edges/SH3Edges.json", 1.71, 1364, 898, 0.0749, "3");

	Map FullerLabsSB = new Map("Fuller Labs SB", "FL", "CS3733_Graphics/FLSB.png", "Graphs/Nodes/FLSB.json",
			"Graphs/Edges/FLSBEdges.json", 1.099, 1636, 497, 0.1735, "SB");
	Map FullerLabsB = new Map("Fuller Labs B", "FL", "CS3733_Graphics/FLB.png", "Graphs/Nodes/FLB.json",
			"Graphs/Edges/FLBEdges.json", 1.099, 1636, 497, 0.1641, "B");
	Map FullerLabs1 = new Map("Fuller Labs 1", "FL", "CS3733_Graphics/FL1.png", "Graphs/Nodes/FL1.json",
			"Graphs/Edges/FL1Edges.json", 1.099, 1636, 497, 0.169, "1");
	Map FullerLabs2 = new Map("Fuller Labs 2", "FL", "CS3733_Graphics/FL2.png", "Graphs/Nodes/FL2.json",
			"Graphs/Edges/FL2Edges.json", 1.099, 1636, 497, 0.168, "2");
	Map FullerLabs3 = new Map("Fuller Labs 3", "FL", "CS3733_Graphics/FL3.png", "Graphs/Nodes/FL3.json",
			"Graphs/Edges/FL3Edges.json", 1.099, 1636, 497, 0.1661, "3");

	// TODO Add rest of maps
	// TODO
	// TODO
	Map SalisburyLabsB = new Map("Salisbury Labs B", "SL", "CS3733_Graphics/SLB.png", "Graphs/Nodes/SLB.json",
			"Graphs/Edges/SLBEdges.json", -1.396, 1438, 717, 0.1636, "B");
	Map SalisburyLabs1 = new Map("Salisbury Labs 1", "SL", "CS3733_Graphics/SL1.png", "Graphs/Nodes/SL1.json",
			"Graphs/Edges/SL1Edges.json", -1.396, 1438, 717, 0.1636, "1");
	Map SalisburyLabs2 = new Map("Salisbury Labs 2", "SL", "CS3733_Graphics/SL2.png", "Graphs/Nodes/SL2.json",
			"Graphs/Edges/SL2Edges.json", -1.396, 1438, 717, 0.1636, "2");
	Map SalisburyLabs3 = new Map("Salisbury Labs 3", "SL", "CS3733_Graphics/SL3.png", "Graphs/Nodes/SL3.json",
			"Graphs/Edges/SL3Edges.json", -1.396, 1438, 717, 0.1636, "3");
	Map SalisburyLabs4 = new Map("Salisbury Labs 4", "SL", "CS3733_Graphics/SL4.png", "Graphs/Nodes/SL4.json",
			"Graphs/Edges/SL4Edges.json", -1.396, 1438, 717, 0.1629, "4");

	Map WestStreetB = new Map("157 West Street B", "West", "CS3733_Graphics/WestB.png", "Graphs/Nodes/WestB.json",
			"Graphs/Edges/WestBEdges.json", -1.413, 1306, 1290, 0.0547, "B");
	Map WestStreet1 = new Map("157 West Street 1", "West", "CS3733_Graphics/West1.png", "Graphs/Nodes/West1.json",
			"Graphs/Edges/West1Edges.json", -1.413, 1306, 1290, 0.0483, "1");
	Map WestStreet2 = new Map("157 West Street 2", "West", "CS3733_Graphics/West2.png", "Graphs/Nodes/West2.json",
			"Graphs/Edges/West2Edges.json", -1.413, 1306, 1290, 0.0532, "2");

	Map WashburnShopsB = new Map("Washburn Shops B", "WS", "CS3733_Graphics/WSB.png", "Graphs/Nodes/WSB.json",
			"Graphs/Edges/WSBEdges.json", 0.157, 1422, 903, 0.1661, "B");
	Map WashburnShops1 = new Map("Washburn Shops 1", "WS", "CS3733_Graphics/WS1.png", "Graphs/Nodes/WS1.json",
			"Graphs/Edges/WS1Edges.json", 0.157, 1422, 903, 0.1661, "1");
	Map WashburnShops2 = new Map("Washburn Shops 2", "WS", "CS3733_Graphics/WS2.png", "Graphs/Nodes/WS2.json",
			"Graphs/Edges/WS2Edges.json", 0.157, 1422, 903, 0.1661, "2");
	Map WashburnShops3 = new Map("Washburn Shops 3", "WS", "CS3733_Graphics/WS3.png", "Graphs/Nodes/WS3.json",
			"Graphs/Edges/WS3Edges.json", 0.157, 1422, 903, 0.1661, "3");
	
	Map HarringtonB = new Map("Harrington Auditorium B", "HA", "CS3733_Graphics/HAB.png", "Graphs/Nodes/HAB.json",
			"Graphs/Edges/HABEdges.json", 0.140, 952, 641, 0.1400, "B");
	Map Harrington1 = new Map("Harrington Auditorium 1", "HA", "CS3733_Graphics/HA1.png", "Graphs/Nodes/HA1.json",
			"Graphs/Edges/HA1Edges.json", 0.140, 952, 641, 0.1400, "1");
	Map Harrington2 = new Map("Harrington Auditorium 2", "HA", "CS3733_Graphics/HA2.png", "Graphs/Nodes/HA2.json",
			"Graphs/Edges/HA2Edges.json", 0.140, 952, 641, 0.1400, "2");
	Map Harrington3 = new Map("Harrington Auditorium 3", "HA", "CS3733_Graphics/HA3.png", "Graphs/Nodes/HA3.json",
			"Graphs/Edges/HA3Edges.json", 0.140, 952, 641, 0.1400, "3");
	
	Map HigginsLabsB = new Map("Higgins Labs B", "HL", "CS3733_Graphics/HLB.png", "Graphs/Nodes/HLB.json",
			"Graphs/Edges/HLBEdges.json", 0.174, 1237, 843, 0.1400, "B");
	Map HigginsLabs1 = new Map("Higgins Labs 1", "HL", "CS3733_Graphics/HL1.png", "Graphs/Nodes/HL1.json",
			"Graphs/Edges/HL1Edges.json", 0.174, 1237, 843, 0.1400, "1");
	Map HigginsLabs2 = new Map("Higgins Labs 2", "HL", "CS3733_Graphics/HL2.png", "Graphs/Nodes/HL2.json",
			"Graphs/Edges/HL2Edges.json", 0.174, 1237, 843, 0.1400, "2");
	Map HigginsLabs3 = new Map("Higgins Labs 3", "HL", "CS3733_Graphics/HL3.png", "Graphs/Nodes/HL3.json",
			"Graphs/Edges/HL3Edges.json", 0.174, 1237, 843, 0.1400, "3");
	
	Map AldenHallSB = new Map("Alden Hall SB", "AH", "CS3733_Graphics/AHSB.png", "Graphs/Nodes/AHSB.json",
			"Graphs/Edges/AHSBEdges.json", -1.396, 1212, 1077, 0.1400, "SB");
	Map AldenHallB = new Map("Alden Hall B", "AH", "CS3733_Graphics/AHB.png", "Graphs/Nodes/AHB.json",
			"Graphs/Edges/AHBEdges.json", -1.396, 1212, 1077, 0.1400, "B");
	Map AldenHall1 = new Map("Alden Hall 1", "AH", "CS3733_Graphics/AH1.png", "Graphs/Nodes/AH1.json",
			"Graphs/Edges/AH1Edges.json", -1.396, 1212, 1077, 0.1400, "1");
	Map AldenHall2 = new Map("Alden Hall 2", "AH", "CS3733_Graphics/AH2.png", "Graphs/Nodes/AH2.json",
			"Graphs/Edges/AH2Edges.json", -1.396, 1212, 1077, 0.1400, "2");

	// set perspective transformations to all 3 groups
	PerspectiveTransform pt = new PerspectiveTransform();
	PerspectiveTransform ptFuller = new PerspectiveTransform();
	PerspectiveTransform ptWest = new PerspectiveTransform();
	final DropShadow shadow = new DropShadow();
	final DropShadow shadowFuller = new DropShadow();
	final DropShadow shadowWest = new DropShadow();
	final FadeTransition fader = new FadeTransition();
	final DropShadow buildingLabelShadow = new DropShadow();

	Path g1path = new Path();
	MoveTo g1moveTo = new MoveTo();
	LineTo g1lineTo = new LineTo();

	// Create the global graph
	Graph globalGraph = new Graph();
	static LinkedList<Building> buildings = new LinkedList<>();
	LinkedList<Map> maps = new LinkedList<>();

	Text keyText = new Text(600, 640, "");
	Text toggleKeyText = new Text(1045, 740, "Show Key");

	// Vars used for displaying Multiple maps after finding the route
	LinkedList<LinkedList<Node>> multiMap = new LinkedList<LinkedList<Node>>();
	int currMaps = 0;
	int currRoute = 0;
	// Button NextInstruction = new Button("Next");
	// Button PrevInstruction = new Button("Prev");

	File NextButtonFile = new File("CS3733_Graphics/MenuGraphics/nextButton.png");
	Image NextButtonImage = new Image(NextButtonFile.toURI().toString());
	ImageView NextInstruction = new ImageView(NextButtonImage);
	ImageView PrevInstruction = new ImageView(NextButtonImage);

	LinkedList<Node> globalNodeList = new LinkedList<Node>();
	// create pin image
	File pinFile = new File("CS3733_Graphics/pin.png");
	Image pinImage = new Image(pinFile.toURI().toString());
	ImageView pinView = new ImageView();

	// create pin image
	File yPinFile = new File("CS3733_Graphics/yellow-pin.png");
	Image yPinImage = new Image(yPinFile.toURI().toString());
	ImageView yPinView = new ImageView();

	File redPinFile = new File("CS3733_Graphics/redPin.png");
	Image redPinImage = new Image(redPinFile.toURI().toString());
	ImageView redPinView = new ImageView();

	File greenPinFile = new File("CS3733_Graphics/greenPin.png");
	Image greenPinImage = new Image(greenPinFile.toURI().toString());
	ImageView greenPinView = new ImageView();

	File goatFile = new File("CS3733_Graphics/goat.png");
	Image goatImage = new Image(goatFile.toURI().toString());
	ImageView goatView = new ImageView();

	boolean pinAttached = false;
	Circle enter = new Circle(10.0, Color.GREEN);
	Circle exit = new Circle(10.0, Color.RED);

	Building BuildingRolledOver = new Building("");
	Building BuildingRolledOverCurrent = new Building("");

	PauseTransition pause = new PauseTransition(Duration.millis(0));

	Button ReturnToCampus = new Button("Back to Campus");

	Button findNearestButton = new Button("Find Nearest");

	// TODO Add Vending Machines and Water Fountains when nodes are made for
	// those types
	ObservableList<String> typeOptions = FXCollections.observableArrayList("Men's Bathroom", "Women's Bathroom",
			"Dining", "Vending Machine", "Water Fountain");
	ComboBox<String> nearestDropdown = new ComboBox<String>(typeOptions);
	VBox nearestBox = new VBox(5);


	// Lists of all nodes of the types
	// TODO Actually make these types of nodes
	// LinkedList<Node> PointNodes = new LinkedList<Node>();
	// LinkedList<Node> StaircaseNodes = new LinkedList<Node>();
	// LinkedList<Node> TransitionNodes = new LinkedList<Node>();
	LinkedList<Node> VendingMachineNodes = new LinkedList<>();
	LinkedList<Node> WaterFountainNodes = new LinkedList<>();
	LinkedList<Node> MensBathroomNodes = new LinkedList<>();
	LinkedList<Node> WomensBathroomNodes = new LinkedList<>();
	// LinkedList<Node> EmergencyPoleNodes = createNodeTypeList("Emergency
	// Pole");
	LinkedList<Node> DiningNodes = new LinkedList<>();
	// LinkedList<Node> ElevatorNodes = new LinkedList<Node>();
	// LinkedList<Node> ComputerLabNodes = new LinkedList<Node>();

	// Button EmailButton = new Button("Email");
	File emailButtonFile = new File("CS3733_Graphics/MenuGraphics/sendEmailButton.png");
	Image emailButtonImage = new Image(emailButtonFile.toURI().toString());
	ImageView EmailButton = new ImageView(emailButtonImage);

	TextField EmailInput = new TextField("");

	// global route variable
	LinkedList<Node> route = new LinkedList<Node>();
	LinkedList<Node> savedRoute = new LinkedList<Node>();
    HashMap<String, Node> globalNodeHashMap = new HashMap<>();

    File dirSliderButtonFile = new File("CS3733_Graphics/MenuGraphics/directionSliderButton.png");
    Image dirSliderImage = new Image(dirSliderButtonFile.toURI().toString());
    ImageView dirSliderButton = new ImageView(dirSliderImage);

    

	// MENU OBJECTS
    static boolean menuIsOut = false;
    static boolean menuEmailIsOut = false;
    static boolean setMenuTransformation = false;
    static boolean aboutMeIsOut = false;
    static boolean directionsAreOut = false;
	static boolean descriptionIsOut = false;
	static boolean instructionsAreOut = false;
	
	Label descriptionTextArea = new Label();

	// UI Panes
	Pane menuPane = new Pane();
	Pane fullMenuPane = new Pane();
	Pane directionsPane = new Pane();
	Pane aboutPane = new Pane();
	Pane descriptionPane = new Pane();
	Pane instructionsPane = new Pane();

	static Group directionsGroup = new Group();
	static Group menuGroup = new Group();
    static Group emailGroup = new Group();
    static Group aboutGroup = new Group();
    static Group descriptionGroup = new Group();
    static Group instructionsGroup = new Group();
    
    //event description elements
    final static Label eventTitle = new Label("Title");
    final static Label eventDescription = new Label("...");
    final static Label eventLocation = new Label("Loc");
    final static Label eventTime = new Label("Time");
    final static Label eventDescriptionLabel = new Label("Event Info: ");
    final static Label eventDescriptionLine1 = new Label("");
    final static Label eventDescriptionLine2 = new Label("");


    //attach the image of the event *** MAKE SURE TO MOVE THINGS WHEN ATTACHING THINGS TO PANE
    //temp image
    static File eventDescriptionImageFile = new File("CS3733_Graphics/EventImages/WPIEvent.png");
    static Image eventDescriptionImage = new Image(eventDescriptionImageFile.toURI().toString());
    static ImageView eventDescriptionImageView = new ImageView(eventDescriptionImage);

	
    
    //Google Calendars things
    static LinkedList<MyEvent> myEventsData = new LinkedList<MyEvent>(); //This is the unparsed data right from google
    static LinkedList<MyEvent> myEvents = new LinkedList<MyEvent>(); //This is populated with the information that we obtain from the above data
    
    //Create the EventMatchers
    
    static LinkedList<String> WPIWords = new LinkedList<String>(Arrays.asList("WPI", "event", "event", "Gompei", "Soccom", "sga", "talent", "show", "perform", "goat", "cdc", "acm"));
    
    static LinkedList<String> FoodWords = new LinkedList<String>(Arrays.asList("food", "ice cream", "snacks", "cookies", "bbq", "dunkin", "pizza", "popcorn"));
    
    static LinkedList<String> SportWords = new LinkedList<String>(Arrays.asList("workout","Sport", "Soccer", "ball", "basketball", "track", "swimming", "zumba", "intermural", "rec center", "football", "base", "hockey", "tennis", "dance", "athletic"));

    static LinkedList<String> AwardWords = new LinkedList<String>(Arrays.asList("award", "ceremony", "trophy", "honor", "banquet", "Humanities", "awarding", "prize", "certificates"));
    
    static LinkedList<String> MovieWords = new LinkedList<String>(Arrays.asList("movie", "film", "Film", "Film", "theater"));


    //variable to store the event your rolled over
    
    //***
    static MyEvent currentEvent = new MyEvent();
    static boolean mouseOnEvent = false;
    
    
    //Get the current date so that we only grab events from this time forward from google calendars
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
 
    //get current date time with Calendar()
    Calendar cal = Calendar.getInstance();
    
    //make a global var that is used for cycling through weeks of events
    static int currWeek;// when we pass this, add 7 to it in calendars class
    static Label WeekLabel = new Label();
    
    //*************************
    
	
    @Override
    public void init() throws Exception {
        //System.out.println(GPSapp.STEP() + "MyApplication#init (doing some heavy lifting), thread: " + Thread.currentThread().getName());

        // Perform some heavy lifting (i.e. database start, check for application updates, etc. )
        for (int i = 0; i < 10000; i++) {
            double progress = (100 * i) / 10000;
            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
        }
        scene.getStylesheets().add(getClass().getResource("Buttons.css").toExternalForm());

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

        HigginsHouseGarage.addMap(HigginsHouseGAR);
        HigginsHouseGarage.addMap(HigginsHouseAPT);

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

        // Store the Buildings in a list
        //TODO Add rest of maps
        SalisburyLabs.addMap(SalisburyLabsB);
		SalisburyLabs.addMap(SalisburyLabs1);
		SalisburyLabs.addMap(SalisburyLabs2);
		SalisburyLabs.addMap(SalisburyLabs3);
		SalisburyLabs.addMap(SalisburyLabs4);

		WashburnShops.addMap(WashburnShopsB);
		WashburnShops.addMap(WashburnShops1);
		WashburnShops.addMap(WashburnShops2);
		WashburnShops.addMap(WashburnShops3);

		WestStreet.addMap(WestStreetB);
		WestStreet.addMap(WestStreet1);
		WestStreet.addMap(WestStreet2);
		
		Harrington.addMap(HarringtonB);
		Harrington.addMap(Harrington1);
		Harrington.addMap(Harrington2);
		Harrington.addMap(Harrington3);
		
		HigginsLabs.addMap(HigginsLabsB);
		HigginsLabs.addMap(HigginsLabs1);
		HigginsLabs.addMap(HigginsLabs2);
		HigginsLabs.addMap(HigginsLabs3);
		
		AldenHall.addMap(AldenHallSB);
		AldenHall.addMap(AldenHallB);
		AldenHall.addMap(AldenHall1);
		AldenHall.addMap(AldenHall2);
        
        
        // TODO Add more buildings to this list
        buildings.add(Campus);
        buildings.add(AtwaterKent);
        buildings.add(GordonLibrary);
        buildings.add(CampusCenter);
        buildings.add(HigginsHouse);
        buildings.add(HigginsHouseGarage);
        buildings.add(StrattonHall);
        buildings.add(ProjectCenter);
        buildings.add(BoyntonHall);
        buildings.add(FullerLabs);
        buildings.add(SalisburyLabs);
        buildings.add(WashburnShops);
        buildings.add(WestStreet);
        buildings.add(HigginsLabs);
        buildings.add(Harrington);
        buildings.add(AldenHall);
        


        toggleKeyText.setFont(Font.font("manteka", 10));
        keyText.setFont(Font.font("manteka", 20));

        // Iterate over the list of buildings and add their maps to another list
        //LinkedList<Map> maps = new LinkedList<>();
        for (Building b : buildings) {
            maps.addAll(b.getMaps());
        }
        mapOptions.addAll(maps);


        EmailInput.setPromptText("Email");

        //Generate the Global map graph
        createGlobalGraph();

        //Lists of all nodes of the types
        //TODO Actually make these types of nodes
//        PointNodes = new LinkedList<Node>();
//        StaircaseNodes = new LinkedList<Node>();
//        TransitionNodes = new LinkedList<Node>();
        VendingMachineNodes = createNodeTypeList("Vending Machine");
        WaterFountainNodes = createNodeTypeList("Water Fountain");
        MensBathroomNodes = createNodeTypeList("Men's Bathroom");
        WomensBathroomNodes = createNodeTypeList("Women's Bathroom");
        //EmergencyPoleNodes = createNodeTypeList("Emergency Pole");
        DiningNodes = createNodeTypeList("Dining");
        //ElevatorNodes = new LinkedList<Node>();
        //ComputerLabNodes = new LinkedList<Node>();
        
      //now we can create the local edge connections
        LinkedList<Edge> edgeList = convertEdgeData(edgeListConversion);
        //root.getChildren().remove(imageViewBlur);

        //generate the local map graph
        //graph = createGraph(graph, nodeList, edgeList);
      //Initialize the Drop down menu for initial Map
    	for(int i = 0; i < globalNodeList.size() ; i ++){
    		if(globalNodeList.get(i).getIsPlace())
    			LocationOptions.add(globalNodeList.get(i));
        }	 

    }
    
    
	@Override
    public void start(Stage primaryStage) {
		
		//initialize the current week to today;
		//increment and decrement by 7 at a time
		currWeek = 0;
		
		//Prent Current date
		System.out.println("************** *** * * * * * ****");
		System.out.println(dateFormat.format(cal.getTime()));
	    //System.out.println(dateFormat.format(date));
		
		//Apply these actions to the google button
		
		
		
		

		
		
		//******************* PROBABLY MOVE ABOUT TO METHOD AND CALL.. BUT ONLY NEEDS TO BE CALLED ON LAUNCH**********


		File iconFile = new File("CS3733_Graphics/PI.png");
		Image iconImage = new Image(iconFile.toURI().toString());
		primaryStage.getIcons().add(iconImage);
		primaryStage.setTitle("PiNavigator");

		double width = 80;
		double height = 60;
		pt = setCorners(pt);
		ptFuller = setCornersFuller(ptFuller);
		ptWest = setCornersWest(ptWest);
		shadow.setInput(pt);
		shadowFuller.setInput(ptFuller);
		shadowWest.setInput(ptWest);

		// create Label for directions

		directionsTitle.setTextFill(Color.WHITE);
		directionsTitle.setFont(Font.font("manteka", 20));
		directionsTitle.setLayoutX(820);
		directionsTitle.setLayoutY(100);
		nearestDropdown.setValue("Dining");

		// Create a label and box for warnings, ie when the coordinates are
		// outside the name
		final HBox warningBox = new HBox(0);
		final Label warningLabel = new Label("");
		warningLabel.setTextFill(Color.WHITE);
		warningBox.setLayoutX(10);
		warningBox.setLayoutY(680);
		warningBox.getChildren().addAll(warningLabel);

		// Find Route Button
		final Button findRouteButton = new Button("Find Route");
		// findRouteButton.relocate(640, 640);

		// Searchable text boxes

		// create Label for Start and Destination
		Label StartLabel = new Label(" Start");
		StartLabel.setTextFill(Color.BLACK);
		StartLabel.setFont(Font.font("manteka", 15));
		StartLabel.setLayoutX(20);
		StartLabel.setLayoutY(610);
		Label DestLabel = new Label(" Destination");
		DestLabel.setTextFill(Color.BLACK);
		DestLabel.setFont(Font.font("manteka", 15));
		DestLabel.setLayoutX(300);
		DestLabel.setLayoutY(610);
		
		mapSelector.setPrefWidth(170);

		ReturnToCampus.setTextFill(Color.BLACK);
		ReturnToCampus.setFont(Font.font("manteka", 10));

		// Labels for the direction
		// MOVE TO METHOD AND USE FOR LOOP ONCE WE HAVE THE ROUTE CALCULATED

		// Create the map image
		File mapFile = new File("CS3733_Graphics/CampusMap.png");
		mapSelector.setValue(mapSelector.getItems().get(0)); // Default Map when
																// App is opened
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
		Image mapImage = new Image(mapFile.toURI().toString());
		ImageView imageView = new ImageView();
		imageView.setImage(mapImage);
		imageView.setLayoutX(0);
		imageView.setLayoutY(0);

		// create background
		File backgroundFile = new File("CS3733_Graphics/Background.jpg");
		Image bgImage = new Image(backgroundFile.toURI().toString());
		ImageView bgView = new ImageView();
		bgView.setImage(bgImage);
		bgView.setLayoutX(0);
		bgView.setLayoutY(0);


		pinView.setImage(pinImage);
		yPinView.setImage(yPinImage);
		redPinView.setImage(redPinImage);
		greenPinView.setImage(greenPinImage);
		goatView.setImage(goatImage);
		
		EmailInput.setPromptText("Email");
		EmailInput.setTooltip(new Tooltip("Enter your email here"));

		ReturnToCampus.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				mapSelector.setValue(mapSelector.getItems().get(0));
				initialPanAmountX = -340;
				initialPanAmountY = -165;
				loadMap(root, imageView);
				fixUI();
			}
		});

		// Add images to the screen
		root.getChildren().add(bgView); // Must add background image first!
		// root.getChildren().add(mapSelectionBoxV);
		// root.getChildren().add(imageView);
		root.getChildren().add(keyText);
		root.getChildren().add(toggleKeyText);

		// root.getChildren().addAll(directionsTitle, DestLabel, StartLabel);


		EmailButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
						
				for (int i = 0; i < currMaps; i++) {
					
					
					
					if (currRoute >= 0 && currRoute <= currMaps - 1) {
						
						
						if(i != 0) {
							currRoute++;
							changeInstructions(NodePane, root, imageView);
						}
							
					
								
					
						zoomPane.toFront();
		        		
		        		SnapshotParameters parameters = new SnapshotParameters();
		        		parameters.setViewport(new Rectangle2D(0, 0, (int) (1100 + stageInitialWidthDifference), (int) (750 + stageInitialHeightDifference))); 
		                WritableImage wi = new WritableImage(1100, 750);
		                WritableImage snapshot = root.snapshot(parameters, wi);

		                File output = new File("snapshot" + i + ".png");
		                try {
		        			ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", output);
		        		} catch (IOException e) {
		        			// TODO Auto-generated catch block
		        			e.printStackTrace();
		        		}
					}
				
				}
				//save variable i here to see how many total maps there are
				currRoute = 0;
				changeInstructions(NodePane, root, imageView);
				fixUI();
				
				
				
				
				
				stepIndicator steps = new stepIndicator(savedRoute);

				LinkedList<Step> emailDirections = steps.lInstructions();
				try{
					EmailSender.sendDirections(emailDirections, EmailInput.getText());
				} catch(RuntimeException e) {
					System.out.println("Please type in an email");
				}
				//keyText.setText("Email Sent");
				if (!menuEmailIsOut) {
					emailPaneAnimation(emailGroup);
					menuEmailIsOut = true;
				} else {
					emailPaneAnimation(emailGroup);
					menuEmailIsOut = false;
				}

				
			}
		});

		Tooltip emailButtonTooltip = new Tooltip();
		emailButtonTooltip.setText("Click to email directions");

		// Removes top bar!! Maybe implement a custom one to look better
		// primaryStage.initStyle(StageStyle.UNDECORATED);

		stageInitialWidthDifference = scene.getWidth() - 1100;
		stageInitialHeightDifference = scene.getHeight() - 750;

		imageView.setScaleX(0.75);
		imageView.setScaleY(0.75);
		imageView.relocate(-1000, -600);
		NodePane.setPrefSize(3000, 2000);
		canvas.resize(3000, 2000);
		canvas.setScaleX(0.75);
		canvas.setScaleY(0.75);
		canvas.relocate(-965, -643);
		highLight(NodePane, imageView, root, keyText);
		drawNodes(nodeList, NodePane, root, StartText, DestText, imageView);
		NodePane.setScaleX(0.75);
		NodePane.setScaleY(0.75);
		NodePane.relocate(-965, -643);
		initialPanAmountX = -340;
		initialPanAmountY = -165;
		final Group group = new Group(imageView, canvas, NodePane);
		zoomPane = createZoomPane(group);

		// scrollContent.setTranslateX(-517);
		// scrollContent.setTranslateY(-236);

		root.getChildren().add(zoomPane);

		// *******MUST MOVE TO SEPARATE METHOD SO THAT IT CAN BE REATTACHED OVER
		// **********
		// ********TESTING NEW UI******************************
		// ********TOP BAR UI******************************
		menuPane.setPrefSize(490, 70);
		menuPane.setStyle("-fx-background-color: #ffffff;" + "-fx-border-radius: 15 2 15 2;"
				+ "-fx-background-radius: 15 2 15 2;"
				+ "-fx-effect: dropshadow(gaussian, black, 10, 0, 4, 4);");
		menuPane.setOpacity(1);
		menuPane.relocate(10, 10);

		File menuFile = new File("CS3733_Graphics/MenuGraphics/MenuButton.png");
		Image menuButtonImage = new Image(menuFile.toURI().toString());
		ImageView menuView = new ImageView();
		menuView.setImage(menuButtonImage);
		menuView.setFitWidth(50);
		menuView.setFitHeight(50);
		menuView.relocate(5, 5);
		menuView.setStyle(" -fx-border-radius: 10 10 10 0;" + "-fx-background-radius: 10 10 10 0;"
				+ "-fx-border-image-slice: 4 4 4 4 fill;  ");

		File pinFile = new File("CS3733_Graphics/MenuGraphics/greenUIpin.png");
		Image pinImage = new Image(pinFile.toURI().toString());
		ImageView pinView1 = new ImageView(pinImage);
		File pinFile2 = new File("CS3733_Graphics/MenuGraphics/redUIpin.png");
		Image pinImage2 = new Image(pinFile2.toURI().toString());
		ImageView pinView2 = new ImageView(pinImage2);//change to different pin images
		pinView1.setFitWidth(20);
		pinView1.setFitHeight(30);
		pinView1.relocate(220, 20);//
		pinView2.setFitWidth(20);
		pinView2.setFitHeight(30);
		pinView2.relocate(410, 20);//

		File findRouteFile = new File("CS3733_Graphics/MenuGraphics/findRouteButton.png");
		Image findRouteImage = new Image(findRouteFile.toURI().toString());
		ImageView findRouteView = new ImageView(findRouteImage);
		findRouteView.resize(15, 15);
		findRouteView.setFitWidth(40);
		findRouteView.setFitHeight(40);

		// *****

		VBox StartSearch = new VBox();
		VBox DestSearch = new VBox();

		StartText.setPromptText("Location");
		StartText.setStyle("-fx-background-color: CADETBLUE , white , white;"
				+ "-fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");

		DestText.setPromptText("Destination");
		DestText.setStyle("-fx-background-color: CADETBLUE , white , white;"
				+ "-fx-background-insets: 0 -1 -1 -1, 0 0 0 0, 0 -1 3 -1;");

		StartText.setPrefWidth(180);
		DestText.setPrefWidth(180);
		StartText.setMaxWidth(180);
		DestText.setMaxWidth(180);

		StartList.setMaxHeight(75);
		StartList.setMaxWidth(180);
		DestList.setMaxHeight(75);
		DestList.setMaxWidth(180);

		StartList.setItems(LocationOptions);
		DestList.setItems(LocationOptions);

		StartSearch.getChildren().addAll(StartText);
		DestSearch.getChildren().addAll(DestText);

		StartList.setOpacity(0);
		DestList.setOpacity(0);
		// *****

		VBox startTestBox = new VBox();
		startTestBox.getChildren().addAll(StartLabel, StartSearch);

		VBox endTestBox = new VBox();
		endTestBox.getChildren().addAll(DestLabel, DestSearch);

		// Hbox containing both start and end input fields
		HBox inputBox = new HBox(5);
		inputBox.getChildren().addAll(startTestBox, endTestBox, findRouteView);
		inputBox.relocate(70, 5);

		menuPane.getChildren().addAll(menuView, inputBox, pinView1, pinView2);

		root.getChildren().remove(menuPane);
		root.getChildren().addAll(menuPane);

		// ********************************************************
		// ********Description UI******************************
		//descriptionGroup
	    //descriptionPane
	    descriptionPane.setPrefSize(300, 100);
		descriptionPane.setStyle("-fx-background-color: #515151;");
		descriptionPane.relocate(1100, 0); //shift after off screen
		
		//set image preferences
		eventDescriptionImageView.setFitWidth(45);
		eventDescriptionImageView.setFitHeight(45);

		eventDescriptionImageView.relocate(2, 2);
		
		//set title preferences
		//eventTitle = new Label("Event Description");
		eventTitle.setTextFill(Color.WHITE);
		eventTitle.setFont(Font.font("manteka", 12));
		eventTitle.relocate(54, 5);
		
		//set location preferences
		eventLocation.setTextFill(Color.WHITE);
		eventLocation.setFont(Font.font("manteka", 12));
		eventLocation.relocate(54, 20);
		
		//set location preferences
		eventTime.setTextFill(Color.WHITE);
		eventTime.setFont(Font.font("manteka", 12));
		eventTime.relocate(54, 35);
		
		//set description label preferences
		eventDescriptionLabel.setTextFill(Color.WHITE);
		eventDescriptionLabel.setFont(Font.font("manteka", 12));
		eventDescriptionLabel.relocate(5, 50);
		
		//set description line 1 pref
		eventDescriptionLine1.setTextFill(Color.WHITE);
		eventDescriptionLine1.setFont(Font.font("manteka", 10));
		eventDescriptionLine1.relocate(5, 65);
		
		//set description line 2 pref
		eventDescriptionLine2.setTextFill(Color.WHITE);
		eventDescriptionLine2.setFont(Font.font("manteka", 10));
		eventDescriptionLine2.relocate(5, 80);
		
		//add description
		currentEvent.setDescription("");
		String descriptionText = currentEvent.getDescription();
		//descriptionText = truncate(descriptionText, 100);
		descriptionTextArea = new Label(descriptionText);
		descriptionTextArea.setPrefWidth(260); descriptionTextArea.setPrefHeight(80);
		descriptionTextArea.relocate(5, 25);
		descriptionTextArea.setTextFill(Color.WHITE);
		descriptionTextArea.setFont(Font.font("manteka", 10));
		
		descriptionPane.getChildren().addAll(eventDescriptionImageView, eventTitle, eventLocation, eventTime, eventDescriptionLabel, eventDescriptionLine1, eventDescriptionLine2, descriptionTextArea);
		
		root.getChildren().remove(descriptionPane);
		root.getChildren().addAll(descriptionPane);
		
		descriptionGroup.getChildren().addAll(descriptionPane);
		
		root.getChildren().addAll(descriptionGroup);


		
	    
		
		// ********SLIDE OUT MENU UI******************************
		fullMenuPane.setPrefSize(200, 750 + stageInitialHeightDifference);
		fullMenuPane.setStyle("-fx-background-color: #515151;");
		fullMenuPane.relocate(-200, 0);

		// Title and icon
		final Label menuTitle = new Label("Navigator");
		menuTitle.setTextFill(Color.WHITE);
		menuTitle.setFont(Font.font("manteka", 20));
		menuTitle.relocate(60, 10);
		File menuIconFile = new File("CS3733_Graphics/PI.png");
		Image menuImage = new Image(menuIconFile.toURI().toString());
		ImageView menuImageView = new ImageView(menuImage);
		menuImageView.setFitHeight(30);
		menuImageView.setFitWidth(30);
		menuImageView.relocate(25, 5);

		// ***Load map and route finding features Here *******
		// Create a Building selection drop down menu
		final VBox mapSelectionBoxV = new VBox(5);
		final Label mapSelectorLabel = new Label("Choose map");
		mapSelectorLabel.setTextFill(Color.WHITE);
		mapSelectorLabel.setFont(Font.font("manteka", 20));
		mapSelectorLabel.setTextFill(Color.WHITE);
		final Button LoadMapButton = new Button("Load");
		LoadMapButton.setId("dark");
		mapSelector.setId("dark");

		mapSelectionBoxV.relocate(12, 50);
		mapSelectionBoxV.setAlignment(Pos.CENTER);
		mapSelectionBoxV.getChildren().addAll(mapSelectorLabel, mapSelector, LoadMapButton);

		findNearestButton.setId("dark");
		nearestDropdown.setId("dark");
		nearestDropdown.setPrefWidth(170);
		// ***Find nearest function ****
		nearestDropdown.setValue("Dining");
		nearestBox.setAlignment(Pos.CENTER);
		nearestBox.getChildren().addAll(nearestDropdown, findNearestButton);
		nearestBox.relocate(12, 161);

		//**ATTACH THE GRAB EVENTS BUTTONS HERE******
		//DateLabel.setText("Date "+ cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DATE) + "/"+ cal.get(Calendar.YEAR));

		
		final Label googleEventsLabel = new Label("Display Events");
		googleEventsLabel.setTextFill(Color.WHITE);
		googleEventsLabel.setFont(Font.font("manteka", 18));
		googleEventsLabel.relocate(25, 425);
		
		//attach next and previous 7 day buttons
		final Button NextWeekButton = new Button("Next Week");
		NextWeekButton.setId("dark");
		NextWeekButton.relocate(100, 450);
				
		final Button PrevWeekButton = new Button("Prev Week");
		PrevWeekButton.setId("dark");
		PrevWeekButton.relocate(5, 450);
		
		//Display the current week dates
		//HBox currDatesBox = new HBox(5);
		int tempWeek;
		if(currWeek == 0)
			tempWeek = (currWeek+1)*7;
		else
			tempWeek = (currWeek)*7;
		//cal.add(field, amount);
		WeekLabel = new Label("Week: " +  cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DATE) +" - "+  cal.get(Calendar.MONTH) + "/" + (cal.get(Calendar.DATE) +tempWeek));
		WeekLabel.setTextFill(Color.WHITE);
		WeekLabel.setFont(Font.font("manteka", 12));
		WeekLabel.relocate(20, 485);
		
		
		//reset to today
		File resetToTodayFile = new File("CS3733_Graphics/MenuGraphics/setToToday.png");
		Image resetToTodayPic = new Image(resetToTodayFile.toURI().toString());
		ImageView resetToTodayButton = new ImageView(resetToTodayPic);
		resetToTodayButton.setFitHeight(40);
		resetToTodayButton.setFitWidth(40);
		resetToTodayButton.relocate(10, 515);
		
		//label the reset to today button
		final Label resetLabel = new Label("Current Week");
		resetLabel.setTextFill(Color.WHITE);
		resetLabel.setFont(Font.font("manteka", 12));
		resetLabel.relocate(60, 525);
		
		
		
		
		//Clear events button
		File clearEventsButtonFile = new File("CS3733_Graphics/MenuGraphics/clearEvents.png");
		Image clearEventsImage = new Image(clearEventsButtonFile.toURI().toString());
		ImageView clearEventsButton = new ImageView(clearEventsImage);
		clearEventsButton.setFitHeight(40);
		clearEventsButton.setFitWidth(40);
		clearEventsButton.relocate(10, 555);
		
		//label the reset to today button
		final Label clearEventsLabel = new Label("Clear events");
		clearEventsLabel.setTextFill(Color.WHITE);
		clearEventsLabel.setFont(Font.font("manteka", 12));
		clearEventsLabel.relocate(60, 565);
		
		
		//Sign in Button
		File googlebuttonFile = new File("CS3733_Graphics/MenuGraphics/googleSignInButton.png"); 
		Image googleButtonImage = new Image(googlebuttonFile.toURI().toString());
		ImageView googleButton = new ImageView(googleButtonImage);
		googleButton.setFitHeight(32);
		googleButton.setFitWidth(180);
		googleButton.relocate(10, 600 );
		//googleSignInButton
		
		
		//ATTACH THE DATE HERE***
		final Label DateLabel = new Label();
		DateLabel.setTextFill(Color.WHITE);
		DateLabel.setFont(Font.font("manteka", 15));
		DateLabel.relocate(5, 650);
		DateLabel.setText("Date "+ cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DATE) + "/"+ cal.get(Calendar.YEAR));
		//time label
		final Label timeLabel = new Label();
		timeLabel.setTextFill(Color.WHITE);
		timeLabel.setFont(Font.font("manteka", 15));
		timeLabel.relocate(5, 670);
		timeLabel.setText("Time "+cal.get(Calendar.HOUR) + "." + cal.get(Calendar.MINUTE));
		
		// **BUTTONS***
		// Create a keyimageButton to place the map key on screen
		File instructionsButtonFile = new File("CS3733_Graphics/MenuGraphics/showInstructionsButton.png"); 
		Image instructionsButtonImage = new Image(instructionsButtonFile.toURI().toString());
		ImageView instructionsButton = new ImageView(instructionsButtonImage);
		instructionsButton.setFitHeight(40);
		instructionsButton.setFitWidth(40);
		instructionsButton.relocate(60, 700 + stageInitialHeightDifference);

		// Create an about me button
		File aboutMeButtonFile = new File("CS3733_Graphics/MenuGraphics/aboutMeButton.png");
		Image aboutMeButtonImage = new Image(aboutMeButtonFile.toURI().toString());
		ImageView aboutMeButton = new ImageView(aboutMeButtonImage);
		aboutMeButton.setFitHeight(40);
		aboutMeButton.setFitWidth(40);
		aboutMeButton.relocate(110, 700 + stageInitialHeightDifference);
		// ***********

		//label the reset to today button
		final Label keyLabel = new Label("Color Key");
		keyLabel.setTextFill(Color.WHITE);
		keyLabel.setFont(Font.font("manteka", 18));
		keyLabel.relocate(45, 295);
		// Pop up key on map
		File keyFile = new File("CS3733_Graphics/MenuGraphics/key.png");
		Image keyImagePic = new Image(keyFile.toURI().toString());
		ImageView keyImage = new ImageView(keyImagePic);
		keyImage.setFitHeight(100); keyImage.setFitWidth(170); //RESIZE WHEN WE GET CORYS ISH
		keyImage.relocate(10, 320);

		// Attach things to this for in the side bar
		fullMenuPane.getChildren().addAll(menuImageView,WeekLabel, menuTitle,keyLabel, mapSelectionBoxV, nearestBox, DateLabel, timeLabel, instructionsButton, aboutMeButton,googleEventsLabel,keyImage, NextWeekButton, PrevWeekButton, resetToTodayButton, resetLabel, googleButton, clearEventsLabel, clearEventsButton);

		root.getChildren().remove(fullMenuPane);
		root.getChildren().addAll(fullMenuPane);

		menuGroup.getChildren().addAll(menuPane, fullMenuPane);

		root.getChildren().addAll(menuGroup);

		// ********About Me SLIDE OUT MENU UI******************************

		aboutPane.setPrefSize(575, 600);
		// aboutPane.setStyle("-fx-background-color: #515151;" );
		// aboutPane.setOpacity(.8);
		aboutPane.relocate(210, 770);
		// 210 +300, 770+300
		Rectangle backgroundrect = new Rectangle(0, 0, 600, 600);
		backgroundrect.setFill(Color.GREY);
		backgroundrect.setStroke(Color.BLACK);
		backgroundrect.setOpacity(.95);

		File aboutMeFile = new File("CS3733_Graphics/MenuGraphics/aboutMeImage.png");
		Image aboutmeimage = new Image(aboutMeFile.toURI().toString());
		ImageView aboutMeContent = new ImageView(aboutmeimage);
		// dirSliderButton.setFitHeight(30); dirSliderButton.setFitWidth(30);
		// aboutMeContent.relocate(230, 230);

		aboutPane.getChildren().addAll(backgroundrect, aboutMeContent);

		aboutGroup.getChildren().addAll(aboutPane);

		root.getChildren().addAll(aboutGroup);
		
		// ********Instruction SLIDE OUT MENU UI******************************

		instructionsPane.setPrefSize(600, 600);
		// aboutPane.setStyle("-fx-background-color: #515151;" );
		// aboutPane.setOpacity(.8);
		instructionsPane.relocate(210, 770);
		// 210 +300, 770+300
		//reuse backgroundrect from before

		File instructionMeFile = new File("CS3733_Graphics/MenuGraphics/instructionsImageContent.png");
		Image instructionFileImage = new Image(instructionMeFile.toURI().toString());
		ImageView instructionView = new ImageView(instructionFileImage);
		instructionView.setFitWidth(600); instructionView.setFitHeight(1656);
		
		//attach instructionView to scroll pane
		ScrollPane instructionScroll = new ScrollPane();
		instructionScroll.setPrefWidth(600);
		instructionScroll.setPrefHeight(600);
		instructionScroll.setContent(instructionView);
		//**MAY NEED TO SET PREF SIZES!!!

		//make another backbround rect so theres 2
		Rectangle backgroundrect2 = new Rectangle(0, 0, 600, 600);
		backgroundrect.setFill(Color.GREY);
		backgroundrect.setStroke(Color.BLACK);
		backgroundrect.setOpacity(.95);
		
		//insert the scroll pane to the instruction pane
		instructionsPane.getChildren().addAll(backgroundrect2, instructionScroll);
		
		//add the pane to the group
		instructionsGroup.getChildren().addAll(instructionsPane);

		root.getChildren().addAll(instructionsGroup);
		
		//instructionsAreOut

		// ********DIRECTIONS SLIDE OUT MENU UI******************************

		// EMAIL PANE***
		Pane emailPane = new Pane();
		emailPane.setStyle("-fx-background-color: #515151;");
		emailPane.setPrefSize(240, 50);
		emailPane.relocate(830 + stageInitialWidthDifference, 710 + stageInitialHeightDifference);

		// EmailButton.setTextFill(Color.BLACK);
		EmailInput.relocate(8, 5);
		EmailInput.setId("dark");
		EmailButton.relocate(180, 5);
		EmailButton.setFitHeight(29);
		EmailButton.setFitWidth(52);

		emailPane.getChildren().addAll(EmailInput, EmailButton);

		root.getChildren().remove(emailPane);
		root.getChildren().addAll(emailPane);

		emailGroup.getChildren().addAll(emailPane);
		root.getChildren().addAll(emailGroup);
		// ****EMAIL**

		directionsPane.setPrefSize(270, 350 + stageInitialHeightDifference);
		directionsPane.setStyle("-fx-background-color: #515151;");
		directionsPane.relocate(830, 710);

		// Attach direction slider button to the corner
		File dirSliderButtonFile = new File("CS3733_Graphics/MenuGraphics/directionSliderButton.png");
		Image dirSliderImage = new Image(dirSliderButtonFile.toURI().toString());
		ImageView dirSliderButton = new ImageView(dirSliderImage);
		dirSliderButton.setFitHeight(30);
		dirSliderButton.setFitWidth(30);
		dirSliderButton.setRotate(90);
		dirSliderButton.relocate(230, 5);

		// create Label for directions
		directionsTitle.setTextFill(Color.WHITE);
		directionsTitle.setFont(Font.font("manteka", 15));
		directionsTitle.relocate(5, 10);

		// email button
		File menuEmailButtonFile = new File("CS3733_Graphics/MenuGraphics/emailButton.png");
		Image menuEmailButtonImage = new Image(menuEmailButtonFile.toURI().toString());
		ImageView menuEmailButton = new ImageView(menuEmailButtonImage);
		menuEmailButton.setFitHeight(30);
		menuEmailButton.setFitWidth(30);
		menuEmailButton.relocate(110, 5);

		// Next button (and previous)
		NextInstruction.relocate(190, 5);
		NextInstruction.setFitHeight(30);
		NextInstruction.setFitWidth(30);
		PrevInstruction.relocate(150, 5);
		PrevInstruction.setFitHeight(30);
		PrevInstruction.setFitWidth(30);

		directionsPane.getChildren().addAll(directionsTitle, menuEmailButton, PrevInstruction, NextInstruction,
				dirSliderButton);

		root.getChildren().remove(directionsPane);
		root.getChildren().addAll(directionsPane);

		s1.setPrefSize(270, 310);
		s1.relocate(830, 750 + stageInitialHeightDifference);

		directionsGroup.getChildren().addAll(directionsPane, s1);

		root.getChildren().addAll(directionsGroup);

		// ******************ACTIONS MENUS******************************
		menuView.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// apply animation
				if (!menuIsOut) {
					menuAnimation(menuGroup);
					// menuView.setRotate(90);
					menuIsOut = true;
				} else {
					menuAnimation(menuGroup);
					// menuView.setRotate(0);
					menuIsOut = false;
				}
			}
		});
		dirSliderButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// apply animation
				if (!directionsAreOut) {
					menuDirectionsAnimation(directionsGroup);
					dirSliderButton.setRotate(-90);
					// System.out.println("X: " + directionsGroup.getLayoutX() +
					// ", Y: " + directionsGroup.getLayoutY());
					directionsAreOut = true;
				} else {
					menuDirectionsAnimation(directionsGroup);
					dirSliderButton.setRotate(90);
					directionsAreOut = false;
				}
			}
		});
		menuEmailButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (!menuEmailIsOut) {
					emailPaneAnimation(emailGroup);
					menuEmailIsOut = true;
				} else {
					emailPaneAnimation(emailGroup);
					menuEmailIsOut = false;
				}
			}
		});
		findRouteView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                findAndDisplayRoute(imageView);
            }
        });
		aboutMeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (!aboutMeIsOut) {
					aboutMeAnimation(aboutGroup);
					aboutMeIsOut = true;
				} else {
					aboutMeAnimation(aboutGroup);
					aboutMeIsOut = false;
				}
			}
		});
		//Sign into google button
		googleButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				//clear events 
				clearEventIcons(imageView); //must be before you clear the list otherwise you lost the reference to it!!
				
				//Grab the events from google calendars
				try { 
					myEventsData = CalendarEvents.getEvents(currWeek); //grab first 7 days worth of events
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getEventsLocal();
				fixUI();
			}
		});
		
		//next week and prev week button actions and resettotoday
		//currWeek
		NextWeekButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				//change to next weeks events
				currWeek += 7;
				//clear events 
				clearEventIcons(imageView); //must be before you clear the list otherwise you lost the reference to it!!
				//Grab the events from google calendars
				try { 
					myEventsData = CalendarEvents.getEvents(currWeek); //grab first 7 days worth of events
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getEventsLocal();
				fixUI();
			}
		});
		PrevWeekButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				//change to next weeks events
				currWeek -= 7;
				//clear events 
				clearEventIcons(imageView); //must be before you clear the list otherwise you lost the reference to it!!
				//Grab the events from google calendars
				try { 
					myEventsData = CalendarEvents.getEvents(currWeek); //grab first 7 days worth of events
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getEventsLocal();
				fixUI();
			}
		});
		resetToTodayButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				//change to next weeks events
				currWeek = 0;
				//clear events 
				clearEventIcons(imageView); //must be before you clear the list otherwise you lost the reference to it!!
				//Grab the events from google calendars
				try { 
					myEventsData = CalendarEvents.getEvents(currWeek); //grab first 7 days worth of events
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getEventsLocal();
				fixUI();
			}
		});
		clearEventsButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				//change to next weeks events
				//currWeek = 0;
				//clear events 
				clearEventIcons(imageView); //must be before you clear the list otherwise you lost the reference to it!!
				
			}
		});
		
		//****** keyImage is teh name of the image of the key, nmake buttona nd attach it

		instructionsButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (!instructionsAreOut) {
					//USE SAME METHOD BECAUSE SAME SIZE
					instructionsAnimation(instructionsGroup);
					instructionsAreOut = true;
				} else {
					instructionsAnimation(instructionsGroup);
					instructionsAreOut = false;
				}

			}
		});
		
		/* KEY METHOD
		 * instructionsButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (toggle) {
					root.getChildren().add(keyImage);
					toggle = false;
				} else {
					root.getChildren().remove(keyImage);
					toggle = true;
				}

			}
		});
		 */
		// ********************************************************

		primaryStage.setScene(scene);
		primaryStage.show();

		findRouteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				findAndDisplayRoute(imageView);
			}
		});

		// Find Nearest Button
		findNearestButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				findNearestDisplayRoute(imageView);
			}
		});

		// Add actions to the Load Map button
		LoadMapButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				loadMap(root, imageView);
				// root.getChildren().remove(PrevInstruction);
				// root.getChildren().remove(NextInstruction);
				fixUI();
			}
		});

		Tooltip loadMapTooltip = new Tooltip();
		loadMapTooltip.setText("Loads the currently selected map");
		LoadMapButton.setTooltip(loadMapTooltip);


		// Next instruction button actions
		NextInstruction.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (currRoute >= 0 && currRoute < currMaps - 1) {
					System.out.println("currRoute: " + currRoute + "currMaps: " + currMaps);
					currRoute++;
					changeInstructions(NodePane, root, imageView);
					fixUI();
				}
			}
		});

		PrevInstruction.setRotate(180);
		// Next instruction button actions
		PrevInstruction.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				if (currRoute > 0 && currRoute <= currMaps) {
					currRoute--;
					changeInstructions(NodePane, root, imageView);
					fixUI();
				}
			}
	    });

        
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            
    		@Override
    		public void changed(ObservableValue<? extends Number> observable,
    				Number oldValue, Number newValue) {
    			stageInitialWidthDifference = scene.getWidth() - 1100;
				scrollPane.setPrefViewportWidth(1100 + stageInitialWidthDifference);
				toggleKeyText.setTranslateX(stageInitialWidthDifference);
				s1.setTranslateX(stageInitialWidthDifference);
				ChangeFloorButtonGroup.setTranslateX(stageInitialWidthDifference);
				keyText.setTranslateX(stageInitialWidthDifference);
				directionsPane.setTranslateX(stageInitialWidthDifference);
				emailPane.setTranslateX(stageInitialWidthDifference);
				//aboutPane.setTranslateX(stageInitialWidthDifference/2);
				scrollContent.setTranslateX(initialPanAmountX + stageInitialWidthDifference / 2);
				descriptionGroup.setTranslateX(stageInitialWidthDifference);
    			
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
    	        stageInitialHeightDifference = scene.getHeight()-750;
    			scrollPane.setPrefViewportHeight(750 + stageInitialHeightDifference);
    			
    			
    			toggleKeyText.setTranslateY(stageInitialHeightDifference);
				keyText.setTranslateY(stageInitialHeightDifference);
				s1.setTranslateY(stageInitialHeightDifference);
				fullMenuPane.setPrefSize(200, 750 + stageInitialHeightDifference);
				BuildingNameLabel.setTranslateY(stageInitialHeightDifference);
				aboutMeButton.setTranslateY(stageInitialHeightDifference);
				instructionsButton.setTranslateY(stageInitialHeightDifference);
				emailPane.setTranslateY(stageInitialHeightDifference);
				directionsPane.setTranslateY(stageInitialHeightDifference);
				aboutPane.setTranslateY(stageInitialHeightDifference/2);
				scrollContent.setTranslateY(initialPanAmountY + stageInitialHeightDifference / 2);
				instructionsPane.setTranslateY(stageInitialHeightDifference/2);
				
    		}
    	});

        StartList.setCellFactory(new Callback<ListView<Node>, ListCell<Node>>() {
            @Override
            public ListCell<Node> call(ListView<Node> param) {
                ListCell cell = new ListCell<Node>() {
                    @Override
                    protected void updateItem(Node node, boolean empty) {
                        super.updateItem(node, empty);
                        if (empty) {
                            setText("");
                        } else {
                            setText(node.getName());
                        }
                    }
                };
                return cell;
            }
        });

        DestList.setCellFactory(new Callback<ListView<Node>, ListCell<Node>>() {
            @Override
            public ListCell<Node> call(ListView<Node> param) {
                ListCell cell = new ListCell<Node>() {
                    @Override
                    protected void updateItem(Node node, boolean empty) {
                        super.updateItem(node, empty);
                        if (empty) {
                            setText("");
                        } else {
                            setText(node.getName());
                        }
                    }
                };
                return cell;
            }
        });

        StartText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                StartText.selectAll();
            }
        });

        DestText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                DestText.selectAll();
            }
        });

        DestText.textProperty().addListener(
            new ChangeListener<Object>() {
                public void changed(ObservableValue<?> observable2,
                                    Object oldVal2, Object newVal2) {
                    handleSearchByKeyDest((String)oldVal2, (String)newVal2);
                }
            });

        StartText.textProperty().addListener(
                new ChangeListener<Object>() {
                    public void changed(ObservableValue<?> observable,
                                        Object oldVal, Object newVal) {
                        handleSearchByKeyStart((String)oldVal, (String)newVal);
                    }
                });

        DestText.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (DestText.isFocused()) {
                    DestSearch.getChildren().remove(DestList);
                    DestSearch.getChildren().add(DestList);
                    if (StartSearch.getChildren().contains(StartList)){
                        StartSearch.getChildren().remove(StartList);
                    }
                }
                else {
//                    DestSearch.getChildren().remove(DestList);
                }
            }
        });

        DestText.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.DOWN){
                    DestList.requestFocus();
                }
                if (event.getCode() == KeyCode.ENTER){
                    //TODO check to see if actual node was selected in both textfields
                        findAndDisplayRoute(imageView);
                }
            }
        });
        StartText.focusedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                if (StartText.isFocused()) {
                    StartSearch.getChildren().remove(StartList);
                    StartSearch.getChildren().add(StartList);
                    if (DestSearch.getChildren().contains(DestList)){
                        DestSearch.getChildren().remove(DestList);
                    }
                }
                else {
//                    StartSearch.getChildren().remove(StartList);
                }
            }
        });
        StartText.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.DOWN){
                    StartList.requestFocus();
                }
            }
        });

        StartList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent arg0) {
                StartText.setText(StartList.getSelectionModel().getSelectedItem().getName());
                StartSearch.getChildren().remove(StartList);
                DestText.requestFocus();
                start = true;
            }
        });
        StartList.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER){
                    StartText.setText(StartList.getFocusModel().getFocusedItem().getName());
                    StartSearch.getChildren().remove(StartList);
                    DestText.requestFocus();
                    start = true;
                }
            }
        });
        DestList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent arg0) {
                DestText.setText(DestList.getSelectionModel().getSelectedItem().getName());
                 DestSearch.getChildren().remove(DestList);
                DestList.requestFocus();
                end = true;
            }
        });
        DestList.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER){
                    DestText.setText(DestList.getFocusModel().getFocusedItem().getName());
                    DestSearch.getChildren().remove(DestList);
                    DestText.requestFocus();
                }
                end = true;
            }
        });

            
        root.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if (event.getCode() == KeyCode.ESCAPE) {
                    loadMap(root, imageView);
                    startBool = false;
                    destBool = false;
                    startButtonBool = false;
                    destButtonBool = false;
                    StartText.setText("");
                    DestText.setText("");
                    StartText.setPromptText("Start");
                    DestText.setPromptText("Destination");
                    start = false;
                    end = false;
                    StartList.setOpacity(0);
                    DestList.setOpacity(0);
                    keyText.setText("");
                    directionBox.getChildren().clear();
                    if(directionsAreOut) {
                        menuDirectionsAnimation(directionsGroup);
                        dirSliderButton.setRotate(90);
                        directionsAreOut = false;
                    }

                }

                if (event.getCode() == KeyCode.S && event.isShortcutDown()){
                    StartText.requestFocus();
                }
                if (event.getCode() == KeyCode.D && event.isShortcutDown()){
                    DestText.requestFocus();
                }
                if (event.getCode() == KeyCode.E && event.isShortcutDown()){
                    EmailInput.requestFocus();
                }
            }
        });
        
        
        fixUI();

    }
	


	///END OF MAIN ***************************************************************

	public static void getEventsLocal(){
	//*** GRAB THE EVENTS AND PARSE THROUGH THEM TO the list ***
			//Evaulte which keyword is most prominent in the description and determine event Type
			int awardCount = 0, foodCount = 0, movieCount = 0, WPIEventCount = 0, sportCount = 0;
			String tempType = "WPIEvent";
			
			String tempDescription = new String(); //parse info from description
			Building tempBuilding = new Building("");
			
			//MyEvent tempEvent = new MyEvent(); //fill in with information as we get it
			System.out.println("myEventsData.size() :  " + myEventsData.size());
			for(int i = 0; i < myEventsData.size();i++){
				MyEvent tempEvent = new MyEvent(); //fill in with information as we get it

				//**** ALSO ADD ERROR CHECKING IN HERE WHEN YOU OBTAIN EACH BIT OF 
				//INFORMATION IS A VALID BIT OF INFO********
				
				//CHECK IF THIS FIRST TO KNOW IF WE WANT TO SKIP EVENTS WITH BAD LOCATIONS
				//Find the location
				//Parse through location information, first match to a place, use that as the location
				tempBuilding =  determineLocation(myEventsData.get(i).getLocation());
				if(tempBuilding == NullBuilding){
					continue; //skip adding this building because unreadable location
				}
				tempEvent.setLocation(tempBuilding.getName());
				
				
				//Get Title of event 
				tempEvent.setSummary(myEventsData.get(i).getSummary());
				
				//Get description of event 
				tempEvent.setDescription(myEventsData.get(i).getDescription());
				
				//Get location of event 
				//MIGHT NEED TO DO CUSTOM LOCATION DEPENDING ON WHERE THE OFFSET IS SET TO
				//SINCE IT MIGHT BE ON A DIFFERENT CORNER THAN THE BOTTOM RIGHT HAND SIDE
				//** IF 2 or more EVENTS HAVE THE SAME LOCATION MOVE ONE OVER
				//iterate through to count how many events are already at that location,
				//relocate them based on how many there are
				int numEvents = 0;
				for(int n = 0; n < myEvents.size(); n++){
					if(myEvents.get(n).getLocation().equals(tempEvent.getLocation())){
						numEvents++;
					}
				}
				//*numEvents MUST ADDD
				tempEvent.setlocationX(tempBuilding.getMaps().get(0).getGlobalToLocalOffsetX()+(numEvents*37));
				tempEvent.setlocationY(tempBuilding.getMaps().get(0).getGlobalToLocalOffsetY());
				//ADD CUSTOM OFFSETS TO PLACE IN CENTER OF BUILINGS
				
				// CC ^40 >30
				if(tempEvent.getLocation().equals("Campus Center")){
					tempEvent.setlocationX(30+tempBuilding.getMaps().get(0).getGlobalToLocalOffsetX()+(numEvents*37));
					tempEvent.setlocationY(-40+tempBuilding.getMaps().get(0).getGlobalToLocalOffsetY());
				}
				//FL down90 >30
				if(tempEvent.getLocation().equals("Fuller Labs")){
					tempEvent.setlocationX(30+tempBuilding.getMaps().get(0).getGlobalToLocalOffsetX()+(numEvents*37));
					tempEvent.setlocationY(+90+tempBuilding.getMaps().get(0).getGlobalToLocalOffsetY());
				}
				//AK ^50 <40
				if(tempEvent.getLocation().equals("Atwater Kent")){
					tempEvent.setlocationX(-40+tempBuilding.getMaps().get(0).getGlobalToLocalOffsetX()+(numEvents*37));
					tempEvent.setlocationY(-50+tempBuilding.getMaps().get(0).getGlobalToLocalOffsetY());
				}
				//SL ^30 >30
				if(tempEvent.getLocation().equals("Salisbury Labs")){
					tempEvent.setlocationX(+tempBuilding.getMaps().get(0).getGlobalToLocalOffsetX()+(numEvents*37));
					tempEvent.setlocationY(-50+tempBuilding.getMaps().get(0).getGlobalToLocalOffsetY());
				}
				//SH ^60 >10
				if(tempEvent.getLocation().equals("Stratton Hall")){
					tempEvent.setlocationX(10+tempBuilding.getMaps().get(0).getGlobalToLocalOffsetX()+(numEvents*37));
					tempEvent.setlocationY(-60+tempBuilding.getMaps().get(0).getGlobalToLocalOffsetY());
				}
				//RC X829 Y787 (rec center, hard code this building)
				if(tempEvent.getLocation().equals("Rec Center")){
					tempEvent.setlocationX(829+(numEvents*37));
					tempEvent.setlocationY(787);
				}
				//HA down50 >50 (harrington, )
				if(tempEvent.getLocation().equals("Harrington Auditorium")){
					tempEvent.setlocationX(50+tempBuilding.getMaps().get(0).getGlobalToLocalOffsetX()+(numEvents*37));
					tempEvent.setlocationY(40+tempBuilding.getMaps().get(0).getGlobalToLocalOffsetY());
				}
				//AH ^90 >30
				if(tempEvent.getLocation().equals("Alden Hall")){
					tempEvent.setlocationX(20+tempBuilding.getMaps().get(0).getGlobalToLocalOffsetX()+(numEvents*37));
					tempEvent.setlocationY(-90+tempBuilding.getMaps().get(0).getGlobalToLocalOffsetY());
				}
				//PC down10 <40
				if(tempEvent.getLocation().equals("Project Center")){
					tempEvent.setlocationX(-40+tempBuilding.getMaps().get(0).getGlobalToLocalOffsetX()+(numEvents*37));
					tempEvent.setlocationY(10+tempBuilding.getMaps().get(0).getGlobalToLocalOffsetY());
				}
				
				//Get the start and end time of the event
				//WAITING FOR THE YANG TO GET THIS STUFF!!!!!
				//NOT VITAL BUT WOULD BE NICE...****
				tempEvent.setStartTime(myEventsData.get(i).getStartTime());
				tempEvent.setEndTime( myEventsData.get(i).getEndTime());
				
				
				
				//Determine the Type of the event based on the information in description
				//cut white space out and cast all lowercase for easier search
				tempDescription = myEventsData.get(i).getDescription();
				tempDescription = tempDescription.replaceAll("\\s+","");
				tempDescription = tempDescription.toLowerCase();
				System.out.println(tempDescription); //works time to parse
				//parse through and search for key words
				for(int f = 0; f < WPIWords.size();f ++){
					if(tempDescription.contains(WPIWords.get(f)))
						WPIEventCount++;
				}
				for(int f = 0; f < FoodWords.size();f ++){
					if(tempDescription.contains(FoodWords.get(f)))
						foodCount++;
				}
				for(int f = 0; f < SportWords.size();f ++){
					if(tempDescription.contains(SportWords.get(f)))
						sportCount++;
				}
				for(int f = 0; f < AwardWords.size();f ++){
					if(tempDescription.contains(AwardWords.get(f)))
						awardCount++;
				}
				for(int f = 0; f < MovieWords.size();f ++){
					if(tempDescription.contains(MovieWords.get(f)))
						movieCount++;
				}
				
				//Add the rest of the parsers once we get this working!!!!!
				//Depending on which count is the highest at the point, choose the event type
				tempType  = determineEventType(awardCount, foodCount, movieCount, WPIEventCount , sportCount);
				tempEvent.setType(tempType);
				
				//depending on the type, set the image icon as well
				File eventIconFile = new File("CS3733_Graphics/EventImages/"+ tempType +".png");
				//File eventIconFile = new File("CS3733_Graphics/DirectionImages/10.png");

				Image EventIconImagePic = new Image(eventIconFile.toURI().toString());
				ImageView EventIconImage = new ImageView(EventIconImagePic);
				EventIconImage.setFitWidth(35); EventIconImage.setFitHeight(35);
				tempEvent.setIcon(EventIconImage);
				//add the tool tips to the event icons
				Tooltip tempToolTip = new Tooltip(tempEvent.getSummary());
				Tooltip.install(EventIconImage, tempToolTip);
				
				//Add delays if time!!
				EventIconImage.setOnMouseEntered(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						currentEvent = tempEvent;
						mouseOnEvent = true;
						
						//get the image of the event
						//use the image, not image view that we obtained right above
						eventDescriptionImageView.resize(35, 35);
						eventDescriptionImageView.setImage(EventIconImagePic); 
						
						//event title
						//see if we should add the ...
						boolean addTitleElip = false;
						if(tempEvent.getSummary().length()>= 30)
							addTitleElip = true;
						if(addTitleElip)
							eventTitle.setText(truncate(tempEvent.getSummary(), 30) + "...");
						else
							eventTitle.setText(truncate(tempEvent.getSummary(), 30));
						
						//event location
						eventLocation.setText("Location: " + truncate(tempEvent.getLocation(), 30));
						
						//event time
						eventTime.setText("Time: " + tempEvent.getStartTime());

						try{
							//eventTime.setText("Time: " + truncate(tempEvent.getStartTime(), 30) + " - "+ truncate(tempEvent.getEndTime(), 30));
						} catch (NullPointerException e){
							System.out.println("Not a good time");
						}
						//event description
						//check to see how long the description is
						int numLines = 0;
						if(tempEvent.getDescription().length()>= 50)
							numLines = 2;//only display first 2 lines of description
						else 
							numLines = 1;
						
						//set lines of descriptions
						if(numLines == 2){
							eventDescriptionLine1.setText(truncate(tempEvent.getDescription(), 50));
							eventDescriptionLine2.setText(tempEvent.getDescription().substring(50, 100));
						}
						else{
							eventDescriptionLine1.setText(truncate(tempEvent.getDescription(), 50));
							eventDescriptionLine2.setText("");
						}
							
							
						//eventDescription.setText(truncate(tempEvent.getSummary(), 100));
						//String tempText = truncate(tempEvent.getDescription(), 100);
						//descriptionTextArea.setText(tempText);
						
						if (!descriptionIsOut) {
							descriptionPaneAnimation(descriptionGroup);
							descriptionIsOut = true;
						} else {
							descriptionPaneAnimation(descriptionGroup);
							descriptionIsOut = false;
						}
					}
				});
				EventIconImage.setOnMouseExited(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						currentEvent = tempEvent;
						mouseOnEvent = true;
						
						if (!descriptionIsOut) {
							descriptionPaneAnimation(descriptionGroup);
							descriptionIsOut = true;
						} else {
							descriptionPaneAnimation(descriptionGroup);
							descriptionIsOut = false;
						}
					}
				});
				//reset the counts!!!!
				 awardCount = 0; foodCount = 0; movieCount = 0; WPIEventCount = 0; sportCount = 0;

				myEvents.add(tempEvent); //myEvents will contain all of the stuff we need for UI, BOO YA!
			}
			System.out.println("myEvents.size() :  " + myEvents.size());
			
			//MOVE IMAGE ATTACHING TO THE END
	}
	
	
	public static String truncate(String value, int length) {
		// Ensure String length is longer than requested size.
		if (value.length() > length) {
		    return value.substring(0, length);
		} else {
		    return value;
		}
	}
	
	private static Building determineLocation(String location) {
		
		//iterate through the list of locations to match the location to set the location
		//split up location description
		try {
			String[] parts = location.toLowerCase().split(" ");

			// Filter out the entries that don't contain the entered text
			ObservableList<Building> subentries = FXCollections.observableArrayList();
			for (Building entry : buildings) {
				boolean match = false;
				String entryText = entry.getName() + entry.getMaps().get(0).getInitials();
				entryText = entryText.toLowerCase();
				for (String part : parts) {
					// The entry needs to contain all portions of the
					// search string *but* in any order
					if (entryText.toLowerCase().contains(part)) {
						match = true;
						break;
					}
				}

				if (match) {
					return entry; //return entire building to later get info from it
				}

			}

			//default location, type check in main, if nullBuilding, dont add this event to the list
			return NullBuilding;
		} catch(NullPointerException ignored){

		}
		return NullBuilding;
	}



	private static String determineEventType(int awardCount, int foodCount, int movieCount, int WPIEventCount, int sportCount) {
		//give WPI events more weight, or whatever, depends on order
		//POSSIBLY delte the = sign to give better image recognition to add weight
		 //if(movieCount > 0) //movie words are pretty unique
			//return "Movie";
		System.out.println("awardCount: " + awardCount + ", foodCount: "+ foodCount + " movieCount: " + movieCount +" WPIEventCount: " + WPIEventCount +" sportCount: " + sportCount);

		
		if(WPIEventCount > awardCount && WPIEventCount > foodCount && WPIEventCount > movieCount && WPIEventCount > sportCount)
			return "WPIEvent";
		else if(foodCount > awardCount  && foodCount > movieCount && foodCount > sportCount)
			return "Food";
		else if(sportCount > awardCount  && sportCount > movieCount)
			return "Sport";
		else if(awardCount > movieCount)
			return "Award";
		else if(movieCount > 0)
			return "Movie";
		
		return "WPIEvent"; //Default event type
	}
	
	//remove the event icons from the screen
	public void clearEventIcons(ImageView imageView){
		WeekLabel.setText("Date "+ cal.get(Calendar.MONTH) + "/" + cal.get(Calendar.DATE) + "/"+ cal.get(Calendar.YEAR));
		NodePane.getChildren().clear(); //clear everything then redraw
		myEventsData.clear();
		myEvents.clear();
		System.out.println("Size when empty: "+ myEventsData.size());
		drawNodes(nodeList, NodePane, root, StartText, DestText, imageView);
	}
	
	//Bring the UI to the front of the screen
	public void fixUI(){
		System.out.println("Size when full: "+ myEventsData.size());
		//put the event images onto of the map
		//ONly do this on the campus map
		//System.out.println("ROLLED OVER*****   "+mapSelector.getValue().getName());
		if(mapSelector.getValue().getName().equals("Campus Map")){
			for(int i = 0; i < myEvents.size();i++){
				System.out.println("*************************");
				System.out.println("Title: " + myEvents.get(i).getSummary()); //works time to parse
				System.out.println("Type: " + myEvents.get(i).getType());
				//System.out.println("Desc: " + myEvents.get(i).getDescription());
				System.out.println("Location: " + myEvents.get(i).getLocation());
				System.out.println("Time: " + myEvents.get(i).getStartTime());
				System.out.println("X: " + myEvents.get(i).getlocationX());
				System.out.println("Y: " + myEvents.get(i).getlocationY());
				
				ImageView eventIcon = myEvents.get(i).getIcon();
				eventIcon.relocate(myEvents.get(i).getlocationX(), myEvents.get(i).getlocationY());
				NodePane.getChildren().remove(myEvents.get(i).getIcon()); //incase we already attached it
				NodePane.getChildren().addAll(myEvents.get(i).getIcon());
				myEvents.get(i).getIcon().toFront();
			}
		}
		aboutGroup.toFront();
		instructionsGroup.toFront();
		emailGroup.toFront();
		directionsGroup.toFront();
		menuGroup.toFront();
		
	}
		


	// Menu Animations
	private void menuAnimation(Group menuGroup) {
		Path g1path = new Path();
		MoveTo g1moveTo = new MoveTo();
		if (!menuIsOut)
			g1moveTo.setX(150);
		else
			g1moveTo.setX(355);
		g1moveTo.setY(375 + stageInitialHeightDifference / 2);
		LineTo g1lineTo = new LineTo();
		if (!menuIsOut)
			g1lineTo.setX(355);
		else
			g1lineTo.setX(150);
		g1lineTo.setY(375 + stageInitialHeightDifference / 2);
		g1path.getElements().add(g1moveTo);
		g1path.getElements().add(g1lineTo);
		PathTransition g1pt = new PathTransition();
		g1pt.setDuration(Duration.millis(500));
		g1pt.setPath(g1path);
		g1pt.setNode(menuGroup);
		g1pt.setAutoReverse(true);
		g1pt.play();
	}
	
	private static void descriptionPaneAnimation(Group descriptionGroup) {
		Path g1path = new Path();
		MoveTo g1moveTo = new MoveTo();

		if (!descriptionIsOut)
			g1moveTo.setX(1100 + 150);
		else 
			g1moveTo.setX(800 + 150);
		
		g1moveTo.setY(0 + 50);

		LineTo g1lineTo = new LineTo();
		if (!descriptionIsOut)
			g1lineTo.setX(800 + 150);
		else
			g1lineTo.setX(1100 + 150);
		
		g1lineTo.setY(0 + 50);
		

		g1path.getElements().add(g1moveTo);
		g1path.getElements().add(g1lineTo);
		PathTransition g1pt = new PathTransition();
		g1pt.setDuration(Duration.millis(500));
		g1pt.setPath(g1path);
		g1pt.setNode(descriptionGroup);
		g1pt.setAutoReverse(true);
		g1pt.play();
	}
	//instructionsAreOut
	//1656 Height
	private void instructionsAnimation(Group instructionsGroup) {
		Path g1path = new Path();
		MoveTo g1moveTo = new MoveTo();

		g1moveTo.setX(210 + 300);

		if (!instructionsAreOut)
			g1moveTo.setY(770 + 300 + stageInitialHeightDifference / 2);
		else
			g1moveTo.setY(200 + 300 + stageInitialHeightDifference / 2);

		LineTo g1lineTo = new LineTo();
		g1lineTo.setX(210 + 300);

		if (!instructionsAreOut)
			g1lineTo.setY(200 + 300 + stageInitialHeightDifference / 2);
		else
			g1lineTo.setY(770 + 300 + stageInitialHeightDifference / 2);

		g1path.getElements().add(g1moveTo);
		g1path.getElements().add(g1lineTo);
		PathTransition g1pt = new PathTransition();
		g1pt.setDuration(Duration.millis(500));
		g1pt.setPath(g1path);
		g1pt.setNode(instructionsGroup);
		g1pt.setAutoReverse(true);
		g1pt.play();
	}
	
	private void aboutMeAnimation(Group aboutGroup) {
		Path g1path = new Path();
		MoveTo g1moveTo = new MoveTo();

		g1moveTo.setX(210 + 300);

		if (!aboutMeIsOut)
			g1moveTo.setY(770 + 300 + stageInitialHeightDifference / 2);
		else
			g1moveTo.setY(200 + 300 + stageInitialHeightDifference / 2);

		LineTo g1lineTo = new LineTo();
		g1lineTo.setX(210 + 300);

		if (!aboutMeIsOut)
			g1lineTo.setY(200 + 300 + stageInitialHeightDifference / 2);
		else
			g1lineTo.setY(770 + 300 + stageInitialHeightDifference / 2);

		g1path.getElements().add(g1moveTo);
		g1path.getElements().add(g1lineTo);
		PathTransition g1pt = new PathTransition();
		g1pt.setDuration(Duration.millis(500));
		g1pt.setPath(g1path);
		g1pt.setNode(aboutGroup);
		g1pt.setAutoReverse(true);
		g1pt.play();
	}

	// Menu Direction Animations
	// Direction relocate (830, 400); //up
	// Direction scroll pane = 270x 310
	private void menuDirectionsAnimation(Group directionsGroup) {
		Path g1path = new Path();
		MoveTo g1moveTo = new MoveTo();

		g1moveTo.setX(830 + 135 + stageInitialWidthDifference);
		if (!directionsAreOut)
			g1moveTo.setY(750 + (135 + stageInitialHeightDifference));
		else
			g1moveTo.setY(750 - 175 + stageInitialHeightDifference);
		LineTo g1lineTo = new LineTo();

		g1lineTo.setX(830 + 135 + stageInitialWidthDifference);
		if (!directionsAreOut)
			g1lineTo.setY(750 - 175 + stageInitialHeightDifference);
		else
			g1lineTo.setY(750 + (135 + stageInitialHeightDifference));

		g1path.getElements().add(g1moveTo);
		g1path.getElements().add(g1lineTo);
		PathTransition g1pt = new PathTransition();
		g1pt.setDuration(Duration.millis(500));
		g1pt.setPath(g1path);
		g1pt.setNode(directionsGroup);
		g1pt.setAutoReverse(true);
		g1pt.play();
	}

	
	//email 
	private void emailPaneAnimation(Group emailGroup){
		 Path g1path = new Path();
		 MoveTo g1moveTo = new MoveTo();
		 
		 if(!menuEmailIsOut)
			 g1moveTo.setX(830 + 120 + stageInitialWidthDifference);
		 else
			 g1moveTo.setX(590 + 120+ stageInitialWidthDifference);
		 g1moveTo.setY(710 +  25+stageInitialHeightDifference);
		 
		 LineTo g1lineTo = new LineTo();
		 if(!menuEmailIsOut)
			 g1lineTo.setX(590 + 120+ stageInitialWidthDifference);
		 else
			 g1lineTo.setX(830 + 120+  stageInitialWidthDifference);
		 g1lineTo.setY(710 + 25+ stageInitialHeightDifference);
		 
		 
		 g1path.getElements().add(g1moveTo);
		 g1path.getElements().add(g1lineTo);
		 PathTransition g1pt = new PathTransition();
		 g1pt.setDuration(Duration.millis(500));
		 g1pt.setPath(g1path);
		 g1pt.setNode(emailGroup);
		 g1pt.setAutoReverse(true);
		 g1pt.play();
		 System.out.println("emailGroup X: " + emailGroup.getLayoutX() + ", Y: "+ emailGroup.getLayoutY());
   }
	
	
	// Display all of the instructions on screen
	private void displayInstructions(LinkedList<Node> route, Pane directionsPane) {

		directionBox.getChildren().clear();

		// s1.setStyle("-fx-background-color: #515151");
		s1.setHbarPolicy(ScrollBarPolicy.NEVER);
		if (route.size() > 2) {
			stepIndicator steps = new stepIndicator(route);

			LinkedList<Step> directions = steps.lInstructions();
			if (currRoute == 0)
				directions
						.addFirst(new Step(0, "Walk Straight", 0, directions.get(0).getX(), directions.get(0).getY()));
			if (currRoute == currMaps - 1)
				directions.add(new Step(99, "You've arrived", 0, directions.get(0).getX(), directions.get(0).getY()));

			// iterate through the list of instructions and create labels for
			// each one and attach to the root
			for (int i = 0; i < directions.size(); i++) {
				HBox StepBox = new HBox(2);
				// happen every other instructions
				if (i % 2 != 1)
					StepBox.setStyle("-fx-background-color: #ffffff");
				else
					StepBox.setStyle("-fx-background-color: #f1f1f1");
				// StepBox.setStyle("-fx-border-color: black;");
				Label newDirection;

				// if(directions.get(i).getDistance() == 0.0) {
                if(!(directions.get(i).getIconID() == 1)
                        &&!(directions.get(i).getIconID() == 2)
                        &&!(directions.get(i).getIconID() == 5)
                        &&!(directions.get(i).getIconID() == 10)) {
                    newDirection = new Label(directions.get(i).getMessage());
                } else {
                    newDirection = new Label(directions.get(i).getBaseMessage());
                }
                newDirection.setFont(Font.font("Menlo"));
				// } else {
				// newDirection = new Label(directions.get(i).getMessage() + "
				// and go for " + round(directions.get(i).getDistance(), 1) + "
				// ft");
				// }

				File arrowFile = new File("CS3733_Graphics/DirectionImages/" + directions.get(i).getIconID() + ".png");
				Image arrowImage = new Image(arrowFile.toURI().toString());
				ImageView arrowView = new ImageView();
				arrowView.setImage(arrowImage);

				String style = StepBox.getStyle();

				int currentInstruction = i;
				StepBox.setOnMouseMoved(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						if (currentInstruction >= 0) {
							StepBox.setStyle("-fx-effect: innershadow(gaussian, #039ed3, 10, 1.0, 0, 0);");
							// highlight the current path
							int NodeX = directions.get(currentInstruction).getX();
							int NodeY = directions.get(currentInstruction).getY();
							// attach an image, or do an animation on this
							// current node
							File PinFile = new File("CS3733_Graphics/pin.png");
							Image pinImage = new Image(PinFile.toURI().toString());

							pinView.setImage(pinImage);
							pinView.setLayoutX(NodeX - 10);
							pinView.setLayoutY(NodeY - 36);
							if (!pinAttached) {
								// System.out.print.println(NodeX + " "+ NodeY);
								NodePane.getChildren().add(pinView);
								pinAttached = true;
							}
						}

					}
				});
				StepBox.setOnMouseExited(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						StepBox.setStyle(style);
						NodePane.getChildren().remove(pinView);
						pinAttached = false;
					}
				});

				StepBox.getChildren().addAll(arrowView, newDirection);
				directionBox.getChildren().addAll(StepBox);
			}

			// directionsPane.getChildren().remove(s1);
			s1.setContent(directionBox);
			// directionsPane.getChildren().add(s1);
		}

		// convert the route to a list of string instructions

		return;

	}

	public void changeInstructions(Pane NodePane, Pane root, ImageView imageView) {

		displayInstructions(multiMap.get(currRoute), root);

		Map initials = null;
		for (int i = 0; i < maps.size(); i++) {
			//// System.out.print.println("CURRENT ROUTE: "+ currRoute);
			//// System.out.print.println("multiMap.get(currRouteE: "+
			//// multiMap.get(currRoute).get(0).getFloorMap());
			if (maps.get(i).getName().equals(multiMap.get(currRoute).get(0).getFloorMap()))
				initials = maps.get(i);
		}
		gc.clearRect(0, 0, 8000, 6000);
		mapSelector.setValue(initials);
		loadMap(root, imageView);

		drawNodes(nodeList, NodePane, root, StartText, DestText, imageView);
		drawRoute(gc, multiMap.get(currRoute));
		
		
		
		
		
		
		
		if (currRoute != 0) {
			NodePane.getChildren().add(enter);
			enter.setLayoutX(multiMap.get(currRoute).getFirst().getX());
			enter.setLayoutY(multiMap.get(currRoute).getFirst().getY());

			NodePane.getChildren().add(exit);
			exit.setLayoutX(multiMap.get(currRoute).get(multiMap.get(currRoute).size() - 2).getX());
			exit.setLayoutY(multiMap.get(currRoute).get(multiMap.get(currRoute).size() - 2).getY());

		} else {
			NodePane.getChildren().add(greenPinView);
			greenPinView.setLayoutX(multiMap.getFirst().getFirst().getX() - 18);
			greenPinView.setLayoutY(multiMap.getFirst().getFirst().getY() - 55);
		}

		// Determine which buttons to display when changing instructions
		if (currRoute > 0) {
			// root.getChildren().remove(PrevInstruction);
			// root.getChildren().add(PrevInstruction);
		} else if (currRoute == 0) {
			// root.getChildren().remove(PrevInstruction);
		}

		// if we are on the last page of instructions, remove next button
		if (currRoute >= currMaps - 1) {
			NodePane.getChildren().remove(exit);

			NodePane.getChildren().add(redPinView);
			redPinView.setLayoutX(multiMap.getLast().getLast().getX() - 18);
			redPinView.setLayoutY(multiMap.getLast().getLast().getY() - 55);

			// root.getChildren().remove(NextInstruction);
		} else {
			// root.getChildren().remove(NextInstruction);
			// root.getChildren().add(NextInstruction);
		}
		// root.getChildren().add(s1);
		
		
		fixUI();
	}

	private void getMapSelector(Building building, Pane root, ImageView imageView) {

		// If the user moved out of the building dont display the map
		/*
		 * if(BuildingRolledOver == null) return;
		 */

		if (NodePane.getChildren().contains(LayerGroup)) {
			NodePane.getChildren().remove(LayerGroup);
			LayerGroup.getChildren().clear();
		}

		gc.clearRect(0, 0, 8000, 6000);
		// drawNodes(nodeList, NodePane, root, StartText, DestText, imageView);

		// ****** ATTACHING MAPS OVER, DELETING ON MOUSE OVER *****
		// MOve this effect to global so we dont make a new one every time

		// Attach Building label
		BuildingNameLabel.setText(building.getName());
		BuildingNameLabel.setTextFill(Color.BLACK);
		BuildingNameLabel.setFont(Font.font("manteka", 20));
		buildingLabelShadow.setOffsetY(3.0f);
		buildingLabelShadow.setColor(Color.GRAY);
		BuildingNameLabel.setEffect(buildingLabelShadow);
		NodePane.getChildren().remove(BuildingNameLabel);
		NodePane.getChildren().add(BuildingNameLabel);

		// Load the layered Maps
		int currentFloor = 0;
		root.getChildren().remove(LayerGroup);
		LayerGroup.getChildren().clear();

		for (int i = 1; i <= building.getNumMaps(); i++) {
			currentFloor = i - 1;
			// System.out.print.println("CS3733_Graphics/LayerMap/"+building.getMaps().get(currentFloor).getInitials()
			// + building.getMaps().get(currentFloor).getFloor() + "L.png");
			File mapFile = new File("CS3733_Graphics/LayerMap/" + building.getMaps().get(currentFloor).getInitials()
					+ building.getMaps().get(currentFloor).getFloor() + "L.png");// Change
																					// back
																					// to
																					// above
			Image image = new Image(mapFile.toURI().toString());
			ImageView mapImageView = new ImageView();
			mapImageView.setImage(image);

			// calculuate the rotational constant and offsets
			double rc = (180 * BuildingRolledOver.getMaps().get(0).getRotationalConstant()) / Math.PI;
			double xplacement = BuildingRolledOver.getMaps().get(0).getGlobalToLocalOffsetX();
			double yplacement = BuildingRolledOver.getMaps().get(0).getGlobalToLocalOffsetY();

			Group g1 = new Group();
			// Fuller has a special transformation
			if (building.equals(FullerLabs))
				g1.setEffect(ptFuller);
			else if (building.equals(WestStreet))
				g1.setEffect(ptWest);
			else
				g1.setEffect(pt);

			g1.setOpacity(.3);
			g1.getChildren().add(mapImageView);

			// TODO
			// ATTAH BUILDING to the layeredGroup so that when you roll out you
			// can still be on the builing
			// CUSTOM OFFSETS AHHHH DAMNIT
			// Align with he lower right hand corner of the building
			if (BuildingRolledOver.getMaps().get(0).getBuildingName().equals("Campus Center")) {
				g1.setLayoutX(xplacement + 340);
				g1.setLayoutY(yplacement + 310 - i * 45);
			}
			if (BuildingRolledOver.getMaps().get(0).getBuildingName().equals("Higgins House")) {
				g1.setLayoutX(xplacement + 370);
				g1.setLayoutY(yplacement + 470 - i * 45);
			}
			// TODO
			/// ****** WHAT IS THAT ACTUAL NAME OF THIS?????***
			if (BuildingRolledOver.getMaps().get(0).getBuildingName().equals("Higgins House Garage")) {
				g1.setLayoutX(xplacement + 270);
				g1.setLayoutY(yplacement + 200 - i * 45);
			}
			if (BuildingRolledOver.getMaps().get(0).getBuildingName().equals("Stratton Hall")) {
				g1.setLayoutX(xplacement + 290);
				g1.setLayoutY(yplacement + 170 - i * 45);
			}
			if (BuildingRolledOver.getMaps().get(0).getBuildingName().equals("Boynton Hall")) {
				g1.setLayoutX(xplacement + 280);
				g1.setLayoutY(yplacement + 180 - i * 45);
			}
			if (BuildingRolledOver.getMaps().get(0).getBuildingName().equals("Gordon Library")) {
				g1.setLayoutX(xplacement + 225);
				g1.setLayoutY(yplacement + 365 - i * 45);
			}
			if (BuildingRolledOver.getMaps().get(0).getBuildingName().equals("Project Center")) {
				g1.setLayoutX(xplacement + 430);
				g1.setLayoutY(yplacement + 272 - i * 45);
			}
			// TODO
			// AWKWARD BC OF SHAPE ?? TOP LEVEL NOT HIGHLIGHTING!!!! - is it not
			// being attached? CHECK NUMBER OF FLOORS
			if (BuildingRolledOver.getMaps().get(0).getBuildingName().equals("Atwater Kent")) {
				// g1.setRotate(180);
				g1.setLayoutX(xplacement + 280);
				g1.setLayoutY(yplacement + 230 - i * 30);
			}
			// TODO FILL IN ONCE WE GET THE FULLER LAB INFO!!!!!!!!
			if (BuildingRolledOver.getMaps().get(0).getBuildingName().equals("Fuller Labs")) {
				g1.setLayoutX(xplacement + 340);
				g1.setLayoutY(yplacement + 560 - i * 45);
			}
			if (BuildingRolledOver.getMaps().get(0).getBuildingName().equals("Salisbury Labs")) {
				g1.setLayoutX(xplacement + 400);
				g1.setLayoutY(yplacement + 350 - i * 45);
			}
			if (BuildingRolledOver.getMaps().get(0).getBuildingName().equals("Washburn Shops")) {
				g1.setLayoutX(xplacement + 350);
				g1.setLayoutY(yplacement + 260 - i * 45);
			}
			if (BuildingRolledOver.getMaps().get(0).getBuildingName().equals("157 West Street")) {
				g1.setLayoutX(xplacement + 260);
				g1.setLayoutY(yplacement + 350 - i * 45);
			}
			if (BuildingRolledOver.getMaps().get(0).getBuildingName().equals("Harrington Auditorium")) {
				g1.setLayoutX(xplacement + 380);
				g1.setLayoutY(yplacement + 370 - i * 45);
			}
			if (BuildingRolledOver.getMaps().get(0).getBuildingName().equals("Higgins Labs")) {
				g1.setLayoutX(xplacement + 380);
				g1.setLayoutY(yplacement + 330 - i * 45);
			}
			if (BuildingRolledOver.getMaps().get(0).getBuildingName().equals("Alden Hall")) {
				g1.setLayoutX(xplacement + 360);
				g1.setLayoutY(yplacement + 330 - i * 45);
			}
			/// ABOVE^^^^^^

			applyAnimation(g1, i, imageView);

			// fade in the group of layered maps
			shortFadeIn(LayerGroup);
			LayerGroup.getChildren().add(g1);

			// used inside action scope
			int floor = currentFloor;
			// Add actions to each of the layered map buttons
			g1.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					// System.out.print.println(floor);

					if (event.isStillSincePress()) {
						root.getChildren().remove(LayerGroup);
						LayerGroup.getChildren().clear();
						if (NodePane.getChildren().contains(BuildingNameLabel))
							NodePane.getChildren().remove(BuildingNameLabel);
						screenFadeBack(imageView);
						BuildingNameLabel.setText(building.getName() + " " + building.getMaps().get(floor).getFloor());
						mapSelector.setValue(building.getMaps().get(floor));
						loadMap(root, imageView);
					}

				}
			});
			g1.setOnMouseExited(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					BuildingNameLabel.setText(building.getName());
					if (building.equals(FullerLabs))
						g1.setEffect(ptFuller);
					else if (building.equals(WestStreet))
						g1.setEffect(ptWest);
					else
						g1.setEffect(pt);
					shortFadeOut(g1);
				}
			});
			g1.setOnMouseMoved(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					BuildingNameLabel.relocate(g1.getLayoutX() - 700, mouseYposition + 150);
					BuildingNameLabel.setText(building.getName() + " " + building.getMaps().get(floor).getFloor());
					
					if (building.equals(WestStreet)) {
						BuildingNameLabel.relocate(g1.getLayoutX() - 500, mouseYposition + 250);
					}
					if (building.equals(Harrington)) {
						BuildingNameLabel.relocate(g1.getLayoutX() - 800, mouseYposition + 150);
					}
					if (building.equals(AldenHall)) {
						BuildingNameLabel.relocate(g1.getLayoutX() - 700, mouseYposition + 200);
					}

					if (building.equals(FullerLabs))
						g1.setEffect(shadowFuller);
					else if (building.equals(WestStreet))
						g1.setEffect(shadowWest);
					else
						g1.setEffect(shadow);
				}
			});

			// Fade out the rest of the maps **START OTHER ALPHA AT LOW**
			g1.setOnMouseEntered(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent event) {
					BuildingNameLabel.relocate(g1.getLayoutX() - 700, mouseYposition + 150);
					if (building.equals(WestStreet)) {
						BuildingNameLabel.relocate(g1.getLayoutX() - 500, mouseYposition + 250);
					}
					BuildingNameLabel.setText(building.getName() + " " + building.getMaps().get(floor).getFloor());
					System.out.println("X: " + g1.getLayoutX() + ", Y: " + g1.getLayoutY());
					if (building.equals(FullerLabs))
						g1.setEffect(shadowFuller);
					else if (building.equals(WestStreet))
						g1.setEffect(shadowWest);
					else
						g1.setEffect(shadow);
					shortFadeIn(g1);
				}
			});
		}
		if (!NodePane.getChildren().contains(LayerGroup))
			NodePane.getChildren().add(LayerGroup);

		// add actions to when you leave the layered group, remove it
		LayerGroup.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				BuildingNameLabel.setText("..");
				if (NodePane.getChildren().contains(BuildingNameLabel))
					NodePane.getChildren().remove(BuildingNameLabel);
				screenFadeBack(imageView);
				/// Change to fade out of the layered group
				fadeOutLayeredGroup(LayerGroup);
			}

			private void fadeOutLayeredGroup(Group layerGroup) {
				FadeTransition blur = new FadeTransition();
				blur.setDuration(Duration.millis(500));
				blur.setFromValue(.3);
				blur.setToValue(0);
				blur.setNode(layerGroup);
				blur.play();
				
				//remove the layered group from stage when we roll out after it fades out
				blur.setOnFinished(new EventHandler<ActionEvent>(){
			            @Override
			            public void handle(ActionEvent arg0) {
			            	NodePane.getChildren().remove(LayerGroup);
			     			LayerGroup.getChildren().clear();
			            }
			    });
			}
     	});

	}

	private void shortFadeOut(Group g1) {
		FadeTransition blur = new FadeTransition();
		blur.setDuration(Duration.millis(300));
		blur.setFromValue(1);
		blur.setToValue(.3);
		blur.setNode(g1);
		blur.play();
	}

	// Fade in for a group
	private void shortFadeIn(Group g1) {
		FadeTransition blur = new FadeTransition();
		blur.setDuration(Duration.millis(300));
		blur.setFromValue(.3);
		blur.setToValue(1);
		blur.setNode(g1);
		blur.play();
	}

	// restore map alpha back to 100%
	private void screenFadeBack(ImageView imageView) {
		// Fade the alpha
		FadeTransition blur = new FadeTransition();
		blur.setDuration(Duration.millis(800));
		blur.setFromValue(.3);
		blur.setToValue(1.0);
		blur.setNode(imageView);
		blur.play();
	}

	private void applyAnimation(Group g1, int i, ImageView imageView) {
		// Fade the alpha
		FadeTransition blur = new FadeTransition();
		blur.setDuration(Duration.millis(500));
		blur.setFromValue(1.0);
		blur.setToValue(.3);
		blur.setNode(imageView);
		blur.play();

		// where to attach the maps
		// **** CHANGING THESE VARS BELOW (ADDING STUFF)*****
		// double xplacement =
		// BuildingRolledOver.getMaps().get(0).getGlobalToLocalOffsetX();
		// double yplacement =
		// BuildingRolledOver.getMaps().get(0).getGlobalToLocalOffsetY();

		// FLOOR 1
		Path g1path = new Path();
		MoveTo g1moveTo = new MoveTo();
		g1moveTo.setX(0);
		g1moveTo.setY(0);
		LineTo g1lineTo = new LineTo();
		g1lineTo.setX(0);// -i*15
		g1lineTo.setY(-i * 14);
		g1path.getElements().add(g1moveTo);
		g1path.getElements().add(g1lineTo);

		PathTransition g1pt = new PathTransition();
		g1pt.setDuration(Duration.millis(500));
		g1pt.setPath(g1path);
		g1pt.setNode(g1);
		g1pt.setOrientation(PathTransition.OrientationType.NONE);
		// g1pt.setCycleCount(Timeline.INDEFINITE); //for levitation, but nahh
		g1pt.setAutoReverse(true);
		g1pt.play();
	}

	// Custom ones for each building, move pt from global to local again
	private PerspectiveTransform setCorners(PerspectiveTransform pt) {
		pt.setUlx(0);// upper left
		pt.setUly(0);
		pt.setUrx(270);// upper right
		pt.setUry(0);
		pt.setLrx(270);// Lower right
		pt.setLry(120);
		pt.setLlx(0);// lower left
		pt.setLly(120);
		return pt;
	}

	// Custom Fuller transform
	private PerspectiveTransform setCornersFuller(PerspectiveTransform pt) {
		pt.setUlx(0);// upper left
		pt.setUly(0);
		pt.setUrx(200);// upper right
		pt.setUry(0);
		pt.setLrx(200);// Lower right
		pt.setLry(200);
		pt.setLlx(0);// lower left
		pt.setLly(200);
		return pt;
	}
	
	// Custom Fuller transform
		private PerspectiveTransform setCornersWest(PerspectiveTransform pt) {
			pt.setUlx(0);// upper left
			pt.setUly(0);
			pt.setUrx(120);// upper right
			pt.setUry(0);
			pt.setLrx(120);// Lower right
			pt.setLry(240);
			pt.setLlx(0);// lower left
			pt.setLly(240);
			return pt;
		}

	// If the route is across multiple maps we want to break it up
	private LinkedList<LinkedList<Node>> splitRoute(LinkedList<Node> route) {
		// System.out.print.println("routeSize = " + route.size());
		// change this.. or check if it before splitorator
		if (route.size() == 0) {
			LinkedList<LinkedList<Node>> splitRoutes = new LinkedList<LinkedList<Node>>();
			return splitRoutes;
		}
		;

		LinkedList<LinkedList<Node>> splitRoutes = new LinkedList<LinkedList<Node>>();
		String aBuilding = route.get(0).getFloorMap();
		int newBuildingIndex = 0;
		// System.out.print.println("aBuilding = " + aBuilding);

		for (int i = 0; i < route.size(); i++) {
			// System.out.print.println("Route.get(i)" +
			// route.get(i).getFloorMap());
			// if the current node is in a different building, chop off the
			// stuff before and place it in its own list
			if (!aBuilding.equals(route.get(i).getFloorMap()) || i == route.size() - 1) {
				// System.out.print.println("Switched Floors");
				// add from newBuildingIndex to i-1
				LinkedList<Node> tempRoute = new LinkedList<Node>();
				for (int k = newBuildingIndex; k <= i; k++) {
					tempRoute.add(route.get(k));
				}
				newBuildingIndex = i;
				aBuilding = route.get(i).getFloorMap(); // since i is the new
														// building
				splitRoutes.add(tempRoute);
				// System.out.print.println("splitRoutesize =
				// "+splitRoutes.size());
			}
			// if we haven't added any yet, route is all on 1 map

		}
		if (splitRoutes.size() == 0) {
			splitRoutes.add(route);
		}
		currMaps = splitRoutes.size();
		return splitRoutes;
	}

	private void createGlobalGraph() {
		// create Global nodes and edges list to pass to other createGraph
		// method

		LinkedList<EdgeDataConversion> globalEdgeListConversion = new LinkedList<EdgeDataConversion>();

		// Manually add all of the Nodes...
		File nodeFolder = new File("Graphs/Nodes");
		for (File file : nodeFolder.listFiles()) {
			if (file.getName().endsWith(".json")) {
				globalNodeList.addAll(JsonParser.getJsonContent("Graphs/Nodes/" + file.getName()));
			}
		}

		globalGraph.setNodes(globalNodeList);

		File edgeFolder = new File("Graphs/Edges");
		for (File file : edgeFolder.listFiles()) {
			if (file.getName().endsWith(".json")) {
				globalEdgeListConversion.addAll(JsonParser.getJsonContentEdge("Graphs/Edges/" + file.getName()));
			}
		}

		// Create HashMap of every node in the globalNodeList
		globalNodeHashMap = new HashMap<>();
		for (Node n : globalNodeList) {
			globalNodeHashMap.put(n.getName(), n);
		}

		// Create edges and add them to the graph
		for (EdgeDataConversion edc : globalEdgeListConversion) {
			Node fromNode = globalNodeHashMap.get(edc.getFrom());
			Node toNode = globalNodeHashMap.get(edc.getTo());
			if (fromNode == null || toNode == null) {
				continue;
			}
			globalGraph.addEdge(fromNode, toNode);
		}
	}

    private void drawNodes(LinkedList<Node> nodes, Pane NodePane, Pane root, TextField startText, TextField destText, ImageView imageView){
    	int i;

    	for(i = 0; i < nodes.size(); i ++){
    		if(nodes.get(i).getIsPlace()){
        		Button newNodeButton = new Button("");
        		newNodeButton.setStyle(
                        "-fx-background-radius: 5em; " +
                        "-fx-min-width: " + 15*buttonRescale + "px; " +
                        "-fx-min-height: " + 15*buttonRescale + "px; " +
                        "-fx-max-width: " + 15*buttonRescale + "px; " +
                        "-fx-max-height: " + 15*buttonRescale + "px;"
                );
        		newNodeButton.setId("glass-grey");
            	newNodeButton.relocate(nodes.get(i).getX()-7*buttonRescale, nodes.get(i).getY()-7*buttonRescale);
            	Node newNode = nodes.get(i);
            	newNodeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                    	if (!start){
                    		if(startButton != null && endButton != startButton)startButton.setId(null);
                            newNodeButton.setId("shiny-orange");
                            startButton = newNodeButton;
                    		if(newNode.getIsPlace()) startNode = newNode.getName();
                    		startText.setText(startNode);
                    		start = true;
                    		startButtonBool = true;
                    	}
                    	else if(!end){
                    		if(endButton != null && endButton != startButton)endButton.setId(null);
                            newNodeButton.setId("shiny-orange");
                            endButton = newNodeButton;
                    		if(newNode.getIsPlace()) endNode = newNode.getName();
                    		DestText.setText(endNode);
                    		start = false;
                    		end = false;
                    		destButtonBool = true;
                    	}
                    	
                    	if(startButtonBool && destButtonBool) {
                            destButtonBool = false;
                            startButtonBool = false;
                    		directionBox.getChildren().clear();
                            gc.clearRect(0, 0, 8000, 6000); // Clears old path
                            root.getChildren().remove(zoomPane);
                            keyText.setText("");

                            // Need to string compare from
                            Node startPlace = new Node(0, 0, 0, "", "", "", false, false, "");
                            Node endPlace = new Node(0, 0, 0, "", "", "", false, false, "");
                            for (int i = 0; i < globalGraph.getNodes().size(); i++) {
                                if ((globalGraph.getNodes().get(i)).getName().equals(StartText.getText())) {
                                    startPlace = (globalGraph.getNodes().get(i));
                                }
                                if ((globalGraph.getNodes().get(i)).getName().equals(DestText.getText())) {
                                    endPlace = (globalGraph.getNodes().get(i));
                                }
                            }


                            route = new LinkedList<Node>();
                            route = globalGraph.findRoute(startPlace, endPlace);
                            savedRoute = route;
							keyText.setText(" ");
                            
                            if(!(startPlace.equals(endPlace))) {
                            	try {
                                    //System.out.print.println("Route length: " + route.size());
                                    //Display the directions on the side
                                    //System.out.print.println("Route = " + route);
                                    //if(!(route.size() <= 1)){
                                    multiMap = splitRoute(route);//is endlessly looping or suttin
                                    currRoute = 0;
                                    
                                    //if the entire route is only on 1 map, display all instruction at once
                                    displayInstructions(multiMap.get(currRoute), root);
                                    
                                    
                                    Map initials = null;
                                    //System.out.print.println("MAPSIZE: " + maps.size());
                                    for (int i = 0; i < maps.size(); i++) {
                                        //System.out.print.println("MAP!!!!!: " + maps.get(i).getName());
                                        //System.out.print.println("CURRENT ROUTE: " + currRoute);
                                        //System.out.print.println("multiMap.get(currRouteE: " + multiMap.get(currRoute).get(0).getFloorMap());
                                        if (maps.get(i).getName().equals(multiMap.get(currRoute).get(0).getFloorMap()))
                                            initials = maps.get(i);
                                    }

                                    //System.out.print.println("MAP!!!!!: " + multiMap.get(currRoute).get(0).getFloorMap());
                                    gc.clearRect(0, 0, 6000, 3000);

                                    mapSelector.setValue(initials);
                                    //System.out.print.println("initials = " + initials);
                                    nodeList = JsonParser.getJsonContent(initials.getNodesPath());

                                    loadMap(root, imageView);
                                    //if(multiMap.get(currRoute).size() > 2) root.getChildren().add(s1);
                                    root.getChildren().remove(zoomPane);
                                    
                                    //System.out.print.println("NodeList Size = " + nodeList.size());
                                    drawRoute(gc, multiMap.get(currRoute));

									NodePane.getChildren().add(greenPinView);
									greenPinView.setLayoutX(multiMap.getFirst().getFirst().getX() - 18);
									greenPinView.setLayoutY(multiMap.getFirst().getFirst().getY() - 55);

									if (multiMap.size() == 1) {

										NodePane.getChildren().add(redPinView);
										redPinView.setLayoutX(multiMap.getFirst().getLast().getX() - 18);
										redPinView.setLayoutY(multiMap.getFirst().getLast().getY() - 55);

									}

									final Group group = new Group(imageView, canvas, NodePane);

                                    zoomPane = createZoomPane(group);
                                    root.getChildren().add(zoomPane);
                                    route = new LinkedList<Node>();
                                    if(!directionsAreOut){
                                        menuDirectionsAnimation(directionsGroup);
                                        dirSliderButton.setRotate(-90);
                                        //System.out.println("X: " + directionsGroup.getLayoutX() + ", Y: " + directionsGroup.getLayoutY());
                                        directionsAreOut = true;
                                    }
                                    
                                    
                                    
                                    
                                    
                                    
                                } catch (NullPointerException n){
                                    keyText.setText("No Path Found");
                            		keyText.setFill(Color.RED);
                            		loadMap(root, imageView);
                                }
                            } else {
                            loadMap(root, imageView);
                            keyText.setFont(Font.font ("manteka", 14));
                            keyText.setFill(Color.RED);
                            keyText.setText("Your Start and Destination are the same");
                            }
                    	}
                    	fixUI();
    				} 
                });
                newNodeButton.setTooltip(new Tooltip(newNode.getName()));
            	NodePane.getChildren().add(newNodeButton);
    		} else if(!nodes.get(i).getIsPlace()){
    			//Do nothing
    		}

		}
		fixUI();
	}

	private void drawRoute(GraphicsContext gc, LinkedList<Node> route) {
		NodePane.getChildren().remove(goatView);

		Color customBlue = Color.web("0x00b3fd");
		gc.setLineCap(StrokeLineCap.ROUND);
		Path path = new Path();
		path.getElements().add(new MoveTo(route.getFirst().getX(), route.getFirst().getY()));

    	//iterate through the route drawing a connection between nodes
    	for(int i = 1; i < route.size(); i ++){
    		if((!route.get(i-1).getType().equals("Transition Point")&&!route.get(i-1).getType().equals("Staircase")&&!route.get(i-1).getType().equals("Elevator"))
    				||(!route.get(i).getType().equals("Transition Point")&&!route.get(i).getType().equals("Staircase")&&!route.get(i).getType().equals("Elevator"))){
    			gc.setLineWidth(5*buttonRescale);
                gc.setStroke(Color.BLACK);
    	  		gc.strokeLine(route.get(i-1).getX(), route.get(i-1).getY(), route.get(i).getX(),route.get(i).getY());
    		}
    		
    	}
    	for(int i = 1; i < route.size(); i ++){
    		if((!route.get(i-1).getType().equals("Transition Point")&&!route.get(i-1).getType().equals("Staircase")&&!route.get(i-1).getType().equals("Elevator"))
    				||(!route.get(i).getType().equals("Transition Point")&&!route.get(i).getType().equals("Staircase")&&!route.get(i).getType().equals("Elevator"))){
    			gc.setLineWidth(3*buttonRescale);
                gc.setStroke(customBlue);
    	  		gc.strokeLine(route.get(i - 1).getX(), route.get(i - 1).getY(), route.get(i).getX(), route.get(i).getY());
				path.getElements().add(new LineTo( route.get(i).getX(), route.get(i).getY()));
    		}
    	}

		// add line follower

		fixUI();
		NodePane.getChildren().add(goatView);
		PathTransition pathTransition = new PathTransition();
		pathTransition.setDuration(Duration.millis(600 * route.size()));
		pathTransition.setPath(path);
		pathTransition.setNode(goatView);
		pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
		pathTransition.setCycleCount(Timeline.INDEFINITE);
		pathTransition.play();
		
		fixUI();
	}

	private LinkedList<Edge> convertEdgeData(LinkedList<EdgeDataConversion> edgeData) {
		LinkedList<Edge> edgeList = new LinkedList<Edge>();
		int from = 0, to = 0;

		// iterate through the edges
		for (int i = 0; i < edgeData.size(); i++) {
			//// System.out.print.println("Edge Iterator: " + i);
			// iterate throught he nodelist to find the matching node
			for (int j = 0; j < globalNodeList.size(); j++) {
				//// System.out.print.println("NodeSize: "+nodeList.size());
				//// System.out.print.println("Node: "+nodeList.get(j)+", i:
				//// "+i+" , j: "+j);
				if (edgeData.get(i).getFrom().equals((globalNodeList.get(j)).getName())) {
					from = j;
				}
				if (edgeData.get(i).getTo().equals((globalNodeList.get(j)).getName())) {
					to = j;
				}

			}
			Edge newEdge = new Edge(globalGraph.getNodes().get(from), globalGraph.getNodes().get(to),
					edgeData.get(i).getDistance());
			edgeList.add(newEdge);
		}

		return edgeList;
	}

	public void handleSearchByKeyStart(String oldVal, String newVal) {
		// If the number of characters in the text box is less than last time
		// it must be because the user pressed delete
		if (oldVal != null && (newVal.length() < oldVal.length())) {
			// Restore the lists original set of entries
			// and start from the beginning
			StartList.setItems(LocationOptions);
		}
		StartList.setOpacity(100);

		// Break out all of the parts of the search text
		// by splitting on white space
		String[] parts = newVal.toUpperCase().split(" ");

		// Filter out the entries that don't contain the entered text
		ObservableList<Node> subentries = FXCollections.observableArrayList();
		for (Node entry : StartList.getItems()) {
			boolean match = true;
			String entryText = entry.getName();
			for (String part : parts) {
				// The entry needs to contain all portions of the
				// search string *but* in any order
				entryText = entryText + entry.getBuilding() + entry.getFloorMap() + entry.getType();
				if (!entryText.toUpperCase().contains(part)) {
					match = false;
					break;
				}
			}

			if (match) {
				subentries.add(entry);
			}
			if (subentries.size() * 25 < 75)
				StartList.setMaxHeight(subentries.size() * 25);
			else
				StartList.setMaxHeight(75);
		}
		StartList.setItems(subentries);

		if (subentries.isEmpty() || subentries.get(0).equals(StartText.getText()))
			StartList.setOpacity(0);

	}

	class DragContext {

		double mouseAnchorX;
		double mouseAnchorY;

		double translateAnchorX;
		double translateAnchorY;

	}

	public void handleSearchByKeyDest(String oldVal, String newVal) {
		// If the number of characters in the text box is less than last time
		// it must be because the user pressed delete
		if (oldVal != null && (newVal.length() < oldVal.length())) {
			// Restore the lists original set of entries
			// and start from the beginning
			DestList.setItems(LocationOptions);
		}
		DestList.setOpacity(100);

		// Break out all of the parts of the search text
		// by splitting on white space
		String[] parts = newVal.toUpperCase().split(" ");

		// Filter out the entries that don't contain the entered text
		ObservableList<Node> subentries = FXCollections.observableArrayList();
		for (Node entry : DestList.getItems()) {
			boolean match = true;
			String entryText = entry.getName();
			for (String part : parts) {
				// The entry needs to contain all portions of the
				// search string *but* in any order
				entryText = entryText + entry.getBuilding() + entry.getFloorMap() + entry.getType();
				if (!entryText.toUpperCase().contains(part)) {
					match = false;
					break;
				}
			}

			if (match) {
				subentries.add(entry);
			}
			if (subentries.size() * 25 < 75)
				DestList.setMaxHeight(subentries.size() * 25);
			else
				DestList.setMaxHeight(75);
		}
		DestList.setItems(subentries);

		if (subentries.isEmpty() || subentries.get(0).equals(DestText.getText()))
			DestList.setOpacity(0);

	}

	private Parent createZoomPane(final Group group) {
		final StackPane zoomPane = new StackPane();
		scrollPane = new ScrollPane();
		scrollPane.setPrefViewportWidth(1100 + stageInitialWidthDifference);
		scrollPane.setPrefViewportHeight(750 + stageInitialHeightDifference);

		zoomPane.getChildren().add(group);

		scrollContent = new Group(zoomPane);
		scrollPane.setContent(scrollContent);

		scrollContent.setTranslateX(initialPanAmountX);
		scrollContent.setTranslateY(initialPanAmountY);

		// Removes Scroll bars
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);

		scrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
			@Override
			public void changed(ObservableValue<? extends Bounds> observable, Bounds oldValue, Bounds newValue) {
				scrollPane.setPrefViewportWidth(1100 + stageInitialWidthDifference);
				scrollPane.setPrefViewportHeight(750 + stageInitialHeightDifference);
				zoomPane.setMinSize(newValue.getWidth(), newValue.getHeight());

			}
		});

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

				if (zoomFactor > 1 && k < 10) {

					scrollContent.getTransforms().add(scale);
					k++;
				}
				if (zoomFactor < 1 && k > -5) {
					scrollContent.getTransforms().add(scale);
					k--;
				}

				event.consume();
			}
		});

		DragContext sceneDragContext = new DragContext();
		// Panning via drag....
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
				scrollContent.setTranslateX(
						sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
				scrollContent.setTranslateY(
						sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);
				event.consume();
			}
		});
		fixUI();
		return scrollPane;
	}

	private void loadMap(Pane root, ImageView imageView) {
		// root.getChildren().remove(s1);
		k = 0; // Reset Zoom Variable
		root.getChildren().remove(zoomPane);
		root.getChildren().remove(canvas);
		imageView.setScaleX(1);
		imageView.setScaleY(1);
		// NodePane.setScaleX(1);
		// NodePane.setScaleY(1);

		nodeList.clear();
		// edgeList.clear();
		StartList.setOpacity(0);
		DestList.setOpacity(0);
		// System.out.print.println("Graphs/Nodes/" + mapSelector.getValue() +
		// ".json");
		nodeList = JsonParser.getJsonContent(mapSelector.getValue().getNodesPath());
		edgeListConversion = JsonParser.getJsonContentEdge(mapSelector.getValue().getEdgesPath());
		// edgeList = convertEdgeData(edgeListConversion);

		// graph = createGraph(new Graph(), nodeList, edgeList);

		File newMapFile = new File(mapSelector.getValue().getMapPath()); // MUST
																			// ADD
																			// png
																			// extension!
		Image mapImage = new Image(newMapFile.toURI().toString());
		imageView.setImage(mapImage);

		// add node buttons to the screen and populates the drop down menus
		/*
		 * LocationOptions.clear(); for(int i = 0; i < nodeList.size() - 1; i
		 * ++){ if(nodeList.get(i).getIsPlace())
		 * LocationOptions.add(nodeList.get(i).getName()); }
		 * StartList.setItems(LocationOptions);
		 * DestList.setItems(LocationOptions);
		 */

		// BASED ON THE INITALS DETERMINE THE BUILDING WERE LOADING..
		if (mapSelector.getValue().getInitials().contains("AK"))
			BuildingRolledOverCurrent = AtwaterKent;

		if (mapSelector.getValue().getInitials().contains("BH"))
			BuildingRolledOverCurrent = BoyntonHall;

		if (mapSelector.getValue().getInitials().contains("CC"))
			BuildingRolledOverCurrent = CampusCenter;

		if (mapSelector.getValue().getInitials().contains("FL"))
			BuildingRolledOverCurrent = FullerLabs;

		if (mapSelector.getValue().getInitials().contains("GL"))
			BuildingRolledOverCurrent = GordonLibrary;

		if (mapSelector.getValue().getInitials().contains("HH"))
			BuildingRolledOverCurrent = HigginsHouse;

		// Special for Higgins house
		if (mapSelector.getValue().getName().contains("Higgins House Garage")
				|| mapSelector.getValue().getName().contains("Higgins House Apartment"))
			BuildingRolledOverCurrent = HigginsHouseGarage;

		if (mapSelector.getValue().getInitials().contains("PC"))
			BuildingRolledOverCurrent = ProjectCenter;

		if (mapSelector.getValue().getInitials().contains("SH"))
			BuildingRolledOverCurrent = StrattonHall;
		
		if (mapSelector.getValue().getInitials().contains("WS"))
			BuildingRolledOverCurrent = WashburnShops;
		
		if (mapSelector.getValue().getInitials().contains("West"))
			BuildingRolledOverCurrent = WestStreet;
		
		if (mapSelector.getValue().getInitials().contains("SL"))
			BuildingRolledOverCurrent = SalisburyLabs;

		// graph = createGraph(graph, nodeList, edgeList);
		NodePane.getChildren().clear();
		gc.clearRect(0, 0, 8000, 6000);
		NodePane.setPrefSize(2450, 1250);
		canvas = new Canvas(2450, 1250);
		gc = canvas.getGraphicsContext2D();

		switch (mapSelector.getValue().getName()) {
		case "Campus Map":
			canvas = new Canvas(3000, 2000);
			gc = canvas.getGraphicsContext2D();
			NodePane.setPrefSize(3000, 2000);
			imageView.setScaleX(0.75);
			imageView.setScaleY(0.75);
			imageView.relocate(-1000, -600);
			highLight(NodePane, imageView, root, keyText);
			NodePane.setScaleX(0.75);
			NodePane.setScaleY(0.75);
			NodePane.relocate(-965, -643);
			canvas.setScaleX(0.75);
			canvas.setScaleY(0.75);
			canvas.relocate(-965, -643);
			buttonRescale = 1 / 0.75;
			initialPanAmountX = -340;
			initialPanAmountY = -165;
			break;
		case "Atwater Kent B":
			imageView.setScaleX(0.6536);
			imageView.setScaleY(0.6536);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6536);
			NodePane.setScaleY(0.6536);
			NodePane.relocate(-212, -88);
			canvas.setScaleX(0.6536);
			canvas.setScaleY(0.6536);
			canvas.relocate(-212, -88);
			buttonRescale = 1 / 0.6536;
			break;
		case "Atwater Kent 1":
			imageView.setScaleX(0.5161);
			imageView.setScaleY(0.5161);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5161);
			NodePane.setScaleY(0.5161);
			NodePane.relocate(-218, -22);
			canvas.setScaleX(0.5161);
			canvas.setScaleY(0.5161);
			canvas.relocate(-218, -22);
			buttonRescale = 1 / 0.5161;
			break;
		case "Atwater Kent 2":
			imageView.setScaleX(0.6706);
			imageView.setScaleY(0.6706);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6706);
			NodePane.setScaleY(0.6706);
			NodePane.relocate(-206, -57);
			canvas.setScaleX(0.6706);
			canvas.setScaleY(0.6706);
			canvas.relocate(-206, -57);
			buttonRescale = 1 / 0.6706;
			break;
		case "Atwater Kent 3":
			imageView.setScaleX(0.6536);
			imageView.setScaleY(0.6536);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6536);
			NodePane.setScaleY(0.6536);
			NodePane.relocate(-212, 0);
			canvas.setScaleX(0.6536);
			canvas.setScaleY(0.6536);
			canvas.relocate(-212, 0);
			buttonRescale = 1 / 0.6536;
			break;
		case "Boynton Hall B":
			imageView.setScaleX(0.5427);
			imageView.setScaleY(0.5427);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5427);
			NodePane.setScaleY(0.5427);
			NodePane.relocate(-200, -90);
			canvas.setScaleX(0.5427);
			canvas.setScaleY(0.5427);
			canvas.relocate(-200, -90);
			buttonRescale = 1 / 0.5427;
			break;
		case "Boynton Hall 1":
			imageView.setScaleX(0.5476);
			imageView.setScaleY(0.5476);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5476);
			NodePane.setScaleY(0.5476);
			NodePane.relocate(-220, -86);
			canvas.setScaleX(0.5476);
			canvas.setScaleY(0.5476);
			canvas.relocate(-220, -86);
			buttonRescale = 1 / 0.5476;
			break;
		case "Boynton Hall 2":
			imageView.setScaleX(0.5438);
			imageView.setScaleY(0.5438);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5438);
			NodePane.setScaleY(0.5438);
			NodePane.relocate(-220, -99);
			canvas.setScaleX(0.5438);
			canvas.setScaleY(0.5438);
			canvas.relocate(-220, -99);
			buttonRescale = 1 / 0.5438;
			break;
		case "Boynton Hall 3":
			imageView.setScaleX(0.5358);
			imageView.setScaleY(0.5358);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5358);
			NodePane.setScaleY(0.5358);
			NodePane.relocate(-220, -110);
			canvas.setScaleX(0.5358);
			canvas.setScaleY(0.5358);
			canvas.relocate(-220, -110);
			buttonRescale = 1 / 0.5358;
			break;
		case "Campus Center 1":
			imageView.setScaleX(0.6107);
			imageView.setScaleY(0.6107);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6107);
			NodePane.setScaleY(0.6107);
			NodePane.relocate(-222, -59);
			canvas.setScaleX(0.6107);
			canvas.setScaleY(0.6107);
			canvas.relocate(-222, -59);
			buttonRescale = 1 / 0.6107;
			break;
		case "Campus Center 2":
			imageView.setScaleX(0.6127);
			imageView.setScaleY(0.6127);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6127);
			NodePane.setScaleY(0.6127);
			NodePane.relocate(-222, -59);
			canvas.setScaleX(0.6127);
			canvas.setScaleY(0.6127);
			canvas.relocate(-222, -59);
			buttonRescale = 1 / 0.6127;
			break;
		case "Campus Center 3":
			imageView.setScaleX(0.6061);
			imageView.setScaleY(0.6061);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6061);
			NodePane.setScaleY(0.6061);
			NodePane.relocate(-222, -59);
			canvas.setScaleX(0.6061);
			canvas.setScaleY(0.6061);
			canvas.relocate(-222, -59);
			buttonRescale = 1 / 0.6061;
			break;
		case "Gordon Library SB":
			imageView.setScaleX(0.5686);
			imageView.setScaleY(0.5686);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5686);
			NodePane.setScaleY(0.5686);
			NodePane.relocate(-225, -42);
			canvas.setScaleX(0.5686);
			canvas.setScaleY(0.5686);
			canvas.relocate(-225, -42);
			buttonRescale = 1 / 0.5686;
			break;
		case "Gordon Library B":
			imageView.setScaleX(0.5409);
			imageView.setScaleY(0.5409);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5409);
			NodePane.setScaleY(0.5409);
			NodePane.relocate(-225, -42);
			canvas.setScaleX(0.5409);
			canvas.setScaleY(0.5409);
			canvas.relocate(-225, -42);
			buttonRescale = 1 / 0.5409;
			break;
		case "Gordon Library 1":
			imageView.setScaleX(0.5678);
			imageView.setScaleY(0.5678);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5678);
			NodePane.setScaleY(0.5678);
			NodePane.relocate(-225, -42);
			canvas.setScaleX(0.5678);
			canvas.setScaleY(0.5678);
			canvas.relocate(-225, -42);
			buttonRescale = 1 / 0.5678;
			break;
		case "Gordon Library 2":
			imageView.setScaleX(0.5638);
			imageView.setScaleY(0.5638);
			imageView.relocate(-0, 0);
			NodePane.setScaleX(0.5638);
			NodePane.setScaleY(0.5638);
			NodePane.relocate(-225, -42);
			canvas.setScaleX(0.5638);
			canvas.setScaleY(0.5638);
			canvas.relocate(-225, -42);
			buttonRescale = 1 / 0.5638;
			break;
		case "Gordon Library 3":
			imageView.setScaleX(0.6119);
			imageView.setScaleY(0.6119);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6119);
			NodePane.setScaleY(0.6119);
			NodePane.relocate(-225, -42);
			canvas.setScaleX(0.6119);
			canvas.setScaleY(0.6119);
			canvas.relocate(-225, -42);
			buttonRescale = 1 / 0.6119;
			break;
		case "Higgins House B":
			imageView.setScaleX(0.5181);
			imageView.setScaleY(0.5181);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5181);
			NodePane.setScaleY(0.5181);
			NodePane.relocate(-360, -22);
			canvas.setScaleX(0.5181);
			canvas.setScaleY(0.5181);
			canvas.relocate(-360, -22);
			buttonRescale = 1 / 0.5181;
			break;
		case "Higgins House 1":
			imageView.setScaleX(0.5535);
			imageView.setScaleY(0.5535);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5535);
			NodePane.setScaleY(0.5535);
			NodePane.relocate(-338, -37);
			canvas.setScaleX(0.5535);
			canvas.setScaleY(0.5535);
			canvas.relocate(-338, -37);
			buttonRescale = 1 / 0.5535;
			break;
		case "Higgins House 2":
			imageView.setScaleX(0.6067);
			imageView.setScaleY(0.6067);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6067);
			NodePane.setScaleY(0.6067);
			NodePane.relocate(-298, -50);
			canvas.setScaleX(0.6067);
			canvas.setScaleY(0.6067);
			canvas.relocate(-298, -50);
			buttonRescale = 1 / 0.6067;
			break;
		case "Higgins House 3":
			imageView.setScaleX(0.5917);
			imageView.setScaleY(0.5917);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5917);
			NodePane.setScaleY(0.5917);
			NodePane.relocate(-310, -48);
			canvas.setScaleX(0.5917);
			canvas.setScaleY(0.5917);
			canvas.relocate(-310, -48);
			buttonRescale = 1 / 0.5917;
			break;
		case "Higgins House Apartment":
			imageView.setScaleX(0.8197);
			imageView.setScaleY(0.8197);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.8197);
			NodePane.setScaleY(0.8197);
			NodePane.relocate(-130, -50);
			canvas.setScaleX(0.8197);
			canvas.setScaleY(0.8197);
			canvas.relocate(-130, -50);
			buttonRescale = 1 / 0.8197;
			break;
		case "Higgins House Garage":
			imageView.setScaleX(0.8172);
			imageView.setScaleY(0.8172);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.8172);
			NodePane.setScaleY(0.8172);
			NodePane.relocate(-133, -53);
			canvas.setScaleX(0.8172);
			canvas.setScaleY(0.8172);
			canvas.relocate(-133, -53);
			buttonRescale = 1 / 0.8172;
			break;
		case "Project Center 1":
			imageView.setScaleX(0.6764);
			imageView.setScaleY(0.6764);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6764);
			NodePane.setScaleY(0.6764);
			NodePane.relocate(-208, -58);
			canvas.setScaleX(0.6764);
			canvas.setScaleY(0.6764);
			canvas.relocate(-208, -58);
			buttonRescale = 1 / 0.6764;
			break;
		case "Project Center 2":
			imageView.setScaleX(0.6006);
			imageView.setScaleY(0.6006);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6006);
			NodePane.setScaleY(0.6006);
			NodePane.relocate(-222, -48);
			canvas.setScaleX(0.6006);
			canvas.setScaleY(0.6006);
			canvas.relocate(-222, -48);
			buttonRescale = 1 / 0.6006;
			break;
		case "Stratton Hall B":
			imageView.setScaleX(0.5464);
			imageView.setScaleY(0.5464);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5464);
			NodePane.setScaleY(0.5464);
			NodePane.relocate(-224, -88);
			canvas.setScaleX(0.5464);
			canvas.setScaleY(0.5464);
			canvas.relocate(-224, -88);
			buttonRescale = 1 / 0.5464;
			break;
		case "Stratton Hall 1":
			imageView.setScaleX(0.5583);
			imageView.setScaleY(0.5583);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5583);
			NodePane.setScaleY(0.5583);
			NodePane.relocate(-224, -82);
			canvas.setScaleX(0.5583);
			canvas.setScaleY(0.5583);
			canvas.relocate(-224, -82);
			buttonRescale = 1 / 0.5583;
			break;
		case "Stratton Hall 2":
			imageView.setScaleX(0.5556);
			imageView.setScaleY(0.5556);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5556);
			NodePane.setScaleY(0.5556);
			NodePane.relocate(-224, -86);
			canvas.setScaleX(0.5556);
			canvas.setScaleY(0.5556);
			canvas.relocate(-224, -86);
			buttonRescale = 1 / 0.5556;
			break;
		case "Stratton Hall 3":
			imageView.setScaleX(0.5544);
			imageView.setScaleY(0.5544);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5544);
			NodePane.setScaleY(0.5544);
			NodePane.relocate(-224, -83);
			canvas.setScaleX(0.5544);
			canvas.setScaleY(0.5544);
			canvas.relocate(-224, -83);
			buttonRescale = 1 / 0.5544;
			break;
		case "Fuller Labs SB":
			imageView.setScaleX(0.7882);
			imageView.setScaleY(0.7882);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.7882);
			NodePane.setScaleY(0.7882);
			NodePane.relocate(-150, -80);
			canvas.setScaleX(0.7882);
			canvas.setScaleY(0.7882);
			canvas.relocate(-150, -80);
			buttonRescale = 1 / 0.7882;
			break;
		case "Fuller Labs B":
			imageView.setScaleX(0.7601);
			imageView.setScaleY(0.7601);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.7601);
			NodePane.setScaleY(0.7601);
			NodePane.relocate(-170, -55);
			canvas.setScaleX(0.7601);
			canvas.setScaleY(0.7601);
			canvas.relocate(-170, -55);
			buttonRescale = 1 / 0.7601;
			break;
		case "Fuller Labs 1":
			imageView.setScaleX(0.6098);
			imageView.setScaleY(0.6098);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6098);
			NodePane.setScaleY(0.6098);
			NodePane.relocate(-250, -52);
			canvas.setScaleX(0.6098);
			canvas.setScaleY(0.6098);
			canvas.relocate(-250, -52);
			buttonRescale = 1 / 0.6098;
			break;
		case "Fuller Labs 2":
			imageView.setScaleX(0.5585);
			imageView.setScaleY(0.5585);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5585);
			NodePane.setScaleY(0.5585);
			NodePane.relocate(-250, -40);
			canvas.setScaleX(0.5585);
			canvas.setScaleY(0.5585);
			canvas.relocate(-250, -40);
			buttonRescale = 1 / 0.5585;
			break;
		case "Fuller Labs 3":
			imageView.setScaleX(0.5515);
			imageView.setScaleY(0.5515);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5515);
			NodePane.setScaleY(0.5515);
			NodePane.relocate(-270, -40);
			canvas.setScaleX(0.5515);
			canvas.setScaleY(0.5515);
			canvas.relocate(-270, -40);
			buttonRescale = 1 / 0.5515;
			break;
		case "157 West Street B":
			imageView.setScaleX(0.6305);
			imageView.setScaleY(0.6305);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6305);
			NodePane.setScaleY(0.6305);
			NodePane.relocate(-270, -55);
			canvas.setScaleX(0.6305);
			canvas.setScaleY(0.6305);
			canvas.relocate(-270, -55);
			buttonRescale = 1 / 0.6305;
			break;
		case "157 West Street 1":
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
		case "157 West Street 2":
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
		case "Salisbury Labs B":
			imageView.setScaleX(0.6976);
			imageView.setScaleY(0.6976);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6976);
			NodePane.setScaleY(0.6976);
			NodePane.relocate(-230, -60);
			canvas.setScaleX(0.6976);
			canvas.setScaleY(0.6976);
			canvas.relocate(-230, -60);
			buttonRescale = 1 / 0.6976;
			break;
		case "Salisbury Labs 1":
			imageView.setScaleX(0.5780);
			imageView.setScaleY(0.5780);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5780);
			NodePane.setScaleY(0.5780);
			NodePane.relocate(-220, -80);
			canvas.setScaleX(0.5780);
			canvas.setScaleY(0.5780);
			canvas.relocate(-220, -80);
			buttonRescale = 1 / 0.5780;
			break;
		case "Salisbury Labs 2":
			imageView.setScaleX(0.5755);
			imageView.setScaleY(0.5755);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5755);
			NodePane.setScaleY(0.5755);
			NodePane.relocate(-220, -80);
			canvas.setScaleX(0.5755);
			canvas.setScaleY(0.5755);
			canvas.relocate(-220, -80);
			buttonRescale = 1 / 0.5755;
			break;
		case "Salisbury Labs 3":
			imageView.setScaleX(0.6840);
			imageView.setScaleY(0.6840);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6840);
			NodePane.setScaleY(0.6840);
			NodePane.relocate(-240, -60);
			canvas.setScaleX(0.6840);
			canvas.setScaleY(0.6840);
			canvas.relocate(-240, -60);
			buttonRescale = 1 / 0.6840;
			break;
		case "Salisbury Labs 4":
			imageView.setScaleX(0.6988);
			imageView.setScaleY(0.6988);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6988);
			NodePane.setScaleY(0.6988);
			NodePane.relocate(-230, -60);
			canvas.setScaleX(0.6988);
			canvas.setScaleY(0.6988);
			canvas.relocate(-230, -60);
			buttonRescale = 1 / 0.6988;
			break;
		case "Washburn Shops B":
			imageView.setScaleX(0.5827);
			imageView.setScaleY(0.5827);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5827);
			NodePane.setScaleY(0.5827);
			NodePane.relocate(-225, -48);
			canvas.setScaleX(0.5827);
			canvas.setScaleY(0.5827);
			canvas.relocate(-225, -48);
			buttonRescale = 1 / 0.5827;
			break;
		case "Washburn Shops 1":
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
		case "Washburn Shops 2":
			imageView.setScaleX(0.5972);
			imageView.setScaleY(0.5972);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5972);
			NodePane.setScaleY(0.5972);
			NodePane.relocate(-225, -45);
			canvas.setScaleX(0.5972);
			canvas.setScaleY(0.5972);
			canvas.relocate(-225, -45);
			buttonRescale = 1 / 0.5972;
			break;
		case "Washburn Shops 3":
			imageView.setScaleX(0.6335);
			imageView.setScaleY(0.6335);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6335);
			NodePane.setScaleY(0.6335);
			NodePane.relocate(-225, -48);
			canvas.setScaleX(0.6335);
			canvas.setScaleY(0.6335);
			canvas.relocate(-225, -48);
			buttonRescale = 1 / 0.6335;
			break;
		case "Harrington Auditorium B":
			imageView.setScaleX(0.5168);
			imageView.setScaleY(0.5168);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5168);
			NodePane.setScaleY(0.5168);
			NodePane.relocate(-215, -35);
			canvas.setScaleX(0.5168);
			canvas.setScaleY(0.5168);
			canvas.relocate(-215, -35);
			buttonRescale = 1 / 0.5168;
			break;
		case "Harrington Auditorium 1":
			imageView.setScaleX(0.5057);
			imageView.setScaleY(0.5057);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5057);
			NodePane.setScaleY(0.5057);
			NodePane.relocate(-232, -10);
			canvas.setScaleX(0.5057);
			canvas.setScaleY(0.5057);
			canvas.relocate(-232, -10);
			buttonRescale = 1 / 0.5057;
			break;
		case "Harrington Auditorium 2":
			imageView.setScaleX(0.5185);
			imageView.setScaleY(0.5185);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5185);
			NodePane.setScaleY(0.5185);
			NodePane.relocate(-218, -45);
			canvas.setScaleX(0.5185);
			canvas.setScaleY(0.5185);
			canvas.relocate(-218, -45);
			buttonRescale = 1 / 0.5185;
			break;
		case "Harrington Auditorium 3":
			imageView.setScaleX(0.5191);
			imageView.setScaleY(0.5191);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5191);
			NodePane.setScaleY(0.5191);
			NodePane.relocate(-218, -45);
			canvas.setScaleX(0.5191);
			canvas.setScaleY(0.5191);
			canvas.relocate(-218, -45);
			buttonRescale = 1 / 0.5191;
			break;
		case "Higgins Labs B":
			imageView.setScaleX(0.6395);
			imageView.setScaleY(0.6395);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6395);
			NodePane.setScaleY(0.6395);
			NodePane.relocate(-215, -65);
			canvas.setScaleX(0.6395);
			canvas.setScaleY(0.6395);
			canvas.relocate(-215, -65);
			buttonRescale = 1 / 0.6395;
			break;
		case "Higgins Labs 1":
			imageView.setScaleX(0.6395);
			imageView.setScaleY(0.6395);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6395);
			NodePane.setScaleY(0.6395);
			NodePane.relocate(-215, -75);
			canvas.setScaleX(0.6395);
			canvas.setScaleY(0.6395);
			canvas.relocate(-215, -75);
			buttonRescale = 1 / 0.6395;
			break;
		case "Higgins Labs 2":
			imageView.setScaleX(0.6431);
			imageView.setScaleY(0.6431);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6431);
			NodePane.setScaleY(0.6431);
			NodePane.relocate(-215, -75);
			canvas.setScaleX(0.6431);
			canvas.setScaleY(0.6431);
			canvas.relocate(-215, -75);
			buttonRescale = 1 / 0.6431;
			break;
		case "Higgins Labs 3":
			imageView.setScaleX(0.6421);
			imageView.setScaleY(0.6421);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6421);
			NodePane.setScaleY(0.6421);
			NodePane.relocate(-215, -65);
			canvas.setScaleX(0.6421);
			canvas.setScaleY(0.6421);
			canvas.relocate(-215, -65);
			buttonRescale = 1 / 0.6421;
			break;
		case "Alden Hall SB":
			imageView.setScaleX(0.6385);
			imageView.setScaleY(0.6385);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6385);
			NodePane.setScaleY(0.6385);
			NodePane.relocate(-215, -85);
			canvas.setScaleX(0.6385);
			canvas.setScaleY(0.6385);
			canvas.relocate(-215, -85);
			buttonRescale = 1 / 0.6385;
			break;
		case "Alden Hall B":
			imageView.setScaleX(0.5780);
			imageView.setScaleY(0.5780);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.5780);
			NodePane.setScaleY(0.5780);
			NodePane.relocate(-220, -58);
			canvas.setScaleX(0.5780);
			canvas.setScaleY(0.5780);
			canvas.relocate(-220, -58);
			buttonRescale = 1 / 0.5780;
			break;
		case "Alden Hall 1":
			imageView.setScaleX(0.6385);
			imageView.setScaleY(0.6385);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6385);
			NodePane.setScaleY(0.6385);
			NodePane.relocate(-245, -85);
			canvas.setScaleX(0.6385);
			canvas.setScaleY(0.6385);
			canvas.relocate(-245, -85);
			buttonRescale = 1 / 0.6385;
			break;
		case "Alden Hall 2":
			imageView.setScaleX(0.6344);
			imageView.setScaleY(6344);
			imageView.relocate(0, 0);
			NodePane.setScaleX(0.6344);
			NodePane.setScaleY(0.6344);
			NodePane.relocate(-215, -75);
			canvas.setScaleX(0.6344);
			canvas.setScaleY(0.6344);
			canvas.relocate(-215, -75);
			buttonRescale = 1 / 0.6344;
			break;
			
		// TODO ADD BUILDINGS
		}
		if (!mapSelector.getValue().getName().equals("Campus Map")) {

			if (mapSelector.getValue().getName().equals("Atwater Kent 1")) {
				initialPanAmountX = (int) ((scene.getWidth() - (mapImage.getWidth() * (1 / buttonRescale))) / 2);
				initialPanAmountY = (int) ((scene.getHeight() - (mapImage.getHeight() * (1 / buttonRescale))) / 2);
			} else {
				initialPanAmountX = (int) ((scene.getWidth() - (mapImage.getWidth() * (1 / buttonRescale))) / 2);
				initialPanAmountY = (int) ((scene.getHeight() - (mapImage.getHeight() * (1 / buttonRescale))) / 2);
			}

		}

		gc.clearRect(0, 0, 8000, 6000);
		drawNodes(nodeList, NodePane, root, StartText, DestText, imageView);

		final Group group = new Group(imageView, canvas, NodePane);
		zoomPane = createZoomPane(group);

		
		
		
		
		root.getChildren().add(zoomPane);
				
		

		// Place the return to campus button on screen if youre not on the
		// campus map
		if (!mapSelector.getValue().getInitials().equals("CampusMap")) {

			root.getChildren().remove(ReturnToCampus);
			root.getChildren().add(ReturnToCampus);
			// ReturnToCampus.toFront();

			// We also want to choose floors, find number of floors based on
			// initials

			// reuse the same instance button to find the width
			Button tempButton = new Button();
			tempButton.setPrefWidth(120);
			// find out the max with to make the box
			double widthOfBox = 50;
			for (int i = 0; i < BuildingRolledOverCurrent.getNumMaps(); i++) {
				tempButton = new Button(BuildingRolledOverCurrent.getName() + " "
						+ BuildingRolledOverCurrent.getMaps().get(i).getFloor());

				final Scene snapScene = new Scene(tempButton);
				snapScene.snapshot(null);
				if (tempButton.getWidth() > widthOfBox)
					widthOfBox = tempButton.getWidth();
				// System.out.println(tempButton.getWidth());
			}
			
			ChangeFloorButtonGroup.getChildren().clear();
			Label ChooseFloorLabel = new Label("Floors");
			ChooseFloorLabel.setTextFill(Color.WHITE);
			ChooseFloorLabel.setFont(Font.font("manteka", 12));
			ChooseFloorLabel.setLayoutX(972);
			ChooseFloorLabel.setLayoutY(2);
			
			widthOfBox += 30; // so extends past edge of buttons
			int heightOfBox = 38 + BuildingRolledOverCurrent.getNumMaps() * 25;
			Rectangle backDrop = new Rectangle(2, 2, widthOfBox, heightOfBox);
			backDrop.setOpacity(.7);
			backDrop.setStyle("-fx-background-color: #515151;");
			backDrop.relocate(963, 0);
			ChangeFloorButtonGroup.getChildren().remove(backDrop);
			ChangeFloorButtonGroup.getChildren().addAll(backDrop, ChooseFloorLabel);
			// backDrop.toFront();
			int offsetCounter = 0;
			for (int i = 0; i < BuildingRolledOverCurrent.getNumMaps(); i++) {
				Button floorButton = new Button(BuildingRolledOverCurrent.getName() + " "
						+ BuildingRolledOverCurrent.getMaps().get(i).getFloor());
				floorButton.setTextFill(Color.BLACK);
				floorButton.setFont(Font.font("manteka", 10));
				floorButton.setLayoutX(970);
				floorButton.setLayoutY(20 + i * 22);

				
				ChangeFloorButtonGroup.getChildren().add(floorButton);
				// floorButton.toFront();
				offsetCounter = i;
				int floor = i;
				floorButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
					public void handle(MouseEvent event) {
						// mapSelector.setValue(value);
						fixUI();
						mapSelector.setValue(BuildingRolledOverCurrent.getMaps().get(floor));
						loadMap(root, imageView);
						fixUI();
					}
				});
			}
			System.out.println("offsetcounter " + offsetCounter);
			ReturnToCampus.relocate(970, 20+ ((offsetCounter+1) * 22));

			ChangeFloorButtonGroup.getChildren().add(ReturnToCampus);
			root.getChildren().remove(ChangeFloorButtonGroup);
			root.getChildren().add(ChangeFloorButtonGroup);
			// ChooseFloorLabel.toFront();
			// Button ReturnToCampus = new Button("Back to Campus");
		} else {
			root.getChildren().remove(ReturnToCampus);
		}
		fixUI();

	}

	public void highLight(Pane NodePane, ImageView imageView, Pane root, Text keyText) {

		DropShadow ds = new DropShadow();
		ds.setOffsetY(3.0);
		ds.setOffsetX(3.0);
		ds.setColor(Color.GRAY);

		Tooltip tooltip = new Tooltip("Click to show building floors");

		Color BuildingName = new Color(1, 1, 1, 1);
		Color key = new Color(1, 1, 1, 0.5);
		Polygon cc = new Polygon();
		double xOffset = 0.0;
		double yOffset = 0.0;
		cc.getPoints()
				.addAll(new Double[] { 1261.0 - xOffset, 649.0 - yOffset, 1272.0 - xOffset, 656.0 - yOffset,
						1273.0 - xOffset, 668.0 - yOffset, 1267.0 - xOffset, 677.0 - yOffset, 1254.0 - xOffset,
						680.0 - yOffset, 1245.0 - xOffset, 673.0 - yOffset, 1242.0 - xOffset, 662.0 - yOffset,
						1239.0 - xOffset, 677.0 - yOffset, 1175.0 - xOffset, 667.0 - yOffset, 1184.0 - xOffset,
						617.0 - yOffset, 1197.0 - xOffset, 620.0 - yOffset, 1213.0 - xOffset, 630.0 - yOffset,
						1220.0 - xOffset, 623.0 - yOffset, 1214.0 - xOffset, 617.0 - yOffset, 1223.0 - xOffset,
						604.0 - yOffset, 1219.0 - xOffset, 601.0 - yOffset, 1218.0 - xOffset, 591.0 - yOffset,
						1222.0 - xOffset, 582.0 - yOffset, 1234.0 - xOffset, 580.0 - yOffset, 1238.0 - xOffset,
						584.0 - yOffset, 1248.0 - xOffset, 573.0 - yOffset, 1235.0 - xOffset, 565.0 - yOffset,
						1249.0 - xOffset, 543.0 - yOffset, 1252.0 - xOffset, 537.0 - yOffset, 1302.0 - xOffset,
						546.0 - yOffset, 1285.0 - xOffset, 652.0 - yOffset });

		cc.setFill(Color.TRANSPARENT);

		cc.setStroke(Color.TRANSPARENT);
		cc.setStrokeWidth(1.0);
		cc.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				cc.setFill(new Color(1.0, 1.0, 0.0, 0.2));
				keyText.setText("Campus Center");
				keyText.setFill(BuildingName);
				BuildingRolledOver = CampusCenter;
			}
		});
		cc.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pause.play();
			}
		});
		cc.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText(" ");
				keyText.setFill(key);
				cc.setFill(Color.TRANSPARENT);
				BuildingRolledOver = NullBuilding;
			}
		});

		NodePane.getChildren().add(cc);

		Polygon olin = new Polygon();
		olin.getPoints().addAll(new Double[] {

				1334.0 - xOffset, 510.0 - yOffset, 1373.0 - xOffset, 516.0 - yOffset, 1350.0 - xOffset, 662.0 - yOffset,
				1311.0 - xOffset, 656.0 - yOffset });

		olin.setFill(Color.TRANSPARENT);

		olin.setStroke(Color.TRANSPARENT);
		olin.setStrokeWidth(1.0);
		olin.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				// keyText.setText("Olin Hall");
				// keyText.setFill(BuildingName);
				// BuildingRolledOver = olinHall;
				// olin.setFill(new Color(1.0, 1.0, 0.0, 0.2));
				// BuildingRolledOver = CampusCenter;
				// pause.play();
			}
		});
		olin.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				// keyText.setText(" ");
				// keyText.setFill(key);
				// olin.setFill(Color.TRANSPARENT);
				// BuildingRolledOver = NullBuilding;
			}
		});

		NodePane.getChildren().add(olin);

		Polygon stratton = new Polygon();
		stratton.getPoints().addAll(new Double[] {

				1377.0 - xOffset, 813.0 - yOffset, 1416.0 - xOffset, 820.0 - yOffset, 1403.0 - xOffset, 903.0 - yOffset,
				1363.0 - xOffset, 896.0 - yOffset });

		stratton.setFill(Color.TRANSPARENT);

		stratton.setStroke(Color.TRANSPARENT);
		stratton.setStrokeWidth(1.0);
		stratton.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				stratton.setFill(new Color(1.0, 1.0, 0.0, 0.2));
				keyText.setText("Stratton Hall");
				keyText.setFill(BuildingName);
				BuildingRolledOver = StrattonHall;
			}
		});
		stratton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pause.play();
			}
		});
		stratton.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText(" ");
				keyText.setFill(key);
				stratton.setFill(Color.TRANSPARENT);
				BuildingRolledOver = NullBuilding;
			}
		});

		NodePane.getChildren().add(stratton);

		Polygon library = new Polygon();
		library.getPoints().addAll(new Double[] {

				1607.0 - xOffset, 712.0 - yOffset, 1667.0 - xOffset, 725.0 - yOffset, 1664.0 - xOffset, 742.0 - yOffset,
				1661.0 - xOffset, 742.0 - yOffset, 1660.0 - xOffset, 769.0 - yOffset, 1658.0 - xOffset, 782.0 - yOffset,
				1655.0 - xOffset, 799.0 - yOffset, 1645.0 - xOffset, 824.0 - yOffset, 1648.0 - xOffset, 825.0 - yOffset,
				1644.0 - xOffset, 841.0 - yOffset, 1584.0 - xOffset, 829.0 - yOffset, 1585.0 - xOffset, 794.0 - yOffset,
				1588.0 - xOffset, 773.0 - yOffset, 1593.0 - xOffset, 750.0 - yOffset });

		library.setFill(Color.TRANSPARENT);

		library.setStroke(Color.TRANSPARENT);
		library.setStrokeWidth(1.0);
		library.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText("Gordon Library");
				keyText.setFill(BuildingName);
				library.setFill(new Color(1.0, 1.0, 0.0, 0.2));
				BuildingRolledOver = GordonLibrary;
			}
		});
		library.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pause.play();
			}
		});
		library.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText(" ");
				keyText.setFill(key);
				library.setFill(Color.TRANSPARENT);
				BuildingRolledOver = NullBuilding;
			}
		});

		NodePane.getChildren().add(library);

		Polygon ak = new Polygon();
		ak.getPoints().addAll(new Double[] {

				1471.0 - xOffset, 439.0 - yOffset, 1508.0 - xOffset, 460.0 - yOffset, 1491.0 - xOffset, 490.0 - yOffset,
				1540.0 - xOffset, 518.0 - yOffset, 1557.0 - xOffset, 489.0 - yOffset, 1594.0 - xOffset, 510.0 - yOffset,
				1553.0 - xOffset, 581.0 - yOffset, 1530.0 - xOffset, 569.0 - yOffset, 1522.0 - xOffset, 582.0 - yOffset,
				1445.0 - xOffset, 537.0 - yOffset, 1452.0 - xOffset, 537.0 - yOffset, 1452.0 - xOffset, 525.0 - yOffset,
				1429.0 - xOffset, 512.0 - yOffset });

		ak.setFill(Color.TRANSPARENT);
		ak.setStroke(Color.TRANSPARENT);
		ak.setStrokeWidth(1.0);
		ak.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText("Atwater Kent");
				keyText.setFill(BuildingName);
				ak.setFill(new Color(1.0, 1.0, 0.0, 0.2));
				BuildingRolledOver = AtwaterKent;
			}
		});
		ak.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pause.play();
			}
		});
		ak.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText(" ");
				keyText.setFill(key);
				ak.setFill(Color.TRANSPARENT);
				BuildingRolledOver = NullBuilding;
			}
		});

		NodePane.getChildren().add(ak);

		Polygon cdc = new Polygon();
		cdc.getPoints().addAll(new Double[] {
                   

				1391.0 - xOffset, 732.0 - yOffset, 1430.0 - xOffset, 738.0 - yOffset, 1420.0 - xOffset, 804.0 - yOffset,
				1380.0 - xOffset, 797.0 - yOffset });

		cdc.setFill(Color.TRANSPARENT);

		cdc.setStroke(Color.TRANSPARENT);
		cdc.setStrokeWidth(1.0);
		cdc.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText("Project Center");
				keyText.setFill(BuildingName);
				cdc.setFill(new Color(1.0, 1.0, 0.0, 0.2));
				BuildingRolledOver = ProjectCenter;
			}
		});
		cdc.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pause.play();
			}
		});
		cdc.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText(" ");
				keyText.setFill(key);
				cdc.setFill(Color.TRANSPARENT);
				BuildingRolledOver = NullBuilding;
			}
		});

		NodePane.getChildren().add(cdc);


		Polygon higginsHouse = new Polygon();
		higginsHouse.getPoints().addAll(new Double[] {

				1130.0 - xOffset, 435.0 - yOffset, 1154.0 - xOffset, 451.0 - yOffset, 1159.0 - xOffset, 443.0 - yOffset,
				1165.0 - xOffset, 446.0 - yOffset, 1161.0 - xOffset, 441.0 - yOffset, 1165.0 - xOffset, 435.0 - yOffset,
				1172.0 - xOffset, 435.0 - yOffset, 1176.0 - xOffset, 433.0 - yOffset, 1197.0 - xOffset, 448.0 - yOffset,
				1209.0 - xOffset, 431.0 - yOffset, 1225.0 - xOffset, 441.0 - yOffset, 1212.0 - xOffset, 459.0 - yOffset,
				1200.0 - xOffset, 452.0 - yOffset, 1192.0 - xOffset, 464.0 - yOffset, 1196.0 - xOffset, 466.0 - yOffset,
				1189.0 - xOffset, 476.0 - yOffset, 1185.0 - xOffset, 473.0 - yOffset, 1163.0 - xOffset, 505.0 - yOffset,
				1137.0 - xOffset, 487.0 - yOffset, 1149.0 - xOffset, 471.0 - yOffset, 1120.0 - xOffset,
				450.0 - yOffset });

		higginsHouse.setFill(Color.TRANSPARENT);

		higginsHouse.setStroke(Color.TRANSPARENT);
		higginsHouse.setStrokeWidth(1.0);
		higginsHouse.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText("Higgins House");
				keyText.setFill(BuildingName);
				higginsHouse.setFill(new Color(1.0, 1.0, 0.0, 0.2));
				BuildingRolledOver = HigginsHouse;
			}
		});
		higginsHouse.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pause.play();
			}
		});
		higginsHouse.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText(" ");
				keyText.setFill(key);
				higginsHouse.setFill(Color.TRANSPARENT);
				BuildingRolledOver = NullBuilding;
			}
		});

		NodePane.getChildren().add(higginsHouse);

		Polygon higginsHouseGAR = new Polygon();
		higginsHouseGAR.getPoints().addAll(new Double[] {

				1231.0 - xOffset, 404.0 - yOffset, 1216.0 - xOffset, 394.0 - yOffset, 1236.0 - xOffset, 367.0 - yOffset,
				1251.0 - xOffset, 377.0 - yOffset });

		higginsHouseGAR.setFill(Color.TRANSPARENT);

		higginsHouseGAR.setStroke(Color.TRANSPARENT);
		higginsHouseGAR.setStrokeWidth(1.0);
		higginsHouseGAR.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText("Higgins House Garage");
				keyText.setFill(BuildingName);
				higginsHouseGAR.setFill(new Color(1.0, 1.0, 0.0, 0.2));
				BuildingRolledOver = HigginsHouseGarage;
			}
		});
		higginsHouseGAR.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pause.play();
			}
		});
		higginsHouseGAR.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText(" ");
				keyText.setFill(key);
				higginsHouseGAR.setFill(Color.TRANSPARENT);
				BuildingRolledOver = NullBuilding;
			}
		});

		NodePane.getChildren().add(higginsHouseGAR);

		Polygon boyntonHall = new Polygon();
		boyntonHall.getPoints().addAll(new Double[] {

				1406.0 - xOffset, 932.0 - yOffset, 1435.0 - xOffset, 937.0 - yOffset, 1434.0 - xOffset, 943.0 - yOffset,
				1501.0 - xOffset, 954.0 - yOffset, 1497.0 - xOffset, 984.0 - yOffset, 1492.0 - xOffset, 984.0 - yOffset,
				1491.0 - xOffset, 988.0 - yOffset, 1480.0 - xOffset, 987.0 - yOffset, 1480.0 - xOffset, 981.0 - yOffset,
				1429.0 - xOffset, 973.0 - yOffset, 1428.0 - xOffset, 980.0 - yOffset, 1399.0 - xOffset,
				975.0 - yOffset });

		boyntonHall.setFill(Color.TRANSPARENT);

		boyntonHall.setStroke(Color.TRANSPARENT);
		boyntonHall.setStrokeWidth(1.0);
		boyntonHall.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText("Boynton Hall");
				keyText.setFill(BuildingName);
				boyntonHall.setFill(new Color(1.0, 1.0, 0.0, 0.2));
				BuildingRolledOver = BoyntonHall;
			}
		});
		boyntonHall.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pause.play();
			}
		});
		boyntonHall.setOnMouseExited(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				keyText.setText(" ");
				keyText.setFill(key);
				boyntonHall.setFill(Color.TRANSPARENT);
				BuildingRolledOver = NullBuilding;
			}
		});

		NodePane.getChildren().add(boyntonHall);

		Polygon fullerLabs = new Polygon();
		fullerLabs.getPoints().addAll(new Double[] {

				1560.0 - xOffset, 592.0 - yOffset, 1586.0 - xOffset, 645.0 - yOffset, 1663.0 - xOffset, 606.0 - yOffset,
				1645.0 - xOffset, 571.0 - yOffset, 1667.0 - xOffset, 558.0 - yOffset, 1638.0 - xOffset, 501.0 - yOffset,
				1632.0 - xOffset, 499.0 - yOffset, 1603.0 - xOffset, 515.0 - yOffset, 1616.0 - xOffset, 540.0 - yOffset,
				1573.0 - xOffset, 563.0 - yOffset, 1582.0 - xOffset, 580.0 - yOffset });

		fullerLabs.setFill(Color.TRANSPARENT);

		fullerLabs.setStroke(Color.TRANSPARENT);
		fullerLabs.setStrokeWidth(1.0);
		fullerLabs.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText("Fuller Labs");
				keyText.setFill(BuildingName);
				fullerLabs.setFill(new Color(1.0, 1.0, 0.0, 0.2));
				BuildingRolledOver = FullerLabs;
			}
		});
		fullerLabs.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pause.play();
			}
		});
		fullerLabs.setOnMouseExited(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				keyText.setText(" ");
				keyText.setFill(key);
				fullerLabs.setFill(Color.TRANSPARENT);
				BuildingRolledOver = NullBuilding;
			}
		});
		NodePane.getChildren().add(fullerLabs);

		Polygon salisburyLabs = new Polygon();
		salisburyLabs.getPoints().addAll(new Double[] {

				1438.0 - xOffset, 700.0 - yOffset, 1446.0 - xOffset, 636.0 - yOffset, 1477.0 - xOffset, 639.0 - yOffset,
				1482.0 - xOffset, 614.0 - yOffset, 1547.0 - xOffset, 624.0 - yOffset, 1529.0 - xOffset, 737.0 - yOffset,
				1487.0 - xOffset, 730.0 - yOffset, 1489.0 - xOffset, 709.0 - yOffset });

		salisburyLabs.setFill(Color.TRANSPARENT);

		salisburyLabs.setStroke(Color.TRANSPARENT);
		salisburyLabs.setStrokeWidth(1.0);
		salisburyLabs.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText("Salisbury Labs");
				keyText.setFill(BuildingName);
				salisburyLabs.setFill(new Color(1.0, 1.0, 0.0, 0.2));
				BuildingRolledOver = SalisburyLabs;
			}
		});
		salisburyLabs.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pause.play();
			}
		});
		salisburyLabs.setOnMouseExited(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				keyText.setText(" ");
				keyText.setFill(key);
				salisburyLabs.setFill(Color.TRANSPARENT);
				BuildingRolledOver = NullBuilding;
			}
		});
		NodePane.getChildren().add(salisburyLabs);

		Polygon washburnShops = new Polygon();
		washburnShops.getPoints().addAll(new Double[] {

				1426.0 - xOffset, 901.0 - yOffset, 1435.0 - xOffset, 848.0 - yOffset, 1448.0 - xOffset, 850.0 - yOffset,
				1450.0 - xOffset, 839.0 - yOffset, 1442.0 - xOffset, 837.0 - yOffset, 1446.0 - xOffset, 809.0 - yOffset,
				1436.0 - xOffset, 806.0 - yOffset, 1442.0 - xOffset, 772.0 - yOffset, 1529.0 - xOffset, 786.0 - yOffset,
				1516.0 - xOffset, 863.0 - yOffset, 1524.0 - xOffset, 864.0 - yOffset, 1520.0 - xOffset, 892.0 - yOffset,
				1512.0 - xOffset, 891.0 - yOffset, 1505.0 - xOffset, 933.0 - yOffset, 1474.0 - xOffset, 928.0 - yOffset,
				1486.0 - xOffset, 854.0 - yOffset, 1478.0 - xOffset, 852.0 - yOffset, 1469.0 - xOffset,
				907.0 - yOffset });

		washburnShops.setFill(Color.TRANSPARENT);

		washburnShops.setStroke(Color.TRANSPARENT);
		washburnShops.setStrokeWidth(1.0);
		washburnShops.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText("Washburn Shops");
				keyText.setFill(BuildingName);
				washburnShops.setFill(new Color(1.0, 1.0, 0.0, 0.2));
				BuildingRolledOver = WashburnShops;
			}
		});
		washburnShops.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pause.play();
			}
		});
		washburnShops.setOnMouseExited(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				keyText.setText(" ");
				keyText.setFill(key);
				washburnShops.setFill(Color.TRANSPARENT);
				BuildingRolledOver = NullBuilding;
			}
		});
		NodePane.getChildren().add(washburnShops);

		Polygon westStreet = new Polygon();
		westStreet.getPoints().addAll(new Double[] {

				1306.0 - xOffset, 1289.0 - yOffset, 1310.0 - xOffset, 1266.0 - yOffset, 1349.0 - xOffset,
				1272.0 - yOffset, 1345.0 - xOffset, 1295.0 - yOffset });

		westStreet.setFill(Color.TRANSPARENT);

		westStreet.setStroke(Color.TRANSPARENT);
		westStreet.setStrokeWidth(1.0);
		westStreet.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText("157 West Street");
				keyText.setFill(BuildingName);
				westStreet.setFill(new Color(1.0, 1.0, 0.0, 0.2));
				BuildingRolledOver = WestStreet;
			}
		});
		westStreet.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pause.play();
			}
		});
		westStreet.setOnMouseExited(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				keyText.setText(" ");
				keyText.setFill(key);
				westStreet.setFill(Color.TRANSPARENT);
				BuildingRolledOver = NullBuilding;
			}
		});
		NodePane.getChildren().add(westStreet);
		
		Polygon harrington = new Polygon();
		harrington.getPoints().addAll(new Double[] {

				956.0 - xOffset, 647.0 - yOffset, 1092.0 - xOffset, 669.0 - yOffset, 1089.0 - xOffset, 714.0 - yOffset,
				1078.0 - xOffset, 754.0 - yOffset, 1048.0 - xOffset, 758.0 - yOffset, 1046.0 - xOffset, 769.0 - yOffset,
				1036.0 - xOffset, 766.0 - yOffset, 1036.0 - xOffset, 759.0 - yOffset, 1029.0 - xOffset, 765.0 - yOffset,
				986.0 - xOffset, 758.0 - yOffset, 989.0 - xOffset, 753.0 - yOffset, 969.0 - xOffset, 744.0 - yOffset,
				967.0 - xOffset, 755.0 - yOffset, 955.0 - xOffset, 754.0 - yOffset, 956.0 - xOffset, 739.0 - yOffset,
				943.0 - xOffset, 733.0 - yOffset, 944.0 - xOffset, 691.0 - yOffset });

		harrington.setFill(Color.TRANSPARENT);

		harrington.setStroke(Color.TRANSPARENT);
		harrington.setStrokeWidth(1.0);
		harrington.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText("Harrington Auditorium");
				keyText.setFill(BuildingName);
				harrington.setFill(new Color(1.0, 1.0, 0.0, 0.2));
				BuildingRolledOver = Harrington;
			}
		});
		harrington.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pause.play();
			}
		});
		harrington.setOnMouseExited(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				keyText.setText(" ");
				keyText.setFill(key);
				harrington.setFill(Color.TRANSPARENT);
				BuildingRolledOver = NullBuilding;
			}
		});
		NodePane.getChildren().add(harrington);
		
		Polygon higgins = new Polygon();
		higgins.getPoints().addAll(new Double[] {

				1323.0 - xOffset, 716.0 - yOffset, 1323.0 - xOffset, 720.0 - yOffset, 1340.0 - xOffset, 724.0 - yOffset,
				1335.0 - xOffset, 753.0 - yOffset, 1319.0 - xOffset, 751.0 - yOffset, 1313.0 - xOffset, 779.0 - yOffset,
				1309.0 - xOffset, 790.0 - yOffset, 1307.0 - xOffset, 820.0 - yOffset, 1322.0 - xOffset, 820.0 - yOffset,
				1317.0 - xOffset, 850.0 - yOffset, 1300.0 - xOffset, 850.0 - yOffset, 1300.0 - xOffset, 856.0 - yOffset,
				1288.0 - xOffset, 855.0 - yOffset, 1272.0 - xOffset, 853.0 - yOffset, 1260.0 - xOffset, 849.0 - yOffset,
				1259.0 - xOffset, 844.0 - yOffset, 1242.0 - xOffset, 842.0 - yOffset, 1240.0 - xOffset, 807.0 - yOffset,
				1254.0 - xOffset, 729.0 - yOffset, 1273.0 - xOffset, 728.0 - yOffset, 1281.0 - xOffset, 708.0 - yOffset});

		higgins.setFill(Color.TRANSPARENT);

		higgins.setStroke(Color.TRANSPARENT);
		higgins.setStrokeWidth(1.0);
		higgins.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText("Higgins Labs");
				keyText.setFill(BuildingName);
				higgins.setFill(new Color(1.0, 1.0, 0.0, 0.2));
				BuildingRolledOver = HigginsLabs;
			}
		});
		higgins.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pause.play();
			}
		});
		higgins.setOnMouseExited(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent event) {
				keyText.setText(" ");
				keyText.setFill(key);
				higgins.setFill(Color.TRANSPARENT);
				BuildingRolledOver = NullBuilding;
			}
		});
		NodePane.getChildren().add(higgins);
		
		Polygon aldenHall = new Polygon();
		aldenHall.getPoints().addAll(new Double[] {

				1242.0 - xOffset, 969.0 - yOffset, 1248.0 - xOffset, 970.0 - yOffset, 1250.0 - xOffset, 965.0 - yOffset,
				1281.0 - xOffset, 970.0 - yOffset, 1280.0 - xOffset, 975.0 - yOffset, 1287.0 - xOffset, 978.0 - yOffset,
				1274.0 - xOffset, 1056.0 - yOffset, 1288.0 - xOffset, 1060.0 - yOffset, 1285.0 - xOffset, 1083.0 - yOffset,
				1263.0 - xOffset, 1079.0 - yOffset, 1263.0 - xOffset, 1087.0 - yOffset, 1231.0 - xOffset, 1083.0 - yOffset,
				1231.0 - xOffset, 1076.0 - yOffset, 1214.0 - xOffset, 1070.0 - yOffset, 1218.0 - xOffset, 1048.0 - yOffset,
				1229.0 - xOffset, 1050.0 - yOffset});

		aldenHall.setFill(Color.TRANSPARENT);

		aldenHall.setStroke(Color.TRANSPARENT);
		aldenHall.setStrokeWidth(1.0);
		aldenHall.setOnMouseEntered(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText("Alden Hall");
				keyText.setFill(BuildingName);
				aldenHall.setFill(new Color(1.0, 1.0, 0.0, 0.2));
				BuildingRolledOver = AldenHall;
			}
		});
		aldenHall.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				pause.play();
			}
		});
		aldenHall.setOnMouseExited(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				keyText.setText(" ");
				keyText.setFill(key);
				aldenHall.setFill(Color.TRANSPARENT);
				BuildingRolledOver = NullBuilding;
			}
		});

		NodePane.getChildren().add(aldenHall);

		Tooltip.install(fullerLabs, tooltip);
		Tooltip.install(cc, tooltip);
		Tooltip.install(ak, tooltip);
		Tooltip.install(boyntonHall, tooltip);
		Tooltip.install(higginsHouse, tooltip);
		Tooltip.install(higginsHouseGAR, tooltip);
		Tooltip.install(cdc, tooltip);
		Tooltip.install(library, tooltip);
		Tooltip.install(stratton, tooltip);
		Tooltip.install(salisburyLabs, tooltip);
		Tooltip.install(washburnShops, tooltip);
		Tooltip.install(westStreet, tooltip);
		Tooltip.install(aldenHall, tooltip);
		Tooltip.install(higgins, tooltip);
		Tooltip.install(harrington, tooltip);

		pause.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				if (!BuildingRolledOver.equals(NullBuilding)) {
					BuildingRolledOverCurrent = BuildingRolledOver;
					getMapSelector(BuildingRolledOver, root, imageView);
				}
			}
		});
		fixUI();
	}

	public static double round(double value, int places) {
		if (places < 0) {
			throw new IllegalArgumentException();
		}

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	protected LinkedList<Node> createNodeTypeList(String s) {
		LinkedList<Node> theList = new LinkedList<>();
		globalGraph.getNodes().stream().filter(n -> Objects.equals(n.getType(), s)).forEach(theList::add);
		return theList;
	}

	protected Node findNearestNode(Node start, String type) {
		LinkedList<Node> nodeTypeList = new LinkedList<>();
		// TODO add more types
		if (Objects.equals(type, "Men's Bathroom")) {
			nodeTypeList = MensBathroomNodes;
		}
		if (Objects.equals(type, "Women's Bathroom")) {
			nodeTypeList = WomensBathroomNodes;
		}
		if (Objects.equals(type, "Dining")) {
			nodeTypeList = DiningNodes;
		}
		if (Objects.equals(type, "Vending Machine")) {
			nodeTypeList = VendingMachineNodes;
		}
		if (Objects.equals(type, "Water Fountain")) {
			nodeTypeList = WaterFountainNodes;
		}
		TreeMap<Double, Node> nearestNodes = new TreeMap<>();
		nodeTypeList.stream().forEach(n -> nearestNodes.put(Graph.d(n, start), n));
		return nearestNodes.pollFirstEntry().getValue();
	}

	public void findAndDisplayRoute(ImageView imageView) {
		directionBox.getChildren().clear();
		gc.clearRect(0, 0, 8000, 6000); // Clears old path
        root.getChildren().remove(zoomPane);
		keyText.setText("");

		// Need to string compare from
		Node startPlace = new Node(0, 0, 0, "", "", "", false, false, "");
		Node endPlace = new Node(0, 0, 0, "", "", "", false, false, "");
		for (int i = 0; i < globalGraph.getNodes().size(); i++) {
			if ((globalGraph.getNodes().get(i)).getName().equals(StartText.getText())) {
				startPlace = (globalGraph.getNodes().get(i));
			}
			if ((globalGraph.getNodes().get(i)).getName().equals(DestText.getText())) {
				endPlace = (globalGraph.getNodes().get(i));
			}
		}
		// System.out.print.println("start: " + startPlace.getName());
		// System.out.print.println("end: " + endPlace.getName());

		route = new LinkedList<Node>();
		route = globalGraph.findRoute(startPlace, endPlace);
		savedRoute = route;
		keyText.setFont(Font.font("manteka", 20));

		if (!(startPlace.equals(endPlace))) {

			try {
				// if the entire route is only on 1 map, display all instruction
				// at once
                multiMap = splitRoute(route);//is endlessly looping or suttin
                currRoute = 0;
				displayInstructions(multiMap.get(currRoute), root);
				root.getChildren().remove(zoomPane);
				Map initials = null;

				for (Map map : maps) {
					// System.out.print.println("CURRENT ROUTE: "+ currRoute);
					// System.out.print.println("multiMap.get(currRouteE: "+
					// multiMap.get(currRoute).get(0).getFloorMap());
					if (map.getName().equals(multiMap.get(currRoute).get(0).getFloorMap()))
						initials = map;
				}
				// System.out.print.println("INITIALS: "+ initials);
				gc.clearRect(0, 0, 6000, 3000);

				mapSelector.setValue(initials);
				loadMap(root, imageView);
//				if (multiMap.get(currRoute).size() > 2)
//					root.getChildren().add(s1);
				drawNodes(nodeList, NodePane, root, StartText, DestText, imageView);
				drawRoute(gc, multiMap.get(currRoute));

				NodePane.getChildren().add(greenPinView);
				greenPinView.setLayoutX(multiMap.getFirst().getFirst().getX() - 18);
				greenPinView.setLayoutY(multiMap.getFirst().getFirst().getY() - 55);

				if (multiMap.size() == 1) {

					NodePane.getChildren().add(redPinView);
					redPinView.setLayoutX(multiMap.getFirst().getLast().getX() - 18);
					redPinView.setLayoutY(multiMap.getFirst().getLast().getY() - 55);
				}

				/*
				 * final Group group = new Group(imageView, canvas, NodePane);
				 * zoomPane = createZoomPane(group);
				 * root.getChildren().add(zoomPane);
				 */

				route = new LinkedList<Node>();
                if(!directionsAreOut){
                    menuDirectionsAnimation(directionsGroup);
                    dirSliderButton.setRotate(-90);
                    //System.out.println("X: " + directionsGroup.getLayoutX() + ", Y: " + directionsGroup.getLayoutY());
                    directionsAreOut = true;
                }
			} catch (NullPointerException n) {
				keyText.setText("Path not Found");
				keyText.setFill(Color.WHITE);
				loadMap(root, imageView);
			}
		} else {
			loadMap(root, imageView);
			keyText.setFont(Font.font("manteka", 14));
			keyText.setFill(Color.WHITE);
			keyText.setText("Your Start and Destination are the same");
		}
		
		
		fixUI();
	}

	public void findNearestDisplayRoute(ImageView imageView) {
		Node actualStartNode = new Node(0, 0, 0, "", "", "", false, false, "");
		startBool = false;
		destBool = false;
		startButtonBool = false;
		destButtonBool = false;
		for (Node n : globalGraph.getNodes()) {
			if (n.getName().equals(StartText.getText())) {
				actualStartNode = n;
			}
		}
		try {
			Node node = findNearestNode(actualStartNode, (String) nearestDropdown.getValue());
			root.getChildren().remove(zoomPane);
			route = globalGraph.findRoute(actualStartNode, node);
			savedRoute = route;
			try {

				multiMap = splitRoute(route);// is endlessly looping or suttin
				currRoute = 0;

				// if the entire route is only on 1 map, display all instruction
				// at once
				displayInstructions(multiMap.get(currRoute), root);
				Map initials = null;

				for (int i = 0; i < maps.size(); i++) {
					// System.out.print.println("CURRENT ROUTE: "+ currRoute);
					// System.out.print.println("multiMap.get(currRouteE: "+
					// multiMap.get(currRoute).get(0).getFloorMap());
					if (maps.get(i).getName().equals(multiMap.get(currRoute).get(0).getFloorMap()))
						initials = maps.get(i);
				}
				// System.out.print.println("INITIALS: "+ initials);
				gc.clearRect(0, 0, 6000, 3000);

				mapSelector.setValue(initials);
				loadMap(root, imageView);
//				if (multiMap.get(currRoute).size() > 2)
//					root.getChildren().add(s1);
				drawNodes(nodeList, NodePane, root, StartText, DestText, imageView);
				drawRoute(gc, multiMap.get(currRoute));

				NodePane.getChildren().add(greenPinView);
				greenPinView.setLayoutX(multiMap.getFirst().getFirst().getX() - 18);
				greenPinView.setLayoutY(multiMap.getFirst().getFirst().getY() - 55);

				if (multiMap.size() == 1) {
					NodePane.getChildren().add(redPinView);
					redPinView.setLayoutX(multiMap.getFirst().getLast().getX() - 18);
					redPinView.setLayoutY(multiMap.getFirst().getLast().getY() - 55);

				}

                if(!directionsAreOut){
                    menuDirectionsAnimation(directionsGroup);
                    dirSliderButton.setRotate(-90);
                    //System.out.println("X: " + directionsGroup.getLayoutX() + ", Y: " + directionsGroup.getLayoutY());
                    directionsAreOut = true;
                }
				/*
				 * final Group group = new Group(imageView, canvas, NodePane);
				 * zoomPane = createZoomPane(group);
				 * root.getChildren().add(zoomPane);
				 */

				route = new LinkedList<Node>();
			} catch (NullPointerException n) {
				keyText.setText("Path not Found");
				keyText.setFill(Color.WHITE);
				loadMap(root, imageView);
			}
			// System.out.println(node.getName());
		} catch (NullPointerException n) {
			// System.out.println("Node not found");
		}
		fixUI();
	}
}
