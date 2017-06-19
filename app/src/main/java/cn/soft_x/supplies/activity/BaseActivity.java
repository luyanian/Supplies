package cn.soft_x.supplies.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.soft_x.supplies.R;
import cn.soft_x.supplies.application.SuppliesApplication;
import cn.soft_x.supplies.utils.Constant;
import cn.soft_x.supplies.utils.encryption.AES;
import cn.soft_x.supplies.utils.encryption.Base64Decoder;
import cn.soft_x.supplies.utils.encryption.Base64Encoder;
import cn.soft_x.supplies.utils.encryption.MD5;
import cn.soft_x.supplies.utils.encryption.RSA;


/**
 * Created by Administrator on 2016-10-31.
 */
public abstract class BaseActivity extends FragmentActivity {
    private TextView titleBarLeft, titleBarCenter, titleBarRight;
    private LinearLayout llBaseRoot;
    private RelativeLayout rlBaseRoot;

    private RelativeLayout rlTitleRoot;

    private View view;
    protected FragmentManager mFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);
        SuppliesApplication.getInstance().addActivity(this);
        mFragmentManager = getSupportFragmentManager();
        initBaseView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParam = getWindow().getAttributes();
            localLayoutParam.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | localLayoutParam.flags);
        }
        initBaseTitle();
    }

    private void initBaseView() {
        titleBarLeft = (TextView) findViewById(R.id.title_bar_left);
        titleBarRight = (TextView) findViewById(R.id.title_bar_right);
        titleBarCenter = (TextView) findViewById(R.id.title_bar_center);
        llBaseRoot = (LinearLayout) findViewById(R.id.ll_base_root);
        rlBaseRoot = (RelativeLayout) findViewById(R.id.rl_base_root);
        rlTitleRoot = (RelativeLayout) findViewById(R.id.rl_title_root);
    }

    @Override
    public void setContentView(int layoutResID) {
        view = getLayoutInflater().inflate(layoutResID, null);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.BELOW, R.id.ll_base_root);
        if (null != rlBaseRoot) rlBaseRoot.addView(view, lp);
    }

    /**
     * 设置标题
     *
     * @param titleText
     */
    public void setTitleText(CharSequence titleText) {
        titleBarCenter.setText(titleText);
    }

    /**
     * 通过R.string设置标题
     *
     * @param resId
     */
    public void setTitleText(int resId) {
        titleBarCenter.setText(resId);
    }

    /**
     * 左边是否显示
     *
     * @param isVisibility true 显示  false 不显示
     */
    public void setTitleLeftVisibility(boolean isVisibility) {
        if (isVisibility) titleBarLeft.setVisibility(View.VISIBLE);
        else titleBarLeft.setVisibility(View.GONE);
    }

    /**
     * 右边是否显示
     *
     * @param isVisibility true 显示  false 不显示
     */
    public void setTitleRightVisibility(boolean isVisibility) {
        if (isVisibility) titleBarRight.setVisibility(View.VISIBLE);
        else titleBarRight.setVisibility(View.GONE);
    }

    /**
     * 设置状态栏颜色
     *
     * @param resId
     */
    public void setStatusBarColor(int resId) {
        rlBaseRoot.setBackgroundResource(resId);
    }

    /**
     * title bar 颜色
     *
     * @param resId
     */
    public void setTitleBarColor(int resId) {
        rlTitleRoot.setBackgroundResource(resId);
    }

    /**
     * 设置左边的背景图片
     *
     * @param drawableId
     */
    public void setTitleLeftBackground(int drawableId) {
        if (titleBarLeft.getVisibility() != View.VISIBLE) setTitleLeftVisibility(true);
        titleBarLeft.setBackgroundResource(drawableId);
    }

    /**
     * 设置右边的文字
     *
     * @param titleText
     */
    public void setTitleRightText(CharSequence titleText) {
        titleBarRight.setVisibility(View.VISIBLE);
        titleBarRight.setText(titleText);
    }

    /**
     * 设置右边的文字 by resId
     *
     * @param resId
     */
    public void setTitleRightText(int resId) {
        titleBarRight.setVisibility(View.VISIBLE);
        titleBarRight.setText(resId);
    }

    /**
     * 设置右边的背景图片
     *
     * @param drawableId
     */
    public void setTitleRightBackground(int drawableId) {
        if (titleBarRight.getVisibility() != View.VISIBLE) setTitleRightVisibility(true);
        titleBarRight.setBackgroundResource(drawableId);
    }

    private ProgressDialog dialog;

    /**
     * @param msg dialog msg
     * @return
     */
    public boolean showProgressDialog(String msg) {
        dialog = ProgressDialog.show(this, "提示", msg);
        dialog.show();
        return true;
    }

    /**
     * @param title
     * @param msg
     * @return
     */
    public boolean showProgressDialog(String title, String msg) {
        dialog = ProgressDialog.show(this, title, msg);
        dialog.show();
        return true;
    }

    public boolean dismissProgressDialog() {
        if (dialog == null)
            return false;
        if (dialog.isShowing()) dialog.dismiss();
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissProgressDialog();
        mFragmentManager = null;
    }

    public RelativeLayout getRlTitleRoot() {
        return rlTitleRoot;
    }

    protected void startWebActivity(String url) {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(Constant.WEB_URL, url);
        startActivity(intent);
    }

    abstract protected void initBaseTitle();

    public String getParamsAESEncode(String json) {
        try {
            String utf8 = new String(json.getBytes("UTF-8"), "UTF-8");
            String md5 = MD5.md5(utf8).substring(0,16);
            String aes = AES.encrypt(md5,json);
            return aes;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    public String getParamsRSEEncode(String json) {
        try {
            String utf8 = new String(json.getBytes("UTF-8"), "UTF-8");
            String md5 = MD5.md5(utf8).substring(0,16);
            byte[] buffer = Base64Decoder.decodeToBytes(Constant.publicKey);
            return Base64Encoder.encode(RSA.encryptByPublicKey(md5.getBytes(), buffer));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
