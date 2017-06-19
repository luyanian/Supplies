package cn.soft_x.supplies.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.soft_x.supplies.R;

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.about_us_rl_1)
    RelativeLayout aboutUsRl1;
    @BindView(R.id.about_us_rl_2)
    RelativeLayout aboutUsRl2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
    }

    @Override
    protected void initBaseTitle() {
        setTitleText(R.string.about_us);
    }

    @OnClick({R.id.about_us_rl_1, R.id.about_us_rl_2,R.id.title_bar_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.about_us_rl_1:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse("http://www.citymine.com.cn");
                intent.setData(content_url);
                startActivity(intent);
                break;
            case R.id.about_us_rl_2:
                break;
            case R.id.title_bar_left:
                finish();
                break;
        }
    }
}
