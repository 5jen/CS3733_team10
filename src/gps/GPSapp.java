package gps;

import java.io.File;
import java.util.LinkedList;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import node.Edge;
import node.Node;
import node.EdgeDataConversion;
import node.Graph;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import io.JsonParser;


public class GPSapp extends Application{
	public static void main(String[] args) {
        launch(args);
    }
	
	//Load up the JSON data and create the nodes for the map
	JsonParser json = new JsonParser();
	LinkedList<Node> nodeList = JsonParser.getJsonContent("Graphs/AK1.json");
	LinkedList<EdgeDataConversion> edgeListConversion = JsonParser.getJsonContentEdge("Graphs/AK1Edges.json");
	LinkedList<Edge> edgeList = convertEdgeData(edgeListConversion);	
	Canvas canvas = new Canvas(800, 650);
    GraphicsContext gc = canvas.getGraphicsContext2D();
	boolean start, end = false;
	String startNode, endNode;
	Graph graph = new Graph();
	ObservableList<String> LocationOptions = FXCollections.observableArrayList();
	ListView<String> StartList = new ListView<String>();
    ListView<String> DestList = new ListView<String>();
    TextField StartText = new TextField();
	TextField DestText = new TextField();
	
    @Override
    public void start(Stage primaryStage) {
    	
    	final Pane root = new Pane(); 
    	
    	//Create a map selection drop down menu
    	final VBox mapSelectionBoxV = new VBox(5);
    	final Label mapSelectorLabel = new Label("Choose map");
    	mapSelectorLabel.setTextFill(Color.WHITE);
    	final HBox mapSelectionBoxH = new HBox(5);
    	ObservableList<String> mapOptions = FXCollections.observableArrayList("AK1", "AK2", "AK3");
    	final ComboBox<String> mapSelector = new ComboBox<String>(mapOptions);
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
    	
    	
    	//Initialize the Drop down menu for initial Map
    	for(int i = 0; i < nodeList.size() ; i ++){ 
    		if(nodeList.get(i).getIsPlace())
    			LocationOptions.add((nodeList.get(i)).getName());
        }
    	
    	//Find Route Button
    	final Button findRouteButton = new Button("Find Route");
    	findRouteButton.relocate(650, 610);

    	
    	
    	//Searchable text boxes
    	VBox StartSearch = new VBox();
        VBox DestSearch = new VBox();
        StartText.setPromptText("Start");
        DestText.setPromptText("Destination");        
        StartList.setMaxHeight(75);
        DestList.setMaxHeight(75);
        StartList.setItems(LocationOptions);      
        DestList.setItems(LocationOptions);
        StartSearch.relocate(300, 610);
        StartSearch.getChildren().addAll(DestText, DestList);
        DestSearch.relocate(20, 610);
        DestSearch.getChildren().addAll(StartText, StartList);
        StartList.setOpacity(0);
        DestList.setOpacity(0);
        

  
        //Create the map image
        File mapFile = new File("CS3733_Graphics/AK1.png");
        mapSelector.setValue("AK1"); // Default Map when App is opened
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
        
        //Create a keyimage to place the map key on screen
    	File keyFile = new File("CS3733_Graphics/Key.png");
        Image keyImage = new Image(keyFile.toURI().toString());
        
        ImageView imageViewKey = new ImageView();
        imageViewKey.setImage(keyImage);
        
        imageViewKey.setLayoutX(830);  
        imageViewKey.setLayoutY(530);
        
        //Add images to the screen
        root.getChildren().add(bgView); //Must add background image first!
        root.getChildren().add(mapSelectionBoxV);
        //root.getChildren().add(imageView);
        root.getChildren().add(imageViewKey);
        root.getChildren().add(StartSearch);
        root.getChildren().add(DestSearch);
        root.getChildren().add(findRouteButton);
        
        //Removes top bar!! Maybe implement a custom one to look better
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        
        
        
        graph = createGraph(graph, nodeList, edgeList);
        Pane NodePane = new Pane();

	    drawNodes(nodeList, NodePane, StartText, DestText);

        final Group group = new Group(imageView, canvas, NodePane);
	    Parent zoomPane = createZoomPane(group);
	    
	    root.getChildren().add(zoomPane);

	   
        
        //Add actions to the Load Map button
        LoadMapButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {

        	    root.getChildren().remove(zoomPane);
        	    root.getChildren().remove(canvas);
   
            	nodeList.clear();
           		edgeList.clear();
           		StartText.clear();
           		DestText.clear();
                StartList.setOpacity(0);
                DestList.setOpacity(0);
            	nodeList = JsonParser.getJsonContent("Graphs/" + (String) mapSelector.getValue() + ".json");
            	edgeListConversion = JsonParser.getJsonContentEdge("Graphs/" + (String) mapSelector.getValue() + "Edges.json");
            	edgeList = convertEdgeData(edgeListConversion);
            	
            	graph = createGraph(new Graph(), nodeList, edgeList);
            	
            	File newMapFile = new File("CS3733_Graphics/" + (String) mapSelector.getValue() + ".png"); //MUST ADD png extension!
            	Image mapImage = new Image(newMapFile.toURI().toString());
                imageView.setImage(mapImage);
                //add node buttons to the screen and populates the drop down menus
                LocationOptions.clear();
                for(int i = 0; i < nodeList.size() - 1; i ++){ 
                	if(nodeList.get(i).getIsPlace())
                		LocationOptions.add(nodeList.get(i).getName());
                }
                StartList.setItems(LocationOptions);      
                DestList.setItems(LocationOptions);
                
                graph = createGraph(graph, nodeList, edgeList);
                Pane NodePane = new Pane();
                gc.clearRect(0, 0, 800, 600);
                drawNodes(nodeList, NodePane, StartText, DestText);
                              
                final Group group = new Group(imageView, canvas, NodePane);
        	    Parent zoomPane = createZoomPane(group);
        	    root.getChildren().add(zoomPane);
        	    
            }
        });
        
        //Add button actions
        findRouteButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent event) {
            	gc.clearRect(0, 0, 800, 600); // Clears old path
            	if (StartText.getText().equals("")|| DestText.getText().equals("")) {
            		
            	} else {
            		root.getChildren().remove(zoomPane);
            	
                	// Need to string compare from 
                	Node startPlace = new Node(0, 0, 0, "", "", false, false, "");
                	Node endPlace = new Node(0, 0, 0, "", "", false, false, "");
                	for(int i = 0; i < nodeList.size(); i ++){ 
                    	if((nodeList.get(i)).getName().equals(StartText.getText())) {
                    		startPlace = (nodeList.get(i));
                    	}
                    	if((nodeList.get(i)).getName().equals(DestText.getText())) {
                    		endPlace = (nodeList.get(i));
                    	}
                    }
                	System.out.println("start: " + startPlace.getName());
                	System.out.println("end: " + endPlace.getName());
                	
                    LinkedList<Node> route = new LinkedList<Node>();
                    route = graph.findRoute(startPlace, endPlace); 
                    
                    System.out.println(" " +route);
                    for(int i = 0; i < route.size(); i++){
                    	System.out.println("Route node: " + i + " , " + route.get(i).getName());
                    }
                    
                    Pane NodePane = new Pane();
                    drawNodes(nodeList, NodePane, StartText, DestText);
                    drawRoute(gc, route);
                    
                    final Group group = new Group(imageView, canvas, NodePane);
            	    Parent zoomPane = createZoomPane(group);
            	    root.getChildren().add(zoomPane);

                    route = new LinkedList<Node>();
            	}
            }
        });
        
  
        primaryStage.setScene(new Scene(root, 1050, 700));  
        primaryStage.show();
        
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
            		if (newPropertyValue) {
            			DestList.setOpacity(100);
                    }
                    else {
                    	DestList.setOpacity(0);
                    }
            	}
            });
            
            StartText.focusedProperty().addListener(new ChangeListener<Boolean>() {
            	public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
            		if (newPropertyValue) {
            			StartList.setOpacity(100);
                    }
                    else {
                    	StartList.setOpacity(0);
                    }
            	}
            });
                    
            StartList.setOnMouseClicked(new EventHandler<MouseEvent>() {

    			public void handle(MouseEvent arg0) {
    				StartText.setText((String) StartList.getSelectionModel().getSelectedItem());
    				
    			}
            });
            DestList.setOnMouseClicked(new EventHandler<MouseEvent>() {

    			public void handle(MouseEvent arg0) {
    				DestText.setText((String) DestList.getSelectionModel().getSelectedItem());
    				
    			}
            });
    }  


	private Graph createGraph(Graph g, LinkedList<Node> nodes, LinkedList<Edge> edges){
    	g.setNodes(nodes);
    
    	for(int i = 0; i < edges.size(); i++){
    		g.addEdge(edges.get(i).getFrom(), edges.get(i).getTo());
    	}
    	
    	return g;
    }
    
    
    private void drawNodes(LinkedList<Node> nodes, Pane root, TextField startText, TextField destText){
    	int i;
    	for(i = 0; i < nodes.size(); i ++){ 
    		if(nodes.get(i).getIsPlace()){
        		Button newNodeButton = new Button("");
            	newNodeButton.setStyle(
                        "-fx-background-radius: 5em; " +
                        "-fx-min-width: 15px; " +
                        "-fx-min-height: 15px; " +
                        "-fx-max-width: 15px; " +
                        "-fx-max-height: 15px;"
                );
            	newNodeButton.relocate(nodes.get(i).getX(), nodes.get(i).getY());
            	Node newNode = nodes.get(i);
            	newNodeButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                    	if (!start){
                    		if(newNode.getIsPlace()) startNode = newNode.getName();
                    		startText.setText(startNode);
                    		start = true;
                    	}
                    	else if(!end){
                    		if(newNode.getIsPlace()) endNode = newNode.getName();
                    		DestText.setText(endNode);
                    		start = false;
                    		end = false;
                    	}
                    }
                });
            	root.getChildren().add(newNodeButton);
    		} else if(!nodes.get(i).getIsPlace()){
    			//Do nothing
    		}
	  		
    	}
    }
    
    private void drawRoute(GraphicsContext gc, LinkedList<Node> route) {
    	 Color customBlue = Color.web("0x00b3fd"); 
    	 
    	 gc.setLineCap(StrokeLineCap.ROUND);
    	//iterate through the route drawing a connection between nodes
    	for(int i = 1; i < route.size(); i ++){
    		gc.setLineWidth(5);
    		
            gc.setStroke(Color.BLACK);
	  		gc.strokeLine(route.get(i-1).getX()+8, route.get(i-1).getY()+8, route.get(i).getX()+8,route.get(i).getY()+8);
            gc.setLineWidth(3);
            
            gc.setStroke(customBlue);
	  		gc.strokeLine(route.get(i-1).getX()+8, route.get(i-1).getY()+8, route.get(i).getX()+8,route.get(i).getY()+8);
	  		
    	}
    }
    
    private LinkedList<Edge> convertEdgeData(LinkedList<EdgeDataConversion> edgeData) {
    	LinkedList<Edge> edgeList = new LinkedList<Edge>();
    	Node fromNode = new Node(0, 0, 0, "", "", false, false, "");
    	Node toNode = new Node(0, 0, 0, "", "", false, false, "");
    	
    	//iterate through the edges 
    	for(int i = 0; i < edgeData.size(); i ++){
    		//System.out.println("Edge Iterator: " + i);
    		//iterate throught he nodelist to find the matching node
    		for(int j = 0; j < nodeList.size(); j ++){
        		//System.out.println("Node Iterator: " + j + ", x valFrom: " + nodeList.get(j).getX() + " =? " + nodeList.get(j).getName());

    			if(edgeListConversion.get(i).getFrom().equals((nodeList.get(j)).getName())){
					fromNode = nodeList.get(j);
				}
				if(edgeListConversion.get(i).getTo().equals((nodeList.get(j)).getName())){
					toNode = nodeList.get(j);
				}
    			
    		}
    		Edge newEdge = new Edge(fromNode, toNode, edgeListConversion.get(i).getDistance());
			edgeList.add(newEdge);
    	}
    	
    	return edgeList;
    }
    
    public void handleSearchByKeyStart(String oldVal, String newVal) {
    	// If the number of characters in the text box is less than last time
        // it must be because the user pressed delete
        if ( oldVal != null && (newVal.length() < oldVal.length()) ) {
            // Restore the lists original set of entries 
            // and start from the beginning
        	StartList.setItems(LocationOptions);
        }
        StartList.setOpacity(100);
        
 
        // Break out all of the parts of the search text 
        // by splitting on white space
        String[] parts = newVal.toUpperCase().split(" ");
 
        // Filter out the entries that don't contain the entered text
        ObservableList<String> subentries = FXCollections.observableArrayList();
        for ( Object entry: StartList.getItems() ) {
            boolean match = true;
            String entryText = (String)entry;
            for ( String part: parts ) {
                // The entry needs to contain all portions of the
                // search string *but* in any order
                if ( ! entryText.toUpperCase().contains(part) ) {
                    match = false;
                    break;
                }
            }
 
            if ( match ) {
                subentries.add(entryText);
            }
            if(subentries.size() *25 < 75)
            	StartList.setMaxHeight(subentries.size() *25);
            else
            	StartList.setMaxHeight(75);
        }
        StartList.setItems(subentries);
        
        	if(subentries.isEmpty() || subentries.get(0).equals(StartText.getText()))
        		StartList.setOpacity(0);
        
    }
 
    public void handleSearchByKeyDest(String oldVal, String newVal) {
        // If the number of characters in the text box is less than last time
        // it must be because the user pressed delete
        if ( oldVal != null && (newVal.length() < oldVal.length()) ) {
            // Restore the lists original set of entries 
            // and start from the beginning
        	DestList.setItems(LocationOptions);
        }
        DestList.setOpacity(100);
        
 
        // Break out all of the parts of the search text 
        // by splitting on white space
        String[] parts = newVal.toUpperCase().split(" ");
 
        // Filter out the entries that don't contain the entered text
        ObservableList<String> subentries = FXCollections.observableArrayList();
        String entryText = "";
        for ( Object entry: DestList.getItems() ) {
            boolean match = true;
            entryText = (String)entry;
            for ( String part: parts ) {
                // The entry needs to contain all portions of the
                // search string *but* in any order
                if ( ! entryText.toUpperCase().contains(part) ) {
                    match = false;
                    break;
                }
            }
 
            if ( match ) {
                subentries.add(entryText);
            }
            if(subentries.size() *25 < 75)
            	DestList.setMaxHeight(subentries.size() *25);
            else
            	DestList.setMaxHeight(75);
        }
        DestList.setItems(subentries);

        
        	if(subentries.isEmpty() || subentries.get(0).equals(DestText.getText()))
        		DestList.setOpacity(0);
        
    }
    
    private Parent createZoomPane(final Group group) {
	    final double SCALE_DELTA = 1.1;
	    final StackPane zoomPane = new StackPane();
	    final ScrollPane scrollPane = new ScrollPane();

	    zoomPane.getChildren().add(group);
	

	    final Group scrollContent = new Group(zoomPane);
	    scrollPane.setContent(scrollContent);
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
	    scrollPane.setPrefViewportHeight(605);

	    zoomPane.setOnScroll(new EventHandler<ScrollEvent>() {
	      @Override
	      public void handle(ScrollEvent event) {
	        event.consume();

	        if (event.getDeltaY() == 0) {
	          return;
	        }

	        double scaleFactor = (event.getDeltaY() > 0) ? SCALE_DELTA
	            : 1 / SCALE_DELTA;

	        // amount of scrolling in each direction in scrollContent coordinate
	        // units
	        Point2D scrollOffset = figureScrollOffset(scrollContent, scrollPane);

	        group.setScaleX(group.getScaleX() * scaleFactor);
	        group.setScaleY(group.getScaleY() * scaleFactor);

	        // move viewport so that old center remains in the center after the
	        // scaling
	        repositionScroller(scrollContent, scrollPane, scaleFactor, scrollOffset);

	      }
	    });

	    // Panning via drag....
	    final ObjectProperty<Point2D> lastMouseCoordinates = new SimpleObjectProperty<Point2D>();
	    scrollContent.setOnMousePressed(new EventHandler<MouseEvent>() {
	      @Override
	      public void handle(MouseEvent event) {
	        lastMouseCoordinates.set(new Point2D(event.getX(), event.getY()));
	      }
	    });

	    scrollContent.setOnMouseDragged(new EventHandler<MouseEvent>() {
	      @Override
	      public void handle(MouseEvent event) {
	        double deltaX = event.getX() - lastMouseCoordinates.get().getX();
	        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scrollPane.getViewportBounds().getWidth();
	        double deltaH = deltaX * (scrollPane.getHmax() - scrollPane.getHmin()) / extraWidth;
	        double desiredH = scrollPane.getHvalue() - deltaH;
	        scrollPane.setHvalue(Math.max(0, Math.min(scrollPane.getHmax(), desiredH)));

	        double deltaY = event.getY() - lastMouseCoordinates.get().getY();
	        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scrollPane.getViewportBounds().getHeight();
	        double deltaV = deltaY * (scrollPane.getHmax() - scrollPane.getHmin()) / extraHeight;
	        double desiredV = scrollPane.getVvalue() - deltaV;
	        scrollPane.setVvalue(Math.max(0, Math.min(scrollPane.getVmax(), desiredV)));
	      }
	    });

	    return scrollPane;
	}
    
    private void repositionScroller(Group scrollContent, ScrollPane scroller, double scaleFactor, Point2D scrollOffset) {
        double scrollXOffset = scrollOffset.getX();
        double scrollYOffset = scrollOffset.getY();
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        if (extraWidth > 0) {
          double halfWidth = scroller.getViewportBounds().getWidth() / 2 ;
          double newScrollXOffset = (scaleFactor - 1) *  halfWidth + scaleFactor * scrollXOffset;
          scroller.setHvalue(scroller.getHmin() + newScrollXOffset * (scroller.getHmax() - scroller.getHmin()) / extraWidth);
        } else {
          scroller.setHvalue(scroller.getHmin());
        }
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        if (extraHeight > 0) {
          double halfHeight = scroller.getViewportBounds().getHeight() / 2 ;
          double newScrollYOffset = (scaleFactor - 1) * halfHeight + scaleFactor * scrollYOffset;
          scroller.setVvalue(scroller.getVmin() + newScrollYOffset * (scroller.getVmax() - scroller.getVmin()) / extraHeight);
        } else {
          scroller.setHvalue(scroller.getHmin());
        }
      }
    
    private Point2D figureScrollOffset(Group scrollContent, ScrollPane scroller) {
        double extraWidth = scrollContent.getLayoutBounds().getWidth() - scroller.getViewportBounds().getWidth();
        double hScrollProportion = (scroller.getHvalue() - scroller.getHmin()) / (scroller.getHmax() - scroller.getHmin());
        double scrollXOffset = hScrollProportion * Math.max(0, extraWidth);
        double extraHeight = scrollContent.getLayoutBounds().getHeight() - scroller.getViewportBounds().getHeight();
        double vScrollProportion = (scroller.getVvalue() - scroller.getVmin()) / (scroller.getVmax() - scroller.getVmin());
        double scrollYOffset = vScrollProportion * Math.max(0, extraHeight);
        return new Point2D(scrollXOffset, scrollYOffset);
      }
       
}
