package cn.soft_x.supplies.view;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.soft_x.supplies.R;


/**
 * ImageView创建工厂
 */
public class ViewFactory {

	/**
	 * 获取ImageView视图的同时加载显示url
	 *
	 */
	public static ImageView getImageView(Context context, String url) {
		SimpleDraweeView imageView = (SimpleDraweeView)LayoutInflater.from(context).inflate(
				R.layout.view_banner, null);
		// 图片地址
//		ImageLoader.getInstance().displayImage(url, imageView);
		imageView.setImageURI(Uri.parse(url));
		return imageView;
	}
}
