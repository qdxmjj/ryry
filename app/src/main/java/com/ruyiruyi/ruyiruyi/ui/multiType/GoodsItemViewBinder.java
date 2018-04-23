package com.ruyiruyi.ruyiruyi.ui.multiType;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruyiruyi.ruyiruyi.R;

import me.drakeet.multitype.ItemViewProvider;

public class GoodsItemViewBinder extends ItemViewProvider<GoodsItem, GoodsItemViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_goods_item, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull GoodsItem goodsItem) {
        holder.goodClassName.setText(goodsItem.getGoodClassName());
        holder.goodIsChoose.setImageResource(goodsItem.getChooseGood()?R.drawable.ic_xuanzhong : R.drawable.ic_weixuan);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView goodClassName;
        private final ImageView goodIsChoose;

        ViewHolder(View itemView) {
            super(itemView);
            goodClassName = ((TextView) itemView.findViewById(R.id.good_class_name_text));
            goodIsChoose = ((ImageView) itemView.findViewById(R.id.good_ischoose));
        }
    }
}