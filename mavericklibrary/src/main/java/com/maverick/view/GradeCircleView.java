package com.maverick.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.maverick.R;


/**
 * 带等级的圆形头像
 * Created by Administrator on 2016/7/25.
 */
public class GradeCircleView extends RelativeLayout {
    private CircleImageView circleImageView;
    private ImageView little_iv;


    public GradeCircleView(Context context) {
        super(context);
        init(context);
    }

    public GradeCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public GradeCircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.head,this,true);
        circleImageView = (CircleImageView) findViewById(R.id.circle_head_img);
        little_iv = (ImageView) findViewById(R.id.circle_head_img_grad);
    }

    public void setGrade(float grade){
        circleImageView.setCircumference(grade);
    }

    public void setGradeImg(int resId){
        little_iv.setVisibility(View.VISIBLE);
        little_iv.setImageResource(resId);
    }

    public void setHeadImg(int resId){
        circleImageView.setImageResource(resId);
    }

    public void setHeadImg(Bitmap bitmap){
        circleImageView.setImageBitmap(bitmap);
    }

    public void hiddGradeImg(){
        little_iv.setVisibility(View.GONE);
    }

    public void setGradeColor(int color){
        circleImageView.setCircumferenceColor(color);
    }

    public CircleImageView getCircleImageView() {
        return circleImageView;
    }

    public ImageView getLittle_iv() {
        return little_iv;
    }
}
