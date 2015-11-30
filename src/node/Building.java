package node;

import java.util.LinkedList;

/**
 * Created by felicemancini on 11/29/15.
 */
public class Building {
    private String mName;
    private LinkedList<Map> mMaps = new LinkedList<>();

    public Building(String name){
        mName = name;
    }

    public String getName() {
        return mName;
    }

    public LinkedList<Map> getMaps() {
        return mMaps;
    }

    public void addMap(Map map){
        map.setBuildingName(mName);
        mMaps.add(map);
    }

    public int getNumMaps(){
        return mMaps.size();
    }

}
