package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;

import me.drakeet.multitype.ItemViewProvider;

public class PromotionHaspersonViewBinder extends ItemViewProvider<PromotionHasperson, PromotionHaspersonViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.promotion_hasperson_binder, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final PromotionHasperson promotionHasperson) {
        if (promotionHasperson.isFirst()) {
            holder.ll_top.setVisibility(View.VISIBLE);
        } else {
            holder.ll_top.setVisibility(View.GONE);
        }
        holder.user.setText(promotionHasperson.getUserPhone());
        holder.state.setText(promotionHasperson.getUserState());
        holder.time.setText(promotionHasperson.getUserTime());

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView user;
        private final TextView state;
        private final TextView time;
        private final LinearLayout ll_top;

        ViewHolder(View itemView) {
            super(itemView);
            user = ((TextView) itemView.findViewById(R.id.user));
            state = ((TextView) itemView.findViewById(R.id.state));
            time = ((TextView) itemView.findViewById(R.id.time));
            ll_top = ((LinearLayout) itemView.findViewById(R.id.ll_top));
        }
    }

}