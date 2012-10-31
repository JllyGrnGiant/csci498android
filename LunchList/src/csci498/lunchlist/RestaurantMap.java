package csci498.lunchlist;

import android.os.Bundle;

import com.google.android.maps.MapActivity;

public class RestaurantMap extends MapActivity {

	public static final String EXTRA_LATITUDE="apt.tutorial.EXTRA_LATITUDE";
	public static final String EXTRA_LONGITUDE="apt.tutorial.EXTRA_LONGITUDE";
	public static final String EXTRA_NAME="apt.tutorial.EXTRA_NAME";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
