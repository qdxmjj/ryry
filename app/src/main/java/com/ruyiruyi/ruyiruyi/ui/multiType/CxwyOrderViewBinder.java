package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;

import me.drakeet.multitype.ItemViewProvider;

public class CxwyOrderViewBinder extends ItemViewProvider<CxwyOrder, CxwyOrderViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_cxwy_order, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull CxwyOrder cxwyOrder) {
        holder.cxwyPriceText.setText("￥" + cxwyOrder.cxwyPrice);
        holder.cxwyCountText.setText("×" + cxwyOrder.getCxwyCount());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView cxwyPriceText;
        private final TextView cxwyCountText;

        ViewHolder(View itemView) {
            super(itemView);
            cxwyPriceText = ((TextView) itemView.findViewById(R.id.cxwy_price_text));
            cxwyCountText = ((TextView) itemView.findViewById(R.id.cxwy_count_order_text));
        }
    }
}