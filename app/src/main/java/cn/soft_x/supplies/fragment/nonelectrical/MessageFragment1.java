package cn.soft_x.supplies.fragment.nonelectrical;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;
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
import butterknife.Unbinder;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.jpush.android.api.JPushInterface;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.activity.BaseActivity;
import cn.soft_x.supplies.activity.WebViewActivity;
import cn.soft_x.supplies.activity.nonelectrical.MainActivity2;
import cn.soft_x.supplies.adapter.nonelectrical.MessageAdapter1;
import cn.soft_x.supplies.db.MyDBControl;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.http.MyXUtilsCallBack;
import cn.soft_x.supplies.model.nonelectrical.HQMsgModel;
import cn.soft_x.supplies.model.nonelectrical.MessageModel1;
import cn.soft_x.supplies.utils.Constant;
import cn.soft_x.supplies.view.MessageView;

/**
 * Created by Administrator on 2016-11-02.
 */
public class MessageFragment1 extends BaseFragment1 implements BGARefreshLayout.BGARefreshLayoutDelegate, AdapterView.OnItemClickListener {
    @BindView(R.id.dd_msg)
    MessageView ddMsg;
    @BindView(R.id.cg_msg)
    MessageView cgMsg;
    @BindView(R.id.system_msg)
    MessageView systemMsg;
    @BindView(R.id.fragment_msg_ll)
    LinearLayout fragmentMsgLl;
    @BindView(R.id.fragment_refreshLayout)
    BGARefreshLayout fragmentRefreshLayout;
    @BindView(R.id.fragment_msg_listView)
    ListView fragmentMsgListView;
    Unbinder unbinder;

    private BGANormalRefreshViewHolder mRefreshHolder;

    private MessageView[] messageViews;
    private List<MessageModel1.ListBean> mData = new ArrayList<>();
    private MessageAdapter1 mAdapter;

    private boolean isFirst = true;
    private int msgType = 1;
    MyDBControl mControl;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(mContext, R.layout.fragment_message, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    protected void initView() {
        mControl = new MyDBControl(mContext);
        initRefreshLayout();
        messageViews = new MessageView[]{ddMsg, cgMsg, systemMsg};
        ddMsg.setMsgTv("订单消息");
        cgMsg.setMsgTv("行情消息");
        systemMsg.setMsgTv("系统消息");
        initData();
    }

    protected void initData() {
        if (!isFirst) {
            clickMsgView(msgType - 1);
        }
        isFirst = false;
        initReadDot();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            initData();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            initData();
        }
    }

    public boolean onBack() {
        return false;
    }

    @OnClick({R.id.dd_msg, R.id.cg_msg, R.id.system_msg})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dd_msg:
                clickMsgView(0);
                Logger.i("click00");
                break;
            case R.id.cg_msg:
                clickMsgView(1);
                Logger.i("click11");
                break;
            case R.id.system_msg:
                clickMsgView(2);
                Logger.i("click22");
                break;
        }
    }

    private void initRefreshLayout() {
        fragmentRefreshLayout.setIsShowLoadingMoreView(true);
        mRefreshHolder = new BGANormalRefreshViewHolder(getActivity(), true);
        fragmentRefreshLayout.setRefreshViewHolder(mRefreshHolder);
        fragmentRefreshLayout.setDelegate(this);
        mAdapter = new MessageAdapter1(mData);
        fragmentMsgListView.setAdapter(mAdapter);
        fragmentMsgListView.setOnItemClickListener(this);
    }

    private void clickMsgView(int index) {
        if (messageViews[index].isDotVisibility()) {
            messageViews[index].setDotVisibility(false);
        }
        setTabMsg(messageViews[index]);
        if (messageViews[index].isDotVisibility()) {
            messageViews[index].setDotVisibility(false);
        }
        setTabMsg(messageViews[index]);
        switch (index) {
            case 0:
                // 2016-12-05 订单消息
                getMsgData(1);
                msgType = 1;
                break;
            case 1:
                // 2016-12-05 采购消息
                getMsgData(2);
                msgType = 2;
                break;
            case 2:
                getMsgData(3);
                msgType = 3;
                // 2016-12-05 系统消息
                break;
        }
    }

    private boolean isConn = false;

    private void getMsgData(final int xxdl) {
        if(mContext==null){
            return;
        }
        if (!isHidden()) {
            mContext.showProgressDialog("正在加载...");
        }
        if (mData.size() > 0) {
            mData.clear();
        }
        if (isConn) {
            return;
        }
        isConn = true;
        RequestParams params = new RequestParams(HttpUrl.MSG_GH);
        params.addBodyParameter("xxdl", xxdl + "");
//        params.addBodyParameter("appyhid", Constant.USER_ID);
        params.addBodyParameter("curnum", "1");

        long time = mControl.searchGhsMsgTime(xxdl);
        if (time == -1) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            try {
                date = format.parse("2016-01-01");
                params.addBodyParameter("time", date.getTime() + "");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            params.addBodyParameter("time", time + "");
        }
        Logger.i("外层消息->time" + time);

        x.http().get(params, new MyXUtilsCallBack(false) {
            MessageModel1 MessageModel1;
            @Override
            public void success(String result) {
                MessageModel1 = JSON.parseObject(result, MessageModel1.class);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }

            @Override
            public void finished() {
                mData.clear();
                if (null != MessageModel1) {
                    mControl.addOrUpDateGhsMsgTable(MessageModel1);
                }
                mData.addAll(mControl.orderGhsMsgByTime(xxdl));
                MainActivity2 activity = (MainActivity2) getActivity();
                isConn = false;
                mContext.dismissProgressDialog();
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void getHQMsgData(int xxdl) {
        RequestParams params = new RequestParams(HttpUrl.HQMSGLIST);
        params.addBodyParameter("hqpm", "");
        params.addBodyParameter("hqszd", "");
        params.addBodyParameter("currentPage", "1");
        x.http().get(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {
                HQMsgModel hqMsgModel = JSON.parseObject(result, HQMsgModel.class);
            }

            @Override
            public void finished() {

            }
        });
    }

    private void setTabMsg(MessageView messageView) {
        for (int i = 0; i < messageViews.length; i++) {
            messageViews[i].setRootBackground(false);
            messageViews[i].setEnabled(true);
        }
        messageView.setRootBackground(true);
        messageView.setEnabled(false);
    }

    /**
     * 生成红点
     * 如果当前选中项则不显示红点
     */
    private void initReadDot() {
        Bundle bundle = getArguments();
        if (null != bundle) {
            for (String key : bundle.keySet()) {
                if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                    if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                        continue;
                    }
                    try {
                        JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                        Logger.json(json.toString());
                        String msgtype = json.getString("xxdl");
                        switch (msgtype) {
                            case "1":
                                //采购消息
                                if (msgType == 1) {
                                    break;
                                }
                                cgMsg.setDotVisibility(true);
                                break;
                            case "2":
                                //订单消息
                                if (msgType == 2) {
                                    break;
                                }
                                ddMsg.setDotVisibility(true);
                                break;
                            case "3":
                                //系统消息
                                if (msgType == 3) {
                                    break;
                                }
                                systemMsg.setDotVisibility(true);
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    continue;
                }
            }
        } else {
            return;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isHidden()) {
            mContext.getRlTitleRoot().setVisibility(View.VISIBLE);
            mContext.setTitleText("消息");
            mContext.setTitleLeftVisibility(false);
        } else {
            mContext.getRlTitleRoot().setVisibility(View.GONE);
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        getMsgData(msgType);
        refreshLayout.endRefreshing();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        refreshLayout.endLoadingMore();
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MessageModel1.ListBean model1 = mData.get(position);
        mControl.upDateGhsMsgRead(model1.getXxdl(), model1.getXxlx(),model1.getGlid(), 1);
        if (mData.get(position).getXxdl() == 3) {
            getMsgData(msgType);
            return;
        }

        Intent intent = new Intent(getActivity(), WebViewActivity.class);
        /**
         * 1：订单消息，2：行情消息，3：系统消息
         */
        switch (model1.getXxdl()) {
            case 1:
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/dd-xq.html");
                break;
            case 2:
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/hangqing-xq.html");
                break;
        }
        Logger.i("click to String ->" + model1.toString());
        Logger.i("MessageDetail->onClick!!!");
        Logger.i("MessageDetail->xxdl->%d,xxlx->%d", model1.getXxdl(), model1.getXxlx());
        intent.putExtra(Constant.WEB_HUNHEID, model1.getGlcompanyid());
        intent.putExtra(Constant.WEB_CGID, model1.getGlid());
        intent.putExtra(Constant.WEB_GLID, model1.getGlid());
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mControl.close();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    public void reload() {
        getMsgData(msgType);
    }
}
