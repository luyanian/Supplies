package cn.soft_x.supplies.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.application.SuppliesApplication;
import cn.soft_x.supplies.fragment.HomeFragment;
import cn.soft_x.supplies.fragment.MessageFragment;
import cn.soft_x.supplies.fragment.WebFragment1;
import cn.soft_x.supplies.fragment.WebFragment2;


public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    @BindView(R.id.activity_main_bottom_bar)
    public BottomNavigationBar mainBottomBar;

    private MessageFragment mMsgFragment;
    private HomeFragment mHomeFragment;
    private WebFragment1 mWebFragment1;
    private WebFragment2 mWebFragment2;

    public int fragmentIndex = 0;

    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            SuppliesApplication.getInstance().exitApp();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initBottomBar();
        //  mFragmentManager.beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        if (null == mHomeFragment) {
            mHomeFragment = new HomeFragment();
        }
        if (!mHomeFragment.isAdded())
            mFragmentManager.beginTransaction().add(R.id.fragment_container, mHomeFragment).commit();
    }

    private void initBottomBar() {
        mainBottomBar.setActiveColor(R.color.color_app_theme)
                .setInActiveColor(R.color.color_home_bottom)
                .setBarBackgroundColor(R.color.color_app_white);
        mainBottomBar.setMode(BottomNavigationBar.MODE_FIXED);
        mainBottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mainBottomBar.addItem(new BottomNavigationItem(R.drawable.bottom_home, "首页"))
                .addItem(new BottomNavigationItem(R.drawable.bottom_huoche, "货车"))
                .addItem(new BottomNavigationItem(R.drawable.bottom_dingdan, "订单"))
                .addItem(new BottomNavigationItem(R.drawable.bottom_caigou, "采购"))
                .addItem(new BottomNavigationItem(R.drawable.bottom_msg, "消息"))
                .initialise();
        mainBottomBar.setTabSelectedListener(this);
    }

    @Override
    protected void initBaseTitle() {
        getRlTitleRoot().setVisibility(View.GONE);
    }


    @Override
    public void onTabSelected(int position) {
        Logger.i(position + "");
        fragmentIndex = position;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideFragment(transaction);
        switch (position) {
            case 0:
                if (null == mHomeFragment) {
                    mHomeFragment = new HomeFragment();
                    if (!mHomeFragment.isAdded())
                        transaction.add(R.id.fragment_container, mHomeFragment);
                } else
                    transaction.show(mHomeFragment);
                getRlTitleRoot().setVisibility(View.GONE);
                break;
            case 1:
                Intent intent = new Intent(this, TruckActivity.class);
                startActivity(intent);
                fragmentIndex = 0;
                break;
            case 2:
                getRlTitleRoot().setVisibility(View.GONE);
                mWebFragment1 = new WebFragment1();
                if (!mWebFragment1.isAdded())
                    transaction.add(R.id.fragment_container, mWebFragment1);
                transaction.show(mWebFragment1);
                //                Intent intent1 = new Intent(this, WebViewActivity.class);
                //                intent1.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/alldingdan.html");
                //                startActivity(intent1);
                break;
            case 3:
                getRlTitleRoot().setVisibility(View.GONE);
                mWebFragment2 = new WebFragment2();
                if (!mWebFragment2.isAdded())
                    transaction.add(R.id.fragment_container, mWebFragment2);
                transaction.show(mWebFragment2);
                //                Intent intent2 = new Intent(this, WebViewActivity.class);
                //                intent2.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/caigou.html");
                //                startActivity(intent2);
                break;
            case 4:
                if (null == mMsgFragment) {
                    mMsgFragment = new MessageFragment();
                    if (!mMsgFragment.isAdded())
                        transaction.add(R.id.fragment_container, mMsgFragment);
                    if (null != bundle) {
                        mMsgFragment.setArguments(bundle);
                    }
                } else
                    transaction.show(mMsgFragment);
                getRlTitleRoot().setVisibility(View.VISIBLE);
                setTitleText("消息");
                break;
            default:
                return;


        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    private void hideFragment(FragmentTransaction transaction) {
        if (null != mHomeFragment) {
            transaction.hide(mHomeFragment);
        }
        if (null != mMsgFragment) {
            transaction.hide(mMsgFragment);
        }
        if (null != mWebFragment1) {
            transaction.hide(mWebFragment1);
        }
        if (null != mWebFragment2) {
            transaction.hide(mWebFragment2);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initIntent();
        mainBottomBar.selectTab(fragmentIndex);
        onTabSelected(fragmentIndex);
        if (fragmentIndex==4){
            getRlTitleRoot().setVisibility(View.VISIBLE);
            setTitleText("消息");
        }else {
            getRlTitleRoot().setVisibility(View.GONE);
        }
    }

    private Bundle bundle = null;

    private void initIntent() {
        Intent intent = getIntent();
        int index = -1;
        index = intent.getIntExtra("index", 0);
        Logger.i("index->%d", index);
        boolean isFromReceiver = intent.getBooleanExtra("boolean", false);
        if (isFromReceiver)
            fragmentIndex = index;
        bundle = intent.getBundleExtra("bundle");
    }

}
