package edu.upenn.cis573.test;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import android.test.ActivityInstrumentationTestCase2;

import edu.upenn.cis573.APIAccessor;
import edu.upenn.cis573.Room;
import edu.upenn.cis573.SearchActivity;
import edu.upenn.cis573.StudySpace;
import edu.upenn.cis573.StudySpaceListActivity;


public class APIAccessorTest extends
		ActivityInstrumentationTestCase2<StudySpaceListActivity> {

	public APIAccessorTest() {
		super("edu.upenn.cis573", StudySpaceListActivity.class);
	}

	@Before
	protected void setUp() throws Exception {
		super.setUp();
		
		StudySpaceListActivity _listActivity = getActivity();
	}
	
	//@Test
	public void testAccessor() {
		
		//test the side effect of the APIAccessor
		StudySpaceListActivity _listActivity = getActivity();
		_listActivity.getSpaces();
		assertFalse(APIAccessor.isFirst);
	}
}

