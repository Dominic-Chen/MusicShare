/**
 * 
 */
package caw.pd;

import java.util.List;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import caw.pd.R;

/**
 * @author domy
 *
 */
public class SettingActivity extends PreferenceActivity{
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onBuildHeaders(List<Header> target) {
		this.loadHeadersFromResource(R.xml.conf_setting_main_pre, target);
	}
	
}
