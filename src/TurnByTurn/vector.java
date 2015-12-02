package TurnByTurn;

/**
 * Created by yx on 12/1/15.
 */

import java.lang.Math;

public class vector {
    private int x;
    private int y;

    vector(int x,int y){
        this.x =x;
        this.y =y;
    }

    public double getCos(){
        System.out.println(x);
        System.out.println(y);

        if ((x==0)&&(y==0)) return 0;
        return (x/(Math.sqrt(x*x+y*y)));
    }

    public double getSin(){
        if ((x==0)&&(y==0)) return 0;
        return (y/(Math.sqrt(x*x+y*y)));
    }

    public int getXPlusDegree(){
        double sin = getSin();
        double cos = getCos();

        System.out.println("COS");
        System.out.println(cos);

        //double abscos = Math.abs(cos);
        //System.out.println(abscos);
        //double abssin = Math.abs(sin);


        /**
        int absdegree = (int) (180*Math.acos(abscos)/(Math.PI));
        if ((sin>=0)&&(cos>=0)) {return absdegree;}
        else if ((sin>=0)&&(cos<=0)) {return (180-absdegree);}
        else if ((sin<=0)&&(cos<=0)) {return (180+absdegree);}
        else if ((sin<=0)&&(cos>=0)) {return (360-absdegree);}
        */

        int degree = (int) (180*Math.acos(cos)/(Math.PI));

        System.out.println(degree);

        if  (sin>=0) return degree;
        else if (sin<0) return 360-degree;


        return -1;
    }
}