package com.ruyiruyi.merchant.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.GoodsBean;
import com.ruyiruyi.merchant.bean.ServicesBean;

import org.xutils.x;

import me.drakeet.multitype.ItemViewProvider;

public class ServiceItemProvider extends ItemViewProvider<ServicesBean, ServiceItemProvider.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_services, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ServicesBean servicesBean) {
        holder.tv_service_item.setText(servicesBean.getServiceInfo());
        holder.ck_service_item.setChecked(servicesBean.getIsChecked()==1?true:false);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_service_item;
        private final CheckBox ck_service_item;
        ViewHolder(View itemView) {
            super(itemView);
            tv_service_item = ((TextView) itemView.findViewById(R.id.tv_service_item));
            ck_service_item = (CheckBox) itemView.findViewById(R.id.ck_service_item);
        }
    }
}