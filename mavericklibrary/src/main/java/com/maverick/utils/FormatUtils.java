package com.maverick.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016-12-22.
 */
public class FormatUtils {
    /**
     * 检查手机电话号码
     *
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        Pattern pattern = Pattern.compile("^((13\\d{9}$)|(15[0,1,2,3,5,6,7,8,9]\\d{8}$)|(18[0,1,2,3,4,5,6,7,8,9]\\d{8}$)|(14[5,7]\\d{8}$)|(17[0,1,3,5,6,7,8]\\d{8}$))");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    /**
     * 检查身份证号
     *
     * @param idNum
     * @return
     */
    public static boolean checkIDCard(String idNum) {
        Pattern idNumPattern = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|x|X)$");
        //通过Pattern获得Matcher
        Matcher idNumMatcher = idNumPattern.matcher(idNum);
        return idNumMatcher.matches();
    }
}
