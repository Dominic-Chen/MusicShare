package caw.pd.player.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Fragment;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import caw.pd.MusicShareActivity;
import caw.pd.R;
import caw.pd.player.data.ImageAdapter;
import caw.pd.util.MusicUtils;

public class AlbumListFragment extends Fragment{
	private int counter=0;
	private Map<String,List> albums=new HashMap();
	private Map<String,String> filters=new HashMap();
	
	private void initFragmentView() {
		try {
			List<Bitmap> result=new ArrayList();
			Cursor myCur=((MusicShareActivity)getActivity()).getMusicCusor();
			for (myCur.moveToFirst(); !myCur.isAfterLast(); myCur.moveToNext()) {
				long songid = myCur.getLong(3);
				long albumid = myCur.getLong(7);
				
				String musicAlbum = myCur.getString(myCur
						.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));
				String musicPath = myCur.getString(myCur
						.getColumnIndex(MediaStore.Audio.AudioColumns.DATA));
				String dir=musicPath.substring(0,musicPath.lastIndexOf("/"));
				if(this.filters.containsKey(dir)){
					continue;
				}
				
				if(!this.albums.containsKey(musicAlbum)){
					List songs=new ArrayList();
					songs.add(songid);
					this.albums.put(musicAlbum, songs);
					Bitmap bm = MusicUtils.getArtwork(getActivity(), songid, albumid, true);
					result.add(bm);
				}else{
					List songs=this.albums.get(musicAlbum);
					songs.add(songid);
				}
			}
			Toast.makeText(getActivity(), this.albums.size(), Toast.LENGTH_LONG).show();
			GridView gridview = (GridView) getActivity()
					.findViewById(R.id.gridview);// 找到main.xml中定义gridview 的id
			gridview.setAdapter(new ImageAdapter(getActivity(), result));// 调用ImageAdapter.java
			gridview.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
				}
			});

			ActionBar actionBar = getActivity().getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		container.removeAllViews();
		inflater.inflate(R.layout.ui_music_artist_list, container);
		initFragmentView();
		
		if(this.filters.isEmpty()){
			this.filters.put("/mnt/sdcard/gameloft/games/hawx", "/mnt/sdcard/gameloft/games/hawx");
		}
		
		if(albums==null){
			albums=new HashMap();
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	

}
