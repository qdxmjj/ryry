package com.ruyiruyi.merchant.ui.multiType;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    private ForFinishFg listener;
    private String typestatus;

    public void setListener(ForFinishFg listener) {
        this.listener = listener;
    }

    public OrderItemProvider(Context context, String typestatus) {
        this.context = context;
        this.typestatus = typestatus;
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
        if (orderItemBean.getOrderType().equals("1")) {
            holder.tv_orderprice.setText(orderItemBean.getOrderActuallyPrice());
            if (!orderItemBean.getOrderActuallyPrice().equals(orderItemBean.getPrice())) {
                holder.tv_quan.setVisibility(View.VISIBLE);
            } else {
                holder.tv_quan.setVisibility(View.GONE);
            }
        } else {
            holder.tv_orderprice.setText(orderItemBean.getPrice());
        }
        switch (orderItemBean.getStatus()) {//orderState:订单状态(): 1 交易完成 2 待收货 3 待商家确认服务 4 作废 5 待发货 6 待车主确认服务 7 待评价 8 待支付
            case "5":
                statusStr = "待发货";
                holder.tv_orderstatus.setText(statusStr);
                holder.tv_orderstatus.setBackgroundResource(R.drawable.login_code_button);
                break;
            case "2":
                statusStr = "确认收货";
                holder.tv_orderstatus.setText(statusStr);
                holder.tv_orderstatus.setBackgroundResource(R.drawable.login_code_button);
                break;
            case "3"://3 待商家确认服务
                statusStr = "确认服务";
                holder.tv_orderstatus.setText(statusStr);
                holder.tv_orderstatus.setBackgroundResource(R.drawable.login_code_button);
                break;
            case "6"://6 待车主确认服务
                statusStr = "待车主确认服务";
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
            case "14":
                statusStr = "已取消";
                holder.tv_orderstatus.setText(statusStr);
                holder.tv_orderstatus.setBackgroundResource(R.drawable.login_code_button);
                break;
            case "15":
                statusStr = "用户已取消";
                holder.tv_orderstatus.setText(statusStr);
                holder.tv_orderstatus.setBackgroundResource(R.drawable.login_code_button);
                break;
            case "9":
                statusStr = "退款中";
                holder.tv_orderstatus.setText(statusStr);
                holder.tv_orderstatus.setBackgroundResource(R.drawable.login_code_button);
                break;
            case "10":
                statusStr = "已退款";
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
                    if (typestatus.equals("all_0")) {
                        intent.putExtra("whereIn", "MyOrderItem");
                        intent.putExtra("select", "0");
                    }
                    if (typestatus.equals("all_1")) {
                        intent.putExtra("whereIn", "MyOrderItem");
                        intent.putExtra("select", "1");
                    }
                    if (typestatus.equals("all_2")) {
                        intent.putExtra("whereIn", "MyOrderItem");
                        intent.putExtra("select", "2");
                    }
                    if (typestatus.equals("all_3")) {
                        intent.putExtra("whereIn", "MyOrderItem");
                        intent.putExtra("select", "3");
                    }
                    if (typestatus.equals("pingtai_0")) {
                        intent.putExtra("whereIn", "MainOrderTop");
                        intent.putExtra("select", "0");
                    }
                    if (typestatus.equals("pingtai_1")) {
                        intent.putExtra("whereIn", "MainOrderTop");
                        intent.putExtra("select", "1");
                    }
                    context.startActivity(intent);
                    listener.forFinishFgListener();
                } else if (orderItemBean.getOrderType().equals("3") && orderItemBean.getStatus().equals("3") && (orderItemBean.getOrderStage().equals("1") || orderItemBean.getOrderStage().equals("3"))) {//(3:免费再换订单  3 待商家确认服务  1 非补差)
                    Intent intent = new Intent(context, OrderConfirmFreeChangeActivity.class);
                    intent.putExtra("orderNo", orderItemBean.getBianhao());
                    intent.putExtra("orderType", orderItemBean.getOrderType());
                    intent.putExtra("orderStage", orderItemBean.getOrderStage());
                    if (typestatus.equals("all_0")) {
                        intent.putExtra("whereIn", "MyOrderItem");
                        intent.putExtra("select", "0");
                    }
                    if (typestatus.equals("all_1")) {
                        intent.putExtra("whereIn", "MyOrderItem");
                        intent.putExtra("select", "1");
                    }
                    if (typestatus.equals("all_2")) {
                        intent.putExtra("whereIn", "MyOrderItem");
                        intent.putExtra("select", "2");
                    }
                    if (typestatus.equals("all_3")) {
                        intent.putExtra("whereIn", "MyOrderItem");
                        intent.putExtra("select", "3");
                    }
                    if (typestatus.equals("pingtai_0")) {
                        intent.putExtra("whereIn", "MainOrderTop");
                        intent.putExtra("select", "0");
                    }
                    if (typestatus.equals("pingtai_1")) {
                        intent.putExtra("whereIn", "MainOrderTop");
                        intent.putExtra("select", "1");
                    }
                    context.startActivity(intent);
                    listener.forFinishFgListener();
                } else if (orderItemBean.getOrderType().equals("4") && orderItemBean.getStatus().equals("3")) {//(4:轮胎修补订单  3 待商家确认服务  1 非补差)
                    Intent intent = new Intent(context, OrderConfirmTireRepairActivity.class);
                    intent.putExtra("orderNo", orderItemBean.getBianhao());
                    intent.putExtra("orderType", orderItemBean.getOrderType());
                    if (typestatus.equals("all_0")) {
                        intent.putExtra("whereIn", "MyOrderItem");
                        intent.putExtra("select", "0");
                    }
                    if (typestatus.equals("all_1")) {
                        intent.putExtra("whereIn", "MyOrderItem");
                        intent.putExtra("select", "1");
                    }
                    if (typestatus.equals("all_2")) {
                        intent.putExtra("whereIn", "MyOrderItem");
                        intent.putExtra("select", "2");
                    }
                    if (typestatus.equals("all_3")) {
                        intent.putExtra("whereIn", "MyOrderItem");
                        intent.putExtra("select", "3");
                    }
                    if (typestatus.equals("pingtai_0")) {
                        intent.putExtra("whereIn", "MainOrderTop");
                        intent.putExtra("select", "0");
                    }
                    if (typestatus.equals("pingtai_1")) {
                        intent.putExtra("whereIn", "MainOrderTop");
                        intent.putExtra("select", "1");
                    }
                    context.startActivity(intent);
                    listener.forFinishFgListener();
                } else {
                    Intent intent = new Intent(context, PublicOrderInfoActivity.class);//其余各订单orderType各状态orderState均复用此页面(
                    Bundle bundle = new Bundle();
                    bundle.putString("orderNo", orderItemBean.getBianhao());
                    bundle.putString("orderType", orderItemBean.getOrderType());
                    bundle.putString("orderState", orderItemBean.getStatus());
                    bundle.putString("storeId", new DbConfig(context).getId() + "");
                    if (typestatus.equals("all_0")) {
                        bundle.putString("whereIn", "MyOrderItem");
                        bundle.putString("select", "0");
                    }
                    if (typestatus.equals("all_1")) {
                        bundle.putString("whereIn", "MyOrderItem");
                        bundle.putString("select", "1");
                    }
                    if (typestatus.equals("all_2")) {
                        bundle.putString("whereIn", "MyOrderItem");
                        bundle.putString("select", "2");
                    }
                    if (typestatus.equals("all_3")) {
                        bundle.putString("whereIn", "MyOrderItem");
                        bundle.putString("select", "3");
                    }
                    if (typestatus.equals("pingtai_0")) {
                        bundle.putString("whereIn", "MainOrderTop");
                        bundle.putString("select", "0");
                    }
                    if (typestatus.equals("pingtai_1")) {
                        bundle.putString("whereIn", "MainOrderTop");
                        bundle.putString("select", "1");
                    }
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    listener.forFinishFgListener();
                }
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_ordertitle;
        private final TextView tv_orderbianhao;
        private final TextView tv_orderstatus;
        private final TextView tv_quan;
        private final TextView tv_orderprice;
        private final ImageView img_order;
        private final FrameLayout order_item_fl;

        ViewHolder(View itemView) {
            super(itemView);
            tv_ordertitle = ((TextView) itemView.findViewById(R.id.tv_ordertitle));
            tv_orderbianhao = ((TextView) itemView.findViewById(R.id.tv_orderbianhao));
            tv_orderprice = ((TextView) itemView.findViewById(R.id.tv_orderprice));
            tv_orderstatus = ((TextView) itemView.findViewById(R.id.tv_orderstatus));
            tv_quan = ((TextView) itemView.findViewById(R.id.tv_quan));
            img_order = ((ImageView) itemView.findViewById(R.id.img_order));
            order_item_fl = ((FrameLayout) itemView.findViewById(R.id.order_item_fl));
        }
    }

    /*
    * 跳转后让MyorderFragment  finish 接口
    * */
    public interface ForFinishFg {
        void forFinishFgListener();// 跳转后通知MyorderFragment  finish

    }

}