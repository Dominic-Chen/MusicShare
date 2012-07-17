package caw.pd.player.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ImageAdapter extends BaseAdapter {
	private Context mContext;
	private List mThumb;
	

	public ImageAdapter(Context c,Collection thumbImages) {
		mContext = c;
		mThumb=new ArrayList(thumbImages);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mThumb.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView imageview;
		if (convertView == null) {
			imageview = new ImageView(mContext);
			imageview.setLayoutParams(new GridView.LayoutParams(100, 100));
			imageview.setScaleType(ImageView.ScaleType.CENTER_CROP);
			//imageview.setPadding(1, 2, 1, 1);
		} else {
			imageview = (ImageView) convertView;
		}
		Object obj=mThumb.get(position);
		if(obj instanceof Bitmap){
			imageview.setImageBitmap((Bitmap)obj);
		}else if(obj instanceof Integer){
			imageview.setImageResource((Integer)obj);
		}
		return imageview;
	}

}
