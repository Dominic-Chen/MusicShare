package caw.pd.player.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import caw.pd.MusicShareActivity;
import caw.pd.R;
import caw.pd.model.Mp3Info;
import caw.pd.player.data.MusicMediaLibaryLoader;
import caw.pd.player.services.PlayerService;
import caw.pd.util.CommonConstDef;

public class SongsListFragment extends ListFragment implements LoaderCallbacks<Cursor>{
	public static List<Mp3Info> mp3InfoList = new ArrayList();
	private SimpleCursorAdapter  simpleAdapter;
	private View view;
	private Handler uiHandler = new Handler();
	private String[] mediaData=new String[] { MediaStore.Audio.Media.TITLE,  
            MediaStore.Audio.Media.DURATION,  
            MediaStore.Audio.Media.ARTIST,  
            MediaStore.Audio.Media._ID,  
            MediaStore.Audio.Media.ALBUM,  
            MediaStore.Audio.Media.DISPLAY_NAME,  
            MediaStore.Audio.Media.DATA,  
            MediaStore.Audio.Media.ALBUM_ID};
	private String curFragTag="";
	private Map<String,String> filters=new HashMap();
	private ListView listView;
	
	private class ModeCallback implements ListView.MultiChoiceModeListener {

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.list_select_menu, menu);
            mode.setTitle("Select Items");
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
            case R.id.share:
                Toast.makeText(getActivity(), "Shared " + listView.getCheckedItemCount() +
                        " items", Toast.LENGTH_SHORT).show();
                mode.finish();
                break;
            default:
                Toast.makeText(getActivity(), "Clicked " + item.getTitle(),
                        Toast.LENGTH_SHORT).show();
                break;
            }
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
        }

        public void onItemCheckedStateChanged(ActionMode mode,
                int position, long id, boolean checked) {
            final int checkedCount = listView.getCheckedItemCount();
            switch (checkedCount) {
                case 0:
                    mode.setSubtitle(null);
                    break;
                case 1:
                    mode.setSubtitle("One item selected");
                    break;
                default:
                    mode.setSubtitle("" + checkedCount + " items selected");
                    break;
            }
        }
        
    }

	public String getCurrentFragTag(){
		return curFragTag;
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.menu_list_item, menu);
	}

	private void initFragmentView(View view) {
		Resources res = getActivity().getResources();
		if (simpleAdapter == null) {
			/*Cursor myCur = getActivity().getContentResolver().query(  
	                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,mediaData, null,null, null);  
			
			((MusicShareActivity)getActivity()).setMusicCursor(myCur);
			
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
			}*/
			
			simpleAdapter=new SimpleCursorAdapter(
					getActivity(),
				    R.layout.ui_mp3_list_item,
				    null,
				    new String[] { MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ARTIST},// A string array of column names in the cursor
					new int[] { R.id.mp3_name, R.id.mp3_artist, R.id.mp3_size },// An integer array of view IDs in the row layout
				    0);
		}
		
		listView=(ListView) view.findViewById(R.id.list);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setMultiChoiceModeListener(new ModeCallback());
		listView.setAdapter(simpleAdapter);
		
		initListViewListener(listView);
		
		initButtonEventListener(view);
	}

	private void initListViewListener(final ListView listView) {
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				if(listView.getChoiceMode()==ListView.CHOICE_MODE_MULTIPLE_MODAL){
					listView.setItemChecked(position, true);
				}else{
					Cursor myCur=((MusicShareActivity)getActivity()).getMusicCusor();
					
					FragmentManager fragManager =getActivity().getFragmentManager();
					Fragment ctrlFragment=fragManager.findFragmentById(R.id.id_control_frame);
					//((UIPlayControlFragment)ctrlFragment).play(mp3info,null);
					Intent intent = new Intent(getActivity(), PlayerService.class);
					intent.putExtra("mp3.postion", position);
					((UIPlayControlFragment)ctrlFragment).sendControlState(CommonConstDef.PLAY_STATUS_PLAY, intent);
	
				    //testInfo(myCur);
				    myCur.moveToPosition(position);
				    ((PlayInforFragment)fragManager.findFragmentByTag("tag.main.info")).refreshAlbumPic(myCur);
					//((MusicShareActivity)getActivity()).getViewPager().setCurrentItem(1);
				}
			}
		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
				return true;
			}
		});
	}
	
	private void initButtonEventListener(View view) {
		final ImageButton btnFavor=(ImageButton) view.findViewById(R.id.btn_list_view_favor);
		btnFavor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getActivity(), "切换收藏列表", Toast.LENGTH_LONG).show();
			}
		});
		
		final ImageButton btnArtist=(ImageButton) view.findViewById(R.id.btn_list_view_artist);
		btnArtist.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fragmentManager = getActivity().getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				//Fragment pre=fragmentManager.findFragmentByTag("tag.main.list");
				//Fragment fragment = fragmentManager.findFragmentByTag("tag.sub.artist.list");
				//fragmentTransaction.hide(pre);
				ArtistListFragment fragment = new ArtistListFragment();
				//fragmentTransaction.show(fragment)(R.id.list_frg_container, fragment);
				//fragmentTransaction.show(fragment);
				fragmentTransaction.replace(R.id.list_frg_container, fragment);
				fragmentTransaction.commit();
				
				curFragTag="tag.sub.artist.list";

			}
		});
		
		final ImageButton btnSpecial=(ImageButton) view.findViewById(R.id.btn_list_view_special);
		btnSpecial.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fragmentManager = getActivity().getFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				AlbumListFragment fragment = new AlbumListFragment();
				//fragmentTransaction.hide(fragment);
				fragmentTransaction.replace(R.id.list_frg_container, fragment);
				fragmentTransaction.commit();
			}
		});
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if(container!=null){
			container.removeAllViews();
			view=inflater.inflate(R.layout.ui_music_songs_list, container);
			initFragmentView(view);
		}
		
		if(this.filters.isEmpty()){
			this.filters.put("/mnt/sdcard/gameloft/games/hawx", "/mnt/sdcard/gameloft/games/hawx");
		}
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public void loadData(){
		/*if(this.simpleAdapter==null){
			uiHandler.postDelayed(new Runnable(){
				public void run() {
					initFragmentView(view);
					getActivity().setProgressBarIndeterminateVisibility(false);
				}}, 1000);
		}*/
		setListShown(false);
		Loader loader=getLoaderManager().initLoader(0, null, this);
		//if(!loader.isStarted()){
			loader.forceLoad();
		//}
	}

	@Override
	public void onResume() {
		//simpleAdapter=null;
		//loadData();
		super.onResume();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		//super.onCreateOptionsMenu(menu, inflater);
		//inflater.inflate(R.menu.menu_action_bar_1, menu);
	}

	@Override
	public Loader onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		return new MusicMediaLibaryLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0,
			Cursor arg1) {
		simpleAdapter.swapCursor(arg1);
		mp3InfoList=((MusicMediaLibaryLoader)arg0).getListResult();
		
		// The list should now be shown.
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
        getActivity().setProgressBarIndeterminateVisibility(false);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		simpleAdapter.swapCursor(null);
	}
}
