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
import com.maverick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.application.SuppliesApplication;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.http.MyXUtilsCallBack;
import cn.soft_x.supplies.model.LoginModel;
import cn.soft_x.supplies.utils.Constant;


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
        RequestParams params = new RequestParams(HttpUrl.LOGIN);
        params.addBodyParameter("loginid", Cfg.loadStr(this, Constant.SH_USER_NAME));
        params.addBodyParameter("password", Cfg.loadStr(this, Constant.SH_PWD));
        params.addBodyParameter("shebei", "0");
        params.addBodyParameter("yhtype", "1");

        x.http().post(params, new MyXUtilsCallBack() {
            LoginModel model;

            @Override
            public void success(String result) {
                model = JSON.parseObject(result, LoginModel.class);
                Constant.USER_ID = model.getYHID();
                LatLng latLng = new LatLng(Float.parseFloat(model.getLatitude()), Float.parseFloat(model.getLongitude()));
                Constant.LOCATION = latLng;
                Constant.USER_HEAD_IMG = HttpUrl.API_HOST + model.getYHTX();
            }

            @Override
            public void finished() {
                if (isSuccess()) {

                    Cfg.saveStr(WelcomeActivity.this, Constant.SH_USER_NC, model.getYHNC());
                    Cfg.saveStr(WelcomeActivity.this, Constant.SH_USER_ID, model.getYHID());
                    Cfg.saveBoolean(WelcomeActivity.this, Constant.SH_IS_LOGIN, true);
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    JPushInterface.setAlias(SuppliesApplication.getAppContext(), model.getYHID(), new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {
                            Logger.i("%d<--->%s", i, s);
                        }
                    });
                    startActivity(intent);
                    finish();
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
}
