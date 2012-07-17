package caw.pd.player.msg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class EqualizerConfgMsgReciever extends BroadcastReceiver {
	private MediaPlayer mMediaPlayer;
	private Visualizer mVisualizer;
    private Equalizer mEqualizer;

	public EqualizerConfgMsgReciever(MediaPlayer player){
		this.mMediaPlayer=player;
	}
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		short band=arg1.getShortExtra("equalizer.band", (short)0);
		short level=arg1.getShortExtra("equalizer.level", (short)0);
		mEqualizer.setBandLevel(band, level);
	}
}
