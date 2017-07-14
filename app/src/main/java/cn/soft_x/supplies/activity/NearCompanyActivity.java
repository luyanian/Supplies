package cn.soft_x.supplies.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.maverick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.adapter.NearCompanyAdapter;
import cn.soft_x.supplies.application.AppManager;
import cn.soft_x.supplies.application.SuppliesApplication;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.http.MyXUtilsCallBack;
import cn.soft_x.supplies.model.NearModel;
import cn.soft_x.supplies.utils.Constant;

public class NearCompanyActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {

    @BindView(R.id.near_mapview)
    MapView nearMapview;
    @BindView(R.id.near_listView)
    ListView nearListView;
    @BindView(R.id.near_refreshLayout)
    BGARefreshLayout nearRefreshLayout;

    private BGARefreshViewHolder viewHolder;
    private BaiduMap mBaiduMap;

    private List<NearModel> mData = new ArrayList<>();
    private NearCompanyAdapter mAdapter;

    private int mPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_company);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        mBaiduMap = nearMapview.getMap();
        mBaiduMap.setBuildingsEnabled(false);
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        initRefreshLayout();
        initData(0);
    }

    private void initData(final int page) {
        //        {"appyhid":"用户ID",
        // "type":"类型(0拆解企业，1，拾起卖，2，调货商,3,供货商)",
        // "currentPage":"页码","numPerPage":"页数"}
        showProgressDialog("正在加载...");
        RequestParams params = new RequestParams(HttpUrl.NEAR);
        params.addBodyParameter("appyhid", Constant.USER_ID);
        params.addBodyParameter("type", "0");
        params.addBodyParameter("filters", 11 + "");
        params.addBodyParameter("currentPage", page + "");
        params.addBodyParameter("numPerPage", "10");
        x.http().post(params, new MyXUtilsCallBack() {
            LatLng companyLatLng;

            @Override
            public void success(String result) {
                Logger.i(result);
                List<NearModel> list = new ArrayList<>();
                try {
                    JSONObject object = new JSONObject(result);
                    companyLatLng = new LatLng(object.getDouble("latitude"), object.getDouble("longitude"));
                    JSONArray array = object.getJSONArray("map");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object1 = array.getJSONObject(i);
                        String org_id = object1.getString("org_id");
                        String org_name = object1.getString("org_name");
                        String address = object1.getString("address");
                        JSONArray latlng = object1.getJSONArray("location");
                        String org_pic = object1.getString("org_pic");
                        LatLng latLng = new LatLng(latlng.getDouble(1), latlng.getDouble(0));
                        NearModel nearModel = new NearModel();
                        nearModel.setAddress(address);
                        nearModel.setLatLng(latLng);
                        nearModel.setOrg_id(org_id);
                        nearModel.setOrg_name(org_name);
                        nearModel.setOrg_pic(org_pic);
                        list.add(nearModel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (page == 0) {
                    if (mData.size() > 0) {
                        mData.clear();
                    }
                    mData.addAll(0, list);
                } else {
                    mData.addAll(list);
                }
            }

            @Override
            public void finished() {
                dismissProgressDialog();
                mAdapter.notifyDataSetChanged();
                if (isSuccess() || (page == 0 && resCode.equals("4"))) {
                    isShowToast = false;
                    initMapDot(mData, Constant.LOCATION);
                }
                if (page == 0 && resCode.equals("4")) {
                    ToastUtil.showToast(NearCompanyActivity.this, "附近500km暂无拆解企业！");
                }
            }
        });
    }

    private void initMapDot(List<NearModel> data, LatLng companyLatLng) {
        mBaiduMap.clear();
        BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_near_marker);
        BitmapDescriptor bd2 = BitmapDescriptorFactory.fromResource(R.drawable.lan);
        LatLng ll;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (data.size() != 0) {
            for (int i = 0; i < data.size(); i++) {
                ll = data.get(i).getLatLng();
                OverlayOptions oo = new MarkerOptions().icon(bd).position(ll).title(data.get(i).getOrg_name());
                mBaiduMap.addOverlay(oo);
                builder.include(ll);
            }
        }
        OverlayOptions companyOo = new MarkerOptions().icon(bd2).position(companyLatLng);
        mBaiduMap.addOverlay(companyOo);
        builder.include(companyLatLng);
        LatLngBounds bounds = builder.build();
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(bounds);
        mBaiduMap.animateMapStatus(u);
    }

    private void initRefreshLayout() {
        nearRefreshLayout.setIsShowLoadingMoreView(true);
        viewHolder = new BGANormalRefreshViewHolder(this, true);
        nearRefreshLayout.setRefreshViewHolder(viewHolder);
        nearRefreshLayout.setDelegate(this);
        mAdapter = new NearCompanyAdapter(this, mData);
        nearListView.setAdapter(mAdapter);
    }


    @Override
    protected void initBaseTitle() {
        setTitleText("附近的拆解企业");
    }

    @OnClick({R.id.title_bar_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_left:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        nearMapview.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        nearMapview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        nearMapview.onPause();
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mPage = 1;
        initData(0);
        refreshLayout.endRefreshing();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        initData(mPage);
        mPage++;
        dismissProgressDialog();
        refreshLayout.endLoadingMore();
        return false;
    }
}
