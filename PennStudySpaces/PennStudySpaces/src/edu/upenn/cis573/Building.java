package edu.upenn.cis573;

import java.io.Serializable;
import java.util.ArrayList;

public class Building implements Serializable {

	private static final long serialVersionUID = 1L;

	private ArrayList<StudySpace> rooms;

	public Building(ArrayList<StudySpace> olist) {
		rooms = new ArrayList<StudySpace>(olist);
	}

	public double getSpaceLatitude() {
		return rooms.get(0).getSpaceLatitude();
	}

	public double getSpaceLongitude() {
		return rooms.get(0).getSpaceLongitude();
	}

	public String getBuildingName() {
		return rooms.get(0).getBuildingName();
	}
	
	public int getRoomCount() {
		return rooms.size();
	}

	public double getDistance() {
		return rooms.get(0).getDistance();
	}

	public ArrayList<StudySpace> getRooms() {
		return rooms;
	}

}
