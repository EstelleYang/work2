package com.lzjs.uappoint.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzjs.uappoint.R;
import com.lzjs.uappoint.bean.Doctor;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;


public class ExpertAdapter extends BaseAdapter {
    private Context context;

    private List<Doctor> itemlists;

    public final class ListItemView {                //自定义控件集合
        public ImageView image;
        public TextView name;
        public TextView depart;
        public TextView special;
        public TextView title;
        public TextView post;
        public TextView degree;
    }

    public void addList(List<Doctor> data) {
        this.itemlists.clear();
        if (data!=null) {
            this.itemlists.addAll(data);
            // 通知适配器数据源改变
            notifyDataSetChanged();
        }
    }

    public void updateList(List<Doctor> data) {
        this.itemlists.clear();
        this.itemlists.addAll(data);
        notifyDataSetChanged();

    }

    public ExpertAdapter(Context context) {
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
            view = LayoutInflater.from(context).inflate(R.layout.act_expert_fragment_item, null);
            listItemView.image = (ImageView) view.findViewById(R.id.tv_image);
            listItemView.name = (TextView) view.findViewById(R.id.tv_name);
            listItemView.depart=(TextView)view.findViewById(R.id.tv_depart);
            listItemView.special=(TextView)view.findViewById(R.id.tv_special);
            listItemView.title=(TextView)view.findViewById(R.id.tv_title);
            listItemView.post=(TextView)view.findViewById(R.id.tv_post);
            listItemView.degree=(TextView)view.findViewById(R.id.tv_degree);
            //设置控件集到view
            view.setTag(listItemView);
        } else {
            listItemView = (ListItemView) view.getTag();
        }
        //listItemView.catName.setVisibility(View.GONE);
        listItemView.image.setTag(itemlists.get(position).getLoginid());
        ImageLoader.getInstance().displayImage(itemlists.get(position).getHeadimage(), listItemView.image);
        listItemView.name.setText(itemlists.get(position).getUsername());
        listItemView.depart.setText(itemlists.get(position).getDeptname());
        listItemView.special.setText(itemlists.get(position).getSpecial());
        listItemView.title.setText(itemlists.get(position).getTitlename());
        listItemView.post.setText(itemlists.get(position).getPostname());
        listItemView.degree.setText(itemlists.get(position).getDegreename());


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

