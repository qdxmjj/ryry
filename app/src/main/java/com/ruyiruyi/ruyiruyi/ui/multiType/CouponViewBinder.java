package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;

import me.drakeet.multitype.ItemViewProvider;

public class CouponViewBinder extends ItemViewProvider<Coupon, CouponViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_coupon, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Coupon coupon) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final LinearLayout couponLayout;
        private final FrameLayout couponColorLayout;
        private final TextView couponNameView;
        private final TextView couponTypeView;
        private final TextView couponBigNameView;
        private final TextView couponCarText;
        private final TextView couponStartTimeView;
        private final TextView couponEndTimwView;

        ViewHolder(View itemView) {
            super(itemView);
            couponLayout = ((LinearLayout) itemView.findViewById(R.id.coupon_layout));
            couponColorLayout = ((FrameLayout) itemView.findViewById(R.id.coupon_color_layotu));
            couponNameView = ((TextView) itemView.findViewById(R.id.coupon_name_view));
            couponTypeView = ((TextView) itemView.findViewById(R.id.coupon_type_view));
            couponBigNameView = ((TextView) itemView.findViewById(R.id.coupon_big_name_view));
            couponCarText = ((TextView) itemView.findViewById(R.id.coupon_car_number_view));
            couponStartTimeView = ((TextView) itemView.findViewById(R.id.coupon_start_time_view));
            couponEndTimwView = ((TextView) itemView.findViewById(R.id.coupon_end_time_view));


        }
    }
}