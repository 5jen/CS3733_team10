package Calendar;

import java.io.File;
import java.util.LinkedList;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EventMatcher {
	
	private ImageView icon;
	//private String eventType;
	private LinkedList<String> words;
	
	public EventMatcher(String e, LinkedList<String> w){
		
		//**** THIS IS IMPORTANT WHAT YOU PASS THE CONSTRUCTOR ON THE OTHER SIDE
		//Either match word exactly or truncate off white space as well
		//*** ADD OTHER TYPES LATER, ONCE WE GET THIS WORKING***
		if(e.equalsIgnoreCase("Food")){
			File eventFile = new File("CS3733_Graphics/DirectionImages/2.png"); //CHange to real images later
	        Image eventImagePic = new Image(eventFile.toURI().toString());
	        ImageView eventImage = new ImageView(eventImagePic);
	        eventImage.setFitHeight(60); eventImage.setFitWidth(60);
	        //DO relocation on GPS
	        //keyImageButton.relocate(60, 700 + stageInitialHeightDifference);
			icon = eventImage;		
		}
		else if(e.equalsIgnoreCase("WPI")){
			File eventFile = new File("CS3733_Graphics/DirectionImages/1.png");
	        Image eventImagePic = new Image(eventFile.toURI().toString());
	        ImageView eventImage = new ImageView(eventImagePic);
	        eventImage.setFitHeight(60); eventImage.setFitWidth(60);
	        //DO relocation on GPS
	        //keyImageButton.relocate(60, 700 + stageInitialHeightDifference);
			icon = eventImage;	
		}
		//ELSE, JUST ATTACH A GOMPEI HEAD IF WE CANT ID THE EVENT
		else{
			File eventFile = new File("CS3733_Graphics/DirectionImages/1.png");
	        Image eventImagePic = new Image(eventFile.toURI().toString());
	        ImageView eventImage = new ImageView(eventImagePic);
	        eventImage.setFitHeight(60); eventImage.setFitWidth(60);
	        //DO relocation on GPS
	        //keyImageButton.relocate(60, 700 + stageInitialHeightDifference);
			icon = eventImage;
		}
		
		this.words = w;
	}

}
