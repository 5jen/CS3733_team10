package node;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by felicemancini on 11/29/15.
 */
public class BuildingTest {

    @Test
    public void testAddMap() throws Exception {
        Map map =  new Map("This is a map", "", "", "", 0, 0 , 0, 0,0);
        Building building = new Building("Building");
        building.addMap(map);

        assertEquals("Building", map.getBuildingName());
    }
}