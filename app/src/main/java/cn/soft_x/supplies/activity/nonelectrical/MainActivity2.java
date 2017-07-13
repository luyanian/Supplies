package cn.soft_x.supplies.activity.nonelectrical;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.activity.BaseActivity;
import cn.soft_x.supplies.activity.TruckActivity;
import cn.soft_x.supplies.application.SuppliesApplication;
import cn.soft_x.supplies.fragment.HomeFragment;
import cn.soft_x.supplies.fragment.MessageFragment;
import cn.soft_x.supplies.fragment.WebFragment1;
import cn.soft_x.supplies.fragment.WebFragment2;
import cn.soft_x.supplies.fragment.nonelectrical.HomeFragment1;
import cn.soft_x.supplies.fragment.nonelectrical.MessageFragment1;
import cn.soft_x.supplies.fragment.nonelectrical.WebFragment11;
import cn.soft_x.supplies.fragment.nonelectrical.WebFragment21;

/**
 * Created by ryon on 2017/6/20.
 */

public class MainActivity2 extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener{
    @BindView(R.id.activity_main_bottom_bar)
    public BottomNavigationBar mainBottomBar;

    private MessageFragment1 mMsgFragment1;
    private HomeFragment1 mHomeFragment1;
    private WebFragment11 mWebFragment11;
    private WebFragment21 mWebFragment21;

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
        if(onFragmentBack()){
            return false;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean onFragmentBack() {
        if(fragmentIndex==0){
            if(mHomeFragment1!=null){
                return mHomeFragment1.onBack();
            }
        }
        if(fragmentIndex==1){
            if(mWebFragment11!=null) {
                return mWebFragment11.onBack();
            }
        }
        if(fragmentIndex==2){
            if(mWebFragment21!=null){
                return mWebFragment21.onBack();
            }
        }
        if(fragmentIndex==3) {
            if (mMsgFragment1 != null) {
                return mMsgFragment1.onBack();
            }
        }

            return false;
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
        if (null == mHomeFragment1) {
            mHomeFragment1 = new HomeFragment1();
        }
        if (!mHomeFragment1.isAdded())
            mFragmentManager.beginTransaction().add(R.id.fragment_container, mHomeFragment1).commit();
    }

    private void initBottomBar() {
        mainBottomBar.setActiveColor(R.color.color_app_theme)
                .setInActiveColor(R.color.color_home_bottom)
                .setBarBackgroundColor(R.color.color_app_white);
        mainBottomBar.setMode(BottomNavigationBar.MODE_FIXED);
        mainBottomBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mainBottomBar.addItem(new BottomNavigationItem(R.drawable.bottom_home, "首页"))
                .addItem(new BottomNavigationItem(R.drawable.bottom_dingdan, "订单"))
                .addItem(new BottomNavigationItem(R.drawable.bottom_caigou, "行情"))
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
                if (null == mHomeFragment1) {
                    mHomeFragment1 = new HomeFragment1();
                    if (!mHomeFragment1.isAdded())
                        transaction.add(R.id.fragment_container, mHomeFragment1);
                } else
                    transaction.show(mHomeFragment1);
                getRlTitleRoot().setVisibility(View.GONE);
                break;
            case 1:
                getRlTitleRoot().setVisibility(View.GONE);
                mWebFragment11 = new WebFragment11();
                if (!mWebFragment11.isAdded())
                    transaction.add(R.id.fragment_container, mWebFragment11);
                transaction.show(mWebFragment11);
                //                Intent intent1 = new Intent(this, WebViewActivity.class);
                //                intent1.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/alldingdan.html");
                //                startActivity(intent1);
                break;
            case 2:
                getRlTitleRoot().setVisibility(View.GONE);
                mWebFragment21 = new WebFragment21();
                if (!mWebFragment21.isAdded())
                    transaction.add(R.id.fragment_container, mWebFragment21);
                transaction.show(mWebFragment21);
                //                Intent intent2 = new Intent(this, WebViewActivity.class);
                //                intent2.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/caigou.html");
                //                startActivity(intent2);
                break;
            case 3:
                if (null == mMsgFragment1) {
                    mMsgFragment1 = new MessageFragment1();
                    if (!mMsgFragment1.isAdded())
                        transaction.add(R.id.fragment_container, mMsgFragment1);
                    if (null != bundle) {
                        mMsgFragment1.setArguments(bundle);
                    }
                } else
                    transaction.show(mMsgFragment1);
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
        onTabSelected(position);
    }


    private void hideFragment(FragmentTransaction transaction) {
        if (null != mHomeFragment1) {
            transaction.hide(mHomeFragment1);
        }
        if (null != mMsgFragment1) {
            transaction.hide(mMsgFragment1);
        }
        if (null != mWebFragment11) {
            transaction.hide(mWebFragment11);
        }
        if (null != mWebFragment21) {
            transaction.hide(mWebFragment21);
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
