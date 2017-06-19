package cn.soft_x.supplies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.activity.WebViewActivity;
import cn.soft_x.supplies.application.SuppliesApplication;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.model.NearModel;
import cn.soft_x.supplies.utils.Constant;

/**
 * Created by Administrator on 2016-11-24.
 */
public class NearCompanyAdapter extends BaseAdapter {

    private List<NearModel> mData;
    private Context context;

    public NearCompanyAdapter(Context context,List<NearModel> mData) {
        this.context = context;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public NearModel getItem(int position) {
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
            convertView = LayoutInflater.from(SuppliesApplication.getAppContext()).inflate(R.layout.item_near, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        NearModel item = getItem(position);
        viewHolder.itemNearBtn.setOnClickListener(new OnButtonClickListener(position));
        viewHolder.itemNearTvAddress.setText(item.getAddress());
        viewHolder.itemNearTvName.setText(item.getOrg_name());
        viewHolder.itemNearIv.setImageURI(Uri.parse(item.getOrg_pic()));
        return convertView;
    }

    class OnButtonClickListener implements View.OnClickListener{

        private int pos;

        public OnButtonClickListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SuppliesApplication.getAppContext(), WebViewActivity.class);
            intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST+"/s/page/chaijieqiye-xq.html");
            intent.putExtra(Constant.WEB_NEAR_ID,mData.get(pos).getOrg_id());
            context.startActivity(intent);
        }
    }
    static class ViewHolder {
        @BindView(R.id.item_near_iv)
        SimpleDraweeView itemNearIv;
        @BindView(R.id.item_near_tv_name)
        TextView itemNearTvName;
        @BindView(R.id.item_near_tv_address)
        TextView itemNearTvAddress;
        @BindView(R.id.item_near_btn)
        Button itemNearBtn;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }

}
