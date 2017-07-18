package cn.soft_x.supplies.fragment.nonelectrical;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import cn.soft_x.supplies.activity.BaseActivity;
import cn.soft_x.supplies.activity.nonelectrical.Wo2Activity;

/**
 * Created by Administrator on 2016-11-02.
 */
public abstract class BaseFragment1 extends Fragment {

    protected BaseActivity mContext;
    protected FragmentManager mChildFragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (BaseActivity) getActivity();
        mChildFragmentManager = getChildFragmentManager();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mChildFragmentManager = null;
        mContext = null;
    }

}
