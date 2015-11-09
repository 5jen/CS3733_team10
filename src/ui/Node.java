package ui;

import java.io.File;
import java.util.LinkedList;

import javafx.application.Application;
import javafx.event.ActionEvent;
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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Node {
	int X;
	int Y;
	String name;
	Image nodeimg;
	LinkedList<Node> edges;
	Button button;
	
	
	/* Node - basic indicator of a location in which the user can travel to or walk through
	 * X - x coordinate of it's location
	 * Y - y coordinate of it's location
	 * name - name of the location
	 * nodeimg - image of the node - same of all
	 * edges - list of places the user can directly get to from this location
	 */
	public Node(int x, int y, String n){
		this.X = x;
		this.Y = y;
		this.name = n;
		
		//create button as node graphic
		File file = new File("/Users/andrewrottier/Documents/CS_3733_team10/CS3733_Graphics/node.png"); //15x17pixels
        Image node = new Image(file.toURI().toString());
        this.button = new Button();
		this.button.setGraphic(new ImageView(node));

		this.nodeimg = node;
		this.edges = new LinkedList<Node>(); //must create other nodes in order to add edges here
	}
	
	
	/* findRoute - finds the route between this and the give node
	 * dest - the place we want to travel to from this node
	 * path - the list of nodes you will have to cross in order to get to your destination
	 * return path
	 */
	
	// return a route (as a LinkedList) from this node to the "to" node
	public LinkedList<Node> findRoute(Node dest, LinkedList<Node> visited){
	    if (this.equals(dest)){
	      LinkedList<Node> result = new LinkedList<Node>();
	      result.add(this);
	      return result;
	    }
	    //check to see if previously visited, if so, we're looping, so skip
	    else if (visited.contains(this))
	      return new LinkedList<Node>();      
	    else{
	      visited.add(this);
	      for (Node n:this.edges){
	        LinkedList<Node> nRoute = n.findRoute(dest, visited);
	        if (nRoute.size()>0){
	          nRoute.addFirst(this);
	          return nRoute;
	        }
	      }
	      return new LinkedList<Node>();
	    }
	  }
	
}
	
	//LinkedList<Node> findRoute(Node dest, LinkedList<Node> path) {
		//LinkedList<Node> route = new LinkedList<Node>();
		//return new LinkedList<Node>().add(places.daka);
		
		//NEEDS TWEAKING
		/*
		if (this.equals(dest)){
			path.add(dest);//add final node to path
			return path;
		}
		else {
			for (Node currentNode : this.edges) {
				//if we find the destination node
		        if (currentNode.edges.contains(dest)) {
		        	path.add(currentNode);//find 
		        	return path;
		        }
		        //otherwise, recursively call with currentNode
		        else {
		        	currentNode.findRoute(dest, path);
		        }
		      }
		return path; //couldnt find path
		}*/
	//
	

