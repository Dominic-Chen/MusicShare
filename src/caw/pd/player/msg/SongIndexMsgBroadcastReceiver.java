package caw.pd.player.msg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import caw.pd.player.fragments.UIPlayControlFragment;

public class SongIndexMsgBroadcastReceiver extends BroadcastReceiver {
	private UIPlayControlFragment fragment;
	
	public SongIndexMsgBroadcastReceiver(UIPlayControlFragment fragment){
		this.fragment=fragment;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		int index=intent.getExtras().getInt("position.message");
		//fragment.refreshSongPosition(index);
	}
}
