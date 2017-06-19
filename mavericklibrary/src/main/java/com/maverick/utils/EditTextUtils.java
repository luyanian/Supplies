package com.maverick.utils;

import android.text.TextUtils;
import android.widget.EditText;

/**
 * Created by Administrator on 2016-08-16.
 */
public class EditTextUtils {
    /**
     * 得到EditText的文字
     *
     * @param editText
     * @return
     */
    public static String getEdText(EditText editText) {
        return editText.getText().toString().trim();
    }

    /**
     * 给EditText判空
     *
     * @param editText
     * @return
     */
    public static boolean isEmpty(EditText editText) {
        return TextUtils.isEmpty(getEdText(editText));
    }
}
