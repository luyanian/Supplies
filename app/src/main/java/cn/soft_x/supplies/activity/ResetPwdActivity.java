package cn.soft_x.supplies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.maverick.utils.EditTextUtils;
import com.maverick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.soft_x.supplies.R;
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
        //        {"dhhm":"18522815693","yhtype":"1","password":"12368975","yzm":"2447"}
        RequestParams params = new RequestParams(HttpUrl.WJMM);
        params.addBodyParameter("dhhm", phone);
        params.addBodyParameter("yhtype", 1 + "");
        params.addBodyParameter("password", EditTextUtils.getEdText(resetPwdEd2));
        params.addBodyParameter("yzm", ver);
        showProgressDialog("请稍候...");
        x.http().post(params, new MyXUtilsCallBack() {
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
