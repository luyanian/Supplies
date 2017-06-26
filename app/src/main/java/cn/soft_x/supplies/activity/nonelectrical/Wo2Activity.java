package cn.soft_x.supplies.activity.nonelectrical;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.maverick.utils.Cfg;
import com.maverick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.activity.BaseActivity;
import cn.soft_x.supplies.activity.InvitationActivity;
import cn.soft_x.supplies.activity.NearCompanyActivity;
import cn.soft_x.supplies.activity.SettingActivity;
import cn.soft_x.supplies.activity.TruckActivity;
import cn.soft_x.supplies.activity.WebViewActivity;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.http.MyXUtilsCallBack;
import cn.soft_x.supplies.utils.Constant;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * Created by ryon on 2017/6/10.
 */

public class Wo2Activity extends BaseActivity {


    @BindView(R.id.wo_img_bg)
    ImageView woImgBg;
    @BindView(R.id.wo_img_moren)
    ImageView woImgMoren;
    @BindView(R.id.wo_touxiang)
    SimpleDraweeView woTouxiang;
    @BindView(R.id.wo_img_fl)
    FrameLayout woImgFl;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;

    private ArrayList<String> imgPath = new ArrayList<>();

    private final static int REQUEST_IMAGE = 0x1332;

    private static File IMG_FILE = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initIntent();
        setContentView(R.layout.activity_wo2);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        PointF focusPoint = new PointF(0f, 0f);
        woTouxiang.getHierarchy().setActualImageFocusPoint(focusPoint);
        if (TextUtils.isEmpty(Constant.USER_HEAD_IMG)) {
            woTouxiang.setVisibility(View.GONE);
            woImgBg.setVisibility(View.VISIBLE);
            woImgMoren.setVisibility(View.VISIBLE);
        } else {
            woTouxiang.setVisibility(View.VISIBLE);
            woImgBg.setVisibility(View.GONE);
            woImgMoren.setVisibility(View.GONE);
            woTouxiang.setImageURI(Uri.parse(Constant.USER_HEAD_IMG));
        }
        tvUserName.setText(Cfg.loadStr(this, Constant.SH_USER_NC));
    }

    private void initIntent() {
        Intent intent = getIntent();
        int code = intent.getIntExtra("tuichu", -1);
        Log.i("code", code + "");
        if (code != 0x111) {
            finish();
        }
    }

    @Override
    protected void initBaseTitle() {
        setTitleText("我");
        setTitleLeftVisibility(true);
    }

    private void checkTakePhotoPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.REQUEST_PERMISSION_CAMERA_CODE);
            } else {
                takePhoto();
            }
        } else {
            takePhoto();
        }

    }

    private void takePhoto() {
        imgPath.clear();
        MultiImageSelector.create().showCamera(true) // 是否显示相机. 默认为显示
                .single() // 单选模式
                .origin(imgPath) // 默认已选择图片. 只有在选择模式为多选时有效
                .start(this, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                List<String> path = data
                        .getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                IMG_FILE = new File(path.get(0));
                woImgMoren.setVisibility(View.VISIBLE);
                woImgBg.setVisibility(View.GONE);
                woImgMoren.setVisibility(View.GONE);
                woTouxiang.setImageURI(Uri.parse("file://" + path.get(0)));
                upLoadImg();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void upLoadImg() {
        RequestParams params = new RequestParams(HttpUrl.UPLOAD_HEAD_IMG);
        params.addBodyParameter("appyhid", Constant.USER_ID);
        params.addBodyParameter("file", IMG_FILE);
        x.http().post(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {
                Logger.d(result);
            }
            @Override
            public void finished() {
                if (isSuccess()) {
                    ToastUtil.showToast(Wo2Activity.this, "头像上传成功!请重新登录!");
                }
            }
        });
    }

    private void callPhone(String phone) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel://" + phone));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Constant.REQUEST_PERMISSION_CAMERA_CODE) {
            Logger.i("length" + grantResults.length);
            if (grantResults.length >= 2) {
                int cameraResult = grantResults[0];//相机权限
                int sdcardResult = grantResults[1];//访问文件权限
                boolean cameraGranted = cameraResult == PackageManager.PERMISSION_GRANTED;//拍照权限
                boolean sdcardGranted = sdcardResult == PackageManager.PERMISSION_GRANTED;//访问文件权限
                if (cameraGranted && sdcardGranted) {
                    //具有拍照权限，调用相机
                    takePhoto();
                } else {
                    //不具有相关权限，给予用户提醒，比如Toast或者对话框，让用户去系统设置-应用管理里把相关权限开启
                    ToastUtil.showToast(this, "相关权限未设置，可能出现使用异常！请去“设置-应用管理”中设置相关权限");
                }
            }
        }
        if (requestCode == Constant.REQUEST_PERMISSION_CALL_PHONE_CODE) {
            if (grantResults.length >= 1) {
                int cameraResult = grantResults[0];//相机权限
                boolean cameraGranted = cameraResult == PackageManager.PERMISSION_GRANTED;//拍照权限
                if (cameraGranted) {
                    //具有电话权限
                    callPhone("4000660567");
                } else {
                    //不具有相关权限，给予用户提醒，比如Toast或者对话框，让用户去系统设置-应用管理里把相关权限开启
                    ToastUtil.showToast(this, "电话权限未设置，可能出现使用异常！请去“设置-应用管理”中设置照相权限");
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick({R.id.wo_img_fl,R.id.title_bar_left,R.id.order_daishenhe, R.id.order_jiaoyizhong, R.id.order_yijiesuan, R.id.more_kefu, R.id.more_zhucexinxi, R.id.more_shezhi})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.wo_img_fl:
                checkTakePhotoPermission();
                break;
            case R.id.title_bar_left:
                finish();
                break;
            case R.id.order_daishenhe:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/daishenhedd.html?roleId="+Constant.ROLE_ID);
//                intent.putExtra(Constant.WEB_DDLX, "");
                startActivity(intent);
                break;
            case R.id.order_jiaoyizhong:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/weiwanchengdd.html?roleId="+Constant.ROLE_ID);
//                intent.putExtra(Constant.WEB_DDLX, "2");
                startActivity(intent);
                break;
            case R.id.order_yijiesuan:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/yiwanchengdd.html?roleId="+Constant.ROLE_ID);
//                intent.putExtra(Constant.WEB_DDLX, "5");
                startActivity(intent);
                break;
            case R.id.more_kefu:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(Wo2Activity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Wo2Activity.this, new String[]{Manifest.permission.CALL_PHONE}, Constant.REQUEST_PERMISSION_CALL_PHONE_CODE);
                    } else {
                        callPhone("4000660567");
                    }
                } else {
                    callPhone("4000660567");
                }
                break;
            case R.id.more_zhucexinxi:
                intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/zhucexinxi.html");
//                intent.putExtra(Constant.WEB_DDLX, "2");
                startActivity(intent);
                break;
            case R.id.more_shezhi:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
        }
    }
}
