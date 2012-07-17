package caw.pd.player.fragments;

import caw.pd.R;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AlbumDetailsFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(container!=null){
			container.removeAllViews();
			View view=inflater.inflate(R.layout.ui_music_details_album_tab, container);
		}else{
			Log.d("debug", "PlayInforFragment view is null!");
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

}
