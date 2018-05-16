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

import me.drakeet.multitype.ItemViewProvider;

public class GoodsInfoViewBinder extends ItemViewProvider<GoodsInfo, GoodsInfoViewBinder.ViewHolder> {

    public Context context;

    public GoodsInfoViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_goods_info, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull GoodsInfo goodsInfo) {
        Glide.with(context).load(goodsInfo.getGoodsImage()).into(holder.goodsImage);
        holder.goodsName.setText(goodsInfo.getGoodsName());
        holder.goodsPrice.setText("￥"+goodsInfo.getGoodsPrice());
        holder.goodsCount.setText("×" + goodsInfo.getCurrentCount());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView goodsImage;
        private final TextView goodsName;
        private final TextView goodsPrice;
        private final TextView goodsCount;

        ViewHolder(View itemView) {
            super(itemView);
            goodsImage = ((ImageView) itemView.findViewById(R.id.goods_image));
            goodsName = ((TextView) itemView.findViewById(R.id.goods_name_text_oder));
            goodsPrice = ((TextView) itemView.findViewById(R.id.goods_price_text_order));
            goodsCount = ((TextView) itemView.findViewById(R.id.goods_count_order));

        }
    }
}