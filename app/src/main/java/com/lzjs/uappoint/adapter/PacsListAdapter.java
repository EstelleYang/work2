package com.lzjs.uappoint.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.model.Text;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.act.BodyAuthenticateActivity;
import com.lzjs.uappoint.act.ImagePagerActivity;
import com.lzjs.uappoint.bean.PacsInfo;
import com.lzjs.uappoint.fresco.FrescoMainActivity;
import com.lzjs.uappoint.myview.CollapsibleTextView;
import com.lzjs.uappoint.myview.NoScrollGridView;
import com.lzjs.uappoint.util.DisplayUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DELL on 2017/1/23.
 */

public class PacsListAdapter extends BaseAdapter {
    private Context mContext;
    private List<PacsInfo> items;
    private boolean clickflag=false;
    public PacsListAdapter(Context ctx, List<PacsInfo> items) {
        this.mContext = ctx;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.act_pacs_list_item, null);
            holder.civ_head = (ImageView) convertView.findViewById(R.id.civ_head);
            holder.tv_user_name=(TextView) convertView.findViewById(R.id.tv_user_name);
            holder.tv_fdata = (TextView) convertView.findViewById(R.id.tv_fdata);
            holder.content = (CollapsibleTextView) convertView.findViewById(R.id.content);
            holder.gridview = (NoScrollGridView) convertView.findViewById(R.id.gv_pacs);
            holder.tv_comment=(TextView)convertView.findViewById(R.id.tv_comment);
            holder.tv_support=(TextView)convertView.findViewById(R.id.tv_support);
            holder.tv_hosname=(TextView)convertView.findViewById(R.id.hosname);
            holder.tv_recommentname=(TextView)convertView.findViewById(R.id.recommentname);
            holder.tv_pacstype=(TextView)convertView.findViewById(R.id.pacstype);
            holder.tv_pacsstatus=(TextView)convertView.findViewById(R.id.pacsstatus);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        PacsInfo itemEntity = items.get(position);
        holder.tv_fdata.setText(itemEntity.getPayfeedate() == null ? itemEntity.getCreatedate() : itemEntity.getPayfeedate());
        holder.content.setText(itemEntity.getRemarks());
        holder.tv_user_name.setText(itemEntity.getUsername());
        holder.tv_hosname.setText(itemEntity.getHisname());
        holder.tv_recommentname.setText(itemEntity.getRecommentname()+"医生");
        holder.tv_pacstype.setText(itemEntity.getPacstype());

        if ("3".equals(itemEntity.getPacsstatus()) || "4".equals(itemEntity.getPacsstatus())) {
            Drawable drawableY= mContext.getResources().getDrawable(R.drawable.list_check_press);
            drawableY.setBounds(0,0,drawableY.getMinimumWidth(),drawableY.getMinimumHeight());
            holder.tv_pacsstatus.setCompoundDrawables(drawableY,null,null,null);
        } else {
            Drawable drawableN= mContext.getResources().getDrawable(R.drawable.list_pause_press);
            drawableN.setBounds(0,0,drawableN.getMinimumWidth(),drawableN.getMinimumHeight());
            holder.tv_pacsstatus.setCompoundDrawables(drawableN,null,null,null);
        }

        ImageLoader.getInstance().displayImage(itemEntity.getHeadimage(), holder.civ_head, DisplayUtil.getOptions());
        final List<String> imageUrls = null; //itemEntity.getImages();
        if (imageUrls == null || imageUrls.size() == 0) { // 没有图片资源就隐藏GridView
            holder.gridview.setVisibility(View.GONE);
        } else {
            holder.gridview.setAdapter(new NoScrollGridAdapter(mContext, imageUrls));
            holder.gridview.setVerticalSpacing(3);
            holder.gridview.setHorizontalSpacing(5);
        }
        // 点击回帖九宫格，查看大图
        holder.gridview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(imageUrls);
                imageBrower(position,arrayList);
            }
        });
        holder.tv_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickflag){
                    view.setSelected(false);
                    clickflag=false;
                }else{
                    view.setSelected(true);
                    clickflag=true;
                }

            }
        });

        holder.tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,FrescoMainActivity.class);
                Bundle mbundle = new Bundle();
                mbundle.putInt("tabId", 4);
                intent.putExtras(mbundle);
                mContext.startActivity(intent);
            }
        });

        holder.tv_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,FrescoMainActivity.class);
                Bundle mbundle = new Bundle();
                mbundle.putInt("tabId", 0);
                intent.putExtras(mbundle);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }



    /**
     * 打开图片查看器
     *
     * @param position
     * @param urls2
     */
    protected void imageBrower(int position, ArrayList<String> urls2) {
        Intent intent = new Intent(mContext, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        mContext.startActivity(intent);
    }
    /**
     * listview组件复用，防止“卡顿”
     *
     * @author Administrator
     *
     */
    class ViewHolder {
        private ImageView civ_head;
        private TextView tv_fdata;
        private CollapsibleTextView content;
        private  TextView tv_user_name;
        private NoScrollGridView gridview;
        private  TextView tv_comment;
        private  TextView tv_support;
        private TextView tv_hosname;
        private TextView tv_recommentname;
        private TextView tv_pacstype;
        private TextView tv_pacsstatus;
    }
}
