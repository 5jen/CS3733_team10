package TurnByTurn;


import node.AbsNode;

import java.util.LinkedList;
import java.lang.Math;

/**
 * Created by yx on 11/20/15.
 */
public class stepIndicator {

    private LinkedList<AbsNode> route;

    /**
     * Constructor
     * @param route
     */
    public stepIndicator(LinkedList<AbsNode> route){
        route = this.route;
    }


    public LinkedList<String> lInstructions(){

        //Current static indicator;
        LinkedList<String> result = new LinkedList<>();
        result.addFirst("Strating nevigation.");

        int i = 1;
        while (i<route.size()-1){
            int angle = getAngleInDegree(route.get(i-1),route.get(i),route.get(i+1));

            String name = route.get(i).getName();
            String message;

            //TODO Add distance between points

            if ((0<angle) && (angle<45)) {message = "Turn sharp right";}
            else if ((45<angle) && (angle<120)) {message = "Turn right";}
            else if ((120<angle) && (angle<160)) {message = "Turn slight right";}
            else {message = "Keep straight";}


            result.addLast(message);
            /**
             else if (i == route.size()-1) {
             message = "You are reaching your destination after ";


             }*/
        }

        // Print out the result
        for (i=0;i<route.size();i++){
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
    public int getAngleInDegree(AbsNode a,AbsNode b,AbsNode c){
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

        int dot_product = (ba_x * ba_y) + (bc_x * bc_y);
        int absMA = (int) Math.sqrt((ba_x*ba_x)+(ba_y*ba_y));
        int absMB = (int) Math.sqrt((bc_x*bc_x)+(bc_y*bc_y));

        double cos = dot_product/(absMA*absMB);
        int result = (int) (180*Math.acos(cos)/(Math.PI));
        return result;
    }



}
