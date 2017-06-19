package cn.soft_x.supplies.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.adapter.CommonAdapter;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.http.MyXUtilsCallBack;
import cn.soft_x.supplies.model.CommonModel;

public class CommonProblemActivity extends BaseActivity {

    @BindView(R.id.common_problem_recyclerview)
    RecyclerView mRecyclerview;

    private List<CommonModel.FaqlistsBean> mData = new ArrayList<>();
    private CommonAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_problem);
        ButterKnife.bind(this);
        initRecyclerView();
        initData();

    }

    private void initData() {
        showProgressDialog("正在加载数据...");
        RequestParams params = new RequestParams(HttpUrl.CJWT);
        params.addBodyParameter("currentPage", 1 + "");
        x.http().post(params, new MyXUtilsCallBack() {
            CommonModel model;
            @Override
            public void success(String result) {
                model = JSON.parseObject(result,CommonModel.class);
            }

            @Override
            public void finished() {
                dismissProgressDialog();
                mData.addAll(model.getFaqlists());
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerview.setLayoutManager(layoutManager);
        mAdapter = new CommonAdapter(mData);
        mRecyclerview.setAdapter(mAdapter);
    }

    @Override
    protected void initBaseTitle() {
        setTitleText(R.string.common_problem);
    }

    @OnClick(R.id.title_bar_left)
    public void onClick(){
        finish();
    }
}
