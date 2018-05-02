package com.lzjs.uappoint.myview.base;

/**
 * Created by mt on 2017/1/6.
 */

import android.view.View;

import com.lzjs.uappoint.myview.ViewHolder;

public interface ItemViewDelegate<T>
{

    public abstract int getItemViewLayoutId();

    public abstract boolean isForViewType(T item, int position);

    public abstract void convert(ViewHolder holder, T t, int position);



}
