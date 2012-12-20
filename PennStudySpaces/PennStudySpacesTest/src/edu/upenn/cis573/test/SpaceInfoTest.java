/*package edu.upenn.cis573.test;

import java.util.ArrayList;



import edu.upenn.cis573.Room;
//import edu.upenn.cis573.SpaceInfo;
import edu.upenn.cis573.StudySpace;

public class SpaceInfoTest extends junit.framework.TestCase {

//	SpaceInfo obj;

	StudySpace a;
	String name = "GSR";
	double lat = 0.0;
	double lon = 0.0;
	int num_rooms = 2;
	String b_name = "Jon M. Huntsman Hall";
	int max_occ = 10;
	boolean has_wh = true;
	String pri = "P";
	boolean has_comp = true;
	String res_type = "";
	boolean has_big_s = true;
	String comm = "";
	Room[] r = null;

	public void testRanks() {

		a = new StudySpace(name, lat, lon, num_rooms, b_name, max_occ, has_wh,
				pri, has_comp, res_type, has_big_s, comm, r);

	//	assertEquals(1, SpaceInfo.getRank(a));

	}

	public void testDescription() {
		a = new StudySpace(name, lat, lon, num_rooms, b_name, max_occ, has_wh,
				pri, has_comp, res_type, has_big_s, comm, r);

		assertEquals(
				"The spacious and modern Group Study Rooms provide the perfect place for small group meetings, with multiple charging points and a computer connected to two monitors.\n\nThese rooms are conveniently located near two Au Bon Pain restaurants and multiple vending machines with food and drinks.\n\nReservable at any time of the day!",
			//	SpaceInfo.getDescription(a));

	}

	public void testSortByRank() {

		a = new StudySpace(name, lat, lon, num_rooms, b_name, max_occ, has_wh,
				pri, has_comp, res_type, has_big_s, comm, r);

		name = "Skirkanich Hall";
		lat = 0.0;
		lon = 0.0;
		num_rooms = 25;
		b_name = "Skirkanich Hall";
		max_occ = 48;
		has_wh = true;
		pri = "P";
		has_comp = false;
		res_type = "";
		has_big_s = false;
		comm = "";
		r = null;

		StudySpace b;

		b = new StudySpace(name, lat, lon, num_rooms, b_name, max_occ, has_wh,
				pri, has_comp, res_type, has_big_s, comm, r);

		ArrayList<StudySpace> studySpaces = new ArrayList<StudySpace>();
		studySpaces.add(b);
		studySpaces.add(a);

		//SpaceInfo.sortByRank(studySpaces);
		assertEquals(1, studySpaces.indexOf(a));

	}

}*/
