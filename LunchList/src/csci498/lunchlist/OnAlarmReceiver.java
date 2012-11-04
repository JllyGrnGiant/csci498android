package csci498.lunchlist;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * OnAlarmReceiver starts a notification when a lunch alarm is received.
 */
public class OnAlarmReceiver extends BroadcastReceiver {

	private static final String message = "It's time for lunch!";
	private static final int    NOTIFY_ME_ID = 1337;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		boolean useNotification       = preferences.getBoolean("use_notification", true);
		
		if (useNotification) {
			NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			Notification note = new Notification(R.drawable.stat_notify_chat, message, System.currentTimeMillis());
			PendingIntent i = PendingIntent.getActivity(context, 0, new Intent(context, AlarmActivity.class), 0);
			
			note.setLatestEventInfo(context, "LunchList", message, i);
			note.flags |= Notification.FLAG_AUTO_CANCEL;
			manager.notify(NOTIFY_ME_ID, note);
		}
		else {
			Intent i = new Intent(context, AlarmActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(i);
		}
	}
}