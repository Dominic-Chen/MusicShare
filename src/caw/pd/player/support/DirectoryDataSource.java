package caw.pd.player.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.widget.Adapter;
import android.widget.SimpleCursorAdapter;
import caw.pd.R;
import caw.pd.model.Mp3Info;
import caw.pd.player.DataSource;
import caw.pd.util.FileUtils;

public class DirectoryDataSource implements DataSource {
	private FileUtils fileUtils=new FileUtils();
	private List<Mp3Info> mp3InfoList = new ArrayList();
	
	@Override
	public Adapter createAdapter(Context context) {
		Resources res=context.getResources();
		
		String filePathValue = PreferenceManager
				.getDefaultSharedPreferences(context).getString(
						res.getString(R.string.KEY_OF_FILE_PREF_PATH),
						"music/");

		boolean isAutoSearch = PreferenceManager
				.getDefaultSharedPreferences(context)
				.getBoolean(
						res.getString(R.string.KEY_OF_FILE_PREF_AUTOSEARCH),
						false);
		boolean isRecurse = PreferenceManager.getDefaultSharedPreferences(
				context).getBoolean(
				res.getString(R.string.KEY_OF_FILE_PREF_RECURSE), false);

		List types = new ArrayList();
		types.add("mp3");
		mp3InfoList = fileUtils
				.getMp3Files(filePathValue, isRecurse, types);
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		List mMusicList = new ArrayList();
		for (Iterator iterator = mp3InfoList.iterator(); iterator.hasNext();) {
			Mp3Info mp3Info = (Mp3Info) iterator.next();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("mp3_name", mp3Info.getMp3Name());
			map.put("mp3_size", mp3Info.getMp3Size());
			map.put("mp3_artist", mp3Info.getArtist());
			mMusicList.add(mp3Info.getMp3Name());
			list.add(map);
		}
		/*simpleAdapter = new SimpleAdapter(getActivity(), list,
				R.layout.ui_mp3_list_item, new String[] { "mp3_name",
						"mp3_artist", "mp3_size" }, new int[] {
						R.id.mp3_name, R.id.mp3_artist, R.id.mp3_size });*/
		
		Cursor myCur = context.getContentResolver().query(  
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,  
                new String[] { MediaStore.Audio.Media.TITLE,  
                        MediaStore.Audio.Media.DURATION,  
                        MediaStore.Audio.Media.ARTIST,  
                        MediaStore.Audio.Media._ID,  
                        MediaStore.Audio.Media.ALBUM,  
                        MediaStore.Audio.Media.DISPLAY_NAME,  
                        MediaStore.Audio.Media.DATA,  
                        MediaStore.Audio.Media.ALBUM_ID}, null,null, null);  
		SimpleCursorAdapter simpleAdapter=new SimpleCursorAdapter(
				context,
			    R.layout.ui_mp3_list_item,
			    myCur,
			    new String[] { MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ARTIST},// A string array of column names in the cursor
				new int[] { R.id.mp3_name, R.id.mp3_artist, R.id.mp3_size },// An integer array of view IDs in the row layout
			    0);  
		return simpleAdapter;
	}

	@Override
	public List getDatas() {
		// TODO Auto-generated method stub
		return this.mp3InfoList;
	}

}
