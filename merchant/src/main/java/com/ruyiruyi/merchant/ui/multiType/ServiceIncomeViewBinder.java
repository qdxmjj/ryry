package com.ruyiruyi.merchant.ui.multiType;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.ui.activity.PublicOrderInfoActivity;
import com.ruyiruyi.merchant.utils.CircleImageView;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2018/9/29 14:39
 */
public class ServiceIncomeViewBinder extends ItemViewProvider<ServiceIncome, ServiceIncomeViewBinder.ViewHolder> {
    private Context mContext;

    public ServiceIncomeViewBinder(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_serviceincome, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final ServiceIncome serviceIncome) {
        holder.tv_orderno.setText(serviceIncome.getOrderNo());
        holder.tv_orderprice.setText(serviceIncome.getOrderPrice() + "");
        if (serviceIncome.getOrderTypeId() == 2) {
            holder.tv_ordertype.setText("首次更换订单");
        }
        if (serviceIncome.getOrderTypeId() == 3) {
            holder.tv_ordertype.setText("免费再换订单");
        }
        if (serviceIncome.getOrderTypeId() == 4) {
            holder.tv_ordertype.setText("轮胎修补订单");
        }
        String timestampToStringAll = new UtilsRY().getTimestampToStringAll(serviceIncome.getOrderTime());
        holder.tv_ordertime.setText(timestampToStringAll);

        /*Glide.with(mContext).load(serviceIncome.getOrderImg()).into(holder.civ_orderimg);*/

        RxViewAction.clickNoDouble(holder.fl_main).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(mContext, PublicOrderInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("orderNo",serviceIncome.getOrderNo());
                bundle.putString("orderType",serviceIncome.getOrderTypeId()+"");
                bundle.putString("orderState","1");
                bundle.putString("whereIn","income");
                bundle.putString("select","0");
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_orderno;
        private final TextView tv_orderprice;
        private final CircleImageView civ_orderimg;
        private final TextView tv_ordertype;
        private final TextView tv_ordertime;
        private final FrameLayout fl_main;

        ViewHolder(View itemView) {
            super(itemView);
            tv_orderno = itemView.findViewById(R.id.tv_orderno);
            tv_orderprice = itemView.findViewById(R.id.tv_orderprice);
            civ_orderimg = itemView.findViewById(R.id.civ_orderimg);
            tv_ordertype = itemView.findViewById(R.id.tv_ordertype);
            tv_ordertime = itemView.findViewById(R.id.tv_ordertime);
            fl_main = itemView.findViewById(R.id.fl_main);
        }
    }
}
