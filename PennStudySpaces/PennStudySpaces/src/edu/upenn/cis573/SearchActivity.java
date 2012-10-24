package edu.upenn.cis573;

import java.io.Serializable;
import java.util.Calendar;
import edu.upenn.cis573.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.location.Location;
import android.location.LocationListener;
import android.location.Criteria;
import android.location.LocationManager;

public class SearchActivity extends Activity {

	private Button mFavoritesButton;
	private Button mSearchButton;
	private TextView mNumberOfPeopleTextView;
	private SeekBar mNumberOfPeopleSlider;
	private CheckBox mPrivateCheckBox;
	private CheckBox mWhiteboardCheckBox;
	private CheckBox mComputerCheckBox;
	private CheckBox mProjectorCheckBox;

	private CheckBox mEngiBox;
	private CheckBox mWharBox;
	private CheckBox mLibBox;
	private CheckBox mOthBox;

	private TextView mStartTimeDisplay;
	private Button mPickStartTime;
	private TextView mEndTimeDisplay;
	private Button mPickEndTime;
	private TextView mDateDisplay;
	private Button mPickDate;
	private SharedPreferences favorites;

	static final int START_TIME_DIALOG_ID = 0;
	static final int END_TIME_DIALOG_ID = 1;
	static final int DATE_DIALOG_ID = 2;

	private Dialog mCurrentDialog;

	private SearchOptions mSearchOptions;

	private SharedPreferences search;
	static final String SEARCH_PREFERENCES = "searchPreferences";

	//revise the program
	protected LocationManager locationManager;
	private String provider;
	private LocationListener locationListener;

	public static double latitude = 0;
	public static double longitude = 0;

	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	/** Called when the activity is first created. */

	protected void showCurrentLocation() {

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		boolean enabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// Check if enabled and if not send user to the GSP settings
		// Better solution would be to display a dialog and suggesting to 
		// go to the settings
		if (!enabled) {
			System.out.println("THE GPS IS NOT ENABLED");
		} 
		// Define the criteria how to select the locatioin provider -> use
		// default
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);

		if(location == null){
			System.out.println("CURRENT LOCATION NOT AVAILAVBLE");
			location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			if(location != null){
				System.out.println("Finally it works");
			}else{
				System.out.println("OOOOOOOOPs, doesn't work again!");
			}
		}
		if (location != null) {
			String message = String.format(
					"Current Location \n Longitude: %1$s \n Latitude: %2$s",
					location.getLongitude(), location.getLatitude()
					);
			latitude = location.getLatitude();
			longitude = location.getLongitude();

			//   onLocationChanged(location);
			System.out.println("CURRENT LOCATION ISISISISISISISIS");
			System.out.println(message);
		} 

	}         


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		//get the location Manager in order to get the current location
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		search = getSharedPreferences(SEARCH_PREFERENCES, 0);
		favorites = getSharedPreferences(StudySpaceListActivity.FAV_PREFERENCES, 0);

		/*
        Log.d("", "Trying to load Bundle.");

        if (savedInstanceState != null && savedInstanceState.containsKey("SEARCH_OPTIONS")) {
        	mSearchOptions = savedInstanceState.getParcelable("SEARCH_OPTIONS");
        	getDataOutOfSearchOptionsObject();
        	Log.d("", "Loaded Bundle.");
        } else {
        	mSearchOptions = new SearchOptions();
        	resetTimeAndDateData();
        }
		 */

		captureViewElements();
		mSearchOptions = new SearchOptions();

		setUpNumberOfPeopleSlider(); // Here???

		// TESTING, NOT FINISHED:
		// TODO: Make a method for this:
		// Access the default SharedPreferences
		if (search.getInt("numberOfPeople", -1) != -1) {
			mSearchOptions.setNumberOfPeople(search.getInt("numberOfPeople", -1));
			mNumberOfPeopleSlider.setProgress(mSearchOptions.getNumberOfPeople()); // BAD.
			updateNumberOfPeopleDisplay(); // BAD.
		}

		resetTimeAndDateData();

		setUpCheckBoxes();
		setUpPrivate();

		updateTimeAndDateDisplays();

		// add a click listener to the button
		mPickStartTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(START_TIME_DIALOG_ID);
			}
		});
		mPickEndTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(END_TIME_DIALOG_ID);
			}
		});
		mPickDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case START_TIME_DIALOG_ID:
			dialogStartTime(mSearchOptions.getStartHour(), mSearchOptions.getStartMinute());
			break;
		case END_TIME_DIALOG_ID:
			dialogEndTime(mSearchOptions.getEndHour(), mSearchOptions.getEndMinute());
			break;
		case DATE_DIALOG_ID:
			dialogDate(mSearchOptions.getYear(), mSearchOptions.getMonth(), mSearchOptions.getDay());
			break;
		}
		return super.onCreateDialog(id);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// TESTING, NOT FINISHED:
		// TODO: Make a method for this:
		//
		// Access the default SharedPreferences

		// The SharedPreferences editor - must use commit() to submit changes
		SharedPreferences.Editor editor = search.edit();
		//
		// Edit the saved preferences
		editor.putBoolean("private", mSearchOptions.getPrivate());
		editor.putBoolean("computer", mSearchOptions.getComputer());
		editor.putBoolean("projector", mSearchOptions.getProjector());
		editor.putBoolean("engineering", mSearchOptions.getEngi());
		editor.putBoolean("library", mSearchOptions.getLib());
		editor.putBoolean("wharton", mSearchOptions.getWhar());
		editor.putBoolean("others", mSearchOptions.getOth());
		editor.putInt("numberOfPeople", mSearchOptions.getNumberOfPeople());
		editor.putBoolean("whiteboard", mSearchOptions.getWhiteboard());
		editor.commit();
	}


	/*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	// Save UI state changes to the savedInstanceState.
    	// This bundle will be passed to onCreate if the process is killed and restarted.
    	outState.putParcelable("SEARCH_OPTIONS", mSearchOptions);
    	Log.d("", "Saving Bundle.");
    }
	 */

	/*
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);
      // Restore UI state from the savedInstanceState.
      // This bundle has also been passed to onCreate.
      if (savedInstanceState != null && savedInstanceState.containsKey("SEARCH_OPTIONS")) {
      	  mSearchOptions = savedInstanceState.getParcelable("SEARCH_OPTIONS");
      	  getDataOutOfSearchOptionsObject();
      	  Log.d("", "Loaded Bundle.");
      } else {
      	  mSearchOptions = new SearchOptions();
      	  resetTimeAndDateData();
      }
    }
	 */

	private void setUpNumberOfPeopleSlider() { 	
		mNumberOfPeopleSlider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUserTouch) {
				mSearchOptions.setNumberOfPeople(progress + 1);
				updateNumberOfPeopleDisplay();
			}
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
		});

		mSearchOptions.setNumberOfPeople(mNumberOfPeopleSlider.getProgress() + 1);

		updateNumberOfPeopleDisplay();
	}

	private void setUpCheckBoxes() {

		mEngiBox.setChecked(search.getBoolean("engineering", true));
		mWharBox.setChecked(search.getBoolean("wharton", true));
		mLibBox.setChecked(search.getBoolean("library", true));
		mOthBox.setChecked(search.getBoolean("others", true));

		/*
        // Do something when the check box is checked or unchecked:
        final CheckBox projectorCheckBox = (CheckBox)findViewById(R.id.projectorCheckBox);
        projectorCheckBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    Toast.makeText(SearchActivity.this, "Projector selected!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SearchActivity.this, "Projector not selected!", Toast.LENGTH_SHORT).show();
                }
            }
        });
		 */
	}

	private void setUpPrivate(){

		mPrivateCheckBox.setChecked(search.getBoolean("private", false));
		mWhiteboardCheckBox.setChecked(search.getBoolean("whiteboard", false));
		mComputerCheckBox.setChecked(search.getBoolean("computer", false));
		mProjectorCheckBox.setChecked(search.getBoolean("projector", false));
	}


	private void updateNumberOfPeopleDisplay() {
		String personPeopleString;
		if (mSearchOptions.getNumberOfPeople() == 1) {
			personPeopleString = " person";
		} else {
			personPeopleString = " people";
		}
		mNumberOfPeopleTextView.setText(Integer.toString(mSearchOptions.getNumberOfPeople()) + personPeopleString);
	}


	private void roundCalendar(Calendar c) {
		if (c.get(Calendar.MINUTE) >= 1 && c.get(Calendar.MINUTE) <= 29) {
			c.add(Calendar.MINUTE, 30 - c.get(Calendar.MINUTE));
		}
		if (c.get(Calendar.MINUTE) >= 31 && c.get(Calendar.MINUTE) <= 59) {
			c.add(Calendar.MINUTE, 60 - c.get(Calendar.MINUTE));
		}
	}

	private int fixMinute(int minute) {
		int fixedMinute = minute;
		if (minute >= 31 && minute <= 45) {
			fixedMinute = 0; // Going up.
		} else if (minute >= 1 && minute <= 15) {
			fixedMinute = 30; // Going down.
		} else if (minute >= 46 && minute <= 59) {
			fixedMinute = 30; // Going up.
		} else if (minute >= 16 && minute <= 29) {
			fixedMinute = 0; // Going down.
		}
		return fixedMinute;
	}

	private int fixYear(int year) {
		Calendar c = Calendar.getInstance();
		if (year < c.get(Calendar.YEAR)) {
			year = c.get(Calendar.YEAR);
		}
		return year;
	}

	private void updateStartTimeText() {
		mStartTimeDisplay.setText(
				new StringBuilder()
				.append(pad(mSearchOptions.getStartHour())).append(":")
				.append(pad(mSearchOptions.getStartMinute())));
	}

	private void updateEndTimeText() {
		mEndTimeDisplay.setText(
				new StringBuilder()
				.append(pad(mSearchOptions.getEndHour())).append(":")
				.append(pad(mSearchOptions.getEndMinute())));
	}

	private void updateDateText() {
		mDateDisplay.setText(
				new StringBuilder()
				// Month is 0 based so add 1
				.append(mSearchOptions.getMonth() + 1).append("-")
				.append(mSearchOptions.getDay()).append("-")
				.append(mSearchOptions.getYear()).append(" "));
	}

	// updates the time we display in the TextView
	private void updateStartTimeDisplay(TimePicker timePicker, int hourOfDay, int minute) {
		// do calculation of next time   
		int fixedMinute = fixMinute(minute);

		// remove ontimechangedlistener to prevent stackoverflow/infinite loop
		timePicker.setOnTimeChangedListener(mNullTimeChangedListener);

		// set minute
		timePicker.setCurrentMinute(fixedMinute);

		// hook up ontimechangedlistener again
		timePicker.setOnTimeChangedListener(mStartTimeChangedListener);

		// update the date variable for use elsewhere in code
		mSearchOptions.setStartHour(hourOfDay);
		mSearchOptions.setStartMinute(fixedMinute);
		//date.setMinutes(nextMinute);  

		// display the time in the text field
		updateStartTimeText();
	}

	private void updateEndTimeDisplay(TimePicker timePicker, int hourOfDay, int minute) {
		// do calculation of next time   
		int fixedMinute = fixMinute(minute);

		// remove ontimechangedlistener to prevent stackoverflow/infinite loop
		timePicker.setOnTimeChangedListener(mNullTimeChangedListener);

		// set minute
		timePicker.setCurrentMinute(fixedMinute);

		// hook up ontimechangedlistener again
		timePicker.setOnTimeChangedListener(mEndTimeChangedListener);

		// update the date variable for use elsewhere in code
		mSearchOptions.setEndHour(hourOfDay);
		mSearchOptions.setEndMinute(fixedMinute);
		//date.setMinutes(nextMinute);  

		// display the time in the text field
		updateEndTimeText();
	}

	// updates the date in the TextView
	private void updateDateDisplay(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
		// do calculation of next time   
		int fixedYear = fixYear(year);

		// remove ontimechangedlistener to prevent stackoverflow/infinite loop
		//datePicker.setOnDateChangedListener(mNullDateChangedListener);

		// set year
		//datePicker.setCurrentYear(fixedYear);

		// hook up ontimechangedlistener again
		//datePicker.setOnDateChangedListener(mDateChangedListener);

		// update the date variable for use elsewhere in code
		mSearchOptions.setYear(fixedYear);
		mSearchOptions.setMonth(monthOfYear);
		mSearchOptions.setDay(dayOfMonth);
		//date.setMinutes(nextMinute);  

		// display the time in the text field
		updateDateText();
	}

	private static String pad(int c) {
		if (c >= 10)
			return String.valueOf(c);
		else
			return "0" + String.valueOf(c);
	}

	private TimePicker.OnTimeChangedListener mStartTimeChangedListener =
			new TimePicker.OnTimeChangedListener() {
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			updateStartTimeDisplay(view, hourOfDay, minute);
		}
	};

	private TimePicker.OnTimeChangedListener mEndTimeChangedListener =
			new TimePicker.OnTimeChangedListener() {
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			updateEndTimeDisplay(view, hourOfDay, minute);
		}
	};

	private TimePicker.OnTimeChangedListener mNullTimeChangedListener =
			new TimePicker.OnTimeChangedListener() {
		public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
			// Do nothing.
		}
	};

	private DatePicker.OnDateChangedListener mDateChangedListener =
			new DatePicker.OnDateChangedListener() {
		public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			updateDateDisplay(view, year, monthOfYear, dayOfMonth);
		}
	};

	private DatePicker.OnDateChangedListener mNullDateChangedListener =
			new DatePicker.OnDateChangedListener() {
		public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			// Do nothing.
		}
	}; 

	private Button.OnClickListener mStartTimeOKListener = 
			new OnClickListener() {
		public void onClick(View view) {
			mCurrentDialog.cancel();
			mCurrentDialog = null;
		}
	};

	private Button.OnClickListener mEndTimeOKListener = 
			new OnClickListener() {
		public void onClick(View view) {
			mCurrentDialog.cancel();
			mCurrentDialog = null;
		}
	};

	private Button.OnClickListener mDateOKListener = 
			new OnClickListener() {
		public void onClick(View view) {
			mCurrentDialog.cancel();
			mCurrentDialog = null;
		}
	};

	private void dialogStartTime(int currentHour, int currentMinute) {
		if (mCurrentDialog != null) mCurrentDialog.cancel();
		mCurrentDialog = new Dialog(this);
		mCurrentDialog.setContentView(R.layout.starttimepicker);
		mCurrentDialog.setCancelable(true);
		mCurrentDialog.setTitle("Pick a start time");
		mCurrentDialog.show();

		TimePicker startTimePicker = (TimePicker)mCurrentDialog.findViewById(R.id.startTimePicker);
		startTimePicker.setCurrentHour(currentHour);
		startTimePicker.setCurrentMinute(currentMinute);
		startTimePicker.setOnTimeChangedListener(mStartTimeChangedListener);
		startTimePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

		Button startTimeOK = (Button)mCurrentDialog.findViewById(R.id.startTimeOK);
		startTimeOK.setOnClickListener(mStartTimeOKListener);
	}

	private void dialogEndTime(int currentHour, int currentMinute) {
		if (mCurrentDialog != null) mCurrentDialog.cancel();
		mCurrentDialog = new Dialog(this);
		mCurrentDialog.setContentView(R.layout.endtimepicker);
		mCurrentDialog.setCancelable(true);
		mCurrentDialog.setTitle("Pick an end time");
		mCurrentDialog.show();

		TimePicker endTimePicker = (TimePicker)mCurrentDialog.findViewById(R.id.endTimePicker);
		endTimePicker.setCurrentHour(currentHour);
		endTimePicker.setCurrentMinute(currentMinute);
		endTimePicker.setOnTimeChangedListener(mEndTimeChangedListener);
		endTimePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);

		Button endTimeOK = (Button)mCurrentDialog.findViewById(R.id.endTimeOK);
		endTimeOK.setOnClickListener(mEndTimeOKListener);
	}

	private void dialogDate(int currentYear, int currentMonth, int currentDay) {
		if (mCurrentDialog != null) mCurrentDialog.cancel();
		mCurrentDialog = new Dialog(this);
		mCurrentDialog.setContentView(R.layout.datepicker);
		mCurrentDialog.setCancelable(true);
		mCurrentDialog.setTitle("Pick a date");
		mCurrentDialog.show();

		DatePicker datePicker = (DatePicker)mCurrentDialog.findViewById(R.id.datePicker);
		datePicker.init(currentYear, currentMonth, currentDay, mDateChangedListener);
		datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

		Button dateOK = (Button)mCurrentDialog.findViewById(R.id.dateOK);
		dateOK.setOnClickListener(mDateOKListener);
	}

	//Clicking back button. Does not update mSearchOptions.
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			putDataInSearchOptionsObject();
			//Returns to List activity
			Intent i = new Intent();
			//Put your searchOption class here
			i.putExtra("SEARCH_OPTIONS", (Serializable)mSearchOptions);
			setResult(RESULT_OK, i);
			//ends this activity
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}    

	//Updates search options then delivers intent
	public void onSearchButtonClick(View view) {

		cd = new ConnectionDetector(getApplicationContext());

		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();


		if (isInternetPresent){
			putDataInSearchOptionsObject();
			//Returns to List activity
			Intent i = new Intent();
			//Put your searchOption class here
			mSearchOptions.setFavSelected(false);
			i.putExtra("SEARCH_OPTIONS", (Serializable)mSearchOptions);

			//on search trigger test
			this.showCurrentLocation();
			setResult(RESULT_OK, i);
			//ends this activity
			finish();
		} else {
			cd.showAlertDialog(SearchActivity.this, "No Internet Connection",
					"You don't have internet connection.", false);

		}
	}

	//Updates search options then delivers intent
	public void onFavsButtonClick(View view) {

		cd = new ConnectionDetector(getApplicationContext());

		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();
		if (isInternetPresent){
			putDataInSearchOptionsObject();
			
			//Returns to List activity
			Intent i = new Intent();
			//Put your searchOption class here
			mSearchOptions.setFavSelected(true);
			i.putExtra("SEARCH_OPTIONS", (Serializable)mSearchOptions);
			
			//on search trigger test
			//this.showCurrentLocation();
			setResult(RESULT_OK, i);
			//ends this activity
			finish();
		} else {
			cd.showAlertDialog(SearchActivity.this, "No Internet Connection",
					"You don't have internet connection.", false);

		}
	}    

	private void putDataInSearchOptionsObject() {
		mSearchOptions.setNumberOfPeople( mNumberOfPeopleSlider.getProgress() );
		mSearchOptions.setPrivate( mPrivateCheckBox.isChecked() );
		mSearchOptions.setWhiteboard( mWhiteboardCheckBox.isChecked() );
		mSearchOptions.setComputer( mComputerCheckBox.isChecked() );
		mSearchOptions.setProjector( mProjectorCheckBox.isChecked() );

		mSearchOptions.setEngi(mEngiBox.isChecked());
		mSearchOptions.setWhar(mWharBox.isChecked());
		mSearchOptions.setLib(mLibBox.isChecked());
		mSearchOptions.setOth(mOthBox.isChecked());
		mSearchOptions.setFavSelected(false);
	}

	private void resetTimeAndDateData() {
		// Initialize the date and time data based on the current time:
		final Calendar cStartTime = Calendar.getInstance();
		roundCalendar(cStartTime);
		mSearchOptions.setStartHour(cStartTime.get(Calendar.HOUR_OF_DAY));
		mSearchOptions.setStartMinute(cStartTime.get(Calendar.MINUTE));
		final Calendar cEndTime = Calendar.getInstance();
		cEndTime.add(Calendar.HOUR_OF_DAY, 1);
		roundCalendar(cEndTime);
		mSearchOptions.setEndHour(cEndTime.get(Calendar.HOUR_OF_DAY));
		mSearchOptions.setEndMinute(cEndTime.get(Calendar.MINUTE));
		mSearchOptions.setYear(cStartTime.get(Calendar.YEAR));
		mSearchOptions.setMonth(cStartTime.get(Calendar.MONTH));
		mSearchOptions.setDay(cStartTime.get(Calendar.DAY_OF_MONTH));
	}

	private void updateTimeAndDateDisplays() {
		updateStartTimeText();
		updateEndTimeText();
		updateDateText();	
	}

	private void captureViewElements() {

		// General:
		mSearchButton = (Button)findViewById(R.id.searchButton);
		mFavoritesButton = (Button)findViewById(R.id.favoritesButton);
		mNumberOfPeopleTextView = (TextView)findViewById(R.id.numberOfPeopleTextView);
		mNumberOfPeopleSlider = (SeekBar)findViewById(R.id.numberOfPeopleSlider);
		mPrivateCheckBox = (CheckBox)findViewById(R.id.privateCheckBox);
		mWhiteboardCheckBox = (CheckBox)findViewById(R.id.whiteboardCheckBox);
		mComputerCheckBox = (CheckBox)findViewById(R.id.computerCheckBox);
		mProjectorCheckBox = (CheckBox)findViewById(R.id.projectorCheckBox);

		mEngiBox = (CheckBox) findViewById(R.id.engibox);
		mWharBox =(CheckBox) findViewById(R.id.whartonbox);
		mLibBox = (CheckBox) findViewById(R.id.libbox);
		mOthBox = (CheckBox) findViewById(R.id.otherbox);

		// Time and date:
		mStartTimeDisplay = (TextView) findViewById(R.id.startTimeDisplay);
		mPickStartTime = (Button) findViewById(R.id.pickStartTime);
		mEndTimeDisplay = (TextView) findViewById(R.id.endTimeDisplay);
		mPickEndTime = (Button) findViewById(R.id.pickEndTime);
		mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
		mPickDate = (Button) findViewById(R.id.pickDate);

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
		case R.id.meme:     
			startActivity(new Intent(this, Meme.class));
			break;
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