package cn.soft_x.supplies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.application.SuppliesApplication;
import cn.soft_x.supplies.model.InvitationModel;

/**
 * Created by Administrator on 2016-12-09.
 */
public class InvitationAdapter extends BaseAdapter {

    private List<InvitationModel.YqlistBean> mData;

    public InvitationAdapter(List<InvitationModel.YqlistBean> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public InvitationModel.YqlistBean getItem(int position) {
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
        InvitationModel.YqlistBean item = getItem(position);
        //        （0-拆解企业、1-拾起卖、2-调货商）
        switch (item.getYQLX()) {
            case 0:
                viewHolder.itemMsgIv.setImageResource(R.drawable.icon_yaoqinghan_chaijieqiye);
                break;
            case 1:
                viewHolder.itemMsgIv.setImageResource(R.drawable.icon_yaoqinghan_shiqimai);
                break;
            case 2:
                viewHolder.itemMsgIv.setImageResource(R.drawable.icon_yaoqinghan_diaohuoshang);
                break;
        }
        viewHolder.itemMsgContent.setText(item.getCONTENT());
        viewHolder.itemMsgTime.setText(item.getTIME());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.item_msg_iv)
        ImageView itemMsgIv;
        @BindView(R.id.item_msg_content)
        TextView itemMsgContent;
        @BindView(R.id.item_msg_time)
        TextView itemMsgTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
