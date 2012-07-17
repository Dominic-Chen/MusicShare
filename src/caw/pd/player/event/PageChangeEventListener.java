package caw.pd.player.event;

import java.util.Map;

import android.app.Fragment;
import android.content.Context;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.widget.Toast;
import caw.pd.MusicShareActivity;
import caw.pd.R;
import caw.pd.player.fragments.SongsListFragment;

public class PageChangeEventListener implements OnPageChangeListener {
	private Context context;
	
	public PageChangeEventListener(Context context){
		this.context=context;
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		Log.d("k", "onPageSelected - " + arg0);
		switch (arg0) {
			case 0: {
				
				//setProgressBarIndeterminateVisibility(true);
				((MusicShareActivity)context).updateActionMenu(0, R.menu.menu_action_bar_1);
				Map fragments=((MusicShareActivity)context).getAttachedFragments();
				Fragment frag=(Fragment)fragments.get("caw.pd.player.fragments.SongsListFragment");
				((SongsListFragment)frag).loadData();
				/*View songsListView=view.findViewById(R.id.list);
				if(songsListView!=null){
					if(((ListView) songsListView).getAdapter().getCount()==0){
						listInitializer.initialize(viewPager.getChildAt(0));
					}
				}*/
				break;
			}
			case 1:{
				((MusicShareActivity)context).updateActionMenu(1, R.menu.menu_action_bar_2);
				Toast.makeText(context, "音乐播放", Toast.LENGTH_LONG).show();
				break;
			}
			case 2:{
				((MusicShareActivity)context).updateActionMenu(2, R.menu.menu_action_bar_3);
				
				Toast.makeText(context, "音乐分享", Toast.LENGTH_LONG).show();
				break;
			}
		}
	}
}
