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


public class ArticleAdapter extends BaseAdapter {
    private Context context;

    private List<Article> itemlists;

    public final class ListItemView {                //自定义控件集合
        public ImageView image;
        public TextView title;
        public TextView brief;
        public TextView replay;
        public TextView assist;
        public TextView catName;
        public TextView date;
    }

    public void addList(List<Article> data) {
        this.itemlists.clear();
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

    public ArticleAdapter(Context context) {
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
            view = LayoutInflater.from(context).inflate(R.layout.act_learning_fragment_item, null);
            listItemView.image = (ImageView) view.findViewById(R.id.tv_photo);
            listItemView.title = (TextView) view.findViewById(R.id.tv_title);
            listItemView.catName=(TextView)view.findViewById(R.id.tv_catName);
            listItemView.brief=(TextView)view.findViewById(R.id.tv_brief);
            listItemView.replay=(TextView)view.findViewById(R.id.tv_replay);
            listItemView.assist=(TextView)view.findViewById(R.id.tv_assist);
            listItemView.date=(TextView)view.findViewById(R.id.tv_date);
            //设置控件集到view
            view.setTag(listItemView);
        } else {
            listItemView = (ListItemView) view.getTag();
        }
        listItemView.catName.setVisibility(View.GONE);
        listItemView.image.setTag(itemlists.get(position).getId());
        ImageLoader.getInstance().displayImage(itemlists.get(position).getImgUrl(), listItemView.image);
        listItemView.title.setText(itemlists.get(position).getTital());
        listItemView.brief.setText(itemlists.get(position).getSceneTitle());
        listItemView.replay.setText(itemlists.get(position).getReplyCount());
        listItemView.assist.setText(itemlists.get(position).getVoteCount());
        listItemView.date.setText(itemlists.get(position).getCreateData());


//        //注册点击事件
//        listItemView.image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(context, MerchantInfoActivity.class);
//                Bundle mBundle = new Bundle();
//                mBundle.putString("merchantId", v.getTag().toString());
//                intent.putExtras(mBundle);
//                context.startActivity(intent);
//                //Toast.makeText(VenueActivity.this, "场景列表被点击了!", Toast.LENGTH_SHORT).show();
//            }
//        });

        return view;
    }
}

