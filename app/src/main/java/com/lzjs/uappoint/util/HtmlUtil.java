/**
 * html格式处理类
 * zhangxw
 * 2015-12-08
 */
package com.lzjs.uappoint.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {  
    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符
      
    /** 
     * @param htmlStr 
     * @return 
     *  删除Html标签 
     */  
    public static String delHTMLTag(String htmlStr) {
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签  
  
        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签  
  
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签  
  
        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签  
        return htmlStr.trim(); // 返回文本字符串  
    }  
    
    public static String getFirstImgUrl(String htmlStr){
    	List<String> pics = getListImgUrl(htmlStr);
    	if(pics.isEmpty()){
    		return "";
    	}
    	return pics.get(0);
    }
    
    public static List<String> getListImgUrl(String htmlStr){
    	String img = "";
    	Pattern p_image;
    	Matcher m_image;
    	List<String> pics = new ArrayList<String>();
    	String regEx_img ="<img.*src\\s*=\\s*(.*?)[^>]*?>";
    	p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
    	m_image = p_image.matcher(htmlStr);
    	while (m_image.find()) {
    		img = img + "," + m_image.group();
    		Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
	    	while (m.find()) {
	    		pics.add(m.group(1));
	    	}
    	}
    	return pics; 
    }
    
    /**
     * 把html中的所有图片路径换为绝对路径
     * @param htmlStr
     * @return
     */
    public static String replaceImgUrl(String htmlStr, String absPath){
    	String img = "";
    	Pattern p_image;
    	Matcher m_image;
    	String regEx_img ="<img.*src\\s*=\\s*(.*?)[^>]*?>";
    	p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
    	m_image = p_image.matcher(htmlStr);
    	while (m_image.find()) {
    		img = img + "," + m_image.group();
    		Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
	    	while (m.find()) {
	    		String imgUrl = m.group(1);
	    		htmlStr = htmlStr.replace("\""+imgUrl+"\"", "\""+absPath+imgUrl+"\"");
	    	}
    	}
    	
    	return htmlStr;
    }
      
    public static String getTextFromHtml(String htmlStr){
        htmlStr = delHTMLTag(htmlStr);  
        htmlStr = htmlStr.replaceAll("&nbsp;", "");  
        htmlStr = htmlStr.substring(0, htmlStr.indexOf("。")+1);  
        return htmlStr;  
    }  
      
    public static void main(String[] args) {
        String str = "<p style=\"text-align:center;\"><img src=\"/gsczjlb/upload/image/20150604/20150604094838_357.jpg\" alt=\"\" /></p>";
        System.out.println(getFirstImgUrl(str));
    }  
}  
