package cn.soft_x.supplies.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.maverick.utils.Cfg;
import com.maverick.utils.EditTextUtils;
import com.maverick.utils.ToastUtil;

import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.application.AppManager;
import cn.soft_x.supplies.application.SuppliesApplication;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.http.MyXUtilsCallBack;
import cn.soft_x.supplies.utils.Constant;

public class ChangePhoneActivity extends BaseActivity {

    @BindView(R.id.change_phone_ed)
    EditText changePhoneEd;
    @BindView(R.id.change_phone_ed_ver)
    EditText changePhoneEdVer;
    @BindView(R.id.change_phone_btn_get_ver)
    Button changePhoneBtnGetVer;
    @BindView(R.id.change_phone_btn)
    Button changePhoneBtn;

    private CountDownTimer timer = new CountDownTimer(120000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (changePhoneBtnGetVer != null) {
                changePhoneBtnGetVer.setClickable(false);//防止重复点击
                changePhoneBtnGetVer.setText(millisUntilFinished / 1000 + "s重发");
                changePhoneBtnGetVer.setBackgroundResource(R.drawable.bg_yellow_btn_enable);
            }
        }

        @Override
        public void onFinish() {
            if (changePhoneBtnGetVer != null) {
                changePhoneBtnGetVer.setClickable(true);//防止重复点击
                changePhoneBtnGetVer.setText(R.string.get_ver);
                changePhoneBtnGetVer.setBackgroundResource(R.drawable.bg_yellow_btn);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
    }

    @Override
    protected void initBaseTitle() {
        setTitleText(R.string.change_phone);
    }

    @OnClick({R.id.change_phone_btn_get_ver, R.id.change_phone_btn,R.id.title_bar_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_phone_btn_get_ver:
                if (EditTextUtils.isEmpty(changePhoneEd)) {
                    ToastUtil.showToast(this, "请输入手机号！");
                } else {
                    timer.start();
                    showProgressDialog("正在获取验证码...");
                    RequestParams params = new RequestParams(HttpUrl.GET_YZM);
                    params.addBodyParameter("dhhm", EditTextUtils.getEdText(changePhoneEd));
                    x.http().post(params, new MyXUtilsCallBack() {
                        @Override
                        public void success(String result) {

                        }

                        @Override
                        public void finished() {
                            dismissProgressDialog();
                        }
                    });
                }
                break;
            case R.id.change_phone_btn:
                showProgressDialog("正在更新信息...");
                if (EditTextUtils.isEmpty(changePhoneEdVer)) {
                    ToastUtil.showToast(this, "请输入验证码！");
                    return;
                }
                RequestParams params = new RequestParams(HttpUrl.GGSJ);
                params.addBodyParameter("dhhm", EditTextUtils.getEdText(changePhoneEd));
                params.addBodyParameter("appyhid",
                        TextUtils.isEmpty(Constant.USER_ID) ?
                                Cfg.loadStr(this, Constant.SH_USER_ID) : Constant.USER_ID);
                params.addBodyParameter("yzm", EditTextUtils.getEdText(changePhoneEdVer));
                x.http().get(params, new MyXUtilsCallBack() {
                    @Override
                    public void success(String result) {

                    }

                    @Override
                    public void finished() {
                        dismissProgressDialog();
                        if (isSuccess()) {
                            ToastUtil.showToast(ChangePhoneActivity.this, "手机号码修改成功！");
                        }
                    }
                });
                break;
            case R.id.title_bar_left:
                finish();
                break;
        }
    }
}
