package cn.soft_x.supplies.utils;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by Administrator on 2016-11-01.
 */
public class Constant {
    public static LatLng LOCATION = null;
    /**
     * 拍照权限申请code
     */
    public static int REQUEST_PERMISSION_CAMERA_CODE = 0x666;
    /**
     * 电话权限申请code
     */
    public static int REQUEST_PERMISSION_CALL_PHONE_CODE = 0x888;
    /**
     * intent key
     */
    public static final String AC_TYPE = "type_ac";
    public static final String INTENT_IDENTITY = "identity";
    public static final String WEBVIEW_URL = "url";
    public static final String PHONE = "phone";
    public static final String VER = "ver";
    public static final String PWD = "pwd_";
    /**
     * SharedPreferences key
     */
    public static final String SH_USER_NC = "user_nc";
    public static final String SH_FIRST_LOGIN = "first_login";
    public static final String SH_IS_LOGIN = "islogin";
    public static final String SH_PWD = "pwd";
    public static final String SH_USER_NAME = "username";
    public static final String SH_USER_ID = "user_id";
    /**
     * intent value
     */
    /**
     * 供货商
     */
    public static final int SUPPLIER = 0X0001;
    /**
     * 调货商
     */
    public static final int TRANSFER = 0X0002;

    public static String USER_ID = "";

    public static String USER_HEAD_IMG = "";
    /**
     * WebActivity intent key
     */
    /**
     * web url
     */
    public static final String WEB_URL = "url";
    /**
     * web type
     */
    public static final String WEB_SEARCH_TYPE = "type";
    /**
     * web search
     */
    public static final String WEB_SEARCH = "search";
    /**
     * web nearCompany ID
     */
    public static final String WEB_NEAR_ID = "near";
    /**
     * web truck ID
     */
    public static final String WEB_TRUCK_ID = "truck";
    /**
     * web news ID
     */
    public static final String WEB_NEWS_ID = "news";
    /**
     * web order ID
     */
    public static final String WEB_ORDER_ID = "order";
    /**
     * web msg ID
     */
    public static final String WEB_GLID = "GL";
    /**
     * web cg ID
     */
    public static final String WEB_CGID = "CG";
    /**
     * web 订单类型
     */
    public static final String WEB_DDLX = "ddlx";
    /**
     * web 二次混合ID
     */
    public static final String WEB_HUNHEID = "hunhe";

    public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCNmmVRfRSwH1jJ8Ou2Eyx0xKFh" + "\r" +
            "X6TTAnCDw7YEP0PGlgXvZxY7tPhPvYWv9ubz6jPz4SnSYHC3HPppxZf6SAPRVkZ7" + "\r" +
            "9WrEKYWwGXcMCx7LLhIb+9A8L1vSjn6yJx4R/vfMFNLPCYMxPUugw0vhxVAhpBzQ" + "\r" +
            "0+lvXzW7982ZWp55OQIDAQAB" + "\r";
}