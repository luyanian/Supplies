package cn.soft_x.supplies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.maverick.utils.EditTextUtils;
import com.maverick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.application.AppManager;
import cn.soft_x.supplies.application.SuppliesApplication;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.http.MyXUtilsCallBack;
import cn.soft_x.supplies.utils.Constant;

public class ResetPwdActivity extends BaseActivity {

    @BindView(R.id.reset_pwd_ed_1)
    EditText resetPwdEd1;
    @BindView(R.id.reset_pwd_ed_2)
    EditText resetPwdEd2;
    @BindView(R.id.reset_pwd_btn)
    Button resetPwdBtn;
    private String phone = "";
    private String ver = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initIntent();
    }

    private void initIntent() {
        Intent intent = getIntent();
        phone = intent.getStringExtra(Constant.PHONE);
        ver = intent.getStringExtra(Constant.VER);
        Logger.i("%s<-手机号,%s<-验证码", phone, ver);
    }

    @Override
    protected void initBaseTitle() {
        setTitleText("重置密码");
    }

    @OnClick({R.id.title_bar_left, R.id.reset_pwd_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset_pwd_btn:
                if (EditTextUtils.isEmpty(resetPwdEd1)) {
                    ToastUtil.showToast(this, "请填写密码！");
                } else if (EditTextUtils.isEmpty(resetPwdEd2)) {
                    ToastUtil.showToast(this, "请填写重复的密码");
                } else if (!EditTextUtils.getEdText(resetPwdEd1).equals(EditTextUtils.getEdText(resetPwdEd2))) {
                    ToastUtil.showToast(this, "两次密码输入不一致！");
                } else
                    doResetPwd();
                break;
            case R.id.title_bar_left:
                finish();
                break;
        }
    }

    private void doResetPwd() {
        Map<String, String> map = new HashMap<>();
        map.put("dhhm", phone);
        map.put("yhtype", "1");
        map.put("password", EditTextUtils.getEdText(resetPwdEd2));
        map.put("yzm", ver);

        String json = JSON.toJSONString(map);
        String search = getParamsAESEncode(json);
        String signature = getParamsRSEEncode(json);

        RequestParams params = new RequestParams(HttpUrl.WJMM);
        params.addBodyParameter("search", search);
        params.addBodyParameter("signature", signature);

        showProgressDialog("请稍候...");
        x.http().get(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {

            }

            @Override
            public void finished() {
                dismissProgressDialog();
                if (isSuccess()) {
                    Intent intent = new Intent(ResetPwdActivity.this, LoginActivity.class);
                    intent.putExtra(Constant.AC_TYPE, 1010);
                    intent.putExtra(Constant.PHONE, phone);
                    intent.putExtra(Constant.PWD, EditTextUtils.getEdText(resetPwdEd2));
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
