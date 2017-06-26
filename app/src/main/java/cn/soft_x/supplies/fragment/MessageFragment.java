package cn.soft_x.supplies.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.jpush.android.api.JPushInterface;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.activity.MainActivity;
import cn.soft_x.supplies.activity.MessageDetailsActivity;
import cn.soft_x.supplies.adapter.MessageAdapter;
import cn.soft_x.supplies.db.MyDBControl;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.http.MyXUtilsCallBack;
import cn.soft_x.supplies.model.MessageModel;
import cn.soft_x.supplies.utils.Constant;
import cn.soft_x.supplies.view.MessageView;

/**
 * Created by Administrator on 2016-11-02.
 */
public class MessageFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate, AdapterView.OnItemClickListener {
    @BindView(R.id.dd_msg)
    MessageView ddMsg;
    @BindView(R.id.cg_msg)
    MessageView cgMsg;
    @BindView(R.id.system_msg)
    MessageView systemMsg;
    @BindView(R.id.fragment_msg_ll)
    LinearLayout fragmentMsgLl;
    @BindView(R.id.fragment_msg_listView)
    ListView fragmentMsgListView;
    @BindView(R.id.fragment_refreshLayout)
    BGARefreshLayout fragmentRefreshLayout;

    private BGANormalRefreshViewHolder mRefreshHolder;

    private MessageView[] messageViews;
    private List<MessageModel.ListBean> mData = new ArrayList<>();
    private MessageAdapter mAdapter;

    private boolean isFirst = true;
    private int msgType = 2;
    MyDBControl mControl;

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_message;
    }

    @Override
    protected void initView() {
        mControl = new MyDBControl(mContext);
        initRefreshLayout();
        messageViews = new MessageView[]{ddMsg, cgMsg, systemMsg};
        ddMsg.setMsgTv("订单消息");
        cgMsg.setMsgTv("采购消息");
        systemMsg.setMsgTv("系统消息");
        clickMsgView(0);
    }

    @Override
    protected void initData() {
        if (!isFirst) {
            getMsgData(msgType);
        }
        isFirst = false;
        initReadDot();
    }

    @Override
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
        mAdapter = new MessageAdapter(mData);
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
                getMsgData(2);
                msgType = 2;
                break;
            case 1:
                // 2016-12-05 采购消息
                getMsgData(1);
                msgType = 1;
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
        if (!isHidden()) {
            mContext.showProgressDialog("正在加载...");
        }
        if (mData.size() > 0) {
            mData.clear();
        }
        //        {"xxdl":"1","appyhid":"049ee74567e5488abfda6422785dac42"}
        if (isConn) {
            return;
        }
        isConn = true;
        RequestParams params = new RequestParams(HttpUrl.MSG_1);
        params.addBodyParameter("xxdl", xxdl + "");
        params.addBodyParameter("appyhid", Constant.USER_ID);
        if (xxdl == 3) {
            long time = mControl.searchMsgTime(xxdl);
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
            Logger.i("外层消息->time"+time);
        }
        x.http().post(params, new MyXUtilsCallBack(false) {
            MessageModel messageModel;

            @Override
            public void success(String result) {
                messageModel = JSON.parseObject(result, MessageModel.class);

            }

            @Override
            public void finished() {
                mData.clear();
                if (null != messageModel) {
                    mControl.addOrUpDateMsgTable(messageModel);
                }
                //                mData.addAll(messageModel.getList());
                mData.addAll(mControl.orderMsgByTime(xxdl));
                MainActivity activity = (MainActivity) getActivity();
                if (activity.fragmentIndex != 4) {
                    isShowToast = false;
                } else {
                    isShowToast = true;
                }
                isConn = false;
                mContext.dismissProgressDialog();
                mAdapter.notifyDataSetChanged();
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
        if (mData.get(position).getXXDL() == 3) {
            return;
        }
        Intent intent = new Intent(mContext, MessageDetailsActivity.class);
        intent.putExtra("xxdl", mData.get(position).getXXDL());
        intent.putExtra("xxlx", mData.get(position).getXXLX());
        //        mControl.upDateMsgRead(mData.get(position).getXXDL(), mData.get(position).getXXLX(), 1);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mControl.close();
    }
}
