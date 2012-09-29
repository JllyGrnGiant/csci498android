package csci498.lunchlist;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.TabActivity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The LunchList activity provides a form for users to enter and store
 * restaurant information and displays that information in a list
 */
@SuppressWarnings("deprecation")
public class LunchList extends TabActivity {

	private List<Restaurant> model;
	private RestaurantAdapter adapter;
	private ArrayAdapter<String> addressSuggestions;
	private EditText name;
	private EditText address;
	private RadioGroup types;
	private Restaurant current;
	private EditText notes;
	private int progress;
	private AtomicBoolean isActive;

	private Runnable longTask = new Runnable() {
		@Override
		public void run() {			
			for (int i=progress; i<10000 && isActive.get(); i+=200) {
				doSomeLongWork(200);
			}
			
			if (isActive.get()) {
				resetProgress();
			}
		}
		
		private void resetProgress() {
			runOnUiThread(new Runnable() {
				public void run() {
					setProgressBarVisibility(false);
					progress = 0;
				}
			});
		}
	};

	private View.OnClickListener onSave = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			current = getRestaurantToSave();
			adapter.add(current);
			addressSuggestions.add(current.getAddress());
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
	};
	
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			current = model.get(position);
			updateRestaurantTab(current);
			getTabHost().setCurrentTab(1);
		}
		
		private void updateRestaurantTab(final Restaurant current) {
			name.setText(current.getName());
			address.setText(current.getAddress());
			notes.setText(current.getNotes());

			switch (current.getType()) {
				case SIT_DOWN:
					types.check(R.id.sit_down);
					break;
				case TAKE_OUT:
					types.check(R.id.take_out);
					break;
				default:
					types.check(R.id.delivery);
					break;
			}
		}
    };
	
	/**
	 * A container for restaurant name, address, and icon resources
	 */
	private static class RestaurantHolder {
		
		private TextView name;
		private TextView address;
		private ImageView icon;
		
		RestaurantHolder(View row) {
			name    = (TextView) row.findViewById(R.id.title);
			address = (TextView) row.findViewById(R.id.address);
			icon    = (ImageView) row.findViewById(R.id.icon);
		}
		
		void populateFrom(Restaurant r) {
			name.setText(r.getName());
			address.setText(r.getAddress());

			switch (r.getType()) {
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
	private class RestaurantAdapter extends ArrayAdapter<Restaurant> {
		public RestaurantAdapter() {
			super(LunchList.this, android.R.layout.simple_list_item_1, model);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			RestaurantHolder holder = null;
			
			if (row == null) {
				row = getLayoutInflater().inflate(R.layout.restaurant_layout, null);
				holder = new RestaurantHolder(row);
				row.setTag(holder);
			}
			else {
				holder = (RestaurantHolder) row.getTag();
			}
			
			holder.populateFrom(model.get(position));
			
			return row;
		}
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_lunch_list);
        
		setupViews();
		setupSaveButton();
        setupRestaurantList();
        setupTabs();
    }
    
    private void setupViews() {
    	name     = (EditText) findViewById(R.id.name);
		address  = (EditText) findViewById(R.id.addr);
		types    = (RadioGroup) findViewById(R.id.types);
		notes    = (EditText) findViewById(R.id.notes);
		model    = new ArrayList<Restaurant>();
		isActive = new AtomicBoolean();
        
        AutoCompleteTextView addressView = (AutoCompleteTextView) findViewById(R.id.addr);
        addressSuggestions = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        addressView.setAdapter(addressSuggestions);
    }
    
    private void setupSaveButton() {
    	Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(onSave);
    }
    
    private void setupRestaurantList() {
    	adapter = new RestaurantAdapter();
    	ListView list = (ListView) findViewById(R.id.restaurants);
        list.setAdapter(adapter);
        list.setOnItemClickListener(onListClick);
    }

	private void setupTabs() {
		TabHost.TabSpec spec = getTabHost().newTabSpec(getString(R.string.list_tab));
        spec.setContent(R.id.restaurants);
        spec.setIndicator(getString(R.string.list), getResources().getDrawable(R.drawable.list));
        getTabHost().addTab(spec);
        
        spec = getTabHost().newTabSpec(getString(R.string.details_tab));
        spec.setContent(R.id.details);
        spec.setIndicator(getString(R.string.details), getResources().getDrawable(R.drawable.restaurant));
        getTabHost().addTab(spec);
        
        getTabHost().setCurrentTab(0);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	if (item.getItemId() == R.id.toast) {
    		String message = getString(R.string.toast_default);
    		if (current != null) {
    			message = current.getNotes();
    		}
    		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    		
    		return true;
    	}
    	else if (item.getItemId() == R.id.run) {
    		startWork();
    		return true;
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
    
    private void doSomeLongWork(final int incr) {
    	runOnUiThread(new Runnable() {
    		public void run() {
	        	progress += incr;
	        	setProgress(progress);
    		}
    	});
    	
    	SystemClock.sleep(250);
    }
    
    @Override
    public void onPause() {
    	super.onPause();
    	isActive.set(false);
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	isActive.set(true);
    	if (progress > 0) {
    		startWork();
    	}
    }

	private void startWork() {
		setProgressBarVisibility(true);
		new Thread(longTask).start();
	}
}