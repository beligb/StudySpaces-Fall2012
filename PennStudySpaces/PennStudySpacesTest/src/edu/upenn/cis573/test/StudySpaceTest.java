package edu.upenn.cis573.test;


import org.json.JSONObject;

import junit.framework.TestCase;

import edu.upenn.cis573.Room;
import edu.upenn.cis573.StudySpace;

public class StudySpaceTest extends TestCase {
	
	//test the type of the study space
	public void testGSRType() {
		String name = "GSR"; double lat = 0.0; double lon = 0.0;
		int num_rooms = 2; String b_name = "Jon M. Huntsman Hall";
		int max_occ = 10; boolean has_wh = true; String pri = "P";
		boolean has_comp = true; String res_type = ""; boolean has_big_s = true;
		String comm = ""; Room[] r = null;
		
		StudySpace a = new StudySpace(name, lat, lon, num_rooms, b_name, max_occ, has_wh,
										pri, has_comp, res_type, has_big_s, comm, r);
		
		assertEquals("Wharton", a.getBuildingType());
	}
	
	//test the GSR names
	public void testGSRRoomNameDifferentRooms(){
		String name = "GSR"; double lat = 0.0; double lon = 0.0;
		int num_rooms = 2; String b_name = "Jon M. Huntsman Hall";
		int max_occ = 10; boolean has_wh = true; String pri = "P";
		boolean has_comp = true; String res_type = ""; boolean has_big_s = true;
		String comm = ""; 
		JSONObject json = new JSONObject();
		Room rm1 = new Room(1,"F12",json);
		//Room rm2 = new Room(2,"302", json);
		Room rm2 = new Room(3,"F11", json);
		
		Room[] rooms = {rm1,rm2};
		System.out.println(rooms[0].getRoomName());
		
		StudySpace a = new StudySpace(name, lat, lon, num_rooms, b_name, max_occ, has_wh,
				pri, has_comp, res_type, has_big_s, comm, rooms);
		
		String roomNames = a.getRoomNames();
		roomNames = roomNames.substring(0,7);
		System.out.println(roomNames);
		assertEquals("F11 F12", roomNames);
		
	}
	
	//test the GSR names
		public void testGSRRoomNameDifferentStairs(){
			String name = "GSR"; double lat = 0.0; double lon = 0.0;
			int num_rooms = 2; String b_name = "Jon M. Huntsman Hall";
			int max_occ = 10; boolean has_wh = true; String pri = "P";
			boolean has_comp = true; String res_type = ""; boolean has_big_s = true;
			String comm = ""; 
			JSONObject json = new JSONObject();
			Room rm2 = new Room(1,"F12",json);
			//Room rm2 = new Room(2,"302", json);
			Room rm1 = new Room(3,"G11", json);
			
			Room[] rooms = {rm1,rm2};
			System.out.println(rooms[0].getRoomName());
			
			StudySpace a = new StudySpace(name, lat, lon, num_rooms, b_name, max_occ, has_wh,
					pri, has_comp, res_type, has_big_s, comm, rooms);
			
			String roomNames = a.getRoomNames();
			roomNames = roomNames.substring(0,9);
			System.out.println(roomNames);
			
			String assertRoomNames = "F12 " + "\n\n" + "G11";
			assertEquals(assertRoomNames, roomNames);	
		}
		
		//test normal room names	
		public void testNromalRoomName(){
			String name = "nromal"; double lat = 0.0; double lon = 0.0;
			int num_rooms = 2; String b_name = "Jon M. Huntsman Hall";
			int max_occ = 10; boolean has_wh = true; String pri = "P";
			boolean has_comp = true; String res_type = ""; boolean has_big_s = true;
			String comm = ""; 
			JSONObject json = new JSONObject();
			Room rm1 = new Room(1,"F12",json);
			//Room rm2 = new Room(2,"302", json);
			Room rm2 = new Room(3,"G11", json);
			
			Room[] rooms = {rm1,rm2};
			
			StudySpace a = new StudySpace(name, lat, lon, num_rooms, b_name, max_occ, has_wh,
					pri, has_comp, res_type, has_big_s, comm, rooms);
			
			String roomNames = a.getRoomNames();
			
			String assertRoomNames = "F12 G11 ";
			assertEquals(assertRoomNames, roomNames);	
		}
	
}
