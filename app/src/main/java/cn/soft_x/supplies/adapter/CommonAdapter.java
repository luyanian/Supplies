package cn.soft_x.supplies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.soft_x.supplies.R;
import cn.soft_x.supplies.application.SuppliesApplication;
import cn.soft_x.supplies.model.CommonModel;

/**
 * Created by Administrator on 2016-11-24.
 */
public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.ViewHolder> {

    private List<CommonModel.FaqlistsBean> mData;

    public CommonAdapter(List<CommonModel.FaqlistsBean> data) {
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(SuppliesApplication.getAppContext()).inflate(R.layout.item_common, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommonModel.FaqlistsBean item = mData.get(position);
        holder.itemCommonContent.setText(item.getAnswer());
        holder.itemCommonTitle.setText(item.getQuestion());

        if (item.isClick()) {
            holder.itemCommonContent.setVisibility(View.VISIBLE);
        } else {
            holder.itemCommonContent.setVisibility(View.GONE);
        }

        holder.itemCommonTitle.setOnClickListener(new ItemTitleOnClick(position));

    }


    class ItemTitleOnClick implements View.OnClickListener {
        private int pos;

        public ItemTitleOnClick(int position) {
            this.pos = position;
        }

        @Override
        public void onClick(View v) {
            if (mData.get(pos).isClick()) {
                mData.get(pos).setClick(false);
            } else
                mData.get(pos).setClick(true);
            Logger.i(mData.get(pos).isClick()+"<-"+pos);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size() == 0 ? 0 : mData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_common_title)
        TextView itemCommonTitle;
        @BindView(R.id.item_common_content)
        TextView itemCommonContent;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
