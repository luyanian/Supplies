package cn.soft_x.supplies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.maverick.view.NoScrollListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.application.SuppliesApplication;
import cn.soft_x.supplies.model.TruckItemModel;
import cn.soft_x.supplies.model.TruckModel;

/**
 * Created by Administrator on 2016-12-05.
 */
public class TruckAdapter extends BaseAdapter implements TruckItemAdapter.CheckedForAllListener {

    private List<TruckModel.DataBean> dataBeen;
    private Context context;

    public TruckAdapter(Context context,List<TruckModel.DataBean> dataBeen) {
        this.context = context;
        this.dataBeen = dataBeen;
    }

    @Override
    public int getCount() {
        return dataBeen == null ? 0 : dataBeen.size();
    }

    @Override
    public TruckModel.DataBean getItem(int position) {
        return dataBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(SuppliesApplication.getAppContext()).inflate(R.layout.item_truck, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TruckModel.DataBean item = dataBeen.get(position);
        TruckItemAdapter adapter = new TruckItemAdapter(item.getJymss(),item.getId(),context,item.getList(), position, this);
        holder.itemTruckListView.setAdapter(adapter);
        holder.itemTruckCompany.setText(item.getCjqymc());
        holder.itemTruckCheck.setOnClickListener(new OnTruckOnClickListener(position));
        if (item.isChecked()) {
            if (!notAll) {
                for (TruckItemModel m : item.getList()) {
                    m.setChecked(true);
                }
            }
            adapter.notifyDataSetChanged();
            holder.itemTruckCheck.setBackgroundResource(R.drawable.bg_truck_2);
        } else if (!item.isChecked()) {
            if (!notAll) {
                for (TruckItemModel m : item.getList()) {
                    m.setChecked(false);
                }
            }
            adapter.notifyDataSetChanged();
            holder.itemTruckCheck.setBackgroundResource(R.drawable.bg_truck_1);
        }
        return convertView;
    }


    boolean notAll = false;

    /**
     * 怎么在for循环里面得到全部
     *
     * @param position
     */
    @Override
    public void OnCheckedForALLListener(int position) {
        for (TruckItemModel m : dataBeen.get(position).getList()) {
            if (!m.isChecked()) {
                notAll = true;
                dataBeen.get(position).setChecked(false);
                this.notifyDataSetChanged();
                return;
            }
        }
        notAll = false;
        dataBeen.get(position).setChecked(true);
        //        for (int i = 0; i < dataBeen.get(position).getList().size(); i++) {
        //            TruckItemModel model = dataBeen.get(position).getList().get(i);
        //            model.isChecked()
        //        }

        this.notifyDataSetChanged();
    }


    class OnTruckOnClickListener implements View.OnClickListener {

        private int pos;

        public OnTruckOnClickListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            dataBeen.get(pos).setChecked(!dataBeen.get(pos).isChecked());
            notAll = false;
            TruckAdapter.this.notifyDataSetChanged();
        }
    }

    static class ViewHolder {
        @BindView(R.id.item_truck_check)
        TextView itemTruckCheck;
        @BindView(R.id.item_truck_company)
        TextView itemTruckCompany;
        @BindView(R.id.item_truck_listView)
        NoScrollListView itemTruckListView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
