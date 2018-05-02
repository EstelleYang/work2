package com.lzjs.uappoint.adapter;

/**
 * Created by DELL on 2017/1/23.
 */

import java.util.ArrayList;
import java.util.List;

import com.lzjs.uappoint.R;
import com.lzjs.uappoint.util.DisplayUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class NoScrollGridAdapter extends BaseAdapter {

    /** 上下文 */
    private Context ctx;
    /** 图片Url集合 */
    private List<String> imageUrls;

    public NoScrollGridAdapter(Context ctx, List<String> urls) {
        this.ctx = ctx;
        this.imageUrls = urls;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return imageUrls == null ? 0 : imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return imageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(ctx, R.layout.channe_gridview_item_image, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_gv_item_icon_image);
        ImageLoader.getInstance().displayImage(imageUrls.get(position), imageView, DisplayUtil.getOptions());
        return view;
    }

}
