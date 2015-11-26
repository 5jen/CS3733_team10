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


    public LinkedList<String> lInstructions(){

        //Current static indicator;
        LinkedList<String> result = new LinkedList<>();
        result.addFirst("Strating nevigation.");

        int i = 1;
        while (i<route.size()-1){
            int angle = getAngleInDegree(route.get(i-1),route.get(i),route.get(i+1));
            //System.out.println(angle);
            //String name = route.get(i).getName();
            String message;

            int x1;//previous x
            int y1;//previous y
            int x2;//current x
            int y2;//current y
            int x3;//next x
            int y3;//next y

            x1 = route.get(i-1).getX();
            y1 = route.get(i-1).getY();
            x2 = route.get(i).getX();
            y2 = route.get(i).getY();
            x3 = route.get(i+1).getX();
            y3 = route.get(i+1).getY();


            //TODO Add distance between points and left turn case
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
            //TODO revise this
            else message = "Go straight";

            result.addLast(message);
            i++;
            /**
             else if (i == route.size()-1) {
             message = "You are reaching your destination after ";


             }*/
        }

        // Print out the result
        for (i=0;i<route.size()-1;i++){
            System.out.println(result.get(i));
        }

        return result;
    }

    public String nextStep(int px, int py, int cx, int cy){
        return "";

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


        if (false) {
            System.out.println(x1);
            System.out.println(y1);
            System.out.println(x2);
            System.out.println(y2);
        }

        int ba_x = x1 - x2;
        int ba_y = y1 - y2;
        int bc_x = x3 - x2;
        int bc_y = y3 - y2;

        int dot_product = (ba_x * bc_x) + (ba_y * bc_y);
        if (false) {System.out.println(dot_product);}

        double absMA =  Math.sqrt((ba_x*ba_x)+(ba_y*ba_y));
        double absMB =  Math.sqrt((bc_x*bc_x)+(bc_y*bc_y));

        double cos = dot_product/(absMA*absMB);

        if (false) {System.out.println(cos);}
        int result = (int) (180*Math.acos(cos)/(Math.PI));
        return result;
    }

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
        int result = 0 ;
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

}
