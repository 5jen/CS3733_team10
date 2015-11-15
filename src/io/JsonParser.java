package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter; 
import java.io.OutputStreamWriter;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONStringer;
import org.json.JSONTokener;
import org.json.JSONArray;


import org.junit.Test;


public class JsonParser {
    
	@Test
    public void Test() throws JSONException, IOException{
    	//System.out.println(testPrepareJSONObject());
        //System.out.println(testGetJSONContent());
        getJsonContent();
        saveFile(jsonToString(),"testdata\\test2.json");        
    }
	
    private static String TestJSONText = "{\"id\":20130001,\"phone\":\"13579246810\",\"name\":\"Jason\"}"; 
    private static String TestJSONText2 = "[{\"id\":20130001,\"phone\":\"13579246810\",\"name\":\"Jason\"},{\"id\":20130031,\"phone\":\"13579246810\",\"name\":\"Jason\"}]";
    /**
     * Get JSON object from a text file includes array of JSONs
     * @return 
     */
	public static String getJsonContent(){
	    JSONArray json = new JSONArray(loadFile("testdata\\test.json"));//change path right here
	    if (json.length()>0){
	    	for (int i=0;i<json.length();i++){
	    		JSONObject job = json.getJSONObject(i);
	    		int x = job.getInt("valx");
	    		int y = job.getInt("valy");
	    		boolean isWalk = job.getBoolean("isWalk");
	    		System.out.println(isWalk);
	    	}
	    }
	    
	    return "";
	}
    /**
     * Load json file to a string
     * @param path is the path of the json file
     * @return a string contains json info.
     */
    public static String loadFile(String path){
		File file = new File(path);
        BufferedReader reader = null;
        String laststr = "";
        try {
         //System.out.println("Reading one line each time");
         reader = new BufferedReader(new FileReader(file));
         String tempString = null;
         int line = 1;
         //read one line each time until read a empty line
         while ((tempString = reader.readLine()) != null) {
          //show line number 
          System.out.println("line " + line + ": " + tempString);
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
     * Change json info to a string
     * @return
     * @throws JSONException
     */
    public static String jsonToString() throws JSONException {
    	//create json object 
    	JSONObject json = new JSONObject();
    	
    	json.put("valx",100);
    	json.put("valy",209);
    	json.put("place", "ak");
    	JSONArray array = new JSONArray();
    	array.put(json);
    	String j2s = array.toString();
        
    	return j2s;
    }
    
    public static void saveFile(String str,String path) throws IOException{
        FileWriter fo = new FileWriter(path);
        PrintWriter out = new PrintWriter(fo);  
        out.write(str);  
        out.println();  
   
		fo.close();

        out.close();  



    }
    
    private static String testPrepareJSONObject(){  
        JSONStringer jsonStringer = new JSONStringer();  
        try {  
            jsonStringer.object();  
            jsonStringer.key("name");  
            jsonStringer.value("Jason");  
            jsonStringer.key("id");  
            jsonStringer.value(20130001);  
            jsonStringer.key("phone");  
            jsonStringer.value("13579246810");  
            jsonStringer.endObject();  
        } catch (JSONException e) {  
            e.printStackTrace();  
        }  
        return jsonStringer.toString();  
    }    
	
    private static String testGetJSONContent(){  
        JSONTokener jsonTokener = new JSONTokener(TestJSONText);   
        JSONObject studentJSONObject;  
        String name = null;  
        int id = 0;  
        String phone = null;  
        try {  
            studentJSONObject = (JSONObject) jsonTokener.nextValue();  
            name = studentJSONObject.getString("name");  
            id = studentJSONObject.getInt("id");  
            phone = studentJSONObject.getString("phone");
              
        } catch (JSONException e) {  
            e.printStackTrace();  
        }  
        return name + "  " + id + "   " + phone;  
    }  
}
