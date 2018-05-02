package com.lzjs.uappoint.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.lzjs.uappoint.R;
import com.lzjs.uappoint.bean.Article;
import com.lzjs.uappoint.bean.CollecteArticle;

import java.util.ArrayList;
import java.util.List;


public abstract class CollectionArticleAdapter<T> extends RecyclerView.Adapter<CollectionArticleAdapter.ViewHolder> {

    protected ArrayList<T> mObjects;
    private Context ctx;
    protected final Object mLock = new Object();


    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public interface OnItemClickListener {
        void OnItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemLongClickListener {
        void OnItemLongClick(View view, int position);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public CollectionArticleAdapter(Context ctx, List<T> l) {
        this.ctx = ctx;
        mObjects = new ArrayList<T>();
        if (l != null)
            mObjects.addAll(l);
    }


    @Override
    public CollectionArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //设置布局文件
        if(ctx==null){
            ctx = parent.getContext();
        }

        View view = LayoutInflater.from(ctx).inflate(R.layout.item_collection_article,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CollectionArticleAdapter.ViewHolder holder, int position) {
        //设置点击事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.OnItemClick(holder.itemView, pos);
                }
            });
        }

        //设置长按事件
        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemLongClickListener.OnItemLongClick(v, pos);
                    return true;
                }
            });
        }
        CollecteArticle article = (CollecteArticle) mObjects.get(position);
        holder.articleTitle.setText(article.getTital());
        /**
         * 之后日期修改为收藏时间
         */
        holder.articleDate.setText(article.getCreateData());
        Glide.with(ctx).load(article.getImgPath()).into(holder.articleImg);

        /*holder.play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                add(pos);
            }
        });
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getLayoutPosition();
                delete(pos);
            }
        });*/
    }


    //删除
    protected abstract void delete(int position);

    //添加
    protected abstract void add(int position);


    @Override
    public int getItemCount() {
        return mObjects.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        View articleView;
        CardView cardView;
        ImageView articleImg;
        TextView articleTitle;
        TextView articleDate;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView)view;
            articleDate = (TextView)view.findViewById(R.id.article_date);
            articleImg = (ImageView)view.findViewById(R.id.article_img);
            articleTitle = (TextView)view.findViewById(R.id.article_title);
        }
    }

    /**
     * 添加数据
     *
     * @param position
     * @param item
     */
    public void addData(int position, T item) {
        synchronized (mLock) {
            mObjects.add(item);
        }
        notifyItemInserted(position);
    }

    /**
     * 移除数据
     *
     * @param position
     */
    public void removeData(int position) {
        synchronized (mLock) {
            mObjects.remove(position);
        }
        notifyItemRemoved(position);
    }

    public void clear() {
        this.mObjects.clear();
    }

}
