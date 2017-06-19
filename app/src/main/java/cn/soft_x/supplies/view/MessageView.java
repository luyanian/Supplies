package cn.soft_x.supplies.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.soft_x.supplies.R;


/**
 * Created by Administrator on 2016-11-22.
 */
public class MessageView extends RelativeLayout {

    private ImageView dot;
    private RelativeLayout root;
    private TextView msgName;

    public MessageView(Context context) {
        super(context);
        init(context);
    }

    public MessageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.message_view, this, true);
        dot = (ImageView) findViewById(R.id.message_dot);
        root = (RelativeLayout) findViewById(R.id.message_root);
        msgName = (TextView) findViewById(R.id.message_tv);
    }

    /**
     * 设置字符串
     * @param text
     */
    public void setMsgTv(CharSequence text) {
        msgName.setText(text);
    }

    public void setDotVisibility(boolean isVisibility) {
        if (isVisibility) {
            dot.setVisibility(VISIBLE);
        } else {
            dot.setVisibility(GONE);
        }
    }

    public boolean isDotVisibility() {
//        if (dot.getVisibility() == VISIBLE)
//            return true;
//        else
//            return false;
        return dot.getVisibility() == VISIBLE;
    }

    public void setRootBackground(boolean isVisibility) {
        if (isVisibility) {
            root.setBackgroundResource(R.drawable.bg_message_view);
            msgName.setTextColor(getResources().getColor(R.color.color_app_theme));
        } else {
            root.setBackground(null);
            msgName.setTextColor(getResources().getColor(R.color.color_message_view_gray));
        }
    }
}
