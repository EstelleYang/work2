package com.lzjs.uappoint.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by zhangxiaowu on 15/12/16.
 */
public class MyFastJSONHelper {


    /**
     * 判断请求是否成功
     * @param jsonData : 请求结果
     * @return
     */
    public static boolean requestIsSuccess(String jsonData){
        boolean bResult = false;
        if( !StringUtils.isEmpty(jsonData) ){
            JSONObject resultObj = JSONObject.parseObject(jsonData);
            String resultCode = resultObj.getString("result");
            if("200".equals(resultCode)){
                bResult = true;
            }
        }
        return bResult;
    }

    public static JSONArray  getResultData(String jsonData){
        JSONArray resultDataArr = null;
        if(requestIsSuccess(jsonData)){
            JSONObject jsonObjData = JSONObject.parseObject(jsonData);
            JSONObject responseTempData = jsonObjData.getJSONObject("Response");
            resultDataArr = responseTempData.getJSONArray("datas");
        }
        return resultDataArr;
    }
}
