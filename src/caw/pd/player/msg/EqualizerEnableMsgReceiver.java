package caw.pd.player.msg;

import caw.pd.player.fragments.UIPlayControlFragment;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class EqualizerEnableMsgReceiver extends BroadcastReceiver {
	private Fragment fragment;
	
	public EqualizerEnableMsgReceiver(Fragment fragment){
		this.fragment=fragment;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		boolean enabled=intent.getExtras().getBoolean("equalizer.enabled");
		((UIPlayControlFragment)fragment).enableEqualizerButton(enabled);
	}

}
