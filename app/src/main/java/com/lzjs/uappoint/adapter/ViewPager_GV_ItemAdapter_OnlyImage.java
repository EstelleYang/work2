package com.lzjs.uappoint.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lzjs.uappoint.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import lzjs.com.picplayer.bean.ADInfo;

public class ViewPager_GV_ItemAdapter_OnlyImage extends BaseAdapter {

	private List<ADInfo> list_info;
	private Context context;
	/** ViewPager页码 */
	private int index;
	/** 根据屏幕大小计算得到的每页item个数 */
	private int pageItemCount;
	/** 传进来的List的总长度 */
	private int totalSize;

	/** 当前页item的实际个数 */
	// private int itemRealNum;
	@SuppressWarnings("unchecked")
	public ViewPager_GV_ItemAdapter_OnlyImage(Context context, List<?> list) {
		this.context = context;
		this.list_info = (List<ADInfo>) list;
	}

	public ViewPager_GV_ItemAdapter_OnlyImage(Context context, List<?> list, int index, int pageItemCount) {
		this.context = context;
		this.index = index;
		this.pageItemCount = pageItemCount;
		list_info = new ArrayList<ADInfo>();
		totalSize = list.size();
		// itemRealNum=list.size()-index*pageItemCount;
		// 当前页的item对应的实体在List<?>中的其实下标
		int list_index = index * pageItemCount;
		for (int i = list_index; i < list.size(); i++) {
			list_info.add((ADInfo) list.get(i));
		}

	}

	@Override
	public int getCount() {
		int size = totalSize / pageItemCount;
		if (index == size)
			return totalSize - pageItemCount * index;
		else
			return pageItemCount;
		// return itemRealNum;
	}

	@Override
	public Object getItem(int position) {
		// return null;
		return list_info.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder iv;
		if (convertView == null) {
			iv = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.channe_gridview_item_image, null);
			iv.iv_icon = (ImageView) convertView.findViewById(R.id.iv_gv_item_icon_image);
			convertView.setTag(iv);
		}else {
			iv = (ViewHolder) convertView.getTag();
		}
		iv.updateViews(position, null);
		return convertView;
	}

	class ViewHolder{
		ImageView iv_icon;

		protected void updateViews(int position, Object inst) {
			// 不管用
			// iv_icon.setBackgroundResource(list_info.get(position).getIconID());
			//iv_icon.setImageResource(list_info.get(position).getIconID());
			ImageLoader.getInstance().displayImage(list_info.get(position).getUrl(), iv_icon);

		}
	}


}
