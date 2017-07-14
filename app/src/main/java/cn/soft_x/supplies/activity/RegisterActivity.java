package cn.soft_x.supplies.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.method.DigitsKeyListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.maverick.utils.Bimp;
import com.maverick.utils.EditTextUtils;
import com.maverick.utils.FormatUtils;
import com.maverick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.application.AppManager;
import cn.soft_x.supplies.application.SuppliesApplication;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.http.MyXUtilsCallBack;
import cn.soft_x.supplies.model.PinZhongModel;
import cn.soft_x.supplies.model.UpLoadModel;
import cn.soft_x.supplies.utils.Constant;
import cn.soft_x.supplies.view.WarpLinearLayout;
import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

/**
 * 注册
 */
public class RegisterActivity extends BaseActivity implements OnGetGeoCoderResultListener, AdapterView.OnItemSelectedListener {

    @BindView(R.id.register_ed_id_card)
    EditText registerEdIdCard;
    @BindView(R.id.register_ed_name)
    EditText registerEdName;
    @BindView(R.id.register_ed_phone)
    EditText registerEdPhone;
    @BindView(R.id.register_ed_pwd1)
    EditText registerEdPwd1;
    @BindView(R.id.register_ed_pwd2)
    EditText registerEdPwd2;
    @BindView(R.id.register_ed_address)
    EditText registerEdAddress;
    @BindView(R.id.register_ed_emergency)
    EditText registerEdEmergency;
    @BindView(R.id.register_ll_checkbox)
    WarpLinearLayout registerLlCheckbox;
    @BindView(R.id.ac_auth_left_iv)
    ImageView acAuthLeftIv;
    @BindView(R.id.ac_auth_right_iv)
    ImageView acAuthRightIv;
    @BindView(R.id.register_ed_ver)
    EditText registerEdVer;
    @BindView(R.id.register_btn_get_ver)
    Button registerBtnGetVer;
    @BindView(R.id.register_do_register)
    Button registerDoRegister;
    @BindView(R.id.register_checkbox)
    CheckBox registerCheckbox;
    @BindView(R.id.register_tv_agreement)
    TextView registerTvAgreement;
    @BindView(R.id.register_sp)
    Spinner registerSp;
    @BindView(R.id.register_ed_city)
    EditText registerEdCity;
    @BindView(R.id.ac_auth_sign_iv)
    ImageView acAuthSignIv;
    // 调货商不需要勾选货物
    private boolean isTransfer = false;
    private boolean isLeft = true;
    private int identity = -1;
    private ArrayList<String> imgPath = new ArrayList<>();

    private final static int REQUEST_IMAGE_LEFT = 0x001;
    private final static int REQUEST_IMAGE_RIGHT = 0x002;
    private final static int REQUEST_IMAGE_SIGN = 0x003;
    private String roleId="";

    private static final String TAG = "RegisterActivity";
    private String leftPicPath="",rightPicPath="",qianmingPicPath="";
    private GeoCoder mSearch = null;

    private String[] citys;

    private CountDownTimer countDownTimer = new CountDownTimer(120000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (registerBtnGetVer != null) {
                registerBtnGetVer.setClickable(false);//防止重复点击
                registerBtnGetVer.setText(millisUntilFinished / 1000 + "s重发");
                registerBtnGetVer.setBackgroundResource(R.drawable.bg_yellow_btn_enable);
            }
        }

        @Override
        public void onFinish() {
            if (registerBtnGetVer != null) {
                registerBtnGetVer.setClickable(true);//防止重复点击
                registerBtnGetVer.setText(R.string.get_ver);
                registerBtnGetVer.setBackgroundResource(R.drawable.bg_yellow_btn);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AppManager.getAppManager().addActivity(this);
        ButterKnife.bind(this);
        initView();
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
    }

    @Override
    protected void initBaseTitle() {
        setTitleText(R.string.register_detail);
        setTitleLeftVisibility(true);
    }

    private void initView() {
        roleId = getIntent().getStringExtra("roleId");
        registerEdIdCard.setKeyListener(DigitsKeyListener.getInstance("0123456789xX"));
        if (isTransfer) {
            registerLlCheckbox.setVisibility(View.GONE);
        }

        citys = getResources().getStringArray(R.array.city);
        registerSp.setOnItemSelectedListener(this);
        RequestParams params = new RequestParams(HttpUrl.PINZHONG);
        params.addBodyParameter("roleId",roleId);
        x.http().get(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {
                PinZhongModel pinZhongModel = JSON.parseObject(result, PinZhongModel.class);
                if (isSuccess()) {
                    initPinZhong(pinZhongModel.getList());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }

            @Override
            public void finished() {

            }
        });
    }

    private void initPinZhong(List<PinZhongModel.ListBean> list) {
        for(PinZhongModel.ListBean listBean : list) {
            CheckBox view = (CheckBox) View.inflate(this, R.layout.activity_register_pz_item, null);
            view.setTag(listBean.getPzid());
            view.setText(listBean.getPzname());
            registerLlCheckbox.addView(view);
        }
    }

    @OnClick({R.id.register_tv_agreement, R.id.title_bar_left, R.id.ac_auth_left_iv, R.id.ac_auth_right_iv,R.id.ac_auth_sign_iv, R.id.register_btn_get_ver, R.id.register_do_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_left:
                finish();
                break;
            case R.id.ac_auth_left_iv:
                isLeft = true;
                checkTakePhotoPermission(REQUEST_IMAGE_LEFT);
                break;
            case R.id.ac_auth_right_iv:
                isLeft = false;
                checkTakePhotoPermission(REQUEST_IMAGE_RIGHT);
                break;
            case R.id.ac_auth_sign_iv:
                checkTakePhotoPermission(REQUEST_IMAGE_SIGN);
                break;
            case R.id.register_btn_get_ver:
                getVerCode();
                break;
            case R.id.register_do_register:
                if (isReadyToRegister()) {
                    showProgressDialog("正在上传信息，请稍候...");
                    getLatLng();
                }
                break;
            case R.id.register_tv_agreement:
                Constant.ROLE_ID = roleId;
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST + "/s/page/zhucetk.html");
                startActivity(intent);
                break;
        }
    }

    private void getLatLng() {
        mSearch.geocode(new GeoCodeOption().city(EditTextUtils.getEdText(registerEdCity)).address(EditTextUtils.getEdText(registerEdAddress)));
    }

    private void checkTakePhotoPermission(int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.REQUEST_PERMISSION_CAMERA_CODE);
            } else {
                takePhoto(requestCode);
            }
        } else {
            takePhoto(requestCode);
        }

    }

    private void takePhoto(int requestCode) {
        imgPath.clear();
        MultiImageSelector.create().showCamera(true) // 是否显示相机. 默认为显示
                .single() // 单选模式
                .origin(imgPath) // 默认已选择图片. 只有在选择模式为多选时有效
                .start(this, requestCode);
    }

    private void getVerCode() {
        if (EditTextUtils.isEmpty(registerEdPhone)) {
            ToastUtil.showToast(this, "手机号不能为空！");
            return;
        } else if (!FormatUtils.checkPhone(EditTextUtils.getEdText(registerEdPhone))) {
            ToastUtil.showToast(this, "手机号格式不对！");
            return;
        }
        showProgressDialog("正在获取验证码...");
        RequestParams params = new RequestParams(HttpUrl.GET_YZM);
        params.addBodyParameter("dhhm", EditTextUtils.getEdText(registerEdPhone));
        x.http().post(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }

            @Override
            public void finished() {
                dismissProgressDialog();
                if(!isSuccess()){
                    Toast.makeText(RegisterActivity.this,resInfo,Toast.LENGTH_SHORT).show();
                }else{
                    countDownTimer.start();
                }
            }
        });
    }

    private String getGoods() {
        String str = "";
        for (int i=1;i<registerLlCheckbox.getChildCount();i++){
            CheckBox checkBox= (CheckBox) registerLlCheckbox.getChildAt(i);
            if(checkBox.isChecked()){
                str = str+"|"+checkBox.getText().toString();
            }
        }
       return  "".equals(str)?"":str.substring(1);
    }

    private void doRegister(double latitude, double longitude) {
        Map<String, String> map = new HashMap<>();
        map.put("yhtype", "1");
        map.put("shebei", "0");
        map.put("brxm", EditTextUtils.getEdText(registerEdName));
        map.put("dhhm", EditTextUtils.getEdText(registerEdPhone));
        map.put("password", EditTextUtils.getEdText(registerEdPwd1));
        map.put("zz", EditTextUtils.getEdText(registerEdAddress));
        map.put("jjlxfs", EditTextUtils.getEdText(registerEdEmergency));
        map.put("sfzh", EditTextUtils.getEdText(registerEdIdCard));
        map.put("ggpz", getGoods());
        map.put("sfztpz", leftPicPath);
        map.put("sfztpf", rightPicPath);
        map.put("qianmtp", qianmingPicPath);
        map.put("yzm", EditTextUtils.getEdText(registerEdVer));
        map.put("latitude", String.valueOf(latitude));
        map.put("longitude", String.valueOf(longitude));
        map.put("roleId", roleId);
        String json = JSON.toJSONString(map);
        String search = getParamsAESEncode(json);
        String signature = getParamsRSEEncode(json);

        RequestParams params = new RequestParams(HttpUrl.REGISTER);
        params.addBodyParameter("search", search);
        params.addBodyParameter("signature", signature);

        x.http().get(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {
                if (isSuccess()) {
                    ToastUtil.showToast(RegisterActivity.this, "信息已经提交，请等待审核通过！");
                }else{
                    ToastUtil.showToast(RegisterActivity.this, resInfo);
                }
            }

            @Override
            public void finished() {
                dismissProgressDialog();
                if (isSuccess()) {
                    AppManager.getAppManager().finishActivityExcept(RegisterActivity.this);
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void setCheckBox(CheckBox checkBox) {
        checkBox.setChecked(!checkBox.isChecked());
    }

    private boolean isReadyToRegister() {
        if (EditTextUtils.isEmpty(registerEdName)) {
            ToastUtil.showToast(this, "姓名不能为空！");
            return false;
        } else if (EditTextUtils.isEmpty(registerEdPhone)) {
            ToastUtil.showToast(this, "手机号不能为空！");
            return false;
        } else if (!FormatUtils.checkPhone(EditTextUtils.getEdText(registerEdPhone))) {
            ToastUtil.showToast(this, "手机号格式不对！");
            return false;
        } else if (EditTextUtils.isEmpty(registerEdPwd1)) {
            ToastUtil.showToast(this, "密码不能为空！");
            return false;
        } else if (EditTextUtils.isEmpty(registerEdPwd2)) {
            ToastUtil.showToast(this, "重复密码不能为空！");
            return false;
        } else if (!EditTextUtils.getEdText(registerEdPwd2)
                .equals(EditTextUtils.getEdText(registerEdPwd1))) {
            ToastUtil.showToast(this, "两次密码输入不一致！");
            return false;
        } else if (spinnerIndex == 0) {
            ToastUtil.showToast(this, "请选择所在地的省(直辖市)！");
            return false;
        } else if (EditTextUtils.isEmpty(registerEdCity)) {
            ToastUtil.showToast(this, "请输入所在城市！");
            return false;
        } else if (EditTextUtils.isEmpty(registerEdAddress)) {
            ToastUtil.showToast(this, "住址不能为空！");
            return false;
        } else if ("".equals(getGoods())) {
            ToastUtil.showToast(this, "至少选择一类供货商品！");
            return false;
        } else if (EditTextUtils.isEmpty(registerEdIdCard)) {
            ToastUtil.showToast(this, "身份证号不能为空！");
            return false;
        } else if (!FormatUtils.checkIDCard(EditTextUtils.getEdText(registerEdIdCard))) {
            ToastUtil.showToast(this, "身份证号码格式不对！");
            return false;
        } else if ("".equals(leftPicPath)) {
            ToastUtil.showToast(this, "请上传身份证正面的照片！");
            return false;
        } else if ("".equals(rightPicPath)) {
            ToastUtil.showToast(this, "请上传身份证反面的照片！");
            return false;
        } else if ("".equals(qianmingPicPath)) {
            ToastUtil.showToast(this, "请上传手写签名照！");
            return false;
        } else if (EditTextUtils.isEmpty(registerEdVer)) {
            ToastUtil.showToast(this, "请输入验证码！");
            return false;
        } else if (!registerCheckbox.isChecked()) {
            ToastUtil.showToast(this, "请同意用户协议！");
            return false;
        } else if (!FormatUtils.checkPhone(EditTextUtils.getEdText(registerEdEmergency))) {
            ToastUtil.showToast(this, "紧急联系方式手机号码格式不对！");
            return false;
        } else if (EditTextUtils.getEdText(registerEdPhone).equals(EditTextUtils.getEdText(registerEdEmergency))) {
            ToastUtil.showToast(this, "紧急联系人与注册号码不能一致！");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_LEFT) {
            if (resultCode == RESULT_OK) {
                // 获取返回的图片列表
                List<String> path = data
                        .getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                File file = new File(path.get(0));
                uploadFile(file,"leftPicPath");
                Bitmap bitmap = Bimp.getimage(path.get(0));
                acAuthLeftIv.setImageBitmap(bitmap);
            }
        } else if (requestCode == REQUEST_IMAGE_RIGHT) {
            if (resultCode == RESULT_OK) {
                List<String> path = data
                        .getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                File file = new File(path.get(0));
                uploadFile(file,"rightPicPath");
                Bitmap bitmap = Bimp.getimage(path.get(0));
                acAuthRightIv.setImageBitmap(bitmap);
            }
        }else if (requestCode == REQUEST_IMAGE_SIGN) {
            if (resultCode == RESULT_OK) {
                List<String> path = data
                        .getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                File file = new File(path.get(0));
                uploadFile(file,"qianmingPicPath");
                Bitmap bitmap = Bimp.getimage(path.get(0));
                acAuthSignIv.setImageBitmap(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                    if (isLeft) takePhoto(REQUEST_IMAGE_LEFT);
                    else takePhoto(REQUEST_IMAGE_RIGHT);
                } else {
                    //不具有相关权限，给予用户提醒，比如Toast或者对话框，让用户去系统设置-应用管理里把相关权限开启
                    ToastUtil.showToast(this, "相关权限未设置，可能出现使用异常！请去“设置-应用管理”中设置相关权限");
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(RegisterActivity.this, "抱歉，无法获取正确地址！请修改后重试！", Toast.LENGTH_LONG)
                    .show();
            dismissProgressDialog();
            return;
        }
        double lat = geoCodeResult.getLocation().latitude;
        double lgt = geoCodeResult.getLocation().longitude;
        Logger.i("纬度：%f 经度：%f",lat ,lgt );
        if(lat>180||lat<-180||lgt>180||lgt<-180){
            Toast.makeText(RegisterActivity.this, "抱歉，无法获取正确地址！请修改后重试！", Toast.LENGTH_LONG).show();
        }else {
            doRegister(lat, lgt);
        }
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }

    @Override
    protected void onDestroy() {
        mSearch.destroy();
        super.onDestroy();
    }

    private int spinnerIndex = 0;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerIndex = position;
        switch (position) {
            case 1:
                registerEdCity.setText(citys[1]);
                registerEdCity.setEnabled(false);
                break;
            case 2:
                registerEdCity.setText(citys[2]);
                registerEdCity.setEnabled(false);
                break;
            case 3:
                registerEdCity.setText(citys[3]);
                registerEdCity.setEnabled(false);
                break;
            case 4:
                registerEdCity.setText(citys[4]);
                registerEdCity.setEnabled(false);
                break;
            default:
                registerEdCity.setEnabled(true);
                registerEdCity.setText("");
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @OnClick(R.id.ac_auth_sign_iv)
    public void onViewClicked() {
    }
    private void uploadFile(File file, final String flag) {
        showProgressDialog("正在上传图片，请稍候...");
        RequestParams params = new RequestParams(HttpUrl.UPLOADIMG);
        List<KeyValue> list = new ArrayList<>();
        list.add(new KeyValue("image",file));
        MultipartBody body = new MultipartBody(list,"utf-8");
        params.setRequestBody(body);
        x.http().post(params, new MyXUtilsCallBack() {
            @Override
            public void success(String result) {
                UpLoadModel upLoadModel = JSON.parseObject(result,UpLoadModel.class);
                if ("leftPicPath".equals(flag)) {
                    leftPicPath = upLoadModel.getPath();
                }
                if ("rightPicPath".equals(flag)) {
                    rightPicPath = upLoadModel.getPath();
                }
                if ("qianmingPicPath".equals(flag)) {
                    qianmingPicPath = upLoadModel.getPath();
                }
                Toast.makeText(RegisterActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
            }

            @Override
            public void finished() {
                dismissProgressDialog();
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
