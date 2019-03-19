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
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.cell.DrawLineTextView;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;


/**
 * Created by Lenovo on 2019/1/11.
 */
public class ExchangeGoodsOrderViewBinder extends ItemViewProvider<ExchangeGoodsOrder, ExchangeGoodsOrderViewBinder.ViewHolder> {
    public Context context;
    public OnOrderClickiListener listener;

    public void setListener(OnOrderClickiListener listener) {
        this.listener = listener;
    }

    public ExchangeGoodsOrderViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_exchange_goods_order, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final ExchangeGoodsOrder exchangeGoodsOrder) {
        holder.orderTimeView.setText(exchangeGoodsOrder.getOrderTime());
        holder.order_no_view.setText("订单编号： " + exchangeGoodsOrder.getOrderNo());
        Glide.with(context).load(exchangeGoodsOrder.getGoodsImage()).into(holder.goods_image_view);
        holder.goods_name_view.setText(exchangeGoodsOrder.getGoodsName());
        holder.goods_price_view.setText("￥" + exchangeGoodsOrder.getGoodsPrice());
        holder.goods_integral_view.setText(exchangeGoodsOrder.getGoodsIntegral());
        if (exchangeGoodsOrder.getOrderType().equals("1")){     //1是商品
            holder.goods_desc_view.setVisibility(View.GONE);
            holder.get_wuliu_view.setVisibility(View.VISIBLE);
            if (exchangeGoodsOrder.getOrderState().equals("1")){ //1 交易完成  5 待发货 2 待收货
                holder.order_type_view.setText("交易完成");
                holder.get_wuliu_view.setVisibility(View.VISIBLE);
            }else if (exchangeGoodsOrder.getOrderState().equals("5")){
                holder.order_type_view.setText("待发货");
                holder.get_wuliu_view.setVisibility(View.GONE);
            }else if (exchangeGoodsOrder.getOrderState().equals("2")){
                holder.order_type_view.setText("待收货");
                holder.get_wuliu_view.setVisibility(View.VISIBLE);
            }
        }else { //优惠券
            holder.goods_desc_view.setVisibility(View.VISIBLE);
            holder.get_wuliu_view.setVisibility(View.GONE);
            holder.order_type_view.setText("交易完成");
        }
        RxViewAction.clickNoDouble(holder.get_wuliu_view)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        listener.onWuliuClickListener(exchangeGoodsOrder.getGoodsName(),exchangeGoodsOrder.goodsImage,exchangeGoodsOrder.orderNo,exchangeGoodsOrder.orderReceivingAddressId);
                    }
                });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView orderTimeView;
        private final TextView order_type_view;
        private final TextView get_wuliu_view;
        private final TextView goods_integral_view;
        private final DrawLineTextView goods_price_view;
        private final TextView goods_desc_view;
        private final TextView goods_name_view;
        private final TextView order_no_view;
        private final ImageView goods_image_view;

        ViewHolder(View itemView) {
            super(itemView);
            orderTimeView = ((TextView) itemView.findViewById(R.id.order_time_view));
            order_type_view = ((TextView) itemView.findViewById(R.id.order_type_view));
            get_wuliu_view = ((TextView) itemView.findViewById(R.id.get_wuliu_view));
            goods_integral_view = ((TextView) itemView.findViewById(R.id.goods_integral_view));
            goods_price_view = ((DrawLineTextView) itemView.findViewById(R.id.goods_price_view));
            goods_desc_view = ((TextView) itemView.findViewById(R.id.goods_desc_view));
            goods_name_view = ((TextView) itemView.findViewById(R.id.goods_name_view));
            order_no_view = ((TextView) itemView.findViewById(R.id.order_no_view));
            goods_image_view = ((ImageView) itemView.findViewById(R.id.goods_image_view));
        }
    }

    public interface OnOrderClickiListener{
        void onWuliuClickListener(String goodsName,String goodsImage,String orderNo,String addressId);
    }
}
