package cn.soft_x.supplies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.maverick.utils.EditTextUtils;
import com.maverick.utils.ToastUtil;

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

public class ChangePwdActivity extends BaseActivity {

    @BindView(R.id.change_pwd_ed_old)
    EditText changePwdEdOld;
    @BindView(R.id.change_pwd_pwd_ed_1)
    EditText changePwdPwdEd1;
    @BindView(R.id.change_pwd_pwd_ed_2)
    EditText changePwdPwdEd2;
    @BindView(R.id.change_pwd_pwd_btn)
    Button changePwdPwdBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
    }

    @Override
    protected void initBaseTitle() {
        setTitleText("修改密码");
    }

    @OnClick({R.id.change_pwd_pwd_btn,R.id.title_bar_left})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.change_pwd_pwd_btn:
                if (EditTextUtils.isEmpty(changePwdEdOld)) {
                    ToastUtil.showToast(this, "请输入原密码！");
                    return;
                } else if (EditTextUtils.isEmpty(changePwdPwdEd1)) {
                    ToastUtil.showToast(this, "请输入新密码！");
                    return;
                } else if (EditTextUtils.isEmpty(changePwdPwdEd2)) {
                    ToastUtil.showToast(this, "请再次输入新密码！");
                    return;
                } else if (!EditTextUtils.getEdText(changePwdPwdEd1).equals(EditTextUtils.getEdText(changePwdPwdEd2))) {
                    ToastUtil.showToast(this, "两次输入新密码不一致！");
                    return;
                }

                Map<String,String> map = new HashMap<>();
                map.put("ymm", EditTextUtils.getEdText(changePwdEdOld));
                map.put("xmm", EditTextUtils.getEdText(changePwdPwdEd2));
                map.put("userid", Constant.USER_ID);
                String json = JSON.toJSONString(map);
                String search = getParamsAESEncode(json);
                String signature = getParamsRSEEncode(json);

                RequestParams params = new RequestParams(HttpUrl.XGMM);
                params.addBodyParameter("search", search);
                params.addBodyParameter("signature", signature);

                x.http().post(params, new MyXUtilsCallBack() {
                    @Override
                    public void success(String result) {

                    }

                    @Override
                    public void finished() {
                        if (isSuccess()) {
                            ToastUtil.showToast(ChangePwdActivity.this, "密码修改成功！");
                            Intent intent = new Intent(ChangePwdActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
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
