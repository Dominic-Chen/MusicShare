package caw.pd.player;

import java.util.List;

import android.content.Context;
import android.widget.Adapter;

public interface DataSource {
	/**
	 * 创建数据源
	 * @param context
	 * @return
	 */
	public Adapter createAdapter(Context context);
	
	/**
	 * 获取数据
	 * @return
	 */
	public List getDatas();
}
