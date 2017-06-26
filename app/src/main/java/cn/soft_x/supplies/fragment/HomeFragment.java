package cn.soft_x.supplies.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.facebook.drawee.view.SimpleDraweeView;
import com.maverick.utils.EditTextUtils;
import com.maverick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.activity.InvitationActivity;
import cn.soft_x.supplies.activity.MainActivity;
import cn.soft_x.supplies.activity.NearCompanyActivity;
import cn.soft_x.supplies.activity.TruckActivity;
import cn.soft_x.supplies.activity.WebViewActivity;
import cn.soft_x.supplies.activity.WoActivity;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.http.MyXUtilsCallBack;
import cn.soft_x.supplies.model.ADInfo;
import cn.soft_x.supplies.model.HomeModel;
import cn.soft_x.supplies.utils.Constant;
import cn.soft_x.supplies.view.CycleViewPager;
import cn.soft_x.supplies.view.ViewFactory;

/**
 * 主页
 * Created by Administrator on 2016-11-02.
 */
public class HomeFragment extends BaseFragment {

    @BindView(R.id.fragment_home_bar)
    LinearLayout fragmentHomeBar;
    @BindView(R.id.fragment_cycle_viewpager_content_rl)
    RelativeLayout fragmentCycleViewpagerContentRl;
    @BindView(R.id.fragment_home_spinner)
    Spinner fragmentHomeSpinner;
    @BindView(R.id.fragment_ed_search)
    EditText fragmentEdSearch;
    @BindView(R.id.fragment_iv_search)
    ImageView fragmentIvSearch;
    @BindView(R.id.fragment_iv_person)
    ImageView fragmentIvPerson;
    @BindView(R.id.fragment_rl_huoche)
    RelativeLayout fragmentRlHuoche;
    @BindView(R.id.news_tv_more)
    TextView newsTvMore;
    @BindView(R.id.news_iv_1)
    SimpleDraweeView newsIv1;
    @BindView(R.id.news_tv_title_1)
    TextView newsTvTitle1;
    @BindView(R.id.news_tv_text_1)
    TextView newsTvText1;
    @BindView(R.id.news_item_1)
    RelativeLayout newsItem1;
    @BindView(R.id.news_iv_2)
    SimpleDraweeView newsIv2;
    @BindView(R.id.news_tv_title_2)
    TextView newsTvTitle2;
    @BindView(R.id.news_tv_text_2)
    TextView newsTvText2;
    @BindView(R.id.news_item_2)
    RelativeLayout newsItem2;
    @BindView(R.id.fragment_ll_caigou)
    LinearLayout fragmentLlCaigou;
    @BindView(R.id.ll_yaoqinghan)
    LinearLayout llYaoqinghan;
    @BindView(R.id.ll_xiaoxi)
    LinearLayout llXiaoxi;
    @BindView(R.id.fragment_ll_fabu)
    LinearLayout fragmentLlFabu;
    @BindView(R.id.fragment_ll_fujin)
    LinearLayout fragmentLlFujin;
    private List<ADInfo> infos;
    private List<ImageView> views;
    public CycleViewPager cycleViewPager;
    private String[] companys;
    private int spinnerIndex = 0;
    private List<String> imageUrls = new ArrayList<>();

    private AdapterView.OnItemSelectedListener selectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Logger.i(companys[position]);
            spinnerIndex = position;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            Logger.i("onNothingSelected");
        }
    };
    private boolean isFirst = true;

    @Override
    protected int setLayoutRes() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView() {
        companys = getResources().getStringArray(R.array.spinner);
        //        cycleViewPager = (CycleViewPager) getActivity().getFragmentManager()
        //                .findFragmentById(R.id.fragment_cycle_viewpager_content);
        if (null == cycleViewPager) {
            cycleViewPager = new CycleViewPager();
        }
        mChildFragmentManager.beginTransaction().replace(R.id.fragment_cycle_viewpager_content, cycleViewPager).commit();
        fragmentHomeSpinner.setOnItemSelectedListener(selectedListener);

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
                for (HomeModel.LbtlistBean l :
                        model.getLbtlist()) {
                    imageUrls.add(l.getPICURL());
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }

            @Override
            public void finished() {
                mContext.dismissProgressDialog();
                if (resCode.equals("4")) {
                    isShowToast = false;
                }
                if (isSuccess()) {
                    HomeModel.NewslistBean bean1 = model.getNewslist().get(0);
                    HomeModel.NewslistBean bean2 = model.getNewslist().get(1);

                    newsIv1.setImageURI(Uri.parse(HttpUrl.API_HOST + bean1.getNEWSIMG()));
                    newsTvTitle1.setText(bean1.getNEWSTITLE());
                    newsTvText1.setText(bean1.getCONTENT());

                    newsIv2.setImageURI(Uri.parse(HttpUrl.API_HOST + bean2.getNEWSIMG()));
                    newsTvTitle2.setText(bean2.getNEWSTITLE());
                    newsTvText2.setText(bean2.getCONTENT());
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

    @OnClick({R.id.fragment_iv_search, R.id.fragment_iv_person, R.id.fragment_rl_huoche, R.id.news_tv_more, R.id.news_item_1, R.id.news_item_2, R.id.fragment_ll_caigou, R.id.ll_yaoqinghan, R.id.ll_xiaoxi, R.id.fragment_ll_fabu, R.id.fragment_ll_fujin})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.fragment_iv_search:
                intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/caigou.html");
                intent.putExtra(Constant.WEB_SEARCH, EditTextUtils.getEdText(fragmentEdSearch));
                intent.putExtra(Constant.WEB_SEARCH_TYPE, companys[spinnerIndex]);
                startActivity(intent);
                break;
            case R.id.fragment_iv_person:
                intent = new Intent(mContext, WoActivity.class);
                intent.putExtra("tuichu", 0x111);
                startActivity(intent);
                break;
            case R.id.fragment_rl_huoche:
                intent = new Intent(mContext, TruckActivity.class);
                startActivity(intent);
                break;
            case R.id.news_tv_more:
                intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/news.html");
                startActivity(intent);
                break;
            case R.id.news_item_1:
                intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/newsxq.html");
                intent.putExtra(Constant.WEB_NEWS_ID, model.getNewslist().get(0).getNEWSID());
                startActivity(intent);
                break;
            case R.id.news_item_2:
                intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/newsxq.html");
                intent.putExtra(Constant.WEB_NEWS_ID, model.getNewslist().get(1).getNEWSID());
                startActivity(intent);
                break;
            case R.id.fragment_ll_caigou:
                MainActivity activity1 = (MainActivity) getActivity();
                activity1.mainBottomBar.selectTab(3);
//                activity1.onTabSelected(3);
//                intent = new Intent(mContext, WebViewActivity.class);
//                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/caigou.html");
//                startActivity(intent);
                break;
            case R.id.ll_yaoqinghan:
                intent = new Intent(mContext, InvitationActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_xiaoxi:
                MainActivity activity = (MainActivity) getActivity();
                activity.mainBottomBar.selectTab(4);
//                activity.onTabSelected(4);
                break;
            case R.id.fragment_ll_fabu:
                intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/fabu.html");
                startActivity(intent);
                break;
            case R.id.fragment_ll_fujin:
                intent = new Intent(mContext, NearCompanyActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
