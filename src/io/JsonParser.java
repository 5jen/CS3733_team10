package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import node.*;

import org.junit.Test;


public class JsonParser {
	
	public JsonParser(){
		
	}

	/**
     * Get JSON object from a text file includes array of JSONs (for nodes and places!)
     * @return 
     */
	public static LinkedList<Node> getJsonContent(String path){
	    JSONArray json = new JSONArray(loadFile(path));//change path right here
	    LinkedList<Node> nodeList = new LinkedList<Node>();
	    
	    if (json.length()>0){
	    	for (int i=0;i<json.length();i++){
	    		JSONObject job = json.getJSONObject(i);
	    		int x = job.getInt("valx");
	    		int y = job.getInt("valy");
	    		int z = job.getInt("valz");
	    		int globalX = job.getInt("globalX");
	    		int globalY = job.getInt("globalY");
	    		String name = job.getString("name");
	    		String building = job.getString("building");
				String floor = job.getString("floorMap");
	    		boolean isWalk = job.getBoolean("isWalkable");
	    		boolean isPlace = job.getBoolean("isPlace");
	    		String type = job.getString("type");
	    		
	    		Node newNode = new Node(x, y, z, name, building, floor, isWalk, isPlace, type);
				newNode.setGlobalX(globalX);
				newNode.setGlobalY(globalY);
	    		nodeList.add(newNode);
	    	}
	    }
	    
	    return nodeList;
	}
	
	/**
     * Get JSON object from a text file includes array of JSONs (for edges!)
     * @return 
     */
	public static LinkedList<EdgeDataConversion> getJsonContentEdge(String path){
	    JSONArray json = new JSONArray(loadFile(path));//change path right here
	    LinkedList<EdgeDataConversion> edgeList = new LinkedList<EdgeDataConversion>();
	    
	    if (json.length()>0){
	    	for (int i=0;i<json.length();i++){
	    		JSONObject job = json.getJSONObject(i);
	    		String from = job.getString("from");
	    		String to = job.getString("to");
	    		int dist = job.getInt("distance");
	    		
	    		EdgeDataConversion newEdge = new EdgeDataConversion(from, to, dist);
				edgeList.add(newEdge);
	    	}
	    }
	    
	    return edgeList;
	}
	
    /**
     * Load json file to a string
     * @param path is the path of the json file
     * @return a string contains json info.
     */
    private static String loadFile(String path){
		File file = new File(path);
        BufferedReader reader = null;
        String laststr = "";
        try {
        	////System.out.println("Reading one line each time");
        	reader = new BufferedReader(new FileReader(file));
        	String tempString = null;
        	int line = 1;
        	//read one line each time until read a empty line
        	while ((tempString = reader.readLine()) != null) {
        		//show line number 
        		//System.out.println("line " + line + ": " + tempString);
        		laststr = laststr + tempString;
        		line ++;
        	}
        	reader.close();
        } catch (IOException e) {
        	e.printStackTrace();
        } finally {
        	if (reader != null) {
        		try {
        			reader.close();
        		} catch (IOException e1) {
        		}
        	}
        }
        return laststr;
	}
    
    
    
    /**
     * Change json info to a string (for nodes and places!)
     * @return
     * @throws JSONException
     */
    //Field order: valx, valy, name, isWalkable, isPlace
    public static String jsonToString(LinkedList<Node> nodeList) throws JSONException {

    	JSONArray array = new JSONArray();
    	for(int i = 0; i < nodeList.size(); i++){
    		JSONObject json = new JSONObject();
    		json.put("valx", nodeList.get(i).getX());
        	json.put("valy", nodeList.get(i).getY());
        	json.put("valz", nodeList.get(i).getZ());
        	json.put("globalX", nodeList.get(i).getGlobalX());
        	json.put("globalY", nodeList.get(i).getGlobalY());
        	json.put("name", nodeList.get(i).getName());
        	json.put("building", nodeList.get(i).getBuilding());
            json.put("floorMap", nodeList.get(i).getFloorMap());
        	json.put("isWalkable", nodeList.get(i).getIsWalkable());
        	json.put("isPlace", nodeList.get(i).getIsPlace());
        	json.put("type", nodeList.get(i).getType());
        	array.put(json);
    	}


		return array.toString(1);
    }
    
    /**
     * Change json info to a string (for edges)
     * @return
     * @throws JSONException
     */
    //Field order: valx, valy, name, isWalkable, isPlace
    public static String jsonToStringEdge(LinkedList<Edge> edgeList) throws JSONException {

    	JSONArray array = new JSONArray();
    	for(int i = 0; i < edgeList.size(); i++){
    		JSONObject json = new JSONObject();
    		json.put("from", edgeList.get(i).getFrom().getName());
    		json.put("to", edgeList.get(i).getTo().getName());
        	json.put("distance", edgeList.get(i).getDistance());
        	array.put(json);
    	}

		return array.toString();
    }
    
    public static void saveFile(String str, String path) throws IOException{
        FileWriter fo = new FileWriter(path);
        PrintWriter out = new PrintWriter(fo);  
        out.write(str);  
        out.println();  
   
		fo.close();
        out.close();  

    }  
}
