package cn.soft_x.supplies.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import cn.soft_x.supplies.R;
import cn.soft_x.supplies.http.HttpUrl;
import cn.soft_x.supplies.model.RoleModel;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ryon on 2017/6/8.
 */

public class RolesListAdapter extends BaseAdapter {

    private Context context;
    private List<RoleModel.DataBean> list;

    public RolesListAdapter(Context context, List<RoleModel.DataBean> list) {

        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView==null||convertView.getTag()==null){
            convertView = View.inflate(context, R.layout.activity_business_type_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RoleModel.DataBean dataBean = list.get(position);
        viewHolder.imgType.setImageURI(Uri.parse(HttpUrl.API_HOST+dataBean.getTubiao()));
        viewHolder.tvType.setText(dataBean.getRoleName());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.img_type)
        SimpleDraweeView imgType;
        @BindView(R.id.tv_type)
        TextView tvType;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
