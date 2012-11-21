package csci498.lunchlist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * DetailForm is started by LunchList to edit and/or
 * add restaurants to the database for display in the
 * LunchList activity
 */
public class DetailFragment extends Fragment {
	
	private static final String ARG_REST_ID = "apt.tutorial.ARG_REST_ID";
	
	private EditText         name;
	private EditText         address;
	private EditText         phone;
	private EditText         notes;
	private EditText         feed;
	private TextView         location;
	private RadioGroup       types;
	private RestaurantHelper helper;
	private String           restaurantId;
	private LocationManager  locMgr;
	private double           latitude;
	private double           longitude;

	LocationListener onLocationChange = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// Not used
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// Not used
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// Not used
		}
		
		@Override
		public void onLocationChanged(Location fix) {
			getHelper().updateLocation(restaurantId, fix.getLatitude(), fix.getLongitude());
			location.setText(String.valueOf(fix.getLatitude()) + ", " + String.valueOf(fix.getLongitude()));
			locMgr.removeUpdates(onLocationChange);
			
			Toast.makeText(getActivity(), getString(R.string.loc_saved), Toast.LENGTH_LONG).show();
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		latitude  = 0;
		longitude = 0;
		
    	setupViews();
    	setupLocationManager();
    	
    	Bundle args = getArguments();
    	if (args != null) {
    		loadRestaurant(args.getString(ARG_REST_ID));
    	}
	}
	
	public RestaurantHelper getHelper() {		
		if (helper == null) {
			helper = new RestaurantHelper(getActivity());
		}

		return helper;
	}
	
	public void loadRestaurant(String restaurantId) {
		this.restaurantId = restaurantId;
		if (restaurantId != null) {
			loadDatabaseData();
		}
	}
	
	public static DetailFragment newInstance(long id) {
		DetailFragment result = new DetailFragment();
		
		Bundle args = new Bundle();
		args.putString(ARG_REST_ID, String.valueOf(id));
		result.setArguments(args);
		
		return result;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.detail_form, container, false);
	}
	
	private void save() {
		if (name.getText().toString().length() > 0) {
			Restaurant save = getRestaurantToSave();
			
			if (restaurantId == null) {
				getHelper().insert(save.getName(), save.getAddress(),
					save.getType(), save.getNotes(),
					feed.getText().toString(), phone.getText().toString());
			}
			else {
				getHelper().update(restaurantId, save.getName(),
					save.getAddress(), save.getType(),
					save.getNotes(), feed.getText().toString(), phone.getText().toString());
			}
		}
	}
	
	private Restaurant getRestaurantToSave() {
		Restaurant save = new Restaurant();
		save.setName(name.getText().toString());
		save.setAddress(address.getText().toString());
		save.setNotes(notes.getText().toString());
		
		switch (types.getCheckedRadioButtonId()) {
			case R.id.sit_down:
				save.setType(RestaurantType.SIT_DOWN);
				break;
			case R.id.take_out:
				save.setType(RestaurantType.TAKE_OUT);
				break;
		    case R.id.delivery:
		    	save.setType(RestaurantType.DELIVERY);
				break;
		}
		
		return save;
	}
	
	private void setupLocationManager() {
		locMgr = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
	}

	private void setRestaurantId() {
		restaurantId = getActivity().getIntent().getStringExtra(LunchList.ID_EXTRA);
	}
    
	private void setupViews() {
		name     = (EditText) getActivity().findViewById(R.id.name);
		address  = (EditText) getActivity().findViewById(R.id.addr);
		types    = (RadioGroup) getActivity().findViewById(R.id.types);
		phone    = (EditText) getActivity().findViewById(R.id.phone);
		notes    = (EditText) getActivity().findViewById(R.id.notes);
		feed     = (EditText) getActivity().findViewById(R.id.feed);
		location = (TextView) getActivity().findViewById(R.id.location);
    }

	private void loadDatabaseData() {
		if (restaurantId == null) {
			return;
		}
		
		Cursor c = getHelper().getById(restaurantId);
		c.moveToFirst();
		
		name.setText(getHelper().getName(c));
		address.setText(getHelper().getAddress(c));
		notes.setText(getHelper().getNotes(c));
		feed.setText(getHelper().getFeed(c));
		phone.setText(getHelper().getPhone(c));
		
		latitude  = getHelper().getLatitude(c);
		longitude = getHelper().getLongitude(c);
		
		location.setText(String.valueOf(latitude)
			+ ", " + String.valueOf(longitude)
		);
		
		switch (getHelper().getType(c)) {
			case SIT_DOWN:
				types.check(R.id.sit_down);
				break;
			case TAKE_OUT:
				types.check(R.id.take_out);
				break;
		    case DELIVERY:
		    	types.check(R.id.delivery);
				break;
		}
		
		c.close();
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		
		state.putString("name", name.getText().toString());
		state.putString("address", address.getText().toString());
		state.putString("notes", notes.getText().toString());
		state.putInt("type", types.getCheckedRadioButtonId());
	}
    
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.details_option, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.feed) {
			if (isNetworkAvailable()) {
				Intent i = new Intent(getActivity(), FeedActivity.class);
				i.putExtra(FeedActivity.FEED_URL, feed.getText().toString());
				startActivity(i);
			}
			else {
				Toast.makeText(getActivity(), "Sorry, the Internet has been destroyed", Toast.LENGTH_LONG).show();
			}
			
			return true;
		}
		else if (item.getItemId() == R.id.location) {
			locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, onLocationChange);
			
			return true;
		}
		else if (item.getItemId() == R.id.map) {
			Intent i = new Intent(getActivity(), RestaurantMap.class);
			i.putExtra(RestaurantMap.EXTRA_LATITUDE, latitude);
			i.putExtra(RestaurantMap.EXTRA_LONGITUDE, longitude);
			i.putExtra(RestaurantMap.EXTRA_NAME, name.getText().toString());
			startActivity(i);
			
			return true;
		}
		else if (item.getItemId() == R.id.call) {
			String toDial = "tel:" + phone.getText().toString();
			
			if (toDial.length() > 4) {
				startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(toDial)));
			}
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if (restaurantId == null) {
			menu.findItem(R.id.location).setEnabled(false);
			menu.findItem(R.id.map).setEnabled(false);
		}
		
		if (isTelephonyAvailable()) {
			menu.findItem(R.id.call).setEnabled(true);
		}
	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager cm   = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo         info = cm.getActiveNetworkInfo();
		
		return (info != null);
	}
	
	private boolean isTelephonyAvailable() {
		return getActivity().getPackageManager().hasSystemFeature("android.hardware.telephony");
	}
	
	@Override
	public void onPause() {
		super.onPause();
		save();
		locMgr.removeUpdates(onLocationChange);
		getHelper().close();
	}
}