package cn.soft_x.supplies.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import cn.soft_x.supplies.activity.BaseActivity;

/**
 * Created by Administrator on 2016-11-02.
 */
public abstract class BaseFragment extends Fragment {

    protected BaseActivity mContext;
    protected FragmentManager mChildFragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = (BaseActivity) getActivity();
        mChildFragmentManager = getChildFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(setLayoutRes(), container, false);
        ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mChildFragmentManager = null;
        mContext = null;
    }

    /**
     * fragment的布局资源
     *
     * @return
     */
    abstract protected int setLayoutRes();

    /**
     * 设置布局
     */
    abstract protected void initView();

    abstract protected void initData();

}
