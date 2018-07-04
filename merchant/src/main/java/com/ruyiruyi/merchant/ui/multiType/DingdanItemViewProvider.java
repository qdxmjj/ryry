package com.ruyiruyi.merchant.ui.multiType;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ruyiruyi.merchant.R;
import com.ruyiruyi.merchant.db.DbConfig;
import com.ruyiruyi.merchant.ui.activity.OrderConfirmFirstChangeActivity;
import com.ruyiruyi.merchant.ui.activity.OrderConfirmFreeChangeActivity;
import com.ruyiruyi.merchant.ui.activity.OrderConfirmTireRepairActivity;
import com.ruyiruyi.merchant.ui.activity.PublicOrderInfoActivity;
import com.ruyiruyi.merchant.ui.multiType.modle.Dingdan;
import com.ruyiruyi.merchant.utils.UtilsRY;
import com.ruyiruyi.rylibrary.android.rx.rxbinding.RxViewAction;
import com.ruyiruyi.rylibrary.utils.glide.GlideCircleTransform;

import me.drakeet.multitype.ItemViewProvider;
import rx.functions.Action1;

public class DingdanItemViewProvider extends ItemViewProvider<Dingdan, DingdanItemViewProvider.ViewHolder> {
    private Context context;

    public DingdanItemViewProvider(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_rlv_dingdan, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final Dingdan dingdan) {

        holder.dingdan_tv_type.setText(dingdan.getOrderName());
        String timestampToStringAll = new UtilsRY().getTimestampToStringAll(dingdan.getOrderTime());
        holder.dingdan_tv_time.setText(timestampToStringAll);
        holder.dingdan_tv_carnum.setText(dingdan.getPlatNumber());
        // 0 空白   1 未读
        switch (dingdan.getIsRead()) {
            case "1":
                holder.dingdan_img_state.setVisibility(View.INVISIBLE);
                break;
            case "2":
                holder.dingdan_img_state.setImageResource(R.drawable.ic_weidu);
                holder.dingdan_img_state.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        if (dingdan.getOrderState().equals("4")) {
            if (dingdan.getOrderType().equals("2")) {
                Glide.with(context)
                        .load(R.drawable.ic_shouci)
                        .transform(new GlideCircleTransform(context))
                        .into(holder.dingdan_img);
            }
            if (dingdan.getOrderType().equals("3")) {
                Glide.with(context)
                        .load(R.drawable.ic_hui_free)
                        .transform(new GlideCircleTransform(context))
                        .into(holder.dingdan_img);
            }
            if (dingdan.getOrderType().equals("4")) {
                Glide.with(context)
                        .load(R.drawable.ic_hui_xiubu)
                        .transform(new GlideCircleTransform(context))
                        .into(holder.dingdan_img);
            }
        } else {
            Glide.with(context)
                    .load(dingdan.getOrderImage())
                    .transform(new GlideCircleTransform(context))
                    .into(holder.dingdan_img);
        }
        //根据orderType和orderState判断跳转不同订单详情
        RxViewAction.clickNoDouble(holder.rl_item).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                /*orderType:  1:普通商品购买订单 2:首次更换订单 3:免费再换订单 4:轮胎修补订单*/
                /*orderState:订单状态(): 1 交易完成 2 待收货 3 待商家确认服务 4 作废 5 待发货 6 待车主确认服务 7 待评价 8 待支付*/

                if (dingdan.getOrderType().equals("2") && dingdan.getOrderState().equals("3")) {//(2:首次更换订单  3 待商家确认服务  1 非补差)
                    Intent intent = new Intent(context, OrderConfirmFirstChangeActivity.class);
                    intent.putExtra("orderNo", dingdan.getOrderNo());
                    intent.putExtra("orderType", dingdan.getOrderType());
                    intent.putExtra("whereIn", "MainOrderItem");
                    context.startActivity(intent);
                } else if (dingdan.getOrderType().equals("3") && dingdan.getOrderState().equals("3") && (dingdan.getOrderStage().equals("1") || dingdan.getOrderStage().equals("3"))) {//(3:免费再换订单  3 待商家确认服务      OrderStage ：1 默认 2 补差 3 补差已支付)
                    Intent intent = new Intent(context, OrderConfirmFreeChangeActivity.class);
                    intent.putExtra("orderNo", dingdan.getOrderNo());
                    intent.putExtra("orderType", dingdan.getOrderType());
                    intent.putExtra("orderStage", dingdan.getOrderStage());
                    intent.putExtra("whereIn", "MainOrderItem");
                    context.startActivity(intent);
                } else if (dingdan.getOrderType().equals("4") && dingdan.getOrderState().equals("3")) {//(4:轮胎修补订单  3 待商家确认服务  1 默认非补差)
                    Intent intent = new Intent(context, OrderConfirmTireRepairActivity.class);
                    intent.putExtra("orderNo", dingdan.getOrderNo());
                    intent.putExtra("orderType", dingdan.getOrderType());
                    intent.putExtra("whereIn", "MainOrderItem");
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, PublicOrderInfoActivity.class);//其余各订单orderType各状态orderState均复用此页面(
                    Bundle bundle = new Bundle();
                    bundle.putString("orderNo", dingdan.getOrderNo());
                    bundle.putString("orderType", dingdan.getOrderType());
                    bundle.putString("orderState", dingdan.getOrderState());
                    bundle.putString("storeId", new DbConfig(context).getId() + "");
                    bundle.putString("whereIn", "MainOrderItem");
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }

            }
        });

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        private ImageView dingdan_img;
        @NonNull
        private TextView dingdan_tv_type;
        @NonNull
        private TextView dingdan_tv_time;
        @NonNull
        private TextView dingdan_tv_carnum;
        @NonNull
        private ImageView dingdan_img_state;
        @NonNull
        private RelativeLayout rl_item;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.dingdan_img = (ImageView) itemView.findViewById(R.id.dingdan_rlv_img);
            this.dingdan_tv_type = (TextView) itemView.findViewById(R.id.dingdan_tv_type);
            this.dingdan_tv_time = (TextView) itemView.findViewById(R.id.dingdan_tv_time);
            this.dingdan_tv_carnum = (TextView) itemView.findViewById(R.id.dingdan_tv_carnum);
            this.dingdan_img_state = (ImageView) itemView.findViewById(R.id.dingdan_img_state);
            this.rl_item = (RelativeLayout) itemView.findViewById(R.id.rl_item);
        }
    }
}