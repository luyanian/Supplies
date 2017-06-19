package cn.soft_x.supplies.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.activity.WebViewActivity;
import cn.soft_x.supplies.application.SuppliesApplication;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.model.TruckItemModel;
import cn.soft_x.supplies.utils.Constant;

/**
 * Created by Administrator on 2016-12-05.
 */
public class TruckItemAdapter extends BaseAdapter {

    private List<TruckItemModel> data;
    private int itemIndex;
    private CheckedForAllListener listener;
    private Context context;
    private String id;
    private String jymss;

    public TruckItemAdapter(String jymss,String id,Context context,List<TruckItemModel> data, int itemIndex, CheckedForAllListener listener) {
        this.jymss = jymss;
        this.id = id;
        this.context = context;
        this.data = data;
        this.itemIndex = itemIndex;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public TruckItemModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(SuppliesApplication.getAppContext()).inflate(R.layout.item_item_truck, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TruckItemModel itemModel = data.get(position);
        holder.itemItemBianhaoTv.setText(itemModel.getCgcode());
        holder.itemItemPingzhongTv.setText(itemModel.getPzmc());
        holder.itemItemCheck.setOnClickListener(new OnTruckItemCheckedListener(position));
        holder.itemItemBtn.setOnClickListener(new OnTruckItemButtonClickListener(position));
        if (itemModel.isChecked()) {
            holder.itemItemCheck.setBackgroundResource(R.drawable.bg_truck_2);
        } else {
            holder.itemItemCheck.setBackgroundResource(R.drawable.bg_truck_1);
        }
        holder.itemItemCaigoufangTv.setText(jymss);
        return convertView;
    }

    class OnTruckItemCheckedListener implements View.OnClickListener {
        int position;

        public OnTruckItemCheckedListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            data.get(position).setChecked(!data.get(position).isChecked());
            if (listener != null) {
                listener.OnCheckedForALLListener(itemIndex);
            }
            TruckItemAdapter.this.notifyDataSetChanged();
        }
    }

    class OnTruckItemButtonClickListener implements View.OnClickListener{

        private int pos;

        public OnTruckItemButtonClickListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            Logger.i("huocheid->"+data.get(pos).getGwcid());
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra(Constant.WEB_TRUCK_ID,data.get(pos).getGwcid());
            intent.putExtra(Constant.WEB_ORDER_ID,id);
            intent.putExtra(Constant.WEB_URL, HttpUrl.API_HOST+"/s/page/huoche-ckxq.html");
            context.startActivity(intent);
        }
    }

    interface CheckedForAllListener {
        void OnCheckedForALLListener(int position);
    }

    static class ViewHolder {
        @BindView(R.id.item_item_check)
        TextView itemItemCheck;
        @BindView(R.id.item_item_iv)
        SimpleDraweeView itemItemIv;
        @BindView(R.id.item_item_bianhao)
        TextView itemItemBianhao;
        @BindView(R.id.item_item_bianhao_tv)
        TextView itemItemBianhaoTv;
        @BindView(R.id.item_item_pingzhong)
        TextView itemItemPingzhong;
        @BindView(R.id.item_item_pingzhong_tv)
        TextView itemItemPingzhongTv;
        @BindView(R.id.item_item_caigoufang)
        TextView itemItemCaigoufang;
        @BindView(R.id.item_item_caigoufang_tv)
        TextView itemItemCaigoufangTv;
        @BindView(R.id.item_item_btn)
        Button itemItemBtn;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
