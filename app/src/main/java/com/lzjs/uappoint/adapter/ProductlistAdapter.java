package com.lzjs.uappoint.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzjs.uappoint.R;
import com.lzjs.uappoint.act.EqDetailActivity;
import com.lzjs.uappoint.bean.Product;
import com.lzjs.uappoint.calback.MyListener;
import com.lzjs.uappoint.util.DisplayUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ProductlistAdapter extends BaseAdapter implements View.OnClickListener{
    private Context context;
    List<Product> productList;
    private MyListener listener;
    /**
     * Called when a view has been clicked.
     *
     */



    public final class ListItemView {                //自定义控件集合
        public ImageView image;
        public TextView price;//实际价格
        public TextView title;//标题
        public ImageButton image_button_jian;
        public ImageButton image_button_add;
        public EditText buy_num;
        public TextView productIntr;
        public TextView eq_listitem_price_dazhe;//打折前价格
    }
    public ProductlistAdapter ( Context context){
        this.context=context;
        this.productList=new ArrayList<>();
    }
    @Override
    public int getCount() {
        return productList.size();
    }

    public  void setListener(MyListener listener) {
        this.listener = listener;

    }

    @Override
    public Product getItem(int position) {
        return productList.get(position);
    }

    public void addList(List<Product> data) {
        this.productList.clear();
        if (data!=null) {
            this.productList.addAll(data);
            // 通知适配器数据源改变
            notifyDataSetChanged();
        }
    }

    public void updateList(List<Product> data) {
        this.productList.clear();
        this.productList.addAll(data);
        notifyDataSetChanged();

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //自定义视图
        ListItemView listItemView = null;
        if(convertView==null){
            listItemView=new ListItemView();
            convertView= LayoutInflater.from(context).inflate(R.layout.list_item,null);
            listItemView.image= (ImageView) convertView.findViewById(R.id.eq_listitem_imgView);
            listItemView.price= (TextView) convertView.findViewById(R.id.eq_listitem_price);
            listItemView.title= (TextView) convertView.findViewById(R.id.eq_list_tital);
            listItemView.image_button_add= (ImageButton) convertView.findViewById(R.id.image_button_add);
            listItemView.image_button_jian= (ImageButton) convertView.findViewById(R.id.image_button_jian);
            listItemView.buy_num= (EditText) convertView.findViewById(R.id.eq_buy_num);
            listItemView.productIntr= (TextView) convertView.findViewById(R.id.productIntr);
            listItemView.eq_listitem_price_dazhe= (TextView) convertView.findViewById(R.id.eq_listitem_price_dazhe);
            convertView.setTag(listItemView);
        }else
            listItemView = (ListItemView) convertView.getTag();
        //Log.e(TAG, "position:" + position);
        ImageLoader.getInstance().displayImage(productList.get(position).getProductPic(), listItemView.image, DisplayUtil.getOptions());
        listItemView.price.setText("￥" + productList.get(position).getProductZkPrice());
        listItemView.title.setText(productList.get(position).getProductName());
        listItemView.buy_num.setText(productList.get(position).getNum()+"");
        listItemView.productIntr.setText(productList.get(position).getProductIntr());
        listItemView.eq_listitem_price_dazhe.setText("￥"+productList.get(position).getProductPrice());
        listItemView.eq_listitem_price_dazhe.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        listItemView.image_button_add.setTag(position);
        listItemView.image_button_jian.setTag(position);

        listItemView.image_button_add.setOnClickListener(this);
        listItemView.image_button_jian.setOnClickListener(this);

        //注册点击事件
        final String productId=productList.get(position).getProductId();
        listItemView.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, EqDetailActivity.class);
                Bundle mbundle=new Bundle();
                mbundle.putString("productId", productId);
                intent.putExtras(mbundle);
                context.startActivity(intent);
            }
        });
        listItemView.productIntr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, EqDetailActivity.class);
                Bundle mbundle=new Bundle();
                mbundle.putString("productId", productId);
                intent.putExtras(mbundle);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    /**
     * 点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        //1代表点击了加 2代表点击了减
        switch (v.getId()){
            case R.id.image_button_add:
                //库存数量
                int productNum=Integer.parseInt(productList.get((Integer) v.getTag()).getProductNum());
                if (productNum>productList.get((Integer)v.getTag()).getNum()) {
                    listener.onItemClick((Integer) v.getTag(), 1);
                }else{
                    v.setClickable(false);
                    Toast.makeText(context, "抱歉，库存数量不足！", Toast.LENGTH_SHORT).show();
                }
                Log.e("+++", "++++++++++" + productList.get((Integer) v.getTag()).getNum());
                break;
            case R.id.image_button_jian:
               try{
                   if (productList.get((Integer)v.getTag()).getNum()>0) {
                       v.setClickable(true);
                       listener.onItemClick((Integer) v.getTag(), 2);
                       Log.e("-----","-----------"+productList.get((Integer)v.getTag()).getNum());
                   }
               }catch (Exception e){

               }
                break;

        }

    }
}