package caw.pd.player.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import caw.pd.R;
import caw.pd.player.data.ImageAdapter;

public class ArtistListFragment extends Fragment {

	private void initFragmentView() {
		List<Integer> thumbImg = new ArrayList<Integer>();
		for (int i = 0; i < 20; i++) {
			thumbImg.add(R.drawable.artist);
		}

		GridView gridview = (GridView) getActivity()
				.findViewById(R.id.gridview);// 找到main.xml中定义gridview 的id
		gridview.setAdapter(new ImageAdapter(getActivity(), thumbImg));// 调用ImageAdapter.java
		gridview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Toast.makeText(gridview.this,""+position,Toast.LENGTH_SHORT).show();//显示信息;
			}
		});

		ActionBar actionBar = getActivity().getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		container.removeAllViews();
		inflater.inflate(R.layout.ui_music_artist_list, container);
		initFragmentView();
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_action_bar_2, menu);
	}

}
