package com.lzjs.uappoint.bean;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lzjs on 15/12/23.
 */


public class PacsInfo implements Serializable {

    private String uploadid;
    private String nodeid;
    private String userid;
    private String username;
    private String headimage;
    private String pacsno;
    private String hisaccno;
    private String patientname;
    private String hiscode;
    private String hisname;
    private String recommentcode;
    private String recommentname;
    private String expertid;
    private String paycode;
    private String advice;
    private String pacsstatus;
    private String createdate;
    private String payfeedate;
    private String advicedate;
    private String rollbackdate;
    private String remarks;
    private String pacstype;

    public String getUploadid() {
        return uploadid;
    }

    public void setUploadid(String uploadid) {
        this.uploadid = uploadid;
    }

    public String getNodeid() {
        return nodeid;
    }

    public void setNodeid(String nodeid) {
        this.nodeid = nodeid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    public String getPacsno() {
        return pacsno;
    }

    public void setPacsno(String pacsno) {
        this.pacsno = pacsno;
    }

    public String getHisaccno() {
        return hisaccno;
    }

    public void setHisaccno(String hisaccno) {
        this.hisaccno = hisaccno;
    }

    public String getPatientname() {
        return patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public String getHiscode() {
        return hiscode;
    }

    public void setHiscode(String hiscode) {
        this.hiscode = hiscode;
    }

    public String getHisname() {
        return hisname;
    }

    public void setHisname(String hisname) {
        this.hisname = hisname;
    }

    public String getRecommentcode() {
        return recommentcode;
    }

    public void setRecommentcode(String recommentcode) {
        this.recommentcode = recommentcode;
    }

    public String getRecommentname() {
        return recommentname;
    }

    public void setRecommentname(String recommentname) {
        this.recommentname = recommentname;
    }

    public String getExpertid() {
        return expertid;
    }

    public void setExpertid(String expertid) {
        this.expertid = expertid;
    }

    public String getPaycode() {
        return paycode;
    }

    public void setPaycode(String paycode) {
        this.paycode = paycode;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getPacsstatus() {
        return pacsstatus;
    }

    public void setPacsstatus(String pacsstatus) {
        this.pacsstatus = pacsstatus;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getPayfeedate() {
        return payfeedate;
    }

    public void setPayfeedate(String payfeedate) {
        this.payfeedate = payfeedate;
    }

    public String getAdvicedate() {
        return advicedate;
    }

    public void setAdvicedate(String advicedate) {
        this.advicedate = advicedate;
    }

    public String getRollbackdate() {
        return rollbackdate;
    }

    public void setRollbackdate(String rollbackdate) {
        this.rollbackdate = rollbackdate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPacstype() {
        return pacstype;
    }

    public void setPacstype(String pacstype) {
        this.pacstype = pacstype;
    }
}
