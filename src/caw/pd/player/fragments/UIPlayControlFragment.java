package caw.pd.player.fragments;

import java.util.HashMap;
import java.util.Map;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import caw.pd.MusicShareActivity;
import caw.pd.R;
import caw.pd.player.msg.EqualizerEnableMsgReceiver;
import caw.pd.player.msg.LrcMessageBroadcastReceiver;
import caw.pd.player.msg.SongInfoMsgBroadcastReceiver;
import caw.pd.player.services.PlayerService;
import caw.pd.util.AppConstant;
import caw.pd.util.CommonConstDef;

public class UIPlayControlFragment extends Fragment {
	private ImageView btnStartOrPause = null;
	private ImageView btnStop = null;
	private ImageView btnPre = null;
	private ImageView btnNext = null;
	private ImageButton btnEqualizer =null;
	private ImageButton btnSwitchMode=null;
	public static ProgressBar barProgress = null;
	public static TextView timePassedText=null;
	
	private boolean isPlaying = false;
	
	private Map<String,IntentFilter> intentFilters = null;
	private int curPlayMode=CommonConstDef.PLAY_MODE_CIRCLE_LIST;

	private BroadcastReceiver songInfoMsgReciever;
	private BroadcastReceiver lrcMsgReciever;
	private BroadcastReceiver eqEnableMsgReciever;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	/**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(caw.pd.R.layout.ui_music_ctrl_fragment, container, false);
        initUIWidgets(v);
        return v;
    }
    
    private OnClickListener mPauseOrStartListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(),
					PlayerService.class);
			if (isPlaying) {
				sendControlState(CommonConstDef.PLAY_STATUS_PAUSE,null);
			} else {
				sendControlState(CommonConstDef.PLAY_STATUS_PLAY,null);
				isPlaying = true;
			}
		}
	};

	private OnClickListener mPreListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			sendControlState(CommonConstDef.PLAY_STATUS_PRE,null);
			btnStartOrPause.setBackgroundResource(android.R.drawable.ic_media_pause);
		}
	};

	private OnClickListener mNextListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			sendControlState(CommonConstDef.PLAY_STATUS_NEXT,null);
			btnStartOrPause.setBackgroundResource(android.R.drawable.ic_media_pause);
		}
	};
	
    private OnClickListener mEqualizerListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Toast.makeText(getActivity(), "打开均衡器", Toast.LENGTH_LONG).show();
			FragmentManager fragmentManager = getActivity()
					.getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			Fragment fragment = (Fragment) ((MusicShareActivity) getActivity())
					.getAttachedFragments().get(
							"caw.pd.player.fragments.EqualizerFxFragment");
			fragmentTransaction.replace(R.id.infor_frg_container, fragment);
			fragmentTransaction.commit();
		}
	};

	private OnClickListener mSwitchModeListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			String msg="";
			switch (curPlayMode) {
			case CommonConstDef.PLAY_MODE_CIRCLE_LIST: {
				curPlayMode = CommonConstDef.PLAY_MODE_SHUFFLE_LIST;
				((ImageButton)v).setImageResource(R.drawable.btn_shuffle_off_holo_dark);
				v.invalidate();
				msg="随机播放模式";
			}
				break;
			case CommonConstDef.PLAY_MODE_CIRCLE_SINGLE:{
				curPlayMode = CommonConstDef.PLAY_MODE_CIRCLE_LIST;
				((ImageButton)v).setImageResource(R.drawable.btn_repeat_all_off_holo_dark);
				v.invalidate();
				msg="列表循环播放模式";
			}
				break;
			case CommonConstDef.PLAY_MODE_SHUFFLE_LIST:{
				curPlayMode = CommonConstDef.PLAY_MODE_CIRCLE_SINGLE;
				((ImageButton)v).setImageResource(R.drawable.btn_repeat_once_off_holo_dark);
				v.invalidate();
				msg="单曲循环播放模式";
			}
				break;
			}
			
			Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
		}
	};
	
	public void refreshSongInfor(int state) {
		if(state==CommonConstDef.PLAY_STATUS_PLAY){
			btnStartOrPause.setBackgroundResource(android.R.drawable.ic_media_pause);
		}
		if(state==CommonConstDef.PLAY_STATUS_PAUSE){
			btnStartOrPause.setBackgroundResource(android.R.drawable.ic_media_play);
		}
	}
	
	public void sendControlState(int state,Intent intent){
		if (intent == null) {
			intent = new Intent(getActivity(), PlayerService.class);
		}
		intent.putExtra("control.playmode", curPlayMode);
		intent.putExtra("control.state", state);
		getActivity().startService(intent);
	}
	
	public void enableEqualizerButton(boolean enabled){
		btnEqualizer.setEnabled(enabled);
	}
	
	private IntentFilter getIntentFilter(String key){
		if(intentFilters == null){
			intentFilters=new HashMap();
			
			IntentFilter intentFilter = new IntentFilter();
			intentFilter.addAction(AppConstant.REFRESH_INFO_MESSAGE_ACTION);
			intentFilters.put(AppConstant.REFRESH_INFO_MESSAGE_ACTION, intentFilter);
			
			intentFilter = new IntentFilter();
			intentFilter.addAction(AppConstant.LRC_MESSAGE_ACTION);
			intentFilters.put(AppConstant.LRC_MESSAGE_ACTION, intentFilter);
			
			intentFilter = new IntentFilter();
			intentFilter.addAction(AppConstant.REFRESH_EQUALIZER_ENABLE_MESSAGE_ACTION);
			intentFilters.put(AppConstant.REFRESH_EQUALIZER_ENABLE_MESSAGE_ACTION, intentFilter);
		}
		return intentFilters.get(key);
	}
	
    @Override
	public void onResume() {
		songInfoMsgReciever = new SongInfoMsgBroadcastReceiver(this);
		getActivity().registerReceiver(songInfoMsgReciever,
				getIntentFilter(AppConstant.REFRESH_INFO_MESSAGE_ACTION));

		lrcMsgReciever = new LrcMessageBroadcastReceiver(this.getActivity());
		getActivity().registerReceiver(lrcMsgReciever,
				getIntentFilter(AppConstant.LRC_MESSAGE_ACTION));

		eqEnableMsgReciever = new EqualizerEnableMsgReceiver(this);
		getActivity().registerReceiver(
						eqEnableMsgReciever,
						getIntentFilter(AppConstant.REFRESH_EQUALIZER_ENABLE_MESSAGE_ACTION));

		/*
		 * songIndexMsgReciever=new SongIndexMsgBroadcastReceiver(this);
		 * getActivity().registerReceiver(songIndexMsgReciever,
		 * getIntentFilter());
		 */
		super.onResume();
	}

	private void initUIWidgets(View view) {
		btnStartOrPause = (ImageView) view.findViewById(R.id.btn_player_pause);
		btnStartOrPause.setOnClickListener(mPauseOrStartListener);

		//btnStop = (ImageView) view.findViewById(R.id.btn_palyer_stop);
		//btnStop.setOnClickListener(mStopListener);

		btnPre = (ImageView) view.findViewById(R.id.btn_player_pre);
		btnPre.setOnClickListener(mPreListener);

		btnNext = (ImageView) view.findViewById(R.id.btn_player_next);
		btnNext.setOnClickListener(mNextListener);

		barProgress = (ProgressBar) view.findViewById(R.id.bar_palyer_progress);
		
		timePassedText=(TextView)view.findViewById(R.id.mp3_played_time_passed);
		
		btnEqualizer=(ImageButton)view.findViewById(R.id.btn_player_equalizer);
		btnEqualizer.setOnClickListener(mEqualizerListener);
		btnEqualizer.setEnabled(false);
		
		btnSwitchMode=(ImageButton)view.findViewById(R.id.btn_player_mode);
		btnSwitchMode.setOnClickListener(mSwitchModeListener);
	}
    
}
