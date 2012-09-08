package csci498.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
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
import android.widget.ViewFlipper;

/**
 * The LunchList activity provides a form for users to enter and store
 * restaurant information and displays that information in a list
 */
public class LunchList extends Activity {

	private List<Restaurant> model                   = new ArrayList<Restaurant>();
	private RestaurantAdapter adapter                = null;
	private ArrayAdapter<String> addressSuggestions  = null;
	private EditText name                            = null;
	private EditText address                         = null;
	private RadioGroup types                         = null;
	private ViewFlipper flipper                      = null;
    
	private View.OnClickListener onSave = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Restaurant r = new Restaurant();
			
			r.setName(name.getText().toString());
			r.setAddress(address.getText().toString());
			
			switch (types.getCheckedRadioButtonId()) {
				case R.id.sit_down:
					r.setType("sit_down");
					break;
				case R.id.take_out:
					r.setType("take_out");
					break;
			    case R.id.delivery:
					r.setType("delivery");
					break;
			}
			
			adapter.add(r);
			addressSuggestions.add(r.getAddress());
		}
		
	};
	
	private View.OnClickListener onFlip = new View.OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			flipper.showNext();
		}
	};
	
	private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Restaurant r = model.get(position);
			
			name.setText(r.getName());
			address.setText(r.getAddress());
			
			if (r.getType().equals("sit_down")) {
				types.check(R.id.sit_down);
			}
			else if (r.getType().equals("take_out")) {
				types.check(R.id.take_out);
			}
			else {
				types.check(R.id.delivery);
			}
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
        setContentView(R.layout.activity_lunch_list);
        
		name    = (EditText) findViewById(R.id.name);
		address = (EditText) findViewById(R.id.addr);
		types   = (RadioGroup) findViewById(R.id.types);
		flipper = (ViewFlipper) findViewById(R.id.flipper);
        
        AutoCompleteTextView addressView = (AutoCompleteTextView) findViewById(R.id.addr);
        addressSuggestions = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        addressView.setAdapter(addressSuggestions);
        
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(onSave);
        
        Button flip = (Button) findViewById(R.id.flip);
        flip.setOnClickListener(onFlip);
        
        ListView list = (ListView) findViewById(R.id.restaurants);
        adapter       = new RestaurantAdapter();
        list.setAdapter(adapter);
        list.setOnItemClickListener(onListClick);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_lunch_list, menu);
        return true;
    }
}
