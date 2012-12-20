package edu.upenn.cis573.test;


import java.util.ArrayList;

import org.junit.Before;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import edu.upenn.cis573.CustomBuildingMap;
import edu.upenn.cis573.Room;
import edu.upenn.cis573.StudySpace;



public class CustomBuildingMapTest extends 
	ActivityInstrumentationTestCase2<CustomBuildingMap> {

	public CustomBuildingMapTest() {
		super(CustomBuildingMap.class);
	}
	
	CustomBuildingMap activity;

	@Before
	public void setUp() throws Exception {
		super.setUp();
		
		String name = "GSR"; double lat = 0.0; double lon = 0.0;
		int num_rooms = 2; String b_name = "Jon M. Huntsman Hall";
		int max_occ = 10; boolean has_wh = true; String pri = "P";
		boolean has_comp = true; String res_type = ""; boolean has_big_s = true;
		String comm = ""; Room[] r = null;
		
		StudySpace hhall = new StudySpace(name, lat, lon, num_rooms, b_name, max_occ, has_wh,
										pri, has_comp, res_type, has_big_s, comm, r);

		
		Intent i = new Intent();
		ArrayList<StudySpace> list = new ArrayList<StudySpace>();
		list.add(hhall);
		i.putExtra("STUDYSPACELIST", list);
		setActivityIntent(i);
	}
	
	public void testCreate() {
		activity = getActivity();
	}

}
