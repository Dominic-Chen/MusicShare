package caw.pd.player.support;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.Adapter;
import android.widget.SimpleCursorAdapter;
import caw.pd.MusicShareActivity;
import caw.pd.R;
import caw.pd.model.Mp3Info;
import caw.pd.player.DataSource;
import caw.pd.util.MusicUtils;

public class MediaStorageDataSource implements DataSource {
	private String[] mediaData=new String[] { MediaStore.Audio.Media.TITLE,  
            MediaStore.Audio.Media.DURATION,  
            MediaStore.Audio.Media.ARTIST,  
            MediaStore.Audio.Media._ID,  
            MediaStore.Audio.Media.ALBUM,  
            MediaStore.Audio.Media.DISPLAY_NAME,  
            MediaStore.Audio.Media.DATA,  
            MediaStore.Audio.Media.ALBUM_ID};
	private List<Mp3Info> mp3InfoList = new ArrayList();
	
	@Override
	public Adapter createAdapter(Context context) {
		Cursor myCur = context.getContentResolver().query(  
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,mediaData, null,null, null);  
		
		((MusicShareActivity)context).setMusicCursor(myCur);
		
		if (null != myCur && myCur.getCount() > 0) {
			for (myCur.moveToFirst(); !myCur.isAfterLast(); myCur
					.moveToNext()) {
				mp3InfoList.add(MusicUtils.createMp3Infor(myCur));
			}
		}
		
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
