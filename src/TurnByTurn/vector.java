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
        if ((x==0)&&(y==0)) return 0;
        return (x/(x*x+y*y));
    }

    public double getSin(){
        if ((x==0)&&(y==0)) return 0;
        return (y/(x*x+y*y));
    }

    public int getXPlusDegree(){
        double sin = getSin();
        double cos = getCos();

        double abscos = Math.abs(cos);
        //double abssin = Math.abs(sin);

        int absdegree = (int) (180*Math.acos(abscos)/(Math.PI));

        if ((sin>=0)&&(cos>=0)) {return absdegree;}
        else if ((sin>=0)&&(cos<=0)) {return (180-absdegree);}
        else if ((sin<=0)&&(cos<=0)) {return (180+absdegree);}
        else if ((sin<=0)&&(cos>=0)) {return (360-absdegree);}

        return -1;
    }
}