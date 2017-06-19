package cn.soft_x.supplies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.maverick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

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
import cn.soft_x.supplies.adapter.InvitationDetailsAdapter;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.http.MyXUtilsCallBack;
import cn.soft_x.supplies.model.InvitationDetailsModel;
import cn.soft_x.supplies.utils.Constant;

public class InvitationDetailsActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate, AdapterView.OnItemClickListener {

    @BindView(R.id.invitation_details_listView)
    ListView invitationDetailsListView;
    @BindView(R.id.invitation_details_refresh)
    BGARefreshLayout invitationDetailsRefresh;

    private BGARefreshViewHolder viewHolder;
    private List<InvitationDetailsModel.YqlistBean> mData = new ArrayList<>();
    private InvitationDetailsAdapter mAdapter;

    private int mPage = 2;
    private int yqlx = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitation_details);
        ButterKnife.bind(this);
        initIntent();
        initListView();
        initData(1);
    }

    private void initIntent() {
        Intent intent = getIntent();
        yqlx = intent.getIntExtra("yqlx", -1);
        if (yqlx == -1) {
            ToastUtil.showToast(this, "发生未知错误！");
        }
    }

    private void initData(final int page) {
        showProgressDialog("正在加载...");
        //        {"appyhid":"049ee74567e5488abfda6422785dac42",
        // "yqlx":"0","currentPage":"1","numPerPage":"5"}
        RequestParams params = new RequestParams(HttpUrl.YQH_2);
        params.addBodyParameter("appyhid", Constant.USER_ID);
        params.addBodyParameter("yqlx", yqlx + "");
        params.addBodyParameter("currentPage", page + "");
        params.addBodyParameter("numPerPage", "15");
        x.http().post(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {
                InvitationDetailsModel bean = JSON.parseObject(result, InvitationDetailsModel.class);
                if (page == 1) {
                    if (mData.size() > 0)
                        mData.clear();
                    mData.addAll(0, bean.getYqlist());
                } else {
                    mData.addAll(bean.getYqlist());
                }
            }

            @Override
            public void finished() {
                dismissProgressDialog();
                if (isSuccess())
                    mAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.title_bar_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_left:
                finish();
                break;
        }
    }

    private void initListView() {
        invitationDetailsRefresh.setIsShowLoadingMoreView(true);
        viewHolder = new BGANormalRefreshViewHolder(this, true);
        invitationDetailsRefresh.setRefreshViewHolder(viewHolder);
        invitationDetailsRefresh.setDelegate(this);
        mAdapter = new InvitationDetailsAdapter(mData, yqlx);
        invitationDetailsListView.setAdapter(mAdapter);
        invitationDetailsListView.setOnItemClickListener(this);
    }

    @Override
    protected void initBaseTitle() {
        setTitleText("消息");
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mPage = 2;
        initData(1);
        refreshLayout.endRefreshing();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        initData(mPage);
        mPage++;
        dismissProgressDialog();
        refreshLayout.endLoadingMore();
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, WebViewActivity.class);
//        （0-拆解企业、1-拾起卖、2-调货商）
        switch (yqlx) {
            case 0:
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/caigou-xq.html");
                break;
            case 1:
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/caigou-xq2.html");
                break;
            case 2:
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/caigou-dhsxq.html");
                break;
            default:
                break;
        }
        intent.putExtra(Constant.WEB_CGID, mData.get(position).getCGID());
        intent.putExtra(Constant.WEB_HUNHEID,mData.get(position).getCompanyid());
        Logger.i(mData.get(position).getCompanyid()+"<-companyID");
        startActivity(intent);
    }
}
