package csci498.lunchlist;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * The AlarmActivity holds a lunch notification
 * to display to the user
 */
public class AlarmActivity extends Activity implements MediaPlayer.OnPreparedListener {
	MediaPlayer player;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm);
		
		player = new MediaPlayer();
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String sound = prefs.getString("alarm_ringtone", null);
		
		if (sound != null) {
			player.setAudioStreamType(AudioManager.STREAM_ALARM);
			
			try {
				player.setDataSource(sound);
				player.setOnPreparedListener(this);
				player.prepareAsync();
			}
			catch (Exception e) {
				Log.e("LunchList", "Exception in playing ringtone", e);
			}
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		if (player.isPlaying()) {
			player.stop();
		}
	}
	
	public void onPrepared(MediaPlayer player) {
		player.start();
	}
}
