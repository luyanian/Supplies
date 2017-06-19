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
import cn.soft_x.supplies.model.InvitationDetailsModel;

/**
 * Created by Administrator on 2016-12-09.
 */
public class InvitationDetailsAdapter extends BaseAdapter {

    private List<InvitationDetailsModel.YqlistBean> mData;
    private int mType;

    public InvitationDetailsAdapter(List<InvitationDetailsModel.YqlistBean> mData,int type) {
        this.mData = mData;
        mType = type;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public InvitationDetailsModel.YqlistBean getItem(int position) {
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
            convertView = LayoutInflater.from(SuppliesApplication.getAppContext()).inflate(R.layout.item_invitation_details, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //        （0-拆解企业、1-拾起卖、2-调货商）
        switch (mType) {
            case 0:
                viewHolder.itemInvitationDetailsIv.setImageResource(R.drawable.icon_yaoqinghan_chaijieqiye);
                break;
            case 1:
                viewHolder.itemInvitationDetailsIv.setImageResource(R.drawable.icon_yaoqinghan_shiqimai);
                break;
            case 2:
                viewHolder.itemInvitationDetailsIv.setImageResource(R.drawable.icon_yaoqinghan_diaohuoshang);
                break;
        }
        InvitationDetailsModel.YqlistBean bean = getItem(position);
        viewHolder.itemInvitationDetailsContent.setText(bean.getCONTENT());
        viewHolder.itemInvitationDetailsTime.setText(bean.getTIME());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.item_invitation_details_iv)
        ImageView itemInvitationDetailsIv;
        @BindView(R.id.item_invitation_details_content)
        TextView itemInvitationDetailsContent;
        @BindView(R.id.item_invitation_details_chakan)
        TextView itemInvitationDetailsChakan;
        @BindView(R.id.item_invitation_details_time)
        TextView itemInvitationDetailsTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
