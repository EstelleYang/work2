package com.lzjs.uappoint.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzjs.uappoint.R;
import com.lzjs.uappoint.bean.Article;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/28.
 */
public class MyAdapter extends BaseAdapter {


        private Context context;

        private List<Article> itemlists;

        public final class ListItemView {                //自定义控件集合
            public ImageView image;
            public TextView title;
        }

        //        public void setList(List<Article> data){
//            this.itemlists=data;
//            notifyDataSetChanged();
//
//        }
        public void addList(List<Article> data) {
            if (data!=null) {
                this.itemlists.addAll(data);
                // 通知适配器数据源改变
                notifyDataSetChanged();
            }
        }

        public void updateList(List<Article> data) {
            this.itemlists.clear();
            this.itemlists.addAll(data);
            notifyDataSetChanged();

        }

        public void chooseList(List<Article> data) {
            this.itemlists.clear();
            this.itemlists.addAll(data);
            notifyDataSetChanged();

        }

        public MyAdapter(Context context) {
            this.context = context;
            this.itemlists=new ArrayList<>();
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return itemlists.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup arg2) {
            // TODO Auto-generated method stub
            //自定义视图
            ListItemView listItemView = null;
            if (view == null) {
                listItemView = new ListItemView();
                view = LayoutInflater.from(context).inflate(R.layout.act_item,null);
                listItemView.image = (ImageView) view.findViewById(R.id.arcimgView);
                listItemView.title = (TextView) view.findViewById(R.id.arctital);
                //设置控件集到view
                view.setTag(listItemView);
            } else {
                listItemView = (ListItemView) view.getTag();
            }

            listItemView.image.setTag(itemlists.get(position).getSceneId());
            ImageLoader.getInstance().displayImage(itemlists.get(position).getInfoPic(), listItemView.image);
            listItemView.title.setText(itemlists.get(position).getSceneTitle());


//            //注册点击事件
//            listItemView.image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent=new Intent(context,PublishInfoDetailActivity.class);
//                    Bundle mbundle=new Bundle();
//                    mbundle.putString("sceneId", v.getTag().toString());
//                    intent.putExtras(mbundle);
//                    context.startActivity(intent);
//                }
//            });

            return view;
        }





}
