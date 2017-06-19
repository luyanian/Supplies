package cn.soft_x.supplies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.application.SuppliesApplication;
import cn.soft_x.supplies.model.MessageDetailModel;

/**
 * Created by Administrator on 2016-12-06.
 */
public class MessageDetailsAdapter extends BaseAdapter {

    private List<MessageDetailModel.ListBean> mData;

    public MessageDetailsAdapter(List<MessageDetailModel.ListBean> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public MessageDetailModel.ListBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(SuppliesApplication.getAppContext()).inflate(R.layout.item_msg_details, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MessageDetailModel.ListBean item = getItem(position);
        viewHolder.itemMsgDetailsContent.setText(item.getCONTENT());
        viewHolder.itemMsgDetailsTime.setText(item.getTimeStr());
        Logger.i("MessageDetailsAdapter  是否已读->" + item.getRead());
        if (item.getRead() == 0) {
            viewHolder.dot.setVisibility(View.VISIBLE);
        } else {
            viewHolder.dot.setVisibility(View.GONE);
        }
        switch (item.getXXDL()) {
            case 1:
                viewHolder.itemMsgDetailsIv.setBackgroundResource(R.drawable.bg_msg_yellow);
                switch (item.getXXLX()) {
                    case 1:
                        viewHolder.itemMsgDetailsIv.setImageResource(R.drawable.icon_shiqimai);
                        break;
                    case 2:
                        viewHolder.itemMsgDetailsIv.setImageResource(R.drawable.icon_diaohuosahng);
                        break;
                    case 3:
                        viewHolder.itemMsgDetailsIv.setImageResource(R.drawable.icon_chaijieqiye);
                        break;
                    case 4:
                        viewHolder.itemMsgDetailsIv.setImageResource(R.drawable.icon_shiqimai);
                        break;
                }
                break;
            case 3:
                viewHolder.itemMsgDetailsIv.setBackgroundResource(R.drawable.bg_msg_yellow);
                viewHolder.itemMsgDetailsIv.setImageResource(R.drawable.icon_xitong);
                break;
            case 2:
                switch (item.getXXLX()) {
                    case 1:
                        viewHolder.itemMsgDetailsIv.setBackgroundResource(R.drawable.bg_msg_yellow);
                        viewHolder.itemMsgDetailsIv.setImageResource(R.drawable.icon_gonghuoxiaoxi);
                        break;
                    case 2:
                        viewHolder.itemMsgDetailsIv.setImageResource(R.drawable.icon_xiugaishijian);
                        viewHolder.itemMsgDetailsIv.setBackgroundResource(R.drawable.bg_msg_black);
                        break;
                    case 3:
                        viewHolder.itemMsgDetailsIv.setBackgroundResource(R.drawable.bg_msg_yellow);
                        viewHolder.itemMsgDetailsIv.setImageResource(R.drawable.icon_daochang);
                        break;
                    case 4:
                        viewHolder.itemMsgDetailsIv.setImageResource(R.drawable.icon_yanzhi);
                        viewHolder.itemMsgDetailsIv.setBackgroundResource(R.drawable.bg_msg_black);
                        break;
                    case 5:
                        viewHolder.itemMsgDetailsIv.setBackgroundResource(R.drawable.bg_msg_yellow);
                        viewHolder.itemMsgDetailsIv.setImageResource(R.drawable.icon_jiesuan);
                        break;
                    case 6:
                        viewHolder.itemMsgDetailsIv.setImageResource(R.drawable.icon_yichang);
                        viewHolder.itemMsgDetailsIv.setBackgroundResource(R.drawable.bg_msg_black);
                        break;
                    case 7:
                        break;
                    case 8:
                        break;
                }
                break;
        }


        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.item_msg_details_iv)
        ImageView itemMsgDetailsIv;
        @BindView(R.id.item_msg_details_content)
        TextView itemMsgDetailsContent;
        @BindView(R.id.item_msg_details_chakan)
        TextView itemMsgDetailsChakan;
        @BindView(R.id.item_msg_details_time)
        TextView itemMsgDetailsTime;
        @BindView(R.id.item_msg_details_dot)
        ImageView dot;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
