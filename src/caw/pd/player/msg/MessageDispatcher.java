package caw.pd.player.msg;

import java.io.Serializable;

import android.content.ContextWrapper;
import android.content.Intent;
import caw.pd.model.Mp3Info;
import caw.pd.util.AppConstant;

public class MessageDispatcher {
	public static void sendRefreshMessage(ContextWrapper wrapper,int position,int state,Mp3Info mp3info){
		Intent intent = new Intent();
		intent.setAction(AppConstant.REFRESH_INFO_MESSAGE_ACTION);
		intent.putExtra("refresh.message", (Serializable)mp3info);
		intent.putExtra("current.status", state);
		intent.putExtra("current.postion", position);
		wrapper.sendBroadcast(intent);
	}
	
	public static void sendCurrentPosMessage(ContextWrapper wrapper,int position){
		Intent intent = new Intent();
		intent.setAction(AppConstant.REFRESH_INDEX_MESSAGE_ACTION);
		intent.putExtra("position.message", position);
		wrapper.sendBroadcast(intent);
	}
	
	public static void sendEqualizerConfMessage(ContextWrapper wrapper,short band,short level){
		Intent intent = new Intent();
		intent.setAction(AppConstant.REFRESH_EQUALIZER_CHANGE_MESSAGE_ACTION);
		intent.putExtra("equalizer.band", band);
		intent.putExtra("equalizer.level", level);
		wrapper.sendBroadcast(intent);
	}
	
	public static void sendEqualizerEnableMessage(ContextWrapper wrapper,boolean enabled){
		Intent intent = new Intent();
		intent.setAction(AppConstant.REFRESH_EQUALIZER_ENABLE_MESSAGE_ACTION);
		intent.putExtra("equalizer.enabled", enabled);
		wrapper.sendBroadcast(intent);
	}
}
