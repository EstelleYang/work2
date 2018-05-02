package com.lzjs.uappoint.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzjs.uappoint.R;
import com.lzjs.uappoint.bean.Venue;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/13.
 */
public class EqlistAdapter extends BaseAdapter {

        private Context context;
        List<Venue> productList;

        public final class ListItemView {                //自定义控件集合
            public ImageView image;
            public TextView price;//价格
            public TextView title;//标题
            public TextView bussenss;//商家
            public TextView addre;//地址
        }
        public EqlistAdapter ( Context context){
            this.context=context;
            this.productList=new ArrayList<>();
        }

    public void addList(List<Venue> data) {
        this.productList.clear();
        if (data!=null) {
            this.productList.addAll(data);
            // 通知适配器数据源改变
            notifyDataSetChanged();
        }
    }

    public void updateList(List<Venue> data) {
        this.productList.clear();
        this.productList.addAll(data);
        notifyDataSetChanged();

    }
        @Override
        public int getCount() {
            return productList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //自定义视图
            ListItemView listItemView = null;
            if(convertView==null){
                listItemView=new ListItemView();
                convertView= LayoutInflater.from(context).inflate(R.layout.act_item,null);
                listItemView.image= (ImageView) convertView.findViewById(R.id.arcimgView);
                listItemView.price= (TextView) convertView.findViewById(R.id.arctital);
                convertView.setTag(listItemView);
            }else
                listItemView = (ListItemView) convertView.getTag();

            ImageLoader.getInstance().displayImage(productList.get(position).getMerchantPic(), listItemView.image);
            listItemView.price.setText(productList.get(position).getMerchantName()+"");
            return convertView;
        }
    }

