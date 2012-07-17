package caw.pd.player.msg;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import caw.pd.model.Mp3Info;
import caw.pd.player.fragments.PlayInforFragment;
import caw.pd.player.fragments.UIPlayControlFragment;

public class SongInfoMsgBroadcastReceiver extends BroadcastReceiver {
	private Fragment fragment;
	
	public SongInfoMsgBroadcastReceiver(Fragment fragment){
		this.fragment=fragment;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if(fragment instanceof UIPlayControlFragment){
			Mp3Info mp3info=(Mp3Info)intent.getExtras().get("refresh.message");
			int state=intent.getExtras().getInt("current.status");
			if(mp3info!=null){
				((UIPlayControlFragment)fragment).refreshSongInfor(state);
			}
		}else if(fragment instanceof PlayInforFragment){
			Mp3Info mp3info=(Mp3Info)intent.getExtras().get("refresh.message");
			int position=intent.getExtras().getInt("current.postion");
			if(mp3info!=null){
				((PlayInforFragment)fragment).refreshSongInfor(mp3info,position);
			}
		}
	}

}
