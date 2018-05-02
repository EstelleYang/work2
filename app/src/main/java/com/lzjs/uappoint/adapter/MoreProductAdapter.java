package com.lzjs.uappoint.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzjs.uappoint.R;
import com.lzjs.uappoint.bean.NewProduct;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/11.
 */
public class MoreProductAdapter extends BaseAdapter{
    private List<NewProduct> data;
    private Context context;

    public MoreProductAdapter(Context context) {
        this.context = context;
        data=new ArrayList<>();
    }
    public void setList(List<NewProduct> data){
        this.data=data;
        notifyDataSetChanged();

    }


    @Override
    public int getCount() {
        return data.size();
    }


    @Override
    public NewProduct getItem(int position) {
        return data.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder holder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.more_list_item,parent,false);
            holder=new ViewHolder();
            holder.name= (TextView) convertView.findViewById(R.id.tv_name_more);
            holder.price= (TextView) convertView.findViewById(R.id.tv_price_more);
            holder.num= (TextView) convertView.findViewById(R.id.tv_num_more);
            holder.pic= (ImageView) convertView.findViewById(R.id.imageview_more);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        ImageLoader.getInstance().displayImage(data.get(position).getProductPic(),holder.pic);
        holder.name.setText(data.get(position).getProductName());
        holder.price.setText(data.get(position).getProductPrice());
        holder.num.setText("X"+data.get(position).getNum()+"");

        return convertView;
    }

    public static class ViewHolder{
        TextView num;
        TextView name;
        TextView price;
        ImageView pic;
    }
}
