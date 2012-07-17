package caw.pd.player.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Queue;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import caw.pd.lrc.LRCParser;
import caw.pd.model.LrcInfo;
import caw.pd.model.Mp3Info;
import caw.pd.player.fragments.SongsListFragment;
import caw.pd.player.fragments.UIPlayControlFragment;
import caw.pd.player.msg.EqualizerConfgMsgReciever;
import caw.pd.player.msg.MessageDispatcher;
import caw.pd.util.AppConstant;
import caw.pd.util.CommonConstDef;

public class PlayerService extends Service implements Runnable,OnCompletionListener {
	//TODO:临时性搞法
	public static MediaPlayer mplayer = null;
	private boolean isStopped = false;
	private int mCurMp3Index = -1;
	private Mp3Info curMp3Info;
	private final Handler handler = new Handler();
	private IBinder binder = new LocalBinder();
	private int playMode = CommonConstDef.PLAY_MODE_CIRCLE_LIST;
	private BroadcastReceiver equalizerConfMsgReciever;
	private IntentFilter intentFilter = null;

	// 以下变量主要解决歌词问题
	private UpdateTimeCallback updateTimeCallback = null;
	private long begin = 0;
	private long currentTimeMill = 0;
	private long nextTimeMill = 0;
	private long pauseTimeMills = 0;
	private String message = null;
	private Map<String,Queue> queues = null;
	private LrcInfo lrcInfo;

	public class LocalBinder extends Binder {
		public PlayerService getService() {
			return PlayerService.this;
		}
	}

	private IntentFilter getIntentFilter(){
		if(intentFilter == null){
			intentFilter = new IntentFilter();
			intentFilter.addAction(AppConstant.REFRESH_EQUALIZER_CHANGE_MESSAGE_ACTION);
		}
		return intentFilter;
	}
	
	@Override
	public void onCreate() {
		mplayer = new MediaPlayer();
		mplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		equalizerConfMsgReciever=new EqualizerConfgMsgReciever(mplayer);
		registerReceiver(equalizerConfMsgReciever, getIntentFilter());
		
		Resources res = getResources();
		/*
		 * this.mSettingPath =
		 * PreferenceManager.getDefaultSharedPreferences(this)
		 * .getString(res.getString(R.string.KEY_OF_FILE_PREF_PATH), "music/");
		 */
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return this.binder;
	}

	@Override
	public void run() {
		// 设置默认进度条当前位置
		int CurrentPosition = 0;
		final int total = mplayer.getDuration();//

		UIPlayControlFragment.barProgress.setMax(total);
		while (mplayer != null && CurrentPosition < total) {
			try {
				Thread.sleep(1000);
				if (mplayer != null) {
					CurrentPosition = mplayer.getCurrentPosition();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			UIPlayControlFragment.barProgress.setProgress(CurrentPosition);
			handler.post(new Runnable() {
				@Override
				public void run() {
					SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
					UIPlayControlFragment.timePassedText.setText(formatter
							.format(mplayer.getCurrentPosition()));
				}
			});
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null && intent.getExtras() != null) {
			int state = intent.getExtras().getInt("control.state");
			if (intent.getExtras().getInt("control.playmode") != 0) {
				this.playMode = intent.getExtras().getInt("control.playmode");
			}
			switch (state) {
			case CommonConstDef.PLAY_STATUS_PLAY: {
				int position = intent.getExtras().getInt("mp3.postion");
				this.mCurMp3Index = position;
				play();
				break;
			}
			case CommonConstDef.PLAY_STATUS_PAUSE:
				pause();
				break;
			case CommonConstDef.PLAY_STATUS_NEXT:
				next();
				break;
			case CommonConstDef.PLAY_STATUS_PRE:
				pre();
				break;
			}
		}
		MessageDispatcher.sendRefreshMessage(this,this.mCurMp3Index,
				mplayer.isPlaying() ? CommonConstDef.PLAY_STATUS_PLAY
						: CommonConstDef.PLAY_STATUS_PAUSE, this.curMp3Info);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		stop();
		super.onDestroy();
	}

	public void play() {
		try {
			Mp3Info mp3info = SongsListFragment.mp3InfoList
					.get(this.mCurMp3Index);
			
			/*
			 * Uri uri = Uri.parse(rootPath + mSettingPath +
			 * mp3info.getFullName());
			 */
			Uri uri = Uri.parse(mp3info.getLocation());
			// curMp3Info=mp3info;

			/* 重置MediaPlayer */
			mplayer.reset();
			/* 设置要播放的文件的路径 */
			mplayer.setDataSource(getApplicationContext(), uri);
			/* 准备播放 */
			mplayer.prepare();
			/* 开始播放 */
			mplayer.start();
			MessageDispatcher.sendEqualizerEnableMessage(this,true);
			prepareLrc(mp3info);
			begin = System.currentTimeMillis();
			handler.postDelayed(updateTimeCallback, 5);
			new Thread(this).start();
			mplayer.setOnCompletionListener(this);
			this.curMp3Info = mp3info;
			// TODO:暂时没想到其他办法，这种搞法有问题，只能是暂时的
			// UIPlayControlFragment.getInstance().refreshSongInfor(mp3info);
			// sendRefreshMessage(mp3info);
		} catch (IOException e) {
		}
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		if (!isStopped) {
			next();
			MessageDispatcher.sendRefreshMessage(this,this.mCurMp3Index,
					mplayer.isPlaying() ? CommonConstDef.PLAY_STATUS_PLAY
							: CommonConstDef.PLAY_STATUS_PAUSE, curMp3Info);
		}
	}

	public void stop() {
		if (mplayer.isPlaying()) {
			// 重置MediaPlayer到初始状态
			mplayer.reset();
			isStopped = true;
		}
	}

	public void pause() {
		if (mplayer.isPlaying()) {
			/* 暂停 */
			mplayer.pause();
			handler.removeCallbacks(updateTimeCallback);
		} else {
			/* 开始播放 */
			mplayer.start();
			handler.postDelayed(updateTimeCallback, 5);
		}
	}

	public void next() {
		switch (this.playMode) {
		case CommonConstDef.PLAY_MODE_CIRCLE_LIST:
			++this.mCurMp3Index;
			break;
		case CommonConstDef.PLAY_MODE_CIRCLE_SINGLE:
			break;
		case CommonConstDef.PLAY_MODE_SHUFFLE_LIST: {
			this.mCurMp3Index = (int) (Math.random() * SongsListFragment.mp3InfoList.size());
			break;
		}
		}

		if (this.mCurMp3Index >= SongsListFragment.mp3InfoList.size()) {
			mCurMp3Index = 0;
		} else {
			// play(ListViewInitializer.mp3InfoList.get(mCurMp3Index));
			play();
		}
	}

	public void pre() {
		switch (this.playMode) {
		case CommonConstDef.PLAY_MODE_CIRCLE_LIST:
			--this.mCurMp3Index;
			break;
		case CommonConstDef.PLAY_MODE_CIRCLE_SINGLE:
			break;
		case CommonConstDef.PLAY_MODE_SHUFFLE_LIST:
			this.mCurMp3Index = (int) Math.random()
					* SongsListFragment.mp3InfoList.size();
			break;
		}

		if (mCurMp3Index == -1) {
			mCurMp3Index = SongsListFragment.mp3InfoList.size();
		} else {
			// play(ListViewInitializer.mp3InfoList.get(mCurMp3Index));
			play();
		}
	}

	private void prepareLrc(Mp3Info mp3Info) {
		try {
			if(mp3Info.getLrc()!=null){
				InputStream inputStream = new FileInputStream(Environment
						.getExternalStorageDirectory().getAbsoluteFile()
						+ File.separator + "duomi/lyric/"+mp3Info.getLrc().getLrcName());
	//			InputStream inputStream = new FileInputStream("file:///sdcard/duomi/lyric/13021456_莫文蔚_完美孤独.lrc");
				
				//LrcProcessor lrcProcessor = new LrcProcessor();
				//queues = lrcProcessor.process(inputStream);
				
				//Analyzer analyzer=new Analyzer();
				//queues=analyzer.analyze(inputStream);
				
				LRCParser parser=new LRCParser();
				queues=parser.parser(inputStream);
				
				// ����һ��UpdateTimeCallback����
				updateTimeCallback = new UpdateTimeCallback(queues);
				begin = 0;
				currentTimeMill = 0;
				nextTimeMill = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class UpdateTimeCallback implements Runnable {
		Queue times = null;
		Queue messages = null;
		//LrcInfo lrcInfo=null;

		public UpdateTimeCallback(Map<String,Queue> queues) {
			// ��ArrayList����ȡ����Ӧ�Ķ���
			if(!queues.isEmpty()){
				if(queues.containsKey(LRCParser.LRC_TIME_RESULT)){
					times = queues.get(LRCParser.LRC_TIME_RESULT);
				}
				if(queues.containsKey(LRCParser.LRC_TEXT_RESULT)){
					messages = queues.get(LRCParser.LRC_TEXT_RESULT);
				}
			}
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			long offset = System.currentTimeMillis() - begin;
			
			if(times==null&&messages==null){
				Log.d("play debug", "Read lrc file failed!");
				return;
			}
			
			if (currentTimeMill == 0&&times!=null&&messages!=null&&!times.isEmpty()&&!messages.isEmpty()) {
				nextTimeMill = (Long) times.poll();
				message = (String) messages.poll();
			}
			if (offset >= nextTimeMill&&!times.isEmpty()&&!messages.isEmpty()) {

				Intent intent = new Intent();
				intent.setAction(AppConstant.LRC_MESSAGE_ACTION);
				intent.putExtra("lrcMessage", message);
				sendBroadcast(intent);
				message = (String) messages.poll();
				nextTimeMill = (Long) times.poll();
			}
			currentTimeMill = currentTimeMill + 10;
			handler.postDelayed(updateTimeCallback, 10);
		}
	}
}
