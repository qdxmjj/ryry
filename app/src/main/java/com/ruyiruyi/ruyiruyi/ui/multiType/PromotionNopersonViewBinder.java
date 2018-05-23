package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;

import me.drakeet.multitype.ItemViewProvider;

public class PromotionNopersonViewBinder extends ItemViewProvider<PromotionNoperson, PromotionNopersonViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.promotion_noperson_binder, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final PromotionNoperson promotionNoperson) {

        holder.tv_txt.setText(promotionNoperson.getTxt());

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_txt;

        ViewHolder(View itemView) {
            super(itemView);
            tv_txt = ((TextView) itemView.findViewById(R.id.tv_txt));
        }
    }

}