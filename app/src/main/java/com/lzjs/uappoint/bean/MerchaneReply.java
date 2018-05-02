package com.lzjs.uappoint.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/13.
 */
public class MerchaneReply implements Serializable {
    /**商家回复时间*/
    private String replyTime;
    /**商家回复内容*/
    private String replyContent;

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }
}
