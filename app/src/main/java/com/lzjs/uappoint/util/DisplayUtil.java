package com.lzjs.uappoint.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * dp、sp 转换为 px 的工具类
 *
 * @author fxsky 2012.11.12
 *
 */
public class DisplayUtil {
    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue
     * @param scale
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue
     * @param scale
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @param fontScale
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @param fontScale
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 获取屏幕密度（每寸像素：120/160/240/320）
     *
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static float getDPI(Activity context){
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;  // 屏幕宽度（像素）
        int height = metric.heightPixels;  // 屏幕高度（像素）
        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240 /320）
        return density;
    }

    public static DisplayImageOptions getOptions(){
        // 使用DisplayImageOptions.Builder()创建DisplayImageOptions
        DisplayImageOptions options= new DisplayImageOptions.Builder()
                .showStubImage(lzjs.com.picplayer.R.drawable.icon_empty)          // 设置图片下载期间显示的图片
                .showImageForEmptyUri(lzjs.com.picplayer.R.drawable.icon_error)  // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(lzjs.com.picplayer.R.drawable.icon_error)       // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中
                        //.displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片
                .build();// 创建配置过得DisplayImageOption对象

        return options;
    }

}