package cn.soft_x.supplies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.maverick.utils.EditTextUtils;
import com.maverick.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.http.MyXUtilsCallBack;
import cn.soft_x.supplies.utils.Constant;

public class ForgetPwdActivity extends BaseActivity {

    @BindView(R.id.forget_pwd_ed_phone)
    EditText forgetPwdEdPhone;
    @BindView(R.id.forget_pwd_ed_ver)
    EditText forgetPwdEdVer;
    @BindView(R.id.forget_pwd_btn_get_ver)
    Button forgetPwdBtnGetVer;
    @BindView(R.id.forget_pwd_ll)
    LinearLayout forgetPwdLl;
    @BindView(R.id.forget_pwd_btn_sure)
    Button forgetPwdBtnSure;

    private CountDownTimer countDownTimer = new CountDownTimer(120000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (forgetPwdBtnGetVer != null) {
                forgetPwdBtnGetVer.setClickable(false);//防止重复点击
                forgetPwdBtnGetVer.setText(millisUntilFinished / 1000 + "s重发");
                forgetPwdBtnGetVer.setBackgroundResource(R.drawable.bg_yellow_btn_enable);
            }
        }

        @Override
        public void onFinish() {
            if (forgetPwdBtnGetVer != null) {
                forgetPwdBtnGetVer.setClickable(true);//防止重复点击
                forgetPwdBtnGetVer.setText(R.string.get_ver);
                forgetPwdBtnGetVer.setBackgroundResource(R.drawable.bg_yellow_btn);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ButterKnife.bind(this);
    }

    @Override
    protected void initBaseTitle() {
        setTitleText("找回密码");
    }

    @OnClick({R.id.title_bar_left, R.id.forget_pwd_btn_get_ver, R.id.forget_pwd_btn_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forget_pwd_btn_get_ver:
                if (EditTextUtils.isEmpty(forgetPwdEdPhone)) {
                    ToastUtil.showToast(this, "请输入手机号！");
                    return;
                } else {
                    phone = EditTextUtils.getEdText(forgetPwdEdPhone);
                    getVerCode();
                }
                break;
            case R.id.forget_pwd_btn_sure:
                if (EditTextUtils.isEmpty(forgetPwdEdVer)) {
                    ToastUtil.showToast(this, "请输入验证码！");
                    return;
                } else if (!EditTextUtils.getEdText(forgetPwdEdVer).equals(yzm)) {
                    ToastUtil.showToast(this, "验证码输入错误！");
                } else {
                    Intent intent = new Intent(this, ResetPwdActivity.class);
                    intent.putExtra(Constant.PHONE, phone);
                    intent.putExtra(Constant.VER,yzm);
                    startActivity(intent);
                }
                break;
            case R.id.title_bar_left:
                finish();
                break;
        }
    }

    private String yzm = "";
    private String phone = "";

    private void getVerCode() {
        countDownTimer.start();
        showProgressDialog("正在获取验证码...");
        RequestParams params = new RequestParams(HttpUrl.GET_WJMM_YZM);
        params.addBodyParameter("dhhm", EditTextUtils.getEdText(forgetPwdEdPhone));
        x.http().post(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {
                if(isSuccess()){
                    countDownTimer.start();
                }else{
                    Toast.makeText(ForgetPwdActivity.this,result,Toast.LENGTH_SHORT).show();
                }
                try {
                    JSONObject object = new JSONObject(result);
                    yzm = object.getInt("yzm")+"";
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void finished() {
                dismissProgressDialog();
            }
        });
    }
}
