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

public class GoodsHorizontalViewBinder extends ItemViewProvider<GoodsHorizontal, GoodsHorizontalViewBinder.ViewHolder> {
    public Context context;

    public GoodsHorizontalViewBinder(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_goods_horizontal, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull GoodsHorizontal goodsHorizontal) {
        Glide.with(context).load(goodsHorizontal.getGoodsImage()).into(holder.goodsImageView);
        holder.goodsNameText.setText(goodsHorizontal.getGoodsName());
        holder.goodsCountText.setText(goodsHorizontal.getCurrentCount()+"");
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView goodsNameText;
        private final TextView goodsCountText;
        private final ImageView goodsImageView;

        ViewHolder(View itemView) {
            super(itemView);
            goodsNameText = ((TextView) itemView.findViewById(R.id.goods_name_text));
            goodsCountText = ((TextView) itemView.findViewById(R.id.goods_count_text));
            goodsImageView = ((ImageView) itemView.findViewById(R.id.goods_image));
        }
    }
}