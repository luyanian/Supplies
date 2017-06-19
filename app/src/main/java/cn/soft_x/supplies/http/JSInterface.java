package cn.soft_x.supplies.http;

import android.app.Activity;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.maverick.utils.Cfg;

import cn.soft_x.supplies.utils.Constant;


/**
 * JS 使用
 * Created by Administrator on 2016-11-10.
 */
public class JSInterface {

    private Activity mActivity;

    public JSInterface(Activity activity) {
        mActivity = activity;
    }

    @JavascriptInterface
    public void finish() {
        mActivity.finish();
    }

    @JavascriptInterface
    public String getUserID() {
        if (!TextUtils.isEmpty(Constant.USER_ID)) {
            return Constant.USER_ID;
        } else {
            if (Cfg.loadStr(mActivity, Constant.SH_USER_ID).equals("defaults") || TextUtils.isEmpty(Cfg.loadStr(mActivity, Constant.SH_USER_ID))) {
                return "这个是空的你知道吗！";
            } else {
                return Cfg.loadStr(mActivity, Constant.SH_USER_ID);
            }
        }
    }
}
