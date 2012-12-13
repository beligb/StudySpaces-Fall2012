package edu.upenn.cis573;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import org.json.*;
import android.app.*;
import android.content.Context;
import android.os.Bundle;

/**
 * Generates a list of all available study spaces from
 * the Penn Study Spaces database
 *
 */
public class APIAccessor extends Activity {

	 static APIAccessor aa = null;
	
	/**
	 * Constructor for class
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	public ArrayList<StudySpace> getStudySpaces() throws Exception {
		
		
		System.out.println("Call the APIAccessor Method!");
		try{
			Context context = getApplicationContext();
			context.getCacheDir();
		}catch(Exception ex){
			System.out.println("Cannot get the cache directory");
		}
		File file = new File(this.getCacheDir(),"studyCache.txt");
		
		BufferedReader reader = null;
		String line = null;
		
		//first look at the cache,if not exists, then download from internet
		
		if(file.exists()){
			System.out.println("File Exsitss!");
			FileReader fr = new FileReader(file);
			reader = new BufferedReader(fr);
			line = reader.readLine();
		}else{
			System.out.println("Downloading the file!");
			String _url = "http://www.pennstudyspaces.com/api?showall=1&format=json";
			
			reader = new BufferedReader(new InputStreamReader(new URL(_url).openStream()));
		
			//save the result
		
			line = reader.readLine();
			try{
				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(line);
				bw.close();
			}catch(Exception ex){
				System.out.println("Cannot write to the cache");
			}
		}
		
		
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
