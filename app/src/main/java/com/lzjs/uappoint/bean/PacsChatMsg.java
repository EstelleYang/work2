package com.lzjs.uappoint.bean;

/**
 * Created by wangdq3  on 2017/3/4.
 * 交互消息
 */

public class PacsChatMsg {
    private static final String TAG = PacsChatMsg.class.getSimpleName();
    //名字
    private String username;
    //日期
    private String msgdate;
    //聊天内容
    private String msgtext;
    //是否为对方发来的信息  是：true  否：false
    private boolean msgtype = true;

    public PacsChatMsg() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMsgdate() {
        return msgdate;
    }

    public void setMsgdate(String msgdate) {
        this.msgdate = msgdate;
    }

    public String getMsgtext() {
        return msgtext;
    }

    public void setMsgtext(String msgtext) {
        this.msgtext = msgtext;
    }

    public boolean isMsgtype() {
        return msgtype;
    }

    public void setMsgtype(boolean msgtype) {
        this.msgtype = msgtype;
    }

    public PacsChatMsg(String username, String msgdate, String msgtext, boolean msgtype) {
        this.username = username;
        this.msgdate = msgdate;
        this.msgtext = msgtext;
        this.msgtype = msgtype;
    }
}
