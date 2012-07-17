package caw.pd.player.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import caw.pd.R;

public class SongDetailsFragment extends Fragment {
	private View view;
	private boolean isInitialized=false;
	private List<Tab> tabs=new ArrayList();

	private void initFragmentView(final View view,Bundle savedInstanceState) {
		final ActionBar bar = getActivity().getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        if(!isInitialized){
        	Tab tab=bar.newTab()
	                .setText("歌手信息")
	                .setTabListener(new TabListener<ArtistDetailsFragment>(
	                        getActivity(), "artist", ArtistDetailsFragment.class));
        	tabs.add(tab);
        	tab=bar.newTab()
	                .setText("专辑信息")
	                .setTabListener(new TabListener<AlbumDetailsFragment>(
	                		getActivity(), "album", AlbumDetailsFragment.class));
        	tabs.add(tab);
        	
        	addTabsToBar();
	        isInitialized=true;
        }
        if (savedInstanceState != null) {
            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }
        
		bar.setDisplayHomeAsUpEnabled(true);
	}
	
	private void addTabsToBar(){
		if(!tabs.isEmpty()){
			final ActionBar bar = getActivity().getActionBar();
			for(int i=0,count=tabs.size();i<count;i++){
				bar.addTab(tabs.get(i));
			}
		}
	}
	
	public void showAllTabs(boolean isShow) {
		if (isShow) {
			addTabsToBar();
		} else {
			ActionBar bar = getActivity().getActionBar();
			bar.removeAllTabs();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		container.removeAllViews();
		view=inflater.inflate(R.layout.ui_music_details_view, container);
		initFragmentView(container,savedInstanceState);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;
        private final Bundle mArgs;
        private Fragment mFragment;

        public TabListener(Activity activity, String tag, Class<T> clz) {
            this(activity, tag, clz, null);
        }

        public TabListener(Activity activity,String tag, Class<T> clz, Bundle args) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
            mArgs = args;

            // Check to see if we already have a fragment for this tab, probably
            // from a previously saved state.  If so, deactivate it, because our
            // initial state is that a tab isn't shown.
            mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
            if (mFragment != null && !mFragment.isDetached()) {
                FragmentTransaction ft = mActivity.getFragmentManager().beginTransaction();
                ft.detach(mFragment);
                ft.commit();
            }
        }

        public void onTabSelected(Tab tab, FragmentTransaction ft) {
            if (mFragment == null) {
                mFragment = Fragment.instantiate(mActivity, mClass.getName(), mArgs);
                ft.add(R.id.infor_frg_container, mFragment, mTag);
            } else {
                ft.attach(mFragment);
            }
        }

        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                ft.detach(mFragment);
            }
        }

        public void onTabReselected(Tab tab, FragmentTransaction ft) {
            Toast.makeText(mActivity, "Reselected!", Toast.LENGTH_SHORT).show();
        }
    }
}
