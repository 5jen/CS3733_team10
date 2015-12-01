
package TurnByTurn;

import node.Node;

import java.util.LinkedList;
import java.lang.Math;

/**
 * Created by yx on 11/20/15.
 */
public class stepIndicator {

    private LinkedList<Node> route;

    /**
     * Constructor
     * @param route
     */
    public stepIndicator(LinkedList<Node> route){
        this.route = route;
    }


    public LinkedList<Step> lInstructions(){

        //Current static indicator;
        LinkedList<Step> result = new LinkedList<>();
        //TODO add starting case

        int i = 1;
        while (i<route.size()- 1){
            int angle = getAngleInDegree(route.get(i-1),route.get(i),route.get(i+1));
            String message;
            String maneuver;
            //the type for a node
            String type = route.get(i).getType();

            int x1;//previous x
            int y1;//previous y
            int x2;//current x
            int y2;//current y
            int x3;//next x
            int y3;//next y

            int icon_id=0;//the icon_id for the instruction

            x1 = route.get(i-1).getX();
            y1 = route.get(i-1).getY();
            x2 = route.get(i).getX();
            y2 = route.get(i).getY();
            x3 = route.get(i+1).getX();
            y3 = route.get(i+1).getY();
            // Transition point between maps
            if (type.compareTo("Transition Point")==0) {
                i++;//skip transition points in pairs
                String map = route.get(i).getFloorMap();
                //TODO convert map name to actual name
                maneuver = "Go to ";//need to add building floor name
                icon_id = 5;
                message = maneuver + map;
            }
            else {
                if (type.compareTo("Staircase") == 0){
                    int z1 = route.get(i-1).getZ();
                    int z2 = route.get(i+1).getZ();
                    i++;//skip staircase in pairs
                    if (z1>z2) {maneuver = "up stair"; icon_id=1;}
                    else {maneuver = "down stair";icon_id =2;}
                    message = "Go "+ maneuver;
                }
                else {
                    if (getTurnDirection(x1,y1,x2,y2,x3,y3) == 1 ) {
                        maneuver = "left";
                        icon_id = 3;
                    }
                    else if (getTurnDirection(x1,y1,x2,y2,x3,y3) == -1 ){
                        maneuver = "right";
                        icon_id = 4;
                    }
                    else maneuver = "straight";
                    // Determine the strength
                    if ((0 < angle) && (angle < 45)) {
                        maneuver = "sharp "+ maneuver;
                        icon_id = icon_id*11;
                    }
                    else if ((120 < angle) && (angle < 160)) {
                        maneuver = "slight "+ maneuver;
                        icon_id = icon_id*13;
                    }
                    else {
                        maneuver = "straight";
                    }
                    if (maneuver.compareTo("straight")==0) message = "Keep "+maneuver;
                    else message = "Turn "+maneuver;
                }
            }
            /** IGNOREIGNOREIGNOREIGNOREIGNOREIGNOREIGNOREIGNOREIGNOREIGNOREIGNOREIGNORE
            if (getTurnDirection(x1,y1,x2,y2,x3,y3)==1) {
                if ((0 < angle) && (angle < 45)) {
                    message = "Turn sharp right";
                } else if ((45 < angle) && (angle < 120)) {
                    message = "Turn right";
                } else if ((120 < angle) && (angle < 160)) {
                    message = "Turn slight right";
                } else {
                    message = "Keep straight";
                }
            }
            else if (getTurnDirection(x1,y1,x2,y2,x3,y3)==-1){
                if ((0 < angle) && (angle < 45)) {
                    message = "Turn sharp left";
                } else if ((45 < angle) && (angle < 120)) {
                    message = "Turn left";
                } else if ((120 < angle) && (angle < 160)) {
                    message = "Turn slight left";
                } else {
                    message = "Keep straight";
                }
            }
             IGNOREIGNOREIGNOREIGNOREIGNOREIGNOREIGNOREIGNOREIGNORE*/
            //TODO create a class for instructions
            /**
             * icon_id               icon
             *  1                  up_stair
             *  2                  down_stair
             *  3                  turn left
             *  4                  turn right
             *  33                 sharp left
             *  44                 sharp right
             *  39                 slight left
             *  52                 slight right
             *  0                  straight
             *  5                  switch map(for transition point)
             */

             result.addLast(new Step(icon_id,message,getDistanceInFeet(1,1)));

             i++;
        }

        return result;
    }

    /**
     * Get angle in degree from 3 given point
     * @param a is the previous node
     * @param b is the current node
     * @param c is the next node
     * @return the angle in degree for node b
     */
    public int getAngleInDegree(Node a,Node b,Node c){
        int x1;//previous x
        int y1;//previous y
        int x2;//current x
        int y2;//current y
        int x3;//next x
        int y3;//next y

        x1 = a.getX();
        y1 = a.getY();
        x2 = b.getX();
        y2 = b.getY();
        x3 = c.getX();
        y3 = c.getY();

        int ba_x = x1 - x2;
        int ba_y = y1 - y2;
        int bc_x = x3 - x2;
        int bc_y = y3 - y2;

        int dot_product = (ba_x * bc_x) + (ba_y * bc_y);
        if (false) {System.out.println(dot_product);}

        double absMA =  Math.sqrt((ba_x*ba_x)+(ba_y*ba_y));
        double absMB =  Math.sqrt((bc_x*bc_x)+(bc_y*bc_y));

        double cos = dot_product/(absMA*absMB);

        return (int) (180*Math.acos(cos)/(Math.PI));
    }

    /**
     * Get slope of the line between two node
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return the slope of the line connecting these two points
     */
    public double getSlope(int x1,int y1, int x2, int y2){
        double result;
        result = (double)(y2-y1)/(x2-x1);
        System.out.println(result);
        return result;
    }

    public double getB(int x, int y,int slope){
        double result;
        result = y-(slope*x);
        return result;
    }

    /**
     * determine turn left/right/straight
     * -1 left 1 right 0 straight
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @return
     */
    public int getTurnDirection(int x1,int y1,int x2,int y2,int x3,int y3){
        int result;
        double tempSlope1;
        double tempSlope2;
        tempSlope1 = getSlope(x1,y1,x3,y3);
        tempSlope2 = getSlope(x1,y1,x2,y2);
        if (x2==x1) {

            if (((x3 - x2 > 0) && (y2-y1 > 0))||((x3 - x2 < 0)&&(y2-y1<0))) {
                result = -1;
            }
            else result = 1;

        }
        else if (x2>x1) {
            if (tempSlope1<tempSlope2) return -1;
            else if (tempSlope1==tempSlope2) return 0;
            else return 1;
        }
        else {
            if (tempSlope1<tempSlope2) return -1;
            else if (tempSlope1==tempSlope2) return 0;
            else return 1;
        }
        return result;
    }

    /**
     * Return the actual distance when given the scaling of the map
     * @param scale is the scaling of the map
     * @param px is the # of pixle calculated by nodes
     * @return
     */
    public int getDistanceInFeet(int scale,int px){
        return px*scale;
    }




}