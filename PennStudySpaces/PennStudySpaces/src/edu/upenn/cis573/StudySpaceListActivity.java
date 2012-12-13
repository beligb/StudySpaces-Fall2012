package edu.upenn.cis573;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Collections;

import edu.upenn.cis573.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.location.*;

/**
 * Generates and displays the list of Study Spaces that is returned once the user
 * runs the search.
 *
 */
public class StudySpaceListActivity extends ListActivity {

	private ProgressDialog ss_ProgressDialog = null; // Dialog when loading
	private ArrayList<StudySpace> ss_list = null; // List containing available
	// rooms
	private StudySpaceListAdapter ss_adapter; // Adapter to format list items
	private Runnable viewAvailableSpaces; // runnable to get available spaces
	public static final int ACTIVITY_ViewSpaceDetails = 1;
	public static final int ACTIVITY_SearchActivity = 2;
	private SearchOptions searchOptions; //create a default search option later
	private boolean favSelected;
	private Preferences preferences;

	private SharedPreferences favorites;
	static final String FAV_PREFERENCES = "favoritePreferences";

	/** 
	 * Called when the activity is first created. 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sslist);

		favorites = getSharedPreferences(FAV_PREFERENCES, 0);

		ss_list = new ArrayList<StudySpace>(); // List to store StudySpaces

		this.ss_adapter = new StudySpaceListAdapter(this, R.layout.sslistitem,
				ss_list);
		this.setListAdapter(this.ss_adapter); // Adapter to read list and display
		Map<String, ?> items = favorites.getAll();
		preferences = new Preferences(); 		//Change this when bundle is implemented.
		for(String s: items.keySet()) {
			if(Boolean.parseBoolean(items.get(s).toString())){
				preferences.addFavorites(s);
			}
		}

		viewAvailableSpaces = new Runnable() {
			public void run() {
				getSpaces(); // retrieves list of study spaces
			}
		};
		Thread thread = new Thread(null, viewAvailableSpaces, "ThreadName"); // change name?
		thread.start();

		// flag for Internet connection status
		Boolean isInternetPresent = false;

		// Connection detector class
		ConnectionDetector cd;

		cd = new ConnectionDetector(getApplicationContext());

		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();

		if(isInternetPresent){ 
			ss_ProgressDialog = ProgressDialog.show(StudySpaceListActivity.this,
					"Please wait...", "Retrieving data ...", true);
		} else {
			ss_ProgressDialog = ProgressDialog.show(StudySpaceListActivity.this,
					"Connection error ", " Please connect to internet.....", true); 
		}

		//Start up the search options screen
		Intent i = new Intent(this, SearchActivity.class);
		startActivityForResult(i,
				StudySpaceListActivity.ACTIVITY_SearchActivity);

		final TextView search = (EditText)findViewById(R.id.search);
		search.addTextChangedListener(new TextWatcher(){
			public void afterTextChanged(Editable s) {
				String query = search.getText().toString();
				ss_adapter.searchNames(query);
			}
			public void beforeTextChanged(CharSequence s, int start, int count, int after){}
			public void onTextChanged(CharSequence s, int start, int before, int count){}
		}); 
	}

	/**
	 * Sets up the action to occur when the user clicks on either the "Favorites"
	 * tab or one of the study spaces displayed in the list.
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) { 
		Intent i = new Intent(this, StudySpaceDetails.class);
		i.putExtra("STUDYSPACE", (StudySpace)getListAdapter().getItem(position));
		i.putExtra("PREFERENCES", preferences);
		startActivityForResult(i,
				StudySpaceListActivity.ACTIVITY_ViewSpaceDetails);
	}

	/**
	 * Sets up activity to occur once user the class receives an intent from
	 * the user's click on "Favorites" or one of the study spaces. 
	 */
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		switch(requestCode){
		case ACTIVITY_SearchActivity:
			searchOptions = (SearchOptions) intent.getSerializableExtra("SEARCH_OPTIONS");
			ImageView image = (ImageView) this.findViewById(R.id.favorite_button);
			image.setImageResource(R.color.yellow);
			favSelected = searchOptions.getFavSelected();
			System.out.println("SEARCH ACTIVITY " + favSelected);
			if(favSelected) {
				ss_adapter.allToFav();
			} else {
				ss_adapter.filterSpaces();
			}
			ss_adapter.updateFavorites(preferences);
			break;
		case ACTIVITY_ViewSpaceDetails:
			preferences = (Preferences) intent.getSerializableExtra("PREFERENCES");
			ss_adapter.updateFavorites(preferences);
			break;
		}
	}

	/**
	 * Allows StudySpaceListActivity to be run/filtered by threads
	 */
	private Runnable returnRes = new Runnable() {
		public void run() {
			ss_ProgressDialog.dismiss();
			ss_adapter.notifyDataSetChanged();
			if(searchOptions!=null)
				ss_adapter.filterSpaces();
		}
	};

	/**
	 * Gets spaces for both the list of search results and for
	 * the Favorites section.
	 */
	private void getSpaces() {
		try {

			System.out.println("Calling the getSpace method!");
			ss_list.addAll(APIAccessor.getStudySpaces()); 
			ss_adapter.updateFavorites(preferences);
			Thread.sleep(2000); // appears to load for 2 seconds

			Log.i("ARRAY", "" + ss_list.size());
		} catch (Exception e) {
			Log.e("BACKGROUND_PROC", "Something went wrong!");
		}

		runOnUiThread(returnRes);
	}

	/**
	 * Defines behavior of method after user clicks on "Favorites"
	 */
	public void onFavClick(View v) {
		Log.d("fav", "FavClick");
		ImageView image = (ImageView) this.findViewById(R.id.favorite_button);
		if(favSelected) {
			favSelected = false;
			image.setImageResource(R.color.yellow);
			ss_adapter.favToAll();
		} else {
			favSelected = true;
			image.setImageResource(R.color.lightblue);
			ss_adapter.allToFav();
		}
	}

	/**
	 * Takes user back to original search screen when user clicks
	 * on "Filter" tab.
	 */
	public void onFilterClick(View view){
		//Start up the search options screen
		Intent i = new Intent(this, SearchActivity.class);
		startActivityForResult(i,
				StudySpaceListActivity.ACTIVITY_SearchActivity);
	}

	/**
	 * Takes user to MapView when use clicks on "MapView" tab.
	 */
	public void onMapViewClick(View view){
		//Start up the search options screen
		Log.d("MapView", "Clicked");
		Intent i = new Intent(this, CustomMap.class);
		i.putExtra("STUDYSPACELIST", this.ss_adapter.list_items);
		startActivity(i);
	}

	/**
	 * Defines behavior of back button on device - takes user to 
	 * original search screen.
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) ) {
			Intent i = new Intent(this, SearchActivity.class);
			startActivityForResult(i,
					StudySpaceListActivity.ACTIVITY_SearchActivity);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * Grabs nearest study space when user clicks on "Find Now"
	 * button.
	 * @param arr An array of study spaces sorted by distance
	 */
	public void onFNButtonSelected(ArrayList<StudySpace> arr){
		Intent i = new Intent(this, StudySpaceDetails.class);
		i.putExtra("STUDYSPACE", (StudySpace) arr.get(0));
		i.putExtra("PREFERENCES", preferences);
		startActivityForResult(i,
				StudySpaceListActivity.ACTIVITY_ViewSpaceDetails);
	}

	/**
	 * Actually filters and generates the list of study spaces that 
	 * the user has requested.  
	 *
	 */
	private class StudySpaceListAdapter extends ArrayAdapter<StudySpace> {

		private ArrayList<StudySpace> list_items;
		private ArrayList<StudySpace> orig_items;
		private ArrayList<StudySpace> before_search;
		private ArrayList<StudySpace> fav_orig_items;
		private ArrayList<StudySpace> temp;	//Store list items for when favorites is displayed

		/**
		 * Constructor for class
		 * @param context Context for activity
		 * @param textViewResourceId TextView identifier
		 * @param items ArrayList of study spaces
		 */
		public StudySpaceListAdapter(Context context, int textViewResourceId,
				ArrayList<StudySpace> items) {
			super(context, textViewResourceId, items);
			this.list_items = items;
			this.orig_items = items;
			this.fav_orig_items = new ArrayList<StudySpace>();
		}

		/**
		 * Generates the way a study space is displayed on the search result
		 * list.
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.sslistitem, null);
			}

			StudySpace o = list_items.get(position);
			if (o != null) {

				TextView tt = (TextView) v.findViewById(R.id.nametext);
				TextView bt = (TextView) v.findViewById(R.id.roomtext);
				if (tt != null) {
					tt.setText(o.getBuildingName() + " - " + o.getSpaceName());
				}
				if (bt != null) {
					if (o.getNumberOfRooms() == 1) {
						bt.setText(o.getRooms()[0].getRoomName());
					} else {
						bt.setText(o.getRooms()[0].getRoomName() + " (and "
								+ String.valueOf(o.getNumberOfRooms() - 1)
								+ " others)");
					}
				}
				ImageView image = (ImageView) v.findViewById(R.id.icon);
				int resID;
				if(image!=null){
					Resources resource = getResources();
					if(o.getBuildingType().equals(StudySpace.ENGINEERING))
						resID = resource.getIdentifier("engiicon", "drawable", getPackageName());
					else if(o.getBuildingType().equals(StudySpace.WHARTON))
						resID = resource.getIdentifier("whartonicon", "drawable", getPackageName());
					else if(o.getBuildingType().equals(StudySpace.LIBRARIES))
						resID = resource.getIdentifier("libicon", "drawable", getPackageName());
					else resID = resource.getIdentifier("othericon","drawable",getPackageName());
					image.setImageResource(resID);
				}
				ImageView priv = (ImageView) v.findViewById(R.id.priv);
				ImageView wb = (ImageView) v.findViewById(R.id.wb);
				ImageView comp = (ImageView) v.findViewById(R.id.comp);
				ImageView proj = (ImageView) v.findViewById(R.id.proj);
				if(priv!=null && o.getPrivacy().equals("S"))
					priv.setVisibility(View.INVISIBLE);
				else priv.setVisibility(View.VISIBLE);
				if(wb!=null && !o.hasWhiteboard())
					wb.setVisibility(View.INVISIBLE);
				else wb.setVisibility(View.VISIBLE);
				if(comp!=null && !o.hasComputer())
					comp.setVisibility(View.INVISIBLE);
				else comp.setVisibility(View.VISIBLE);
				if(proj!=null && !o.has_big_screen())
					proj.setVisibility(View.INVISIBLE);
				else proj.setVisibility(View.VISIBLE);
			}
			return v;
		}

		/**
		 * Gets the number of study spaces in search results
		 */
		@Override
		public int getCount(){
			return list_items.size();
		}

		/**
		 * Returns a study space based on its position in the list
		 */
		@Override
		public StudySpace getItem(int position){
			return list_items.get(position);
		}

		/**
		 * Filters the original list of study spaces (generated by the APIAccessor class)
		 * based on user's selected filters
		 */
		@SuppressWarnings("unchecked")
		public void filterSpaces(){

			ArrayList<StudySpace> filtered = (ArrayList<StudySpace>) orig_items.clone();


			int i = 0;
			while(i<filtered.size()){
				if(!searchOptions.getEngi() && filtered.get(i).getBuildingType().equals(StudySpace.ENGINEERING)){
					filtered.remove(i);
					continue;
				}
				if(!searchOptions.getWhar() &&filtered.get(i).getBuildingType().equals(StudySpace.WHARTON)){
					filtered.remove(i);
					continue;
				}
				if(!searchOptions.getLib() &&filtered.get(i).getBuildingType().equals(StudySpace.LIBRARIES)){
					filtered.remove(i);
					continue;
				}
				if(!searchOptions.getOth() &&filtered.get(i).getBuildingType().equals(StudySpace.OTHER)){
					filtered.remove(i);
					continue;
				}
				if(searchOptions.getPrivate() && filtered.get(i).getPrivacy().equals("S")){
					filtered.remove(i);
					continue;
				}

				if(searchOptions.getWhiteboard() && !filtered.get(i).hasWhiteboard()){
					filtered.remove(i);
					continue;
				}
				if(searchOptions.getComputer() && !filtered.get(i).hasComputer()){
					filtered.remove(i);
					continue;
				}
				if(searchOptions.getProjector() && !filtered.get(i).has_big_screen()){
					filtered.remove(i);
					continue;
				}
				i++;
			}

			this.list_items = SpaceInfo.sortByRank(filtered);
			this.list_items = filterByPeople(list_items);
			this.list_items = sortByDistance(list_items);
			if(SearchActivity.isFNButtonClicked()){
				System.out.println("Remove Array Method called");
				this.list_items = findNearest(list_items);
				SearchActivity.setFNButtonClicked(false);
			}
			this.before_search = (ArrayList<StudySpace>) this.list_items.clone();
			System.out.println("before search size = " + before_search.size());

			notifyDataSetChanged();
		}
		
		/**
		 * Returns the nearest study space that fits the user criteria
		 * @param arr ArrayList of study spaces
		 * @return Returns nearest study space - some buildings have multiple study spaces,
		 * so if the nearest building has several study spaces, they are returned in an ArrayList
		 */
		public ArrayList<StudySpace> findNearest(ArrayList<StudySpace> arr) {
			if(arr.size() > 1) {
				ArrayList<StudySpace> nArr = new ArrayList<StudySpace>();
				StudySpace nSpace = arr.get(0);
				double nDistance = nSpace.getDistance();
				int index = 0;
				while(index < arr.size()) {
					if (arr.get(index).getDistance() == nDistance){
						nArr.add(arr.get(index));
					}
					index++;
				}
				onFNButtonSelected(nArr);
				return nArr;
			} else {
				return arr;
			}
		}

		public ArrayList<StudySpace> sortByDistance(ArrayList<StudySpace> arr){
			double currentLatitude = SearchActivity.latitude;
			double currentLongitude = SearchActivity.longitude;
			for(StudySpace temp: arr){
				double spaceLatitude = temp.getSpaceLatitude();
				double spaceLongitude = temp.getSpaceLongitude();
				float results[] = new float[3];
				Location.distanceBetween(currentLatitude, currentLongitude, spaceLatitude, spaceLongitude, results);
				double distance = results[0];
				temp.setDistance(distance);
			}
			Collections.sort(arr);
			System.out.println("Results havs already been sorted!");
		
			
			if(arr.isEmpty()){

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getContext());
				// set title
				alertDialogBuilder.setTitle("No Results Found!");

				// set dialog message
				alertDialogBuilder
				.setMessage("No rooms available with the selected criteria")
				.setCancelable(false)
				.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {

						dialog.cancel();
					}
				});


				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

			}

			return arr;
		}

		@SuppressWarnings("unchecked")
		public void searchNames(String query){
			query = query.toLowerCase(Locale.US);
			this.list_items = (ArrayList<StudySpace>) this.before_search.clone();
			if(!query.equals("")){
				for(int i = list_items.size()-1; i>=0; i--){
					StudySpace s = list_items.get(i);
					if(s.getBuildingName().toLowerCase(Locale.US).indexOf(query)>=0 || 
							s.getSpaceName().toLowerCase(Locale.US).indexOf(query)>=0 || 
							s.getRoomNames().toLowerCase(Locale.US).indexOf(query)>=0){
					}else{
						list_items.remove(i);
					}
				}
			}
			notifyDataSetChanged();
		}

		//switch to favorites
		public void allToFav(){
			this.temp = this.list_items;	//remember list items
			this.list_items = fav_orig_items;
			notifyDataSetChanged();
		}
		public void favToAll(){
			this.list_items = this.temp;	//restore list items
			notifyDataSetChanged();
		}
		public void updateFavorites(Preferences p){
			this.fav_orig_items = SpaceInfo.sortByRank(this.orig_items);
			for(int i = fav_orig_items.size()-1; i>=0; i--){
				if(!p.isFavorite(fav_orig_items.get(i).getBuildingName()+fav_orig_items.get(i).getSpaceName()))
					fav_orig_items.remove(i);
			}
		}
	}

	public ArrayList<StudySpace> filterByDate(ArrayList<StudySpace> arr){
		Date d1 = searchOptions.getStartDate();
		Date d2 = searchOptions.getEndDate();

		for(int i = arr.size()-1; i>=0; i--){
			boolean flag = false;
			for(Room r: arr.get(i).getRooms()){
				try {
					if(r.searchAvailability(d1, d2)){
						flag = true;
					}
				} catch (Exception e) {
					//Log.e("exception","here");
					//arr.remove(i);		shouldn't be here
				}
			}

			if(!flag) 
				arr.remove(i);
		}
		return arr;
	}

	public ArrayList<StudySpace> filterByPeople(ArrayList<StudySpace> arr){
		for(int i = arr.size()-1; i>=0; i--){
			if(arr.get(i).getMaximumOccupancy()<searchOptions.getNumberOfPeople())
				arr.remove(i);
		}
		return arr;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:     
			startActivity(new Intent(this, About.class));
			break;
		case R.id.help:     
			startActivity(new Intent(this, Help.class));
			break;
		}
		return true;
	}
}