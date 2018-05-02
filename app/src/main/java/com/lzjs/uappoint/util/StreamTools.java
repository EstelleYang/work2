/**
 * 流处理类
 * zhangxw
 * 2015-12-08
 *
 */
package com.lzjs.uappoint.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class StreamTools {
	
	/**
	 * 把输入流的内容转化成字符串
	 * */
	public static String readInputStream(InputStream is){
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int len = 0;
			byte[] buffer = new byte[1024];
			while((len = is.read(buffer))!=-1){
				baos.write(buffer,0,len);
			}
			is.close();
			baos.close();
			byte[] result = baos.toByteArray();
			String returnStr = new String(result,"UTF-8");
			return returnStr;
		} catch (Exception e) {
			e.printStackTrace();
			return "获取失败!";
		}
	}

}
