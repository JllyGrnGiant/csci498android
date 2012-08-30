/**
 * The LunchList activity provides a form for users to enter and store
 * restaurant information and displays that information in a list
 */

package csci498.lunchlist;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;

public class LunchList extends Activity {

	private List<Restaurant> model                   = new ArrayList<Restaurant>();
	private RestaurantAdapter adapter    = null;
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
	
	private class RestaurantAdapter extends ArrayAdapter<Restaurant> {
		public RestaurantAdapter() {
			super(LunchList.this,
				android.R.layout.simple_list_item_1,
				model);
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
        adapter = new RestaurantAdapter();
        list.setAdapter(adapter);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_lunch_list, menu);
        return true;
    }
    
}
