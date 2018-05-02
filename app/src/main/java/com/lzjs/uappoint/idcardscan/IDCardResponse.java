package com.lzjs.uappoint.idcardscan;

import java.io.Serializable;

/**
 * 扫描返回的身份证数据
 *
 * 注意：此类不可混淆！！！！！
 */

public class IDCardResponse implements Serializable {


    /**
     * errorcode : 0
     * errormsg : OK
     * session_id :
     * name : 李明
     * name_confidence_all : [99,99]
     * sex : 男
     * sex_confidence_all : [68]
     * nation : 汉
     * nation_confidence_all : [39]
     * birth : 1987/1/1
     * birth_confidence_all : [68,69,65]
     * address : 北京市石景山区高新技术园腾讯大楼
     * address_confidence_all : [13,19,36,19,2,45,45,8,17,14,4,15,5,13,29,0]
     * id : 440524198701010014
     * id_confidence_all : [54,56,76,59,62,58,68,69,65,75,80,72,77,69,74,70,68,51]
     * frontimage : /9j/4AAQSkZJRgAv1qHuXE//Z
     * frontimage_confidence_all : []
     * watermask_confidence_all : []
     * valid_date_confidence_all : []
     * authority_confidence_all : []
     * backimage_confidence_all : []
     * detail_errorcode : []
     * detail_errormsg : []
     */
    private int errorcode;
    private String errormsg;
    private String session_id;
    private String name;
    private String sex;
    private String nation;
    private String birth;
    private String address;
    private String id;
    private String frontimage;

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrontimage() {
        return frontimage;
    }

    public void setFrontimage(String frontimage) {
        this.frontimage = frontimage;
    }
}
