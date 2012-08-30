package csci498.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * The LunchList activity provides a form for users to enter and store
 * restaurant information and displays that information in a list
 */
public class LunchList extends Activity {

	private List<Restaurant> model                   = new ArrayList<Restaurant>();
	private RestaurantAdapter adapter                = null;
	private ArrayAdapter<String> addressSuggestions  = null;
    
	private View.OnClickListener onSave = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			EditText name    = (EditText) findViewById(R.id.name);
			EditText address = (EditText) findViewById(R.id.addr);
			RadioGroup types = (RadioGroup) findViewById(R.id.types);
			Restaurant r     = new Restaurant();
			
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
			
			if (name.getText().length() > 6)
				{ name.setTextColor(Color.RED); }
			
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
				
				String type = model.get(position).getType();
				if (type.equals("sit_down"))
					{ row = inflater.inflate(R.layout.sit_down_row, null); }
				else if (type.equals("take_out"))
					{ row = inflater.inflate(R.layout.take_out_row, null); }
				else if (type.equals("delivery"))
					{ row = inflater.inflate(R.layout.delivery_row, null); }
				else
					{ row = inflater.inflate(android.R.layout.simple_list_item_1, null); }

				holder = new RestaurantHolder(row);
				row.setTag(holder);
			}
			else {
				holder = (RestaurantHolder) row.getTag();
			}
			
			holder.populateFrom(model.get(position));
			
			return row;
		}
		
		@Override
		public int getItemViewType(int position) {
			String type = model.get(position).getType();
			if (type.equals("sit_down")) 
				{ return 0; }
			else if (type.equals("take_out"))
				{ return 1; }
			else if (type.equals("delivery"))
				{ return 2; }
			else
				{ return 3; }
		}
		
		@Override
		public int getViewTypeCount() {
			return 4;
		}
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_list);
        
        AutoCompleteTextView addressView = (AutoCompleteTextView) findViewById(R.id.addr);
        addressSuggestions = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        addressView.setAdapter(addressSuggestions);
        
        Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(onSave);
        
        ListView list = (ListView) findViewById(R.id.restaurants);
        adapter       = new RestaurantAdapter();
        list.setAdapter(adapter);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_lunch_list, menu);
        return true;
    }
    
}
