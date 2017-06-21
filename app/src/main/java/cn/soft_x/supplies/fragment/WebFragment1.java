package cn.soft_x.supplies.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.maverick.utils.Cfg;
import com.maverick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.activity.InvitationActivity;
import cn.soft_x.supplies.activity.NearCompanyActivity;
import cn.soft_x.supplies.activity.SettingActivity;
import cn.soft_x.supplies.activity.TruckActivity;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.utils.Constant;

/**
 * 订单
 * Created by Administrator on 2017-01-11.
 */
public class WebFragment1 extends BaseFragment {
    @BindView(R.id.fragment_web_webView)
    WebView mFragmentWebWebView;

    private String webUrl = HttpUrl.API_HOST + "/s/page/alldingdan.html";

    private String phone = "";
    /**
     * 搜索内容
     */
    private String searchInfo = "";
    /**
     * 搜索类型
     */
    private String searchType = "";
    /**
     * 附近拆解企业id
     */
    private String nearCompanyId = "";
    /**
     * 货车id
     */
    private String truckId = "";
    /**
     * 新闻id
     */
    private String newsId = "";
    /**
     * 关联id
     */
    private String glId = "";

    /**
     * 订单类型
     */
    private String ddlx = "";
    /**
     * 混合id
     */
    private String hhid = "";
    /**
     * 采购id
     */
    private String cgId = "";
    /**
     * 订单id
     */
    private String orderId = "";

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_web;
    }

    @Override
    protected void initView() {
        mContext.showProgressDialog("正在加载...");
    }


    @Override
    protected void initData() {
        if (!isHidden()){
            mContext.getRlTitleRoot().setVisibility(View.GONE);
            Logger.i("web 1 显示");
        }else {
            Logger.i("web 1 隐藏");
        }
        mFragmentWebWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mFragmentWebWebView.getSettings().setJavaScriptEnabled(true);
        mFragmentWebWebView.addJavascriptInterface(new JsInterFace(), "jsinterface");
        //启动缓存
        mFragmentWebWebView.getSettings().setAppCacheEnabled(true);
        //设置缓存模式
        mFragmentWebWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //貌似不可信
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mContext.dismissProgressDialog();
            }


        });
        mFragmentWebWebView.setWebChromeClient(new WebChromeClient());
        mFragmentWebWebView.loadUrl(webUrl);
    }

    private void callPhone(String phone) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel://" + phone));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constant.REQUEST_PERMISSION_CALL_PHONE_CODE) {
            if (grantResults.length >= 1) {
                int cameraResult = grantResults[0];//相机权限
                boolean cameraGranted = cameraResult == PackageManager.PERMISSION_GRANTED;//拍照权限
                if (cameraGranted) {
                    //具有电话权限
                    callPhone(phone);
                } else {
                    //不具有相关权限，给予用户提醒，比如Toast或者对话框，让用户去系统设置-应用管理里把相关权限开启
                    ToastUtil.showToast(mContext, "电话权限未设置，可能出现使用异常！请去“设置-应用管理”中设置照相权限");
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public class JsInterFace {

        /**
         * 得到userId
         *
         * @return
         */
        @JavascriptInterface
        public String getUserID() {
            if (!TextUtils.isEmpty(Constant.USER_ID)) {
                return Constant.USER_ID;
            } else {
                if (Cfg.loadStr(mContext, Constant.SH_USER_ID).equals("defaults")
                        || TextUtils.isEmpty(Cfg.loadStr(mContext, Constant.SH_USER_ID))) {
                    return "我没有USER_ID,别找我要啦！";
                } else {
                    return Cfg.loadStr(mContext, Constant.SH_USER_ID);
                }
            }
        }
        /**
         * 得到roleId
         *
         * @return
         */
        @JavascriptInterface
        public String getroleId() {
            if (!TextUtils.isEmpty(Constant.ROLE_ID)) {
                return Constant.ROLE_ID;
            } else {
                if (Cfg.loadStr(mContext, Constant.SH_ROLE_ID).equals("defaults")
                        || TextUtils.isEmpty(Cfg.loadStr(mContext, Constant.SH_ROLE_ID))) {
                    return "我没有ROLE_ID,别找我要啦！";
                } else {
                    return Cfg.loadStr(mContext, Constant.SH_ROLE_ID);
                }
            }
        }
        /**
         * 打电话
         *
         * @param phone
         */
        @JavascriptInterface
        public void goCallPhone(String phone) {
            WebFragment1.this.phone = phone;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.CALL_PHONE}, Constant.REQUEST_PERMISSION_CALL_PHONE_CODE);
                } else {
                    callPhone(phone);
                }
            } else {
                callPhone(phone);
            }

        }

        @JavascriptInterface
        public String getSearchInfo() {
            return searchInfo;
        }

        @JavascriptInterface
        public String getNearCompanyId() {
            if (TextUtils.isEmpty(nearCompanyId)) {
                return "没有东西";
            } else {
                return nearCompanyId;
            }
        }

        @JavascriptInterface
        public String getTruckId() {
            if (TextUtils.isEmpty(truckId)) {
                return "没有东西";
            } else {
                return truckId;
            }
        }

        @JavascriptInterface
        public String getSearchType() {
            return searchType;
        }

        @JavascriptInterface
        public String getNewsId() {
            if (TextUtils.isEmpty(newsId)) {
                return "没有东西";
            } else {
                return newsId;
            }
        }
        /**
         * 订单id
         *
         * @return
         */
        @JavascriptInterface
        public String getOrderId() {
            if (TextUtils.isEmpty(orderId)) {
                return "没有东西";
            } else {
                return orderId;
            }
        }

        public static final String SETTING_AC = "1";
        public static final String NEAR_COMPANY_AC = "2";
        public static final String INVITATION_AC = "3";
        public static final String TRUCK_AC = "4";

        /**
         * 跳转的activity
         * <p/>
         * 1-> 设置界面
         * 2-> 附近的拆解企业
         * 3-> 邀请函
         * 4-> 货车
         *
         * @param type
         */
        @JavascriptInterface
        public void goActivity(String type) {
            Intent intent = null;
            Logger.i(type);
            switch (type) {
                case SETTING_AC:
                    intent = new Intent(mContext, SettingActivity.class);
                    startActivity(intent);
                    break;
                case NEAR_COMPANY_AC:
                    intent = new Intent(mContext, NearCompanyActivity.class);
                    startActivity(intent);
                    break;
                case INVITATION_AC:
                    intent = new Intent(mContext, InvitationActivity.class);
                    startActivity(intent);
                    break;
                case TRUCK_AC:
                    intent = new Intent(mContext, TruckActivity.class);
                    startActivity(intent);
                    break;
            }
        }

        /**
         * 打印 toast
         *
         * @param toast 内容
         */
        @JavascriptInterface
        public void showToastMsg(String toast) {
            ToastUtil.showToast(mContext, toast);
        }

        /**
         * 得到app的使用类型
         *
         * @return "ghs"->供货商
         * "dhs"->调货商
         */
        @JavascriptInterface
        public String getUserType() {
            return "ghs";
        }

        /**
         * 得到关联ID
         *
         * @return
         */
        @JavascriptInterface
        public String getGLID() {
            if (TextUtils.isEmpty(glId)) {
                return "没有东西";
            } else {
                return glId;
            }
        }

        /**
         * 得到采购id
         *
         * @return
         */
        @JavascriptInterface
        public String getCGID() {
            if (TextUtils.isEmpty(cgId)) {
                return "没有东西";
            } else {
                return cgId;
            }
        }

        @JavascriptInterface
        public String getDDLX() {
            if (TextUtils.isEmpty(ddlx)) {
                return "没有东西";
            } else return ddlx;
        }

        @JavascriptInterface
        public String getHHID() {
            if (TextUtils.isEmpty(hhid)) {
                return "没有东西";
            } else return hhid;
        }

        @JavascriptInterface
        public String showBackButton(){
            return "hide";
        }


    }

}
