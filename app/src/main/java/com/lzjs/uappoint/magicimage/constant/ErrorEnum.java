package com.lzjs.uappoint.magicimage.constant;

/**
 * Created by SangJP on 2018.4.23.
 */

public enum ErrorEnum {
    ERROR_ENUM_7001(-7001,"未检测到身份证，请对准边框(请避免拍摄时倾角和旋转角过大、摄像头)"),
    ERROR_ENUM_7002(-7002,"请使用第二代身份证件进行扫描"),
    ERROR_ENUM_7003(-7003,"不是身份证正面照片(请使用带证件照的一面进行扫描)"),
    ERROR_ENUM_7004(-7004,"不是身份证反面照片(请使用身份证反面进行扫描)"),
    ERROR_ENUM_7005(-7005,"确保扫描证件图像清晰"),
    ERROR_ENUM_7006(-7006,"请避开灯光直射在证件表面"),
    ERROR_ENUM_OTHER(-1,"识别失败，请稍后重试");

    private String errormsg;
    private int errorcode;

    private ErrorEnum(int errorcode,String errormsg) {
        this.errorcode = errorcode;
        this.errormsg = errormsg;
    }

    public static String getErrormsg(int errorcode) {
        for (ErrorEnum e:ErrorEnum.values()) {
            if (e.getErrorcode() == errorcode) {
                return e.errormsg;
            }
        }
        return ERROR_ENUM_OTHER.ERROR_ENUM_OTHER.name();
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

}
