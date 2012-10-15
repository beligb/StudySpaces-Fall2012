package edu.upenn.cis573.test;

import org.junit.Before;
import edu.upenn.cis573.StudySpaceListActivity;

import android.test.ActivityInstrumentationTestCase2;

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
		
//		ArrayList<StudySpace> _list = _listActivity.getList();
//		_listActivity.getSpaces();
		
//		assertEquals(_list.size(), 206);
	}

}
