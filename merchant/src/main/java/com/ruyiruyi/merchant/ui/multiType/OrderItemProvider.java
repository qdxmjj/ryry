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
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.bean.OrderItemBean;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.activity.OrderConfirmFirstChangeActivity;
import com.ruyiruyi.merchant.ui.activity.OrderConfirmFreeChangeActivity;
import com.ruyiruyi.merchant.ui.activity.OrderConfirmTireRepairActivity;
import com.ruyiruyi.merchant.ui.activity.PublicOrderInfoActivity;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class OrderItemProvider extends ItemViewProvider<OrderItemBean, OrderItemProvider.ViewHolder> {
    private Context context;
    private String TAG = OrderItemProvider.class.getSimpleName();
    private String statusStr;

    public OrderItemProvider(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_order, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final OrderItemBean orderItemBean) {

        holder.tv_ordertitle.setText(orderItemBean.getTitle());
        holder.tv_orderbianhao.setText(orderItemBean.getBianhao());
        holder.tv_orderprice.setText(orderItemBean.getPrice());
        switch (orderItemBean.getStatus()) {//orderState:订单状态(): 1 交易完成 2 待收货 3 待商家确认服务 4 作废 5 待发货 6 待车主确认服务 7 待评价 8 待支付
            case "5":
                statusStr = "待发货";
                holder.tv_orderstatus.setText(statusStr);
                holder.tv_orderstatus.setBackgroundResource(R.drawable.login_code_button);
                break;
            case "2":
                statusStr = "待收货";
                holder.tv_orderstatus.setText(statusStr);
                holder.tv_orderstatus.setBackgroundResource(R.drawable.login_code_button);
                break;
            case "3"://3 待商家确认服务
                statusStr = "待服务";
                holder.tv_orderstatus.setText(statusStr);
                holder.tv_orderstatus.setBackgroundResource(R.drawable.login_code_button);
                break;
            case "6"://6 待车主确认服务
                statusStr = "待服务";
                holder.tv_orderstatus.setText(statusStr);
                holder.tv_orderstatus.setBackgroundResource(R.drawable.login_code_button);
                break;
            case "1":
                statusStr = "已完成";
                holder.tv_orderstatus.setText(statusStr);
                holder.tv_orderstatus.setBackgroundResource(R.drawable.login_code_button_huise);
                break;
            case "7":
                statusStr = "待评价";
                holder.tv_orderstatus.setText(statusStr);
                holder.tv_orderstatus.setBackgroundResource(R.drawable.login_code_button);
                break;
        }

        Glide.with(context)
                .load(orderItemBean.getImgUrl())
                .into(holder.img_order);


        RxViewAction.clickNoDouble(holder.order_item_fl).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                   /*orderType:  1:普通商品购买订单 2:首次更换订单 3:免费再换订单 4:轮胎修补订单*/
                /*orderState:订单状态(): 1 交易完成 2 待收货 3 待商家确认服务 4 作废 5 待发货 6 待车主确认服务 7 待评价 8 待支付*/

                if (orderItemBean.getOrderType().equals("2") && orderItemBean.getStatus().equals("3")) {//(2:首次更换订单  3 待商家确认服务  1 非补差)
                    Intent intent = new Intent(context, OrderConfirmFirstChangeActivity.class);
                    intent.putExtra("orderNo", orderItemBean.getBianhao());
                    intent.putExtra("orderType", orderItemBean.getOrderType());
                    context.startActivity(intent);
                } else if (orderItemBean.getOrderType().equals("3") && orderItemBean.getStatus().equals("3") && (orderItemBean.getOrderStage().equals("1") || orderItemBean.getOrderStage().equals("3"))) {//(3:免费再换订单  3 待商家确认服务  1 非补差)
                    Intent intent = new Intent(context, OrderConfirmFreeChangeActivity.class);
                    intent.putExtra("orderNo", orderItemBean.getBianhao());
                    intent.putExtra("orderType", orderItemBean.getOrderType());
                    intent.putExtra("orderStage", orderItemBean.getOrderStage());
                    context.startActivity(intent);
                } else if (orderItemBean.getOrderType().equals("4") && orderItemBean.getStatus().equals("3")) {//(4:轮胎修补订单  3 待商家确认服务  1 非补差)
                    Intent intent = new Intent(context, OrderConfirmTireRepairActivity.class);
                    intent.putExtra("orderNo", orderItemBean.getBianhao());
                    intent.putExtra("orderType", orderItemBean.getOrderType());
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, PublicOrderInfoActivity.class);//其余各订单orderType各状态orderState均复用此页面(
                    Bundle bundle = new Bundle();
                    bundle.putString("orderNo", orderItemBean.getBianhao());
                    bundle.putString("orderType", orderItemBean.getOrderType());
                    bundle.putString("orderState", orderItemBean.getStatus());
                    bundle.putString("storeId", new DbConfig().getId() + "");
                    bundle.putString("whereIn", "MyOrderItem");
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_ordertitle;
        private final TextView tv_orderbianhao;
        private final TextView tv_orderstatus;
        private final TextView tv_orderprice;
        private final ImageView img_order;
        private final FrameLayout order_item_fl;

        ViewHolder(View itemView) {
            super(itemView);
            tv_ordertitle = ((TextView) itemView.findViewById(R.id.tv_ordertitle));
            tv_orderbianhao = ((TextView) itemView.findViewById(R.id.tv_orderbianhao));
            tv_orderprice = ((TextView) itemView.findViewById(R.id.tv_orderprice));
            tv_orderstatus = ((TextView) itemView.findViewById(R.id.tv_orderstatus));
            img_order = ((ImageView) itemView.findViewById(R.id.img_order));
            order_item_fl = ((FrameLayout) itemView.findViewById(R.id.order_item_fl));
        }
    }

}