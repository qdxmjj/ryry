package com.ruyiruyi.merchant.ui.multiType;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.ruyiruyi.merchant.ui.activity.ShipmentInfoActivity;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

/**
 * @作者 倾春
 * eml.dongbinjava@163.com
 * @创建日期 2018/9/3 15:33
 */

public class OrderForShipmentViewBinder extends ItemViewProvider<OrderForShipment, OrderForShipmentViewBinder.ViewHolder> {
    private Context mContext;

    public OrderForShipmentViewBinder(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.orderforshipmentbinder, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final OrderForShipment orderForShipment) {
        holder.ordernumber.setText(orderForShipment.getOrderNo());
        String timestampToStringAll = new UtilsRY().getTimestampToStringAll(orderForShipment.getOrderTime());
        holder.ordertime.setText(timestampToStringAll);

        if ((!orderForShipment.isShoeConsistent()) && (orderForShipment.getShoeNum_rear() > 0) && (orderForShipment.getShoeNum_front() > 0)) { //如果前后不一致且前后轮个数都大于0 则全部展示
            holder.fl_front.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(orderForShipment.getOrderImg_front()).into(holder.shoeimg_front);
            holder.shoetitle_front.setText(orderForShipment.getShoeTitle_front());
            holder.shoeprice_front.setText("¥ " + orderForShipment.getShoePrice_front());

            holder.fl_rear.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(orderForShipment.getOrderImg_rear()).into(holder.shoeimg_rear);
            holder.shoetitle_rear.setText(orderForShipment.getShoeTitle_rear());
            holder.shoeprice_rear.setText("¥ " + orderForShipment.getShoePrice_rear());
        } else //不需要全部展示的情况 分三种
//            holder.fl_rear.setVisibility(View.GONE);//00
            //1.前后轮一致
            if (orderForShipment.isShoeConsistent()) {
                holder.fl_rear.setVisibility(View.GONE);//00
                holder.fl_front.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(orderForShipment.getOrderImg_front()).into(holder.shoeimg_front);
                holder.shoetitle_front.setText(orderForShipment.getShoeTitle_front());
                holder.shoeprice_front.setText("¥ " + orderForShipment.getShoePrice_front());
            }
            //2.不一致 且只有前轮
            else if ((!orderForShipment.isShoeConsistent()) && (orderForShipment.getShoeNum_rear() == 0) && (orderForShipment.getShoeNum_front() > 0)) {
                holder.fl_rear.setVisibility(View.GONE);//00
                holder.fl_front.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(orderForShipment.getOrderImg_front()).into(holder.shoeimg_front);
                holder.shoetitle_front.setText(orderForShipment.getShoeTitle_front());
                holder.shoeprice_front.setText("¥ " + orderForShipment.getShoePrice_front());
            }
            //3.不一致 且只有后轮
            else if ((!orderForShipment.isShoeConsistent()) && (orderForShipment.getShoeNum_rear() > 0) && (orderForShipment.getShoeNum_front() == 0)) {
                holder.fl_front.setVisibility(View.GONE);
                holder.fl_rear.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(orderForShipment.getOrderImg_rear()).into(holder.shoeimg_rear);
                holder.shoetitle_rear.setText(orderForShipment.getShoeTitle_rear());
                holder.shoeprice_rear.setText("¥ " + orderForShipment.getShoePrice_rear());
            } else { //不存在的情况 防错乱
                holder.fl_front.setVisibility(View.GONE);//00
                holder.fl_rear.setVisibility(View.GONE);
            }


        RxViewAction.clickNoDouble(holder.shipment_front).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //跳转发货
                Intent intent = new Intent(mContext, ShipmentInfoActivity.class);
                intent.putExtra("orderNo", orderForShipment.getOrderNo());
                intent.putExtra("platNumber", orderForShipment.getCarNumber());
                intent.putExtra("orderTime", orderForShipment.getOrderTime());
                intent.putExtra("userPhone", orderForShipment.getUserPhone());
                intent.putExtra("isConsistent", orderForShipment.isShoeConsistent());
                intent.putExtra("shoeTitle_front", orderForShipment.getShoeTitle_front());
                intent.putExtra("shoeTitle_rear", orderForShipment.getShoeTitle_rear());
                intent.putExtra("shoeNum_front", orderForShipment.getShoeNum_front());
                intent.putExtra("shoeNum_rear", orderForShipment.getShoeNum_rear());

                intent.putExtra("tyreId_front", orderForShipment.getTyreId_front());
                intent.putExtra("tyreId_rear", orderForShipment.getTyreId_rear());
                intent.putExtra("orderImg_front", orderForShipment.getOrderImg_front());
                intent.putExtra("orderImg_rear", orderForShipment.getOrderImg_rear());
                intent.putExtra("fontRearFlag_front", orderForShipment.getFontRearFlag_front());
                intent.putExtra("fontRearFlag_rear", orderForShipment.getFontRearFlag_rear());
                intent.putExtra("shoePrice_front", orderForShipment.getShoePrice_front());
                intent.putExtra("shoePrice_rear", orderForShipment.getShoePrice_rear());
                intent.putExtra("orderId", orderForShipment.getOrderId());
                mContext.startActivity(intent);
            }
        });
        RxViewAction.clickNoDouble(holder.shipment_rear).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //跳转发货
                Intent intent = new Intent(mContext, ShipmentInfoActivity.class);
                intent.putExtra("orderNo", orderForShipment.getOrderNo());
                intent.putExtra("platNumber", orderForShipment.getCarNumber());
                intent.putExtra("orderTime", orderForShipment.getOrderTime());
                intent.putExtra("userPhone", orderForShipment.getUserPhone());
                intent.putExtra("isConsistent", orderForShipment.isShoeConsistent());
                intent.putExtra("shoeTitle_front", orderForShipment.getShoeTitle_front());
                intent.putExtra("shoeTitle_rear", orderForShipment.getShoeTitle_rear());
                intent.putExtra("shoeNum_front", orderForShipment.getShoeNum_front());
                intent.putExtra("shoeNum_rear", orderForShipment.getShoeNum_rear());

                intent.putExtra("tyreId_front", orderForShipment.getTyreId_front());
                intent.putExtra("tyreId_rear", orderForShipment.getTyreId_rear());
                intent.putExtra("orderImg_front", orderForShipment.getOrderImg_front());
                intent.putExtra("orderImg_rear", orderForShipment.getOrderImg_rear());
                intent.putExtra("fontRearFlag_front", orderForShipment.getFontRearFlag_front());
                intent.putExtra("fontRearFlag_rear", orderForShipment.getFontRearFlag_rear());
                intent.putExtra("shoePrice_front", orderForShipment.getShoePrice_front());
                intent.putExtra("shoePrice_rear", orderForShipment.getShoePrice_rear());
                intent.putExtra("orderId", orderForShipment.getOrderId());
                mContext.startActivity(intent);
            }
        });

        holder.carnum.setText(orderForShipment.getCarNumber());
        holder.userphone.setText(orderForShipment.getUserPhone());
        RxViewAction.clickNoDouble(holder.call).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //跳转拨打电话页面
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + orderForShipment.getUserPhone());
                intent.setData(data);
                mContext.startActivity(intent);
            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView ordernumber;
        private final TextView ordertime;

        private final FrameLayout fl_front;
        private final ImageView shoeimg_front;
        private final TextView shoetitle_front;
        private final TextView shoeprice_front;
        private final TextView shipment_front;

        private final FrameLayout fl_rear;
        private final ImageView shoeimg_rear;
        private final TextView shoetitle_rear;
        private final TextView shoeprice_rear;
        private final TextView shipment_rear;

        private final TextView carnum;
        private final TextView userphone;
        private final ImageView call;


        ViewHolder(View itemView) {
            super(itemView);
            ordernumber = ((TextView) itemView.findViewById(R.id.ordernumber));
            ordertime = ((TextView) itemView.findViewById(R.id.ordertime));
            fl_front = ((FrameLayout) itemView.findViewById(R.id.fl_front));
            shoeimg_front = ((ImageView) itemView.findViewById(R.id.shoeimg_front));
            shoetitle_front = ((TextView) itemView.findViewById(R.id.shoetitle_front));
            shoeprice_front = ((TextView) itemView.findViewById(R.id.shoeprice_front));
            shipment_front = ((TextView) itemView.findViewById(R.id.shipment_front));
            fl_rear = ((FrameLayout) itemView.findViewById(R.id.fl_rear));
            shoeimg_rear = ((ImageView) itemView.findViewById(R.id.shoeimg_rear));
            shoetitle_rear = ((TextView) itemView.findViewById(R.id.shoetitle_rear));
            shoeprice_rear = ((TextView) itemView.findViewById(R.id.shoeprice_rear));
            shipment_rear = ((TextView) itemView.findViewById(R.id.shipment_rear));
            carnum = ((TextView) itemView.findViewById(R.id.carnum));
            userphone = ((TextView) itemView.findViewById(R.id.userphone));
            call = ((ImageView) itemView.findViewById(R.id.call));
        }
    }
}