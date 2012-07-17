/**
 * 
 */
package caw.pd.player.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import caw.pd.R;
import caw.pd.model.Mp3Info;
import caw.pd.util.FileUtils;

/**
 * @author domy
 * @param <D>
 *
 */
public class MusicDirectoryLoader extends AsyncTaskLoader<List<Map<String, String>>> {
	private Context context;
	private List mp3InfoList;
	private FileUtils fileUtils = new FileUtils();
	
	public MusicDirectoryLoader(Context context) {
		super(context);
		this.context=context;
	}

	@Override
	public List<Map<String, String>> loadInBackground() {
		Resources res = context.getResources();
		String filePathValue = PreferenceManager
				.getDefaultSharedPreferences(context).getString(
						res.getString(R.string.KEY_OF_FILE_PREF_PATH),
						"music/");
		/*Toast.makeText(getActivity(), filePathValue, Toast.LENGTH_LONG)
				.show();*/

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
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

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
		return list;
	}

	@Override
	public void deliverResult(List<Map<String, String>> data) {
		if (isReset()) {
            if (data != null) {
            	mp3InfoList.clear();
            	mp3InfoList=null;
            }
        }
        
		List<Map<String, String>> oladApps=data;
        mp3InfoList=data;
        if(isStarted()){
            super.deliverResult(data);
        }
        
        if(oladApps!=null){
            //释放资源
        }
	}
}
