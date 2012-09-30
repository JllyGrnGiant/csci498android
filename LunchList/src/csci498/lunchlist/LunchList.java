package csci498.lunchlist;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * The LunchList activity provides a form for users to enter and store
 * restaurant information and displays that information in a list
 */
@SuppressWarnings("deprecation")
public class LunchList extends ListActivity {

	public final static String ID_EXTRA = "apt.tutorial._ID";
	
	private RestaurantHelper  helper;
	private SharedPreferences prefs;
	private Cursor            model;
	
	private SharedPreferences.OnSharedPreferenceChangeListener prefListener =
		new SharedPreferences.OnSharedPreferenceChangeListener() {
			
		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPrefs, String key) {
			if (key.equals(getString(R.string.pref_key))) {
				setupRestaurantList();
			}
		}
	};
	
	/**
	 * A container for restaurant name, address, and icon resources
	 */
	private static class RestaurantHolder {
		
		private TextView  name;
		private TextView  address;
		private ImageView icon;
		
		RestaurantHolder(View row) {
			name    = (TextView) row.findViewById(R.id.title);
			address = (TextView) row.findViewById(R.id.address);
			icon    = (ImageView) row.findViewById(R.id.icon);
		}

		/**
		 * Updates the RestaurantHolder's fields with data pointed
		 * to by the Cursor obtained from the RestaurantHelper
		 */
		public void populateFrom(Cursor c, RestaurantHelper helper) {
			name.setText(helper.getName(c));
			address.setText(helper.getAddress(c));
			
			switch (helper.getType(c)) {
				case SIT_DOWN:
					icon.setImageResource(R.drawable.ball_green);
					break;
				case TAKE_OUT:
					icon.setImageResource(R.drawable.ball_yellow);
					break;
				default:
					icon.setImageResource(R.drawable.ball_red);
					break;
			}
		}
	}
	
	/**
	 * A custom adapter for displaying restaurant information stored
	 * in RestaurantHolder objects
	 */
	private class RestaurantAdapter extends CursorAdapter {
		
		public RestaurantAdapter(Cursor c) {
			super(LunchList.this, c);
		}
		
		@Override
		public void bindView(View row, Context context, Cursor c) {
			RestaurantHolder holder = (RestaurantHolder) row.getTag();
			holder.populateFrom(c, helper);
		}
		
		@Override
		public View newView(Context context, Cursor c, ViewGroup parent) {
			View row = getLayoutInflater().inflate(R.layout.restaurant_layout, parent, false);
			RestaurantHolder holder = new RestaurantHolder(row);
			row.setTag(holder);
			
			return row;
		}
	}
    
    private void setupDatabaseHelper() {
    	helper = new RestaurantHelper(this);
	}
    
    private void setupRestaurantList() {
    	if (model != null) {
    		stopManagingCursor(model);
    		model.close();
    	}
    	
    	model = helper.getAll(prefs.getString(
    		getString(R.string.pref_key), getString(R.string.pref_default)));
    	startManagingCursor(model);
    	setListAdapter(new RestaurantAdapter(model));
    }
    
    private void setupPreferences() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(prefListener);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_list);
        
        setupPreferences();
        setupDatabaseHelper();
        setupRestaurantList();
    }
    
    @Override
	public void onListItemClick(ListView list, View view, int position, long id) {
		Intent i = new Intent(LunchList.this, DetailForm.class);
		i.putExtra(ID_EXTRA, String.valueOf(id));
		startActivity(i);
    };
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == R.id.add) {
    		startActivity(new Intent(LunchList.this, DetailForm.class));
    		return true;
    	}
    	else if (item.getItemId() == R.id.prefs) {
    		startActivity(new Intent(this, EditPreferences.class));
    		return true;
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		helper.close();
	}
}