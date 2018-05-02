/**
 * 屏幕适配工具类
 * zhangxw
 * 2015-12-08
 */
package com.lzjs.uappoint.util;

import android.content.Context;

/**
 * Created by Administrator on 2015/10/12.
 */
public class FacilityUtil {

    /**
     * dp转px
     * @param context 上下文
     * @param dipValue dp值
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
