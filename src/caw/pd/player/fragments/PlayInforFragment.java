package caw.pd.player.fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import caw.pd.MusicShareActivity;
import caw.pd.R;
import caw.pd.model.Mp3Info;
import caw.pd.player.msg.SongInfoMsgBroadcastReceiver;
import caw.pd.util.AppConstant;
import caw.pd.util.MusicUtils;

public class PlayInforFragment extends Fragment {
	private boolean isVisible = true;
	private BroadcastReceiver songInfoMsgReciever;
	private IntentFilter intentFilter = null;
	private View view;
	
	private void initFragmentView(final View view) {
		this.view=view;
		
		initImgEventListener();

		initButtonEventListener();
	}
	
	private IntentFilter getIntentFilter(){
		if(intentFilter == null){
			intentFilter = new IntentFilter();
			intentFilter.addAction(AppConstant.REFRESH_INFO_MESSAGE_ACTION);
			intentFilter.addAction(AppConstant.LRC_MESSAGE_ACTION);
		}
		return intentFilter;
	}
	
	@Override
	public void onResume() {
		songInfoMsgReciever=new SongInfoMsgBroadcastReceiver(this);
    	getActivity().registerReceiver(songInfoMsgReciever, getIntentFilter());
    	
		super.onResume();
	}

	public void refreshAlbumPic(Cursor cursor){
		long songid = cursor.getLong(3); 
        long albumid = cursor.getLong(7);  
        Bitmap bm = MusicUtils.getArtwork(getActivity(), songid, albumid,true);  
        if(bm != null){  
            Log.d("debug","bm is not null==========================");
            ((ImageView)view.findViewById(R.id.mp3_cover_image)).setImageBitmap(bm);
        }else{  
            Log.d("debug","bm is null============================");  
        }
	}
	
	public void refreshSongInfor(Mp3Info mp3info, int position) {
		if (view != null && mp3info != null) {
			((TextView) view.findViewById(R.id.mp3_name)).setText(mp3info
					.getMp3Name());
			((TextView) view.findViewById(R.id.mp3_artist)).setText(mp3info
					.getArtist());
		} else {
			Log.d("debug", "infoView is:" + view + ",mp3info is:" + mp3info);
		}

		Cursor myCur=((MusicShareActivity)getActivity()).getMusicCusor();
		
		if(position<myCur.getCount()){
			myCur.moveToPosition(position);
			refreshAlbumPic(myCur);
		}
	}

	private void initImgEventListener() {
		((ImageView) view.findViewById(R.id.mp3_cover_image))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (isVisible) {
							view.findViewById(R.id.mp3_base_info).animate()
									.alpha(0);// .setVisibility(View.INVISIBLE);
							view.findViewById(R.id.mp3_action_info)
									.setVisibility(View.INVISIBLE);
							isVisible = false;
						} else {
							view.findViewById(R.id.mp3_base_info).animate()
									.alpha(40);
							view.findViewById(R.id.mp3_action_info)
									.setVisibility(View.VISIBLE);
							isVisible = true;
						}
					}
				});
	}

	private void initButtonEventListener() {
		View inforView = view.findViewById(R.id.info_main);
		((ImageButton) view.findViewById(R.id.btn_details))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						FragmentManager fragmentManager = getActivity()
								.getFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager
								.beginTransaction();
						Fragment fragment = (Fragment) ((MusicShareActivity) getActivity())
								.getAttachedFragments()
								.get("caw.pd.player.fragments.SongDetailsFragment");
						fragmentTransaction.replace(R.id.infor_frg_container,
								fragment);
						fragmentTransaction.commit();
					}
				});

		((ImageButton) view.findViewById(R.id.btn_faverite))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});

		((ImageButton) view.findViewById(R.id.btn_share))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});

		((ImageButton) view.findViewById(R.id.btn_setas))
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
					}
				});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(container!=null){
			container.removeAllViews();
			View view=inflater.inflate(R.layout.ui_music_play_info, container);
			initFragmentView(view);
		}else{
			Log.d("debug", "PlayInforFragment view is null!");
		}
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//super.onCreateOptionsMenu(menu, inflater);
		//inflater.inflate(R.menu.menu_action_bar_2, menu);
	}
}
