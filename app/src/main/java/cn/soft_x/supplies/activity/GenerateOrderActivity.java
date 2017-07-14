package cn.soft_x.supplies.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.alibaba.fastjson.JSON;
import com.maverick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

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
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.application.AppManager;
import cn.soft_x.supplies.application.SuppliesApplication;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.http.MyXUtilsCallBack;
import cn.soft_x.supplies.model.TruckItemModel;
import cn.soft_x.supplies.model.XiaoshourenyuanModel;
import cn.soft_x.supplies.utils.Constant;

public class GenerateOrderActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.order_spinner)
    Spinner orderSpinner;
    @BindView(R.id.order_sure)
    Button orderSure;
    @BindView(R.id.order_dp)
    DatePicker orderDp;
    @BindView(R.id.order_ll_1)
    LinearLayout mOrderLl1;

    private ArrayAdapter mAdapter;

    private List<String> strings = new ArrayList<>();

    private ArrayList<TruckItemModel> data = new ArrayList<>();
    private String id = "";
    private XiaoshourenyuanModel model;
    private int spinnerIndex = 0;
    private int jyms = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_order);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initIntent();

    }

    private void initIntent() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        data = (ArrayList<TruckItemModel>) bundle.getSerializable("model");
        id = bundle.getString("id");
        jyms = bundle.getInt("jyms");
        Logger.i("交易模式->%d", jyms);
        if (jyms != 1) {
            mOrderLl1.setVisibility(View.INVISIBLE);
        } else {
            initData();
        }
    }


    private void initData() {
        showProgressDialog("正在加载...");
        RequestParams params = new RequestParams(HttpUrl.XSRY);
        params.addBodyParameter("sdid", id);
        x.http().post(params, new MyXUtilsCallBack() {

            @Override
            public void success(String result) {
                model = JSON.parseObject(result, XiaoshourenyuanModel.class);
                for (XiaoshourenyuanModel.ListBean bean :
                        model.getList()) {
                    strings.add(bean.getName());
                }
            }

            @Override
            public void finished() {
                dismissProgressDialog();
                if (isSuccess()) {
                    mAdapter = new ArrayAdapter(GenerateOrderActivity.this, android.R.layout.simple_list_item_1, strings);
                    orderSpinner.setAdapter(mAdapter);
                    orderSpinner.setOnItemSelectedListener(GenerateOrderActivity.this);
                }

            }
        });
    }

    @Override
    protected void initBaseTitle() {
        setTitleText("生成订单");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Logger.i(strings.get(position));
        spinnerIndex = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @OnClick({R.id.title_bar_left, R.id.order_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_left:
                finish();
                break;
            case R.id.order_sure:
                Logger.e("日期->" + orderDp.getYear() + "-" + orderDp.getMonth() + "-" + orderDp.getDayOfMonth());
                readyForUpload();
                break;
        }
    }

    private void readyForUpload() {
        //        {"appyhid":"用户ID",
        // "gwcids":"购物车组(多个购物车id用|分割)","id":"锁单ID",
        // "yjdcsj":"预计到厂时间","sxrymc":"销售人员","sxryid":"销售人员Id"}
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String currentTime = formatter.format(curDate);
        String choseTime = "";
        if ((orderDp.getMonth() + 1) >= 10) {
            choseTime = orderDp.getYear() + "-" + (orderDp.getMonth() + 1) + "-" + orderDp.getDayOfMonth();
        } else {
            choseTime = orderDp.getYear() + "-0" + (orderDp.getMonth() + 1) + "-" + orderDp.getDayOfMonth();
        }
        Date choseDate;
        Logger.i("currentTime->%s,choseTime->%s", currentTime, choseTime);
        try {
            choseDate = formatter.parse(choseTime);
            Logger.i("curDate->" + curDate.getTime() + ",choseTime->" + choseDate.getTime());
            if (currentTime.equals(choseTime)) {
                Logger.i("curDate->" + curDate.getTime() + ",choseTime->" + choseDate.getTime());
                doUpload(choseTime);
                return;
            } else if (curDate.getTime() > choseDate.getTime()) {
                ToastUtil.showToast(this, "送达时间选择异常！");
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        doUpload(choseTime);
    }

    private void doUpload(String choseTime) {
        showProgressDialog("正在生成订单...");
        RequestParams params = new RequestParams(HttpUrl.SCDD);
        params.addBodyParameter("appyhid", Constant.USER_ID);
        params.addBodyParameter("id", id);
        params.addBodyParameter("gwcids", getGWCids());
        params.addBodyParameter("yjdcsj", choseTime);
        if (jyms == 1) {
            params.addBodyParameter("sxrymc", strings.get(spinnerIndex));
            params.addBodyParameter("sxryid", model.getList().get(spinnerIndex).getId());
        } else {
            params.addBodyParameter("sxrymc", "");
            params.addBodyParameter("sxryid", "");
        }
        x.http().post(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {

            }

            @Override
            public void finished() {
                dismissProgressDialog();
                if (isSuccess()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GenerateOrderActivity.this)
                            .setTitle("提示").setMessage("生成订单成功，等待商家确认。").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    finish();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    //                    ToastUtil.showToast(GenerateOrderActivity.this, "订单生成成功！");
                }
            }
        });
    }


    private String getGWCids() {
        String s = "";
        for (int i = 0; i < data.size(); i++) {
            if (i != data.size() - 1) {
                s += data.get(i).getGwcid() + "|";
            } else
                s += data.get(i).getGwcid();
        }
        Logger.i("getCids->%s", s);
        return s;


    }
}
