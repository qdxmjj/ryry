package com.ruyiruyi.ruyiruyi.ui.multiType;

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
import com.ruyiruyi.ruyiruyi.R;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class OrderViewBinder extends ItemViewProvider<Order, OrderViewBinder.ViewHolder> {
    public Context context;
    public OnOrderItemClick listener;

    public void setListener(OnOrderItemClick listener) {
        this.listener = listener;
    }

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
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Order order) {
        Glide.with(context).load(order.getOrderImage()).into(holder.orderImageView);
        holder.orderTitleText.setText(order.getOrderName());

        holder.orderNoText.setText("订单编号： " + order.getOrderNo());
        holder.orderTimeText.setText("下单时间： " + order.getOrderTime());

        if (order.getOrderType().equals("2") || order.getOrderType().equals("3") || order.getOrderType().equals("4")){
            holder.orderPriceText.setVisibility(View.INVISIBLE);
        }else {
            holder.orderPriceText.setVisibility(View.VISIBLE);
            holder.orderPriceText.setText("￥"+order.getOrderPrice());
        }
        // OrderType 0:轮胎购买订单 1:普通商品购买订单 2:首次更换订单 3:免费再换订单 4:轮胎修补订单
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
            }else if (order.getOrderState().equals("7")){
                holder.orderTypeText.setText("退款中");
            }else if (order.getOrderState().equals("8")){
                holder.orderTypeText.setText("退款成功");
            }else if (order.getOrderState().equals("9")){
                holder.orderTypeText.setText("订单已取消");
            }else {
                holder.orderTypeText.setText("其他状态");
            }
        }else if (order.getOrderType().equals("8")){ //订单状态 待支付 1 待审核 2 续保成功 3  续保失败 4
            if (order.getOrderState().equals("1")) {
                holder.orderTypeText.setText("待支付");
            }else if (order.getOrderState().equals("2")){
                holder.orderTypeText.setText("待审核");
            }else if (order.getOrderState().equals("3")){
                holder.orderTypeText.setText("续保成功");
            }else if (order.getOrderState().equals("4")){
                holder.orderTypeText.setText("续保失败");
            }

        }else {//订单状态(orderType::1 2 3 4 ): 1 交易完成 2 待收货 3 待商家确认服务 4 作废 5 待发货 6 待车主确认服务 7 待评价 8 待支付

            if (order.getOrderStage().equals("1")){  //orderStage:订单二段状态 1 默认(不需要支付差价)  2 待车主支付差价 3 已支付差价 4 待车主支付运费 5 已支付运费
                if (order.getOrderState().equals("1")) {
                    holder.orderTypeText.setText("交易完成");
                }else if (order.getOrderState().equals("2")){
                    holder.orderTypeText.setText("待收货");
                }else if (order.getOrderState().equals("3")){
                    holder.orderTypeText.setText("待商家确认服务");
                }else if (order.getOrderState().equals("4")){
                    holder.orderTypeText.setText("订单已取消");
                }else if (order.getOrderState().equals("5")){
                    holder.orderPriceText.setVisibility(View.INVISIBLE);
                    holder.orderTypeText.setText("待发货");
                }else if (order.getOrderState().equals("6")){
                    holder.orderTypeText.setText("确认服务");
                }else if (order.getOrderState().equals("7")){
                    holder.orderTypeText.setText("待评价");
                }else if (order.getOrderState().equals("8")){
                    holder.orderTypeText.setText("待支付");
                }else if (order.getOrderState().equals("9")){
                    holder.orderTypeText.setText("退款中");
                }else if (order.getOrderState().equals("10")){
                    holder.orderTypeText.setText("退款成功");
                }else if (order.getOrderState().equals("11")){
                    holder.orderTypeText.setText("审核中");
                }else if (order.getOrderState().equals("12")){
                    holder.orderTypeText.setText("审核未通过");
                }else if (order.getOrderState().equals("13")){
                    holder.orderTypeText.setText("审核通过");
                }else if (order.getOrderState().equals("14")){
                    holder.orderTypeText.setText("拒绝服务");
                }else if (order.getOrderState().equals("15")){
                    holder.orderTypeText.setText("订单已取消");
                }else {
                    holder.orderTypeText.setText("其他状态");
                }
            }else if (order.getOrderStage().equals("2")){
                holder.orderTypeText.setText("前往支付差价");
            }else if (order.getOrderStage().equals("3")){
                holder.orderTypeText.setText("已支付差价");
            }else if (order.getOrderStage().equals("4")){
                holder.orderTypeText.setText("前往支付运费");
            }else if (order.getOrderStage().equals("5")){
                holder.orderTypeText.setText("已支付运费");
            }else {
                holder.orderTypeText.setText("其他状态");
            }

        }

        RxViewAction.clickNoDouble(holder.orderLayout)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onOrderItemClickListener(order.getOrderState(),order.getOrderType(),order.getOrderNo(),order.storeId,order.getOrderStage());
                    }
                });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView orderImageView;
        private final TextView orderTitleText;
        private final TextView orderNoText;
        private final TextView orderTimeText;
        private final TextView orderPriceText;
        private final TextView orderTypeText;
        private final LinearLayout orderLayout;

        ViewHolder(View itemView) {
            super(itemView);
            orderImageView = ((ImageView) itemView.findViewById(R.id.order_image));
            orderTitleText = ((TextView) itemView.findViewById(R.id.order_title_text));
            orderNoText = ((TextView) itemView.findViewById(R.id.order_no_text));
            orderTimeText = ((TextView) itemView.findViewById(R.id.order_time_text));
            orderPriceText = ((TextView) itemView.findViewById(R.id.order_price_text));
            orderTypeText = ((TextView) itemView.findViewById(R.id.order_type_button));
            orderLayout = ((LinearLayout) itemView.findViewById(R.id.order_layout));

        }
    }

    public interface OnOrderItemClick{
        void onOrderItemClickListener(String state,String orderType,String orderNo,String storeId,String orderStage);
    }
}