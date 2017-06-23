package cn.soft_x.supplies.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.model.LatLng;
import com.maverick.utils.Cfg;
import com.maverick.utils.EditTextUtils;
import com.maverick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.activity.nonelectrical.MainActivity2;
import cn.soft_x.supplies.application.SuppliesApplication;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.http.MyXUtilsCallBack;
import cn.soft_x.supplies.model.LoginModel;
import cn.soft_x.supplies.model.RoleDetailModel;
import cn.soft_x.supplies.utils.Constant;
import cn.soft_x.supplies.utils.encryption.AES;
import cn.soft_x.supplies.utils.encryption.Base64Decoder;
import cn.soft_x.supplies.utils.encryption.Base64Encoder;
import cn.soft_x.supplies.utils.encryption.MD5;
import cn.soft_x.supplies.utils.encryption.RSA;


public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        init();
    }

    //    @Override
    //    protected void initBaseTitle() {
    //        getRlTitleRoot().setVisibility(View.GONE);
    ////        setStatusBarColor(android.R.color.transparent);
    //    }

    private void init() {
        if (Cfg.loadBoolean(this, Constant.SH_FIRST_LOGIN, true) || (!Cfg.loadBoolean(this, Constant.SH_IS_LOGIN, false))) {
            Cfg.saveBoolean(this, Constant.SH_FIRST_LOGIN, false);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
            return;
        }
        if (Cfg.loadStr(this, Constant.SH_PWD).equals("defaults") || Cfg.loadStr(this, Constant.SH_USER_NAME).equals("defaults")) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 2000);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    autoLogin();
                }
            }, 1000);
        }
    }

    private void autoLogin() {
        Map<String,String> map = new HashMap<>();
        map.put("loginid",Cfg.loadStr(this, Constant.SH_USER_NAME));
        map.put("password", Cfg.loadStr(this, Constant.SH_PWD));
        map.put("shebei","0");
        map.put("yhtype","1");
        String json = JSON.toJSONString(map);
        String search = getParamsAESEncode(json);
        String signature = getParamsRSEEncode(json);
        RequestParams params = new RequestParams(HttpUrl.LOGIN);
        params.addBodyParameter("search", search);
        params.addBodyParameter("signature", signature);

        x.http().post(params, new MyXUtilsCallBack() {
            LoginModel model;

            @Override
            public void success(String result) {
                model = JSON.parseObject(result, LoginModel.class);
                Constant.USER_ID = model.getYHID();
                Constant.USER_NAME = model.getYHNC();
                Constant.ROLE_ID = model.getRoleId();
                LatLng latLng = new LatLng(Float.parseFloat(model.getLatitude()), Float.parseFloat(model.getLongitude()));
                Constant.LOCATION = latLng;
                Constant.USER_HEAD_IMG = HttpUrl.API_HOST + model.getYHTX();
            }

            @Override
            public void finished() {
                if (isSuccess()) {
                    Cfg.saveStr(WelcomeActivity.this, Constant.SH_USER_NC, model.getYHNC());
                    Cfg.saveStr(WelcomeActivity.this, Constant.SH_USER_ID, model.getYHID());
                    Cfg.saveStr(WelcomeActivity.this,Constant.SH_ROLE_ID,model.getRoleId());
                    Cfg.saveBoolean(WelcomeActivity.this, Constant.SH_IS_LOGIN, true);
                    JPushInterface.setAlias(SuppliesApplication.getAppContext(), model.getYHID(), new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {
                            Logger.i("%d<--->%s", i, s);
                        }
                    });
                    getRoleDetail(model.getRoleId());
                } else {
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    Cfg.saveBoolean(WelcomeActivity.this, Constant.SH_IS_LOGIN, false);
                    ToastUtil.showToast(WelcomeActivity.this, "登录失败！");
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    private void getRoleDetail(final String roleId) {
        RequestParams params = new RequestParams(HttpUrl.ROLEDETAIL);
        params.addBodyParameter("roleId", roleId);
        x.http().get(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {
                if(isSuccess()) {
                    RoleDetailModel roleDetailModel = JSON.parseObject(result, RoleDetailModel.class);
                    Constant.ROLE_YWTAGS = roleDetailModel.getYwtags();
                    Cfg.saveStr(WelcomeActivity.this, Constant.SH_ROLE_YWTAGS, roleDetailModel.getYwtags());
                    Cfg.saveStr(WelcomeActivity.this, Constant.SH_ROLE_CODE, roleDetailModel.getRoleCode());
                    Cfg.saveStr(WelcomeActivity.this, Constant.SH_ROLE_NAME, roleDetailModel.getRoleName());
                    if("2".equals(roleDetailModel.getYwtags())){
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity2.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void finished() {

            }
        });
    }
    public String getParamsAESEncode(String json) {
        try {
            String utf8 = new String(json.getBytes("UTF-8"), "UTF-8");
            String md5 = MD5.md5(utf8).substring(0,16);
            String aes = AES.encrypt(md5,json);
            return aes;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public String getParamsRSEEncode(String json) {
        try {
            String utf8 = new String(json.getBytes("UTF-8"), "UTF-8");
            String md5 = MD5.md5(utf8).substring(0,16);
            byte[] buffer = Base64Decoder.decodeToBytes(Constant.publicKey);
            return Base64Encoder.encode(RSA.encryptByPublicKey(md5.getBytes(), buffer));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
