package com.ruyiruyi.merchant.ui.multiType;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.activity.PublicOrderInfoActivity;
import com.ruyiruyi.merchant.ui.multiType.modle.Dianpu;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class DianpuItemViewProvider extends ItemViewProvider<Dianpu, DianpuItemViewProvider.ViewHolder> {
    private Context context;
    private String stateStr;

    public DianpuItemViewProvider(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_rlv_dianpu, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Dianpu dianpu) {

        switch (dianpu.getOrderServcieTypeName_First()) {
            case "汽车保养":
                holder.dianpu_rlv_img.setImageResource(R.drawable.ic_baoyang);
                break;
            case "美容清洗":
                holder.dianpu_rlv_img.setImageResource(R.drawable.ic_qingxi);
                break;
            case "安装":
                holder.dianpu_rlv_img.setImageResource(R.drawable.ic_anzhuang);
                break;
            case "轮胎服务":
                holder.dianpu_rlv_img.setImageResource(R.drawable.ic_luntai);
                break;
        }
        holder.dianpu_tv_type.setText(dianpu.getOrderServcieTypeName());
        String timestampToStringAll = new UtilsRY().getTimestampToStringAll(dianpu.getOrderTime());
        holder.dianpu_tv_time.setText(timestampToStringAll);

        // orderState: 1 交易完成 2 待收货 3 待商家确认服务 4 作废 5 待发货 6 待车主确认服务 7 待评价 8 待支付
        switch (dianpu.getOrderState()) {
            case "1":
                stateStr = "已完成";
                holder.dianpu_tv_state.setText(stateStr);
                holder.dianpu_tv_money.setText("+" + dianpu.getOrderPrice());
                holder.dianpu_tv_money.setTextColor(context.getResources().getColor(R.color.theme_primary));
                break;
            case "2":
                stateStr = "待收货";
                holder.dianpu_tv_state.setText(stateStr);
                holder.dianpu_tv_money.setText(dianpu.getOrderPrice());
                holder.dianpu_tv_money.setTextColor(context.getResources().getColor(R.color.c7));
                break;
            case "3":
                stateStr = "待服务";
                holder.dianpu_tv_state.setText(stateStr);
                holder.dianpu_tv_money.setText(dianpu.getOrderPrice());
                holder.dianpu_tv_money.setTextColor(context.getResources().getColor(R.color.c7));
                break;
            case "5":
                stateStr = "待发货";
                holder.dianpu_tv_state.setText(stateStr);
                holder.dianpu_tv_money.setText(dianpu.getOrderPrice());
                holder.dianpu_tv_money.setTextColor(context.getResources().getColor(R.color.c7));
                break;
            case "6":
                stateStr = "待车主确认服务";
                holder.dianpu_tv_state.setText(stateStr);
                holder.dianpu_tv_money.setText(dianpu.getOrderPrice());
                holder.dianpu_tv_money.setTextColor(context.getResources().getColor(R.color.c7));
                break;
            case "7":
                stateStr = "待评价";
                holder.dianpu_tv_state.setText(stateStr);
                holder.dianpu_tv_money.setText("+" + dianpu.getOrderPrice());
                holder.dianpu_tv_money.setTextColor(context.getResources().getColor(R.color.theme_primary));
                break;
            case "8":
                stateStr = "待支付";
                holder.dianpu_tv_state.setText(stateStr);
                holder.dianpu_tv_money.setText(dianpu.getOrderPrice());
                holder.dianpu_tv_money.setTextColor(context.getResources().getColor(R.color.c7));
                break;
        }
        RxViewAction.clickNoDouble(holder.rl_item).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent();
                intent.setClass(context, PublicOrderInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("orderNo", dianpu.getOrderNo());
                bundle.putString("orderType", dianpu.getOrderType());
                bundle.putString("orderState", dianpu.getOrderState());
                bundle.putString("storeId", new DbConfig(context).getId() + "");
                bundle.putString("whereIn", "MainStoreItem");
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private ImageView dianpu_rlv_img;
        @NonNull
        private TextView dianpu_tv_type;
        @NonNull
        private TextView dianpu_tv_time;
        @NonNull
        private TextView dianpu_tv_money;
        @NonNull
        private TextView dianpu_tv_state;
        @NonNull
        private RelativeLayout rl_item;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dianpu_rlv_img = (ImageView) itemView.findViewById(R.id.dianpu_rlv_img);
            this.dianpu_tv_type = (TextView) itemView.findViewById(R.id.dianpu_tv_type);
            this.dianpu_tv_time = (TextView) itemView.findViewById(R.id.dianpu_tv_time);
            this.dianpu_tv_money = (TextView) itemView.findViewById(R.id.dianpu_tv_money);
            this.dianpu_tv_state = (TextView) itemView.findViewById(R.id.dianpu_tv_state);
            this.rl_item = (RelativeLayout) itemView.findViewById(R.id.rl_item);
        }
    }
}