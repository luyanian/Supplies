package cn.soft_x.supplies.utils;

import android.os.CountDownTimer;

/**
 * 这个类写真失败
 * Created by Administrator on 2016-11-03.
 */
@Deprecated
public class TimeCount extends CountDownTimer {

    private TimeCountCallBack mCallBack;

    public TimeCount(long millisInFuture, long countDownInterval, TimeCountCallBack callBack) {
        super(millisInFuture, countDownInterval);
        mCallBack = callBack;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mCallBack._onTick(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        mCallBack._onFinish();
    }

    public interface TimeCountCallBack {
        void _onTick(long millisUntilFinished);

        void _onFinish();
    }
}
