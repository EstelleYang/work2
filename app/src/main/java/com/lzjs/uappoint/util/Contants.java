/**
 * 常量类
 * wangdq
 * 2015-12-08
 */
package com.lzjs.uappoint.util;


public class Contants {

    /**
     * 服务器地址
     */
    //public static final String SERVER_ADDRESS = "http://182.92.197.85:8888/appoint-client";
    //public static final String SERVER_ADDRESS = "http://101.201.120.196:8080/dmservice";   //云1
    public static final String SERVER_ADDRESS = "http://118.190.90.11:8080/dmservice";   //云2
    //public static final String SERVER_ADDRESS = "http://10.0.2.2:8080/dmservice";  //本机
    //public static final String SERVER_ADDRESS = "http://192.168.1.100:8080/dmservice"; //单位
    //public static final String SERVER_ADDRESS = "http://192.168.3.14:8080/dmservice"; //家

    public static final  String RESOURCE_PATH=SERVER_ADDRESS+"/res/";
    public static final  String RESOURCE_PATH_ADVERT=SERVER_ADDRESS+"/res/advert/";
    public static final  String RESOURCE_PATH_HEAD=SERVER_ADDRESS+"/res/head/";
    public static final  String RESOURCE_PATH_PACS=SERVER_ADDRESS+"/res/pacs/";
    public static final  String RESOURCE_PATH_TOPIC=SERVER_ADDRESS+"/res/topic/";
    /**
     * 首页轮播图的查询
     */
    public static final String HOME_CAROUSEL_PICS = SERVER_ADDRESS + "/appjson/getadverts.do";
    /**
     * 首页轮播图的前缀
     */
    public static final String HOME_CAROUSEL_PICS_FX = SERVER_ADDRESS + "/res/advert/";
    /**
     * 首页国内专家
     */
    public static final String HOME_GNZJ_LIST=SERVER_ADDRESS+"/appjson/getexpertcountry.do";
    /**
     * 首页省内专家
     */
    public static final String HOME_SNZJ_LIST=SERVER_ADDRESS+"/appjson/getexpertprovince.do";
    /**
     * 首页肿瘤专家
     */
    public static final String HOME_ZLZJ_LIST=SERVER_ADDRESS+"/appjson/getexpertzl.do";
    /**
     * 更多肿瘤专家
     */
    public static final String HOME_ZLZJ_LIST_MORE=SERVER_ADDRESS+"/appjson/getexpertzlmore.do";
    /**
     * 专家详情
     */
    public static final String HOME_ZJ_DETAIL=SERVER_ADDRESS+"/appjson/getexpertdetail.do";

    /**
     * 专家头像前缀
     */
    public static final String HOME_ZJHEAD_FIX=SERVER_ADDRESS+"/res/head/";

    /**
     * 首页影像头条列表
     */
    public static final String TOPNEWSLISTS = SERVER_ADDRESS + "/appjson/refreshtopnewslist.do";

    /**
     * 影像圈话题列表
     */
    public static final String FRIENDSLISTS = SERVER_ADDRESS + "/appjson/refreshusertopics.do";

    /**
     * 会议/通告列表
     */
    public static final String MEETINGLISTS = SERVER_ADDRESS + "/appjson/refreshmeetings.do";

    /**
     * 影像列表
     */
    public static final String PACSLISTS = SERVER_ADDRESS + "/appjson/refreshpacslist.do";

    /**
     * 首页专题
     */
    public static final String HOME_ZHUANGTI =  "http://182.92.197.85:8888/appoint-client/business/scene-topic!listAjax.do";

    /**
     * 频道列表
     */
    public static final String CHANNEL_LISTS = SERVER_ADDRESS + "/appjson/getchannelitem.do";

    /**
     * 科室列表
     */
    public static final String DEPART_LISTS = SERVER_ADDRESS + "/appjson/getdepartitem.do";

    /**
     * 学术文章列表
     */
    public static final String ART_LISTS=SERVER_ADDRESS + "/appjson/refreshtopicsbychannel.do";
    /**
     * 获取验证码的
     */
    public static final String GET_VALIDATE_CODE = SERVER_ADDRESS + "/appjson/genAppSmsCode.do";

    /**
     * 判断验证码
     */
    public static final String JUDGMENT_VALIDATE_CODE = SERVER_ADDRESS + "/appjson/verityAppSmsCode.do";

    /**
     * 注册创建密码
     */
    public static final String REGISTER_CREATE_PASS = SERVER_ADDRESS + "/appjson/userregister.do";

    /**
     * 用户登录接口
     */
    public static final String USER_LOGIN = SERVER_ADDRESS + "/appjson/userverify.do";

    /**
     * 获取装备栏目信息
     */
    public static final String GET_MENUS_BYPARENT = SERVER_ADDRESS + "/business/product-index-get-info!listAjax.do";

    /**
     * 根据行业类型获取推荐商家信息
     */
    public static final String GET_PUSH_MERCHANTS = SERVER_ADDRESS + "/business/regi-merchant-info!getPushInfo.do";

    /**
     * 根据商家id获得评论信息
     */
    public static final String GET_EVALUATE_INFOS = SERVER_ADDRESS + "/business/evaluate-query!listAjax.do";

    /**
     * 根据商家ID获得该商家发布场地和信息
     */
    public static final String GET_PRODUCT_INFOS = SERVER_ADDRESS + "/business/product-order-get-info!getAllProductStatus.do";

    /**
     * 根据商家ID获得该商家发布场地信息
     */
    public static final String GET_VENUE_INFOS = SERVER_ADDRESS + "/business/venue-order-get-info!listAjax.do";


    /**
     * 根据场地ID获得该商家发布场地详细信息
     */
    public static final String GET_VENUE_INFO_DETAIL = SERVER_ADDRESS + "/business/venue-order-get-info!getVenueDetail.do";

    /**
     * 根据装备ID获得该商家发布装备详细信息
     */
    public static final String GET_PRODUCT_INFO_DETAIL = SERVER_ADDRESS + "/business/product-order-get-info!getProductDetail.do";

    /**
     * 保存场地订单详细信息
     */
    public static final String SAVE_VENUE_ORDER = SERVER_ADDRESS + "/business/venue-order-get-info!saveVenueOrder.do";

    /**
     * 根据栏目ID获得该栏目下发布装备信息
     */
    public static final String GET_PRODUCT_MENUS = SERVER_ADDRESS + "/business/product-order-get-info!listAjax.do";

    /**
     * 订单保存
     **/
    public static final String SAVE_ORDER = SERVER_ADDRESS + "/business/product-order-get-info!saveAOrderForm.do";
    /**
     * 订单明细接口
     **/
    public static final String ORDER_DETAIL = SERVER_ADDRESS + "/business/regi-user-order-query!getOrderDetail.do";

    /**
     * 完善用户注册信息
     */
    public static final String PERFECT_USER_MESS = SERVER_ADDRESS + "/sys/sys-account!incomplete.do";

    /**
     * 用户信息编辑的接口
     */

    public static final String USER_INFO_EDITOR = SERVER_ADDRESS + "/business/regi-user-info!save.do";

    /**
     * 附件上传
     */
    public static final String FILE_UPLOAD = SERVER_ADDRESS + "/appjson/usertopicimgupload.do";

    /**
     * 用户修改密码的接口
     */

    public static final String USER_RESIVE_PWD = SERVER_ADDRESS + "/business/regi-user-info!updatePwd.do";

    /**
     * 商家信息查询接口
     */
    public static final String GET_MERCHANT_INFO = SERVER_ADDRESS + "/business/regi-merchant-info!getMerchantInfo.do";

    /**
     * 收货地址查询接口
     */
    public static final String GET_ADDRESS_INFO_LIST = SERVER_ADDRESS + "/business/regi-user-info!getUserAddress.do";
    /**
     * 收货地址保存接口
     */
    public static final String SAVE_ADDRESS_INFO = SERVER_ADDRESS + "/business/regi-user-info!saveUserAddress.do";
    /**
     * 查询区域列表接
     */
    public static final String GET_AREAS =  "http://182.92.197.85:8888/appoint-client/sys/area!getCityByPid.do";
    /**
     * 查询县区列表接
     */
    public static final String GET_AREAS_BYCITY = "http://182.92.197.85:8888/appoint-client/sys/area!getCountByCity.do";

    /**
     * 获取用户的默认地址
     */
    public static final String GET_ADDRESS_DEFAULT = SERVER_ADDRESS + "/business/regi-user-info!getUserDefaultAddress.do";
    /**
     * 获取用户的默认地址
     */
    public static final String GET_ADDRESS_BYID = SERVER_ADDRESS + "/business/regi-user-info!getUserAddressById.do";
    /**
     * 用户场地订单查询接口
     */
    public static final String QUERY_USER_VENUE = SERVER_ADDRESS + "/business/regi-user-order-query!listAjax.do";
    /**
     * 圈子详情
     * http://101.201.120.196:8080/dmservice/appjson/getusertopic.do?id=UT0000001
     */
    public static final String QUERY_USER_QNUEN = SERVER_ADDRESS + "/appjson/getusertopic.do";

    public static final String REPLY_LIST=SERVER_ADDRESS +"/appjson/refreshusercomment.do";
    /**
     * 装备列表详细信息接口
     */
    public static final String QUERY__USER_QNUEN_INFO = SERVER_ADDRESS + "/business/merchant-order-query!getOrderFormDetail.do";


    /**
     * 用户信息查询接口
     */
    public static final String QUERY_USER_INFO = SERVER_ADDRESS + "/business/regi-user-index-get-info!getAregiUserById.do";

    /**
     * 删除用户地址信息接口
     */

    public static final String DEL_USER_ADDRESS_INFO = SERVER_ADDRESS + "/business/regi-user-info!delAddressId.do";

    /**
     * 收货人地址信息
     */
    public static final String ACCEPT_ADDRESS_INFO = SERVER_ADDRESS + "/business/regi-user-info!getUserAddressById.do";

    /**
     * 评论列表借口
     */
    public static final String USER_DISCUSS_LIST = SERVER_ADDRESS + "/business/evaluate-query!listAjaxRegiUserId.do";

    /**
     * 用户添加评论的接口
     */
    public static final String USER_ADD_DISCUSS = SERVER_ADDRESS + "/business/evaluate-query!saveEvaluate.do";
    /**
     * 用户找回密码接口
     */
    public static final String FIND_USER_PASSWORD = SERVER_ADDRESS + "/business/regi-user-info!retrievePasword.do";
    /**
     * 版本更新查询接口
     */
    public static final String QUERY_VERSION_CODE = SERVER_ADDRESS + "/business/check-version-update!listAjax.do";
    //商户PID
    public static final String PARTNER = "2088021214876870";
    //商户收款账号
    public static final String SELLER = "18993100938@189.cn";
    //商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALcMqI/xLH87OjhzHRJ78ElJpGgzG4EvRded53WozyxCTa+2DRH1trG2dalfCxns3BU9MS0eybeyRxdMqsB+H5+BzdHpnR14cHnLpKycHPkxrAncs5K9dq1URBqBwSuSq1q1Wa9hlqd4wGmhOOsxqEjlYBypwvp+B/NIaR6JVRObAgMBAAECgYAJ353r/bdHuZsfVl44FAVW/qjmkfMm2bTrt3G3r/5UxdLI6onz8QwEs/HOy/ieUl9gqedQJrv8s5oQEKuZLHRqH68MgOIK8rqnTcE97qrTgPyvcF+tw2RbMYtfZfNmHoJzJsqqhVl3D98Xc3+fCHwnQVTBXO1UPxVlcrkcyfqkeQJBAO83NdTWKcil4PgPfPh0QZvavVsmS992Boj1LUHIQqr9RCFoi+A+uDRMzdZHhIgBvPrP8bO8fdaHaZkgWchRNicCQQDD5JdOdI07FNLgbwtKrIr9JTOEcKlZ+YMZVap02kWI/NTy9+xkvTfzjH9KGtvBiQS/eSksbx6NdQ6jAUEXFvNtAkA+Moy8mjKjCP5FgUFtGE02yNkTKhHwOC/azGTIBdnPu6pLRXOZCrW6Y33hiRbchhcs34Rox0mwzeSW++JcQ0FhAkEAvLUP4R88EicsQdIXIZd3yQc2SqYxLiDD3vr8WtcN1zogdfLJQf5Z0P+Oe7fF7Plunnk8Zbahywlx0StbJcARPQJAI7E8XiNVl3OT/GRxxAhkKoOlAsfuNmXhYnMItV2d+hVj2vY4o4N46C8H8PCtu8XqVIHj1R9cqPPEPjapnfB0aA==";
    //支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

    public static final String GET_SCENE_DETAIL = SERVER_ADDRESS + "/business/scene-pages!list.do?sceneId=";

    //查询地市列表接口
    public static final String GET_AREA_LIST=SERVER_ADDRESS + "/appjson/getcitylist.do";

    //查询区域列表接口
    public static final String GET_AREA_TYPE = SERVER_ADDRESS + "/sys/area!getAreaType.do";

    /**
     * 查询区域列表接口
     */
    public static final String GET_FIND_INFO = SERVER_ADDRESS + "/business/regi-user-index-get-info!list.do";
    /**
     * 文章详情
     */
    public static final String GET_GUANG = SERVER_ADDRESS + "/appjson/topic.do?topicId=";
    /**
     * 会议/通告详情
     */
    public static final String GET_MEETING_TYPE = SERVER_ADDRESS + "/appjson/meeting.do?id=";
    /**
     * 商家信息列表
     */
    public static final String GET_MERCHANT_INFO_LIST = SERVER_ADDRESS + "/business/regi-user-merchant!getmerchantList.do";
    /**
     * 订单异步通知的地址
     */
    public static final String NOTIFY_URL = "http://182.92.197.85:8888/appoint/OrderBack";
    /**
     * 查询栏目
     */
    public static final String GET_search = SERVER_ADDRESS + "/business/a-menu!listAjax.do";
    /**
     * 根据商家ID获取该商家所有场地信息接口
     */
    public static final String GET_VENULIST_BY_ID = SERVER_ADDRESS + "/business/venue-order-get-info!list.do";
    /**
     * 用户端搜索全部接口
     */
    public static final String SEARCH_RESULT = SERVER_ADDRESS + "/business/regi-user-merchant!listMerchantName.do";
    /**
     * 用户端申请退款接口
     */
    public static final String APPLICATION_FOR_DRAWBACK = SERVER_ADDRESS + "/business/product-order-form!saveOrderFormRegiUser.do";
    /**
     * 用户端按类别搜索接口
     */
    public static final String SEARCH_CATEGORY_RESULT = SERVER_ADDRESS + "/business/regi-user-merchant!listMerchantNameMenuId.do";
    /**
     * 获取商家性质的接口
     */
    public static final String GET_MERCHANE_NATURE=SERVER_ADDRESS+"/business/regi-merchant-info!getAllMerchantId.do";
  /**
   * 根据订单ID和用户ID获取收货信息接口
   */
  public static final String GET_ORDER_ADDRESS=SERVER_ADDRESS+"/business/product-order-get-info!getUserAdd.do";

  /**
   * 上传图片
   */
  public static final String SEND_PIC=SERVER_ADDRESS+"/business/regi-user-info!saveHeadPicId.do";

  /**
   * 用户端物品订单删除接口
   */
  public static final String DELETE_ORDER_EQUIMENT=SERVER_ADDRESS+"/business/product-order-get-info!delOrderForm.do";
  /**
   * 场地订单详细接口
   */
  public static final String VENUE_ORDER_DETIAL=SERVER_ADDRESS+"/business/regi-user-order-query!getVenueOrderDetail.do";
  /**
   * 专题列表
   */
  public static final String SPECIAL_LIST=SERVER_ADDRESS+"/business/scene-pages!getAsceneTopicId.do";

  /**
   * 地图上显示商家
   */
  public static final String MERCHANT_MAPS=SERVER_ADDRESS+"/business/regi-merchant-info!findNeighPosition.do";
}
