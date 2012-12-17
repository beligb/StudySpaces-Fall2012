package edu.upenn.cis573;

import java.io.Serializable;
import java.util.ArrayList;

//import edu.upenn.cis573.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

public class StudySpaceDetails extends FragmentActivity {

	private TabDetails tabdetails;
	private StudySpace o;
	private Preferences p;

	private SharedPreferences favorites;

	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ssdetails);

		favorites = getSharedPreferences(StudySpaceListActivity.FAV_PREFERENCES, 0);

		Intent i = getIntent();
		o = (StudySpace) i.getSerializableExtra("STUDYSPACE");
		p = (Preferences) i.getSerializableExtra("PREFERENCES");
		if(p == null) {
			p = new Preferences();
		}
		tabdetails = new TabDetails();

		cd = new ConnectionDetector(getApplicationContext());


		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();

		// check for Internet status
		if (isInternetPresent) {

			// Saves the first state of the code
			ImageView image = (ImageView) findViewById(R.id.button_details);
			image.setImageResource(R.color.lightblue);
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			transaction.replace(R.id.fragment_container, tabdetails);
			transaction.commit();
		}

		else {
			// Internet connection is not present
			// Ask user to connect to Internet
			cd.showAlertDialog(StudySpaceDetails.this, "No Internet Connection",
					"You don't have internet connection.", false);


		}
	}

	public void onShareClick(View v) {

	}

	public void onDetailsClick(View v) {

		ImageView image = (ImageView) findViewById(R.id.button_details);
		image.setImageResource(R.color.lightblue);

		// Create new fragment and transaction
		// Fragment newFragment = new TabDetails();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_container, tabdetails);
		// transaction.addToBackStack(null);

		// Commit the transaction
		transaction.commit();
	}

	public void onMapClick(View v){
		Intent i = new Intent(this, CustomMap.class);
		ArrayList<StudySpace> olist = new ArrayList<StudySpace>();
		olist.add(o);
		i.putExtra("STUDYSPACE", o);
		startActivity(i);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent i = new Intent();

			i.putExtra("PREFERENCES", (Serializable)p);
			setResult(RESULT_OK, i);
			//ends this activity
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onFavClick(View v){
		p.addFavorites(o.getBuildingName()+o.getSpaceName());
		tabdetails.onFavClick(v);

		SharedPreferences.Editor editor = favorites.edit();
		editor.putBoolean(o.getBuildingName()+o.getSpaceName(), true);
		editor.commit();
	}
	
	public void onRemoveFavClick(View v){
		p.removeFavorites(o.getBuildingName()+o.getSpaceName());
		tabdetails.onRemoveFavClick(v);
		SharedPreferences.Editor editor = favorites.edit();
		editor.putBoolean(o.getBuildingName()+o.getSpaceName(), false);
		editor.commit();
	}
	
	public void onCalClick(View v) {
		tabdetails.onCalClick(v);

	}

	public void onReserveClick(View v){
		tabdetails.onReserveClick(v);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

}
