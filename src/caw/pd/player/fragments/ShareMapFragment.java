package caw.pd.player.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import caw.pd.R;
import caw.pd.component.AutoScrollTextView;

public class ShareMapFragment extends Fragment {
	private Handler handler=new Handler();
	private Canvas canvas=new Canvas();
	
	private void initFragmentView(final View view) {
		List<String> texts=new ArrayList();
		for(int i=0;i<10;i++){
			texts.add("this is a test!");
		}
		
		final AutoScrollTextView scrollView=new AutoScrollTextView(getActivity(), texts);
		((ViewGroup)view).addView(scrollView);
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				//scrollView.draw(canvas);
			}
		}, 1000);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(container!=null){
			container.removeAllViews();
			View view = inflater.inflate(R.layout.ui_music_map_share, container);
			initFragmentView(view);
		}else{
			Log.d("debug", "ShareMapFragment view is null");
		}
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_action_bar_3, menu);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}
}
