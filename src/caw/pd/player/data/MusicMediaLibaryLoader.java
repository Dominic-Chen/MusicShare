/**
 * 
 */
package caw.pd.player.data;

import java.util.ArrayList;
import java.util.List;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import caw.pd.MusicShareActivity;
import caw.pd.util.FileUtils;
import caw.pd.util.MusicUtils;

/**
 * @author domy
 * @param <D>
 *
 */
public class MusicMediaLibaryLoader extends AsyncTaskLoader<Cursor> {
	private Context context;
	private List mp3InfoList=new ArrayList();
	private FileUtils fileUtils = new FileUtils();
	private String[] mediaData=new String[] { MediaStore.Audio.Media.TITLE,  
            MediaStore.Audio.Media.DURATION,  
            MediaStore.Audio.Media.ARTIST,  
            MediaStore.Audio.Media._ID,  
            MediaStore.Audio.Media.ALBUM,  
            MediaStore.Audio.Media.DISPLAY_NAME,  
            MediaStore.Audio.Media.DATA,  
            MediaStore.Audio.Media.ALBUM_ID};
	
	public MusicMediaLibaryLoader(Context context) {
		super(context);
		this.context=context;
	}

	@Override
	public Cursor loadInBackground() {
		Cursor myCur = context.getContentResolver().query(  
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,mediaData, null,null, null);  
		
		((MusicShareActivity)context).setMusicCursor(myCur);
		
		if (null != myCur && myCur.getCount() > 0) {
			for (myCur.moveToFirst(); !myCur.isAfterLast(); myCur
					.moveToNext()) {
				String musicPath = myCur.getString(myCur
						.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
				String dir=musicPath.substring(0,musicPath.lastIndexOf("/"));
				//if(!this.filters.containsKey(dir)){
					mp3InfoList.add(MusicUtils.createMp3Infor(myCur));
				//}
			}
		}
		
		/*SimpleCursorAdapter simpleAdapter=new SimpleCursorAdapter(
				context,
			    R.layout.ui_mp3_list_item,
			    myCur,
			    new String[] { MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ARTIST},// A string array of column names in the cursor
				new int[] { R.id.mp3_name, R.id.mp3_artist, R.id.mp3_size },// An integer array of view IDs in the row layout
			    0);*/
		return myCur;
	}

	@Override
	public void deliverResult(Cursor data) {
		if (isReset()) {
            if (data != null) {
            	mp3InfoList.clear();
            	mp3InfoList=null;
            }
        }
        
        if(isStarted()){
            super.deliverResult(data);
        }
        
	}
	
	public List getListResult(){
		return this.mp3InfoList;
	}
}
