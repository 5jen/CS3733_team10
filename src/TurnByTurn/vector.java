package TurnByTurn;

/**
 * The vector class for calculating turn by turn instructions
 * Created by yx on 12/1/15.
 */

import java.lang.Math;

public class vector {
    private int x;
    private int y;

    /**
     * Constructor
     * @param x
     * @param y
     */
    vector(int x,int y){
        this.x =x;
        this.y =y;
    }

    /**
     * The cosine value calculated by x and y
     * @return a double represents the cos value
     */
    public double getCos(){
        //System.out.println(x);
        //System.out.println(y);

        if ((x==0)&&(y==0)) return 0;
        return (x/(Math.sqrt(x*x+y*y)));
    }

    /**
     * The sine value of this vector
     * @return
     */
    public double getSin(){
        if ((x==0)&&(y==0)) return 0;
        return (y/(Math.sqrt(x*x+y*y)));
    }

    /**
     * The angle between the vector and x=axis
     * @return the anle in degrees
     */
    public int getXPlusDegree(){
        double sin = getSin();
        double cos = getCos();

        int degree = (int) (180*Math.acos(cos)/(Math.PI));

        //System.out.println(degree);

        if  (sin>=0) return degree;
        else return 360-degree;
    }
}