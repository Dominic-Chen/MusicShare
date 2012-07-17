package caw.pd.player.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import caw.pd.R;

public class PlayPreFragment extends PreferenceFragment{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		this.addPreferencesFromResource(R.xml.conf_setting_play_pre);
	}
}
