package cn.soft_x.supplies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.utils.Constant;
@Deprecated
public class SelectIdentityActivity extends BaseActivity {

    @BindView(R.id.select_ll_transfer)
    LinearLayout selectLlTransfer;
    @BindView(R.id.select_ll_supplier)
    LinearLayout selectLlSupplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_identity);
        ButterKnife.bind(this);
    }

    @Override
    protected void initBaseTitle() {
        setTitleText(R.string.register);
    }

    @OnClick({R.id.title_bar_left, R.id.select_ll_transfer, R.id.select_ll_supplier})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.title_bar_left:
                finish();
                break;
            case R.id.select_ll_transfer:
                intent = new Intent(this,RegisterActivity.class);
                intent.putExtra(Constant.INTENT_IDENTITY,Constant.TRANSFER);
                startActivity(intent);
                break;
            case R.id.select_ll_supplier:
                intent = new Intent(this,RegisterActivity.class);
                intent.putExtra(Constant.INTENT_IDENTITY,Constant.SUPPLIER);
                startActivity(intent);
                break;
        }
    }
}
