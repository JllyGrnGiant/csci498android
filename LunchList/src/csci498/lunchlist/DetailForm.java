package csci498.lunchlist;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * DetailForm is started by LunchList to edit and/or
 * add restaurants to the database for display in the
 * LunchList activity
 */
public class DetailForm extends Activity {
	
	private EditText         name;
	private EditText         address;
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
			helper.updateLocation(restaurantId, fix.getLatitude(), fix.getLongitude());
			location.setText(String.valueOf(fix.getLatitude())
				+ ", " + String.valueOf(fix.getLongitude()));
			locMgr.removeUpdates(onLocationChange);
			
			Toast.makeText(DetailForm.this, "Location saved", Toast.LENGTH_LONG).show();
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		
		latitude  = 0;
		longitude = 0;
		
		setupDatabaseHelper();
    	setupViews();
    	setupLocationManager();
    	setRestaurantId();
    	loadDatabaseData();
	}
	
	private void save() {
		if (name.getText().toString().length() > 0) {
			Restaurant save = getRestaurantToSave();
			
			if (restaurantId == null) {
				helper.insert(save.getName(), save.getAddress(),
					save.getType(), save.getNotes(),
					feed.getText().toString());
			}
			else {
				helper.update(restaurantId, save.getName(),
					save.getAddress(), save.getType(),
					save.getNotes(), feed.getText().toString());
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
		locMgr = (LocationManager) getSystemService(LOCATION_SERVICE);
	}

	private void setRestaurantId() {
		restaurantId = getIntent().getStringExtra(LunchList.ID_EXTRA);
	}

	private void setupDatabaseHelper() {
    	helper = new RestaurantHelper(this);
	}
    
	private void setupViews() {
		name     = (EditText) findViewById(R.id.name);
		address  = (EditText) findViewById(R.id.addr);
		types    = (RadioGroup) findViewById(R.id.types);
		notes    = (EditText) findViewById(R.id.notes);
		feed     = (EditText) findViewById(R.id.feed);
		location = (TextView) findViewById(R.id.location);
    }

	private void loadDatabaseData() {
		if (restaurantId == null) {
			return;
		}
		
		Cursor c = helper.getById(restaurantId);
		c.moveToFirst();
		
		name.setText(helper.getName(c));
		address.setText(helper.getAddress(c));
		notes.setText(helper.getNotes(c));
		feed.setText(helper.getFeed(c));
		
		latitude  = helper.getLatitude(c);
		longitude = helper.getLongitude(c);
		
		location.setText(String.valueOf(latitude)
			+ ", " + String.valueOf(longitude)
		);
		
		switch (helper.getType(c)) {
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
	public void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		
		name.setText(state.getString("name"));
		address.setText(state.getString("address"));
		notes.setText(state.getString("notes"));
		types.check(state.getInt("type"));
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.details_option, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.feed) {
			if (isNetworkAvailable()) {
				Intent i = new Intent(this, FeedActivity.class);
				i.putExtra(FeedActivity.FEED_URL, feed.getText().toString());
				startActivity(i);
			}
			else {
				Toast.makeText(this, "Sorry, the Internet has been destroyed", Toast.LENGTH_LONG).show();
			}
			
			return true;
		}
		else if (item.getItemId() == R.id.location) {
			locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, onLocationChange);
			
			return true;
		}
		else if (item.getItemId() == R.id.map) {
			Intent i = new Intent(this, RestaurantMap.class);
			i.putExtra(RestaurantMap.EXTRA_LATITUDE, latitude);
			i.putExtra(RestaurantMap.EXTRA_LONGITUDE, longitude);
			i.putExtra(RestaurantMap.EXTRA_NAME, name.getText().toString());
			startActivity(i);
			
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (restaurantId == null) {
			menu.findItem(R.id.location).setEnabled(false);
			menu.findItem(R.id.map).setEnabled(false);
		}
		
		return super.onPrepareOptionsMenu(menu);
	}
	
	private boolean isNetworkAvailable() {
		ConnectivityManager cm   = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo         info = cm.getActiveNetworkInfo();
		
		return (info != null);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		save();
		locMgr.removeUpdates(onLocationChange);
	}
	
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	helper.close();
    }
}