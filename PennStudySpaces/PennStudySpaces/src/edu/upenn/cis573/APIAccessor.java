package edu.upenn.cis573;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

/**
 * Generates a list of all available study spaces from the Penn Study Spaces
 * database
 * 
 */
public class APIAccessor {

	public static boolean isFirst = true;

	/**
	 * Populates a list with all the study spaces from Penn Study Spaces
	 * 
	 * @return ArrayList<StudySpace>
	 * @throws Exception
	 */
	public static ArrayList<StudySpace> getStudySpaces(Context context)
			throws Exception {
		System.out.println("Call the APIAccessor Method!");
		String line = null;
		try {

			File file = new File(context.getCacheDir(), "studyCache.txt");

			BufferedReader reader = null;
			// first look at the cache,if not exists, then download from
			// internet

			if (file.exists() && !isFirst) {
				System.out.println("File Exists!");
				FileReader fr = new FileReader(file);
				reader = new BufferedReader(fr);
				line = reader.readLine();
			} else {
				System.out.println("Downloading the file!");
				String _url = "http://www.pennstudyspaces.com/api?showall=1&format=json";

				reader = new BufferedReader(new InputStreamReader(
						new URL(_url).openStream()));

				// save the result

				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				
				StringBuffer sb = new StringBuffer();
				
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
				} catch (Exception ex) {
					System.out.println("Cannot write to the cache");
				}
				bw.write(sb.toString());
				bw.close();
				fw.close();
				line =sb.toString();
				isFirst = false;
			}
		} catch (Exception ex) {
			System.out.println("Cannot get the cache directory");
			throw ex;
		}
		
		JSONObject json_obj = new JSONObject(line);
		JSONArray buildings_arr = json_obj.getJSONArray("buildings");

		ArrayList<StudySpace> study_spaces = new ArrayList<StudySpace>();

		for (int i = 0; i < buildings_arr.length(); i++) {

			JSONArray roomkinds_arr = buildings_arr.getJSONObject(i)
					.getJSONArray("roomkinds");

			for (int j = 0; j < roomkinds_arr.length(); j++) {

				JSONArray t = roomkinds_arr.getJSONObject(j).getJSONArray(
						"rooms");
				Room[] rooms = new Room[t.length()];

				for (int k = 0; k < t.length(); k++) {
					Room temp = new Room(t.getJSONObject(k).getInt("id"), t
							.getJSONObject(k).getString("name"), t
							.getJSONObject(k).getJSONObject("availabilities"));
					rooms[k] = temp;
					
					if(k == 0){
						System.out.println("The room name is " + temp.getRoomName());
					}
				}
				
				StudySpace temp = new StudySpace(roomkinds_arr.getJSONObject(j)
						.getString("name"), buildings_arr.getJSONObject(i)
						.getDouble("latitude"), buildings_arr.getJSONObject(i)
						.getDouble("longitude"), t.length(), buildings_arr
						.getJSONObject(i).getString("name"), roomkinds_arr
						.getJSONObject(j).getInt("max_occupancy"),
						roomkinds_arr.getJSONObject(j).getBoolean(
								"has_whiteboard"), roomkinds_arr.getJSONObject(
								j).getString("privacy"), roomkinds_arr
								.getJSONObject(j).getBoolean("has_computer"),
						roomkinds_arr.getJSONObject(j)
								.getString("reserve_type"), roomkinds_arr
								.getJSONObject(j).getBoolean("has_big_screen"),
						roomkinds_arr.getJSONObject(j).getString("comments"),
						rooms);

				study_spaces.add(temp);
				System.out.println(temp.getBuildingName());
				System.out.println("The name of the Studyapce is -----");
				System.out.println(temp.getSpaceName());
			}
		}
		return study_spaces;
	}

}
