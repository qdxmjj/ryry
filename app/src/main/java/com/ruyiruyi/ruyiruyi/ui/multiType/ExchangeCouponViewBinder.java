package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruyiruyi.ruyiruyi.R;

import me.drakeet.multitype.ItemViewProvider;

/**
 * Created by Lenovo on 2018/12/25.
 */
public class ExchangeCouponViewBinder extends ItemViewProvider<ExchangeCoupon, ExchangeCouponViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_exchange_coupon, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull ExchangeCoupon exchangeCoupon) {

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
