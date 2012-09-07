package csci498.lunchlist;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	
	final static int year;
	final static int month;
	final static int day;
	
	static {
		final Calendar c = Calendar.getInstance();
		year  = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day   = c.get(Calendar.DAY_OF_MONTH);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		// TODO Auto-generated method stub	
	}
}