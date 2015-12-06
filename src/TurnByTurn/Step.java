package TurnByTurn;

/**
 * The Step class represents each step of Instructions
 * Created by yx on 11/30/15.
 */
public class Step {
    private String message;
    private int icon_id;
    private double distance;

    /**
     * The constructor
     * @param id is the icon id (please reference icon id for different types of instruction icons)
     * @param message is the instruction in text format
     * @param distance is the distance to tell user before executing this instruction
     */
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

    /**
     * Change the distance value of the step class
     * @param d is the distance need to be added
     */
    public void updateDistance(double d){
        this.distance += d;
    }

}