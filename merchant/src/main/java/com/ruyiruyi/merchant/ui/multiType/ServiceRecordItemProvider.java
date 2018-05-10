package com.ruyiruyi.merchant.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.OrderItemBean;
import com.ruyiruyi.merchant.bean.ServiceRecordBean;

import me.drakeet.multitype.ItemViewProvider;

public class ServiceRecordItemProvider extends ItemViewProvider<ServiceRecordBean, ServiceRecordItemProvider.ViewHolder> {
    private Context context;
    private String TAG = ServiceRecordItemProvider.class.getSimpleName();

    public ServiceRecordItemProvider(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_service_record, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final ServiceRecordBean serviceRecordBean) {

        holder.service_xm.setText(serviceRecordBean.getService_xm());
        holder.service_store.setText(serviceRecordBean.getService_store());
        holder.service_time.setText(serviceRecordBean.getService_time());
        switch (serviceRecordBean.getType()) {
            case "mfxb":
                holder.service_img.setImageResource(R.drawable.ic_xiubu);
                holder.ll_horizontal.setVisibility(View.GONE);
                holder.ll_vertical.setVisibility(View.VISIBLE);
                holder.service_bianma_aa.setText(serviceRecordBean.getService_luntai_a());
                holder.service_bianma_bb.setText(serviceRecordBean.getService_luntai_b());
                holder.service_bianma_cc.setText(serviceRecordBean.getService_luntai_c());
                holder.service_bianma_dd.setText(serviceRecordBean.getService_luntai_d());
                break;
            case "scltgh":
                holder.service_img.setImageResource(R.drawable.ic_genghuan);
                holder.ll_horizontal.setVisibility(View.VISIBLE);
                holder.ll_vertical.setVisibility(View.GONE);
                holder.service_bianma_a.setText(serviceRecordBean.getService_luntai_a());
                holder.service_bianma_b.setText(serviceRecordBean.getService_luntai_b());
                holder.service_bianma_c.setText(serviceRecordBean.getService_luntai_c());
                holder.service_bianma_d.setText(serviceRecordBean.getService_luntai_d());
                break;
            case "ltmfgh":
                holder.service_img.setImageResource(R.drawable.ic_mianfei);
                holder.ll_horizontal.setVisibility(View.VISIBLE);
                holder.ll_vertical.setVisibility(View.GONE);
                holder.service_bianma_a.setText(serviceRecordBean.getService_luntai_a());
                holder.service_bianma_b.setText(serviceRecordBean.getService_luntai_b());
                holder.service_bianma_c.setText(serviceRecordBean.getService_luntai_c());
                holder.service_bianma_d.setText(serviceRecordBean.getService_luntai_d());
                break;
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView service_img;
        private final TextView service_xm;
        private final TextView service_store;
        private final TextView service_time;
        private final TextView service_bianma_a;
        private final TextView service_bianma_b;
        private final TextView service_bianma_c;
        private final TextView service_bianma_d;
        private final TextView service_bianma_aa;
        private final TextView service_bianma_bb;
        private final TextView service_bianma_cc;
        private final TextView service_bianma_dd;
        private final LinearLayout ll_horizontal;
        private final LinearLayout ll_vertical;

        ViewHolder(View itemView) {
            super(itemView);
            service_img = ((ImageView) itemView.findViewById(R.id.service_img));
            service_xm = ((TextView) itemView.findViewById(R.id.service_xm));
            service_store = ((TextView) itemView.findViewById(R.id.service_store));
            service_time = ((TextView) itemView.findViewById(R.id.service_time));
            service_bianma_a = ((TextView) itemView.findViewById(R.id.service_bianma_a));
            service_bianma_b = ((TextView) itemView.findViewById(R.id.service_bianma_b));
            service_bianma_c = ((TextView) itemView.findViewById(R.id.service_bianma_c));
            service_bianma_d = ((TextView) itemView.findViewById(R.id.service_bianma_d));
            service_bianma_aa = ((TextView) itemView.findViewById(R.id.service_bianma_aa));
            service_bianma_bb = ((TextView) itemView.findViewById(R.id.service_bianma_bb));
            service_bianma_cc = ((TextView) itemView.findViewById(R.id.service_bianma_cc));
            service_bianma_dd = ((TextView) itemView.findViewById(R.id.service_bianma_dd));
            ll_horizontal = ((LinearLayout) itemView.findViewById(R.id.ll_horizontal));
            ll_vertical = ((LinearLayout) itemView.findViewById(R.id.ll_vertical));
        }
    }

}