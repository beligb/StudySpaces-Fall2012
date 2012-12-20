package edu.upenn.cis573.test;

import org.json.JSONObject;
import org.junit.Before;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.test.ActivityInstrumentationTestCase2;
import edu.upenn.cis573.Preferences;
import edu.upenn.cis573.Room;
import edu.upenn.cis573.StudySpace;
import edu.upenn.cis573.StudySpaceDetails;

public class StudySpaceDetailsTest extends 
ActivityInstrumentationTestCase2<StudySpaceDetails> {

	@Before
	public void setUp() throws Exception {
		super.setUp();
		// Removed "GSR" - pretending Huntsman only has one room and is formatted normally for testing  
		String name = ""; double lat = 0.0; double lon = 0.0;
		int num_rooms = 2; String b_name = "Jon M. Huntsman Hall";
		int max_occ = 10; boolean has_wh = true; String pri = "P";
		boolean has_comp = true; String res_type = ""; 
		boolean has_big_s = true;
		String comm = ""; 
		Room[] r = new Room[1];
		JSONObject _json = new JSONObject();
		Room _room1 = new Room(3, "Huntsman", _json);
		r[0] = _room1;

		StudySpace hhall = new StudySpace(name, lat, lon, num_rooms, b_name, max_occ, has_wh,
				pri, has_comp, res_type, has_big_s, comm, r);
		Intent i = new Intent();
		Preferences p = new Preferences();
		
		i.putExtra("STUDYSPACE", hhall);
		i.putExtra("PREFERENCES", p);
		setActivityIntent(i);
	}
	
	public StudySpaceDetailsTest() {
		super(StudySpaceDetails.class);
	}

	// Test borrowed from http://stackoverflow.com/questions/6491270/fragmentactivity-junit-testing 
	public void testFragmentManager() {
		FragmentActivity activity = getActivity();
		assertNotNull(activity.getSupportFragmentManager());
	}

}