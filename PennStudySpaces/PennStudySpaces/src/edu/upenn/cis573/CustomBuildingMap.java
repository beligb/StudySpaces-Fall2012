package edu.upenn.cis573;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

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
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class CustomBuildingMap extends MapActivity {

	LinearLayout linearLayout;
	MapView mapView;
	MapController mc;
	GeoPoint p;
	GeoPoint q;
	GeoPoint avg;
	Drawable drawableRed, drawableBlue;
	Preferences pref;
	private ArrayList<StudySpace> olist;
	
	Boolean isInternetPresent = false;

	// Connection detector class
	ConnectionDetector cd;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = super.getIntent();
		pref = (Preferences) i.getSerializableExtra("PREFERENCES");
		olist = (ArrayList<StudySpace>) i.getSerializableExtra("STUDYSPACELIST");

		cd = new ConnectionDetector(getApplicationContext());

		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();

		// check for Internet status
		if (isInternetPresent) {
			setContentView(R.layout.buildingmapview);
			mapView = (MapView) findViewById(R.id.mapview);
			mapView.setBuiltInZoomControls(true);

			drawableBlue = this.getResources().getDrawable(R.drawable.pushpin_blue);
			drawableRed = this.getResources().getDrawable(R.drawable.pushpin_red);
			
			int clat = 0;
			int clon = 0;
			int cnt = 0;

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
				++cnt;

				OverlayItem overlayitem = new OverlayItem(q, "", "");
				PinOverlay pinsBlue = new PinOverlay(drawableBlue);
				pinsBlue.addOverlay(overlayitem, null);
				mapView.getOverlays().add(pinsBlue);
			}

			PinOverlay pinsRed = new PinOverlay(drawableRed);
			Set<String> buildings = new HashSet<String>();
			for (StudySpace o: olist)
				buildings.add(o.getBuildingName());
			for (String buildingName : buildings) {
				ArrayList<StudySpace> rooms = new ArrayList<StudySpace>();
				for (StudySpace o: olist) {
					if (o.getBuildingName().equals(buildingName)) {
						rooms.add(o);
					}
				}
				double longitude = rooms.get(0).getSpaceLongitude();
				double latitude = rooms.get(0).getSpaceLatitude();
				p = new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
				clat += p.getLatitudeE6();
				clon += p.getLongitudeE6();
				++cnt;
				OverlayItem overlayitem = new OverlayItem(p, "", "");
				pinsRed.addOverlay(overlayitem, new Building(rooms));
	
//				avgLong += longitude;
//				avgLat += latitude;
			}
			mapView.getOverlays().add(pinsRed);

			clat /= cnt;
			clon /= cnt;
			GeoPoint center = new GeoPoint(clat, clon);
			mc = mapView.getController();
			mc.animateTo(center);
			mc.setZoom(17);
		} else {
			// Internet connection is not present
			// Ask user to connect to Internet
			cd.showAlertDialog(CustomBuildingMap.this, "No Internet Connection",
					"You don't have internet connection.", false);
		}

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	public class PinOverlay extends ItemizedOverlay<OverlayItem>{

		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
		private ArrayList<Building> mBuildings = new ArrayList<Building>();
		
		public PinOverlay(Drawable defaultMarker) {
			super(boundCenterBottom(defaultMarker));
		}

		public void addOverlay(OverlayItem overlay, Building building) {
			mOverlays.add(overlay);
			mBuildings.add(building);
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

		@Override
		protected boolean onTap(int index) {
			final Building building = mBuildings.get(index);
			
			if (building == null)
				return true;
			Geocoder geoCoder = new Geocoder(
					getBaseContext(), Locale.getDefault());
			try {
				List<Address> addresses = geoCoder.getFromLocation(
						building.getSpaceLatitude(), building.getSpaceLongitude(), 1); 

				String add = "";
				if (addresses.size() > 0) 
				{
					for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); 
							i++)
						add += addresses.get(0).getAddressLine(i) + "\n";
				}

				AlertDialog.Builder builder = new AlertDialog.Builder(CustomBuildingMap.this);
				builder.setTitle("Building Information");
				builder.setMessage(building.getBuildingName() + ": " + building.getRoomCount() + 
						" rooms \n" + add + "Distance: " + Math.round(building.getDistance()) + " m");
				builder.setPositiveButton("Back to Map", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				builder.setNegativeButton("View Rooms", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						Intent i = new Intent();
						i.putExtra("STUDYSPACELIST", building.getRooms());
						setResult(RESULT_OK, i);
						//ends this activity
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Intent i = new Intent();
			ArrayList<StudySpace> nlist = new ArrayList<StudySpace>();
			i.putExtra("STUDYSPACELIST", nlist);
			setResult(RESULT_OK, i);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
