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
        //previous step id
        int pstep_id = -1;
        //TODO add starting case

        int i = 1;
        while (i<route.size()- 1){
           // int angle = getAngleInDegree(route.get(i-1),route.get(i),route.get(i+1));
            String message;
            String maneuver;
            //the type for a node
            String type = route.get(i).getType();
            String name = route.get(i).getName();
            //System.out.println(name);
            int x1;//previous x
            int y1;//previous y
            int x2;//current x
            int y2;//current y
            int x3;//next x
            int y3;//next y
            int globalx1;//previous globalx
            int globaly1;//previous globaly
            int globalx2;//current globalx
            int globaly2;//current globaly
            int globalx3;//next globalx
            int globaly3;//next globaly


            int icon_id=0;//the icon_id for the instruction

            x1 = route.get(i-1).getX();
            y1 = route.get(i-1).getY();
            x2 = route.get(i).getX();
            y2 = route.get(i).getY();
            x3 = route.get(i+1).getX();
            y3 = route.get(i+1).getY();
            
            globalx1 = route.get(i-1).getGlobalX();
            globaly1 = route.get(i-1).getGlobalY();
            globalx2 = route.get(i).getGlobalX();
            globaly2 = route.get(i).getGlobalY();
            globalx3 = route.get(i+1).getGlobalX();
            globaly3 = route.get(i+1).getGlobalY();

            int dis = getDistance(globalx1,globalx2,globaly1,globaly2);

            // Transition point between maps
            if ((type.compareTo("Transition Point")==0) &&
                    (route.get(i+1).getType().compareTo("Transition Point") == 0)){
                i++;//skip transition points in pairs
                String map = route.get(i).getFloorMap();
                //TODO convert map name to actual name
                maneuver = "Go to ";//need to add building floor name
                icon_id = 5;
                message = maneuver + map;
            }
            else {
                if ((type.compareTo("Staircase") == 0) &&
                        (route.get(i+1).getType().compareTo("Staircase") == 0)){
                    int z1 = route.get(i-1).getZ();
                    int z2 = route.get(i+1).getZ();

                    while ((i<route.size()-1) && (route.get(i+1).getType().compareTo("Staircase")==0)){
                        i++;
                    }

                    if (z2>z1) {maneuver = "up stair"; icon_id=1;}
                    else {maneuver = "down stair";icon_id =2;}
                    message = "Go "+ maneuver;
                }
                else {

                    message = generateMessage(x1,y1,x2,y2,x3,y3);

                    if (message.compareTo("Keep straight for") == 0) {icon_id = 0;}
                    else if (message.compareTo("Turn left in") == 0) {icon_id = 3;}
                    else if (message.compareTo("Turn right in") == 0) {icon_id = 4;}
                    else if (message.compareTo("Turn sharp left in") == 0) {icon_id = 33;}
                    else if (message.compareTo("Turn sharp right in") == 0) {icon_id =44;}
                    else if (message.compareTo("Turn slight left in") ==0) {icon_id = 39;}
                    else if (message.compareTo("Turn slight right in") == 0) {icon_id = 52;}
                    else if (message.compareTo("Make a U turn in") == 0){icon_id = 6;}
                }

            }

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
             *  6                  U turn
             *  7                  starting route
             *  8                  reach destination
             */

             //check duplicate go straight
             if ((icon_id==0) && (icon_id==pstep_id)) {
                 result.getLast().updateDistance(getDistanceInFeet(2.6053,dis));
             }
             else {
                 result.addLast(new Step(icon_id, message, getDistanceInFeet(2.6053,dis), route.get(i).getX(), route.get(i).getY()));
             }
             pstep_id = icon_id;

             i++;
        }

        //result.addFirst(new Step(0,"Walk Straight",0));
        //result.addFirst(new Step(7,"Starting navigation",0));
        //result.addLast(new Step(8,"You have reached your destination",0));

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
        //System.out.println(result);
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
     * get distance in pixels for two given points
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @return distance in pixels
     */
    public int getDistance(int x1, int x2, int y1, int y2){
        int dx = Math.abs(x2-x1);
        int dy = Math.abs(y2-y1);
        return (int)Math.sqrt(dx*dx+dy*dy);
    }
    /**
     * Return the actual distance when given the scaling of the map
     * @param scale is the scaling of the map
     * @param distance is the # of pixle calculated by nodes
     * @return ditance in feet
     */
    public double getDistanceInFeet(double scale,int distance){
        return distance*scale;
    }



    public String generateMessage(int x1, int y1, int x2, int y2, int x3, int y3){
        vector ab = new vector(x2-x1,y1-y2);
        vector bc = new vector(x3-x2,y2-y3);

        int angleDifference = bc.getXPlusDegree()-ab.getXPlusDegree();

        if (angleDifference<0) angleDifference+=360;

       // System.out.println(angleDifference);


        if ((angleDifference > 20)&&(angleDifference <= 45)) return "Turn slight left in";
        else if ((angleDifference >45) && (angleDifference<=120)) return "Turn left in";
        else if ((angleDifference>120) && (angleDifference<=160)) return "Turn sharp left in";
        else if ((angleDifference>160) && (angleDifference<=200)) return "Make a U turn in";
        else if ((angleDifference>200) && (angleDifference<=240)) return "Turn sharp right in";
        else if ((angleDifference>240) && (angleDifference<=315)) return "Turn right in";
        else if ((angleDifference>315) && (angleDifference<=340)) return "Turn slight right in";
        else return "Keep straight for";

    }

}