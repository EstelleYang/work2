package com.lzjs.uappoint.util;

/**
 * Created by SangJP on 2018.4.22.
 */

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class ToolsUtil {

    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * md5 加密
     *
     * @param origin
     * @return
     */
    public static String md5Encode(String origin) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString
                    .getBytes()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultString.toUpperCase();
    }

    /**
     * 手机号码格式匹配
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,1,3,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        System.out.println(m.matches() + "-telnum-");
        return m.matches();
    }

    /**
     * 是否含有指定字符
     *
     * @param expression
     * @param text
     * @return
     */
    private static boolean matchingText(String expression, String text) {
        Pattern p = Pattern.compile(expression);
        Matcher m = p.matcher(text);
        boolean b = m.matches();
        return b;
    }

    /**
     * 邮政编码
     *
     * @param zipcode
     * @return
     */
    public static boolean isZipcode(String zipcode) {
        Pattern p = Pattern.compile("[0-9]\\d{5}");
        Matcher m = p.matcher(zipcode);
        System.out.println(m.matches() + "-zipcode-");
        return m.matches();
    }

    /**
     * 邮件格式
     *
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email) {
        Pattern p = Pattern
                .compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
        Matcher m = p.matcher(email);
        System.out.println(m.matches() + "-email-");
        return m.matches();
    }

    /**
     * 固话号码格式
     *
     * @param telfix
     * @return
     */
    public static boolean isTelfix(String telfix) {
        Pattern p = Pattern.compile("d{3}-d{8}|d{4}-d{7}");
        Matcher m = p.matcher(telfix);
        System.out.println(m.matches() + "-telfix-");
        return m.matches();
    }

    /**
     * 用户名匹配
     *
     * @param name
     * @return
     */
    public static boolean isCorrectUserName(String name) {
        Pattern p = Pattern.compile("([A-Za-z0-9]){2,10}");
        Matcher m = p.matcher(name);
        System.out.println(m.matches() + "-name-");
        return m.matches();
    }

    /**
     * 密码匹配，以字母开头，长度 在6-18之间，只能包含字符、数字和下划线。
     *
     * @param pwd
     * @return
     *
     */
    public static boolean isCorrectUserPwd(String pwd) {
        Pattern p = Pattern.compile("\\w{6,18}");
        Matcher m = p.matcher(pwd);
        System.out.println(m.matches() + "-pwd-");
        return m.matches();
    }

    /**
     * 检查是否存在SDCard
     *
     * @return
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 计算剩余日期
     *
     * @param
     * @return
     */
    public static String calculationRemainTime(String endTime, long countDown) {

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date now = new Date(System.currentTimeMillis());// 获取当前时间
            Date endData = df.parse(endTime);
            long l = endData.getTime() - countDown - now.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            return "剩余" + day + "天" + hour + "小时" + min + "分" + s + "秒";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void showLongToast(Context act, String pMsg) {
        Toast toast = Toast.makeText(act, pMsg, Toast.LENGTH_LONG);
        toast.show();
    }

    public static void showShortToast(Context act, String pMsg) {
        Toast toast = Toast.makeText(act, pMsg, Toast.LENGTH_SHORT);
        toast.show();
    }

    /**
     * 获取手机Imei号
     *
     * @param context
     * @return
     */
    public static String getImeiCode(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    /**
     * @author sunglasses
     * @param listView
     * @category 计算listview的高度
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
