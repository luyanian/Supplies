package cn.soft_x.supplies.http;

import android.content.Context;

import com.maverick.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;

import cn.soft_x.supplies.application.SuppliesApplication;

/**
 * Created by Administrator on 2016/7/15.
 */
public abstract class MyXUtilsCallBack implements Callback.CommonCallback<String> {
    public String resCode = "";
    public String resInfo = "";
    public String jsonResult = "";
    public static final String TAG = MyXUtilsCallBack.class.getName();
    private Context context = SuppliesApplication.getAppContext();
    public boolean isShowToast;

    public MyXUtilsCallBack() {
        isShowToast = true;
    }


    public MyXUtilsCallBack(boolean isShowToast) {
        this.isShowToast = isShowToast;
    }

    @Override
    public void onSuccess(String result) {
        // 配合logger使用
        Logger.json(result);
        jsonResult = result;
        try {
            JSONObject object = new JSONObject(result);
            resCode = object.getString("resultCode");
            resInfo = object.getString("resultInfo");
            if(result.contains("未登录")){
                onSessionTimeOut();
                return;
            }
            if (isSuccess()) {
                success(result);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        ex.printStackTrace();
    }

    @Override
    public void onCancelled(CancelledException cex) {
        cex.printStackTrace();
    }

    @Override
    public void onFinished() {
        finished();
        if (resCode.equals("")) {
            ToastUtil.showToast(context, "网络连接异常...");
        } else if (!isSuccess() && isShowToast) {
            ToastUtil.showToast(context, resInfo);
        }
    }

    public boolean isSuccess() {
        return resCode.equals("0");
    }

    public abstract void success(String result);

    public abstract void finished();

    public void onSessionTimeOut(){};

}
