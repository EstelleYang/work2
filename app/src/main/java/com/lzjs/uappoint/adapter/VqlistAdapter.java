package com.lzjs.uappoint.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzjs.uappoint.R;
import com.lzjs.uappoint.act.LoginActivity;
import com.lzjs.uappoint.act.VenueOrderActivity;
import com.lzjs.uappoint.act.VenueViewActivity;
import com.lzjs.uappoint.bean.Venue;
import com.lzjs.uappoint.util.DisplayUtil;
import com.lzjs.uappoint.util.SharedUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/29.
 */
public class VqlistAdapter extends BaseAdapter {
    private Context context;
    List<Venue> venueList;
    private String VenueId;
    private String merchantId;

    public final class ListItemView {                //自定义控件集合
        public ImageView image;
        public TextView venueIntr;//简介
        public TextView venue_yuanjia;//原价
        public TextView venue_xianjia;//现价
        public TextView venue_name;//场地名称
        public Button orderButton;
    }

    public VqlistAdapter(Context context,String merchantId) {
        this.context = context;
        this.venueList = new ArrayList<>();
        this.merchantId=merchantId;
    }
    public void addList(List<Venue> data) {
        this.venueList.clear();
        if (data!=null) {
            this.venueList.addAll(data);
            // 通知适配器数据源改变
            notifyDataSetChanged();
        }
    }

    public void updateList(List<Venue> data) {
        this.venueList.clear();
        this.venueList.addAll(data);
        notifyDataSetChanged();

    }
    @Override
    public int getCount() {
        return venueList.size();
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
        if (convertView == null) {
            listItemView = new ListItemView();
            convertView = LayoutInflater.from(context).inflate(R.layout.venue_listitem, null);
            listItemView.image = (ImageView) convertView.findViewById(R.id.venueItemView1);
            listItemView.venueIntr = (TextView) convertView.findViewById(R.id.venue_Intr);
            listItemView.venue_yuanjia = (TextView) convertView.findViewById(R.id.venue_yuanjia);
            listItemView.venue_xianjia = (TextView) convertView.findViewById(R.id.venue_xianjia);
            listItemView.venue_name = (TextView) convertView.findViewById(R.id.venue_name);


            listItemView.orderButton = (Button) convertView.findViewById(R.id.orderButton);
            convertView.setTag(listItemView);
        } else
            listItemView = (ListItemView) convertView.getTag();
        ImageLoader.getInstance().displayImage(venueList.get(position).getVenuePic(), listItemView.image, DisplayUtil.getOptions());
        listItemView.venueIntr.setText(venueList.get(position).getVenueIntr());
        listItemView.venue_name.setText(venueList.get(position).getVenueName());
        listItemView.venue_yuanjia.setText("￥" + venueList.get(position).getVenuePrice());
        listItemView.venue_xianjia.setText("￥" + venueList.get(position).getVenueZkPrice());
        listItemView.venue_yuanjia.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        final int pos = position;
        //注册点击事件
        listItemView.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VenueId = venueList.get(pos).getVenueId();

                Intent intent = new Intent(context, VenueViewActivity.class);
                Bundle mbundle = new Bundle();
                mbundle.putString("venueId", VenueId);
                intent.putExtras(mbundle);
                context.startActivity(intent);
            }
        });

        listItemView.venueIntr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VenueId = venueList.get(pos).getVenueId();
                Intent intent = new Intent(context, VenueViewActivity.class);
                Bundle mbundle = new Bundle();
                mbundle.putString("venueId", VenueId);
                intent.putExtras(mbundle);
                context.startActivity(intent);
            }
        });

        //预约按钮点击事件
        listItemView.orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                Bundle mbundle = new Bundle();
                //判断用户是否登录
                String userId = SharedUtils.getLoginUserId(context);
                VenueId = venueList.get(pos).getVenueId();
                if (userId != null && !userId.equals("")) {
                    intent = new Intent(context, VenueOrderActivity.class);

                    mbundle.putString("venueId", VenueId);
                    mbundle.putString("userId", userId);
                    mbundle.putString("merchantId", merchantId);
                    intent.putExtras(mbundle);
                } else {
                    intent = new Intent(context, LoginActivity.class);
                    mbundle.putString("venueId", VenueId);
                }
                if (intent != null) {
                    context.startActivity(intent);
                }

            }

        });

        return convertView;
    }
}