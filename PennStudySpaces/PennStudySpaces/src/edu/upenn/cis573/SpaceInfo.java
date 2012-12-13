 package edu.upenn.cis573;

import java.util.ArrayList;
import java.util.HashMap;

public class SpaceInfo {

	//To store ranking, descriptions and pictures of study spaces.

	//Static rankings. 1-10. 1 - most popular
	static final HashMap<String, Integer> ranks = new HashMap<String, Integer>(){
		private static final long serialVersionUID = 1L;

		{
			put("Jon M. Huntsman HallGSR", 1);
			//put("", 2);
		}};

		//Descriptions.
		static final HashMap <String, String> descriptions = new HashMap<String, String>(){
			private static final long serialVersionUID = 1L;

			{
				put("GSR", "The spacious and modern Group Study Rooms provide the perfect place for small group meetings, with multiple charging points and a computer connected to two monitors.\n\nThese rooms are conveniently located near two Au Bon Pain restaurants and multiple vending machines with food and drinks.\n\nReservable at any time of the day!");
				put("Upper Lobby","With multiple tables and plenty of seating arrangements, Rodin's Upper Lobby provides the perfect location to meet in a group, large or small.\n\nThe only drawback is that it can get noisy as well as crowded that times.\n\nAccessible by all Penn students prior to 2am, it is available round-the-clock for Rodin residents. There is also the Rodin House Cafe located nearby, making a quick stop for a drink or a bite.");
			}};

			//Pictures
			static final HashMap<String,String> pictures = new HashMap<String, String>(){
				private static final long serialVersionUID = 1L;

				{
					put("GSR","gsr");
					put("Upper Lobby","upperlobby");
				}};

				public static ArrayList<StudySpace> sortByRank(ArrayList<StudySpace> arr){

					//Counting Sort
					int[] C = new int[201];
					ArrayList<StudySpace> S = new ArrayList<StudySpace>();
					int[] random = new int[arr.size()];

					for(int i = 0; i < arr.size(); i++){
						random[i] = getRank(arr.get(i));
						C[random[i]]++;
						S.add(null);
					}


					for(int k=1; k<=200; k++){
						C[k] += C[k-1];
					}

					for(int j=arr.size()-1; j>=0; j--){
						S.set(C[random[j]]-1, arr.get(j));
						C[random[j]]--;
					}

					return S;
				}

				public static int getRank(StudySpace s){
					if(!ranks.containsKey(s.getBuildingName()+s.getSpaceName()))
						return (int)(Math.random()*100 + 100.0);
					else return ranks.get(s.getBuildingName()+s.getSpaceName());
				}

				public static String getDescription(StudySpace s){
					if(!descriptions.containsKey(s.getSpaceName()))
						return "";
					else return descriptions.get(s.getSpaceName());
				}

				public static String getPicture(StudySpace s){
					if(!pictures.containsKey(s.getSpaceName()))
						return "";
					else return pictures.get(s.getSpaceName());
				}
}
