package caw.pd.player.support;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import caw.pd.MusicShareActivity;
import caw.pd.R;
import caw.pd.player.Callback;
import caw.pd.player.Initializer;

public class UIFragmentsInitializer implements Initializer<Activity> {

	@Override
	public void initialize(Activity activity, Callback callback) {
		Map<String, Fragment> fragments = new HashMap();

		String[] fragNames = new String[] {
				"caw.pd.player.fragments.AlbumListFragment",
				"caw.pd.player.fragments.ArtistListFragment",
				"caw.pd.player.fragments.SongsListFragment",
				"caw.pd.player.fragments.PlayInforFragment",
				"caw.pd.player.fragments.SongDetailsFragment",
				"caw.pd.player.fragments.ShareMapFragment",
				"caw.pd.player.fragments.EqualizerFxFragment"};
		for (int i = 0; i < fragNames.length; i++) {
			Fragment tmpFragment = Fragment.instantiate(activity, fragNames[i]);
			fragments.put(fragNames[i], tmpFragment);
		}
		FragmentManager fragmentManager = activity.getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		// fragmentTransaction.add(fragments.get("caw.pd.player.fragments.ArtistListFragment"),
		// "tag.sub.artist.list");
		// fragmentTransaction.add(fragments.get("caw.pd.player.fragments.AlbumListFragment"),
		// "tag.sub.album.list");
		fragmentTransaction.add(R.id.list_frg_container,
				fragments.get("caw.pd.player.fragments.SongsListFragment"),
				"tag.main.list");
		((MusicShareActivity) activity).updateCurFragment(0,
				fragments.get("caw.pd.player.fragments.SongsListFragment"));

		fragmentTransaction.add(R.id.infor_frg_container,
				fragments.get("caw.pd.player.fragments.PlayInforFragment"),
				"tag.main.info");
		((MusicShareActivity) activity).updateCurFragment(1,
				fragments.get("caw.pd.player.fragments.PlayInforFragment"));

		/*
		 * fragmentTransaction.add(R.id.share_frg_container,
		 * fragments.get("caw.pd.player.fragments.ShareMapFragment"
		 * ),"tag.main.share"); curFragment.put(2,
		 * fragments.get("caw.pd.player.fragments.ShareMapFragment"));
		 */

		fragmentTransaction.commit();
		callback.doCallback(fragments);
	}

}
