package com.lzjs.uappoint.adapter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.amap.api.maps2d.model.Text;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lzjs.uappoint.R;
import com.lzjs.uappoint.act.BodyAuthenticateActivity;
import com.lzjs.uappoint.act.ImagePagerActivity;
import com.lzjs.uappoint.bean.Article;
import com.lzjs.uappoint.myview.NoScrollGridView;
import com.lzjs.uappoint.util.DisplayUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.redare.imagepicker.AndroidImagePicker;
import com.redare.imagepicker.activity.ImagePreviewActivity;
import com.redare.imagepicker.activity.ImagesGridActivity;
import com.redare.imagepicker.bean.ImageItem;
import com.redare.imagepicker.ui.ImagePreviewFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Created by DELL on 2017/1/23.
 */

public class FriendsListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Article> items;
    private boolean clickflag=false;
    public FriendsListAdapter(Context ctx, List<Article> items) {
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
            convertView = View.inflate(mContext, R.layout.act_friends_list_item, null);
            holder.civ_head = (ImageView) convertView.findViewById(R.id.civ_head);
            holder.tv_user_name=(TextView) convertView.findViewById(R.id.tv_user_name);
            holder.tv_fdata = (TextView) convertView.findViewById(R.id.tv_fdata);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            holder.gridview = (NoScrollGridView) convertView.findViewById(R.id.gv_friends);
            holder.tv_comment=(TextView)convertView.findViewById(R.id.tv_comment);
            holder.tv_support=(TextView)convertView.findViewById(R.id.tv_support);
            holder.tv_viewcount=(TextView)convertView.findViewById(R.id.tv_view_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Article itemEntity = items.get(position);
        holder.tv_fdata.setText(itemEntity.getCreateData());
        holder.content.setText(itemEntity.getTital());
        holder.tv_user_name.setText(itemEntity.getAuthor());
        holder.tv_comment.setText(itemEntity.getReplyCount());
        holder.tv_support.setText(itemEntity.getVoteCount());
        holder.tv_viewcount.setText(itemEntity.getViewCount());
        ImageLoader.getInstance().displayImage(itemEntity.getImgUrl(), holder.civ_head, DisplayUtil.getOptions());
        final List<String> imageUrls = itemEntity.getImages();
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
                TextView tvitem = (TextView) view;
                if(clickflag){
                    view.setSelected(false);
                    tvitem.setText(String.valueOf(Integer.parseInt(tvitem.getText().toString()) - 1));
                    clickflag=false;
                }else{
                    view.setSelected(true);
                    tvitem.setText(String.valueOf(Integer.parseInt(tvitem.getText().toString()) + 1));
                    clickflag=true;
                }

            }
        });

        holder.tv_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,BodyAuthenticateActivity.class);
                Bundle mbundle = new Bundle();
                mbundle.putBoolean("isImgShow", true);
                mbundle.putString("titleShow", "发表评论");
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
     * @author Administrator
     */
    class ViewHolder {
        private ImageView civ_head;
        private TextView tv_fdata;
        private TextView content;
        private  TextView tv_user_name;
        private NoScrollGridView gridview;
        private  TextView tv_comment;
        private  TextView tv_support;
        private  TextView tv_viewcount;
    }
}
