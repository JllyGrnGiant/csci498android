package csci498.lunchlist;

import android.app.Activity;
import android.os.Bundle;

/**
 * The AlarmActivity holds a lunch notification
 * to display to the user
 */
public class AlarmActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm);
	}
}
