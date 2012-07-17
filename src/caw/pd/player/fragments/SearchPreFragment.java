package caw.pd.player.fragments;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import caw.pd.R;

public class SearchPreFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{
	private EditTextPreference filePathPref=null;
	private SwitchPreference isAutoSearchPref=null;
	private SwitchPreference isNestedPref=null;
	private ListPreference fileTypesPref=null;
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		Resources res=getResources();
		
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
		filePathPref.setSummary(getString(R.string.text_file_path)+filePathPref.getSharedPreferences().getString(res.getString(R.string.KEY_OF_FILE_PREF_PATH), ""));
		isAutoSearchPref.setSummary(getString(R.string.text_auto_search)+filePathPref.getSharedPreferences().getBoolean(res.getString(R.string.KEY_OF_FILE_PREF_AUTOSEARCH), false));
		isNestedPref.setSummary(getString(R.string.text_auto_nest_search)+filePathPref.getSharedPreferences().getBoolean(res.getString(R.string.KEY_OF_FILE_PREF_RECURSE), false));
		fileTypesPref.setSummary(getString(R.string.text_file_type)+filePathPref.getSharedPreferences().getString(res.getString(R.string.KEY_OF_FILE_PREF_FILETYPE), null));
		super.onResume();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.addPreferencesFromResource(R.xml.conf_setting_file_pre);
		
		filePathPref=(EditTextPreference)getPreferenceScreen().findPreference("path");
		isAutoSearchPref=(SwitchPreference)getPreferenceScreen().findPreference("autosearch");
		isNestedPref=(SwitchPreference)getPreferenceScreen().findPreference("isnested");
		fileTypesPref=(ListPreference)getPreferenceScreen().findPreference("filetype");
		
	}

	@Override
	public void onSharedPreferenceChanged(
			SharedPreferences sharedPreferences, String key) {
		// TODO Auto-generated method stub
		if(key.equals("path")){
			filePathPref.setSummary(getString(R.string.text_file_path)+sharedPreferences.getString(key, ""));
		}else if(key.equals("autosearch")){
			isAutoSearchPref.setSummary(getString(R.string.text_auto_search)+sharedPreferences.getBoolean(key, false));
		}else if(key.equals("isnested")){
			isNestedPref.setSummary(getString(R.string.text_auto_nest_search)+sharedPreferences.getBoolean(key, false));
		}
	}
}
