package cn.soft_x.supplies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.model.LatLng;
import com.maverick.utils.Cfg;
import com.maverick.utils.EditTextUtils;
import com.maverick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.activity.nonelectrical.MainActivity2;
import cn.soft_x.supplies.application.AppManager;
import cn.soft_x.supplies.application.SuppliesApplication;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.http.MyXUtilsCallBack;
import cn.soft_x.supplies.model.LoginModel;
import cn.soft_x.supplies.model.RoleDetailModel;
import cn.soft_x.supplies.utils.Constant;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.login_phone)
    EditText loginPhone;
    @BindView(R.id.login_pwd)
    EditText loginPwd;
    @BindView(R.id.login_do_login)
    Button loginDoLogin;
    @BindView(R.id.login_forget_pwd)
    TextView loginForgetPwd;
    @BindView(R.id.login_register)
    TextView loginRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initIntent();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent.getIntExtra(Constant.AC_TYPE, -1) == 1010) {
            loginPhone.setText(intent.getStringExtra(Constant.PHONE));
            loginPwd.setText(intent.getStringExtra(Constant.PWD));
        }
    }

    @Override
    protected void initBaseTitle() {
        setTitleLeftVisibility(false);
        setTitleText(R.string.login);
    }

    @OnClick({R.id.login_do_login, R.id.login_forget_pwd, R.id.login_register, R.id.title_bar_left})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.login_do_login:
                if (EditTextUtils.isEmpty(loginPhone)) {
                    ToastUtil.showToast(this, "请输入手机号！");
                } else if (EditTextUtils.isEmpty(loginPwd)) {
                    ToastUtil.showToast(this, "请输入密码！");
                } else
                    doLogin();
                break;
            case R.id.login_forget_pwd:
                intent = new Intent(this, ForgetPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.login_register:
                intent = new Intent(this, BusinessTypeActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void doLogin() {
        //        {"loginid":"18522815693","password":"111111","shebei":"0","yhtype":1}
        //        {"loginid":"登录账号","password":"密码","shebei":"设备(0-andriod、1-ios)","yhtype":用户类型（0-调货商、1-供货商）}
        showProgressDialog("正在登陆...");
        Map<String,String> map = new HashMap<>();
        map.put("loginid",EditTextUtils.getEdText(loginPhone));
        map.put("password",EditTextUtils.getEdText(loginPwd));
        map.put("shebei","0");
        map.put("yhtype","1");
        String json = JSON.toJSONString(map);
        final String search = getParamsAESEncode(json);
        String signature = getParamsRSEEncode(json);
        RequestParams params = new RequestParams(HttpUrl.LOGIN);
        params.addBodyParameter("search", search);
        params.addBodyParameter("signature", signature);

        x.http().get(params, new MyXUtilsCallBack() {
            LoginModel model;

            @Override
            public void success(String result) {
                model = JSON.parseObject(result, LoginModel.class);
                Constant.USER_ID = model.getYHID();
                Constant.USER_NAME = model.getYHNC();
                Constant.HDDDCOUNT = model.getHdddcount();
                Constant.ROLE_ID = model.getRoleId();
                LatLng latLng = new LatLng(Float.parseFloat(model.getLatitude()),Float.parseFloat(model.getLongitude()));
                Constant.LOCATION = latLng;
                Constant.USER_HEAD_IMG = HttpUrl.API_HOST + model.getYHTX();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }

            @Override
            public void finished() {
                dismissProgressDialog();
                if (isSuccess()) {
                    Cfg.saveStr(LoginActivity.this,Constant.SH_USER_NC,model.getYHNC());
                    Cfg.saveStr(LoginActivity.this, Constant.SH_USER_ID, model.getYHID());
                    Cfg.saveStr(LoginActivity.this, Constant.HDDDCOUNT, model.getHdddcount());
                    Cfg.saveStr(LoginActivity.this, Constant.SH_USER_NAME, EditTextUtils.getEdText(loginPhone));
                    Cfg.saveStr(LoginActivity.this, Constant.SH_PWD, EditTextUtils.getEdText(loginPwd));
                    Cfg.saveStr(LoginActivity.this,Constant.SH_ROLE_ID,model.getRoleId());
                    Cfg.saveBoolean(LoginActivity.this, Constant.SH_IS_LOGIN, true);

                    getRoleDetail(model.getRoleId());

                    Set<String> set = new HashSet<String>();
                    String[] tags = model.getTags().split("-");
                    for (String tag : tags){
                        set.add(tag);
                    }
                    JPushInterface.init(getApplicationContext());
                    JPushInterface.resumePush(getApplicationContext());
                    JPushInterface.setAliasAndTags(SuppliesApplication.getAppContext(), model.getYHID(), set, new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {
                            Logger.i("%d<--->%s", i, s);
                        }
                    });

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
                    Cfg.saveStr(LoginActivity.this, Constant.SH_ROLE_YWTAGS, roleDetailModel.getYwtags());
                    Cfg.saveStr(LoginActivity.this, Constant.SH_ROLE_CODE, roleDetailModel.getRoleCode());
                    Cfg.saveStr(LoginActivity.this, Constant.SH_ROLE_NAME, roleDetailModel.getRoleName());
                    if("2".equals(roleDetailModel.getYwtags())){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
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

    @Override
    public void onBackPressed() {
        AppManager.getAppManager().AppExit(this);
    }
}
