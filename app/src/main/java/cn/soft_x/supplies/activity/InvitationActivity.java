package cn.soft_x.supplies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.adapter.InvitationAdapter;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.http.MyXUtilsCallBack;
import cn.soft_x.supplies.model.InvitationModel;
import cn.soft_x.supplies.utils.Constant;

public class InvitationActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate, AdapterView.OnItemClickListener {
    @BindView(R.id.invitation_listView)
    ListView invitationListView;
    @BindView(R.id.invitation_refresh)
    BGARefreshLayout invitationRefresh;

    private BGARefreshViewHolder mRefreshHolder;

    private List<InvitationModel.YqlistBean> mData = new ArrayList<>();

    private InvitationAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation);
        ButterKnife.bind(this);
        initListView();
        initData();
    }

    private void initData() {
        RequestParams params = new RequestParams(HttpUrl.YQH_1);
        params.addBodyParameter("appyhid", Constant.USER_ID);
        x.http().post(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {
                InvitationModel model = JSON.parseObject(result, InvitationModel.class);
                if (mData.size()>0){
                    mData.clear();
                }
                mData.addAll(model.getYqlist());
            }

            @Override
            public void finished() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initListView() {
        invitationRefresh.setIsShowLoadingMoreView(true);
        mRefreshHolder = new BGANormalRefreshViewHolder(this, true);
        invitationRefresh.setRefreshViewHolder(mRefreshHolder);
        invitationRefresh.setDelegate(this);
        mAdapter = new InvitationAdapter(mData);
        invitationListView.setAdapter(mAdapter);
        invitationListView.setOnItemClickListener(this);
    }

    @Override
    protected void initBaseTitle() {
        setTitleText("邀请函");
    }

    @OnClick({R.id.title_bar_left})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_left:
                finish();
                break;
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        initData();
        refreshLayout.endRefreshing();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        refreshLayout.endLoadingMore();
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mData.size()==0){
            return;
        }
        Intent intent = new Intent(this,InvitationDetailsActivity.class);
        intent.putExtra("yqlx",mData.get(position).getYQLX());
        startActivity(intent);
    }
}
