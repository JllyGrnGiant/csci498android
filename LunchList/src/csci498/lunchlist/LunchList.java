package csci498.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.app.TabActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
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
public class LunchList extends TabActivity {

	private List<Restaurant> model                   = new ArrayList<Restaurant>();
	private RestaurantAdapter adapter                = null;
	private ArrayAdapter<String> addressSuggestions  = null;
	private EditText name                            = null;
	private EditText address                         = null;
	private RadioGroup types                         = null;
	private Restaurant current                       = null;
	private EditText notes                           = null;
	private int progress;
	private Handler handler                          = new Handler();
    
	private Runnable longTask = new Runnable() {
		
		@Override
		public void run() {
			for (int i=0; i<20; ++i) {
				doSomeLongWork(500);
			}
			
			handler.post(new Runnable() {
				public void run() {
					setProgressBarVisibility(false);
				}
			});
		}
		
	};
	
	private View.OnClickListener onSave = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			current = new Restaurant();
			
			current.setName(name.getText().toString());
			current.setAddress(address.getText().toString());
			current.setNotes(notes.getText().toString());
			
			switch (types.getCheckedRadioButtonId()) {
				case R.id.sit_down:
					current.setType("sit_down");
					break;
				case R.id.take_out:
					current.setType("take_out");
					break;
			    case R.id.delivery:
			    	current.setType("delivery");
					break;
			}
			
			adapter.add(current);
			addressSuggestions.add(current.getAddress());
		}
		
	};
	
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			current = model.get(position);
			
			name.setText(current.getName());
			address.setText(current.getAddress());
			notes.setText(current.getNotes());
			
			if (current.getType().equals("sit_down")) {
				types.check(R.id.sit_down);
			}
			else if (current.getType().equals("take_out")) {
				types.check(R.id.take_out);
			}
			else {
				types.check(R.id.delivery);
			}
			
			getTabHost().setCurrentTab(1);
		}
    };
	
	/**
	 * A container for restaurant name, address, and icon resources
	 */
	private static class RestaurantHolder {
		private TextView name    = null;
		private TextView address = null;
		private ImageView icon   = null;
		
		RestaurantHolder(View row) {
			name    = (TextView) row.findViewById(R.id.title);
			address = (TextView) row.findViewById(R.id.address);
			icon    = (ImageView) row.findViewById(R.id.icon);
		}
		
		void populateFrom(Restaurant r) {
			name.setText(r.getName());
			address.setText(r.getAddress());
			
			if (r.getType().equals("sit_down"))
				{ icon.setImageResource(R.drawable.ball_green); }
			else if (r.getType().equals("take_out"))
				{ icon.setImageResource(R.drawable.ball_yellow); }
			else
				{ icon.setImageResource(R.drawable.ball_red); }
		}
	}
	
	/**
	 * A custom adapter for displaying restaurant information stored
	 * in RestaurantHolder objects
	 */
	private class RestaurantAdapter extends ArrayAdapter<Restaurant> {
		public RestaurantAdapter() {
			super(LunchList.this,
				android.R.layout.simple_list_item_1,
				model);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			RestaurantHolder holder = null;
			
			if (row == null) {
				LayoutInflater inflater = getLayoutInflater();
				row = inflater.inflate(R.layout.restaurant_layout, null);
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
        
		name    = (EditText) findViewById(R.id.name);
		address = (EditText) findViewById(R.id.addr);
		types   = (RadioGroup) findViewById(R.id.types);
		notes   = (EditText) findViewById(R.id.notes);
        
        AutoCompleteTextView addressView = (AutoCompleteTextView) findViewById(R.id.addr);
        addressSuggestions = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        addressView.setAdapter(addressSuggestions);
        
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(onSave);
        
        ListView list = (ListView) findViewById(R.id.restaurants);
        adapter       = new RestaurantAdapter();
        list.setAdapter(adapter);
        list.setOnItemClickListener(onListClick);
        
        TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");
        
        spec.setContent(R.id.restaurants);
        spec.setIndicator("List", getResources().getDrawable(R.drawable.list));
        
        getTabHost().addTab(spec);
        
        spec = getTabHost().newTabSpec("tag2");
        spec.setContent(R.id.details);
        spec.setIndicator("Details", getResources().getDrawable(R.drawable.restaurant));
        
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
    		String message = "No restaurant selected";
    		
    		if (current != null) {
    			message = current.getNotes();
    		}
    		
    		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    		
    		return true;
    	}
    	else if (item.getItemId() == R.id.run) {
    		setProgressBarVisibility(true);
    		progress = 0;
    		new Thread(longTask).start();
    		
    		return true;
    	}
    	
    	return super.onOptionsItemSelected(item);
    }
    
    private void doSomeLongWork(final int incr) {
    	handler.post(new Runnable() {
    		public void run() {
	        	progress += incr;
	        	setProgress(progress);
    		}
    	});

    	
    	SystemClock.sleep(250);
    }
}
