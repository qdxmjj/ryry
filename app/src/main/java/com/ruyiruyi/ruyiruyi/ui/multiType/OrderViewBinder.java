package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.ruyiruyi.R;

import me.drakeet.multitype.ItemViewProvider;

public class OrderViewBinder extends ItemViewProvider<Order, OrderViewBinder.ViewHolder> {
    public Context context;

    public OrderViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_order, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Order order) {
        Glide.with(context).load(order.getOrderImage()).into(holder.orderImageView);
        holder.orderTitleText.setText(order.getOrderName());
        holder.orderPriceText.setText("￥"+order.getOrderPrice());
        holder.orderNoText.setText("订单编号： " + order.getOrderNo());
        if (order.getOrderType().equals("0")){//轮胎订单状态(orderType:0) :1 已安装 2 待服务 3 支付成功 4 支付失败 5 待支付 6 已退货
            if (order.getOrderState().equals("1")) {
                holder.orderTypeText.setText("已安装");
            }else if (order.getOrderState().equals("2")){
                holder.orderTypeText.setText("待服务");
            }else if (order.getOrderState().equals("3")){
                holder.orderTypeText.setText("交易完成");
            }else if (order.getOrderState().equals("4")){
                holder.orderTypeText.setText("支付失败");
            }else if (order.getOrderState().equals("5")){
                holder.orderTypeText.setText("待支付");
            }else if (order.getOrderState().equals("6")){
                holder.orderTypeText.setText("已退货");
            }
        }else {//订单状态(orderType:1): 1 交易完成 2 待收货 3 待商家确认服务 4 作废 5 待发货 6 待车主确认服务 7 待评价 8 待支付
            if (order.getOrderState().equals("1")) {
                holder.orderTypeText.setText("交易完成");
            }else if (order.getOrderState().equals("2")){
                holder.orderTypeText.setText("待收货");
            }else if (order.getOrderState().equals("3")){
                holder.orderTypeText.setText("待商家确认服务");
            }else if (order.getOrderState().equals("4")){
                holder.orderTypeText.setText("作废");
            }else if (order.getOrderState().equals("5")){
                holder.orderTypeText.setText("待发货");
            }else if (order.getOrderState().equals("6")){
                holder.orderTypeText.setText("待车主确认服务");
            }else if (order.getOrderState().equals("7")){
                holder.orderTypeText.setText("待评价");
            }else if (order.getOrderState().equals("8")){
                holder.orderTypeText.setText("待支付");
            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView orderImageView;
        private final TextView orderTitleText;
        private final TextView orderNoText;
        private final TextView orderPriceText;
        private final TextView orderTypeText;

        ViewHolder(View itemView) {
            super(itemView);
            orderImageView = ((ImageView) itemView.findViewById(R.id.order_image));
            orderTitleText = ((TextView) itemView.findViewById(R.id.order_title_text));
            orderNoText = ((TextView) itemView.findViewById(R.id.order_no_text));
            orderPriceText = ((TextView) itemView.findViewById(R.id.order_price_text));
            orderTypeText = ((TextView) itemView.findViewById(R.id.order_type_button));

        }
    }
}