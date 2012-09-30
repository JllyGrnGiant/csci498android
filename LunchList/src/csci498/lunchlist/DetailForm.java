package csci498.lunchlist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class DetailForm extends Activity {
	
	private EditText         name;
	private EditText         address;
	private EditText         notes;
	private RadioGroup       types;
	private RestaurantHelper helper;
	
	private View.OnClickListener onSave = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Restaurant save = getRestaurantToSave();
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_form);
		
		setupDatabaseHelper();
    	setupViews();
    	setupSaveButton();
	}
    
	private void setupDatabaseHelper() {
    	helper = new RestaurantHelper(this);
	}
    
	private void setupViews() {
		name    = (EditText) findViewById(R.id.name);
		address = (EditText) findViewById(R.id.addr);
		types   = (RadioGroup) findViewById(R.id.types);
		notes   = (EditText) findViewById(R.id.notes);
    }
    
    private void setupSaveButton() {
    	Button save = (Button) findViewById(R.id.save);
        save.setOnClickListener(onSave);
    }
}