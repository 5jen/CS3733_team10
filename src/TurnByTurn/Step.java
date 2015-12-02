package TurnByTurn;

/**
 * Created by yx on 11/30/15.
 */
public class Step {
    private String message;
    private int icon_id;
    private double distance;
    //TODO need type?

    Step(int id, String message, double distance){
        this.icon_id = id;
        this.distance = distance;
        this.message = message;
    }

    public int getIconID(){
        return this.icon_id;
    }

    public String getMessage(){
        return this.message;
    }

    public double getDistance(){
        return this.distance;
    }

    public void updateDistance(double v){
        this.distance = this.distance+v;
    }

}