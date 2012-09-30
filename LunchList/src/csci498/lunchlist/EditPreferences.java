package csci498.lunchlist;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * EditPreferences allows the user to edit the application preferences
 */
public class EditPreferences extends PreferenceActivity {

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}