package com.maverick.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Administrator on 2016-07-28.
 */
public class ToastUtil {
    private static Toast mToast;

    public static void showToast(Context context, CharSequence content) {
        if (null == mToast) {
            mToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(content);
        }
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.show();
    }

    public static void showToast(Context context, int strId) {
        if (null == mToast) {
            mToast = Toast.makeText(context, strId, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(strId);
        }
        mToast.setGravity(Gravity.CENTER,0,0);
        mToast.show();
    }
}
