package TurnByTurn;

/**
 * Created by yx on 11/30/15.
 */
public class Step {
    private String message;
    private int icon_id;
    private double distance;
    private int X;
    private int Y;

    public Step(int id, String message, double distance, int x, int y){
        this.icon_id = id;
        this.distance = distance;
        this.message = message;
        this.X = x;
        this.Y = y;
    }

    public int getIconID(){
        return this.icon_id;
    }

    public String getMessage(){
        formatMessage();
        return message;
    }

    public double getDistance(){
        return this.distance;
    }

    public void updateDistance(double v){
        this.distance = this.distance+v;
    }
    
    public int getX(){
    	return this.X;
    }
    
    public int getY(){
    	return this.Y;
    }

    public void formatMessage(){
        int mLength = message.length();
        String distance;
        if (this.distance == 0) {
        	distance = "";
        } else 
        	distance = ((int) this.distance)+" ft";
        int dLength = distance.length();
        int nSpace  = 26- mLength- dLength;
        for (int i=0;i<nSpace;i++){
            message +=" ";
        }
        message += distance;
    }

}