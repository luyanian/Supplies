package cn.soft_x.supplies.fragment.nonelectrical;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.maverick.utils.EditTextUtils;
import com.maverick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.activity.InvitationActivity;
import cn.soft_x.supplies.activity.MainActivity;
import cn.soft_x.supplies.activity.NearCompanyActivity;
import cn.soft_x.supplies.activity.TruckActivity;
import cn.soft_x.supplies.activity.WebViewActivity;
import cn.soft_x.supplies.activity.WoActivity;
import cn.soft_x.supplies.activity.nonelectrical.MainActivity2;
import cn.soft_x.supplies.activity.nonelectrical.Wo2Activity;
import cn.soft_x.supplies.fragment.BaseFragment;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.http.MyXUtilsCallBack;
import cn.soft_x.supplies.model.ADInfo;
import cn.soft_x.supplies.model.HomeModel;
import cn.soft_x.supplies.utils.Constant;
import cn.soft_x.supplies.view.CycleViewPager;
import cn.soft_x.supplies.view.ViewFactory;

/**
 * Created by ryon on 2017/6/20.
 */

public class HomeFragment1 extends BaseFragment {

    @BindView(R.id.fragment_cycle_viewpager_content)
    FrameLayout fragmentCycleViewpagerContent;
    @BindView(R.id.fragment_cycle_viewpager_content_rl)
    RelativeLayout fragmentCycleViewpagerContentRl;
    @BindView(R.id.huoche_iv)
    ImageView huocheIv;
    Unbinder unbinder;
    private List<ADInfo> infos;
    private List<ImageView> views;
    public CycleViewPager cycleViewPager;
    private List<String> imageUrls = new ArrayList<>();
    private boolean isFirst = true;

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_home1;
    }

    @Override
    protected void initView() {
        if (null == cycleViewPager) {
            cycleViewPager = new CycleViewPager();
        }
        mChildFragmentManager.beginTransaction().replace(R.id.fragment_cycle_viewpager_content, cycleViewPager).commit();

    }

    private HomeModel model;

    @Override
    protected void initData() {
        if (!isFirst) {
            return;
        }
        //        mContext.showProgressDialog("正在加载数据...");
        RequestParams params = new RequestParams(HttpUrl.HOME);
        x.http().get(params, new MyXUtilsCallBack(false) {

            @Override
            public void success(String result) {
                model = JSON.parseObject(result, HomeModel.class);
                imageUrls.clear();
                for (HomeModel.LbtlistBean lbtlistBean : model.getLbtlist()) {
                    imageUrls.add(lbtlistBean.getPICURL());
                }

            }

            @Override
            public void finished() {
                mContext.dismissProgressDialog();
                if (resCode.equals("4")) {
                    isShowToast = false;
                }
                if (isSuccess()) {
                    if (isFirst) {
                        isFirst = false;
                        initialize();
                    }
                } else {
                    ToastUtil.showToast(mContext, "数据加载失败！");
                }
            }
        });
    }

    @Override
    public boolean onBack() {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!this.isHidden())
            mContext.getRlTitleRoot().setVisibility(View.GONE);
        else
            mContext.getRlTitleRoot().setVisibility(View.VISIBLE);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 适配轮播图
     */
    private void initialize() {
        infos = new ArrayList<>();
        views = new ArrayList<>();
        for (int i = 0; i < imageUrls.size(); i++) {
            ADInfo info = new ADInfo();
            info.setUrl(HttpUrl.API_HOST + imageUrls.get(i));
            info.setContent("图片-->" + i);
            infos.add(info);
        }

        // 将最后一个ImageView添加进来
        views.add(ViewFactory.getImageView(getActivity(),
                infos.get(infos.size() - 1).getUrl()));
        for (int i = 0; i < infos.size(); i++) {
            views.add(ViewFactory.getImageView(getActivity(), infos.get(i)
                    .getUrl()));
        }
        // 将第一个ImageView添加进来
        views.add(ViewFactory
                .getImageView(getActivity(), infos.get(0).getUrl()));

        // 设置循环，在调用setData方法前调用
        cycleViewPager.setCycle(true);

        // 在加载数据前设置是否循环
        cycleViewPager.setData(views, infos, mAdCycleViewListener);
        // 设置轮播
        cycleViewPager.setWheel(true);

        // 设置轮播时间，默认5000ms
        cycleViewPager.setTime(4000);
        // 设置圆点指示图标组居中显示，默认靠右
        cycleViewPager.setIndicatorCenter();
    }

    /**
     * 轮播点击事件
     */
    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener = new CycleViewPager.ImageCycleViewListener() {

        @Override
        public void onImageClick(ADInfo info, int position, View imageView) {
            //            if (cycleViewPager.isCycle()) {
            //                position = position - 1;
            //                Toast.makeText(getActivity(),
            //                        "position-->" + info.getContent(), Toast.LENGTH_SHORT)
            //                        .show();
            //                Intent intentCarousel = new Intent(getActivity(), CarouselActivity.class);
            //                startActivity(intentCarousel);
            //            }
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.view_hangqing, R.id.ll_dingdan, R.id.ll_xiaoxi,R.id.btnWo})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.ll_dingdan:
                MainActivity2 activity1 = (MainActivity2) getActivity();
                activity1.mainBottomBar.selectTab(1);
                break;
            case R.id.view_hangqing:
                MainActivity2 activity2 = (MainActivity2) getActivity();
                activity2.mainBottomBar.selectTab(2);
                break;
            case R.id.ll_xiaoxi:
                MainActivity2 activity3 = (MainActivity2) getActivity();
                activity3.mainBottomBar.selectTab(3);
                break;
            case R.id.btnWo:
                intent = new Intent(getActivity(), Wo2Activity.class);
                intent.putExtra("tuichu", 0x111);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
