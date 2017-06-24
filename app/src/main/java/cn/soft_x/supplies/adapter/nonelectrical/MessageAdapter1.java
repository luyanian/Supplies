package cn.soft_x.supplies.adapter.nonelectrical;

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
import cn.soft_x.supplies.model.nonelectrical.MessageModel1;

/**
 * Created by Administrator on 2016-12-06.
 */
public class MessageAdapter1 extends BaseAdapter {

    private List<MessageModel1.ListBean> mData;

    public MessageAdapter1(List<MessageModel1.ListBean> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public MessageModel1.ListBean getItem(int position) {
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
            convertView = LayoutInflater.from(SuppliesApplication.getAppContext()).inflate(R.layout.item_msg, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MessageModel1.ListBean item = getItem(position);
        viewHolder.itemMsgContent.setText(item.getContent());
        viewHolder.itemMsgTime.setText(item.getTimeStr());
        Logger.i("MessageAdapter  是否已读->" + item.getRead());
        if (item.getRead() == 0) {
            viewHolder.dot.setVisibility(View.VISIBLE);
        } else {
            viewHolder.dot.setVisibility(View.GONE);
        }
        Logger.i("long time->" + item.getTime() + ", str time->" + item.getTimeStr());
        /**
         * 消息类型 (
         * 1:采购信息 [
         *      1:拾起卖消息，2:调货商，3:拆解企业，4:供货商消息]
         * 2:订单消息[
         *      1.供货消息，2.修改预计到厂时间3.到厂时间，4.验质消息，5结算消息，6,异常消息,
         *      7,发车消息,8发布供货消息]
         * 3:系统消息[
         *      1，修改密码，2修改手机号]
         * )
         */
        switch (item.getXxdl()) {
            case 1:
                viewHolder.itemMsgIv.setBackgroundResource(R.drawable.bg_msg_yellow);
                switch (item.getXxlx()) {
                    case 1:
                        viewHolder.itemMsgIv.setImageResource(R.drawable.icon_shiqimai);
                        break;
                    case 2:
                        viewHolder.itemMsgIv.setImageResource(R.drawable.icon_diaohuosahng);
                        break;
                    case 3:
                        viewHolder.itemMsgIv.setImageResource(R.drawable.icon_chaijieqiye);
                        break;
                    case 4:
                        viewHolder.itemMsgIv.setImageResource(R.drawable.icon_shiqimai);
                        break;
                }
                break;
            case 3:
                viewHolder.itemMsgIv.setBackgroundResource(R.drawable.bg_msg_yellow);
                viewHolder.itemMsgIv.setImageResource(R.drawable.icon_xitong);
                break;
            case 2:
                switch (item.getXxlx()) {
                    case 1:
                        viewHolder.itemMsgIv.setBackgroundResource(R.drawable.bg_msg_yellow);
                        viewHolder.itemMsgIv.setImageResource(R.drawable.icon_gonghuoxiaoxi);
                        break;
                    case 2:
                        viewHolder.itemMsgIv.setImageResource(R.drawable.icon_xiugaishijian);
                        viewHolder.itemMsgIv.setBackgroundResource(R.drawable.bg_msg_black);
                        break;
                    case 3:
                        viewHolder.itemMsgIv.setBackgroundResource(R.drawable.bg_msg_yellow);
                        viewHolder.itemMsgIv.setImageResource(R.drawable.icon_daochang);
                        break;
                    case 4:
                        viewHolder.itemMsgIv.setImageResource(R.drawable.icon_yanzhi);
                        viewHolder.itemMsgIv.setBackgroundResource(R.drawable.bg_msg_black);
                        break;
                    case 5:
                        viewHolder.itemMsgIv.setBackgroundResource(R.drawable.bg_msg_yellow);
                        viewHolder.itemMsgIv.setImageResource(R.drawable.icon_jiesuan);
                        break;
                    case 6:
                        viewHolder.itemMsgIv.setImageResource(R.drawable.icon_yichang);
                        viewHolder.itemMsgIv.setBackgroundResource(R.drawable.bg_msg_black);
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
        @BindView(R.id.item_msg_iv)
        ImageView itemMsgIv;
        @BindView(R.id.item_msg_content)
        TextView itemMsgContent;
        @BindView(R.id.item_msg_time)
        TextView itemMsgTime;
        @BindView(R.id.item_msg_dot)
        ImageView dot;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
