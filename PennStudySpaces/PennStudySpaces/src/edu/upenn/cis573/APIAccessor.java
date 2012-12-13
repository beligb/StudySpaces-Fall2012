package edu.upenn.cis573;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import org.json.*;
import android.app.*;

/**
 * Generates a list of all available study spaces from
 * the Penn Study Spaces database
 *
 */
public class APIAccessor extends Activity {

	private static APIAccessor aa = null;
	
	/**
	 * Constructor for class
	 */
	public static APIAccessor getAPIAccessor() {
		if(aa == null){
			aa = new APIAccessor();
		}
		return aa;
	}
	
	/**
	 * Populates a list with all the study spaces from Penn Study Spaces
	 * @return ArrayList<StudySpace>
	 * @throws Exception
	 */
	public static ArrayList<StudySpace> getStudySpaces() throws Exception {
		System.out.println("Call the APIAccessor Method!");
		BufferedReader reader = null;
		String line = null;

		String _url = "http://www.pennstudyspaces.com/api?showall=1&format=json";
		reader = new BufferedReader(new InputStreamReader(new URL(_url).openStream()));		
		line = reader.readLine();

		JSONObject json_obj = new JSONObject(line);

		JSONArray buildings_arr = json_obj.getJSONArray("buildings");

		ArrayList<StudySpace> study_spaces = new ArrayList<StudySpace>();

		for(int i = 0 ; i < buildings_arr.length() ; i++){

			JSONArray roomkinds_arr = buildings_arr.getJSONObject(i).getJSONArray("roomkinds");

			for(int j = 0; j < roomkinds_arr.length(); j++) {

				JSONArray t = roomkinds_arr.getJSONObject(j).getJSONArray("rooms");
				Room[] rooms = new Room[t.length()];

				for(int k = 0; k < t.length(); k++) {
					Room temp = new Room(t.getJSONObject(k).getInt("id"), 
							t.getJSONObject(k).getString("name"), t.getJSONObject(k).getJSONObject("availabilities"));
					rooms[k] = temp;
				}

				StudySpace temp = new StudySpace(
						roomkinds_arr.getJSONObject(j).getString("name"), 
						buildings_arr.getJSONObject(i).getDouble("latitude"), 
						buildings_arr.getJSONObject(i).getDouble("longitude"),
						t.length(), 
						buildings_arr.getJSONObject(i).getString("name"),
						roomkinds_arr.getJSONObject(j).getInt("max_occupancy"),
						roomkinds_arr.getJSONObject(j).getBoolean("has_whiteboard"),
						roomkinds_arr.getJSONObject(j).getString("privacy"),
						roomkinds_arr.getJSONObject(j).getBoolean("has_computer"),
						roomkinds_arr.getJSONObject(j).getString("reserve_type"),
						roomkinds_arr.getJSONObject(j).getBoolean("has_big_screen"),
						roomkinds_arr.getJSONObject(j).getString("comments"),
						rooms
						);

				study_spaces.add(temp);
				System.out.println(temp.getBuildingName());
			}
		}
		return study_spaces;
	}

}
