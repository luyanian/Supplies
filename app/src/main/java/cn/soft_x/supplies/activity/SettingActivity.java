package cn.soft_x.supplies.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.maverick.utils.Cfg;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.utils.Constant;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.change_phone)
    TextView changePhone;
    @BindView(R.id.change_pwd)
    TextView changePwd;
    @BindView(R.id.common_problem)
    TextView changjianwenti;
    @BindView(R.id.about_us)
    TextView aboutUs;
    @BindView(R.id.setting_logout)
    Button settingLogout;
    @BindView(R.id.setting_version)
    TextView settingVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        settingVersion.setText("版本号:" + getVersionName());
    }

    private String getVersionName() {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;

        return version;
    }

    @Override
    protected void initBaseTitle() {
        setTitleText("设置");
    }

    @OnClick({R.id.title_bar_left,R.id.change_phone, R.id.change_pwd, R.id.common_problem, R.id.about_us, R.id.setting_logout})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.title_bar_left:
                finish();
                break;
            case R.id.change_phone:
                intent = new Intent(this, ChangePhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.change_pwd:
                intent = new Intent(this, ChangePwdActivity.class);
                startActivity(intent);
                break;
            case R.id.common_problem:
                intent = new Intent(this, CommonProblemActivity.class);
                startActivity(intent);
                break;
            case R.id.about_us:
                intent = new Intent(this, AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.setting_logout:
                doLogout();
                break;
        }
    }

    private void doLogout() {
        Intent intent = new Intent(this, LoginActivity.class);
        Cfg.saveBoolean(this, Constant.SH_IS_LOGIN, false);
        startActivity(intent);
        finish();
    }
}
