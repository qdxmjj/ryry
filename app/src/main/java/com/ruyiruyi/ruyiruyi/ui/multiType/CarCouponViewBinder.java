package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by Lenovo on 2018/9/13.
 */
public class CarCouponViewBinder extends ItemViewProvider<CarCoupon, CarCouponViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_car_coupon, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull CarCoupon carCoupon) {
        holder.couponCountText.setText(carCoupon.getCouponName() + " *" + carCoupon.getCouponCount());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView couponCountText;

        ViewHolder(View itemView) {
            super(itemView);
            couponCountText = ((TextView) itemView.findViewById(R.id.coupon_text_count));
        }
    }
}
