package edu.upenn.cis573.test;

import java.util.ArrayList;

import org.junit.Before;

import android.test.ActivityInstrumentationTestCase2;

import edu.upenn.cis573.Room;
import edu.upenn.cis573.SearchActivity;
import edu.upenn.cis573.StudySpace;
import edu.upenn.cis573.StudySpaceListActivity;


public class StudySpaceListActivityTest extends
		ActivityInstrumentationTestCase2<StudySpaceListActivity> {

	public StudySpaceListActivityTest() {
		super("edu.upenn.cis573", StudySpaceListActivity.class);
	}

	@Before
	protected void setUp() throws Exception {
		super.setUp();
		
		StudySpaceListActivity _listActivity = getActivity();
	}

	public void testListSize() {
		StudySpaceListActivity _listActivity = getActivity();
		
		ArrayList<StudySpace> list = _listActivity.getList();
	//	list = _listActivity.sortByDistance(list);
		
		assertEquals(208, list.size());
	}
	
}
