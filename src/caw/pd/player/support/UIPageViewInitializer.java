package caw.pd.player.support;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import caw.pd.MusicShareActivity;
import caw.pd.R;
import caw.pd.player.Callback;
import caw.pd.player.Initializer;
import caw.pd.player.data.GuidePageAdapter;
import caw.pd.player.event.PageChangeEventListener;

public class UIPageViewInitializer implements Initializer<Activity> {
	private List<View> pageViews;
	private ViewPager viewPager;
	
	@Override
	public void initialize(Activity activity, Callback callback) {
		LayoutInflater inflater = activity.getLayoutInflater();
		pageViews = new ArrayList<View>();
		View View=inflater.inflate(R.layout.ui_music_list_pager, null);
		pageViews.add(View);
		View=inflater.inflate(R.layout.ui_music_info_pager, null);
		pageViews.add(View);
		View=inflater.inflate(R.layout.ui_music_share_pager, null);
		pageViews.add(View);
		
		ViewGroup main = (ViewGroup) inflater.inflate(R.layout.main_frame, null);

		GuidePageAdapter adapter = new GuidePageAdapter(pageViews);
		viewPager = (ViewPager) main.findViewById(R.id.viewpagerLayout);
		viewPager.setAdapter(adapter);
		
		viewPager.setCurrentItem(1);
		
		viewPager.setOnPageChangeListener(new PageChangeEventListener(activity));
		activity.setContentView(main);
		
		callback.doCallback(1);
	}

}
