package cn.soft_x.supplies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.maverick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.adapter.MessageDetailsAdapter;
import cn.soft_x.supplies.db.MyDBControl;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.model.MessageDetailModel;
import cn.soft_x.supplies.utils.Constant;

public class MessageDetailsActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate, AdapterView.OnItemClickListener {

    @BindView(R.id.msg_details_listView)
    ListView msgDetailsListView;
    @BindView(R.id.msg_details_refreshLayout)
    BGARefreshLayout msgDetailsRefreshLayout;

    private BGARefreshViewHolder mRefreshViewHolder;

    private MyDBControl mControl;

    private MessageDetailsAdapter mAdapter;
    private List<MessageDetailModel.ListBean> mData = new ArrayList<>();

    private int mPage = 1;

    private int xxdl;
    private int xxlx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        mControl = new MyDBControl(this);
        ButterKnife.bind(this);
        initIntent();
        initRefreshLyout();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData(0);
    }

    @OnClick({R.id.title_bar_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_left:
                finish();
                break;
        }
    }

    private void initIntent() {
        Intent intent = getIntent();
        xxdl = intent.getIntExtra("xxdl", -1);
        xxlx = intent.getIntExtra("xxlx", -1);
        Logger.i("xxdl->%d,xxlx->%d", xxdl, xxlx);
    }

    private void initRefreshLyout() {
        msgDetailsRefreshLayout.setIsShowLoadingMoreView(true);
        mRefreshViewHolder = new BGANormalRefreshViewHolder(this, true);
        msgDetailsRefreshLayout.setRefreshViewHolder(mRefreshViewHolder);
        msgDetailsRefreshLayout.setDelegate(this);
        mAdapter = new MessageDetailsAdapter(mData);
        msgDetailsListView.setAdapter(mAdapter);
        msgDetailsListView.setOnItemClickListener(this);

    }

    private void initData(final int page) {
        showProgressDialog("正在加载...");
        if (xxdl == -1 || xxlx == -1) {
            ToastUtil.showToast(this, "数据初始化失败！");
            dismissProgressDialog();
            return;
        }
        //        {"xxdl":"消息大类 (1:采购消息，2:订单消息，3:系统消息)",
        // "appyhid":"用户ID","xxlx":"消息类型"}
        //{"xxdl":"2","appyhid":"049ee74567e5488abfda6422785dac42",
        // "xxlx":"3","currentPage":"1","numPerPage":"5"}
        RequestParams params = new RequestParams(HttpUrl.MSG_2);
        params.addBodyParameter("xxdl", "" + xxdl);
        params.addBodyParameter("xxlx", "" + xxlx);
        params.addBodyParameter("appyhid", Constant.USER_ID);
        //        params.addBodyParameter("currentPage", page + "");
        //        params.addBodyParameter("numPerPage", 10 + "");
        //        cxsj
        long time = mControl.searchMsgTime(xxdl, xxlx);
        Logger.e("申请接口的time->"+time);
        if (time == -1) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            try {
                date = format.parse("2016-01-01");
                params.addBodyParameter("cxsj", date.getTime() + "");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            params.addBodyParameter("cxsj", time + "");
        }
        //        new MyXUtilsCallBack(false) {
        //            @Override
        //            public void success(String result) {
        //                MessageDetailModel bean = JSON.parseObject(result, MessageDetailModel.class);
        //                if (page == 1) {
        //                    if (mData.size() > 0)
        //                        mData.clear();
        //                    mData.addAll(0, bean.getList());
        //                } else {
        //                    mData.addAll(bean.getList());
        //                }
        //                mControl.addMsgDetailTable(bean);
        //                mControl.selectMsgDetailTable(xxdl + "", xxlx + "", 0);
        //            }
        //
        //            @Override
        //            public void finished() {
        //                dismissProgressDialog();
        //                mAdapter.notifyDataSetChanged();
        //            }
        //        };
        x.http().post(params, new Callback.CommonCallback<String>() {
            MessageDetailModel bean;

            @Override
            public void onSuccess(String result) {
                bean = JSON.parseObject(result, MessageDetailModel.class);
                Logger.json(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                if (bean.resultCode.equals("0")) {
                    mControl.addMsgDetailTable(bean);
                }
                if (page == 0) {
                    if (mData.size() > 0)
                        mData.clear();
                    mData.addAll(0, mControl.selectMsgDetailTable(xxdl + "", xxlx + "", 0));
                } else {
                    mData.addAll(mControl.selectMsgDetailTable(xxdl + "", xxlx + "", page));
                }
                dismissProgressDialog();
                mAdapter.notifyDataSetChanged();
            }
        });


    }


    @Override
    protected void initBaseTitle() {
        setTitleText("消息");
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mPage = 1;
        initData(0);
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
//        mControl.searchForReadTest(mData.get(position).getGLID(), mData.get(position).getXXDL(), mData.get(position).getXXLX(),"前");
        mControl.upDateForRead(mData.get(position).getGLID(), mData.get(position).getXXDL(), mData.get(position).getXXLX());
//        mControl.searchForReadTest(mData.get(position).getGLID(), mData.get(position).getXXDL(), mData.get(position).getXXLX(),"后");
        mData.get(position).setRead(1);
        Intent intent = new Intent(this, WebViewActivity.class);
        /**
         * 消息类型 (
         * 1:采购信息 [
         *      1:拾起卖消息，2:调货商，3:拆解企业，4:供货商消息]
         * 2:订单消息[
         *      1.供货消息，2.修改预计到厂时间3.到厂时间，4.验质消息，5结算消息，6,异常消息,
         *      7,发车消息,8发布供货消息]
         * 3:系统消息[
         *      1，修改密码，2修改手机号]
         * )
         */
        switch (mData.get(position).getXXDL()) {
            case 1:
                switch (mData.get(position).getXXLX()) {
                    case 1:
                        intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/caigou-xq2.html");
                        break;
                    case 2:
                        intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/caigou-dhsxq.html");
                        break;
                    case 3:
                        intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/caigou-xq.html");
                        break;
                    case 4:
                        break;
                }
                break;
            case 2:
                switch (mData.get(position).getXXLX()) {
                    case 1:
                        intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/fache-xq.html");
                        break;
                    case 3:
                        intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/dingdan-xq.html");
                        break;
                    case 4:
                        intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/dingdan-wcddxq.html");
                        break;
                    case 5:
                        intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/dingdan-wcddxq.html");
                        break;
                    default:
                        intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/dingdan-xq.html");
                        break;
                }
                break;
        }
        Logger.i("click to String ->" + mData.get(position).toString());
        Logger.i("MessageDetail->onClick!!!");
        Logger.i("MessageDetail->xxdl->%d,xxlx->%d", mData.get(position).getXXDL(), mData.get(position).getXXLX());
        intent.putExtra(Constant.WEB_HUNHEID, mData.get(position).getGLCOMPANYID());
        intent.putExtra(Constant.WEB_CGID, mData.get(position).getGLID());
        intent.putExtra(Constant.WEB_GLID, mData.get(position).getGLID());
        startActivity(intent);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mControl.close();
    }
}
