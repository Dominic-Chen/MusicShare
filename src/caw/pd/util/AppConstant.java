package caw.pd.util;

public interface AppConstant {
	public class PlayerMsg{
		public static final int PLAY_MSG = 1;
		public static final int PAUSE_MSG = 2;
		public static final int STOP_MSG = 3;
	}
	public class URL{
		public static final String BASE_URL = "http://172.16.99.36:8888/MP3/";
	}
	public static final String LRC_MESSAGE_ACTION = "mars.mp3player.lrcmessage.action";
	
	public static final String REFRESH_INFO_MESSAGE_ACTION ="org.caw.pd.songsinfo.refresh.action";
	
	public static final String REFRESH_INDEX_MESSAGE_ACTION ="org.caw.pd.songsinfo.refresh.curindex.action";
	
	public static final String REFRESH_EQUALIZER_ENABLE_MESSAGE_ACTION ="org.caw.pd.euqlizer.enable.action";
	
	public static final String REFRESH_EQUALIZER_CHANGE_MESSAGE_ACTION ="org.caw.pd.equalizer.config.change.action";
}
