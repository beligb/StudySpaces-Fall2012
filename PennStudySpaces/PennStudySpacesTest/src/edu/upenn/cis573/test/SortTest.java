package edu.upenn.cis573.test;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;

import edu.upenn.cis573.Room;
import edu.upenn.cis573.SearchActivity;
import edu.upenn.cis573.StudySpace;
import edu.upenn.cis573.StudySpaceListActivity;


public class SortTest extends
		ActivityInstrumentationTestCase2<StudySpaceListActivity> {

	public SortTest() {
		super("edu.upenn.cis573", StudySpaceListActivity.class);
	}

	@Before
	protected void setUp() throws Exception {
		super.setUp();
		
		StudySpaceListActivity _listActivity = getActivity();
	}
	
	//@Test
	public void testSortSameDistance() {
		
		StudySpaceListActivity _listActivity = getActivity();
		
		ArrayList<StudySpace> list = new ArrayList<StudySpace>();
		
		Room[] r = new Room[5]; 
		StudySpace test1 = new StudySpace("Towne",0.0,0.0, 10,
					"TowneStudyRoom",90,true,"yes",true,"no",false,"a big room",r);
		
		StudySpace test2 = new StudySpace("Moor",0.0,0.0, 10,
				"MoorStudyRoom",90,true,"yes",true,"no",false,"a big room",r);
		
		list.add(test1);
		list.add(test2);
		
		SearchActivity.setLongitude(0.0);
		SearchActivity.setLatitude(0.0);
		
		list = _listActivity.sortByDistance(list);
		
		//test size of the list
		assertEquals(2,list.size());
		for(int i = 0; i < 2; i++){
			assertTrue(test1.getBuildingName() == list.get(i).getBuildingName() ||
					test2.getBuildingName() == list.get(i).getBuildingName());
			
		}
	}

}
