package caw.pd;

import java.util.HashMap;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import caw.pd.player.Callback;
import caw.pd.player.Initializer;
import caw.pd.player.fragments.SongsListFragment;
import caw.pd.player.msg.MessageDispatcher;
import caw.pd.player.support.UIFragmentsInitializer;
import caw.pd.player.support.UIPageViewInitializer;

public class MusicShareActivity extends Activity {
	private Map<String,Fragment> fragments=new HashMap();
	private Map<String,Integer> actionMenu=new HashMap();
	private int pageIndex;
	private Map<Integer,Fragment> curFragment=new HashMap();
	private Cursor musicCursor;
	
	private Initializer<Activity> fragInit=new UIFragmentsInitializer();
	private Initializer<Activity> pagerInit=new UIPageViewInitializer();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		Handler handle=new Handler();
		handle.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				pagerInit.initialize(MusicShareActivity.this, new Callback<Integer>() {

					@Override
					public void doCallback(Integer t) {
						MusicShareActivity.this.pageIndex=t;
						//viewPager=(ViewPager)MusicShareActivity.this.findViewById(R.id.viewpagerLayout);
					}
				});
				
				fragInit.initialize(MusicShareActivity.this, new Callback<Map<String, Fragment>>() {
					@Override
					public void doCallback(Map<String, Fragment> fragments) {
						MusicShareActivity.this.fragments=fragments;
					}
				});
			}
		}, 1000);
	}
	
	public void setMusicCursor(Cursor cursor){
		this.musicCursor=cursor;
	}
	
	public Cursor getMusicCusor(){
		return this.musicCursor;
	}
	
	public Map getAttachedFragments(){
		return this.fragments;
	}
	
	public void updateActionMenu(int index,int resID){
		pageIndex=index;
		actionMenu.put("index", resID);
		invalidateOptionsMenu();
	}
	
	public void updateCurFragment(int index,Fragment fragment){
		this.curFragment.put(index, fragment);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getMenuInflater().inflate(R.menu.menu_list_item, menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		if(actionMenu.get("index")==null){
			getMenuInflater().inflate(R.menu.menu_action_bar_2, menu);
		}else{
			getMenuInflater().inflate(actionMenu.get("index"), menu);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		switch (item.getItemId()) {
			case R.id.menu_item_file_settings: {
				Intent intent = new Intent();
				intent.setClass(this, SettingActivity.class);
				this.startActivity(intent);
			}
			case android.R.id.home: {
				if(pageIndex==0){
					setProgressBarIndeterminateVisibility(true);
					Fragment frag=fragments.get("caw.pd.player.fragments.SongsListFragment");
					fragmentTransaction.replace(R.id.list_frg_container,frag);
					fragmentTransaction.commit();
					((SongsListFragment)frag).loadData();
					//Fragment mainFrag=fragmentManager.findFragmentByTag("tag.main.list");
					//Fragment fragment = fragmentManager.findFragmentByTag(((SongsListFragment)mainFrag).getCurrentFragTag());
					
					//fragmentTransaction.hide(fragment);
					//fragmentTransaction.show(fragment)(R.id.list_frg_container, fragment);
					//fragmentTransaction.show(mainFrag);
				}
				if (pageIndex == 1) {
					fragmentTransaction.replace(R.id.infor_frg_container,
							fragments.get("caw.pd.player.fragments.PlayInforFragment"));
					fragmentTransaction.commit();
					MessageDispatcher.sendEqualizerEnableMessage(this,true);
				}
				getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
				getActionBar().setDisplayHomeAsUpEnabled(false);
			}
		}

		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		return super.onContextItemSelected(item);
	}
}