package cn.soft_x.supplies.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
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
import cn.soft_x.supplies.adapter.TruckAdapter;
import cn.soft_x.supplies.application.AppManager;
import cn.soft_x.supplies.application.SuppliesApplication;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.http.MyXUtilsCallBack;
import cn.soft_x.supplies.model.TruckItemModel;
import cn.soft_x.supplies.model.TruckModel;
import cn.soft_x.supplies.utils.Constant;

public class TruckActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @BindView(R.id.truck_listview)
    ListView truckListview;
    @BindView(R.id.truck_refresh)
    BGARefreshLayout truckRefresh;
    @BindView(R.id.truck_order)
    Button truckOrder;

    private List<TruckModel.DataBean> mData = new ArrayList<>();
    private TruckAdapter mAdapter;

    private BGARefreshViewHolder mRefreshHolder;

    private int mPage = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData(1);
    }

    private void initData(final int page) {
        //        {"appyhid":"12345678","currentPage":"1","numPerPage":"2"}
        //        showProgressDialog("正在加载...");
        // 在货车生成订单时，如果是拾起卖模式则需要选择销售人员，选择预计到达时间。
        // （目前的情况）如果是调货商、拆解企业模式则只选择预计到达时间即可，不需要选择销售人员
        RequestParams params = new RequestParams(HttpUrl.HC);
        params.addBodyParameter("appyhid", Constant.USER_ID);
        params.addBodyParameter("currentPage", page + "");
        params.addBodyParameter("numPerPage", 5 + "");
        x.http().post(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {
                TruckModel bean = JSON.parseObject(result, TruckModel.class);
                if (page == 1) {
                    if (mData.size() > 0)
                        mData.clear();
                    mData.addAll(0, bean.getData());
                } else {
                    mData.addAll(bean.getData());
                }
            }

            @Override
            public void finished() {
                dismissProgressDialog();
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        truckRefresh.setIsShowLoadingMoreView(true);
        mRefreshHolder = new BGANormalRefreshViewHolder(this, true);
        truckRefresh.setRefreshViewHolder(mRefreshHolder);
        truckRefresh.setDelegate(this);
        mAdapter = new TruckAdapter(this, mData);
        truckListview.setAdapter(mAdapter);
    }

    @Override
    protected void initBaseTitle() {
        setTitleRightText("删除");
        setTitleText("货车");
    }


    @OnClick({R.id.truck_order, R.id.title_bar_left, R.id.title_bar_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar_right:
                if (isOK(1)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this)
                            .setTitle("提示").setMessage("确定要删除？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteInfo();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            case R.id.title_bar_left:
                finish();
                break;
            case R.id.truck_order:
                if (isOK(1)) {
                    Intent intent = new Intent(this, GenerateOrderActivity.class);
                    ArrayList<TruckItemModel> lists = new ArrayList<>();
                    int jyms = -1;
                    for (TruckModel.DataBean dataBean : mData) {
                        for (TruckItemModel model :
                                dataBean.getList()) {
                            if (model.isChecked()) {
                                lists.add(model);
                                jyms = dataBean.getJyms();
                            }
                        }
                    }
                    Bundle b = new Bundle();
                    b.putSerializable("model", lists);
                    b.putString("id", id);
                    b.putInt("jyms", jyms);
                    intent.putExtra("bundle", b);
                    startActivity(intent);
                }
                break;
        }
    }

    private void deleteInfo() {
        ArrayList<TruckItemModel> uploadList = new ArrayList<>();
        for (int i = mData.size(); i > 0; i--) {
            TruckModel.DataBean dataBean = mData.get(i - 1);
            for (int j = dataBean.getList().size(); j > 0; j--) {
                TruckItemModel model = dataBean.getList().get(j - 1);
                if (model.isChecked()) {
                    uploadList.add(model);
                    //                    dataBean.getList().remove(j - 1);
                }
            }
//            if (dataBean.getList().size() == 0) {
//                                mData.remove(i - 1);
//            }
        }
        Logger.i("mdata的长度->%d，要删的数据->%d", mData.size(), uploadList.size());
        showProgressDialog("正在删除...");
        RequestParams params = new RequestParams(HttpUrl.DELETE_HUOCHE);
        params.addBodyParameter("sdid", id);
        params.addBodyParameter("gwcidpj", pjId(uploadList));
        x.http().post(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {

            }

            @Override
            public void finished() {
                dismissProgressDialog();
                if (isSuccess()){
                    initData(1);
                }
            }
        });
    }

    private String pjId(ArrayList<TruckItemModel> uploadList) {
        String gwcidpj = "";
        for (int i = 0; i < uploadList.size(); i++) {
            if (i != uploadList.size() - 1) {
                gwcidpj += uploadList.get(i).getGwcid() + "|";
            } else
                gwcidpj += uploadList.get(i).getGwcid();
        }
        return gwcidpj;
    }

    private String id = "";

    private boolean isOK(int type) {
        int res1 = 0;
        int res2 = 0;
        for (TruckModel.DataBean dataBean : mData) {
            if (dataBean.isChecked()) {
                res1++;
            }
            for (TruckItemModel model :
                    dataBean.getList()) {
                if (model.isChecked()) {
                    id = dataBean.getId();
                    res2++;
                }
            }
        }
        Logger.i("res1->%d,res2->%d", res1, res2);
        if (res1 == 0) {
            if (res2 != 0) {
                return true;
            }
            ToastUtil.showToast(this, "请选择一条订单！");
            return false;
        } else if (res1 > 1) {
            if (type == 1) {
                ToastUtil.showToast(this, "只能选择同一类订单！");
                return false;
            }
        }
        return true;
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
}
