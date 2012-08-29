package csci498.lunchlist;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class LunchList extends Activity {

	private Restaurant r                = new Restaurant();
    private View.OnClickListener onSave = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			EditText name    = (EditText) findViewById(R.id.name);
			EditText address = (EditText) findViewById(R.id.addr);
			RadioGroup types = (RadioGroup) findViewById(R.id.types);
			
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
		}
		
	};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lunch_list);
        
        Button save = (Button) findViewById(R.id.save);
        
        save.setOnClickListener(onSave);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_lunch_list, menu);
        return true;
    }
    
}
