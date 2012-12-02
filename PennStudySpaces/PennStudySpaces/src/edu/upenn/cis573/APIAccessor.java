package edu.upenn.cis573;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

import org.json.*;

import android.app.*;

public class APIAccessor extends Activity{
	
	private static APIAccessor aa = null;
	//public static JSONObject availabilities;
	public static APIAccessor getAPIAccessor(){
		if(aa == null){
			aa = new APIAccessor();
		}
		return aa;
	}
	
	private APIAccessor(){
		
	}
	
	public static ArrayList<StudySpace> getStudySpaces() throws Exception {
		
		
		System.out.println("Call the APIAccessor Method!");
	//	File file = new File(this.getCacheDir(),"studyCache.txt");
		
		BufferedReader reader = null;
		String line = null;
		
		//first look at the cache,if not exists, then download from internet
		
		/*if(file.exists()){
			FileReader fr = new FileReader(file);
			reader = new BufferedReader(fr);
			line = reader.readLine();
		}else{*/
			String _url = "http://www.pennstudyspaces.com/api?showall=1&format=json";
			
			reader = new BufferedReader(new InputStreamReader(new URL(_url).openStream()));
		
			//save the result
		
			line = reader.readLine();
		/*	try{
				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(line);
				bw.close();
			}catch(Exception ex){
				System.out.println("Cannot write to the cache");
			}*/
	//	}
		
		
		JSONObject json_obj = new JSONObject(line);
		
		JSONArray buildings_arr = json_obj.getJSONArray("buildings");
	
		ArrayList<StudySpace> study_spaces = new ArrayList<StudySpace>();
		
		for(int i = 0 ; i < buildings_arr.length() ; i++){
		    
			JSONArray roomkinds_arr = buildings_arr.getJSONObject(i).getJSONArray("roomkinds");
			
			for(int j = 0; j < roomkinds_arr.length(); j++) {
				
				JSONArray t = roomkinds_arr.getJSONObject(j).getJSONArray("rooms");
				Room[] rooms = new Room[t.length()];
				
				for(int k = 0; k < t.length(); k++) {
					/*System.out.println(t.getJSONObject(k).getInt("id"));
					System.out.println(t.getJSONObject(k).getString("name"));
					System.out.println(roomkinds_arr.getJSONObject(j).getString("name"));
					System.out.println(t.getJSONObject(k).getJSONObject("availabilities").getJSONArray("2012-03-27"));
					System.out.println();
					availabilities = t.getJSONObject(k).getJSONObject("availabilities");
					System.out.println(availableNow());
					//System.out.println(availabilities);
					//JSONArray ja = availabilities.getJSONArray("2012-04-08");
					if(ja.get(1) == null) {
						System.out.println("No JSONArray at index 1!");
					}
					else {
						System.out.println(ja.get(1));
					}*/
					Room temp = new Room(t.getJSONObject(k).getInt("id"), t.getJSONObject(k).getString("name"), t.getJSONObject(k).getJSONObject("availabilities"));
					rooms[k] = temp;
					/*System.out.println(rooms[k].getRoomName());
					System.out.println(rooms[k].getID());
					System.out.println();*/
				}
				
				/*System.out.println(roomkinds_arr.getJSONObject(j).get("max_occupancy"));
				System.out.println(t.length());
				System.out.println(roomkinds_arr.getJSONObject(j));
				System.out.println(roomkinds_arr.getJSONObject(j).getString("name"));
			    System.out.println(buildings_arr.getJSONObject(i).getString("name"));*/
			    
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
			   //test the sequences of the result
			    System.out.println(temp.getBuildingName());
			}
			
		}
		return study_spaces;
	}

}
