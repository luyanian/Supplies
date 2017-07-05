package cn.soft_x.supplies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.facebook.imagepipeline.producers.Consumer;
import com.maverick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.adapter.RolesListAdapter;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.http.MyXUtilsCallBack;
import cn.soft_x.supplies.model.MessageDetailModel;
import cn.soft_x.supplies.model.RoleModel;
import cn.soft_x.supplies.utils.Constant;

/**
 * Created by ryon on 2017/6/5.
 */

public class BusinessTypeActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    @BindView(R.id.grid)
    GridView grid;

    private List<RoleModel.DataBean> mList = new ArrayList<>();
    private RolesListAdapter rolesListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_type);
        ButterKnife.bind(this);

        rolesListAdapter = new RolesListAdapter(this,mList);
        grid.setAdapter(rolesListAdapter);
        grid.setOnItemClickListener(this);
        getNetDate();
    }

    @Override
    protected void initBaseTitle() {
        setTitleText("注册");
        setTitleLeftVisibility(true);
    }

    private void getNetDate() {
        showProgressDialog("正在加载数据...");
        RequestParams params = new RequestParams(HttpUrl.ROLESLIST);
        x.http().get(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {

            }

            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                RoleModel bean = JSON.parseObject(result, RoleModel.class);
                Logger.json(result);
                if(isSuccess()) {
                    initDate(bean.getData());
                }else{
                    Toast.makeText(BusinessTypeActivity.this,bean.resultInfo,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void finished() {
                dismissProgressDialog();
            }
        });
    }

    private void initDate(List<RoleModel.DataBean> data) {
        mList.clear();
        mList.addAll(data);
        rolesListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RoleModel.DataBean dataBean = (RoleModel.DataBean) ((RolesListAdapter)parent.getAdapter()).getItem(position);
        if(dataBean!=null) {
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra("roleId", dataBean.getRoleId());
            startActivity(intent);
        }
    }
    @OnClick({R.id.title_bar_left})
    public void onClick(View view) {
      finish();
    }

}
