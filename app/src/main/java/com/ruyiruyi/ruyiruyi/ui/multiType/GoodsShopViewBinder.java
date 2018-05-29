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

public class GoodsShopViewBinder extends ItemViewProvider<GoodsShop, GoodsShopViewBinder.ViewHolder> {

    public Context context;

    public GoodsShopViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_goods_shop, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull GoodsShop goodsShop) {
        Glide.with(context).load(goodsShop.getGoodsImage()).into(holder.goodsImageView);
        holder.goodsNameText.setText(goodsShop.getGoodsName());
        holder.gooidsPriceText.setText("￥" + goodsShop.getGoodsPrice());
        holder.goodsDistanceText.setText(goodsShop.getGoodsDistance() + "km");
        holder.goodsAddressText.setText(goodsShop.getGoodsAddress());

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView goodsImageView;
        private final TextView goodsNameText;
        private final TextView gooidsPriceText;
        private final TextView goodsAddressText;
        private final TextView goodsDistanceText;

        ViewHolder(View itemView) {
            super(itemView);
            goodsImageView = ((ImageView) itemView.findViewById(R.id.goods_image_view));
            goodsNameText = ((TextView) itemView.findViewById(R.id.goods_name_text));
            gooidsPriceText = ((TextView) itemView.findViewById(R.id.goods_price_text));
            goodsAddressText = ((TextView) itemView.findViewById(R.id.goods_address_text));
            goodsDistanceText = ((TextView) itemView.findViewById(R.id.goods_distence_text));
        }
    }
}