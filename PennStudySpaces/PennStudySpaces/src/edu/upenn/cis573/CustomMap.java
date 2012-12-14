package edu.upenn.cis573;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

/**
 * Generates the map to display location of various study spaces
 *
 */
public class CustomMap extends MapActivity {

	LinearLayout linearLayout;
	MapView mapView;
	MapController mc;
	GeoPoint p;
	GeoPoint q;
	GeoPoint avg;
	Drawable drawableRed, drawableBlue;
	Preferences pref;
	private StudySpace space;
	
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	/**
	 * Generates initial map with pointers for study spaces
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = super.getIntent();
		pref = (Preferences) i.getSerializableExtra("PREFERENCES");
		space = (StudySpace) i.getSerializableExtra("STUDYSPACE");

		cd = new ConnectionDetector(getApplicationContext());

		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();

		// check for Internet status
		if (isInternetPresent) {
			setContentView(R.layout.mapview);
			mapView = (MapView) findViewById(R.id.mapview);
			mapView.setBuiltInZoomControls(true);

			drawableBlue = this.getResources().getDrawable(R.drawable.pushpin_blue);
			drawableRed = this.getResources().getDrawable(R.drawable.pushpin_red);

			PinOverlay pinsRed = new PinOverlay(drawableRed);
			double longitude = space.getSpaceLongitude();
			double latitude = space.getSpaceLatitude();

			p = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
			OverlayItem overlayitem = new OverlayItem(p, "", "");
			pinsRed.addOverlay(overlayitem, space);
			mapView.getOverlays().add(pinsRed);

			int clat = p.getLatitudeE6();
			int clon = p.getLongitudeE6();
			int cnt = 1;
			
			LocationManager locationManager = (LocationManager) this
					.getSystemService(Context.LOCATION_SERVICE);

			Criteria _criteria = new Criteria();
			String _bestProvider = locationManager.getBestProvider(_criteria, true);
			Location location = locationManager.getLastKnownLocation(_bestProvider);

			LocationListener loc_listener = new LocationListener() {
				public void onLocationChanged(Location l) {
				}

				public void onProviderEnabled(String p) {
				}

				public void onProviderDisabled(String p) {
				}

				public void onStatusChanged(String p, int status, Bundle extras) {
				}
			};
			locationManager.requestLocationUpdates(_bestProvider, 0, 0,
					loc_listener);
			location = locationManager.getLastKnownLocation(_bestProvider);

			if (location != null) {
				double gpsLat = location.getLatitude();
				double gpsLong = location.getLongitude();
				q = new GeoPoint((int) (gpsLat * 1E6), (int) (gpsLong * 1E6));
				clat += q.getLatitudeE6();
				clon += q.getLongitudeE6();
				cnt++;

				OverlayItem overlayitem1 = new OverlayItem(q, "", "");
				PinOverlay pinsBlue = new PinOverlay(drawableBlue);
				pinsBlue.addOverlay(overlayitem1, null);
				mapView.getOverlays().add(pinsBlue);
			}


			clat /= cnt;
			clon /= cnt;
			GeoPoint center = new GeoPoint(clat, clon);
			mc = mapView.getController();
			mc.animateTo(center);
			mc.setZoom(17);
		} else {
			// Internet connection is not present
			// Ask user to connect to Internet
			cd.showAlertDialog(CustomMap.this, "No Internet Connection",
					"You don't have internet connection.", false);
		}

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	/**
	 * Creates the PinOverlay used to display individual pointers for study
	 * spaces.
	 *
	 */	
	public class PinOverlay extends ItemizedOverlay<OverlayItem>{

		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		private ArrayList<StudySpace> mSpaces = new ArrayList<StudySpace>();
		
		public PinOverlay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));
		}

		public void addOverlay(OverlayItem overlay, StudySpace space) {
			mOverlays.add(overlay);
			mSpaces.add(space);
			populate();
		}

		@Override
		protected OverlayItem createItem(int i) {
			return mOverlays.get(i);
		}


		@Override
		public int size() {
			return mOverlays.size();
		}

		/**
		 * Generates the dialog used to tell user more details about a study
		 * space when he/she taps the pointer
		 */
		@Override
		protected boolean onTap(int index) {
			final StudySpace space = mSpaces.get(index);
			
			if (space == null)
				return true;
			Geocoder geoCoder = new Geocoder(
					getBaseContext(), Locale.getDefault());
			try {
				List<Address> addresses = geoCoder.getFromLocation(
						space.getSpaceLatitude(), space.getSpaceLongitude(), 1); 

				String add = "";
				if (addresses.size() > 0) 
				{
					for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); 
							i++)
						add += addresses.get(0).getAddressLine(i) + "\n";
				}

				AlertDialog.Builder builder = new AlertDialog.Builder(CustomMap.this);
				builder.setTitle("Location Information");
				builder.setMessage(space.getBuildingName() + ": " + space.getSpaceName() + 
						"\n" + add + "Distance: " + Math.round(space.getDistance()) + " m");
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				builder.setNegativeButton("Info", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();		
					}
				});
				AlertDialog alert = builder.create();
				alert.show();

			}
			catch (IOException e) {                
				e.printStackTrace();
			}   
			return true;
		}

	}


}
