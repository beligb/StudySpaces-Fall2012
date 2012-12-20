package edu.upenn.cis573.test;

import java.util.ArrayList;

import org.junit.Before;

import edu.upenn.cis573.Building;
import edu.upenn.cis573.Room;
import edu.upenn.cis573.StudySpace;
import junit.framework.TestCase;

public class BuildingTest extends TestCase {

	Building building;
	ArrayList<StudySpace> list;
	
	@Before
	public void setUp() {
		String name = "GSR"; double lat = 0.0; double lon = 0.0;
		int num_rooms = 2; String b_name = "Jon M. Huntsman Hall";
		int max_occ = 10; boolean has_wh = true; String pri = "P";
		boolean has_comp = true; String res_type = ""; boolean has_big_s = true;
		String comm = ""; Room[] r = null;
		
		StudySpace hhall = new StudySpace(name, lat, lon, num_rooms, b_name, max_occ, has_wh,
										pri, has_comp, res_type, has_big_s, comm, r);
		list = new ArrayList<StudySpace>();
		list.add(hhall);
		building = new Building(list);
	}
	
	public void testGetSpaceLatitude() {
		assertEquals(0, building.getSpaceLatitude(), 1e-8);
	}

	public void testGetSpaceLongitude() {
		assertEquals(0, building.getSpaceLongitude(), 1e-8);
	}

	public void testGetBuildingName() {
		assertEquals("Jon M. Huntsman Hall", building.getBuildingName());
	}

	public void testGetRoomCount() {
		assertEquals(1, building.getRoomCount());
	}

	public void testGetRooms() {
		assertEquals(list, building.getRooms());
	}

}
