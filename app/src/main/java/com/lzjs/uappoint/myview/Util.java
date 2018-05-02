package com.lzjs.uappoint.myview;

import android.content.Context;
import android.view.WindowManager;

public class Util {

	
	public static int getWindowWidth(Context context){
		WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		return width;
	}
	
	public static int getWindowHeight(Context context){
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		int height = wm.getDefaultDisplay().getHeight();
		return height;
	}
}
