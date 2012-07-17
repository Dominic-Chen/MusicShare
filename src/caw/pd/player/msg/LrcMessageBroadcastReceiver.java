package caw.pd.player.msg;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;
import caw.pd.R;

public class LrcMessageBroadcastReceiver extends BroadcastReceiver {
	private Activity activity;
	
	public LrcMessageBroadcastReceiver(Activity activity){
		this.activity=activity;
	}
	@Override
	public void onReceive(Context arg0, Intent intent) {
		TextView view = (TextView)this.activity.findViewById(R.id.text_lrc_view);
		if(view!=null){
			view.setText(intent.getExtras().getString("lrcMessage"));
		}
	}

}
