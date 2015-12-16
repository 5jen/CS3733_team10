package Calendar;

import javafx.scene.image.ImageView;

/**
 * Created by yx on 12/14/15.
 */
public class MyEvent {
	
    private String summary;
    private String description; 
    private String location; //building name
    private int locationX;
    private int locationY;
    private String startTime;
    private String endTime;
    private String type; //probably dont need but could be useful
    private ImageView icon; //image of the type of image, only needed for final list of events with parsed info

    /**
     * Constructor
     * @param summary
     * @param location
     * @param startTime
     * @param endTime
     */
    public MyEvent(String summary, String description, String location, String startTime, String endTime){
        this.summary = summary;
        this.description = description;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    //Used to create a generic event, added setters and getters so that we can set them 
    //later on as we obtain the info from each file
    //THIS VERSION is that that we can parse that data and create a new list of MyEvents
    //that contains useful information
    public MyEvent(){
        
    }

    public String getSummary(){
        return this.summary;
    }
    public String setSummary(String s){
        return this.summary = s;
    }
    
    public String getDescription(){
        return this.description;
    }
    public String setDescription(String s){
        return this.description = s;
    }

    public String getLocation(){
        return this.location;
    }
    public String setLocation(String s){
        return this.location = s;
    }

    public String getStartTime(){
        return this.startTime;
    }
    public String setStartTime(String s){
        return this.startTime = s;
    }

    public String getEndTime(){
        return this.endTime;
    }
    public String setEndTime(String s){
        return this.endTime = s;
    }
    
    public String getType(){
        return this.type;
    }
    public String setType(String s){
        return this.type = s;
    }
    
    public ImageView getIcon(){
        return this.icon;
    }
    public ImageView setIcon(ImageView pic){
        return this.icon = pic;
    }
    
    public int getlocationX(){
    	return this.locationX;
    }
    public int setlocationX(int i){
    	return this.locationX = i;
    }
    
    public int getlocationY(){
    	return this.locationY;
    }
    public int setlocationY(int i){
    	return this.locationY = i;
    }
    
}
